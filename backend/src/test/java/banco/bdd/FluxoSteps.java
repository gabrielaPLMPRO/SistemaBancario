package banco.bdd;

import banco.BancoApplication;
import banco.infra.BancoEmMemoria;
import com.fasterxml.jackson.databind.*;
import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.java.pt.*;
import io.cucumber.datatable.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest(classes=BancoApplication.class,webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FluxoSteps {
    @LocalServerPort int porta;
    @Autowired BancoEmMemoria banco;
    @Autowired ObjectMapper mapper;
    HttpResponse<String> ultima;
    List<JsonNode> clientes = new ArrayList<>();
    JsonNode origem,destino;
    final HttpClient http=HttpClient.newBuilder().connectTimeout(java.time.Duration.ofSeconds(5)).build();
    @Dado("que o ambiente de teste está vazio")
    public void limpar() { banco.limpar(); clientes.clear(); }
    @Quando("cadastro {string} com ID {int} e idade {int}")
    public void cadastrar(String nome,int id,int idade) throws Exception {
        post("/api/clientes",Map.of("nome",nome,"id",id,"idade",idade,"email","cliente"+id+"@example.com"));
        if(ultima.statusCode()==201) clientes.add(mapper.readTree(ultima.body()));
    }
    @Entao("os dois clientes são cadastrados sem conta")
    public void clientesSemConta() { assertEquals(2,clientes.size()); clientes.forEach(c->assertTrue(c.get("contaId").isNull())); }
    @Quando("abro a conta {int} para o cliente {int}")
    public void abrir(int conta,int cliente) throws Exception { post("/api/clientes/"+cliente+"/contas",Map.of("id",conta)); assertEquals(201,ultima.statusCode(),ultima.body()); }
    @Entao("as contas existem vinculadas e com saldo zero")
    public void vinculadas() throws Exception {
        consultar(); assertEquals(101,origem.get("clienteId").asInt()); assertEquals(102,destino.get("clienteId").asInt());
        saldos("0.00","0.00");
        assertEquals(1001,banco.buscarCliente(101).orElseThrow().contaId());
        assertEquals(1002,banco.buscarCliente(102).orElseThrow().contaId());
    }
    @Dado("que a fixture interna define os saldos")
    public void fixture(DataTable tabela) {
        synchronized(banco) {
            for(var linha:tabela.cells().subList(1,tabela.height())) {
                var conta=banco.buscarConta(Integer.parseInt(linha.get(0))).orElseThrow();
                banco.salvarConta(conta.comSaldo(new BigDecimal(linha.get(1))));
            }
        }
    }
    @Quando("consulto as duas contas")
    public void consultar() throws Exception { origem=get(1001); destino=get(1002); }
    @Entao("vejo os saldos {string} e {string}")
    public void saldos(String a,String b) { assertEquals(a,origem.get("saldo").asText()); assertEquals(b,destino.get("saldo").asText()); }
    @Quando("transfiro {string} da conta {int} para a conta {int}")
    public void transferir(String valor,int de,int para) throws Exception { post("/api/transferencias",Map.of("origemId",de,"destinoId",para,"valor",valor)); }
    @Entao("a transferência é confirmada")
    public void confirmada() { assertEquals(200,ultima.statusCode(),ultima.body()); }
    @Entao("a soma dos saldos permanece {string}")
    public void total(String esperado) { assertEquals(new BigDecimal(esperado),new BigDecimal(origem.get("saldo").asText()).add(new BigDecimal(destino.get("saldo").asText()))); }
    @Entao("a API responde {int}")
    public void status(int esperado) { assertEquals(esperado,ultima.statusCode(),ultima.body()); }
    @Entao("existem {int} clientes cadastrados")
    public void quantidade(int esperado) { assertEquals(esperado,banco.quantidadeClientes()); }
    @Dado("que existem duas contas com saldos {string} e {string}")
    public void duasContas(String a,String b) throws Exception {
        cadastrar("Ana",101,25); abrir(1001,101); cadastrar("Bruno",102,30); abrir(1002,102);
        fixture(DataTable.create(List.of(List.of("conta","saldo"),List.of("1001",a),List.of("1002",b))));
    }
    private void post(String caminho,Object corpo) throws Exception {
        ultima=http.send(HttpRequest.newBuilder(URI.create("http://localhost:"+porta+caminho)).timeout(java.time.Duration.ofSeconds(10))
            .header("Content-Type","application/json").POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(corpo))).build(),HttpResponse.BodyHandlers.ofString());
    }
    private JsonNode get(int id) throws Exception {
        var resposta=http.send(HttpRequest.newBuilder(URI.create("http://localhost:"+porta+"/api/contas/"+id)).timeout(java.time.Duration.ofSeconds(10)).GET().build(),HttpResponse.BodyHandlers.ofString());
        assertEquals(200,resposta.statusCode()); return mapper.readTree(resposta.body());
    }
}
