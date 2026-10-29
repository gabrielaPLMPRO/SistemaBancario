# Ponto de retomada — finalização da entrega

Trabalho retomado pelo usuário em 29/10/2026 a partir deste arquivo. O usuário já autorizou todas as etapas do roteiro, sem confirmações adicionais; essa autorização permanece válida. O registro abaixo conserva o contexto da pausa e identifica o avanço da retomada.

## Projeto e direção

- Workspace: C:/Users/guiba/Documents/SistemaBancario.
- Base obrigatória: roteiro e artigos da pasta contexto para IA, especialmente refatoracao de codigo llm - processo.pdf. Ler docs/README.md e os READMEs das etapas 1 a 6.
- Branch atual: evolucao/fluxo-bancario-web.
- Commit local principal: 49ffbf014b0da4c255cd1f5645e354b46ef653bf — Implementa fluxo bancario Web com BDD e testes de qualidade.
- Não houve push, PR, merge ou tags de entrega. O último comando que faria o segundo commit foi interrompido antes de executá-lo: conferir git status/log ao retomar.

## Implementado e validado

- Backend Spring Boot 3.5.16 com JDK 21; domínio imutável, BigDecimal, serviços, repositório em memória, API REST e validação JSON estrita.
- Frontend separado em frontend/, servido como recurso estático; cadastro, abertura, consulta e transferência.
- Legado inteiro somente em backend/src/test/java/negocio/, com seus 38 testes preservados.
- 110 testes Java aprovados, incluindo 12 cenários Cucumber por HTTP real, testes de API, concorrência e comparação diferencial.
- JaCoCo: domínio 109/109 instruções, 8/8 ramos; aplicação 233/233 instruções, 14/14 ramos.
- PIT: 31 mutantes, todos eliminados; nenhum sobrevivente ou sem cobertura.
- Cypress: 3 cenários aprovados — fluxo principal, rejeições e tela pequena.
- Checkout limpo em tmp/verificacao-checkout: 110 testes e 3 Cypress aprovados com o primeiro conjunto de dependências.
- Artefato reproduzido byte a byte após normalizar o POM local em LF: JAR principal e JAR desse checkout ficaram idênticos.
- Documentação, OpenAPI, workflow CI, apresentação, uso de IA e contribuição dos alunos preparados.

## Ajustes finais na retomada

O npm audit do checkout limpo encontrou seis alertas nas dependências do Cypress 15.5.0. Foi atualizado para 15.21.1, sem alertas na nova auditoria. Os três testes passaram também nessa versão; e2e/results/resumo.json registra versão real do Cypress e navegador.

Depois dessa aprovação foram acrescentadas pequenas configurações finais ao teste:

- allowCypressEnv: false;
- trashAssetsBeforeRuns: false para evitar falha do utilitário de lixeira no Windows;
- overwrite: true nas capturas, evitando nomes com (1) e evidências antigas;
- executor registra resumo.json;
- registrador de evidências lê versões reais e pode validar --checkout.

A tentativa anterior foi interrompida antes do segundo commit. Na retomada, o E2E com todas as configurações finais foi concluído: 3 testes aprovados, zero falhas ou ignorados, Cypress 15.21.1 / Electron 138.0.7204.251. Relatório de 29/10/2026 às 19:51 (-03:00); capturas desktop e mobile inspecionadas e evidências regeneradas. O registrador também confere a versão e as contagens reais do resumo do navegador, inclusive no checkout de reprodução.

Arquivos modificados incluem docs, e2e/package*.json, configuração/spec Cypress, scripts e evidências. backend/pom.xml pode aparecer como alteração de finais de linha sem diff semântico. Não descartar mudanças.

## Pendências para concluir

1. Conferir processos e git status/log.
2. Executar E2E com as configurações finais, inspecionar capturas e regenerar evidências com scripts/registrar-evidencias.py.
3. Commitar a atualização corretiva e documentação. Atualizar o checkout limpo via git pull --ff-only, npm ci --ignore-scripts e repetir a validação com Cypress 15.21.1. Para evitar colisão se houver testes simultâneos, usar BANCARIO_E2E_PORT diferente (ex.: 18082).
4. Atualizar evidências para o commit correto com --checkout tmp/verificacao-checkout. O digest cobre fontes com finais LF. Não afirmar aprovação remota da CI antes de executá-la.
5. Conferir links, git diff --check e estado final. Criar referências de baseline/entrega se apropriado.
6. Enviar branch ao origin e abrir PR para main pelo conector GitHub. Permissão de push foi confirmada para gabrielaPLMPRO/SistemaBancario, mas o envio ainda não foi tentado. Não fazer merge automático.
7. Conferir CI remota e corrigir falhas reais; registrar status e link de PR no README de entrega.
8. Informar ao usuário resultados e limitações. Autores/matrículas, revisão acadêmica e apresentação ao professor são atividades humanas não realizadas pelo agente.

## Ambiente e comandos úteis

JDK portátil: tmp/ferramentas/jdk21/jdk-21.0.13+9.
Maven Wrapper 3.9.16/3.3.4 na raiz. Node 24.21.0.
Caches locais usados: MAVEN_USER_HOME em tmp/maven-home; repositório Maven em $env:TEMP/SistemaBancario-codex/maven-repository; Cypress em $env:TEMP/SistemaBancario-codex/Cypress.

```powershell
$env:JAVA_HOME = (Resolve-Path 'tmp/ferramentas/jdk21/jdk-21.0.13+9').Path
$env:MAVEN_USER_HOME = Join-Path (Get-Location) 'tmp/maven-home'
$env:CYPRESS_CACHE_FOLDER = Join-Path $env:TEMP 'SistemaBancario-codex/Cypress'
$repo = Join-Path $env:TEMP 'SistemaBancario-codex/maven-repository'
./mvnw.cmd -B -ntp "-Dmaven.repo.local=$repo" verify dependency:build-classpath '-Dmdep.outputFile=target/e2e-classpath.txt' '-Dmdep.includeScope=test'
node scripts/executar-e2e.mjs
./mvnw.cmd -f backend/pom.xml -B -ntp "-Dmaven.repo.local=$repo" org.pitest:pitest-maven:mutationCoverage
```

Os comandos Java/rede precisaram rodar fora do sandbox; dentro dele houve bloqueios. Propriedades Maven com ponto devem estar entre aspas no PowerShell. Não iniciar servidor sobre o JAR em target antes de clean: o Windows mantém o arquivo aberto. A prévia PID 28556 foi encerrada; os executores E2E encerram seus próprios servidores.

Logs reais estão em tmp/validacao-final.log, tmp/pit-final.log, tmp/e2e-final.log e tmp/checkout-*.log. Relatórios completos em backend/target, resultados de navegador em e2e/results e e2e/screenshots. Evidências versionadas em evidencias/entrega ainda precisam da atualização final de versão/commit/capturas.

## Retomar a conversa

Pode pedir: “Continue a partir de docs/RETOMADA.md, seguindo o roteiro da pasta contexto para IA”. Esse arquivo é um resumo de contexto e não uma cópia integral da conversa.
