package TDD;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import negocio.ContaCorrente;
import negocio.GerenciadoraContas;

public class RF12 {
    
    // TESTES DO RF12 TRANSFERÊNCIA ENTRE CONTAS
    // O teste garante que, ao transferir um valor menor ou igual ao saldo da conta
    // de origem, a transferência é bem-sucedida.
    @Test
    public void TransferenciaEntreContas() throws Exception {
        GerenciadoraContas gerContas = criaGerenciadoraParaTransferencia();

        assertTrue(gerContas.transfereValor(1, 30, 2));
        assertEquals(70.0, gerContas.pesquisaConta(1).getSaldo(), 0.01);
        assertEquals(80.0, gerContas.pesquisaConta(2).getSaldo(), 0.01);

    }

    @Test
    public void naoDeveTransferirValorZero() {
        GerenciadoraContas gerContas = criaGerenciadoraParaTransferencia();

        assertFalse(gerContas.transfereValor(1, 0, 2));
        assertEquals(100.0, gerContas.pesquisaConta(1).getSaldo(), 0.01);
        assertEquals(50.0, gerContas.pesquisaConta(2).getSaldo(), 0.01);
    }

    @Test
    public void naoDeveTransferirValorNegativo() {
        GerenciadoraContas gerContas = criaGerenciadoraParaTransferencia();

        assertFalse(gerContas.transfereValor(1, -30, 2));
        assertEquals(100.0, gerContas.pesquisaConta(1).getSaldo(), 0.01);
        assertEquals(50.0, gerContas.pesquisaConta(2).getSaldo(), 0.01);
    }

    @Test
    public void naoDeveTransferirQuandoSaldoForInsuficiente() {
        GerenciadoraContas gerContas = criaGerenciadoraParaTransferencia();

        assertFalse(gerContas.transfereValor(1, 150, 2));
        assertEquals(100.0, gerContas.pesquisaConta(1).getSaldo(), 0.01);
        assertEquals(50.0, gerContas.pesquisaConta(2).getSaldo(), 0.01);
    }

    private GerenciadoraContas criaGerenciadoraParaTransferencia() {
        List<ContaCorrente> contas = new ArrayList<>();
        contas.add(new ContaCorrente(1, 100, true));
        contas.add(new ContaCorrente(2, 50, true));

        return new GerenciadoraContas(contas);
    }
}
