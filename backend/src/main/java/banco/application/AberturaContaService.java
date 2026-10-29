package banco.application;

import banco.domain.*;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class AberturaContaService {
    private final BancoRepository banco;
    public AberturaContaService(BancoRepository banco) { this.banco = banco; }
    public Conta abrir(int clienteId, int contaId) {
        synchronized (banco) {
            Cliente cliente = banco.buscarCliente(clienteId).orElseThrow(() ->
                new RegraNegocioException("CLIENTE_NAO_ENCONTRADO", "Cliente não encontrado."));
            if (banco.buscarConta(contaId).isPresent()) {
                throw new RegraNegocioException("CONTA_DUPLICADA", "Já existe conta com este ID.");
            }
            if (cliente.contaId() != null) {
                throw new RegraNegocioException("CLIENTE_JA_POSSUI_CONTA", "O cliente já possui uma conta.");
            }
            Conta conta = new Conta(contaId, clienteId, new BigDecimal("0.00"), true);
            banco.salvarConta(conta);
            banco.salvarCliente(cliente.vincular(contaId));
            return conta;
        }
    }
}
