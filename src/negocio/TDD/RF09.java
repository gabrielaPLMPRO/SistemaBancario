package TDD;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import negocio.Cliente;
import negocio.ContaCorrente;
import negocio.GerenciadoraClientes;
import negocio.GerenciadoraContas;

public class RF09 {

    @Test
    public void deveCadastrarContaParaClienteExistente() throws Exception {

        List<Cliente> clientes = new ArrayList<>();

        clientes.add(new Cliente(1, "Maria Silva", 30, "maria@email.com", 2, true));

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientes);

        GerenciadoraContas gerContas = new GerenciadoraContas(new ArrayList<ContaCorrente>());

        ContaCorrente novaConta = new ContaCorrente(2, 0, true);

        assertTrue(gerContas.cadastraContaParaCliente(novaConta, 1, gerClientes));

        assertEquals(novaConta, gerContas.pesquisaConta(2));
    }

    @Test
    public void naoDeveCadastrarContaParaClienteInexistente() throws Exception {

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(new ArrayList<Cliente>());

        GerenciadoraContas gerContas = new GerenciadoraContas(new ArrayList<ContaCorrente>());

        ContaCorrente novaConta = new ContaCorrente(2, 0, true);

        assertFalse(gerContas.cadastraContaParaCliente(novaConta, 99, gerClientes));

        assertEquals(0, gerContas.getContasDoBanco().size());
    }
}