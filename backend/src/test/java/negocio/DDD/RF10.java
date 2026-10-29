package negocio.DDD;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import negocio.ContaCorrente;
import negocio.GerenciadoraContas;

public class RF10 {

    @Test
    public void deveIdentificarContaAtiva() {
        List<ContaCorrente> contas = new ArrayList<ContaCorrente>();

        ContaCorrente conta = new ContaCorrente(1, 1000.0, true);

        contas.add(conta);

        GerenciadoraContas gerContas = new GerenciadoraContas(contas);

        assertTrue(gerContas.contaAtiva(1));
    }

    @Test
    public void deveIdentificarContaInativa() {

        List<ContaCorrente> contas = new ArrayList<ContaCorrente>();

        ContaCorrente conta = new ContaCorrente(1, 1000.0, false);

        contas.add(conta);

        GerenciadoraContas gerContas = new GerenciadoraContas(contas);

        assertFalse(gerContas.contaAtiva(1));
    }



}
