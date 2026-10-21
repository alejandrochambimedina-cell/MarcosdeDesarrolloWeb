/* ===== BUSES - JAVASCRIPT =====
   Paso 2 del flujo: Servicios -> BUSES -> Reserva -> Boleto.
   Lee el servicio y la ruta elegidos antes, deja escoger fecha y bus,
   y guarda la elección en "reservaBus" para la página de Reserva. */

// 1. Sin servicio elegido no se puede estar aquí: se vuelve al paso anterior.
const servicioElegido = JSON.parse(
    localStorage.getItem("servicioSeleccionado") || "null"
);

if (!servicioElegido) {
    window.location.href = "/servicios";
}

// 2. Ruta elegida en Inicio (por defecto Lima → Cusco).
const rutaGuardada = JSON.parse(
    localStorage.getItem("rutaSeleccionada") || "null"
) || { origen: "Lima", destino: "Cusco" };

const rutaTexto = rutaGuardada.origen + " → " + rutaGuardada.destino;

// 3. Pintar ruta, servicio y precio en la página.
if (servicioElegido) {

    document.getElementById("rutaTitulo").textContent = rutaTexto;

    document.getElementById("servicioElegido").textContent =
        servicioElegido.nombre + " (S/. " + servicioElegido.precio + " por asiento)";

    document.querySelectorAll("[data-precio]").forEach(function (precio) {
        precio.textContent = "S/. " + servicioElegido.precio;
    });
}

// 4. Fecha de viaje: desde hoy en adelante (por defecto, mañana).
const campoFecha = document.getElementById("fechaViaje");
const mensajeFecha = document.getElementById("mensajeFecha");

function aTextoISO(fecha) {
    const anio = fecha.getFullYear();
    const mes = String(fecha.getMonth() + 1).padStart(2, "0");
    const dia = String(fecha.getDate()).padStart(2, "0");
    return anio + "-" + mes + "-" + dia;
}

const hoy = new Date();
const manana = new Date();
manana.setDate(hoy.getDate() + 1);

campoFecha.min = aTextoISO(hoy);

// Si el usuario ya había elegido fecha antes, se respeta.
const reservaPrevia = JSON.parse(localStorage.getItem("reservaBus") || "null");
campoFecha.value = (reservaPrevia && reservaPrevia.fechaISO) || aTextoISO(manana);

// 5. Al elegir un bus: se guarda todo y se pasa a Reserva.
document.querySelectorAll(".btn-seleccionar").forEach(function (boton) {
    boton.addEventListener("click", function () {

        if (!campoFecha.value || campoFecha.value < campoFecha.min) {
            mensajeFecha.hidden = false;
            campoFecha.focus();
            return;
        }

        mensajeFecha.hidden = true;

        // "2026-10-27" -> "27/10/2026"
        const partes = campoFecha.value.split("-");
        const fechaTexto = partes[2] + "/" + partes[1] + "/" + partes[0];

        localStorage.setItem("reservaBus", JSON.stringify({
            ruta: rutaTexto,
            bus: boton.dataset.bus,
            salida: boton.dataset.salida,
            llegada: boton.dataset.llegada,
            duracion: boton.dataset.duracion,
            fecha: fechaTexto,
            fechaISO: campoFecha.value
        }));

        // Cambió el bus: se descartan asientos y boleto de una reserva anterior.
        localStorage.removeItem("asientosSeleccionados");
        localStorage.removeItem("equipajeSeleccionado");
        localStorage.removeItem("boletoEmitido");

        window.location.href = "/reserva";
    });
});
