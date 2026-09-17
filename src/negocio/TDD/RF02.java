package TDD;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import negocio.Cliente;
import negocio.GerenciadoraClientes;

public class RF02 {

    @Test
    public void deveCadastrarClienteValido() throws Exception {
        GerenciadoraClientes gerClientes = new GerenciadoraClientes(new ArrayList<Cliente>());

        Cliente novoCliente = new Cliente(2, "Ana Souza", 25, "ana@email.com", 2, true);

        gerClientes.cadastraCliente(novoCliente);

        assertEquals(novoCliente, gerClientes.pesquisaCliente(2));
        assertEquals(1, gerClientes.getClientesDoBanco().size());
    }
}
