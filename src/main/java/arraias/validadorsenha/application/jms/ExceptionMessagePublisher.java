package arraias.validadorsenha.application.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import arraias.validadorsenha.application.config.ExceptionPublisherConfig;

@Component
public class ExceptionMessagePublisher {
	
	@Autowired
	private ExceptionPublisherConfig config;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void publicar(String mensagemErro) {
		
		//
		// Aqui deveria conter uma implementacao para publicacao em uma fila de
		// envio de email, que nao faz parte desse escopo. Por questao didatica
		// sera apenas impresso no log a fila e a mensagem.
		//
		
		logger.info("Mensagem enviada para a fila {}: \n{}", config.getQueue(), mensagemErro);
		
	}
}
