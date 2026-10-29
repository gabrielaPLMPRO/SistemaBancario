const { defineConfig } = require('cypress');
const fs = require('node:fs/promises');
const path = require('node:path');
const { randomUUID } = require('node:crypto');

module.exports = defineConfig({
  video: false,
  screenshotOnRunFailure: true,
  trashAssetsBeforeRuns: false,
  allowCypressEnv: false,
  viewportWidth: 1280,
  viewportHeight: 900,
  screenshotsFolder: 'screenshots',
  videosFolder: 'videos',
  reporter: 'junit',
  reporterOptions: { mochaFile: 'results/resultado-[hash].xml', toConsole: true },
  e2e: {
    baseUrl: process.env.BANCARIO_BASE_URL || 'http://127.0.0.1:18080',
    supportFile: false,
    specPattern: 'specs/**/*.cy.js',
    setupNodeEvents(on) {
      on('before:browser:launch', (browser, options) => {
        if (browser.name === 'electron' && browser.isHeadless) {
          options.preferences.width = 1600;
          options.preferences.height = 1200;
        }
        return options;
      });
      on('task', {
        async fixture(dados) {
          const diretorio = process.env.BANCARIO_FIXTURE_DIR;
          if (!diretorio) throw new Error('Execute com npm test; a fixture interna nao esta configurada.');
          const id = randomUUID();
          const temporario = path.join(diretorio, `${id}.tmp`);
          const pedido = path.join(diretorio, `${id}.request.json`);
          const resposta = path.join(diretorio, `${id}.response.json`);
          await fs.writeFile(temporario, JSON.stringify(dados));
          await fs.rename(temporario, pedido);
          const limite = Date.now() + 15000;
          while (Date.now() < limite) {
            let texto;
            try { texto = await fs.readFile(resposta, 'utf8'); }
            catch (error) { if (error.code !== 'ENOENT') throw error; }
            if (texto) {
              const resultado = JSON.parse(texto);
              await fs.unlink(resposta);
              if (!resultado.ok) throw new Error(resultado.erro);
              return resultado;
            }
            await new Promise(resolve => setTimeout(resolve, 50));
          }
          throw new Error('A fixture interna nao respondeu no prazo.');
        }
      });
    }
  }
});
