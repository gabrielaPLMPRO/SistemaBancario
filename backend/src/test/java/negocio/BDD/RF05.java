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
        List<Cliente> clientes = new ArrayList<Cliente>();  //cira a lista de clientes

        Cliente cliente = new Cliente(1, "Maria Silva", 30, "maria@email.com", 1, true); //instancia um cliente ativo   

        clientes.add(cliente); //adiciona o cliente na lista

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientes); //instancia a gerenciadora de clientes com a lista de clientes   

        gerClientes.desativaCliente(1); //desativa o cliente com id 1

        assertFalse(gerClientes.clienteAtivo(1)); //verifica se o cliente com id 1 está inativo
    }

    @Test
    public void naoDeveDesativarClienteInexistente() {

        List<Cliente> clientes = new ArrayList<Cliente>();

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientes);

        assertFalse(gerClientes.desativaCliente(99));
    }
}