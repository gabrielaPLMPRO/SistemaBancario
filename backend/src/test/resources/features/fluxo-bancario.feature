# language: pt
Funcionalidade: Abertura e movimentação de contas
  Como operador do sistema bancário
  Quero cadastrar clientes e movimentar suas contas
  Para conferir a transferência e os saldos resultantes

  Contexto:
    Dado que o ambiente de teste está vazio

  @CT-FL01 @RF02 @RF03 @RF09 @RF08 @RF12
  Cenário: Transferir entre contas de clientes cadastrados
    Quando cadastro "Ana" com ID 101 e idade 25
    E cadastro "Bruno" com ID 102 e idade 30
    Então os dois clientes são cadastrados sem conta
    Quando abro a conta 1001 para o cliente 101
    E abro a conta 1002 para o cliente 102
    Então as contas existem vinculadas e com saldo zero
    Dado que a fixture interna define os saldos
      | conta | saldo  |
      | 1001  | 500.00 |
      | 1002  | 100.00 |
    Quando consulto as duas contas
    Então vejo os saldos "500.00" e "100.00"
    Quando transfiro "150.00" da conta 1001 para a conta 1002
    Então a transferência é confirmada
    Quando consulto as duas contas
    Então vejo os saldos "350.00" e "250.00"
    E a soma dos saldos permanece "600.00"

  @RF03
  Esquema do Cenário: Validar fronteiras de idade no cadastro
    Quando cadastro "Ana" com ID 101 e idade <idade>
    Então a API responde <status>
    E existem <quantidade> clientes cadastrados
    Exemplos:
      | idade | status | quantidade |
      | 17    | 422    | 0          |
      | 18    | 201    | 1          |
      | 65    | 201    | 1          |
      | 66    | 422    | 0          |

  @RF12
  Esquema do Cenário: Rejeitar movimentação sem alterar os saldos
    Dado que existem duas contas com saldos "500.00" e "100.00"
    Quando transfiro "<valor>" da conta <origem> para a conta <destino>
    Então a API responde <status>
    Quando consulto as duas contas
    Então vejo os saldos "500.00" e "100.00"
    Exemplos:
      | valor  | origem | destino | status |
      | 0.00   | 1001   | 1002    | 422    |
      | -1.00  | 1001   | 1002    | 422    |
      | 501.00 | 1001   | 1002    | 422    |
      | 0.001  | 1001   | 1002    | 422    |
      | 1.00   | 9999   | 1002    | 404    |
      | 1.00   | 1001   | 9999    | 404    |
      | 1.00   | 1001   | 1001    | 422    |
