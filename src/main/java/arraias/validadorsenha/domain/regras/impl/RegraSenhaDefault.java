package arraias.validadorsenha.domain.regras.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import arraias.validadorsenha.domain.regras.RegraSenha;

@Component("regraDefault")
public class RegraSenhaDefault implements RegraSenha {
	
	private static final int TAMANHO_MINIMO = 9;
	private static final int TAMANHO_MAXIMO = 74;
	
	private List<String> regrasCaracteres;
	
	public RegraSenhaDefault() {
		regrasCaracteres = new ArrayList<>();
		regrasCaracteres.add("\\d");
		regrasCaracteres.add("[a-z]");
		regrasCaracteres.add("[A-Z]");
		regrasCaracteres.add("[!@#$%^&*()\\-+]");
	}
	
	@Override
	public boolean validar(String senha) {
		
		if (validarPossuiTamanhoCorreto(senha) &&
				validarTodosCaracteresValidos(senha) &&
				validarTodosCaracteresDistintos(senha)) {
			return true;
		}

		return false;
	}
	
	private boolean validarPossuiTamanhoCorreto(String senha) {
		
		if (senha == null) {
			return false;
		}
		
		int tamanhoSenha = senha.length();
		
		if (tamanhoSenha < TAMANHO_MINIMO || tamanhoSenha > TAMANHO_MAXIMO) {
			return false;
		}
		
		return true;
	}

	private boolean validarTodosCaracteresValidos(String senha) {
		
		String senhaLimpa = senha;
		
		for (String regra : regrasCaracteres) {
			
			String replaced = senhaLimpa.replaceAll(regra, "");
			
			if (replaced.equals(senhaLimpa)) {
				return false; // Nao possui caracteres minimos obrigatorios
			}
			
			senhaLimpa = replaced;
			
		}
		
		return senhaLimpa.isEmpty();
	}
	
	private boolean validarTodosCaracteresDistintos(String senha) {

		Set<Character> chars = new HashSet<>();

		for (char c : senha.toCharArray()) {
			if (!chars.add(c)) {
				return false;
			}
		}

		return true;
	}
}
