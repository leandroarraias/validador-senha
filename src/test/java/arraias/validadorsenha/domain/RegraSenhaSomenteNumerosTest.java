package arraias.validadorsenha.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import arraias.validadorsenha.domain.regras.impl.RegraSenhaSomenteNumeros;

public class RegraSenhaSomenteNumerosTest {
	
	private ValidadorSenhaService service =
			new ValidadorSenhaService(new RegraSenhaSomenteNumeros());
	
	@ParameterizedTest(name = "#{index} - Testando senha valida {0}")
	@MethodSource("senhasValidas")
	void testSenhasValidas(String senha) {
		assertTrue(service.validar(senha));
	}

	@ParameterizedTest(name = "#{index} - Testando senha invalida {0}")
	@MethodSource("senhasInvalidas")
	void testSenhasInvalidas(String senha) {
		assertFalse(service.validar(senha));
	}
	
	static Stream<String> senhasValidas() {
		return Stream.of(
				"0",
				"01",
				"0123456789",
				"01234567890123456789012345678901234567890123456789");
	}
	
	static Stream<String> senhasInvalidas() {
        return Stream.of(
                null,
                "",
                " ",
                " 0",
                "0 ",
                " 0 ",
                " a",
                "a ",
                " a ",
                "a",
                "a1",
                "1a",
                " .",
                ". ",
                " . ",
                ".",
                ".1",
                "1.",
                "1.2",
                "012345678901234567890123456789012345678901234567890");
    }
}
