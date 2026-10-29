# Evidências da entrega

Execuções locais em 29/10/2026, Windows 11, JDK Temurin 21.0.13+9, Maven 3.9.16, Node 24.21.0 e Cypress 15.5.0 com Electron 138 headless. Os resultados são gerados a partir dos relatórios reais por `scripts/registrar-evidencias.py`.

| Verificação | Resultado |
| --- | --- |
| Suíte Java completa | **110 testes, zero falhas, erros ou ignorados** |
| Cenários Cucumber via HTTP real | **12 cenários aprovados**, incluídos nos 110 testes |
| Snapshot legado | 38 testes preservados dentro da suíte |
| JaCoCo — domínio | **109/109 instruções e 8/8 ramificações** |
| JaCoCo — aplicação/serviços | **233/233 instruções e 14/14 ramificações** |
| PIT — mesmo recorte de negócio | **31 mutantes gerados, 31 eliminados; zero sobreviventes e zero sem cobertura** |
| Cypress | **3 testes aprovados**, fluxo completo, rejeições e mobile |
| Conteúdo do JAR | Sem snapshot legado ou fixture de teste |

Não somar os 12 cenários Cucumber novamente aos 110 testes. Os percentuais do JaCoCo nesta tabela são do domínio e dos serviços; não são cobertura global da aplicação ou do JavaScript. O PIT reportou separadamente 59/60 linhas exercitadas nas classes mutadas. Não há mutante sobrevivente a justificar nesta execução; nenhuma regra bancária foi excluída para melhorar a pontuação.

## Arquivos verificáveis

- [resultados.json](resultados.json): commit de referência, estado da árvore, digest dos fontes com finais de linha normalizados em LF, ambiente, contagens por classe e escopos das métricas.
- [jacoco.xml](jacoco.xml): contadores reais por pacote e classe.
- [mutacoes.xml](mutacoes.xml): cada mutante, operador, localização e teste que o eliminou.
- [cucumber.json](cucumber.json): passos e resultados dos 12 cenários.
- [cypress.xml](cypress.xml): resultados reais dos três testes de navegador.
- [Captura desktop](desktop.png) e [captura mobile](mobile.png): interface real após as operações, inspecionada visualmente.

Os relatórios completos navegáveis ficam em `backend/target/` e são publicados como artefatos pela CI. A pasta guarda uma amostra portátil, sem as propriedades de sistema dos XMLs completos do Surefire. Um novo build pode substituir relatórios locais; esta amostra corresponde à execução registrada no JSON.

## Casos e oráculos

CT-FL01 cadastra Ana e Bruno, abre contas vinculadas com saldo zero, aplica fixture interna 500/100, consulta, transfere 150 e confere 350/250 e total 600. O teste não simula respostas de transferência. Rejeições confirmam os dois saldos; a tela pequena é verificada em 390 × 844. Há testes Java adicionais de limites, duplicidade, vínculo, tipos JSON, contas ausentes/inativas, mesma conta e concorrência.

O teste diferencial compara legado e novo sistema com estado inicial equivalente e registra diferenças intencionais, como vínculo efetivo e erro controlado em conta ausente. Asserções aprovadas não equivalem a uma prova de equivalência para todas as entradas possíveis.

## Ajustes e limitações

Foram corrigidos parâmetros de rota sem nomes de reflexão e uma tabela de fixture Cucumber sem conversor. O primeiro E2E tentou usar o navegador ainda em extração; foi reexecutado após a instalação. Capturas desktop exigiram configurar a resolução real do Electron, distinta do viewport da aplicação. Uma prévia local manteve o JAR aberto no Windows e bloqueou `clean`; a prévia foi encerrada e o build limpo repetido. Nenhum teste válido foi retirado para obter aprovação.

O ciclo TDD começou com erro de compilação por serviços ausentes e passou após implementar 24 casos iniciais; os logs completos intermediários estão em `tmp/`. Revisão acadêmica, autoria individual e apresentação ao professor continuam sendo atividades humanas. O status remoto da CI deve ser conferido no PR; a aprovação local não é apresentada como resultado da CI.
