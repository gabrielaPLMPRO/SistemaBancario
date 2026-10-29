package negocio.DDD;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import negocio.ContaCorrente;
import negocio.GerenciadoraContas;

public class RF11 {

    @Test 
    public void naoDeveRemoverContaAtiva() {
        List<ContaCorrente> contas = new ArrayList<ContaCorrente>();

        ContaCorrente conta = new ContaCorrente(1, 1000.0, true);

        contas.add(conta);

        GerenciadoraContas gerContas = new GerenciadoraContas(contas);

        boolean removido = gerContas.removeConta(1);

        assertFalse(removido);
        assertTrue(gerContas.contaAtiva(1));
    }

    @Test
    public void deveRemoverContaInativa() {
        List<ContaCorrente> contas = new ArrayList<ContaCorrente>();

        ContaCorrente conta = new ContaCorrente(1, 1000.0, false);

        contas.add(conta);

        GerenciadoraContas gerContas = new GerenciadoraContas(contas);

        boolean removido = gerContas.removeConta(1);

        assertTrue(removido);
        assertFalse(gerContas.contaAtiva(1));
    }
}
