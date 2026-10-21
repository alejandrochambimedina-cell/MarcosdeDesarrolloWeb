document.addEventListener('DOMContentLoaded', () => {

  // Clave donde se guarda la ruta elegida (la leen buses.js y boleto.js).
  const CLAVE_RUTA = 'rutaSeleccionada';

  // 1. Animación de contadores de métricas
  const stats = document.querySelectorAll('.stat-number');
  stats.forEach(stat => {
    const target = +stat.getAttribute('data-target');
    let count = 0;
    const speed = target / 30;

    const updateCount = () => {
      count += speed;
      if (count < target) {
        stat.innerText = target === 100 ? `${Math.ceil(count)}%` : `+${Math.ceil(count)}`;
        setTimeout(updateCount, 40);
      } else {
        stat.innerText = target === 100 ? `${target}%` : `+${target}`;
      }
    };
    updateCount();
  });

  // 2. Buscador: valida la ruta, la guarda y pasa al primer paso (Servicios)
  const searchForm = document.getElementById('search-form');
  const mensajeRuta = document.getElementById('mensaje-ruta');

  if (searchForm) {
    searchForm.addEventListener('submit', (e) => {
      e.preventDefault();

      const origen = document.getElementById('origen').value;
      const destino = document.getElementById('destino').value;

      if (origen === destino) {
        mensajeRuta.textContent = 'El origen y el destino no pueden ser la misma ciudad.';
        mensajeRuta.hidden = false;
        return;
      }

      mensajeRuta.hidden = true;
      localStorage.setItem(CLAVE_RUTA, JSON.stringify({ origen, destino }));
      window.location.href = '/servicios';
    });
  }

  // 3. Rutas populares: guardan su ruta y el enlace lleva a Servicios
  document.querySelectorAll('.btn-route').forEach(boton => {
    boton.addEventListener('click', () => {
      localStorage.setItem(CLAVE_RUTA, JSON.stringify({
        origen: boton.dataset.origen,
        destino: boton.dataset.destino
      }));
    });
  });

  // 4. Acordeón (Preguntas frecuentes)
  document.querySelectorAll('.faq-question').forEach(question => {
    question.addEventListener('click', () => {
      question.parentElement.classList.toggle('active');
    });
  });

});
