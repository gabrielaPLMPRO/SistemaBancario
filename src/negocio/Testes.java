package negocio;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class Testes {

        //Espera que a idade 30 seja aceita e o método retorne true.
    @Test
    public void validaIdadeAceita() throws IdadeNaoPermitidaException {
        GerenciadoraClientes gerClientes = new GerenciadoraClientes(new ArrayList<Cliente>());
        assertTrue(gerClientes.validaIdade(30));
    }

        //Como a idade é menor que 18, o método deve lançar IdadeNaoPermitidaException.
    @Test(expected = IdadeNaoPermitidaException.class)
    public void validaIdadeRejeitaIdadeForaDoIntervalo() throws IdadeNaoPermitidaException {
        GerenciadoraClientes gerClientes = new GerenciadoraClientes(new ArrayList<Cliente>());
        gerClientes.validaIdade(17);
    }

       //O teste garante que, ao consultar um ID ainda não cadastrado, a validação não lança erro. 
    @Test
    public void validaClienteNaoDuplicadoAceitaIdNaoCadastrado() throws Exception {
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente(1, "Maria Silva", 30, "maria@email.com", 1, true));

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientes);
        gerClientes.validaClienteNaoDuplicado(2);
    }
        //O teste garante que, ao consultar um ID já cadastrado, a validação lança a exceção ClienteJaExisteException.
    @Test(expected = ClienteJaExisteException.class)
    public void validaClienteNaoDuplicadoRejeitaIdDuplicado() throws Exception {
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente(1, "Maria Silva", 30, "maria@email.com", 1, true));

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientes);
        gerClientes.validaClienteNaoDuplicado(1);
    }
        //O teste garante que, ao consultar um ID ainda não cadastrado, a validação não lança erro.
    @Test
    public void validaContaNaoDuplicadaAceitaIdNaoCadastrado() throws Exception {
        List<ContaCorrente> contas = new ArrayList<>();
        contas.add(new ContaCorrente(1, 0, true));

        GerenciadoraContas gerContas = new GerenciadoraContas(contas);
        gerContas.validaContaNaoDuplicada(2);
    }
        //O teste garante que, ao consultar um ID já cadastrado, a validação lança a exceção ContaJaExisteException.
    @Test(expected = ContaJaExisteException.class)
    public void validaContaNaoDuplicadaRejeitaIdDuplicado() throws Exception {
        List<ContaCorrente> contas = new ArrayList<>();
        contas.add(new ContaCorrente(1, 0, true));

        GerenciadoraContas gerContas = new GerenciadoraContas(contas);
        gerContas.validaContaNaoDuplicada(1);
    }
}
