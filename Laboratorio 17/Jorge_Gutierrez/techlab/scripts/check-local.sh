#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
TMP=$(mktemp -d)
trap 'rm -rf "$TMP"' EXIT
javac -d "$TMP" src/main/java/pe/edu/techlab/domain/CourseRules.java tests/RulesCheck.java
java -cp "$TMP" RulesCheck
node --test tests/filter.test.mjs
node --check src/main/resources/static/js/app.mjs
python3 - <<'PY2'
import xml.etree.ElementTree as ET
import yaml
from pathlib import Path
ET.parse('pom.xml')
for f in ['compose.yml','compose.deploy.yml','src/main/resources/application.yml']:
    yaml.safe_load(Path(f).read_text())
print('PASS: XML Maven y 3 YAML legibles; no implica resolución de dependencias')
PY2
