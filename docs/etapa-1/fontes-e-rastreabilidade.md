# Fontes e rastreabilidade metodológica

Atualizado em 29/10/2026. A base do trabalho é a pasta `contexto para IA`, conforme determinação do usuário. O roteiro define a sequência e o recorte; o compilado de práticas com LLM orienta o método; os artigos fornecem os fundamentos e limites. Documentação de ferramentas serve para configurar as ferramentas previstas, sem substituir o método local.

## Materiais consultados diretamente neste incremento

Foi extraído e conferido o texto do [roteiro metodológico](<../../contexto para IA/Roteiro_metodologico_evolucao_sistema_bancario.pdf>), de 13 páginas, e do [compilado de refatoração com LLM](<../../contexto para IA/refatoracao de codigo llm - processo.pdf>), de 6 páginas. Isso complementa a consulta anterior à síntese e ao script gerador do roteiro.

Também foram consultadas seções dos artigos locais pertinentes à preparação e aos testes. A paginação abaixo conta as páginas do arquivo PDF, podendo diferir da numeração impressa:

| Material | Trecho conferido | Aplicação nesta evolução |
| --- | --- | --- |
| [iTest Learning](<../../contexto para IA/artigo  1.pdf>) | Páginas 2 a 4, seção 2 e itens do planejamento | Registrar recorte, itens de teste, níveis, ferramentas e artefatos |
| [Oliveira — LLMs no teste de software](../../contexto%20para%20IA/MariaLSO_MONO.pdf) | Páginas 33 a 36 e 41: métricas, diretrizes, valores limite e execução | Caracterizar fronteiras 17/18/65/66, conferir oráculos e registrar ajustes; cobertura e mutação serão medidas em etapa própria |
| [Campos — automação usando BDD](../../contexto%20para%20IA/PropostaImplantacaoAutomacao.pdf) | Páginas 19, 36, 37 e 43: processo de testes, Gherkin e proposta de solução | Manter execução e documentação do processo; preparar especificações de comportamento antes da reconstrução |

A extração dos oito PDFs foi feita com pypdf, sem modificar os originais. Algumas páginas do material de Campos não têm texto extraível; as conclusões desta tabela se restringem às páginas textuais indicadas. Não se declara uma nova leitura integral de todos os artigos. Os demais materiais seguem registrados na [síntese do contexto](<../../contexto para IA/SINTESE_DO_CONTEXTO.md>) e serão retomados nas etapas pertinentes, especialmente frontend, E2E e avaliação.

## Relação entre orientação e trabalho concreto

| Orientação local | Ação e evidência | Estado |
| --- | --- | --- |
| Roteiro, p. 3; compilado LLM, pp. 1 e 4: analisar módulos, extrair regras e declarar lacunas | `docs/diagnostico-legado.md`, `docs/regras-negocio.md` e `docs/decisoes.md` | Extração realizada; revisão humana pendente |
| Roteiro, p. 3: linha de base e testes de caracterização | 30 testes anteriores e 8 testes adicionais; 38 aprovados antes da reorganização | Executado |
| Roteiro, p. 8: JDK 21, Maven Wrapper, produção/teste separados e descoberta dos RFxx | POMs, Wrapper, `backend/src/main/java` e `backend/src/test/java`; 38 testes encontrados pelo Surefire | Executado |
| Roteiro, p. 9; compilado LLM, p. 2: regressão durante incrementos | Hashes dos 22 arquivos preservados na movimentação; 38 testes aprovados no Maven | Executado neste incremento estrutural |
| Roteiro, p. 5; compilado LLM, pp. 2, 4 e 5: BDD antes do novo comportamento | Próximo incremento: cenários e decisões para os cinco RF | Pendente; sem implementação de API antecipada |
| Roteiro, pp. 9 e 11; compilado LLM, pp. 2 e 6: comparação, cobertura e mutação | Comparação funcional entre versões, JaCoCo e PIT após implementar o recorte | Pendente; nenhuma porcentagem declarada |
| Roteiro, pp. 12 e 13: evidências e transparência de IA | READMEs por etapa, evidências resumidas e `USO_DE_IA.md` | Atualizados; autoria e revisão dos alunos pendentes |

## Escolhas de configuração, separadas da metodologia

Foi adotado um POM agregador na raiz e o módulo `backend/`, com um único Wrapper na raiz. É uma adaptação de organização do exemplo da página 12 do roteiro, que mostra o Wrapper em `backend/`. O comando equivalente dentro de `backend/` é `../mvnw.cmd clean verify`. Isso permite compilar a partir da raiz e preserva a separação prevista para o futuro `frontend/`.

Maven 3.9.16, Wrapper 3.3.4 e versões fixas dos plugins são escolhas de configuração. JUnit 4 foi mantido temporariamente para conferir a mesma regressão antes de migrar para Jupiter/Cucumber, como prevê a preparação gradual do roteiro. Não foram adotadas novas regras de negócio nem dependências Spring nesta etapa.

Foram consultadas as documentações oficiais do [Maven Wrapper](https://maven.apache.org/tools/wrapper/), da [seleção de testes do Surefire](https://maven.apache.org/surefire/maven-surefire-plugin/examples/inclusion-exclusion.html), da [opção release do compilador](https://maven.apache.org/plugins/maven-compiler-plugin/examples/set-compiler-release.html) e da [distribuição portátil do Temurin](https://adoptium.net/installation/archives/). Essas fontes apoiam a configuração técnica; não são a origem das etapas metodológicas.

## Limites de aprovação

Na implementação posterior foram retomados Costa (guia front-end, seção 4.4, página 29 do PDF), Borowski (qualidade/estabilidade da automação, página 10 do PDF) e GameTest (limites da avaliação educacional, página 15 do PDF). Essas fontes orientam a separação dos níveis de teste, o registro dos resultados e a cautela com alegações de qualidade e aprendizagem. GameTest não foi usado para justificar arquitetura bancária ou afirmar ganhos de aprendizagem no nosso projeto. Os detalhes de execução posteriores estão nos READMEs das etapas 2 a 6.

Suíte aprovada demonstra apenas os cenários executados. Não demonstra aprovação humana das RN, ausência de defeitos, satisfação de usuários ou qualidade global do sistema. As políticas posteriores foram decididas e registradas sob autorização explícita do usuário; a revisão acadêmica permanece pendente, conforme docs/decisoes.md.
