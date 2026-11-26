import test from 'node:test';
import assert from 'node:assert/strict';
import { matches, normalize } from '../src/main/resources/static/js/filter.mjs';
test('ignora tildes y mayúsculas', () => assert.ok(matches('Programación', 'PROGRAMACION')));
test('consulta vacía muestra todo', () => assert.ok(matches('Java', '  ')));
test('consulta ausente', () => assert.equal(matches('Java', 'Python'), false));
test('normaliza nulos y espacios', () => assert.equal(normalize(null), ''));
