import { request } from './apiClient.js';

const moeda = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' });
const contas = new Map();
document.querySelector('#data-atual').textContent = new Intl.DateTimeFormat('pt-BR', { day: 'numeric', month: 'long', year: 'numeric' }).format(new Date());

function mostrarConta(conta) {
  contas.set(conta.id, conta);
  const area = document.querySelector('#contas');
  area.replaceChildren();
  for (const item of contas.values()) {
    const card = document.createElement('article');
    card.className = 'account-card';
    card.dataset.testid = `conta-card-${item.id}`;
    const top = document.createElement('div'); top.className = 'account-top';
    const titulo = document.createElement('span'); titulo.textContent = `Conta ${item.id}`;
    const status = document.createElement('span'); status.className = 'account-status'; status.textContent = item.ativa ? 'Ativa' : 'Inativa';
    top.append(titulo, status);
    const label = document.createElement('p'); label.className = 'account-label'; label.textContent = 'SALDO DISPONÍVEL';
    const saldo = document.createElement('p'); saldo.className = 'account-balance'; saldo.dataset.testid = `saldo-${item.id}`; saldo.textContent = moeda.format(item.saldo);
    const dono = document.createElement('p'); dono.className = 'account-owner'; dono.textContent = `Cliente ${item.clienteId}`;
    card.append(top, label, saldo, dono); area.append(card);
  }
}

function formulario(seletor, acao) {
  const form = document.querySelector(seletor);
  form.addEventListener('submit', async event => {
    event.preventDefault();
    const botao = form.querySelector('button');
    const feedback = form.querySelector('.feedback');
    feedback.textContent = 'Aguarde, processando…'; feedback.dataset.state = 'pending'; botao.disabled = true;
    try {
      feedback.textContent = await acao(new FormData(form)); feedback.dataset.state = 'success';
    } catch (error) {
      feedback.textContent = error.message; feedback.dataset.state = 'error';
    } finally { botao.disabled = false; }
  });
}

formulario('#form-cliente', async dados => {
  const cliente = await request('/clientes', { method: 'POST', body: { id: Number(dados.get('id')), nome: dados.get('nome'), idade: Number(dados.get('idade')), email: dados.get('email') } });
  document.querySelector('[data-testid=conta-cliente-id]').value = cliente.id;
  return `Cliente ${cliente.nome} cadastrado com sucesso. Agora você pode abrir sua conta.`;
});
formulario('#form-conta', async dados => {
  const conta = await request(`/clientes/${dados.get('clienteId')}/contas`, { method: 'POST', body: { id: Number(dados.get('id')) } });
  mostrarConta(conta);
  return `Conta ${conta.id} aberta e vinculada ao cliente ${conta.clienteId}.`;
});
formulario('#form-consulta', async dados => {
  const conta = await request(`/contas/${dados.get('id')}`); mostrarConta(conta);
  return `Conta ${conta.id} consultada. Saldo atualizado no acompanhamento.`;
});
formulario('#form-transferencia', async dados => {
  const resultado = await request('/transferencias', { method: 'POST', body: { origemId: Number(dados.get('origemId')), destinoId: Number(dados.get('destinoId')), valor: dados.get('valor').trim().replace(',', '.') } });
  // Saldos sao sempre consultados no servidor; o navegador nao calcula movimentacoes.
  try {
    const atualizadas = await Promise.all([request(`/contas/${resultado.origemId}`), request(`/contas/${resultado.destinoId}`)]);
    atualizadas.forEach(mostrarConta);
  } catch {
    return 'Transferência confirmada. Consulte as contas novamente para atualizar os saldos.';
  }
  return `Transferência confirmada da conta ${resultado.origemId} para a conta ${resultado.destinoId}.`;
});
