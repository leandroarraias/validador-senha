package arraias.validadorsenha.application.exception;

public class MensagemErro {

	private String uuid;
	private String erro;

	public MensagemErro(String uuid, String erro) {
		this.uuid = uuid;
		this.erro = erro;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	@Override
	public String toString() {
		return "UUID = " + uuid + " >>> " + erro;
	}
}
