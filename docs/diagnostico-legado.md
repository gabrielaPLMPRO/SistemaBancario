# Diagnóstico do legado

Data: 29/10/2026. Referência inicial: `cc1767583ee9b7c1d1e9246c078164a210223c34`. Processo e execuções: [README da etapa 1](etapa-1/README.md).

O inventário abaixo descreve a estrutura encontrada no diagnóstico. Primeiro os fontes foram separados na estrutura Maven; depois o snapshot inteiro do legado passou para `backend/src/test/java/negocio/`, para regressão e comparação. A produção nova está em `backend/src/main/java/banco/`, com evoluções explícitas registradas em `docs/decisoes.md`. Os problemas diagnosticados continuam presentes no snapshot histórico, não são apresentados como regras da versão nova.

## Estrutura e responsabilidades

| Componente | Responsabilidade e estado |
| --- | --- |
| `src/negocio/Main.java` | Console com Scanner, menu, dados iniciais e orquestração de cadastro |
| `Cliente` | ID, nome, idade, email, situação e ID de conta; campos mutáveis |
| `ContaCorrente` | ID, saldo em double e situação; campos mutáveis |
| `GerenciadoraClientes` | Lista em memória, busca, cadastro, validação, ativação e remoção |
| `GerenciadoraContas` | Lista em memória, busca, cadastro, remoção e transferência |
| Exceções | ID de cliente duplicado, ID de conta duplicado e idade inválida |
| `negocio.Testes`, `BDD`, `DDD`, `TDD` | Testes JUnit 4 colocados na mesma árvore dos fontes de produção |

Não existe armazenamento durável. O construtor das gerenciadoras recebe listas e os getters devolvem as próprias listas, permitindo alteração externa. Inserções diretas e setters também permitem contornar validações.

## Entradas, saídas e fluxos do recorte

| Operação | Entrada | Saída e efeito |
| --- | --- | --- |
| `cadastraCliente` | Objeto Cliente | Retorno void; valida duplicidade e idade antes de inserir; lança exceção em rejeição |
| `validaIdade` | int | true entre 18 e 65; exceção fora do intervalo |
| `cadastraContaParaCliente` | Conta, ID do cliente e gerenciadora de clientes | false se cliente ausente; exceção por conta duplicada; true após inserir |
| `pesquisaConta` | ID | Primeiro objeto com esse ID ou null, sem alteração |
| `transfereValor` | Origem, valor double e destino | true após crédito/débito; false para valor não positivo ou saldo insuficiente; pode lançar NullPointerException com conta ausente |

O menu cadastra cliente e conta juntos, chamando validadores e métodos de inserção diretamente. Os testes de cadastro usam `cadastraCliente` e `cadastraContaParaCliente`. Portanto, menu e testes não exercitam uma única orquestração.

## Diferenças e lacunas encontradas por leitura

- A idade máxima de 65 existe no código e na mensagem da exceção, mas o README destaca apenas a mínima. Preservada e coberta com fronteiras.
- `cadastraContaParaCliente` verifica a existência do cliente, mas não atualiza `Cliente.idContaCorrente`. O teste RF09 já fornece o vínculo preenchido e não detecta essa lacuna.
- `transfereValor` não valida a existência das contas antes de acessar seus saldos. O destino é creditado antes do débito da origem.
- Não há proteção de concorrência ou transação na transferência. O dinheiro usa double.
- Não há restrição explícita de contas inativas ou de transferência para a mesma conta; essas decisões não serão inventadas como regras do legado.
- O `case 8` do menu não tem break, passando ao `case 9` e encerrando o sistema mesmo quando a conta não é removida.
- A entrada do valor usa `Double.parseDouble` sem tratamento; entrada inválida pode interromper o console.
- As listas e setters expostos enfraquecem invariantes de unicidade e vínculo.
- O `.gitignore` tinha marcadores de conflito antigos. Foram removidos na preparação.

Essas constatações são evidências de leitura; os defeitos acima não foram corrigidos neste incremento. Não há alegação de teste de console ou concorrência. Os testes executados e seus limites estão no README da etapa 1.
