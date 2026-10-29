package negocio.TDD;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import negocio.*;

/** Linha de base das RN01, RN02, RN03 e RN05, antes da migracao. */
public class CaracterizacaoLegadoTest {
    @Test public void rejeita17AnosSemInserirCliente() throws Exception { verificaIdadeRejeitada(17); }
    @Test public void aceita18Anos() throws Exception { verificaIdadeAceita(18); }
    @Test public void aceita65Anos() throws Exception { verificaIdadeAceita(65); }
    @Test public void rejeita66AnosSemInserirCliente() throws Exception { verificaIdadeRejeitada(66); }

    @Test public void cadastroDuplicadoPreservaClienteOriginal() throws Exception {
        Cliente original = cliente(30);
        GerenciadoraClientes clientes = new GerenciadoraClientes(new ArrayList<Cliente>());
        clientes.cadastraCliente(original);
        try {
            clientes.cadastraCliente(cliente(25));
            fail("Deveria rejeitar ID duplicado");
        } catch (ClienteJaExisteException esperada) {
            assertSame(original, clientes.pesquisaCliente(1));
            assertEquals(1, clientes.getClientesDoBanco().size());
        }
    }

    @Test public void contaDuplicadaPreservaContaOriginal() throws Exception {
        GerenciadoraClientes clientes = new GerenciadoraClientes(new ArrayList<Cliente>());
        clientes.cadastraCliente(cliente(30));
        ContaCorrente original = new ContaCorrente(10, 100, true);
        GerenciadoraContas contas = new GerenciadoraContas(
                new ArrayList<ContaCorrente>(Arrays.asList(original)));
        try {
            contas.cadastraContaParaCliente(new ContaCorrente(10, 0, true), 1, clientes);
            fail("Deveria rejeitar conta duplicada");
        } catch (ContaJaExisteException esperada) {
            assertSame(original, contas.pesquisaConta(10));
            assertEquals(100, original.getSaldo(), 0.001);
            assertEquals(1, contas.getContasDoBanco().size());
        }
    }

    @Test public void transferenciaValidaPreservaTotalDosSaldos() {
        GerenciadoraContas contas = contas();
        assertTrue(contas.transfereValor(10, 150, 20));
        assertEquals(350, contas.pesquisaConta(10).getSaldo(), 0.001);
        assertEquals(250, contas.pesquisaConta(20).getSaldo(), 0.001);
        assertEquals(600, contas.pesquisaConta(10).getSaldo()
                + contas.pesquisaConta(20).getSaldo(), 0.001);
    }

    @Test public void transferenciaDoSaldoInteiroZeraOrigem() {
        GerenciadoraContas contas = contas();
        assertTrue(contas.transfereValor(10, 500, 20));
        assertEquals(0, contas.pesquisaConta(10).getSaldo(), 0.001);
        assertEquals(600, contas.pesquisaConta(20).getSaldo(), 0.001);
    }

    private Cliente cliente(int idade) {
        return new Cliente(1, "Ana", idade, "ana@example.com", 10, true);
    }

    private void verificaIdadeAceita(int idade) throws Exception {
        GerenciadoraClientes clientes = new GerenciadoraClientes(new ArrayList<Cliente>());
        Cliente novo = cliente(idade);
        clientes.cadastraCliente(novo);
        assertSame(novo, clientes.pesquisaCliente(1));
    }

    private void verificaIdadeRejeitada(int idade) throws Exception {
        GerenciadoraClientes clientes = new GerenciadoraClientes(new ArrayList<Cliente>());
        try {
            clientes.cadastraCliente(cliente(idade));
            fail("Deveria rejeitar idade fora de 18 a 65");
        } catch (IdadeNaoPermitidaException esperada) {
            assertNull(clientes.pesquisaCliente(1));
            assertTrue(clientes.getClientesDoBanco().isEmpty());
        }
    }

    private GerenciadoraContas contas() {
        return new GerenciadoraContas(new ArrayList<ContaCorrente>(Arrays.asList(
                new ContaCorrente(10, 500, true), new ContaCorrente(20, 100, true))));
    }
}
