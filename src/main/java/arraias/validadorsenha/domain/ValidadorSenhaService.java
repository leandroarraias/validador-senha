package arraias.validadorsenha.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import arraias.validadorsenha.domain.regras.RegraSenha;

@Service
public class ValidadorSenhaService {
	
	private RegraSenha regra;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	public ValidadorSenhaService(RegraSenha regra) {
		this.regra = regra;
	}

	public boolean validar(String senha) {
		
		logger.info("Validando senha.");
		
		boolean senhaValida = regra.validar(senha);
		
		logger.info("Senha valida? {}", senhaValida);
		
		return senhaValida;
		
	}
}
