package banco.diferencial;

import banco.application.*;
import banco.infra.BancoEmMemoria;
import banco.domain.RegraNegocioException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ComparacaoLegadoTest {
    @Test void duplicidadeDeClientePreservaOsDadosNasDuasVersoes() throws Exception {
        var legado=new negocio.GerenciadoraClientes(new ArrayList<>());
        var original=new negocio.Cliente(101,"Ana",25,"a@example.com",0,true);
        legado.cadastraCliente(original);
        assertThrows(negocio.ClienteJaExisteException.class,()->legado.cadastraCliente(new negocio.Cliente(101,"Outra",30,"b@example.com",0,true)));
        var novo=new BancoEmMemoria(); var cadastro=new CadastroClienteService(novo);
        var atual=cadastro.cadastrar(101,"Ana",25,"a@example.com");
        var ex=assertThrows(RegraNegocioException.class,()->cadastro.cadastrar(101,"Outra",30,"b@example.com"));
        assertEquals("CLIENTE_DUPLICADO",ex.codigo());
        assertSame(original,legado.pesquisaCliente(101)); assertEquals(atual,novo.buscarCliente(101).orElseThrow());
    }
    @Test void aberturaComparaAusenciaDuplicidadeEVinculoIntencional() throws Exception {
        var clientes=new negocio.GerenciadoraClientes(new ArrayList<>());
        var legado=new negocio.GerenciadoraContas(new ArrayList<>());
        var novo=new BancoEmMemoria(); var abertura=new AberturaContaService(novo);
        assertFalse(legado.cadastraContaParaCliente(new negocio.ContaCorrente(1001,0,true),101,clientes));
        assertEquals("CLIENTE_NAO_ENCONTRADO",assertThrows(RegraNegocioException.class,()->abertura.abrir(101,1001)).codigo());
        clientes.cadastraCliente(new negocio.Cliente(101,"Ana",25,"a@example.com",0,true));
        new CadastroClienteService(novo).cadastrar(101,"Ana",25,"a@example.com");
        assertTrue(legado.cadastraContaParaCliente(new negocio.ContaCorrente(1001,0,true),101,clientes));
        var conta=abertura.abrir(101,1001);
        assertEquals(legado.pesquisaConta(1001).getId(),conta.id());
        assertEquals(0,BigDecimal.valueOf(legado.pesquisaConta(1001).getSaldo()).compareTo(conta.saldo()));
        assertEquals(0,clientes.pesquisaCliente(101).getIdContaCorrente());
        assertEquals(1001,novo.buscarCliente(101).orElseThrow().contaId());
        assertThrows(negocio.ContaJaExisteException.class,()->legado.cadastraContaParaCliente(new negocio.ContaCorrente(1001,0,true),101,clientes));
        assertEquals("CONTA_DUPLICADA",assertThrows(RegraNegocioException.class,()->abertura.abrir(101,1001)).codigo());
    }
    @ParameterizedTest @ValueSource(ints={17,18,65,66})
    void mesmosLimitesDeCadastro(int idade) throws Exception {
        var legado=new negocio.GerenciadoraClientes(new ArrayList<>());
        boolean antigo=true;
        try { legado.cadastraCliente(new negocio.Cliente(101,"Ana",idade,"a@example.com",0,true)); }
        catch(negocio.IdadeNaoPermitidaException ex) { antigo=false; }
        var novo=new BancoEmMemoria(); boolean atual=true;
        try { new CadastroClienteService(novo).cadastrar(101,"Ana",idade,"a@example.com"); }
        catch(RegraNegocioException ex) { atual=false; }
        assertEquals(antigo,atual);
        assertEquals(legado.pesquisaCliente(101)!=null,novo.buscarCliente(101).isPresent());
    }
    @ParameterizedTest @ValueSource(strings={"150.00","0.00","-1.00","501.00","500.00"})
    void mesmasEntradasMesmosSaldos(String quantia) {
        var origem=new negocio.ContaCorrente(1,500,true); var destino=new negocio.ContaCorrente(2,100,true);
        var legado=new negocio.GerenciadoraContas(new ArrayList<>(List.of(origem,destino)));
        var novo=new BancoEmMemoria();
        novo.salvarConta(new banco.domain.Conta(1,1,new BigDecimal("500.00"),true));
        novo.salvarConta(new banco.domain.Conta(2,2,new BigDecimal("100.00"),true));
        boolean antigo=legado.transfereValor(1,Double.parseDouble(quantia),2); boolean atual=true;
        try { new TransferenciaService(novo).transferir(1,2,new BigDecimal(quantia)); }
        catch(RegraNegocioException ex) { atual=false; }
        assertEquals(antigo,atual);
        assertEquals(0,BigDecimal.valueOf(origem.getSaldo()).compareTo(novo.buscarConta(1).orElseThrow().saldo()));
        assertEquals(0,BigDecimal.valueOf(destino.getSaldo()).compareTo(novo.buscarConta(2).orElseThrow().saldo()));
    }
    @Test void ausenciaNaConsultaTemMesmoSignificado() {
        assertNull(new negocio.GerenciadoraContas(new ArrayList<>()).pesquisaConta(999));
        var ex=assertThrows(RegraNegocioException.class,()->new ConsultaContaService(new BancoEmMemoria()).consultar(999));
        assertEquals("CONTA_NAO_ENCONTRADA",ex.codigo());
    }
    @Test void contaAusenteCorrigeExcecaoNaoTratada() {
        var legado=new negocio.GerenciadoraContas(new ArrayList<>());
        assertThrows(NullPointerException.class,()->legado.transfereValor(999,1,100));
        var novo=new BancoEmMemoria();
        var ex=assertThrows(RegraNegocioException.class,()->new TransferenciaService(novo).transferir(999,100,BigDecimal.ONE));
        assertEquals("CONTA_NAO_ENCONTRADA",ex.codigo());
    }
}
