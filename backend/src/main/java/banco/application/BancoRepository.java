package banco.application;

import banco.domain.Cliente;
import banco.domain.Conta;
import java.util.Optional;

/** A mesma instancia e o mesmo monitor protegem todas as operacoes compostas. */
public interface BancoRepository {
    Optional<Cliente> buscarCliente(int id);
    Optional<Conta> buscarConta(int id);
    void salvarCliente(Cliente cliente);
    void salvarConta(Conta conta);
}
