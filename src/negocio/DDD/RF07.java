package negocio.DDD;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import negocio.Cliente;
import negocio.GerenciadoraClientes;

public class RF07 {

    @Test
    public void naoDeveRemoverClienteAtivo() {
        List<Cliente> clientes = new ArrayList<Cliente>();

        Cliente cliente = new Cliente(1, "Maria Silva", 30, "maria@email.com", 1, true);

        clientes.add(cliente);

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientes);

        boolean removido = gerClientes.removeCliente(1);

        assertFalse(removido);
        assertNotNull(gerClientes.pesquisaCliente(1));
    }

    @Test
    public void deveRemoverClienteInativo() {
        List<Cliente> clientes = new ArrayList<Cliente>();

        Cliente cliente = new Cliente(1, "Maria Silva", 30, "maria@email.com", 1, false);

        clientes.add(cliente);

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientes);

        boolean removido = gerClientes.removeCliente(1);

        assertTrue(removido);
        assertNull(gerClientes.pesquisaCliente(1));
    }
}