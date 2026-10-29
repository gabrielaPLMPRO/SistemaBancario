package banco.api;

import banco.BancoApplication;
import banco.infra.BancoEmMemoria;
import banco.domain.Conta;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes=BancoApplication.class) @AutoConfigureMockMvc
class ApiTest {
    @Autowired MockMvc http;
    @Autowired BancoEmMemoria banco;
    @BeforeEach void limpar() { banco.limpar(); }
    @Test void cadastrarAbrirConsultar() throws Exception {
        cliente();
        http.perform(post("/api/clientes/101/contas").contentType("application/json").content("{\"id\":1001}"))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.clienteId").value(101)).andExpect(jsonPath("$.saldo").value("0.00"));
        assertEquals(1001,banco.buscarCliente(101).orElseThrow().contaId());
        http.perform(get("/api/contas/1001")).andExpect(status().isOk()).andExpect(jsonPath("$.ativa").value(true));
    }
    @Test void duplicadoEIdadeInvalidaPreservamEstado() throws Exception {
        cliente();
        http.perform(post("/api/clientes").contentType("application/json").content(jsonCliente(17)))
            .andExpect(status().isConflict()).andExpect(jsonPath("$.codigo").value("CLIENTE_DUPLICADO"));
        assertEquals(25,banco.buscarCliente(101).orElseThrow().idade());
        banco.limpar();
        http.perform(post("/api/clientes").contentType("application/json").content(jsonCliente(66)))
            .andExpect(status().isUnprocessableEntity()).andExpect(jsonPath("$.codigo").value("IDADE_INVALIDA"));
        assertEquals(0,banco.quantidadeClientes());
    }
    @Test void ausenciasSao404() throws Exception {
        http.perform(get("/api/contas/999")).andExpect(status().isNotFound());
        http.perform(post("/api/clientes/999/contas").contentType("application/json").content("{\"id\":1001}"))
            .andExpect(status().isNotFound());
        http.perform(post("/api/transferencias").contentType("application/json").content("{\"origemId\":1,\"destinoId\":2,\"valor\":\"1.00\"}"))
            .andExpect(status().isNotFound());
        assertTrue(banco.buscarConta(1001).isEmpty());
    }
    @ParameterizedTest @ValueSource(strings={"{}","{","{\"id\":101,\"nome\":\"\",\"idade\":25,\"email\":\"x\"}","{\"id\":101,\"nome\":\"Ana\",\"idade\":null,\"email\":\"a@example.com\"}","{\"id\":101,\"nome\":\"Ana\",\"idade\":25,\"email\":\"a@example.com\",\"saldo\":500}"})
    void entradaInvalidaNaoInsere(String json) throws Exception {
        http.perform(post("/api/clientes").contentType("application/json").content(json))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.codigo").value("ENTRADA_INVALIDA"));
        assertEquals(0,banco.quantidadeClientes());
    }
    @ParameterizedTest @ValueSource(strings={"abc","0","-1"})
    void parametroInvalidoTemRespostaControlada(String id) throws Exception {
        http.perform(get("/api/contas/"+id)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.codigo").value("ENTRADA_INVALIDA"));
    }
    @Test void transferenciaRetornaStringsEDoisSaldos() throws Exception {
        saldos();
        transferir("150.00").andExpect(status().isOk()).andExpect(jsonPath("$.saldoOrigem").value("350.00"))
            .andExpect(jsonPath("$.saldoDestino").value("250.00"));
        assertEquals(new BigDecimal("350.00"),banco.buscarConta(1001).orElseThrow().saldo());
    }
    @ParameterizedTest @ValueSource(strings={"0","-1","0.001","501.00"})
    void transferenciaRejeitadaNaoMove(String valor) throws Exception {
        saldos(); transferir(valor).andExpect(status().isUnprocessableEntity());
        assertEquals(new BigDecimal("500.00"),banco.buscarConta(1001).orElseThrow().saldo());
        assertEquals(new BigDecimal("100.00"),banco.buscarConta(1002).orElseThrow().saldo());
    }
    @Test void textoMonetarioInvalidoTem400() throws Exception {
        saldos(); transferir("NaN").andExpect(status().isBadRequest());
        assertEquals(new BigDecimal("500.00"),banco.buscarConta(1001).orElseThrow().saldo());
    }
    @Test void fixtureNaoExisteEmProducao() throws Exception {
        http.perform(post("/api/test/seed").contentType("application/json").content("{}" )).andExpect(status().isNotFound());
    }
    @ParameterizedTest @ValueSource(strings={"{\"id\":101.5,\"nome\":\"Ana\",\"idade\":25,\"email\":\"a@example.com\"}","{\"id\":\"101\",\"nome\":\"Ana\",\"idade\":25,\"email\":\"a@example.com\"}"})
    void naoConverteTiposDeIdSilenciosamente(String json) throws Exception {
        http.perform(post("/api/clientes").contentType("application/json").content(json)).andExpect(status().isBadRequest());
        assertEquals(0,banco.quantidadeClientes());
    }
    @Test void dinheiroNoJsonPrecisaSerString() throws Exception {
        saldos();
        http.perform(post("/api/transferencias").contentType("application/json").content("{\"origemId\":1001,\"destinoId\":1002,\"valor\":1.25}"))
            .andExpect(status().isBadRequest());
        assertEquals(new BigDecimal("500.00"),banco.buscarConta(1001).orElseThrow().saldo());
    }
    void cliente() throws Exception {
        http.perform(post("/api/clientes").contentType("application/json").content(jsonCliente(25)))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.ativo").value(true)).andExpect(jsonPath("$.contaId").isEmpty());
    }
    String jsonCliente(int idade) { return "{\"id\":101,\"nome\":\"Ana\",\"idade\":"+idade+",\"email\":\"ana@example.com\"}"; }
    void saldos() { banco.salvarConta(new Conta(1001,101,new BigDecimal("500.00"),true)); banco.salvarConta(new Conta(1002,102,new BigDecimal("100.00"),true)); }
    org.springframework.test.web.servlet.ResultActions transferir(String valor) throws Exception {
        return http.perform(post("/api/transferencias").contentType("application/json").content("{\"origemId\":1001,\"destinoId\":1002,\"valor\":\""+valor+"\"}"));
    }
}
