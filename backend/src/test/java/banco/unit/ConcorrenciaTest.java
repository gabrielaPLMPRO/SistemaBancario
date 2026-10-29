package banco.unit;

import banco.application.*;
import banco.domain.*;
import banco.infra.BancoEmMemoria;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class ConcorrenciaTest {
    @Test void concorrenciaNaoPermiteGastoDuplo() throws Exception {
        var banco=new BancoEmMemoria();
        banco.salvarConta(new Conta(1,1,new BigDecimal("10.00"),true));
        banco.salvarConta(new Conta(2,2,new BigDecimal("0.00"),true));
        var service=new TransferenciaService(banco);
        var inicio=new CountDownLatch(1);
        try(var pool=Executors.newFixedThreadPool(8)) {
            List<Future<Boolean>> resultados=new ArrayList<>();
            for(int i=0;i<20;i++) resultados.add(pool.submit(()->{
                inicio.await();
                try { service.transferir(1,2,BigDecimal.ONE); return true; }
                catch(RegraNegocioException ex) { assertEquals("SALDO_INSUFICIENTE",ex.codigo()); return false; }
            }));
            inicio.countDown(); int sucessos=0;
            for(var f:resultados) if(f.get(10,TimeUnit.SECONDS)) sucessos++;
            assertEquals(10,sucessos);
        }
        assertEquals(new BigDecimal("0.00"),banco.buscarConta(1).orElseThrow().saldo());
        assertEquals(new BigDecimal("10.00"),banco.buscarConta(2).orElseThrow().saldo());
    }
    @Test void aberturaConcorrenteMantemUmaContaPorCliente() throws Exception {
        var banco=new BancoEmMemoria();
        new CadastroClienteService(banco).cadastrar(1,"Ana",25,"a@example.com");
        var service=new AberturaContaService(banco); var inicio=new CountDownLatch(1);
        try(var pool=Executors.newFixedThreadPool(2)) {
            List<Future<Integer>> resultados=new ArrayList<>();
            for(int id:new int[]{10,20}) resultados.add(pool.submit(()->{
                inicio.await(); try { service.abrir(1,id); return id; }
                catch(RegraNegocioException ex) { assertEquals("CLIENTE_JA_POSSUI_CONTA",ex.codigo()); return 0; }
            }));
            inicio.countDown(); int soma=0; int sucessos=0;
            for(var f:resultados) { int id=f.get(10,TimeUnit.SECONDS); if(id!=0){soma+=id;sucessos++;} }
            assertEquals(1,sucessos);
            assertEquals(soma,banco.buscarCliente(1).orElseThrow().contaId());
            assertEquals(1,(banco.buscarConta(10).isPresent()?1:0)+(banco.buscarConta(20).isPresent()?1:0));
        }
    }
}
