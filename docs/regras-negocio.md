# Regras de negócio extraídas

Data: 29/10/2026. As RN abaixo descrevem o legado extraído e caracterizado; a revisão acadêmica continua pendente. O snapshot completo, com métodos e testes antigos, está em `backend/src/test/java/negocio/`. Os nomes relativos de testes abaixo referem-se a essa pasta. A produção nova está em `backend/src/main/java/banco/` e implementa essas regras mais as evoluções explícitas de `docs/decisoes.md`. A extração inicial foi feita antes das movimentações, em `src/negocio/`.

| ID | RF | Comportamento observado | Origem | Evidência automatizada |
| --- | --- | --- | --- | --- |
| RN01 | RF02 | Cadastro verifica ID duplicado antes da idade e insere somente após ambas as validações | `GerenciadoraClientes.cadastraCliente`, `validaClienteNaoDuplicado` | `TDD/RF02`, `Testes`, `CaracterizacaoLegadoTest.cadastroDuplicadoPreservaClienteOriginal` |
| RN02 | RF03 | Idade entre 18 e 65 inclusive; fora do intervalo lança IdadeNaoPermitidaException | `GerenciadoraClientes.validaIdade` | `TDD/RF03`; quatro testes de fronteira em `TDD/CaracterizacaoLegadoTest` |
| RN03 | RF09 | Exige cliente existente e ID de conta sem duplicidade; não insere em rejeição | `GerenciadoraContas.cadastraContaParaCliente`, `validaContaNaoDuplicada` | `TDD/RF09`, `Testes`, `CaracterizacaoLegadoTest.contaDuplicadaPreservaContaOriginal` |
| RN04 | RF08 | Consulta retorna conta por ID ou null quando ausente | `GerenciadoraContas.pesquisaConta` | `BDD/RF08` |
| RN05 | RF12 | Com contas presentes, aceita valor maior que zero se saldo suficiente; credita destino e debita origem | `GerenciadoraContas.transfereValor` | `TDD/RF12`, `Testes.TransferenciaEntreContas`, dois testes de sucesso em `TDD/CaracterizacaoLegadoTest` |

## Limites da interpretação

A existência de um cliente é verificada antes da duplicidade da conta em RN03. A associação persistente entre conta e cliente não é garantida pelo método. A mensagem de idade existente deve manter os acentos após a conversão de codificação.

RN05 não estabelece restrições para conta inativa, mesma conta, fração de centavo ou saldo não finito. Essas entradas exigem decisões e testes específicos na evolução. O caso de conta ausente não tem erro de negócio controlado hoje.

## Critérios de preservação para o próximo incremento

Cadastro rejeitado não deve substituir ou inserir objetos; transferência rejeitada por valor zero, negativo ou saldo insuficiente mantém os dois saldos. Transferência aceita deve conferir os dois saldos e conservar a soma. Não basta verificar um booleano ou, futuramente, apenas o status HTTP.

Os testes de ativação, situação e remoção, embora fora do recorte principal, seguem na regressão. Não foram calculados percentuais de cobertura.
