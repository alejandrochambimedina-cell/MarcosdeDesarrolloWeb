/* =====================================
   SELECCIONAR BUS
===================================== */

function seleccionarBus(
    nombre,
    salida,
    llegada,
    duracion,
    precio
) {

    // Obtener los elementos HTML

    const resultado =
        document.getElementById("resultado");

    const mensaje =
        document.getElementById("mensaje");


    // Mostrar la sección

    resultado.style.display = "block";


    // Mostrar información del bus

    mensaje.innerHTML = `

        <p>
            <strong>Ruta:</strong>
            Ruta seleccionada
        </p>

        <p>
            <strong>Bus:</strong>
            ${nombre}
        </p>

        <p>
            <strong>Hora de salida:</strong>
            ${salida}
        </p>

        <p>
            <strong>Hora de llegada:</strong>
            ${llegada}
        </p>

        <p>
            <strong>Duración:</strong>
            ${duracion}
        </p>

        <p>
            <strong>Precio:</strong>
            ${precio}
        </p>

    `;


    // Desplazar la pantalla hasta el resultado

    resultado.scrollIntoView({
        behavior: "smooth"
    });

}
