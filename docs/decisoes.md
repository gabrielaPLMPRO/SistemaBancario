# Decisões e questões em aberto

Registro iniciado em 29/10/2026. Revisão humana de regras: pendente.

Atualização: o usuário autorizou expressamente a implementação de todas as etapas, sem novas confirmações. As decisões de evolução abaixo foram adotadas para executar o roteiro; a revisão acadêmica pelos alunos continua pendente e não foi simulada.

| ID | Situação | Decisão ou questão |
| --- | --- | --- |
| D01 | Aplicada nesta preparação | Preservar a faixa existente de 18 a 65 inclusive; mudança depende de decisão explícita do grupo |
| D02 | Aplicada nesta preparação | Converter três arquivos Windows-1252 para UTF-8, preservando caracteres e lógica |
| D03 | Aplicada nesta preparação | Manter JUnit 4.12 e todos os testes; usar script local para executar a linha de base sem downloads |
| D04 | Implementada | Cliente sem conta usa contaId null; abertura efetiva vínculo e permite uma conta por cliente. Segunda conta retorna 409 |
| D05 | Implementada | Contas ausentes retornam erro controlado/404 antes de alterar saldos |
| D06 | Implementada | BigDecimal, duas casas sem arredondar fração de centavo, JSON monetário em strings e bloqueio comum na operação inteira |
| D07 | Implementada como evolução | Transferência exige contas distintas e ambas ativas; rejeição 422 sem movimentação |
| D08 | Executada e validada | JDK 21.0.13+9, Maven 3.9.16, Wrapper 3.3.4 e separação de produção/testes; build Maven com 38 testes aprovados. Jupiter/Cucumber ainda pendentes |
| D09 | Implementada | RF02/RF03/RF09/RF08/RF12 com serviços Java, API REST e frontend separado; testes e relatórios nas evidências |
| D10 | Aplicada nesta preparação | POM agregador e Wrapper únicos na raiz, módulo backend; adaptação organizacional do exemplo do roteiro, sem alterar o recorte ou a sequência metodológica |
| D11 | Implementada | Snapshot legado exclusivamente em testes; sem segunda implementação bancária no produto |
| D12 | Implementada | Fixture E2E por canal interno de arquivos, classes apenas de teste; nenhuma rota pública de saldo ou depósito |
| D13 | Implementada | JSON estrito: IDs inteiros, dinheiro string, rejeição de campos desconhecidos. Formato inválido retorna 400 |

Os READMEs por etapa documentam o que foi executado e seus limites. Essas decisões técnicas não representam comprovação de revisão humana ou apresentação acadêmica.
