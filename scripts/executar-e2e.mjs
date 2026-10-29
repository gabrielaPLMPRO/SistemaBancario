import { spawn } from 'node:child_process';
import { readFile, writeFile, mkdtemp, mkdir, open } from 'node:fs/promises';
import { tmpdir } from 'node:os';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import cypress from '../e2e/node_modules/cypress/dist/index.js';

const raiz = fileURLToPath(new URL('..', import.meta.url));
delete process.env.ELECTRON_RUN_AS_NODE;
if (!process.env.JAVA_HOME) throw new Error('Configure JAVA_HOME com JDK 21.');
const fixtureDir = await mkdtemp(path.join(tmpdir(), 'bancario-fixture-'));
const classpath = (await readFile(path.join(raiz, 'backend/target/e2e-classpath.txt'), 'utf8')).trim();
const cp = [path.join(raiz, 'backend/target/test-classes'), path.join(raiz, 'backend/target/classes'), classpath].join(path.delimiter);
await mkdir(path.join(raiz, 'e2e/results'), { recursive: true });
const log = await open(path.join(raiz, 'e2e/results/backend.log'), 'w');
const porta = process.env.BANCARIO_E2E_PORT || '18080';
const baseUrl = `http://127.0.0.1:${porta}`;
const servidor = spawn(path.join(process.env.JAVA_HOME, 'bin', process.platform === 'win32' ? 'java.exe' : 'java'),
  ['-cp', cp, 'banco.e2e.E2eApplication', `--server.port=${porta}`, '--server.address=127.0.0.1', `--banco.fixture-dir=${fixtureDir}`],
  { cwd: raiz, stdio: ['ignore', log.fd, log.fd], windowsHide: true });
try {
  let pronto = false;
  for (let i = 0; i < 120; i++) {
    if (servidor.exitCode !== null) throw new Error('Backend encerrou; consulte e2e/results/backend.log.');
    try { if ((await fetch(baseUrl)).ok) { pronto = true; break; } } catch { }
    await new Promise(resolve => setTimeout(resolve, 500));
  }
  if (!pronto) throw new Error('Backend nao iniciou em 60 segundos.');
  process.env.BANCARIO_FIXTURE_DIR = fixtureDir;
  process.env.BANCARIO_BASE_URL = baseUrl;
  const resultado = await cypress.run({ project: path.join(raiz, 'e2e'), browser: 'electron' });
  await writeFile(path.join(raiz, 'e2e/results/resumo.json'), JSON.stringify({
    cypressVersion: resultado.cypressVersion,
    browserName: resultado.browserName,
    browserVersion: resultado.browserVersion,
    nodeVersion: process.version,
    totalTests: resultado.totalTests,
    totalPassed: resultado.totalPassed,
    totalFailed: resultado.totalFailed,
    totalPending: resultado.totalPending,
    totalSkipped: resultado.totalSkipped
  }, null, 2));
  if (resultado.status === 'failed' || resultado.totalFailed !== 0) process.exitCode = 1;
} finally {
  servidor.kill();
  await new Promise(resolve => servidor.exitCode !== null ? resolve() : servidor.once('exit', resolve));
  await log.close();
}
