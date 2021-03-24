package arraias.validadorsenha.application.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import arraias.validadorsenha.application.jms.ExceptionMessagePublisher;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private ExceptionMessagePublisher publisher;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request) {
		
		String paramName = ex.getParameter().getParameterName();
		
		return handleExceptionInternal(
				ex,
				String.format("Parametro \"%s\" invalido", paramName),
				new HttpHeaders(),
				BAD_REQUEST,
				request);
	}
	
	@ExceptionHandler(value = { Exception.class})
	public ResponseEntity<Object> tratarExcecaoGenerica(
			Exception ex,
			WebRequest request) {
		
		MensagemErro mensagem = new MensagemErro(
				UUID.randomUUID().toString(),
				"Erro ao processar requisicao.");
		
		logger.error(mensagem.toString(), ex);
		publicarErro(mensagem.toString(), ex);
		
		return handleExceptionInternal(
				ex,
				mensagem,
				new HttpHeaders(),
				INTERNAL_SERVER_ERROR,
				request);
	}
	
	private void publicarErro(String mensagem, Exception ex) {
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		
		publisher.publicar("".concat(mensagem).concat("\n").concat(sw.toString()));
	}
}
