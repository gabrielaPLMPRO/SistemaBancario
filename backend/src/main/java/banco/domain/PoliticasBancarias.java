package banco.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PoliticasBancarias {
    private PoliticasBancarias() { }
    public static void validarIdade(int idade) {
        if (idade < 18 || idade > 65) {
            throw new RegraNegocioException("IDADE_INVALIDA", "A idade deve estar entre 18 e 65 anos.");
        }
    }
    public static BigDecimal valorMonetario(BigDecimal valor) {
        if (valor == null || valor.signum() <= 0) {
            throw new RegraNegocioException("VALOR_INVALIDO", "Informe um valor maior que zero.");
        }
        try {
            return valor.setScale(2, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException ex) {
            throw new RegraNegocioException("FRACAO_DE_CENTAVO", "O valor não pode conter fração de centavo.");
        }
    }
}
