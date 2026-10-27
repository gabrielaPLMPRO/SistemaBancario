# Contexto de referência para a evolução do sistema bancário

Leitura realizada em 26/10/2026, a pedido do usuário. Este registro consolida os materiais locais para retomada do trabalho. Não constitui diagnóstico do código nem comprovação de execução dos testes do sistema.

## Materiais lidos

Foram lidos seis trabalhos de apoio (235 páginas), além do compilado metodológico (6 páginas) e do README da pasta. As oito páginas sem texto extraível do trabalho de Campos foram conferidas como imagens.

| Fonte local | Contribuição | Limites a considerar |
| --- | --- | --- |
| [iTest Learning: Um Jogo para o Ensino do Planejamento de Testes de Software](<artigo  1.pdf>) — Farias et al., 2012, 9 páginas | Planejar itens, tipos, níveis, critérios de aceitação, ferramentas e artefatos antes da execução. O planejamento sugerido serve para reflexão e depende do contexto. | O artigo apresenta o jogo; a avaliação de sua eficácia educacional aparece como trabalho futuro. Não é um estudo de modernização com IA. |
| [GameTest: Um jogo para praticar teste de software](<138735+-+GameTest+Um+jogo+para+praticar+teste+de+software.pdf>) — Liberato et al., 2024, 18 páginas | Processo ENgAGED adaptado e avaliação com MEEGA+, incluindo usabilidade, experiência e percepção de aprendizagem. | Avaliação com 31 participantes, público não controlado e foco em percepção e usabilidade. Os autores reconhecem a necessidade de medir os benefícios reais para a aprendizagem. |
| [A eficácia dos testes automatizados para melhorar a satisfação do cliente em projetos de desenvolvimento de software](<93-2024.2 -Michele Ramos Borowski.pdf>) — Borowski e Andrade, 14 páginas | Planejamento antecipado, estabilidade dos testes, cobertura de cenários e análise da relação entre qualidade, usabilidade e satisfação. Compara dois estudos de caso publicados. | Pesquisa bibliográfica e análise qualitativa; declara falta de dados numéricos de satisfação. Automação funcional não demonstra, por si só, melhoria de usabilidade. |
| [Um estudo sobre a aplicação de LLMs no teste de software](MariaLSO_MONO.pdf) — Oliveira, 2025, 60 páginas | Guia de sete tarefas com prompts para classes de equivalência, valores limite, linhas, ramificações, condições e geração de scripts. Usa JaCoCo e PIT como medidas complementares. | Experimento com quatro programas didáticos e um modelo, Gemini Pro 2.5. Houve testes inválidos, falhos e ajustes manuais. Há divergências internas entre algumas contagens, tabelas e descrições; resultados numéricos exigem conferência antes de reutilização acadêmica. |
| [Proposta para Implantação de Automação de Testes de Software usando Behavior Driven Development (BDD) — Estudo de Caso](PropostaImplantacaoAutomacao.pdf) — Campos, 2024, 67 páginas | BDD como colaboração sobre comportamento; Gherkin como especificação; Cucumber, Cypress e Page Objects na implementação. Descreve planejamento, execução, evidências e manutenção. | Recorte de nove cenários em uma interface de um sistema de precificação. Tempos observados nesse caso não são estimativas para este projeto. |
| [Um guia para testes de software em aplicações front-end](RosendoLSC_MONO.pdf) — Costa, 2024, 67 páginas | Introdução gradual a testes unitários, de integração e E2E; exemplos com Jest e Cypress; documentação aberta e atualizável. | Levantamento inicial com 32 respostas e feedback do guia com três desenvolvedores. Exemplos são voltados a aplicações Web; não determinam a tecnologia do sistema bancário. |
| [Compilado metodológico de refatoração e reconstrução com IA](<refatoracao de codigo llm - processo.pdf>), 6 páginas | Organiza engenharia reversa, extração de regras, requisitos, BDD, modernização incremental, comparação de comportamento e avaliação da qualidade dos testes. | É uma síntese gerada com apoio de IA, conforme declaração do próprio documento. As referências citadas dentro dos trabalhos não foram todas fornecidas como artigos originais. |
| [Registro do trabalho de teste de software](README.md) | Relata RF01 a RF12, distribuição entre TDD, BDD e DDD e um exemplo integrado de remoção de cliente. | Os relatos de implementação e aprovação de testes ainda precisam ser confrontados com o código e com uma execução atual da suíte. |

