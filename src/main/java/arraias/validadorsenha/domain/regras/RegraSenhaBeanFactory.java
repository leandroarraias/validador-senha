package arraias.validadorsenha.domain.regras;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import arraias.validadorsenha.application.config.RegraSenhaConfig;
import arraias.validadorsenha.domain.regras.impl.RegraSenhaDefault;

@Configuration
public class RegraSenhaBeanFactory {
	
	private BeanFactory beanFactory;
	private RegraSenhaConfig config;
	
	@Autowired
    public RegraSenhaBeanFactory(BeanFactory beanFactory, RegraSenhaConfig config) {
        this.beanFactory = beanFactory;
        this.config = config;
    }
	
	@Bean
	@Primary
	public RegraSenha regraSenha() {
		
		String regraSelecionada = config.getRegraSelecionada();
		
		if (regraSelecionada != null && beanFactory.containsBean(regraSelecionada)) {
			return (RegraSenha) beanFactory.getBean(regraSelecionada);
		}
		
		return new RegraSenhaDefault();
	}
}
