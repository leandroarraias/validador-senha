package arraias.validadorsenha.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import arraias.validadorsenha.domain.regras.impl.RegraSenhaDefault;

public class RegraSenhaDefaultTest {
	
	private ValidadorSenhaService service =
			new ValidadorSenhaService(new RegraSenhaDefault());
	
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
				"AbTp9!fok",
				"abcABC12@",
				"abcdefghijklmnopqrstuvwxyzA1@",
				"ABCDEFGHIJKLMNOPQRSTUVWXYZa1@",
				"1234567890Aa@",
				"!@#$%^&*()-+Aa1",
				"!@#$%^&*()-+0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}
	
	static Stream<String> senhasInvalidas() {
        return Stream.of(
                null,
                "",
                " ",
                "aa",
                "ab",
                "AAAbbbCc",
                "AbTp9!foo",
                "AbTp9!foA",
                "AbTp9 fok",
                "abcAB12@",
                "abcABC123",
                "abcABCDE@",
                "abcdef12@",
                "ABCDEF12@",
                "abcAB 12@",
                "abcAB_12@",
                "abcABÇ12@",
                "abcAB=12@",
                "abcAB/12@",
                "abcAB\\12@",
                "abcAB\\\\12@",
                "abcAB,12@",
                "abcAB;12@",
                "abcAB<12@",
                "abcAB>12@",
                "abcAB.12@",
                "!@#$%^&*()-+0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZZ");
    }
}
