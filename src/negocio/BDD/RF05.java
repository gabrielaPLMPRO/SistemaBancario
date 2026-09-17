package negocio.BDD;

import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import negocio.Cliente;
import negocio.GerenciadoraClientes;

public class RF05 {

    @Test
    public void deveDesativarClienteAtivo() {
        List<Cliente> clientes = new ArrayList<Cliente>();

        Cliente cliente = new Cliente(
                1, "Maria Silva", 30,
                "maria@email.com", 1, true);

        clientes.add(cliente);

        GerenciadoraClientes gerClientes =
                new GerenciadoraClientes(clientes);

        gerClientes.desativaCliente(1);

        assertFalse(gerClientes.clienteAtivo(1));
    }
}