const campo = id => cy.get(`[data-testid="${id}"]`);
function cadastrar(id, nome, idade) {
  campo('cliente-id').clear().type(`${id}`);
  campo('cliente-nome').clear().type(nome);
  campo('cliente-idade').clear().type(`${idade}`);
  campo('cliente-email').clear().type(`${nome.toLowerCase()}@example.com`);
  campo('cadastrar').click();
  cy.wait('@cadastro').its('response.statusCode').should('eq', 201);
  campo('cliente-resultado').should('contain', 'cadastrado com sucesso');
}
function abrir(cliente, conta) {
  campo('conta-cliente-id').clear().type(`${cliente}`);
  campo('conta-id').clear().type(`${conta}`);
  campo('abrir-conta').click();
  cy.wait('@abertura').then(({response}) => {
    expect(response.statusCode).to.eq(201);
    expect(response.body.clienteId).to.eq(cliente);
    expect(response.body.saldo).to.eq('0.00');
  });
}
function consultar(id, saldo) {
  campo('consulta-id').clear().type(`${id}`); campo('consultar').click();
  cy.wait('@consulta').its('response.statusCode').should('eq', 200);
  campo(`saldo-${id}`).invoke('text').should('match', new RegExp(saldo));
}
describe('CT-FL01 — cadastro, abertura, consulta e transferencia reais', () => {
  beforeEach(() => {
    cy.task('fixture', { op: 'reset' });
    cy.intercept('POST', '/api/clientes').as('cadastro');
    cy.intercept('POST', '/api/clientes/*/contas').as('abertura');
    cy.intercept('GET', '/api/contas/*').as('consulta');
    cy.intercept('POST', '/api/transferencias').as('transferencia');
    cy.visit('/');
  });
  it('conclui o fluxo e conserva os saldos totais', () => {
    cadastrar(101, 'Ana', 25); cadastrar(102, 'Bruno', 30);
    abrir(101, 1001); abrir(102, 1002);
    cy.task('fixture', { op: 'saldos', saldos: { 1001: '500.00', 1002: '100.00' } });
    consultar(1001, '500,00'); consultar(1002, '100,00');
    campo('transferencia-origem').type('1001'); campo('transferencia-destino').type('1002');
    campo('transferencia-valor').type('150,00'); campo('transferir').click();
    cy.wait('@transferencia').then(({request, response}) => {
      expect(request.body.valor).to.eq('150.00');
      expect(response.statusCode).to.eq(200);
      expect(response.body.saldoOrigem).to.eq('350.00');
      expect(response.body.saldoDestino).to.eq('250.00');
      expect(Number(response.body.saldoOrigem) + Number(response.body.saldoDestino)).to.eq(600);
    });
    campo('transferencia-resultado').should('contain', 'Transferência confirmada');
    consultar(1001, '350,00'); consultar(1002, '250,00');
    cy.document().then(doc => expect(doc.documentElement.scrollWidth).to.be.at.most(doc.documentElement.clientWidth));
    cy.scrollTo('top'); cy.screenshot('CT-FL01-desktop', { capture: 'viewport', overwrite: true });
  });
  it('mostra rejeicoes reais e preserva os saldos', () => {
    cadastrar(101, 'Ana', 25); abrir(101, 1001);
    campo('cadastrar').click(); cy.wait('@cadastro').its('response.statusCode').should('eq', 409);
    campo('cliente-resultado').should('contain', 'Já existe cliente');
    cadastrar(102, 'Bruno', 30); abrir(102, 1002);
    cy.task('fixture', { op: 'saldos', saldos: { 1001: '500.00', 1002: '100.00' } });
    campo('transferencia-origem').type('1001'); campo('transferencia-destino').type('1002'); campo('transferencia-valor').type('501,00');
    campo('transferir').click(); cy.wait('@transferencia').its('response.statusCode').should('eq', 422);
    campo('transferencia-resultado').should('contain', 'Saldo insuficiente');
    consultar(1001, '500,00'); consultar(1002, '100,00');
  });
  it('permite operar em tela pequena sem rolagem horizontal', () => {
    cy.viewport(390, 844);
    cadastrar(101, 'Ana', 25); abrir(101, 1001); consultar(1001, '0,00');
    cy.document().then(doc => expect(doc.documentElement.scrollWidth).to.be.at.most(doc.documentElement.clientWidth));
    cy.scrollTo('top'); cy.screenshot('CT-mobile', { capture: 'fullPage', overwrite: true });
  });
});
