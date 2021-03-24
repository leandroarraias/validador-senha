#Objetivo do projeto
O objetivo deste projeto é demonstrar a estrutura básica de uma aplicação WEB, utilizando Spring Boot.

#Funcionalidade do sistema
O sistema terá um único serviço exposto ao cliente, que consiste na validação de senhas.

A senha é informada para o sistema via requisição HTTP/REST, e é retornado um valor booleano: <b>true</b> se válida ou <b>false</b> se inválida.

Caso ocorra algum erro de processamento da requisição, será retornado ao cliente uma mensagem amigável informando o problema e um código <b>uuid</b> de referência ao erro. Os detalhes técnicos do erro serão impressos no log da aplicação, bem como enviados para uma fila em um Message Broker para alerta ao time de sustentação (para efeito de demonstração nesse projeto, a API de publicação na fila do Message Broker apenas imprimirá no log os dados que seriam enviados ao mesmo).

#Regra padrão de validação das senhas
O sistema poderá conter diversas regras de validação, conforme será exibido adiante. Porém, a regra default será:

- De 9 a até 74 caracteres;
- Ao menos 1 dígito
- Ao menos 1 letra minúscula
- Ao menos 1 letra maiúscula
- Ao menos 1 caractere especial. Serão aceitos os seguintes caracteres: !@#$%^&*()-+
- Não possuir caracteres repetidos dentro do conjunto

Espaços em branco, letras com acento incluindo "ç" por exemplo, são considerados inválidos.

#Execução do sistema
1 - Certifique-se de que a versão corrente do Java instalado seja a 11. Para isso, execute o comando <code>java -version</code>. O resultado, deverá ser semelhante ao exibido abaixo:

```
D:\workspace\validador-senha>java -version
java version "11.0.10" 2021-01-19 LTS
Java(TM) SE Runtime Environment 18.9 (build 11.0.10+8-LTS-162)
Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.10+8-LTS-162, mixed mode)
```

2 - Para executar a aplicação via prompt de comando, navegue até a pasta raiz do projeto (onde está o arquivo pom.xml) e execute o comando <code>mvnw spring-boot:run</code>. O Spring Boot dará start no projeto e ao fim da inicialização serão informados a porta em que a aplicação está sendo executada e que a aplicação foi inicializada com sucesso:

```
2021-03-23 15:48:05.557  INFO 9732 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2021-03-23 15:48:05.558  INFO 9732 --- [main] d.s.w.p.DocumentationPluginsBootstrapper : Context refreshed
2021-03-23 15:48:05.572  INFO 9732 --- [main] d.s.w.p.DocumentationPluginsBootstrapper : Found 1 custom documentation plugin(s)
2021-03-23 15:48:05.595  INFO 9732 --- [main] s.d.s.w.s.ApiListingReferenceScanner     : Scanning for api listing references
2021-03-23 15:48:05.686  INFO 9732 --- [main] a.v.ValidadorSenhaApplication            : Started ValidadorSenhaApplication in 2.199 seconds (JVM running for 2.542)
```
Caso deseje cancelar a execução, basta pressionar <code>CTRL + C</code> e confirmar.

3 - Para importar o projeto para a sua IDE de preferência, é recomendada a compilação do projeto, pois as APIs utilizadas pela camada de controller são geradas em tempo de compilação. Para isso, basta executar o build pelo comando da sua IDE ou, via prompt de comando, navegue para a pasta raiz do projeto e execute o comando <code>mvn clean install</code>.

4 - Para executar a API, você poderá utilizar a página Swagger do sistema onde está descrito o serviço exposto:

```
http://localhost:8080/swagger-ui.html#
```

Abaixo, seguem exemplos da request e response do serviço:

Request:

```
POST /validarsenha HTTP/1.1
Host: localhost:8080
Content-Type: application/json;charset=utf-8

{
    "senha":"ABCabc@1234"
}
```
Response:

```
{
  "senhavalida" : true
}
```

#Estruturação do sistema
A estruturação do projeto se baseou no conceito de modelagem hexagonal. Podemos dividir o projeto em duas packages principais:
<u><h1>arraias.validadorsenha.domain</h1></u>
Esta package concentra a regras de negócio. Nela encontramos a classe de serviço executada pelo RestController bem como as classes que representam as regras disponíveis para validação de senhas.

<b>subpackage regras: </b>Possui as regras de validação de senhas. Cada regra deverá ser implementada em componentes distintos e depois declarando o uso no arquivo application.yaml. A declaração e configuração serão exemplificadas adiante.

<u><h1>arraias.validadorsenha.application</h1></u>

