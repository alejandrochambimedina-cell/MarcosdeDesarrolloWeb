document.addEventListener("DOMContentLoaded", () => {

    const modalRegistro = document.getElementById("modalRegistro");
    const formulario = document.getElementById("formRegistro");
    const campoCurso = document.getElementById("curso");

    // Detectar qué curso seleccionó el usuario
    modalRegistro.addEventListener("show.bs.modal", (event) => {

        const boton = event.relatedTarget;

        const cursoSeleccionado = boton.getAttribute("data-curso");

        campoCurso.value = cursoSeleccionado;
    });


    // Validar formulario
    formulario.addEventListener("submit", (event) => {

        event.preventDefault();

        event.stopPropagation();

        if (!formulario.checkValidity()) {

            formulario.classList.add("was-validated");

            return;
        }


        formulario.classList.add("was-validated");

        alert("Solicitud enviada correctamente.");

        formulario.reset();

        formulario.classList.remove("was-validated");

        const modal = bootstrap.Modal.getInstance(modalRegistro);

        modal.hide();

    });

});