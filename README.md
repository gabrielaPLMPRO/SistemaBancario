# SistemaBancario

# TDD

# RF2 Cadastrar Usuario / RF9 Cadastro de Conta Corrente / RF3 Validar Idade 
  NesteS RFF acabei fazendo os metodos e Exceptions necessarios e depois implementei os testes, ou seja, nao pratiquei TDD de forma correta, mas a fim de suprir esta necessidade eu comentei no codigo os metodos criados e rodei os testes para seguir o fluxo correto do TDD: 
  # Metodos que verificam se o id do cliente é duplicado e se a idade esta dentro do intervalo correto:
  <img width="675" height="325" alt="image" src="https://github.com/user-attachments/assets/f4afe3e8-4ae6-4922-ab2e-ccb20610f277" />

# Metodo que verifica se o id da conta corrente é duplicado:
<img width="617" height="146" alt="image" src="https://github.com/user-attachments/assets/121428a8-1414-48ec-a4f3-ecd84d8a362c" />

# Resultado dos testes sem os metodos: (RED)
<img width="551" height="231" alt="image" src="https://github.com/user-attachments/assets/b8978c36-e6b6-4e7e-ba53-2d30be7d7d3a" />

# Restaurando os métodos: 
<img width="640" height="146" alt="image" src="https://github.com/user-attachments/assets/8a29f1f5-79f9-4a02-a423-764d53505cd0" />
<img width="682" height="374" alt="image" src="https://github.com/user-attachments/assets/0d41cd28-4886-40a1-805e-1bd510ae3b1f" />

# Compilando testes e testes aprovados: (GREEN): 
<img width="550" height="229" alt="image" src="https://github.com/user-attachments/assets/3d857c75-7fcd-4be4-8cfb-c4309f742985" />

# Tive de alterar a MAIN pois nao estava sendo construido o objeto da conta corrente (REFACTOR).
<img width="651" height="24" alt="image" src="https://github.com/user-attachments/assets/ce30ff27-1b82-49f7-8322-1adc41837a95" />

#RF12 Transferir valor entre contas
Construi um teste para a transferencia entre contas, ele roda e aparentemente nao causa nenhum erro, porem eu ainda nao implementei a funcao no sistema, o metodo apenas existe (GREEN)
<img width="1215" height="442" alt="image" src="https://github.com/user-attachments/assets/08b5e11f-447b-4aaf-ac45-d71e90bfa05c" />



# Codei o case 6: a transferencia no main com as entradas fornecidas (REFACTOR)
<img width="576" height="226" alt="image" src="https://github.com/user-attachments/assets/d716ff52-2352-4d0e-8c41-a602516e2a9b" />

# Fiz um teste unitario, modifiquei o valor nas contas para testar o sistema de forma manual: (REFACTOR)
<img width="634" height="127" alt="image" src="https://github.com/user-attachments/assets/daecbd5c-f512-4e88-803b-02976b1faa0d" />
<img width="332" height="271" alt="image" src="https://github.com/user-attachments/assets/a66393b9-ec03-411e-ae7d-81b9d3d7183d" />
<img width="292" height="241" alt="image" src="https://github.com/user-attachments/assets/6211ac37-7480-4584-a69f-5fcf4ffa3540" />

# Finalmente rodei os testes ja montados e com a refatoracao do main: (GREEN)
<img width="969" height="285" alt="image" src="https://github.com/user-attachments/assets/08bd778d-7fa3-436e-9249-498f5c5ebd8c" />
<img width="524" height="80" alt="image" src="https://github.com/user-attachments/assets/e412443e-aa44-4465-983f-de020888f42c" />



