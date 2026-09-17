package negocio.BDD;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import negocio.Cliente;
import negocio.GerenciadoraClientes;

public class RF04 {

    @Test
    public void deveAtivarClienteInativo() {

        List<Cliente> clientes = new ArrayList<Cliente>();

        Cliente cliente = new Cliente(1, "Maria Silva", 30, "maria@email.com", 1, false);

        clientes.add(cliente);

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientes);

        gerClientes.ativaCliente(1);

        assertTrue(gerClientes.clienteAtivo(1));
    }
}