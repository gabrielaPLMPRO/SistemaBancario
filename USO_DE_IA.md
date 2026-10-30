# Registro de uso de IA

## 29/10/2026 — Diagnóstico e caracterização inicial

- Ferramenta: Codex (OpenAI); identificador exato do modelo não registrado.
- Solicitação: iniciar a implementação seguindo o roteiro local e descrever o processo no README da etapa 1.
- Apoio realizado: leitura do código e contexto, inventário, rastreabilidade de regras, script de testes, testes de caracterização, normalização de codificação e documentação.
- Validação automatizada: 30 testes originais aprovados; depois, 38 testes aprovados em 14 classes com fontes em UTF-8.
- Dificuldades: fontes com codificações distintas; bloqueio do compilador pelo sandbox, resolvido com execução autorizada fora dele; bibliotecas de leitura de PDF indisponíveis, sendo consultado o script gerador do roteiro.
- Revisão humana: pendente. Não foi atribuída aprovação das regras ou autoria individual dos alunos. As pessoas responsáveis e suas contribuições devem ser preenchidas pelo grupo.
- Evidências e limites: [README da etapa 1](docs/etapa-1/README.md).

## 29/10/2026 — Build Maven e consulta direta às fontes

- Solicitação: prosseguir com a preparação do build e manter toda a implementação orientada pelo roteiro, pelo compilado de práticas com LLM e pelos artigos da pasta de contexto.
- Apoio realizado: movimentação de 22 fontes com hashes preservados, POMs, Wrapper oficial, preparação portátil de JDK 21, configuração de descoberta dos RFxx, atualização do script de regressão e dos READMEs.
- Fontes: consulta direta ao texto dos PDFs do roteiro e do compilado LLM, além de seções pertinentes de iTest Learning, Oliveira e Campos. A [rastreabilidade](docs/etapa-1/fontes-e-rastreabilidade.md) identifica os trechos e os limites; não se declara nova leitura integral dos demais artigos.
- Documentação oficial: utilizada para configuração de Maven, Surefire, Compiler e JDK; a metodologia continua sendo a definida nos materiais locais.
- Verificações: Maven `clean verify` com JDK 21: 38 testes em 14 classes, zero falhas/erros/ignorados; JUnitCore após movimentação: 38 aprovados; hashes dos 22 fontes preservados; JAR com 8 classes de produção e entrada de console iniciada/encerrada.
- Dificuldades e ajustes: rede restrita, criação de pastas do cache, checksum de Wrapper indisponível em SHA-512 e redirecionamento de stderr no PowerShell, documentados no README da etapa 1.
- Revisão humana: continua pendente para as regras e políticas em aberto. Não foram inventados nomes de autores, aprovações ou resultados de cobertura/mutação.

## 29/10/2026 — Execução das etapas restantes, autorizada pelo usuário

- Solicitação explícita: implementar até a última etapa sem pedir novas autorizações. O agente assumiu e documentou as políticas necessárias, preservando como pendente a comprovação de revisão acadêmica pelos alunos.
- Trabalho apoiado: especificação Gherkin e OpenAPI antes dos serviços; ciclo RED/GREEN; domínio imutável e serviços em memória; API Spring Boot; frontend separado; testes HTTP, concorrência, comparação diferencial, Cucumber, Cypress, JaCoCo e PIT; CI, documentação, evidências e preparação da entrega GitHub.
- Fontes locais: roteiro e compilado LLM como base; seções pertinentes de Oliveira, Campos, iTest Learning, Costa, Borowski e GameTest. As fontes de jogos educacionais foram tratadas como apoio de planejamento e avaliação, sem alegar resultados educacionais para este sistema.
- Resultados executados: testes e relatórios reais em `evidencias/entrega/`. O primeiro teste de serviços falhou por classes ausentes; 24 testes iniciais passaram após a implementação. Falhas de integração e ambiente foram investigadas e corrigidas sem retirar cenários válidos.
- Revisões automáticas: oráculos verificam aceitação/rejeição e estado, incluindo os dois saldos e vínculos; escopos de cobertura e mutação são explicitados. Capturas reais do Cypress foram inspecionadas e a captura desktop foi ajustada após corte visual.
- Limites: o agente não atribuiu nomes, matrículas, aprovação humana, apresentação ao professor ou satisfação de usuários. Não declara equivalência completa com o legado fora dos casos comparados.

## 29/10/2026 — Retomada e verificação final

- Solicitação: continuar a partir de `docs/RETOMADA.md`, mantendo a autorização anterior para as etapas restantes.
- Apoio realizado: atualização corretiva do Cypress para 15.21.1, registro das versões reais do navegador, conferência de evidências e reprodução do commit `cddd0de` em checkout limpo.
- Validação: 110 testes Java e 3 Cypress aprovados na reprodução; zero alertas npm nessa instalação; fontes conferidos e JAR idêntico byte a byte. Capturas desktop e mobile inspecionadas; links locais conferidos.
- Publicação: branch e PR preparados para revisão, com status remoto registrado nas evidências somente após consulta à execução real. Revisão acadêmica e autoria individual continuam pendentes de preenchimento pelos alunos.

## 29/10/2026 — Relatório de testes, ferramentas e análise de tempo

- Solicitação: criar `docs/relatorios.md` com contagem de testes, ferramentas e comparação de tempo com fontes confiáveis da internet.
- Apoio realizado: conferência das evidências locais e consulta a fontes primárias de Peng et al., Cui et al./Management Science e METR, incluindo a atualização de fevereiro de 2026.
- Validação: contagens de 110 testes Java e 3 Cypress conferidas no JSON; cálculos dos cenários recalculados; links locais verificados. Não houve nova execução da suíte, pois a alteração é documental.
- Limite: a economia de tempo específica deste projeto não foi medida. Os resultados externos são identificados como resultados de outros estudos; a base de 40 horas e os tempos derivados são simulações, sem atribuição de ganho comprovado à entrega. Revisão acadêmica pelos alunos permanece pendente.
