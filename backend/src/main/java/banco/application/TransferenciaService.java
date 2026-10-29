package banco.application;

import banco.domain.*;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class TransferenciaService {
    private final BancoRepository banco;
    public TransferenciaService(BancoRepository banco) { this.banco = banco; }
    public record Resultado(Conta origem, Conta destino) { }
    public Resultado transferir(int origemId, int destinoId, BigDecimal valor) {
        synchronized (banco) {
            ConsultaContaService consulta = new ConsultaContaService(banco);
            Conta origem = consulta.consultar(origemId);
            Conta destino = consulta.consultar(destinoId);
            BigDecimal quantia = PoliticasBancarias.valorMonetario(valor);
            if (origemId == destinoId) {
                throw new RegraNegocioException("MESMA_CONTA", "Escolha contas de origem e destino diferentes.");
            }
            if (!origem.ativa() || !destino.ativa()) {
                throw new RegraNegocioException("CONTA_INATIVA", "As duas contas precisam estar ativas.");
            }
            if (origem.saldo().compareTo(quantia) < 0) {
                throw new RegraNegocioException("SALDO_INSUFICIENTE", "Saldo insuficiente para a transferência.");
            }
            Conta novaOrigem = origem.comSaldo(origem.saldo().subtract(quantia));
            Conta novoDestino = destino.comSaldo(destino.saldo().add(quantia));
            banco.salvarConta(novaOrigem);
            banco.salvarConta(novoDestino);
            return new Resultado(novaOrigem, novoDestino);
        }
    }
}
