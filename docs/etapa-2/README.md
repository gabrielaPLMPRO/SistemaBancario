# Etapa 2 — Especificação antes da reconstrução

Data: 29/10/2026. Autorização do usuário: implementar todas as etapas do roteiro, assumindo as decisões necessárias sem novas confirmações. Isso autoriza a execução técnica; não atribui ao grupo uma revisão acadêmica que não ocorreu.

Base: roteiro local, páginas 3 a 5 e 7; compilado `refatoracao de codigo llm - processo.pdf`, páginas 1, 2, 4 e 5; Oliveira, diretrizes de testes funcionais e valores limite; Campos, especificação Gherkin e documentação viva. A rastreabilidade inicial está em `docs/etapa-1/fontes-e-rastreabilidade.md`.

## Decisões antes da implementação

- Preservar RN01–RN05, inclusive idades 18 a 65.
- Cliente novo não tem conta (`contaId: null`). Abertura cria conta com saldo zero e vínculo nos dois sentidos, atomicamente. Uma conta por cliente; segunda abertura retorna 409.
- Saldo e valor usam BigDecimal. JSON monetário usa strings decimais. Zero, negativo e fração de centavo retornam 422; texto que não representa número retorna 400.
- Conta ausente retorna 404 antes de qualquer movimentação. Conta inativa e transferência para a própria conta retornam 422. São decisões explícitas de evolução.
- Operações concorrentes compartilham o mesmo bloqueio na implementação em memória. Rejeições não alteram clientes, vínculos ou saldos.
- IDs positivos, nome/email obrigatórios e email com formato válido são validações de entrada (400). A idade é regra do domínio (422).
- Sem depósito, autenticação ou banco persistente, conforme recorte do roteiro. Saldo para demonstração de transferência é fornecido exclusivamente por fixture interna de teste.

## Artefatos prévios

Os cenários em `backend/src/test/resources/features/fluxo-bancario.feature` e os testes de domínio são escritos antes dos serviços. O ciclo RED/GREEN será registrado com os resultados reais. A API seguirá `docs/api.yaml`.

O snapshot legado será mantido exclusivamente na árvore de testes, permitindo regressão e comparação diferencial. Não será empacotado na aplicação Web. É uma reconstrução comportamental, com diferenças intencionais documentadas, e não uma alegação de refatoração estritamente equivalente.

## Revisão

As políticas acima são decisões técnicas adotadas sob autorização do usuário. A revisão acadêmica pelos alunos continua sendo atividade humana a realizar; não impede a execução autorizada. As evidências posteriores devem distinguir aceitação técnica automática de aprovação humana.
