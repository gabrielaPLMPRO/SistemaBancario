package banco.domain;

public record Cliente(int id, String nome, int idade, String email, boolean ativo, Integer contaId) {
    public Cliente vincular(int conta) {
        return new Cliente(id, nome, idade, email, ativo, conta);
    }
}
