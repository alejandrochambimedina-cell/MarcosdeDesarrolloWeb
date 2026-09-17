"use strict";

document.addEventListener("DOMContentLoaded", () => {
  // 1. Asignar año actual al footer
  const anioEl = document.querySelector("#anioActual");
  if (anioEl) {
    anioEl.textContent = new Date().getFullYear();
  }

  // 2. Transferencia de datos al abrir el modal
  const modalRegistro = document.getElementById("modalRegistro");
  const inputCurso = document.getElementById("curso");
  const form = document.querySelector("#formRegistro");
  const estado = document.querySelector("#estadoFormulario");

  if (modalRegistro) {
    modalRegistro.addEventListener("show.bs.modal", (event) => {
      const button = event.relatedTarget;
      const curso = button?.getAttribute("data-curso") || "";
      if (inputCurso) {
        inputCurso.value = curso;
      }
    });

    // Limpiar formulario y estado al cerrar el modal
    modalRegistro.addEventListener("hidden.bs.modal", () => {
      if (form) {
        form.reset();
        form.classList.remove("was-validated");
      }
      if (estado) {
        estado.classList.add("d-none");
        estado.textContent = "";
      }
    });
  }

  // 3. Validación de Formulario
  if (form && estado) {
    form.addEventListener("submit", (event) => {
      event.preventDefault();
      form.classList.add("was-validated");

      if (!form.checkValidity()) {
        form.querySelector(":invalid")?.focus();
        return;
      }

      estado.textContent = "Registro de demostración completado con éxito.";
      estado.classList.remove("d-none");
    });
  }
});