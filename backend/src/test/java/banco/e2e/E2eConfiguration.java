package banco.e2e;

import banco.infra.BancoEmMemoria;
import com.fasterxml.jackson.databind.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.*;
import java.nio.file.*;
import java.math.BigDecimal;
import java.util.*;

@TestConfiguration(proxyBeanMethods=false) @EnableScheduling
public class E2eConfiguration {
    @Bean CanalFixture canalFixture(BancoEmMemoria banco, ObjectMapper mapper, Environment env) throws Exception {
        String diretorio=env.getRequiredProperty("banco.fixture-dir");
        return new CanalFixture(banco,mapper,Path.of(diretorio));
    }
    static class CanalFixture {
        final BancoEmMemoria banco; final ObjectMapper mapper; final Path pasta;
        CanalFixture(BancoEmMemoria banco,ObjectMapper mapper,Path pasta) throws Exception {
            this.banco=banco; this.mapper=mapper; this.pasta=pasta; Files.createDirectories(pasta);
        }
        @Scheduled(fixedDelay=50)
        public void processar() throws Exception {
            try(var arquivos=Files.list(pasta)) {
                for(Path pedido:arquivos.filter(p->p.toString().endsWith(".request.json")).toList()) {
                    Map<String,Object> resposta;
                    try {
                        JsonNode dados=mapper.readTree(pedido.toFile());
                        synchronized(banco) {
                            switch(dados.path("op").asText()) {
                                case "reset" -> banco.limpar();
                                case "saldos" -> {
                                    Map<Integer,BigDecimal> saldos=new HashMap<>();
                                    var campos=dados.path("saldos").fields();
                                    while(campos.hasNext()) {
                                        var entrada=campos.next(); int id=Integer.parseInt(entrada.getKey());
                                        banco.buscarConta(id).orElseThrow();
                                        BigDecimal valor=new BigDecimal(entrada.getValue().asText()).setScale(2);
                                        if(valor.signum()<0) throw new IllegalArgumentException("Saldo negativo na fixture");
                                        saldos.put(id,valor);
                                    }
                                    saldos.forEach((id,valor)->banco.salvarConta(banco.buscarConta(id).orElseThrow().comSaldo(valor)));
                                }
                                default -> throw new IllegalArgumentException("Comando desconhecido");
                            }
                        }
                        resposta=Map.of("ok",true);
                    } catch(Exception ex) { resposta=Map.of("ok",false,"erro",ex.toString()); }
                    Path temporario=pasta.resolve(pedido.getFileName()+".tmp");
                    mapper.writeValue(temporario.toFile(),resposta);
                    Files.move(temporario,pasta.resolve(pedido.getFileName().toString().replace(".request.json",".response.json")),StandardCopyOption.REPLACE_EXISTING);
                    Files.delete(pedido);
                }
            }
        }
    }
}
