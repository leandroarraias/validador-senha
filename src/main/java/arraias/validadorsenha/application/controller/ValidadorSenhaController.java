package arraias.validadorsenha.application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import arraias.validadorsenha.domain.ValidadorSenhaService;
import arraias.validadorsenha.provider.api.ValidarsenhaApi;
import arraias.validadorsenha.provider.model.SenhaRepresentation;
import arraias.validadorsenha.provider.model.SenhaValidadaRepresentation;

@RestController
public class ValidadorSenhaController implements ValidarsenhaApi {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ValidadorSenhaService service;
	
	@Override
	public ResponseEntity<SenhaValidadaRepresentation> validarsenhaPost(SenhaRepresentation senha) {
		
		logger.info("Recebendo requisicao para validacao de senha.");
		
		boolean senhaValida = service.validar(senha.getSenha());
		
		SenhaValidadaRepresentation response = new SenhaValidadaRepresentation();
		response.setSenhavalida(senhaValida);
		
		ResponseEntity<SenhaValidadaRepresentation> entity =
				new ResponseEntity<>(response, HttpStatus.OK);
		
		logger.info("Validacao de senha processada com sucesso.");
		
		return entity;
	}
}
