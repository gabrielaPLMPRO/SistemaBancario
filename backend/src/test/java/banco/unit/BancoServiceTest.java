package banco.unit;

import banco.application.*;
import banco.domain.*;
import banco.infra.BancoEmMemoria;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class BancoServiceTest {
    BancoEmMemoria banco;
    CadastroClienteService cadastro;
    AberturaContaService abertura;
    TransferenciaService transferencia;
    ConsultaContaService consulta;

    @BeforeEach void preparar() {
        banco = new BancoEmMemoria();
        cadastro = new CadastroClienteService(banco);
        abertura = new AberturaContaService(banco);
        transferencia = new TransferenciaService(banco);
        consulta = new ConsultaContaService(banco);
    }

    @ParameterizedTest @ValueSource(ints={18,65})
    void aceitaFronteiras(int idade) {
        var cliente = cadastro.cadastrar(101,"Ana",idade,"ana@example.com");
        assertEquals(101,cliente.id()); assertEquals(idade,cliente.idade());
        assertEquals("Ana",cliente.nome()); assertEquals("ana@example.com",cliente.email());
        assertTrue(cliente.ativo()); assertNull(cliente.contaId());
        assertEquals(cliente,banco.buscarCliente(101).orElseThrow());
    }
    @ParameterizedTest @ValueSource(ints={17,66})
    void rejeitaFronteirasSemInserir(int idade) {
        erro("IDADE_INVALIDA",()->cadastro.cadastrar(101,"Ana",idade,"ana@example.com"));
        assertTrue(banco.buscarCliente(101).isEmpty());
    }
    @Test void duplicadoPreservaOriginalMesmoSeNovaIdadeForInvalida() {
        var original = cadastro.cadastrar(101,"Ana",25,"ana@example.com");
        erro("CLIENTE_DUPLICADO",()->cadastro.cadastrar(101,"Outra",17,"outra@example.com"));
        assertEquals(original,banco.buscarCliente(101).orElseThrow());
    }
    @Test void aberturaVinculaAmbosComSaldoZero() {
        cadastro.cadastrar(101,"Ana",25,"ana@example.com");
        var conta=abertura.abrir(101,1001);
        assertEquals(1001,conta.id()); assertEquals(101,conta.clienteId());
        assertEquals(new BigDecimal("0.00"),conta.saldo()); assertTrue(conta.ativa());
        assertEquals(1001,banco.buscarCliente(101).orElseThrow().contaId());
        assertEquals(conta,consulta.consultar(1001));
    }
    @Test void aberturaSemClienteNaoInsereConta() {
        erro("CLIENTE_NAO_ENCONTRADO",()->abertura.abrir(999,1001));
        assertTrue(banco.buscarConta(1001).isEmpty());
    }
    @Test void contaDuplicadaNaoVinculaSegundoCliente() {
        contas();
        cadastro.cadastrar(103,"Carlos",30,"c@example.com");
        var antes=consulta.consultar(1001);
        erro("CONTA_DUPLICADA",()->abertura.abrir(103,1001));
        assertNull(banco.buscarCliente(103).orElseThrow().contaId());
        assertEquals(antes,consulta.consultar(1001));
    }
    @Test void segundaContaDoClienteNaoInsereNada() {
        contas();
        erro("CLIENTE_JA_POSSUI_CONTA",()->abertura.abrir(101,1003));
        assertTrue(banco.buscarConta(1003).isEmpty());
        assertEquals(1001,banco.buscarCliente(101).orElseThrow().contaId());
    }
    @Test void consultaAusenteTemErroControlado() {
        erro("CONTA_NAO_ENCONTRADA",()->consulta.consultar(999));
    }
    @ParameterizedTest @ValueSource(strings={"150.00","500.00","0.01"})
    void transferenciaConservaTotal(String valor) {
        contas();
        var resultado=transferencia.transferir(1001,1002,new BigDecimal(valor));
        assertEquals(new BigDecimal("500.00").subtract(new BigDecimal(valor)),resultado.origem().saldo());
        assertEquals(new BigDecimal("100.00").add(new BigDecimal(valor)),resultado.destino().saldo());
        assertEquals(new BigDecimal("600.00"),consulta.consultar(1001).saldo().add(consulta.consultar(1002).saldo()));
    }
    @ParameterizedTest @ValueSource(strings={"0","-1","0.001","501.00"})
    void rejeicaoMantemSaldos(String valor) {
        contas(); var origem=consulta.consultar(1001); var destino=consulta.consultar(1002);
        assertThrows(RegraNegocioException.class,()->transferencia.transferir(1001,1002,new BigDecimal(valor)));
        assertEquals(origem,consulta.consultar(1001)); assertEquals(destino,consulta.consultar(1002));
    }
    @Test void aceitaZerosExtrasSemFracaoDeCentavo() {
        contas(); transferencia.transferir(1001,1002,new BigDecimal("1.000"));
        assertEquals(new BigDecimal("499.00"),consulta.consultar(1001).saldo());
    }
    @Test void contasAusentesNaoAlteramEstado() {
        contas();
        erro("CONTA_NAO_ENCONTRADA",()->transferencia.transferir(999,1002,BigDecimal.ONE));
        erro("CONTA_NAO_ENCONTRADA",()->transferencia.transferir(1001,999,BigDecimal.ONE));
        assertEquals(new BigDecimal("500.00"),consulta.consultar(1001).saldo());
        assertEquals(new BigDecimal("100.00"),consulta.consultar(1002).saldo());
    }
    @Test void rejeitaMesmaConta() {
        contas(); erro("MESMA_CONTA",()->transferencia.transferir(1001,1001,BigDecimal.ONE));
        assertEquals(new BigDecimal("500.00"),consulta.consultar(1001).saldo());
    }
    @ParameterizedTest @ValueSource(ints={1001,1002})
    void rejeitaContaInativa(int id) {
        contas(); var conta=consulta.consultar(id);
        banco.salvarConta(new Conta(conta.id(),conta.clienteId(),conta.saldo(),false));
        erro("CONTA_INATIVA",()->transferencia.transferir(1001,1002,BigDecimal.ONE));
        assertEquals(new BigDecimal("500.00"),consulta.consultar(1001).saldo());
        assertEquals(new BigDecimal("100.00"),consulta.consultar(1002).saldo());
    }
    @Test void saldoInsuficienteIdentificaRegra() {
        contas(); erro("SALDO_INSUFICIENTE",()->transferencia.transferir(1001,1002,new BigDecimal("500.01")));
    }
    @Test void valorNuloTemErroControlado() {
        contas(); erro("VALOR_INVALIDO",()->transferencia.transferir(1001,1002,null));
    }
    void contas() {
        cadastro.cadastrar(101,"Ana",25,"ana@example.com"); cadastro.cadastrar(102,"Bruno",30,"b@example.com");
        abertura.abrir(101,1001); abertura.abrir(102,1002);
        banco.salvarConta(new Conta(1001,101,new BigDecimal("500.00"),true));
        banco.salvarConta(new Conta(1002,102,new BigDecimal("100.00"),true));
    }
    void erro(String codigo, org.junit.jupiter.api.function.Executable acao) {
        var ex=assertThrows(RegraNegocioException.class,acao);
        assertEquals(codigo,ex.codigo()); assertFalse(ex.getMessage().isBlank());
    }
}
