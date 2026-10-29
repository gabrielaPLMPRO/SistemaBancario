# Evidências da etapa 1

Execução de 29/10/2026, na branch `evolucao/etapa-1-maven`. O commit de referência é `cc1767583ee9b7c1d1e9246c078164a210223c34`; a reorganização e a configuração do build ainda não têm commit próprio.

[resultados-maven.json](resultados-maven.json) foi gerado a partir dos 14 relatórios XML reais do Surefire e da conferência SHA-256 dos fontes movidos. Contém versões, contagem por classe, totais, hash do JAR e os 22 hashes de movimentação.

| Verificação | Resultado |
| --- | --- |
| Maven `clean verify`, JDK 21 | BUILD SUCCESS |
| Suíte via Surefire | 38 testes; 0 falhas; 0 erros; 0 ignorados |
| Descoberta | 14 classes, incluindo os 12 RFxx |
| Movimentação | 22 arquivos com conteúdo preservado |
| JAR | 8 classes de produção; nenhuma classe de teste ou JUnit incluído |
| Executor JUnitCore após movimentação | 38 testes aprovados, com alvo Java 8 |
| Entrada do JAR | Console iniciou e encerrou com opção 9 |

O JSON registra a execução Maven encerrada às 18:20:25 (-03:00). Os relatórios completos dessa execução estão em `backend/target/surefire-reports/` e o log em `tmp/maven-verify.log`, ambos locais e ignorados pelo Git. Uma nova execução pode substituir esses arquivos locais.

Não foram medidas cobertura ou mutação. Não foram executados Cucumber ou Cypress. Não foi aprovada a revisão humana das regras. O teste do JAR valida apenas início e encerramento do console.

O procedimento completo, as falhas intermediárias e os comandos estão no [README da etapa 1](../../docs/etapa-1/README.md). A ligação ao roteiro e aos artigos está no [registro metodológico](../../docs/etapa-1/fontes-e-rastreabilidade.md).
