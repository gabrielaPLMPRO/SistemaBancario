# Sistema Bancário — evolução orientada por comportamento

Aplicação Java com frontend HTML/CSS/JavaScript separado, API REST e dados em memória. Implementa o recorte de cinco requisitos do roteiro da pasta **contexto para IA**: cadastro de cliente (RF02), validação de idade (RF03), abertura de conta (RF09), consulta de conta (RF08) e transferência (RF12).

A execução segue o roteiro metodológico local e o compilado de práticas de refatoração com LLM, apoiados nos artigos fornecidos. O [mapa de etapas](docs/README.md) e a [rastreabilidade das fontes](docs/etapa-1/fontes-e-rastreabilidade.md) distinguem orientações, escolhas de implementação e resultados executados.

## Executar a aplicação

Requisitos: **JDK 21** e acesso à rede no primeiro build. Maven 3.9.16 é baixado pelo Wrapper, com checksum fixado. Node 24 é necessário apenas para os testes de navegador.

Na raiz, em PowerShell:

```powershell
$env:JAVA_HOME = 'C:/caminho/para/seu/jdk-21'
./mvnw.cmd clean verify
& "$env:JAVA_HOME/bin/java.exe" -jar backend/target/sistema-bancario-backend-0.1.0-SNAPSHOT.jar
```

Abra **http://localhost:8080**. Para encerrar, use Ctrl+C no terminal do servidor. No Linux/macOS, use `sh mvnw clean verify` e `$JAVA_HOME/bin/java -jar backend/target/sistema-bancario-backend-0.1.0-SNAPSHOT.jar`.

O ambiente inicia vazio. Cadastre um cliente entre 18 e 65 anos, abra sua conta e consulte o saldo. A conta começa com R$ 0,00. Os dados são perdidos ao encerrar; não há depósito, autenticação ou banco persistente neste recorte. Para demonstrar uma transferência positiva, use o fluxo automatizado com fixture interna descrito abaixo.

## Comportamentos e contrato

| RF | Operação | Resultado principal |
| --- | --- | --- |
| RF02 / RF03 | `POST /api/clientes` | ID único, idade de 18 a 65, cliente ativo sem conta |
| RF09 | `POST /api/clientes/{id}/contas` | Uma conta por cliente, vínculo efetivo e saldo zero |
| RF08 | `GET /api/contas/{id}` | Conta, titular, saldo decimal e situação; 404 se ausente |
| RF12 | `POST /api/transferencias` | Contas ativas distintas, valor positivo, sem fração de centavo e saldo suficiente |

Dinheiro usa BigDecimal no servidor e strings decimais no JSON. Rejeições preservam o estado. O bloqueio compartilhado garante atomicidade das operações compostas na instância em memória. O frontend envia solicitações e exibe saldos retornados pelo servidor.

Exemplo de transferência:

```json
{ "origemId": 1001, "destinoId": 1002, "valor": "150.00" }
```

[Contrato OpenAPI](docs/api.yaml), [decisões e diferenças do legado](docs/decisoes.md).

## Testes e relatórios

Regressão, API, comparação diferencial, Cucumber e cobertura:

```powershell
./mvnw.cmd clean verify
```

Mutação do domínio e dos serviços:

```powershell
./mvnw.cmd -f backend/pom.xml test-compile org.pitest:pitest-maven:mutationCoverage
```

Fluxo real no navegador:

```powershell
./mvnw.cmd verify dependency:build-classpath '-Dmdep.outputFile=target/e2e-classpath.txt' '-Dmdep.includeScope=test'
npm.cmd ci --prefix e2e --ignore-scripts
node e2e/node_modules/cypress/bin/cypress install
npm.cmd --prefix e2e test
```

O executor inicia seu próprio servidor de testes, aplica a fixture por canal interno e encerra o processo ao terminar. A fixture não é uma rota HTTP e suas classes não estão no JAR de produção. CT-FL01 usa Ana (101/1001) e Bruno (102/1002), saldos 500/100, transfere 150 e verifica 350/250 e total 600. Há rejeições reais e verificação de tela pequena. No Linux sem display, use `xvfb-run -a npm --prefix e2e test`.

No Windows também existe o atalho `./scripts/validar.ps1 -Mutacao -E2e`, com JAVA_HOME configurado.

| Evidência | Caminho após a execução |
| --- | --- |
| Regressão JUnit | `backend/target/surefire-reports/` |
| Cucumber | `backend/target/cucumber/relatorio.html` |
| Cobertura JaCoCo | `backend/target/site/jacoco/index.html` |
| Mutação PIT | `backend/target/pit-reports/index.html` |
| Cypress | `e2e/results/` e `e2e/screenshots/` |
| Amostra da entrega | [evidencias/entrega](evidencias/entrega/README.md) |

As metas verificadas pelo build são 90% de instruções e ramificações nos pacotes de domínio/aplicação e 80% de mutação no mesmo recorte. Os resultados e limites reais estão nas evidências; esses percentuais não representam todo o produto.

## Organização e processo

```text
frontend/                    interface e cliente HTTP
backend/src/main/java/banco/ dominio, servicos, API e memoria
backend/src/test/java/       testes novos e snapshot do legado
backend/src/test/resources/  cenarios Gherkin
e2e/                       testes Cypress e package-lock
docs/                      processo, regras, contrato e apresentacao
evidencias/                resultados selecionados de execucoes reais
.github/workflows/ci.yml    regressao, E2E, cobertura e mutacao
```

Os 38 testes anteriores e as classes antigas estão exclusivamente no classpath de testes, em `negocio`, para preservação da linha de base e comparação diferencial. O produto empacota somente o código novo. A aplicação de console anterior pode ser testada isoladamente com `scripts/testar-legado.ps1`; o [relato original](docs/historico/trabalho-1.md) foi preservado.

A CI executa os testes no GitHub e publica os relatórios como artefatos. O status efetivo da execução remota deve ser conferido no PR; uma configuração de CI existente não equivale a uma execução aprovada.

## Autoria e apresentação

Apoio de Codex (OpenAI) documentado em [USO_DE_IA.md](USO_DE_IA.md). Os alunos devem preencher [autoria e contribuições](docs/contribuicoes.md), revisar as regras e preparar a explicação individual. Essas atividades humanas não são declaradas como realizadas pela automação. O [roteiro da apresentação](docs/apresentacao.md) organiza uma demonstração de 10 a 12 minutos.
