

# Trabalho 1 — Teste de Software

# [Link do docs](https://www.exemplo.com)

## TDD

# RF02 – Cadastrar cliente

Regra testada:

O sistema deve cadastrar um cliente válido, desde que seu identificador ainda não esteja sendo utilizado por outro cliente.

## 🔴 RED

Teste criado:

`deveCadastrarClienteValido()`

Resultado obtido:

O teste não compilou porque o método `cadastraCliente` ainda não existia na classe `GerenciadoraClientes`.

Por que falhou?

O teste foi criado primeiro para definir o comportamento esperado: validar a duplicidade do identificador e adicionar um cliente válido à lista de clientes do banco.

## 🟢 GREEN

Alteração realizada:

Foi criado o método `cadastraCliente` na classe `GerenciadoraClientes`. O método verifica se o identificador do novo cliente já está cadastrado e, caso não esteja, adiciona o cliente à lista.

Resultado:

O teste `deveCadastrarClienteValido()` passou.

## 🔵 REFACTOR

Refatoração realizada:

Não foi necessária.

Justificativa:

O método possui responsabilidade clara e reutiliza `validaClienteNaoDuplicado()` e `adicionaCliente()`.

Resultado final:

Todos os testes permaneceram aprovados.

# RF03 – Validar idade do cliente

Regra testada:

O sistema não deve cadastrar clientes menores de 18 anos.

## 🔴 RED

Teste criado:

`naoDeveCadastrarClienteMenorDeIdade()`

Resultado obtido:

A exceção `IdadeNaoPermitidaException` não foi lançada.

Por que falhou?

O método cadastrava o cliente sem validar a idade.

## 🟢 GREEN

Alteração realizada:

O método passou a chamar `validaIdade()` antes do cadastro e declarar a exceção.

Resultado:

O teste passou.

## 🔵 REFACTOR

Não foi necessária refatoração.

# RF09 – Cadastrar conta corrente

Regra testada:

Cadastrar conta apenas para clientes existentes e impedir identificadores duplicados.

## 🔴 RED

Teste criado:

`deveCadastrarContaParaClienteExistente()`

Resultado:

O método `cadastraContaParaCliente()` não existia.

## 🟢 GREEN

Implementação:

Foi criado o método que valida duplicidade e adiciona a conta.

Resultado:

Teste aprovado.

## RF09 – Cliente inexistente

Regra testada:

Não permitir cadastro de conta para cliente inexistente.

### 🔴 RED

Teste:

`naoDeveCadastrarContaParaClienteInexistente()`

Resultado:

O método retornava `true` e cadastrava a conta.

### 🟢 GREEN

Correção:

Foi adicionada a verificação da existência do cliente.

Resultado:

Teste aprovado.

### 🔵 REFACTOR

Nenhuma alteração adicional.

# RF12 – Transferir valores entre contas

Regra testada:

Aceitar apenas valores positivos e com saldo suficiente.

## Caso 1 — Valor zero

### 🔴 RED

Teste:

`naoDeveTransferirValorZero()`

Resultado:

Transferência de valor zero era aceita.

### 🟢 GREEN

Correção:

A condição passou a exigir valor diferente de zero.

## Caso 2 — Valor negativo

### 🔴 RED

Teste:

`naoDeveTransferirValorNegativo()`

Resultado:

Valores negativos alteravam os saldos incorretamente.

### 🟢 GREEN

Correção:

A regra passou para `valor > 0`.

## Caso 3 — Saldo insuficiente

Teste:

`naoDeveTransferirQuandoSaldoForInsuficiente()`

Resultado:

Passou na primeira execução.

### 🔵 REFACTOR

Foi criado o método auxiliar `criaGerenciadoraParaTransferencia()` para eliminar repetição nos testes.

# BDD

# RF01 – Consultar cliente

## Funcionalidade

Consulta de cliente por identificador.

## História

> Como usuário do sistema bancário, quero consultar um cliente por seu identificador para visualizar seus dados.

### Cenário 1 — Cliente existente

Dado que existe um cliente com ID 1

Quando consultar o ID 1

Então o sistema retorna o cliente correspondente.

Teste automatizado:

`deveConsultarClienteExistente()`

### Cenário 2 — Cliente inexistente

Dado que não existe cliente com ID 99

Quando consultar o ID 99

Então o sistema retorna `null`.

Teste automatizado:

`deveRetornarNullParaClienteInexistente()`

Resultado final: ambos os cenários passaram sem alterações no código.

# RF04 – Ativar cliente

## História

> Como usuário, quero ativar um cliente para que ele fique ativo.

### Cenário 1 — Ativar cliente inativo

Teste: `deveAtivarClienteInativo()`

* RED: método inexistente

* GREEN: criação de `ativaCliente(int)`

* Resultado: aprovado

### Cenário 2 — Cliente inexistente

Teste: `naoDeveAtivarClienteInexistente()`

O método retorna `false` quando o cliente não existe.

# RF05 – Desativar cliente

## História

> Como usuário, quero desativar um cliente para que ele fique inativo.

### Cenário 1 — Cliente ativo

Teste: `deveDesativarClienteAtivo()`

* RED: método inexistente

* GREEN: criação de `desativaCliente(int)`

* Resultado: aprovado

### Cenário 2 — Cliente inexistente

Teste: `naoDeveDesativarClienteInexistente()`

Retorna `false`.

# RF08 – Consultar conta corrente

## Funcionalidade

Consulta de conta por identificador.

### Cenário 1 — Conta existente

Teste: `deveConsultarContaExistente()`

Passou na primeira execução.

