package negocio.TDD;

import java.util.ArrayList;

import org.junit.Test;

import negocio.Cliente;
import negocio.GerenciadoraClientes;
import negocio.IdadeNaoPermitidaException;

public class RF03 {
    
    @Test(expected = IdadeNaoPermitidaException.class)
    public void naoDeveCadastrarClienteMenorDeIdade() throws Exception {
        GerenciadoraClientes gerClientes = new GerenciadoraClientes(new ArrayList<Cliente>());

        Cliente clienteMenor = new Cliente(2, "Joao", 17, "joao@email.com", 2, true);

        gerClientes.cadastraCliente(clienteMenor);
    }
}
