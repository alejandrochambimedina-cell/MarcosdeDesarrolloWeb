document.addEventListener('DOMContentLoaded', () => {
  
  // 1. Efecto Scroll en Navbar
  const header = document.getElementById('main-header');
  window.addEventListener('scroll', () => {
    if (window.scrollY > 50) {
      header.classList.add('scrolled');
    } else {
      header.classList.remove('scrolled');
    }
  });

  // 2. Animación de Contadores de Métricas
  const stats = document.querySelectorAll('.stat-number');
  const animateStats = () => {
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
  };
  animateStats();

  // 3. Manejo del Formulario de Búsqueda
  const searchForm = document.getElementById('search-form');
  if (searchForm) {
    searchForm.addEventListener('submit', (e) => {
      e.preventDefault();
      const origen = document.getElementById('origen').value;
      const destino = document.getElementById('destino').value;
      window.location.href = `buses.html?origen=${encodeURIComponent(origen)}&destino=${encodeURIComponent(destino)}`;
    });
  }

  // 4. Interacción Acordeón (Preguntas Frecuentes)
  const faqQuestions = document.querySelectorAll('.faq-question');
  faqQuestions.forEach(question => {
    question.addEventListener('click', () => {
      const faqItem = question.parentElement;
      faqItem.classList.toggle('active');
    });
  });

});