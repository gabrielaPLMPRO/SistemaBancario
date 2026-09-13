package negocio;

/**
 * Exceção lançada quando o ID do cliente informado já está cadastrado.
 */
public class ClienteJaExisteException extends Exception {

    public static final String MSG_CLIENTE_JA_EXISTE = "Cliente com este ID já existe no banco.";

    public ClienteJaExisteException(String msg) {
        super(msg);
    }
}