package banco.application;

import banco.domain.*;
import org.springframework.stereotype.Service;

@Service
public class CadastroClienteService {
    private final BancoRepository banco;
    public CadastroClienteService(BancoRepository banco) { this.banco = banco; }
    public Cliente cadastrar(int id, String nome, int idade, String email) {
        synchronized (banco) {
            if (banco.buscarCliente(id).isPresent()) {
                throw new RegraNegocioException("CLIENTE_DUPLICADO", "Já existe cliente com este ID.");
            }
            PoliticasBancarias.validarIdade(idade);
            Cliente cliente = new Cliente(id, nome, idade, email, true, null);
            banco.salvarCliente(cliente);
            return cliente;
        }
    }
}
