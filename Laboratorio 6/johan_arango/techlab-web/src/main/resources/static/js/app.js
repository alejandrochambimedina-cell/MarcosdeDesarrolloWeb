"use strict";

// 1. Asignar año dinámico al footer de manera segura (RF-10)
const anioSpan = document.querySelector("#anioActual");
if (anioSpan) {
    anioSpan.textContent = new Date().getFullYear();
}

// 2. Controladores del Ciclo de Vida del Modal Reutilizable (RF-06, RF-09)
const modalElemento = document.getElementById('modalRegistro');
if (modalElemento) {
    
    // Transferir dinámicamente el valor de 'data-curso' al input readonly
    modalElemento.addEventListener('show.bs.modal', (event) => {
        const botonIniciador = event.relatedTarget; 
        if (botonIniciador) {
            const nombreCurso = botonIniciador.getAttribute('data-curso');
            const inputCurso = modalElemento.querySelector('#curso');
            if (inputCurso) {
                inputCurso.value = nombreCurso;
            }
        }
    });

    // Resetear el formulario completamente al cerrar el modal (Limpieza)
    modalElemento.addEventListener('hidden.bs.modal', () => {
        const formulario = document.querySelector("#formRegistro");
        const contenedorMensaje = document.querySelector("#estadoFormulario");
        
        if (formulario) {
            formulario.reset();
            formulario.classList.remove("was-validated");
        }
        if (contenedorMensaje) {
            contenedorMensaje.classList.add("d-none");
            contenedorMensaje.textContent = "";
        }
    });
}

// 3. Algoritmo de envío y validación de formulario controlado (RF-07, RF-08)
const formInscripcion = document.querySelector("#formRegistro");
const mensajeEstado = document.querySelector("#estadoFormulario");

if (formInscripcion && mensajeEstado) {
    formInscripcion.addEventListener("submit", (event) => {
        event.preventDefault(); // Detener el envío convencional del navegador
        
        // Agregar la pseudo-clase visual de Bootstrap
        formInscripcion.classList.add("was-validated");

        // Evaluar las restricciones HTML5 nativas de los controles
        if (!formInscripcion.checkValidity()) {
            // Enfocar automáticamente el primer control inválido encontrado (Puerta 4)
            formInscripcion.querySelector(":invalid")?.focus();
            return;
        }

        // Si todos los campos son válidos, inyectar el mensaje legible para lectores de pantalla
        mensajeEstado.textContent = "¡Registro de demostración completado con éxito!";
        mensajeEstado.classList.remove("d-none");
    });
}
