# Etapa 5 — Regressão, comparação e qualidade

Base: roteiro, páginas 9 a 11; compilado LLM, páginas 2 e 6; Oliveira, testes de fronteira, oráculos e métricas complementares. As metas do roteiro foram adotadas: mínimo de 90% de instruções e ramificações nos pacotes de domínio e aplicação; mínimo de 80% de mutação no mesmo recorte.

## Comparação diferencial

`ComparacaoLegadoTest` executa legado e versão nova com entradas e estado inicial equivalentes. Compara aceitação e cadastro nas idades 17/18/65/66; transferência 150, zero, negativa, acima do saldo e todo o saldo; consulta ausente e correção do erro por conta ausente. Booleanos e exceções são normalizados para aceitação/rejeição e os dois saldos são comparados numericamente.

| Caso | Legado | Nova versão | Classificação |
| --- | --- | --- | --- |
| Idades 17/66 | Rejeita | Rejeita | Preservação |
| Idades 18/65 | Aceita | Aceita | Preservação |
| Transferir 150 de 500 para 100 | 350/250 | 350/250 | Preservação |
| Zero, negativo ou insuficiente | Rejeita, saldos preservados | Rejeita, saldos preservados | Preservação |
| Transferir 500 de 500 para 100 | 0/600 | 0/600 | Preservação |
| Consulta ausente | null | Erro de domínio, HTTP 404 na borda | Adaptação |
| Transferência com conta ausente | NullPointerException | Erro controlado, HTTP 404 | Correção intencional |
| Abertura de conta | Verifica cliente, mas não efetiva vínculo | Efetiva vínculo e limita a uma conta | Evolução decidida |
| Dinheiro | double | BigDecimal, fração de centavo rejeitada | Evolução decidida |

A tabela se apoia nos testes indicados. Não se afirma equivalência em entradas fora desse conjunto nem que as políticas novas existiam no legado.

## Cobertura

JaCoCo instrumenta o backend. As verificações obrigatórias do build se aplicam especificamente a `banco.application` e `banco.domain`. O relatório também mostra API e infraestrutura separadamente. Não se soma cobertura de Java e JavaScript nem se apresenta cobertura do domínio como cobertura do produto inteiro.

## Mutação

PIT tem como alvo as classes `banco.domain.*` e `banco.application.*` e executa os testes unitários desse recorte. Não foram excluídos métodos de regra bancária para elevar o resultado. O relatório XML permite conferir cada mutante, sua localização, operador e teste que o matou. Se houver sobreviventes ou mutantes sem cobertura em uma execução futura, devem ser investigados e justificados antes de concluir a entrega.

Primeira execução real: 31 mutantes gerados e 31 eliminados, nenhum sem cobertura, escore 100%. Não há sobreviventes para classificar como equivalentes nessa execução. A cobertura de linhas reportada pelo próprio PIT foi 59/60; ela é distinta das métricas de instruções e ramificações do JaCoCo. A aprovação não implica ausência de todos os defeitos possíveis.

## Reprodução

```powershell
./mvnw.cmd clean verify
./mvnw.cmd -f backend/pom.xml test-compile org.pitest:pitest-maven:mutationCoverage
```

Relatórios: `backend/target/site/jacoco/index.html`, `backend/target/pit-reports/index.html`, `backend/target/cucumber/relatorio.html`, `backend/target/surefire-reports/`. Resultados finais e amostras portáveis ficam em `evidencias/entrega/`.

Asserções verificam valores retornados e estado final, incluindo os dois saldos. Há testes de concorrência para gasto duplo e abertura simultânea; eles exercitam o modelo em memória, sem demonstrar garantias entre múltiplas instâncias do servidor.
