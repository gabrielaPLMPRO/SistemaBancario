package negocio.DDD;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import negocio.Cliente;
import negocio.GerenciadoraClientes;

public class RF06 {

    @Test
    public void deveIdentificarClienteAtivo() {
        List<Cliente> clientes = new ArrayList<Cliente>();

        Cliente cliente = new Cliente(1, "Maria Silva", 30, "maria@email.com", 1, true);

        clientes.add(cliente);

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientes);

        assertTrue(gerClientes.clienteAtivo(1));
    }

    @Test
    public void deveIdentificarClienteInativo() {

        List<Cliente> clientes = new ArrayList<Cliente>();

        Cliente cliente = new Cliente(1, "Maria Silva", 30, "maria@email.com", 1, false);

        clientes.add(cliente);

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientes);

        assertFalse(gerClientes.clienteAtivo(1));
    }
}