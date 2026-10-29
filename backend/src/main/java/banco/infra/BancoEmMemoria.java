package banco.infra;

import banco.application.BancoRepository;
import banco.domain.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class BancoEmMemoria implements BancoRepository {
    private final Map<Integer, Cliente> clientes = new HashMap<>();
    private final Map<Integer, Conta> contas = new HashMap<>();
    public synchronized Optional<Cliente> buscarCliente(int id) { return Optional.ofNullable(clientes.get(id)); }
    public synchronized Optional<Conta> buscarConta(int id) { return Optional.ofNullable(contas.get(id)); }
    public synchronized void salvarCliente(Cliente cliente) { clientes.put(cliente.id(), cliente); }
    public synchronized void salvarConta(Conta conta) { contas.put(conta.id(), conta); }
    // Acessado pela fixture interna de testes; nao existe rota HTTP correspondente.
    public synchronized void limpar() { clientes.clear(); contas.clear(); }
    public synchronized int quantidadeClientes() { return clientes.size(); }
}
