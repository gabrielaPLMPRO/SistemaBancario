# Etapa 4 — Frontend e fluxo no navegador

Base: roteiro local, páginas 5, 6 e 10; compilado LLM, páginas 2 e 5; Campos, cenários e automação de interface; Costa, organização gradual dos níveis de teste; Borowski, necessidade de estabilidade e qualidade além de simplesmente automatizar.

## Interface e comunicação

Os arquivos em `frontend/` são HTML, CSS e JavaScript sem framework ou dependência externa de fontes. Há formulários de cadastro, abertura, consulta e transferência, feedback por operação, cards de contas e layout responsivo. O Maven copia esses arquivos para os recursos estáticos do backend. A separação do código-fonte é preservada e o acesso ao negócio ocorre por HTTP/JSON.

`apiClient.js` centraliza fetch e erros. O navegador não calcula novos saldos; após a transferência, consulta o servidor. A formatação monetária é apenas visual. Se a confirmação da transferência chegar mas a consulta posterior falhar, a interface informa que a transferência foi confirmada e pede nova consulta, evitando induzir uma repetição da movimentação.

Campos possuem rótulos, foco visível e feedback com `role=status`. Dados retornados são renderizados com textContent, sem interpolação HTML. Botões ficam desabilitados enquanto a requisição está em andamento. A validação visual não substitui a validação no backend.

## Fixture sem rota pública

`banco.e2e.E2eApplication` e sua configuração ficam exclusivamente na árvore de testes. O executor Node inicia essa entrada de testes, espera a aplicação ficar pronta e só depois executa Cypress. Não inicia servidor por cy.task.

`cy.task('fixture', ...)` comunica-se por arquivos em uma pasta temporária exclusiva com um bean do processo Java de testes. Permite limpar o ambiente e atribuir saldos controlados após abertura das contas. Não existe botão de depósito, endpoint público de seed ou rota de ajuste de saldo. A entrada de testes e o bean da fixture não são incluídos no JAR de produção.

## Cenários implementados

1. CT-FL01: cadastrar Ana e Bruno, abrir suas contas com saldo zero e vínculos corretos, aplicar fixture 500/100, consultar, transferir 150, conferir 350/250 e total 600.
2. Rejeições pela interface: cliente duplicado e saldo insuficiente, com consulta dos saldos preservados.
3. Tela pequena: cadastro, abertura e consulta em 390 × 844, sem rolagem horizontal.

Os intercepts observam requisições reais; não substituem respostas da API. Screenshots desktop e mobile são produzidos pelos testes e inspecionados antes da entrega. Os resultados reais finais estão nas evidências da etapa 5; a existência dos scripts, isoladamente, não significa aprovação.

## Reprodução

```powershell
./mvnw.cmd verify dependency:build-classpath '-Dmdep.outputFile=target/e2e-classpath.txt' '-Dmdep.includeScope=test'
npm.cmd ci --prefix e2e --ignore-scripts
node e2e/node_modules/cypress/bin/cypress install
npm.cmd --prefix e2e test
```

É necessário JDK 21 em JAVA_HOME e Node 24. Cypress está fixado em 15.21.1, com package-lock.json. No Linux com servidor gráfico virtual, execute `xvfb-run -a npm --prefix e2e test`. O executor encerra seu backend ao terminar. A porta padrão é 18080; `BANCARIO_E2E_PORT` permite mudá-la.

## Ajustes do ambiente

O npm local não executou o postinstall do Cypress automaticamente. Foi executado explicitamente o instalador oficial. A primeira tentativa de E2E ocorreu antes da extração do navegador terminar e falhou por binário incompleto; isso não foi registrado como teste aprovado. A inspeção interativa via ferramenta de navegador ficou indisponível por ausência de navegador conectado; a inspeção visual usa as capturas reais do Cypress.

O primeiro fluxo aprovado usou Cypress 15.5.0. O npm audit do checkout limpo reportou seis alertas em dependências dessa ferramenta de teste. Foi adotada a atualização corretiva compatível 15.21.1 indicada pelo audit, sem mudar o recorte do produto. O resultado da nova auditoria e a repetição do E2E são registrados nas evidências finais. A extração do pacote oficial de navegador atualizado foi feita com o utilitário tar do Windows no cache de testes, para evitar a extração JavaScript lenta observada na instalação anterior.
