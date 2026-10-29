"""Consolida apenas resultados reais; execute depois de verify, PIT e Cypress."""
from pathlib import Path
from datetime import datetime
from collections import Counter
import hashlib
import json
import shutil
import subprocess
import xml.etree.ElementTree as ET
import zipfile
import argparse

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "evidencias/entrega"
OUT.mkdir(parents=True, exist_ok=True)

def git(*args):
    return subprocess.check_output(["git", *args], cwd=ROOT, text=True).strip()

suites = [ET.parse(p).getroot() for p in sorted((ROOT / "backend/target/surefire-reports").glob("TEST-*.xml"))]
assert suites, "Execute os testes antes de registrar evidencias"
totals = {key: sum(int(s.attrib[key]) for s in suites) for key in ("tests", "failures", "errors", "skipped")}
assert totals["tests"] > 38 and totals["failures"] == totals["errors"] == totals["skipped"] == 0, totals

jacoco = ET.parse(ROOT / "backend/target/site/jacoco/jacoco.xml").getroot()
coverage = {}
for package in jacoco.findall("package"):
    if package.attrib["name"] in ("banco/domain", "banco/application"):
        counters = {c.attrib["type"]: {k: int(c.attrib[k]) for k in ("covered", "missed")} for c in package.findall("counter")}
        coverage[package.attrib["name"]] = counters
        for kind in ("INSTRUCTION", "BRANCH"):
            counts = counters[kind]
            assert counts["covered"] / (counts["covered"] + counts["missed"]) >= .90
assert len(coverage) == 2

mutations = ET.parse(ROOT / "backend/target/pit-reports/mutations.xml").getroot()
mutation_status = dict(Counter(m.attrib["status"] for m in mutations))
mutation_total = len(mutations)
assert mutation_total and mutation_status.get("KILLED", 0) / mutation_total >= .80, mutation_status

features = json.loads((ROOT / "backend/target/cucumber/relatorio.json").read_text(encoding="utf-8"))
scenarios = [e for f in features for e in f.get("elements", []) if e.get("type") == "scenario"]
assert scenarios and all(step["result"]["status"] == "passed" for s in scenarios for step in s.get("steps", []))

cypress_path = max((ROOT / "e2e/results").glob("resultado-*.xml"), key=lambda p: p.stat().st_mtime)
cypress = ET.parse(cypress_path).getroot()
browser = json.loads((ROOT / "e2e/results/resumo.json").read_text(encoding="utf-8"))
assert browser["totalTests"] == browser["totalPassed"] == 3
assert browser["totalFailed"] == browser["totalPending"] == browser["totalSkipped"] == 0
assert browser["cypressVersion"] == json.loads((ROOT / "e2e/package.json").read_text(encoding="utf-8"))["devDependencies"]["cypress"]
assert int(cypress.attrib["tests"]) == 3 and int(cypress.attrib["failures"]) == 0
assert not cypress.findall(".//failure") and not cypress.findall(".//skipped")
jar = ROOT / "backend/target/sistema-bancario-backend-0.1.0-SNAPSHOT.jar"
with zipfile.ZipFile(jar) as archive:
    assert not any(n.startswith("BOOT-INF/classes/negocio/") or n.startswith("BOOT-INF/classes/banco/e2e/") for n in archive.namelist())

def digest(root):
    files = sorted([*root.glob("backend/src/**/*.java"), *root.glob("backend/src/**/*.feature"), *root.glob("frontend/*"), root / "pom.xml", root / "backend/pom.xml", root / "e2e/package-lock.json", root / "e2e/cypress.config.cjs", root / "e2e/specs/fluxo.cy.js", root / "scripts/executar-e2e.mjs"])
    result = hashlib.sha256()
    for path in files:
        result.update(path.relative_to(root).as_posix().encode() + b"\0")
        result.update(path.read_bytes().replace(b"\r\n", b"\n"))
    return result.hexdigest()

