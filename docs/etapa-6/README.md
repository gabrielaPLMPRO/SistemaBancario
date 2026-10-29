# Etapa 6 — Entrega reproduzível e preparação da apresentação

Base: roteiro local, páginas 12 e 13; compilado LLM, declaração de uso de IA e documentação de preservação/comparação.

## Artefatos de entrega

- README raiz com requisitos, instalação, comandos exatos, arquitetura, fixture e relatórios.
- READMEs por incremento e mapa que os relaciona às 13 páginas do roteiro.
- OpenAPI, decisões de domínio e comparação diferencial.
- CI de regressão/API/BDD/JaCoCo/Cypress e job separado de PIT, com relatórios como artefatos.
- Amostra de resultados reais, screenshots e análise em `evidencias/entrega/`.
- Roteiro de apresentação, contribuição dos alunos e registro de uso de IA.

O código é preparado em branch de evolução e será enviado ao remoto já configurado, com PR para revisão. A autorização do usuário para todas as etapas inclui essa preparação e publicação previstas no roteiro. Não implica merge automático na branch principal nem mensagem a pessoas fora do fluxo do repositório.

## Reprodutibilidade

JDK 21, Maven/Wrapper e plugins fixos; Spring Boot e Cucumber fixos; Cypress com package-lock.json. O produto não depende dos JARs locais de `lib/`; esses pertencem somente ao executor histórico. Não são versionados caches, JDK, dependências, binários ou logs completos da máquina.

Os testes separam o recorte moderno do snapshot legado. O executor E2E controla início, prontidão e encerramento do próprio servidor. A fixture não existe no JAR de produção. A CI guarda os relatórios completos; o repositório guarda uma amostra identificada com ambiente e execução, sem segredos de autenticação.

## Atividades humanas

Os alunos devem preencher nomes e matrículas, conferir as políticas adotadas, reproduzir os comandos e apresentar o resultado ao professor. Essas tarefas não podem ser comprovadas pela execução de testes ou pela autorização dada ao agente. O roteiro de fala está em `docs/apresentacao.md`.

O status real do commit, PR e CI será registrado na evidência final. Falhas de execução remota, se ocorrerem, devem permanecer visíveis e ser corrigidas ou explicitamente relatadas, sem marcar resultados não executados como aprovados.