### Cenário 2 — Conta inexistente

Teste: `deveRetornarNullParaContaInexistente()`

Retorna `null`.

# DDD

# RF06 – Verificar situação do cliente

## Conceito de domínio

Cliente e sua situação (ativo/inativo).

### Regra de negócio

Todo cliente possui uma situação própria.

### Problema identificado

`GerenciadoraClientes` verificava diretamente a situação do cliente.

### Modelagem proposta

* `Cliente` é responsável por informar seu estado (`isAtivo()`).

* `GerenciadoraClientes` apenas localiza o cliente.

### Resultado

A responsabilidade foi delegada corretamente sem quebrar os testes.

# RF07 – Remover cliente

## Regra de negócio

Um cliente só pode ser removido quando estiver inativo.

### Problema

Clientes ativos podiam ser removidos.

### Modelagem

* Localizar cliente

* Verificar `isAtivo()`

* Remover apenas se estiver inativo

### Resultado

O teste passou após a alteração.

# RF10 – Verificar situação da conta corrente

## Conceito de domínio

Conta corrente e sua situação.

### Modelagem proposta

* `ContaCorrente` informa seu estado através de `isAtiva()`.

* `GerenciadoraContas` apenas localiza a conta.

### Resultado

Os testes permaneceram aprovados.

# RF11 – Remover conta

## Regra de negócio

Uma conta corrente somente pode ser removida quando estiver inativa.

### Problema

O método removia contas ativas.

### Solução

A remoção passou a verificar a situação da entidade `ContaCorrente`.

### Resultado

* Conta ativa → não remove

* Conta inativa → remove com sucesso

## Resultado Geral

|
Categoria

|

Requisitos

|
| --- | --- |
|

TDD

|

RF02, RF03, RF09, RF12

|
|

BDD

|

RF01, RF04, RF05, RF08

|
|

DDD

|

RF06, RF07, RF10, RF11

|

# Integração das três abordagens
# Requisito escolhido: RF07 — Remover cliente
O requisito consiste em permitir a remoção de um cliente somente quando ele estiver inativo. Um cliente que esteja ativo não deve ser removido.
A análise desse requisito pode ser realizada utilizando conjuntamente TDD, BDD e DDD, pois cada abordagem observa uma parte diferente do desenvolvimento.

# TDD: Quais testes conduziriam a implementação?
No TDD, a implementação seria conduzida pelos testes automatizados.
Para o RF07, seriam necessários pelo menos dois cenários principais:

## Não permitir a remoção de um cliente ativo:
<img width="559" height="158" alt="image" src="https://github.com/user-attachments/assets/81b97b13-1777-484b-ad3b-bc363751f2db" />


O teste verifica que a operação retorna false e que o cliente continua cadastrado. 


## Permitir a remoção de um cliente inativo:
<img width="566" height="155" alt="image" src="https://github.com/user-attachments/assets/5c9e4245-7e22-4310-ae55-3a5a2ed6daae" />
Nesse caso, o teste verifica que a remoção é realizada e que o cliente deixa de existir na lista. 

# BDD: Quais comportamentos e cenários descrevem o requisito?
No BDD, o mesmo requisito é descrito pela perspectiva do comportamento esperado do sistema, do ponto de vista do usuário em si.
História:
Como funcionário do banco, quero remover clientes que estejam inativos para manter o cadastro atualizado.
## Cenário 1:  Remover cliente inativo
Dado que existe um cliente cadastrado
 E que o cliente está inativo
 Quando for solicitada a remoção do cliente
 Então o cliente deverá ser removido do cadastro.

## Cenário 2: Tentar remover cliente ativo
Dado que existe um cliente cadastrado
 E que o cliente está ativo
 Quando for solicitada a remoção do cliente
 Então o sistema não deverá remover o cliente.
# DDD: Quais conceitos e regras estão envolvidos e onde deveriam estar representados? 
Conceito de domínio envolvido:
Cliente, situação do cliente e remoção de cliente.
## Regra de negócio:
Um cliente somente poderá ser removido do sistema quando estiver inativo. Clientes ativos não poderão ser removidos diretamente.

Eles seguem representados pelas classes Cliente e GerenciadoraClientes.

BDD
↓
O que o sistema deve fazer?

Um cliente inativo pode ser removido.
Um cliente ativo não pode ser removido.

        ↓

DDD
↓
Como essa regra pertence ao domínio?

Cliente possui uma situação.
A remoção deve respeitar a situação do cliente.
A regra fica associada à responsabilidade de gerenciamento do cliente.

        ↓

TDD
↓
Como validar essa regra durante o desenvolvimento?

Teste 1:
cliente ativo → não remove

Teste 2:
cliente inativo → remove

        ↓

Sistema + Testes Automatizados


# Uso de IA:

## Prompts usados: “Me ajuda a entender a proposta do professor, no momento eu finalizei o cadastro de cliente” 
## Resposta recebida: 
O que o professor espera de vocês
Ele dividiu os 12 requisitos em 3 partes. Cada abordagem trabalha 4 requisitos diferentes.
Markdown.md colado
TDD (desenvolver pelos testes)
Você já começou aqui com Cadastrar Cliente.
Requisitos 2, 3, 9 e 12
BDD (descrever o comportamento)
Não é código primeiro; são histórias e cenários.
Requisitos 1, 4, 5 e 8
DDD (organizar as regras do domínio)
Aqui vocês analisam se as classes representam corretamente o banco.
Requisitos 6, 7, 10 e 11


Todos os requisitos foram implementados e a suíte completa de testes permaneceu aprovada.
