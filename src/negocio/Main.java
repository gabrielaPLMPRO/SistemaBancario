package negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	private static GerenciadoraClientes gerClientes;
	private static GerenciadoraContas gerContas;
	
	public static void main(String[] args) {
		
		inicializaSistemaBancario(); // criando algumas contas e clientes fict�cios
		
		Scanner sc = new Scanner(System.in);
		boolean continua = true;
		
		while (continua){
			
			printMenu();
			
			int opcao = leituraInt(sc);
			
			switch (opcao) {
			// Consultar por um cliente
			case 1:
				System.out.print("Digite o ID do cliente: ");
				int idCliente = leituraInt(sc);
				Cliente cliente = gerClientes.pesquisaCliente(idCliente);
				
				if(cliente != null)
					System.out.println(cliente.toString());
				else
					System.out.println("Cliente n�o encontrado!");
				
				pulalinha();
				break;

				//cadastrar um cliente
			case 2:
				System.out.print("Digite o ID do cliente: ");
				int idClienteCadastro = leituraInt(sc);
				System.out.print("Digite o nome do cliente: ");
				String nomeCliente = leituraTexto(sc);
				System.out.print("Digite a idade do cliente: ");
				int idadeCliente = leituraInt(sc);
				System.out.print("Digite o email do cliente: ");
				String emailCliente = leituraTexto(sc);
				System.out.print("Digite o ID da conta corrente do cliente: ");
				int idContaCliente = leituraInt(sc);
				try {
					gerClientes.validaClienteNaoDuplicado(idClienteCadastro);
					gerContas.validaContaNaoDuplicada(idContaCliente);
					gerClientes.validaIdade(idadeCliente);

					Cliente novoCliente = new Cliente(idClienteCadastro, nomeCliente, idadeCliente, emailCliente, idContaCliente, true);
					gerClientes.adicionaCliente(novoCliente);
					gerContas.adicionaConta(new ContaCorrente(idContaCliente, 0, true));	
					System.out.println("Cliente cadastrado com sucesso!");
				} catch (ClienteJaExisteException e) {
					System.out.println(e.getMessage());
				} catch (ContaJaExisteException e) {
					System.out.println(e.getMessage());
				} catch (IdadeNaoPermitidaException e) {
					System.out.println(e.getMessage());
				}

				pulalinha();
				break;
			case 3:
				System.out.print("Digite o ID da conta: ");
				int idConta = leituraInt(sc);
				ContaCorrente conta = gerContas.pesquisaConta(idConta);
				
				if(conta != null)
					System.out.println(conta.toString());
				else
					System.out.println("Conta n�o encontrado!");
				
				pulalinha();
				break;

			// Ativar um cliente
			case 4:
				
				System.out.print("Digite o ID do cliente: ");
				int idCliente2 = leituraInt(sc);
				Cliente cliente2 = gerClientes.pesquisaCliente(idCliente2);
				
				if(cliente2 != null){
					cliente2.setAtivo(true);
					System.out.println("Cliente ativado com sucesso!");
				}
				else
					System.out.println("Cliente n�o encontrado!");
			
				pulalinha();
				break;
				
			// Desativar um cliente
			case 5:
				
				System.out.print("Digite o ID do cliente: ");
				int idCliente3 = leituraInt(sc);
				Cliente cliente3 = gerClientes.pesquisaCliente(idCliente3);
				
				if(cliente3 != null){
					cliente3.setAtivo(false);
					System.out.println("Cliente desativado com sucesso!");
				}
				else
					System.out.println("Cliente n�o encontrado!");
				
				pulalinha();
				break;
			
			//Transferir dinheiro entre contas
			case 6:
				System.out.print("Digite o ID da conta de origem: ");
				int idContaOrigem = leituraInt(sc);
				System.out.print("Digite o ID da conta de destino: ");
				int idContaDestino = leituraInt(sc);
				System.out.print("Digite o valor a ser transferido: ");
				double valor = Double.parseDouble(sc.nextLine().trim());
				gerContas.transfereValor(idContaOrigem, valor, idContaDestino);
				pulalinha();
				break;

			
			
				// Sair
			case 7:
				continua = false;
				System.out.println("################# Sistema encerrado #################");
				break;
				
			default:
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				break;
				
			} 
			
		}
	
		
	}

	private static int leituraInt(Scanner sc) {
		while (true) {
			try {
				return Integer.parseInt(sc.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Entrada inválida. Digite um número inteiro.");
			}
		}
	}

	private static String leituraTexto(Scanner sc) {
		return sc.nextLine();
	}

	private static void pulalinha() {
		System.out.println("\n");
	}

	/**
	 * Imprime menu de op��es do nosso sistema banc�rio
	 */
	private static void printMenu() {
		
		System.out.println("O que voc� deseja fazer? \n");
		System.out.println("1) Consultar por um cliente");
		System.out.println("2) Cadastrar um cliente");
		System.out.println("3) Consultar por uma conta corrente");
		System.out.println("4) Ativar um cliente");
		System.out.println("5) Desativar um cliente");
		System.out.println("6) Transferir dinheiro entre contas");
		System.out.println("7) Sair");
		System.out.println();
		
	}

	/**
	 * M�todo que cria e insere algumas contas e clientes no sistema do banco,
	 * apenas para realiza��o de testes manuais atrav�s do m�todo main acima.
	 */
	private static void inicializaSistemaBancario() {
		// criando lista vazia de contas e clientes
		List<ContaCorrente> contasDoBanco = new ArrayList<>();
		List<Cliente> clientesDoBanco = new ArrayList<>();
		
		// criando e inserindo duas contas na lista de contas correntes do banco
		ContaCorrente conta01 = new ContaCorrente(1, 500, true);
		ContaCorrente conta02 = new ContaCorrente(2, 500, true);
		contasDoBanco.add(conta01);
		contasDoBanco.add(conta02);
		
		// criando dois clientes e associando as contas criadas acima a eles
		Cliente cliente01 = new Cliente(1, "Maria Silva", 31, "mariasilva@gmail.com", conta01.getId(), true);
		Cliente cliente02 = new Cliente(2, "Felipe Augusto", 34, "felipeaugusto@gmail.com", conta02.getId(), true);
		// inserindo os clientes criados na lista de clientes do banco
		clientesDoBanco.add(cliente01);
		clientesDoBanco.add(cliente02);
		
		gerClientes = new GerenciadoraClientes(clientesDoBanco);
		gerContas = new GerenciadoraContas(contasDoBanco);
		
	}
	
}

