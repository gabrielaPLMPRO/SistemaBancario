# Etapa 1 — Diagnóstico e linha de base do legado

Registro de execução em 29/10/2026. Esta pasta concentra o processo inicial do roteiro metodológico da pasta `contexto para IA`. O recorte de evolução é RF02, RF03, RF09, RF08 e RF12.

**Registro histórico da etapa 1:** build Maven com JDK 21 validado e 38 testes aprovados naquele incremento. A evolução posterior, autorizada pelo usuário para todas as etapas, está no [mapa de execução](../README.md). A seção 8 registra a preparação do build e a seção 9 relaciona o trabalho às fontes locais.

## 1. Conferência do ponto de partida

Foi conferido o estado do Git antes das alterações: árvore de trabalho limpa, branch `main`, commit de referência `cc1767583ee9b7c1d1e9246c078164a210223c34`. Não foram criados commit, tag ou publicação remota nesta execução. A branch de evolução ainda deve ser criada antes da migração.

Foram lidos a síntese do contexto, o README existente, o código Java e o script local que gerou o roteiro em PDF (`tmp/pdfs/gerar_roteiro.py`). Não foi possível extrair diretamente o PDF com o Python disponível, pois as bibliotecas de PDF não estavam instaladas; o script gerador contém as etapas e os textos do roteiro. Não houve instalação de dependências.

No início do diagnóstico, o projeto era uma aplicação de console com estrutura Eclipse, Java 8 e JUnit 4.12. Os JARs de JUnit e Hamcrest já estavam em `lib/`. Naquele ponto não havia Maven, API HTTP, frontend Web ou persistência. A preparação posterior do Maven está registrada na seção 8.

## 2. Inventário e extração das regras

Foi feita leitura das entidades, gerenciadoras, exceções, menu e todos os testes. As evidências estão organizadas nestes documentos:

- [Diagnóstico do legado](../diagnostico-legado.md): responsabilidades, entradas, saídas, fluxos e problemas observados.
- [Regras de negócio](../regras-negocio.md): RN01 a RN05, métodos de origem e testes.
- [Decisões](../decisoes.md): o que foi preservado e o que precisa de revisão humana.

As regras extraídas descrevem o código existente. A revisão do grupo ainda está pendente; a execução automatizada não substitui essa revisão.

## 3. Execução dos testes originais

`java` e `javac` não estavam no PATH. Foi localizado o JDK distribuído com a extensão Java do VS Code:

```text
C:/Users/guiba/.vscode/extensions/redhat.java-1.58.0-win32-x64/jre/25.0.4.1-win32-x86_64
OpenJDK Temurin 25.0.4.1
JUnit 4.12 / Hamcrest Core 1.3
```

A primeira compilação com `-encoding UTF-8 --release 8` identificou três arquivos em Windows-1252: `Cliente.java`, `ContaCorrente.java` e `IdadeNaoPermitidaException.java`. Para executar a linha de base antes de editar os fontes, foi usado `-encoding windows-1252`. Isso permite compilar, mas interpreta incorretamente os acentos dos arquivos que já eram UTF-8; por isso foi apenas uma execução provisória.

O compilador foi bloqueado pelo sandbox ao fechar os JARs locais. A execução fora do sandbox foi autorizada pelo usuário e concluída. Comandos equivalentes, executados a partir da raiz:

```powershell
$jdk = 'C:/Users/guiba/.vscode/extensions/redhat.java-1.58.0-win32-x64/jre/25.0.4.1-win32-x86_64'
New-Item -ItemType Directory -Force tmp/baseline | Out-Null
$fontes = @(Get-ChildItem src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
& "$jdk/bin/javac.exe" -encoding windows-1252 --release 8 -cp 'lib/*' -d tmp/baseline @fontes
& "$jdk/bin/java.exe" -cp 'tmp/baseline;lib/*' org.junit.runner.JUnitCore negocio.Testes negocio.TDD.RF02 negocio.TDD.RF03 negocio.TDD.RF09 negocio.TDD.RF12 negocio.BDD.RF01 negocio.BDD.RF04 negocio.BDD.RF05 negocio.BDD.RF08 negocio.DDD.RF06 negocio.DDD.RF07 negocio.DDD.RF10 negocio.DDD.RF11
```

Resultado real: **30 testes aprovados em 13 classes**, `OK (30 tests)`.

## 4. Ajustes de preparação

Os três arquivos identificados foram convertidos de Windows-1252 para UTF-8 sem alterar suas regras ou textos. A conversão preserva os caracteres, inclusive a mensagem da exceção de idade. O `.gitignore` foi organizado para remover marcadores de conflito antigos e ignorar saídas de compilação.

Foi criado [scripts/testar-legado.ps1](../../scripts/testar-legado.ps1), que:

1. Localiza o JDK por `-JdkHome`, `JAVA_HOME` ou `javac` no PATH.
2. Cria uma pasta nova em `tmp/testes-legado/` para cada compilação.
3. Compila todos os fontes em UTF-8 com `--release 8`.
4. Descobre classes com `@Test`, incluindo `RFxx`, e as executa via JUnitCore.
5. Interrompe a execução com erro se a compilação ou algum teste falhar.

