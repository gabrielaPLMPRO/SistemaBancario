# Relatório de testes, ferramentas e análise de tempo com IA

Data: 29/10/2026. Projeto: Sistema Bancário. A análise combina evidências locais da entrega e pesquisas externas consultadas nesta data.

**Resultado verificável:** foram executados e aprovados **113 casos automatizados: 110 Java e 3 Cypress**. O ganho de tempo de desenvolvimento deste projeto em relação a uma execução sem IA **não foi medido**. Os estudos abaixo fornecem comparações reais em outros contextos e permitem construir simulações explícitas, sem atribuir ao projeto horas economizadas que não foram observadas.

## 1. Base documental e recorte

O relatório usa o [consolidado de resultados](../evidencias/entrega/resultados.json), os [relatórios e capturas da entrega](../evidencias/entrega/README.md), a [caracterização inicial](etapa-1/README.md) e o [registro de uso de IA](../USO_DE_IA.md).

O commit `cddd0de16e4a80a49f7314c736e6bc65ffbacb6a` foi reproduzido em checkout limpo: fontes conferidos, 110 testes Java e 3 Cypress aprovados e JAR idêntico byte a byte. A entrega documentada no commit `887f4eabc48a7ff52deb31cf2cf9b3f647fe17ec` também aprovou os jobs de regressão/E2E e mutação na [CI do PR](https://github.com/gabrielaPLMPRO/SistemaBancario/actions/runs/40971155871). O [PR #1](https://github.com/gabrielaPLMPRO/SistemaBancario/pull/1) reúne a implementação para revisão.

O recorte funcional abrange cadastro e validação de clientes, abertura de contas, consulta e transferência. Persistência, autenticação e depósito estão fora desse recorte.

## 2. Quantos testes foram utilizados

| Grupo | Quantidade | O que verifica |
| --- | ---: | --- |
| Snapshot do legado, incluindo caracterização | 38 | Regras e comportamentos anteriores preservados para regressão |
| Serviços — `BancoServiceTest` | 24 | Cadastro, vínculo, abertura, consulta, transferência e rejeições |
| Concorrência — `ConcorrenciaTest` | 2 | Abertura simultânea e proteção contra gasto duplo |
| API — `ApiTest` | 21 | Contrato HTTP/JSON, validações, erros e estado após rejeições |
| Comparação diferencial — `ComparacaoLegadoTest` | 13 | Comportamentos do legado e da versão nova com entradas equivalentes |
| BDD — `RunCucumberTest` | 12 | Cenários Gherkin executados por HTTP real |
| **Subtotal Java, em 19 classes de execução** | **110** | **Zero falhas, erros ou ignorados** |
| Cypress — navegador | 3 | Fluxo completo, rejeições com saldos preservados e tela pequena |
| **Total Java + navegador** | **113** | **Todos aprovados** |

Os **12 cenários Cucumber já fazem parte dos 110 testes Java**. Reexecuções locais, reprodução em checkout limpo e execuções na CI validam novamente o mesmo conjunto; não aumentam a quantidade de casos. Alguns níveis exercitam os mesmos requisitos sob perspectivas diferentes.

A linha de base inicial tinha **30 testes**. Foram adicionados 8 testes de caracterização, chegando aos 38 do snapshot, e depois 72 casos Java da versão nova e 3 casos Cypress. Assim, o conjunto final contém **30 originais + 83 adicionados = 113 casos automatizados**. Essa contagem descreve a suíte; quantidade isolada não demonstra qualidade.

### Cobertura e força das asserções

| Medida | Resultado real | Escopo |
| --- | --- | --- |
| JaCoCo — instruções do domínio | 109/109, **100%** | `banco.domain` |
| JaCoCo — ramificações do domínio | 8/8, **100%** | `banco.domain` |
| JaCoCo — instruções dos serviços | 233/233, **100%** | `banco.application` |
| JaCoCo — ramificações dos serviços | 14/14, **100%** | `banco.application` |
| PIT — teste de mutação | **31/31 mutantes eliminados, 100%** | Domínio e serviços; zero sobreviventes e zero sem cobertura |

Os 31 mutantes são alterações artificiais avaliadas pelo PIT; **não são 31 casos adicionais** na contagem de 113. A cobertura apresentada é dos dois pacotes indicados, não de todo o backend, do frontend ou do produto. Fontes: [JaCoCo XML](../evidencias/entrega/jacoco.xml) e [mutações XML](../evidencias/entrega/mutacoes.xml).

## 3. Ferramentas utilizadas

As versões abaixo pertencem ao ambiente validado, sem pretensão de indicar as versões mais recentes disponíveis. As configurações podem ser conferidas no [POM principal](../pom.xml), no [POM do backend](../backend/pom.xml), no [lockfile npm](../e2e/package-lock.json) e no [workflow da CI](../.github/workflows/ci.yml).

| Ferramenta ou tecnologia | Versão/identificação registrada | Utilização |
| --- | --- | --- |
| Codex, da OpenAI | Identificador exato do modelo não registrado no histórico da implementação | Leitura das fontes, especificação, código, testes, documentação e auxílio à execução; atividades em [USO_DE_IA.md](../USO_DE_IA.md) |
| Java / Eclipse Temurin | JDK 21.0.13+9 | Compilação e execução do backend |
| Maven / Maven Wrapper | 3.9.16 / 3.3.4 | Build reproduzível e execução dos plugins |
| Spring Boot / Spring MVC | Boot 3.5.16 | API REST, validação e servidor HTTP; entrega dos arquivos estáticos |
| HTML, CSS e JavaScript | Frontend sem framework | Formulários, consultas e comunicação com a API por `fetch` |
| JUnit Jupiter / Vintage | 5.12.2 | Execução dos testes novos e compatibilidade com a suíte anterior |
| JUnit do legado | 4.13.2 no build final | Suíte histórica executada por Vintage |
| Maven Surefire | 3.5.4 | Descoberta e execução dos testes Java, incluindo classes `RFxx` |
| Cucumber-JVM / Gherkin | 7.30.0 / cenários em português | Especificação BDD executável e cenários por HTTP real |
| JaCoCo | 0.8.14 | Cobertura de instruções e ramificações e verificação das metas |
| PIT / plugin JUnit 5 | 1.20.3 / 1.2.3 | Mutação das classes de negócio |
| Node.js / npm | Node 24.21.0; dependências fixadas pelo lockfile | Executor E2E, instalação e auditoria das dependências de teste |
| Cypress / Electron | 15.21.1 / 138.0.7204.251 no Windows validado | Testes reais de navegador e capturas desktop/mobile |
| Git / GitHub / GitHub Actions | Branch, PR e workflow versionados | Histórico, publicação, validação remota no Linux e artefatos |
| OpenAPI / YAML | Contrato em `docs/api.yaml` | Documentação das entradas, respostas e erros da API |
| Python / PowerShell | Scripts locais | Consolidação das evidências, conferências e comandos de validação |

O ambiente local registrado é Windows 11. A instalação final pelo lockfile apontou **zero alertas npm conhecidos naquele momento**, restritos às dependências de teste; isso não representa uma auditoria de segurança completa do sistema. Veja a [auditoria registrada](../evidencias/entrega/auditoria-npm.json).

## 4. O que é possível medir sobre o tempo deste projeto

Há registros reais de duração de comandos:

| Execução local registrada | Duração | Origem |
| --- | ---: | --- |
| Build e validação Java da execução principal | 44,401 s | `tmp/validacao-final.log` |
| Execução Maven do PIT | 29,712 s | `tmp/pit-final.log` |
| Suíte Cypress principal | 31,335 s | [XML versionado](../evidencias/entrega/cypress.xml) |
| Build e validação Java no checkout limpo | 2 min 41 s | `tmp/checkout-final.log` |

Os logs de `tmp/` são locais e não são versionados. São execuções distintas, com condições de cache e ambiente próprias. Esses tempos medem comandos de automação; não medem horas de análise, elaboração de código, revisão, instalação, espera, correções ou trabalho dos alunos. Não devem ser somados para declarar o tempo total de desenvolvimento.

Não houve controle de horas de todas as atividades nem uma equipe de comparação realizando o mesmo escopo sem IA. Portanto, **não há um número comprovado de horas ou percentual economizado neste projeto**. Também não é correto usar a diferença entre datas de commits como medida de esforço humano.

## 5. Evidências reais de pesquisas externas

Foram priorizadas fontes primárias: artigos dos próprios pesquisadores e publicação da instituição que executou o experimento. Os resultados medem contextos e ferramentas diferentes dos deste projeto.

| Fonte | Desenho e amostra | Resultado observado | Limite de aplicação |
| --- | --- | --- | --- |
| [Peng et al., 2023 — artigo do experimento com GitHub Copilot](https://arxiv.org/pdf/2302.06590) | 95 profissionais randomizados; tarefa de implementar servidor HTTP em JavaScript; 35 concluintes em cada grupo | Entre os concluintes: **160,89 min sem Copilot e 71,17 min com Copilot**; redução reportada de **55,8%** no tempo, IC 95% de 21% a 89% | Tarefa delimitada; não mede a entrega completa de um sistema nem o agente usado aqui. Os autores incluem pesquisadores de Microsoft/GitHub e MIT |
| [Cui et al., 2026 — Management Science, publicado em 27/02/2026](https://pubsonline.informs.org/doi/10.1287/mnsc.2025.00535) | Três experimentos randomizados em empresas, com **4.867 desenvolvedores** | **26,08% mais tarefas concluídas**, erro padrão de 10,3 pontos percentuais; efeitos variam entre experimentos | Mede produção de tarefas, não redução direta do tempo total de um projeto. Há autores vinculados à Microsoft e a universidades |
| [Becker et al./METR, 10/07/2025](https://metr.org/blog/2025-07-10-early-2025-ai-experienced-os-dev-study/) | **16 desenvolvedores experientes e 246 tarefas** em repositórios nos quais já trabalhavam; permissão de IA randomizada por tarefa | Com ferramentas do início de 2025, sobretudo Cursor e Claude, o tempo foi **19% maior** | Evidência de que IA também pode atrasar trabalho; contexto de manutenção com alta familiaridade prévia, diferente deste projeto |

No primeiro estudo, a diferença das médias foi **89,72 minutos economizados naquela tarefa**: `160,89 − 71,17`. Esse é um ganho observado no experimento, não no Sistema Bancário. [Fonte: Peng et al., seção de resultados](https://arxiv.org/pdf/2302.06590).

### Atualização relevante de 2026

Em 24/02/2026, a METR publicou dados posteriores: estimativas de **18% menos tempo** entre participantes que retornaram e **4% menos tempo** entre novos participantes. Os intervalos informados para a variação do tempo foram, respectivamente, **−38% a +9%** e **−15% a +9%**, incluindo ausência de ganho e possível atraso. A própria instituição considera o sinal pouco confiável para quantificar o efeito atual, por seleção de participantes/tarefas e dificuldades de medir uso simultâneo de agentes. Assim, o resultado de 2025 não deve ser generalizado a todas as ferramentas de 2026, e a atualização tampouco comprova um ganho neste projeto. [Fonte: METR, atualização do desenho experimental](https://metr.org/blog/2026-02-24-uplift-update/).

## 6. Comparação de tempo por simulação

**Esta seção é uma simulação matemática, não uma medição nem uma previsão do Sistema Bancário.** Para tornar os percentuais comparáveis, adota-se uma base arbitrária de **40 horas sem IA**. Não há evidência de que este trabalho exigiria 40 horas. A tabela mostra o que ocorreria somente se cada efeito externo fosse transferível ao mesmo escopo e padrão de qualidade.

| Hipótese aplicada à base de 40 h sem IA | Tempo calculado com IA | Economia calculada | Natureza do cálculo |
| --- | ---: | ---: | --- |
| Redução de tempo de 55,8% do experimento [Peng et al.](https://arxiv.org/pdf/2302.06590) | **17,68 h** | **22,32 h**, ou 55,8% | `40 × (1 − 0,558)` |
| Produção por hora 26,08% maior, usando como hipótese adicional o resultado de [Cui et al.](https://pubsonline.informs.org/doi/10.1287/mnsc.2025.00535) | **31,73 h** | **8,27 h**, ou aproximadamente 20,69% | `40 ÷ 1,2608`; exige produção proporcional ao tempo e tarefas equivalentes |
| Aumento de tempo de 19% do experimento [METR de 2025](https://metr.org/blog/2025-07-10-early-2025-ai-experienced-os-dev-study/) | **47,60 h** | **−7,60 h**: atraso de 7,60 h | `40 × 1,19` |

O segundo cenário é uma inferência matemática sob hipóteses adicionais; o artigo não demonstrou que este projeto economizaria 20,69% de tempo. **26,08% mais produção não equivale a 26,08% menos tempo.** Os três cenários não constituem uma faixa estatística de previsão e não devem ser promediados, pois representam populações, tarefas e medidas diferentes.

Para obter um ganho real em uma próxima avaliação, devem ser registrados o tempo com IA e o tempo sem IA em tarefas equivalentes, com os mesmos critérios de conclusão e revisão. A conta é:

```text
Economia em horas = tempo sem IA − tempo com IA
Economia percentual = (tempo sem IA − tempo com IA) / tempo sem IA × 100
```

O registro deve incluir análise, implementação, testes, revisão, correções e esperas; tarefas podem ser distribuídas aleatoriamente entre condições para reduzir efeitos de experiência e aprendizado. Os testes existentes ajudam a fixar critérios de aceitação, mas não substituem avaliação de qualidade ou controle do experimento.

## 7. Resultado que pode ser apresentado na entrega

O uso do agente apoiou a construção e validação do sistema, com **113 casos automatizados aprovados**, cobertura integral de instruções e ramificações no recorte de domínio/serviços e **31 mutantes eliminados**. O histórico documenta esse apoio; não permite isolar seu efeito causal sobre prazo ou qualidade.

A literatura consultada contém ganhos reais de tempo e produção, além de resultados de atraso. Para este trabalho, a afirmação sustentada pelas evidências é: **a entrega foi executada com apoio de IA e validada automaticamente; a economia real de tempo em relação ao desenvolvimento sem IA permanece não mensurada**. As simulações da seção 6 servem para comparação de cenários e devem conservar essa identificação em apresentações e citações.
