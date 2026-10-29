package banco.domain;

import java.math.BigDecimal;

public record Conta(int id, int clienteId, BigDecimal saldo, boolean ativa) {
    public Conta comSaldo(BigDecimal novoSaldo) {
        return new Conta(id, clienteId, novoSaldo, ativa);
    }
}
