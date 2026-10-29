package negocio.BDD;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import negocio.ContaCorrente;
import negocio.GerenciadoraContas;

public class RF08 {

    @Test
    public void deveConsultarContaExistente() {

        List<ContaCorrente> contas = new ArrayList<ContaCorrente>();

        ContaCorrente conta = new ContaCorrente(1, 1000.0, true);

        contas.add(conta);

        GerenciadoraContas gerContas = new GerenciadoraContas(contas);

        ContaCorrente contaEncontrada = gerContas.pesquisaConta(1);

        assertNotNull(contaEncontrada);
        assertEquals(1, contaEncontrada.getId());
    }

    @Test
    public void deveRetornarNullParaContaInexistente() {
        List<ContaCorrente> contas = new ArrayList<ContaCorrente>();

        GerenciadoraContas gerContas = new GerenciadoraContas(contas);

        ContaCorrente contaEncontrada = gerContas.pesquisaConta(99);

        assertNull(contaEncontrada);
    }
}