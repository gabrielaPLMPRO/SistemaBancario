package negocio;

/**
 * Exceção lançada quando o ID da conta corrente informado já está cadastrado.
 */
public class ContaJaExisteException extends Exception {

    public static final String MSG_CONTA_JA_EXISTE = "Conta corrente com este ID já existe no banco.";

    public ContaJaExisteException(String msg) {
        super(msg);
    }
}
