package negocio.BDD;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import negocio.Cliente;
import negocio.GerenciadoraClientes;

public class RF01 {

    @Test
    public void deveConsultarClienteExistente() {
        
        List<Cliente> clientes = new ArrayList<>();

        Cliente clienteCadastrado = new Cliente(1, "Maria Silva", 30, "maria@email.com", 1, true);

        clientes.add(clienteCadastrado);

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientes);

        Cliente clienteEncontrado = gerClientes.pesquisaCliente(1);

        assertNotNull(clienteEncontrado);
        assertEquals(1, clienteEncontrado.getId());
        assertEquals("Maria Silva", clienteEncontrado.getNome());
        assertEquals("maria@email.com", clienteEncontrado.getEmail());
    }
}