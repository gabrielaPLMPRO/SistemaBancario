# Backend — aplicação e regressão

O módulo contém a aplicação Spring Boot em `src/main/java/banco/` e os testes em `src/test/java/`. O snapshot do legado está apenas na árvore de testes, no pacote `negocio`, para regressão e comparação. A implementação segue os cenários escritos na etapa 2 e as decisões documentadas sob autorização do usuário.

## Compilar e testar

É necessário um **JDK 21**. Configure `JAVA_HOME` para a raiz desse JDK. O Maven Wrapper oficial está na raiz do repositório e fixa Maven 3.9.16, com verificação SHA-256 da distribuição.

Na raiz do repositório:

```powershell
$env:JAVA_HOME = 'C:/caminho/para/jdk-21'
./mvnw.cmd clean verify
```

Ou, a partir de `backend/`:

```powershell
../mvnw.cmd clean verify
```

No Linux/macOS, use `sh ./mvnw clean verify` na raiz, ou `sh ../mvnw clean verify` em `backend/`. Esses comandos POSIX ainda não foram executados nesta etapa. O Wrapper baixa o Maven na primeira utilização; o Maven baixa plugins e dependências do Maven Central. O cache padrão do usuário é utilizado, salvo configuração explícita.

## Executar a aplicação Web

Após `verify`, na raiz do repositório:

```powershell
& "$env:JAVA_HOME/bin/java.exe" -jar backend/target/sistema-bancario-backend-0.1.0-SNAPSHOT.jar
```

Abra `http://localhost:8080`. Os dados ficam em memória e o ambiente inicia vazio. Ctrl+C encerra o servidor. O JAR inclui o frontend e as dependências de execução, sem JUnit, snapshot legado ou fixture de testes. Não há dados de demonstração inseridos automaticamente na aplicação normal.

## Testes e dependências

JUnit Jupiter executa os testes novos; Vintage mantém a suíte anterior com JUnit 4.13.2. Os padrões do Surefire incluem explicitamente `RF*.java`, além dos padrões convencionais. `negocio.Testes`, `CaracterizacaoLegadoTest` e `RunCucumberTest` também são descobertos. O build falha se não encontrar nenhum teste. Cucumber usa HTTP real com porta aleatória.

O build Maven não usa os JARs de `lib/`; eles continuam disponíveis apenas para o script auxiliar `scripts/testar-legado.ps1`, que compila isoladamente o snapshot do pacote `negocio` com alvo Java 8. O build principal compila para Java 21. JaCoCo e PIT estão configurados; comandos e relatórios estão no README raiz.

Os relatórios locais estão em `backend/target/surefire-reports/`. A evidência resumida desta etapa está em [evidencias/etapa-1](../evidencias/etapa-1/README.md). O processo completo e as limitações estão no [README da etapa 1](../docs/etapa-1/README.md).

## IDE

Importe `pom.xml` da raiz como projeto Maven ou abra a pasta raiz no VS Code com as extensões Java/Maven e JDK 21 configurado. Os metadados Eclipse antigos apontavam para `src/negocio` e foram removidos; não devem ser usados para compilar a nova estrutura. A configuração de execução do VS Code aponta para o módulo `sistema-bancario-backend`.
