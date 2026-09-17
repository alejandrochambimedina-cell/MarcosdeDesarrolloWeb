"use strict";

const formulario = document.querySelector("form");
const nombre = document.querySelector("#nombre");

if (formulario) {
  formulario.addEventListener("submit", (evento) => {
    evento.preventDefault();
    const valor = nombre.value.trim() || "estudiante";
    window.alert(`Hola, ${valor}. Bienvenido a TechLab.`);
  });
}
