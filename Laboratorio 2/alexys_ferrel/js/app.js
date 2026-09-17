"use strict";

document.addEventListener("DOMContentLoaded", () => {
  const yearElement = document.querySelector("[data-current-year]");
  const statusElement = document.querySelector("#estado-registro");
  const workshopButtons = document.querySelectorAll("[data-taller]");

  if (yearElement) {
    yearElement.textContent = String(new Date().getFullYear());
  }

  if (!statusElement) return;

  workshopButtons.forEach((button) => {
    button.addEventListener("click", () => {
      const workshopName = button.dataset.taller ?? "el servicio seleccionado";
      statusElement.textContent = `Has seleccionado el servicio: ${workshopName}. Registro simulado con éxito.`;
      statusElement.classList.remove("d-none");
    });
  });
});