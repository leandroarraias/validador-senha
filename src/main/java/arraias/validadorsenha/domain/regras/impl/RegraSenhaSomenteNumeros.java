package arraias.validadorsenha.domain.regras.impl;

import java.math.BigInteger;

import org.springframework.stereotype.Component;

import arraias.validadorsenha.domain.regras.RegraSenha;

@Component("regraSomenteNumeros")
public class RegraSenhaSomenteNumeros implements RegraSenha {
	
	private static final int TAMANHO_MAXIMO = 50;
	
	@Override
	public boolean validar(String senha) {
		
		if (senha == null || senha.length() > TAMANHO_MAXIMO) {
			return false;
		}
		
		try {
			new BigInteger(senha);
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}
}
