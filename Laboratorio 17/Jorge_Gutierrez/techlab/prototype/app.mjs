import { matches } from './filter.mjs';
const input=document.querySelector('#query');
const cards=[...document.querySelectorAll('[data-name]')];
function filter() {
  for (const card of cards) card.hidden=!matches(card.dataset.name,input.value);
  document.querySelector('#status').textContent=`${cards.filter(c=>!c.hidden).length} curso(s)`;
}
input.addEventListener('input',filter); filter();