Nesta package se encontram as classes relacionadas a configuração do sistema, tarefas auxiliares como tratamento de erros bem como as APIs que se integram à classe de serviços, como por exemplo o RestController e o JMS Publisher. Caso a aplicação utilizasse banco de dados, nessa package estariam declaradas as queries e implementações para acesso ao database.

<b>subpackage config: </b>Possui classes relacionadas a configuração do sistema, como por exemplo configuração do swagger e loads do arquivo application.yaml.
<br><br>
<b>subpackage controller: </b>Contem as APIs que expoem as regras de negócio para o ambiente externo.
<br><br>
<b>subpackage exception: </b>Realiza o tratamento centralizado das exceções lançadas no sistema.
<br><br>
<b>subpackage jms: </b>Contém as APIs de integração com o Message Broker (neste sistema, a funcionalidade de conexão e envio de mensagens para o Message Broker é apenas simulada através da impressão de logs).

#Customizações
<h1>Regras de validação de senhas</h1>

O sistema permite a inclusão de novas regras de negócio e selecionar a regra a ser utilizada via arquivo application.yaml. Para isso, deve-se seguir os seguintes passos:

1 - Criar uma nova classe concreta na subpackage <b>regras.impl</b>. Essa classe deverá implementar a interface <b>RegraSenha</b> e receber um nome único via annotation <b>@Component</b>:

```
package arraias.validadorsenha.domain.regras.impl;

import org.springframework.stereotype.Component;
import arraias.validadorsenha.domain.regras.RegraSenha;

@Component("minhaNovaRegra")
public class MinhaNovaRegra implements RegraSenha {
	
	@Override
	public boolean validar(String senha) {
	////
	....
	////
```

2 - Declarar o nome dado ao componente (nesse exemplo, "minhaNovaRegra") na propriedade <b>regrasenha.regraSelecionada</b> do arquivo <b>application.yaml</b>:

```
regrasenha:
  regraSelecionada: "minhaNovaRegra"
```

Pronto. O sistema já irá considerar a nova regra. Caso esse atributo não seja declarado, ou seja informado uma regra que não existe, será sempre executada a regra padrão, descrita no início desse documento. Esse projeto, já possui uma regra adicional de exemplo para referência: <b>regraSomenteNumeros</b>.

<h1>Configuração do Message Broker</h1>

Nesse sistema, o uso do Message Broker é apenas figurativo e a API de interface com o mesmo apenas imprime a mensagem que seria enviada e a fila de destino.

Para fins didáticos, porém, a aplicação foi configurada de maneira a ler do arquivo <b>application.yaml</b> a fila de destino configurada na propriedade <b>exceptionpublisher.queue</b>. Em caso de real implementação, esse conjunto de propriedades deveria conter outras informações como destino, login e senha do Message Broker:

```
exceptionpublisher:
  queue: "queueException"
```


#Considerações arquiteturais
 - Devido ser uma API geralmente utilizada em cadastros de login, não foi considerada nenhuma forma de autenticação;
 - O HTTP Method selecionado foi o POST, para que possa ser criptografado via protocolo HTTP;
 - Por ser uma API aberta, deve-se considerar a implementação de um mecanismo de CAPTCHA, que não foi objeto nessa aplicação de exemplo;
 - Caso seja definida um serviço externo para regras de validação de senhas, como Active Directory, e o mesmo costume apresentar baixa performance, há de se considerar a implementação de um interceptor para calcular o tempo de processamento e comunicar o time de sustentação automaticamente caso ultrapasse um limite aceitável;
 - Por se tratar de validação de senhas, os logs impressos não contem os dados trafegados. No entanto, quando os dados não são sigilosos ou há como ser camuflado, é importante que os logs identifiquem a requisição corrente bem como informe o maior detalhe possível do processamento, facilitando futuras análises de problemas;
 - Para este caso, foi "simulada" uma integração com um Message Broker apenas, em caso de erros sistêmicos. Porém, há de se considerar uma implementação mais abstrata (como utilizado nas regras de validações) para que diversos mecanismos de tratamento de erro possam ser utilizados ao mesmo tempo, por exemplo, o envio de uma mensagem para sustentação e gravação de logs de erro em banco de dados;
 - Embora a solução possa ser desenvolvida em outras plataformas, o Spring Framework foi escolhido pois fornece todos os meios necessários para a construção de uma plataforma corporativa completa, além de permitir o uso de web containers (Tomcat e Jetty por exemplo) para uso de recursos importantes, como controle transacional, injeção de recursos, etc. Além disso, a possibilidade de uso do Spring Boot acelera tanto a configuração do ambiente quanto o desenvolvimento e teste da aplicação em tempo de desenvolvimento, ao mesmo tempo que há amplo suporte da comunidade de desenvolvimento em todo o mundo;

 