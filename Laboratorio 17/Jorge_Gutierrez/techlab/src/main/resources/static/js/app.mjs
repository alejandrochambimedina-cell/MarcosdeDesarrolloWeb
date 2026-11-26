import { matches } from './filter.mjs';
const input = document.querySelector('#filter');
const rows = [...document.querySelectorAll('[data-course-row]')];
const count = document.querySelector('#count');
function filter() {
  let visible = 0;
  for (const row of rows) {
    const text = `${row.cells[0].textContent} ${row.cells[1].textContent}`;
    row.hidden = !matches(text, input.value);
    if (!row.hidden) visible++;
  }
  count.textContent = `${visible} curso(s)`;
}
if (input && count) { input.addEventListener('input', filter); filter(); }
for (const form of document.querySelectorAll('[data-confirm]')) {
  form.addEventListener('submit', event => {
    if (!window.confirm(form.dataset.confirm)) event.preventDefault();
  });
}
