export function normalize(value) {
  return String(value ?? '').normalize('NFD').replace(/\p{M}/gu, '')
    .toLocaleLowerCase('es').trim();
}
export function matches(text, query) {
  return normalize(text).includes(normalize(query));
}