## Interpretação para o projeto

As decisões abaixo são uma adaptação proposta para o sistema bancário, não requisitos impostos por todos os artigos:

1. Mapear módulos, responsabilidades, entradas, saídas e fluxos do código existente.
2. Extrair regras com identificadores e evidências de localização no código; registrar ambiguidades e hipóteses separadamente.
3. Confrontar comportamento implementado, documentação do README e comportamento desejado pelo usuário.
4. Descrever critérios de aceite e cenários BDD antes de implementar mudanças de comportamento.
5. Usar TDD para conduzir a implementação das alterações escolhidas e DDD para orientar linguagem e responsabilidades do domínio.
6. Refatorar em etapas, preservando o comportamento acordado e comparando saídas e efeitos no estado do sistema.
7. Avaliar testes com entradas válidas, inválidas, fronteiras e combinações relevantes; conferir os resultados esperados com as regras de negócio.
8. Medir cobertura e capacidade de detectar defeitos separadamente. JaCoCo e PIT são referências para o contexto Java; sua configuração será decidida após examinar o projeto.
9. Registrar mudanças, testes executados, falhas, ajustes manuais e limitações de cada etapa.

## Cuidados derivados da leitura

- Cobertura elevada mostra código exercitado, mas não prova a correção das asserções nem a ausência de defeitos.
- Um teste gerado que falha requer investigação: pode revelar defeito do sistema, erro no teste ou especificação ambígua.
- Não comentar ou excluir um teste válido apenas para obter uma suíte aprovada. O estudo de Oliveira relata exclusões dos cálculos experimentais, mas essa prática não deve esconder defeitos durante a evolução do sistema.
- Distinguir refatoração, que preserva comportamento, de correção e evolução funcional, que podem alterá-lo intencionalmente.
- BDD não se resume a escrever arquivos Gherkin; envolve entendimento compartilhado dos exemplos e resultados esperados.
- Ferramentas e arquiteturas citadas nos artigos são exemplos. Não há decisão tomada de migrar para Web, APIs, microsserviços ou Clean Architecture.
- Os textos de jogos educacionais contribuem principalmente para planejamento, aprendizagem e avaliação; não fundamentam isoladamente um método de reconstrução de sistemas legados com LLMs.
- O material oferece referências úteis, mas não contém todos os artigos originais citados na revisão sobre LLMs. Quando for necessária uma citação acadêmica direta desses autores, consultar o original correspondente.

## Contexto documental do sistema bancário

O README da pasta relata a seguinte distribuição:

| Abordagem | Requisitos relatados |
| --- | --- |
| TDD | RF02 cadastrar cliente; RF03 validar idade; RF09 cadastrar conta; RF12 transferir valores |
| BDD | RF01 consultar cliente; RF04 ativar cliente; RF05 desativar cliente; RF08 consultar conta |
| DDD | RF06 verificar situação do cliente; RF07 remover cliente; RF10 verificar situação da conta; RF11 remover conta |

Regras documentadas incluem identificadores sem duplicidade, impedimento de cadastro de menores de 18 anos, conta vinculada a cliente existente, remoção apenas de clientes e contas inativos e transferências com valor positivo e saldo suficiente. São informações documentais, ainda não verificadas no código nesta etapa.

## Estado ao concluir esta leitura

Nenhum código do sistema foi alterado e nenhum teste do sistema foi executado nesta tarefa. O próximo trabalho poderá começar pelo diagnóstico do código e pela rastreabilidade entre regras, requisitos e testes existentes.
