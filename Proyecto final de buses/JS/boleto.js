/* ===== CONFIRMACIÓN Y BOLETO - JAVASCRIPT ===== */

/* ========== DATOS POR DEFECTO DEL VIAJE ================ */
/* Se usan si todavía no existe una página de reserva/buses
   que guarde estos datos en localStorage (reservaBus). */

const viajeDefecto = {
    ruta: "Lima → Cusco",
    bus: "Bus Andino",
    salida: "20:00",
    llegada: "06:00",
    equipajePrecio: 15
};

/* ========== RECUPERAR DATOS GUARDADOS ================ */

const datosPasajero = JSON.parse(
    localStorage.getItem("datosPasajero") || "null"
);

const servicioSeleccionado = JSON.parse(
    localStorage.getItem("servicioSeleccionado") || "null"
);

const reservaBus = JSON.parse(
    localStorage.getItem("reservaBus") || "null"
);

/* ========== FECHA DEL VIAJE ================ */

function formatearFecha(fecha) {
    const dia = String(fecha.getDate()).padStart(2, "0");
    const mes = String(fecha.getMonth() + 1).padStart(2, "0");
    const anio = fecha.getFullYear();
    return dia + "/" + mes + "/" + anio;
}

const fechaViaje = reservaBus && reservaBus.fecha
    ? reservaBus.fecha
    : formatearFecha(new Date());

/* ========== ASIENTO ASIGNADO ================ */
/* Se genera una sola vez y se guarda, para que no cambie
   si el pasajero recarga la página del boleto. */

let asiento = localStorage.getItem("asientoAsignado");

if (!asiento) {
    asiento = String(Math.floor(Math.random() * 40) + 1).padStart(2, "0");
    localStorage.setItem("asientoAsignado", asiento);
}

/* ========== DATOS FINALES DEL VIAJE ================ */

const ruta = (reservaBus && reservaBus.ruta) || viajeDefecto.ruta;
const bus = (reservaBus && reservaBus.bus) || viajeDefecto.bus;
const salida = (reservaBus && reservaBus.salida) || viajeDefecto.salida;
const llegada = (reservaBus && reservaBus.llegada) || viajeDefecto.llegada;

const nombreServicio = servicioSeleccionado
    ? servicioSeleccionado.nombre
    : "Estándar";

const precioPasaje = servicioSeleccionado
    ? servicioSeleccionado.precio
    : 85;

const precioEquipaje = viajeDefecto.equipajePrecio;
const precioTotal = precioPasaje + precioEquipaje;

const nombrePasajero = datosPasajero
    ? (datosPasajero.nombres + " " + datosPasajero.apellidos).trim()
    : "Pasajero sin registrar";

/* ========== CÓDIGO DE RESERVA ================ */
/* Se genera una sola vez a partir de las iniciales del bus
   y la fecha del viaje, y se guarda para mantenerlo estable. */

function generarCodigoReserva() {
    const iniciales = bus
        .split(" ")
        .map(palabra => palabra.charAt(0).toUpperCase())
        .join("");

    const partesFecha = fechaViaje.split("/");
    const dia = partesFecha[0];
    const mes = partesFecha[1];
    const anio = partesFecha[2].slice(-2);

    return iniciales + dia + mes + anio;
}

let codigoReserva = localStorage.getItem("codigoReserva");

if (!codigoReserva) {
    codigoReserva = generarCodigoReserva();
    localStorage.setItem("codigoReserva", codigoReserva);
}

/* ========== PINTAR RESUMEN DE RESERVA ================ */

document.getElementById("resumenRuta").textContent = ruta;
document.getElementById("resumenFecha").textContent = fechaViaje;
document.getElementById("resumenBus").textContent = bus;
document.getElementById("resumenPasajero").textContent = nombrePasajero;
document.getElementById("resumenSalida").textContent = salida;
document.getElementById("resumenLlegada").textContent = llegada;
document.getElementById("resumenServicio").textContent = nombreServicio;
document.getElementById("resumenAsiento").textContent = asiento;
document.getElementById("resumenEquipaje").textContent = "1 maleta";

/* ========== PINTAR PRECIO ================ */

document.getElementById("precioPasaje").textContent = "S/. " + precioPasaje;
document.getElementById("precioEquipaje").textContent = "S/. " + precioEquipaje;
document.getElementById("precioTotal").textContent = "S/. " + precioTotal;

/* ========== PINTAR BOLETO ================ */

document.getElementById("ticketBus").textContent = bus.toUpperCase();
document.getElementById("ticketRuta").textContent = ruta;
document.getElementById("ticketFecha").textContent = fechaViaje;
document.getElementById("ticketSalida").textContent = salida;
document.getElementById("ticketLlegada").textContent = llegada;
document.getElementById("ticketPasajero").textContent = nombrePasajero;
document.getElementById("ticketAsiento").textContent = asiento;
document.getElementById("ticketServicio").textContent = nombreServicio;
document.getElementById("ticketCodigo").textContent = codigoReserva;

/* ========== BOTÓN IMPRIMIR ================ */

document.getElementById("botonImprimir")
    .addEventListener("click", function () {
        window.print();
    });

/* ========== BOTÓN NUEVA RESERVA ================ */
/* Limpia los datos de la reserva actual para empezar una nueva,
   sin afectar el resto del sitio. */

document.getElementById("botonNuevaReserva")
    .addEventListener("click", function () {
        localStorage.removeItem("datosPasajero");
        localStorage.removeItem("servicioSeleccionado");
        localStorage.removeItem("reservaBus");
        localStorage.removeItem("asientoAsignado");
        localStorage.removeItem("codigoReserva");
    });