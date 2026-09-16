package negocio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class Testes {

    // TESTES DO RF3 VALIDAR IDADE DO CLIENTE
    // Espera que a idade 30 seja aceita e o método retorne true.
    @Test
    public void validaIdadeAceita() throws IdadeNaoPermitidaException {
        GerenciadoraClientes gerClientes = new GerenciadoraClientes(new ArrayList<Cliente>());
        assertTrue(gerClientes.validaIdade(30));
    }

    /*
     * Como a idade é menor que 18, o método deve lançar IdadeNaoPermitidaException.
     */
    @Test(expected = IdadeNaoPermitidaException.class)
    public void validaIdadeRejeitaIdadeForaDoIntervalo() throws IdadeNaoPermitidaException {
        GerenciadoraClientes gerClientes = new GerenciadoraClientes(new ArrayList<Cliente>());
        gerClientes.validaIdade(17);
    }

    // TESTES DO RF2 CADASTRO DE UM CLIENTE
    // O teste garante que, ao consultar um ID ainda não cadastrado, a validação não
    // lança erro.
    @Test
    public void validaClienteNaoDuplicadoAceitaIdNaoCadastrado() throws Exception {
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente(1, "Maria Silva", 30, "maria@email.com", 1, true));

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientes);
        gerClientes.validaClienteNaoDuplicado(2);
    }

    // O teste garante que, ao consultar um ID já cadastrado, a validação lança a
    // exceção ClienteJaExisteException.
    @Test(expected = ClienteJaExisteException.class)
    public void validaClienteNaoDuplicadoRejeitaIdDuplicado() throws Exception {
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente(1, "Maria Silva", 30, "maria@email.com", 1, true));

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientes);
        gerClientes.validaClienteNaoDuplicado(1);
    }

    // TESTES DO RF9 CADASTRO DA CONTA CORRENTE
    // O teste garante que, ao consultar um ID ainda não cadastrado, a validação não
    // lança erro.
    @Test
    public void validaContaNaoDuplicadaAceitaIdNaoCadastrado() throws Exception {
        List<ContaCorrente> contas = new ArrayList<>();
        contas.add(new ContaCorrente(1, 0, true));

        GerenciadoraContas gerContas = new GerenciadoraContas(contas);
        gerContas.validaContaNaoDuplicada(2);
    }

    // O teste garante que, ao consultar um ID já cadastrado, a validação lança a
    // exceção ContaJaExisteException.
    @Test(expected = ContaJaExisteException.class)
    public void validaContaNaoDuplicadaRejeitaIdDuplicado() throws Exception {
        List<ContaCorrente> contas = new ArrayList<>();
        contas.add(new ContaCorrente(1, 0, true));

        GerenciadoraContas gerContas = new GerenciadoraContas(contas);
        gerContas.validaContaNaoDuplicada(1);
    }

    // TESTES DO RF12 TRANSFERÊNCIA ENTRE CONTAS
    // O teste garante que, ao transferir um valor menor ou igual ao saldo da conta
    // de origem, a transferência é bem-sucedida.
    @Test
    public void TransferenciaEntreContas() throws Exception {
        List<ContaCorrente> contas = new ArrayList<>();
        contas.add(new ContaCorrente(1, 100, true));
        contas.add(new ContaCorrente(2, 50, true));

        GerenciadoraContas gerContas = new GerenciadoraContas(contas);

        assertTrue(gerContas.transfereValor(1, 30, 2));
        assertEquals(70.0, gerContas.pesquisaConta(1).getSaldo(), 0.01);
        assertEquals(80.0, gerContas.pesquisaConta(2).getSaldo(), 0.01);

    }

    @Test
    public void naoDeveTransferirValorZero() {
        List<ContaCorrente> contas = new ArrayList<>();
        contas.add(new ContaCorrente(1, 100, true));
        contas.add(new ContaCorrente(2, 50, true));

        GerenciadoraContas gerContas = new GerenciadoraContas(contas);

        assertFalse(gerContas.transfereValor(1, 0, 2));
        assertEquals(100.0, gerContas.pesquisaConta(1).getSaldo(), 0.01);
        assertEquals(50.0, gerContas.pesquisaConta(2).getSaldo(), 0.01);
    }

    @Test
    public void naoDeveTransferirValorNegativo() {
        List<ContaCorrente> contas = new ArrayList<>();
        contas.add(new ContaCorrente(1, 100, true));
        contas.add(new ContaCorrente(2, 50, true));

        GerenciadoraContas gerContas = new GerenciadoraContas(contas);

        assertFalse(gerContas.transfereValor(1, -30, 2));
        assertEquals(100.0, gerContas.pesquisaConta(1).getSaldo(), 0.01);
        assertEquals(50.0, gerContas.pesquisaConta(2).getSaldo(), 0.01);
    }

    @Test
    public void naoDeveTransferirQuandoSaldoForInsuficiente() {
        List<ContaCorrente> contas = new ArrayList<>();
        contas.add(new ContaCorrente(1, 100, true));
        contas.add(new ContaCorrente(2, 50, true));

        GerenciadoraContas gerContas = new GerenciadoraContas(contas);

        assertFalse(gerContas.transfereValor(1, 150, 2));
        assertEquals(100.0, gerContas.pesquisaConta(1).getSaldo(), 0.01);
        assertEquals(50.0, gerContas.pesquisaConta(2).getSaldo(), 0.01);
    }
}