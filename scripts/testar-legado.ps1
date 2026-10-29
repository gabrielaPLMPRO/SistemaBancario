param([string]$JdkHome = $env:JAVA_HOME)

$ErrorActionPreference = 'Stop'
$raiz = Split-Path $PSScriptRoot -Parent
if (-not $JdkHome) {
    $compilador = Get-Command javac -ErrorAction SilentlyContinue
    if ($compilador) { $JdkHome = Split-Path (Split-Path $compilador.Source -Parent) -Parent }
}
if (-not $JdkHome) {
    throw 'Informe -JdkHome com a pasta de um JDK 9 ou superior, ou configure JAVA_HOME.'
}
$javac = Join-Path $JdkHome 'bin/javac.exe'
$java = Join-Path $JdkHome 'bin/java.exe'
if (-not (Test-Path $javac) -or -not (Test-Path $java)) {
    throw 'O JDK informado precisa conter bin/javac.exe e bin/java.exe.'
}

# Uma pasta nova impede que classes de uma compilacao anterior mascarem falhas.
$saida = Join-Path $raiz ('tmp/testes-legado/' + [guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Force $saida | Out-Null
$fontesLegado = @(Get-ChildItem (Join-Path $raiz 'backend/src/test/java/negocio') -Recurse -Filter *.java | Sort-Object FullName)
$fontesProducao = @($fontesLegado | Where-Object { (Get-Content -Raw -Encoding UTF8 $_.FullName) -notmatch '@Test\b' })
$fontesTeste = @($fontesLegado | Where-Object { (Get-Content -Raw -Encoding UTF8 $_.FullName) -match '@Test\b' })
$testes = @($fontesTeste | Where-Object {
    (Get-Content -Raw -Encoding UTF8 $_.FullName) -match '@Test\b'
} | ForEach-Object {
    $texto = Get-Content -Raw -Encoding UTF8 $_.FullName
    if ($texto -notmatch 'package\s+([\w.]+)\s*;') { throw "Package ausente: $($_.FullName)" }
    $Matches[1] + '.' + $_.BaseName
})
if ($testes.Count -eq 0) { throw 'Nenhuma classe de teste encontrada.' }
$bibliotecas = Join-Path $raiz 'lib/*'
Write-Output "Saida: $saida"
& $java -version
& $javac -encoding UTF-8 --release 8 -d $saida @($fontesProducao.FullName)
if ($LASTEXITCODE -ne 0) { throw "Compilacao de producao falhou: $LASTEXITCODE" }
& $javac -encoding UTF-8 --release 8 -cp "$saida;$bibliotecas" -d $saida @($fontesTeste.FullName)
if ($LASTEXITCODE -ne 0) { throw "Compilacao falhou: $LASTEXITCODE" }
Write-Output "Classes de teste ($($testes.Count)): $($testes -join ', ')"
& $java -cp "$saida;$bibliotecas" org.junit.runner.JUnitCore @testes
if ($LASTEXITCODE -ne 0) { throw "Testes falharam: $LASTEXITCODE" }
