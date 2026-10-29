# Roteiro de apresentação — 10 a 12 minutos

Este documento prepara a apresentação prevista na página 12 do roteiro local. A apresentação ao professor e a explicação individual pelos alunos são atividades humanas; não são marcadas como realizadas pela automação.

1. **1 minuto — problema e recorte.** Mostrar RF02, RF03, RF09, RF08 e RF12 e sua conexão: cadastrar, validar idade, abrir, consultar e transferir.
2. **2 minutos — engenharia reversa.** Mostrar as RN extraídas, a idade máxima de 65 e a lacuna de vínculo encontrada. Diferenciar comportamento observado de melhoria decidida. Abrir `docs/decisoes.md`.
3. **2 minutos — arquitetura.** Mostrar `frontend/apiClient.js`, controller, serviço e repositório. No navegador, mostrar uma chamada HTTP real. Explicar que o frontend não calcula saldos.
4. **3 minutos — CT-FL01.** Executar `npm --prefix e2e test`, mostrar a abertura com saldo zero, a fixture interna, transferência 150 e saldos 350/250. Mostrar a rejeição por saldo insuficiente.
5. **2 minutos — qualidade.** Abrir JaCoCo e PIT, delimitar o escopo medido e mostrar um mutante eliminado. Explicar por que 100% no recorte não garante ausência de defeitos.
6. **1 a 2 minutos — entrega e autoria.** Mostrar README, comparação diferencial, PR e relatórios da CI. Explicar o apoio de IA e as revisões que o grupo efetivamente realizou.

Antes da apresentação: preencher os autores em `docs/contribuicoes.md`, revisar as regras e conseguir reproduzir os comandos. Usar os screenshots como apoio se o ambiente ao vivo não estiver disponível. O tempo e o formato finais devem respeitar as orientações do professor.
