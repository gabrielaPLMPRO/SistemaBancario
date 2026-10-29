package banco.api;

import banco.application.*;
import banco.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api")
public class BancoController {
    private final CadastroClienteService cadastro;
    private final AberturaContaService abertura;
    private final ConsultaContaService consulta;
    private final TransferenciaService transferencia;
    public BancoController(CadastroClienteService cadastro, AberturaContaService abertura,
                           ConsultaContaService consulta, TransferenciaService transferencia) {
        this.cadastro=cadastro; this.abertura=abertura; this.consulta=consulta; this.transferencia=transferencia;
    }
    public record NovoCliente(@NotNull @Positive Integer id, @NotBlank @Size(max=120) String nome,
                              @NotNull Integer idade, @NotBlank @Email @Size(max=254) String email) { }
    public record NovaConta(@NotNull @Positive Integer id) { }
    public record NovaTransferencia(@NotNull @Positive Integer origemId, @NotNull @Positive Integer destinoId,
                                     @NotBlank @Size(max=80) @Pattern(regexp="[+-]?[0-9]+(?:\\.[0-9]+)?") String valor) { }
    public record ContaResposta(int id, int clienteId, String saldo, boolean ativa) {
        static ContaResposta de(Conta conta) { return new ContaResposta(conta.id(),conta.clienteId(),conta.saldo().toPlainString(),conta.ativa()); }
    }
    public record TransferenciaResposta(int origemId, int destinoId, String saldoOrigem, String saldoDestino) { }

    @PostMapping("/clientes")
    public ResponseEntity<Cliente> cadastrar(@Valid @RequestBody NovoCliente entrada) {
        Cliente cliente=cadastro.cadastrar(entrada.id(),entrada.nome().trim(),entrada.idade(),entrada.email());
        return ResponseEntity.status(201).body(cliente);
    }
    @PostMapping("/clientes/{id}/contas")
    public ResponseEntity<ContaResposta> abrir(@PathVariable("id") @Positive int id, @Valid @RequestBody NovaConta entrada) {
        Conta conta=abertura.abrir(id,entrada.id());
        return ResponseEntity.created(URI.create("/api/contas/"+conta.id())).body(ContaResposta.de(conta));
    }
    @GetMapping("/contas/{id}")
    public ContaResposta consultar(@PathVariable("id") @Positive int id) { return ContaResposta.de(consulta.consultar(id)); }
    @PostMapping("/transferencias")
    public TransferenciaResposta transferir(@Valid @RequestBody NovaTransferencia entrada) {
        var resultado=transferencia.transferir(entrada.origemId(),entrada.destinoId(),new BigDecimal(entrada.valor()));
        return new TransferenciaResposta(resultado.origem().id(),resultado.destino().id(),
                resultado.origem().saldo().toPlainString(),resultado.destino().saldo().toPlainString());
    }
}