args = argparse.ArgumentParser()
args.add_argument("--checkout", type=Path, help="Checkout limpo verificado, opcional")
checkout = args.parse_args().checkout
checkout_result = None
if checkout:
    checkout = checkout.resolve()
    checked = [ET.parse(p).getroot() for p in (checkout / "backend/target/surefire-reports").glob("TEST-*.xml")]
    checked_totals = {k: sum(int(r.attrib[k]) for r in checked) for k in totals}
    assert checked_totals == totals
    checked_e2e = ET.parse(max((checkout / "e2e/results").glob("resultado-*.xml"), key=lambda p:p.stat().st_mtime)).getroot()
    assert int(checked_e2e.attrib["tests"]) == 3 and int(checked_e2e.attrib["failures"]) == 0
    assert not checked_e2e.findall(".//failure") and not checked_e2e.findall(".//skipped")
    checked_browser = json.loads((checkout / "e2e/results/resumo.json").read_text(encoding="utf-8"))
    assert checked_browser["totalTests"] == checked_browser["totalPassed"] == 3
    assert checked_browser["totalFailed"] == checked_browser["totalPending"] == checked_browser["totalSkipped"] == 0
    assert checked_browser["cypressVersion"] == browser["cypressVersion"]
    assert digest(checkout) == digest(ROOT)
    assert not subprocess.check_output(["git","status","--porcelain"],cwd=checkout,text=True).strip()
    checked_jar = checkout / "backend/target/sistema-bancario-backend-0.1.0-SNAPSHOT.jar"
    checkout_result = {"commit": subprocess.check_output(["git","rev-parse","HEAD"],cwd=checkout,text=True).strip(), "junit": checked_totals, "cypress": 3, "cypress_version": checked_browser["cypressVersion"], "fontes_conferem": True, "jar_identico": checked_jar.read_bytes() == jar.read_bytes()}

summary = {
    "registrado_em": datetime.now().astimezone().isoformat(),
    "commit_fonte": git("rev-parse", "HEAD"),
    "branch": git("branch", "--show-current"),
    "arvore_com_alteracoes": bool(git("status", "--porcelain")),
    "sha256_conjunto_fontes": digest(ROOT),
    "normalizacao_digest": "Caminhos relativos e conteudo textual com finais de linha LF",
    "ambiente": {"java": "Temurin 21.0.13+9", "maven": "3.9.16", "spring_boot": "3.5.16", "node": browser["nodeVersion"], "cypress": browser["cypressVersion"], "browser": browser["browserName"] + " " + browser["browserVersion"] + " headless", "sistema": "Windows 11"},
    "junit": {"totais": totals, "classes": [{"nome": s.attrib["name"], **{k: int(s.attrib[k]) for k in totals}} for s in suites]},
    "cucumber": {"cenarios": len(scenarios), "resultado": "passed"},
    "jacoco_recorte": coverage,
    "pit": {"total": mutation_total, "status": mutation_status},
    "cypress": {"tests": 3, "failures": 0, "testes": [t.attrib["name"] for t in cypress.findall(".//testcase")]},
    "jar_sha256": hashlib.sha256(jar.read_bytes()).hexdigest(),
    "fixture_e_legado_ausentes_do_jar": True,
    "verificacao_checkout_limpo": checkout_result,
    "limites": ["Metricas de dominio/aplicacao nao representam todo o produto.", "Sem avaliacao de usuarios ou revisao academica comprovada.", "A CI remota possui execucao propria; consultar seu status no GitHub."]
}
(OUT / "resultados.json").write_text(json.dumps(summary, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
for source, target in [
    (ROOT / "backend/target/site/jacoco/jacoco.xml", "jacoco.xml"),
    (ROOT / "backend/target/pit-reports/mutations.xml", "mutacoes.xml"),
    (ROOT / "backend/target/cucumber/relatorio.json", "cucumber.json"),
    (cypress_path, "cypress.xml"),
    (ROOT / "e2e/screenshots/fluxo.cy.js/CT-FL01-desktop.png", "desktop.png"),
    (ROOT / "e2e/screenshots/fluxo.cy.js/CT-mobile.png", "mobile.png")
]:
    shutil.copyfile(source, OUT / target)
print(json.dumps({"junit": totals, "cucumber": len(scenarios), "pit": mutation_status, "cypress": 3}, ensure_ascii=False))
