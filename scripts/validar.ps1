param([string]$JdkHome = $env:JAVA_HOME, [switch]$Mutacao, [switch]$E2e)
$ErrorActionPreference = 'Stop'
if (-not $JdkHome) { throw 'Configure JAVA_HOME com JDK 21 ou informe -JdkHome.' }
$env:JAVA_HOME = (Resolve-Path -LiteralPath $JdkHome).Path
$raiz = Split-Path $PSScriptRoot -Parent
Push-Location $raiz
try {
    & ./mvnw.cmd -B -ntp clean verify dependency:build-classpath '-Dmdep.outputFile=target/e2e-classpath.txt' '-Dmdep.includeScope=test'
    if ($LASTEXITCODE -ne 0) { throw 'Build, testes ou cobertura falharam.' }
    if ($Mutacao) {
        & ./mvnw.cmd -B -ntp -f backend/pom.xml org.pitest:pitest-maven:mutationCoverage
        if ($LASTEXITCODE -ne 0) { throw 'Mutacao falhou.' }
    }
    if ($E2e) {
        & npm.cmd ci --prefix e2e --ignore-scripts
        if ($LASTEXITCODE -ne 0) { throw 'Instalacao das dependencias E2E falhou.' }
        & node e2e/node_modules/cypress/bin/cypress install
        if ($LASTEXITCODE -ne 0) { throw 'Instalacao do Cypress falhou.' }
        & npm.cmd --prefix e2e test
        if ($LASTEXITCODE -ne 0) { throw 'E2E falhou.' }
    }
} finally { Pop-Location }
