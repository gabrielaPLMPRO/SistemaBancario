package banco.application;

import banco.domain.*;
import org.springframework.stereotype.Service;

@Service
public class ConsultaContaService {
    private final BancoRepository banco;
    public ConsultaContaService(BancoRepository banco) { this.banco = banco; }
    public Conta consultar(int id) {
        return banco.buscarConta(id).orElseThrow(() ->
            new RegraNegocioException("CONTA_NAO_ENCONTRADA", "Conta não encontrada."));
    }
}
