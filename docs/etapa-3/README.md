# Etapa 3 — Domínio, serviços e API

Executada em 29/10/2026. Base: roteiro, páginas 6 a 9; compilado LLM, páginas 1 e 2. A especificação e as decisões foram registradas na [etapa 2](../etapa-2/README.md) antes dos serviços.

## Implementação

O código novo está em `backend/src/main/java/banco/`: domínio imutável, serviços de cadastro, abertura, consulta e transferência, porta de repositório e implementação em memória, controllers e tratamento de erros. `BancoApplication` inicia Spring Boot 3.5.16. O frontend usa exclusivamente o contrato [api.yaml](../api.yaml).

O snapshot anterior ficou em `backend/src/test/java/negocio/`, com seus 38 testes. Essa cópia é uma referência de comparação, não é servida nem empacotada no produto. O executor auxiliar do legado foi atualizado para essa localização. Assim, a produção não mantém duas implementações bancárias concorrentes.

Todas as operações compostas usam o mesmo monitor do repositório. Entidades são records imutáveis. Uma transferência lê e valida as duas contas, calcula os dois novos saldos e só então os grava, dentro do bloqueio. Isso garante atomicidade para a implementação em memória, em uma instância; não é uma transação distribuída nem substitui transações de um banco futuro.

O servidor valida IDs e formato dos campos (400), idade e movimentação (422), ausência (404) e conflitos (409). Campos JSON desconhecidos são rejeitados. Abertura grava conta e vínculo sob o mesmo bloqueio. Dinheiro é BigDecimal, sem arredondamento silencioso; o JSON retorna strings com duas casas.

## TDD e ajustes efetivamente executados

1. `BancoServiceTest` foi escrito antes dos serviços. A primeira execução falhou por ausência de `TransferenciaService`, `ConsultaContaService` e demais classes novas: RED real, registrado localmente em `tmp/tdd-red.log`.
2. Os serviços foram implementados. Os 24 testes iniciais passaram: GREEN real, em `tmp/tdd-green.log`.
3. Foram acrescentados testes de API, concorrência, comparação e Cucumber. A primeira integração encontrou parâmetros HTTP sem nomes disponíveis por reflexão. Foram explicitados os nomes em `@PathVariable` e habilitado `maven.compiler.parameters`.
4. Cucumber detectou que uma DataTable construída manualmente não tinha conversor. A fixture passou a ler as células da tabela diretamente. Nenhum cenário foi retirado para obter aprovação.
5. A suíte completa passou com 105 testes, zero falhas, erros e ignorados. Os 38 testes antigos permaneceram aprovados via JUnit Vintage; os novos usam Jupiter e Cucumber. JUnit foi atualizado para 4.13.2 no escopo de testes.

Na validação final foram adicionados casos de tipos JSON estritos e comparação de duplicidade/abertura entre versões. O total final passou a 110 testes, todos aprovados, incluindo 12 cenários Cucumber. Esses resultados foram repetidos em checkout limpo.

Uma tentativa de comando Maven no PowerShell separou um argumento `-D` com ponto; os comandos com propriedades foram corrigidos para argumentos entre aspas. Isso foi falha de execução do comando, não um teste RED de negócio.

## Reprodução

Na raiz, com JDK 21 em JAVA_HOME:

```powershell
./mvnw.cmd clean verify
& "$env:JAVA_HOME/bin/java.exe" -jar backend/target/sistema-bancario-backend-0.1.0-SNAPSHOT.jar
```

A aplicação inicia em `http://localhost:8080`, vazia, e perde os dados ao encerrar. Os relatórios estão em `backend/target/surefire-reports`, `backend/target/cucumber` e `backend/target/site/jacoco`.

## Limites e diferenças intencionais

O cadastro e a abertura agora são separados. Conta inexistente na transferência tem erro controlado. Uma conta por cliente, contas ativas e contas distintas são políticas novas documentadas. Ativação/desativação/remoção continuam caracterizadas no snapshot, mas não foram adicionadas à API do recorte de cinco RF. Não há autenticação, depósito ou persistência, conforme o roteiro.
