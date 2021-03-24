package arraias.validadorsenha.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "regrasenha")
public class RegraSenhaConfig {
	
	private String regraSelecionada;

	public String getRegraSelecionada() {
		return regraSelecionada;
	}

	public void setRegraSelecionada(String regraSelecionada) {
		this.regraSelecionada = regraSelecionada;
	}
	
}