O script exige JDK 9 ou superior por usar `--release`; o bytecode gerado tem como alvo Java 8. A execução foi validada no JDK 25 acima, não em uma JVM Java 8. O JDK 21 previsto para a nova versão ainda não foi preparado.

## 5. Testes de caracterização

Foi adicionada [CaracterizacaoLegadoTest.java](../../backend/src/test/java/negocio/TDD/CaracterizacaoLegadoTest.java), inicialmente em `src/negocio/TDD/` e depois movida para a estrutura Maven, com oito testes:

| Caso | Resultado verificado |
| --- | --- |
| Idade 17 | Rejeição sem inserir cliente |
| Idade 18 | Cadastro aceito |
| Idade 65 | Cadastro aceito |
| Idade 66 | Rejeição sem inserir cliente |
| ID de cliente repetido | Exceção, quantidade e objeto original preservados |
| ID de conta repetido | Exceção, quantidade, conta original e saldo preservados |
| Transferir 150 de 500 para 100 | Saldos 350 e 250; soma 600 |
| Transferir todo o saldo de 500 | Origem zerada e destino com 600 |

São testes de caracterização de regras já implementadas, sem ciclo RED/GREEN de nova funcionalidade. Os 30 testes originais foram preservados.

## 6. Reprodução e resultado final

Na raiz do projeto, com `JAVA_HOME` configurado:

```powershell
./scripts/testar-legado.ps1
```

Ou informe o JDK explicitamente:

```powershell
./scripts/testar-legado.ps1 -JdkHome 'C:/caminho/para/seu/jdk'
```

Comando efetivamente validado nesta máquina:

```powershell
./scripts/testar-legado.ps1 -JdkHome 'C:/Users/guiba/.vscode/extensions/redhat.java-1.58.0-win32-x64/jre/25.0.4.1-win32-x86_64'
```

Saída relevante em 29/10/2026:

```text
Classes de teste (14)
JUnit version 4.12
OK (38 tests)
```

Resultado: **38 testes aprovados, sem falhas**. O compilador emite três avisos de obsolescência do alvo Java 8 no JDK 25; são avisos, não falhas. Cobertura, mutação, API, Cucumber e E2E não foram executados nem medidos.

## 7. Pendências ao final do primeiro incremento — histórico

- Revisão humana das RN e das decisões, especialmente vínculo cliente/conta, contas inativas e transferência para a própria conta.
- Criar branch de evolução; estabelecer build Maven com separação entre produção e testes, preservando a descoberta das classes RFxx.
- Preparar JDK 21 e Maven Wrapper para o novo backend, conforme o roteiro.
- Especificar cenários BDD e contrato da API antes de mudar os comportamentos.
- Implementar serviços, API e frontend em incrementos posteriores.

O diagnóstico e a caracterização inicial foram executados. A etapa metodológica ainda depende da revisão das regras pelo grupo e da preparação do build Maven; não está declarada integralmente concluída.

## 8. Segundo incremento — build Maven e separação de fontes

### 8.1 Objetivo e orientação do roteiro

Executar a preparação prevista na página 8 do roteiro: criar build Maven, separar produção/teste, incluir os padrões RFxx no Surefire e confirmar a descoberta dos testes. A página 9 orienta manter a regressão durante incrementos. Este incremento altera a organização e o build; não altera as regras bancárias.

Foi criada a branch `evolucao/etapa-1-maven`, mantendo como referência o commit inicial da seção 1. As alterações dos dois incrementos continuam na árvore de trabalho, sem commit, tag ou publicação remota.

### 8.2 Movimentação dos fontes

Foram movidos 22 arquivos, com SHA-256 conferido antes e depois:

| Conteúdo | Origem | Destino | Quantidade |
| --- | --- | --- | --- |
| Produção | `src/negocio/` | `backend/src/main/java/negocio/` | 8 arquivos |
| Testes | `src/negocio/`, `BDD/`, `DDD/`, `TDD/` | `backend/src/test/java/negocio/` e subpacotes | 14 arquivos |

Os pacotes Java e o conteúdo dos arquivos foram preservados. Não foram feitas alterações nas asserções ou na lógica bancária durante a movimentação. Foram removidos metadados antigos do Eclipse que apontavam para a árvore anterior, atualizada a execução do VS Code e adicionadas regras de finais de linha e de exclusão de `target/`.

### 8.3 Ambiente e configuração

- JDK portátil: **Eclipse Temurin 21.0.13+9**, baixado da distribuição oficial indicada pela API Adoptium, com SHA-256 conferido.
- Maven: **3.9.16**, distribuição oficial do Maven Central, com SHA-512 conferido no download inicial.
- Maven Wrapper: **3.3.4**, scripts oficiais do tipo `only-script`; checksum SHA-1 do pacote conferido com o publicado no Maven Central. O Wrapper verifica SHA-256 da distribuição Maven em suas primeiras instalações.
- Plugins fixados: Clean 3.5.0, Resources 3.3.1, Compiler 3.14.1, Surefire 3.5.4, JAR 3.4.2 e Enforcer 3.6.1.
- JUnit 4.12 continua no escopo `test`; a migração para Jupiter/Cucumber será um incremento posterior.

Não foi feita instalação global do JDK ou Maven. As ferramentas portáteis ficaram em `tmp/ferramentas/`. Os POMs exigem JDK 21, usam UTF-8 e compilam para Java 21. O Enforcer rejeita outra versão principal do JDK. O script auxiliar continua compilando o código legado com alvo Java 8 para comparação.

Foi adotado um POM agregador e um único Wrapper na raiz, com o módulo `backend/`. É uma adaptação de organização do exemplo da página 12 do roteiro, que coloca o Wrapper em `backend/`. Dentro de `backend/`, o comando equivalente é `../mvnw.cmd clean verify`.

### 8.4 Execução e dificuldades encontradas

1. A rede estava bloqueada no sandbox; os downloads oficiais e a execução do Maven foram autorizados fora dele.
2. A criação de subpastas do repositório Maven em `tmp/maven-repository` falhou nas primeiras tentativas. O cache foi transferido para o diretório temporário do Windows; essa configuração é local e não foi fixada no POM.
3. A primeira consulta ao checksum SHA-512 do pacote Wrapper retornou HTTP 404. Foi usado o checksum SHA-1 publicado para esse pacote; a distribuição Maven permanece fixada com SHA-256 no Wrapper.
4. O script auxiliar voltou a encontrar bloqueio do compilador dentro do sandbox. A validação foi repetida fora dele. Uma tentativa de registrar a saída com `2>&1` foi interrompida pelo PowerShell ao interpretar o texto de versão emitido em stderr pelo Java como erro; a execução direta concluiu normalmente.

Comando efetivamente executado na raiz para a validação Maven:

```powershell
$env:JAVA_HOME = (Resolve-Path 'tmp/ferramentas/jdk21/jdk-21.0.13+9').Path
$env:MAVEN_USER_HOME = Join-Path (Get-Location) 'tmp/maven-home'
$repositorio = Join-Path $env:TEMP 'SistemaBancario-codex/maven-repository'
./mvnw.cmd -B -ntp "-Dmaven.repo.local=$repositorio" clean verify
```

Resultado real em 29/10/2026, encerrado às 18:20:25 no fuso -03:00:

```text
Compiling 8 source files with javac [debug release 21]
Compiling 14 source files with javac [debug release 21]
Tests run: 38, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

O Surefire descobriu as 14 classes, incluindo os 12 RFxx. Os relatórios XML foram conferidos: 38 testes, zero falhas, zero erros e zero ignorados. O JAR contém somente as 8 classes de produção e define `negocio.Main` como entrada; JUnit e testes não estão no artefato de produção.

O script `scripts/testar-legado.ps1` foi adaptado para compilar produção e testes separadamente nos novos caminhos. Com JDK 21 e alvo Java 8, repetiu **38 testes aprovados em 14 classes**. O JAR foi iniciado e encerrado com a opção 9, validando sua entrada de console. Isso não é um teste completo dos fluxos do menu.

As evidências resumidas, geradas a partir dos relatórios reais, estão em [evidencias/etapa-1](../../evidencias/etapa-1/README.md). Os relatórios completos e arquivos compilados permanecem em `backend/target/`, fora do versionamento.

### 8.5 Como reproduzir

Com um JDK 21 instalado e `JAVA_HOME` configurado, na raiz:

```powershell
./mvnw.cmd clean verify
& "$env:JAVA_HOME/bin/java.exe" -jar backend/target/sistema-bancario-backend-0.1.0-SNAPSHOT.jar
```

Para o comparativo com o executor antigo:

```powershell
./scripts/testar-legado.ps1 -JdkHome $env:JAVA_HOME
```

A primeira execução precisa de rede para Maven e dependências. As instruções de uso do módulo e importação na IDE estão no [README do backend](../../backend/README.md).

## 9. Fontes, estado atual e próxima etapa

O [registro de fontes e rastreabilidade](fontes-e-rastreabilidade.md) relaciona cada ação ao roteiro, ao compilado de práticas com LLM e aos trechos pertinentes dos artigos locais. Neste segundo incremento foram conferidos os PDFs diretamente, complementando a leitura anterior da síntese e do script gerador. A ferramenta de extração foi usada apenas para leitura, sem modificar os PDFs.

Preparação do build Maven, JDK 21 e separação dos fontes: **executada e validada**. Diagnóstico e caracterização: **executados**. Revisão humana das regras e decisões: **pendente**.

O próximo incremento é a especificação BDD dos cinco requisitos e a resolução documentada das políticas de vínculo e movimentação. A implementação de serviços, API e frontend virá depois dessa especificação, conforme o roteiro. Não foram executados Cucumber, Cypress, JaCoCo, PIT ou teste diferencial entre duas implementações funcionais; a suíte atual ainda caracteriza o legado reorganizado.
