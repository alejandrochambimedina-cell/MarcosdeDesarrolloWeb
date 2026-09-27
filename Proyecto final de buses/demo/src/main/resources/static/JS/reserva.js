/* ===== RESERVA DE ASIENTO Y EQUIPAJE - JAVASCRIPT ===== */

// Tamaño del bus: 10 filas x 4 asientos = 40 asientos.
const FILAS = 10;
const ASIENTOS_POR_FILA = 4;

// Asientos ya ocupados (datos de ejemplo).
const asientosOcupados = [3, 7, 11, 14, 18, 22, 26, 29, 33, 37];

// Viaje y servicio por defecto, por si el usuario entra sin pasar por buses/servicios.
const viajeDefecto = {
    ruta: "Lima → Cusco",
    bus: "Bus Andino",
    salida: "20:00",
    llegada: "06:00"
};

const servicioDefecto = {
    nombre: "Estándar",
    precio: 85
};

// Recuperamos lo elegido en pasos anteriores (buses.html / servicios.html).
const reservaBusGuardada = JSON.parse(
    localStorage.getItem("reservaBus") || "null"
);

const servicioSeleccionado = JSON.parse(
    localStorage.getItem("servicioSeleccionado") || "null"
) || servicioDefecto;

const viaje = {
    ruta: (reservaBusGuardada && reservaBusGuardada.ruta) || viajeDefecto.ruta,
    bus: (reservaBusGuardada && reservaBusGuardada.bus) || viajeDefecto.bus,
    salida: (reservaBusGuardada && reservaBusGuardada.salida) || viajeDefecto.salida,
    llegada: (reservaBusGuardada && reservaBusGuardada.llegada) || viajeDefecto.llegada
};

const precioAsiento = servicioSeleccionado.precio;

// Pintamos el resumen del viaje en la parte de arriba.
document.getElementById("resumenRuta").textContent = viaje.ruta;
document.getElementById("resumenBus").textContent = viaje.bus;
document.getElementById("resumenHorario").textContent =
    viaje.salida + " - " + viaje.llegada;
document.getElementById("resumenServicio").textContent =
    servicioSeleccionado.nombre + " (S/. " + precioAsiento + ")";

// Un objeto por asiento: número + estado (disponible/ocupado/seleccionado).
const asientos = [];

for (let numero = 1; numero <= FILAS * ASIENTOS_POR_FILA; numero++) {
    asientos.push({
        numero: numero,
        estado: asientosOcupados.includes(numero) ? "ocupado" : "disponible"
    });
}

// Elementos del DOM que usamos varias veces.
const mapaAsientos = document.getElementById("mapaAsientos");
const listaAsientosElegidos = document.getElementById("listaAsientosElegidos");
const alertaSinAsientos = document.getElementById("alertaSinAsientos");
const resumenPrecioAsientos = document.getElementById("resumenPrecioAsientos");
const resumenPrecioEquipaje = document.getElementById("resumenPrecioEquipaje");
const resumenPrecioTotal = document.getElementById("resumenPrecioTotal");
const botonContinuar = document.getElementById("botonContinuar");
const formReserva = document.getElementById("formReserva");
const mensajeGuardado = document.getElementById("mensajeGuardado");

// Precio de equipaje adicional según el radio button marcado.
const PRECIOS_EQUIPAJE = {
    "0": 0,
    "1": 15,
    "2": 30
};

// Convierte un asiento en su <li><button> de HTML.
function pintarAsiento(asiento) {

    const numeroTexto = String(asiento.numero).padStart(2, "0");

    return (
        "<li>" +
            "<button type=\"button\" " +
            "class=\"asiento\" " +
            "data-numero=\"" + asiento.numero + "\" " +
            "data-estado=\"" + asiento.estado + "\" " +
            (asiento.estado === "ocupado" ? "disabled " : "") +
            "aria-label=\"Asiento " + numeroTexto + ", " + asiento.estado + "\">" +
                numeroTexto +
            "</button>" +
        "</li>"
    );
}

// Dibuja el mapa completo, fila por fila (2 asientos, pasillo, 2 asientos).
function pintarMapa() {

    let html = "";

    for (let fila = 0; fila < FILAS; fila++) {

        const inicioFila = fila * ASIENTOS_POR_FILA;

        const asientosFila = asientos.slice(
            inicioFila,
            inicioFila + ASIENTOS_POR_FILA
        );

        const izquierda = asientosFila.slice(0, 2).map(pintarAsiento).join("");
        const pasillo = "<li class=\"aisle\" aria-hidden=\"true\"></li>";
        const derecha = asientosFila.slice(2, 4).map(pintarAsiento).join("");

        html += izquierda + pasillo + derecha;
    }

    mapaAsientos.innerHTML = html;
}

pintarMapa();

// Clic en un asiento: alterna entre disponible y seleccionado.
mapaAsientos.addEventListener("click", function (evento) {

    const boton = evento.target.closest(".asiento");

    if (!boton || boton.disabled) {
        return;
    }

    const numero = Number(boton.dataset.numero);

    const asiento = asientos.find(
        item => item.numero === numero
    );

    if (!asiento) {
        return;
    }

    asiento.estado =
        asiento.estado === "seleccionado" ? "disponible" : "seleccionado";

    boton.dataset.estado = asiento.estado;

    actualizarResumen();

});

// Recalcular cuando cambia el equipaje adicional.
const opcionesEquipaje = document.querySelectorAll(
    "input[name='equipajeAdicional']"
);

opcionesEquipaje.forEach(opcion => {
    opcion.addEventListener("change", actualizarResumen);
});

// Precio del equipaje adicional elegido.
function calcularEquipaje() {

    const opcionElegida = document.querySelector(
        "input[name='equipajeAdicional']:checked"
    );

    const valor = opcionElegida ? opcionElegida.value : "0";

    return PRECIOS_EQUIPAJE[valor] || 0;
}

// filter(): solo los asientos en estado "seleccionado".
function obtenerAsientosSeleccionados() {

    return asientos.filter(
        asiento => asiento.estado === "seleccionado"
    );
}

// reduce(): suma el precio de todos los asientos seleccionados.
function calcularPrecioAsientos(seleccionados) {

    return seleccionados.reduce(
        (total, asiento) => total + precioAsiento,
        0
    );
}

// Actualiza chips, alerta, precios y el botón Continuar.
function actualizarResumen() {

    const seleccionados = obtenerAsientosSeleccionados();

    listaAsientosElegidos.innerHTML = seleccionados
        .map(asiento => "<li>Asiento " + String(asiento.numero).padStart(2, "0") + "</li>")
        .join("");

    alertaSinAsientos.classList.toggle("d-none", seleccionados.length > 0);

    const precioEquipaje = calcularEquipaje();
    const precioAsientos = calcularPrecioAsientos(seleccionados);
    const precioTotal = precioAsientos + precioEquipaje;

    resumenPrecioAsientos.textContent = "S/. " + precioAsientos;
    resumenPrecioEquipaje.textContent = "S/. " + precioEquipaje;
    resumenPrecioTotal.textContent = "S/. " + precioTotal;

    botonContinuar.disabled = seleccionados.length === 0;
}

// Pintar el resumen vacío al cargar la página.
actualizarResumen();

// Fecha con formato dd/mm/aaaa (igual que boleto.js).
function formatearFecha(fecha) {
    const dia = String(fecha.getDate()).padStart(2, "0");
    const mes = String(fecha.getMonth() + 1).padStart(2, "0");
    const anio = fecha.getFullYear();
    return dia + "/" + mes + "/" + anio;
}

// Al enviar el formulario: guardar todo y pasar a pasajero.html.
formReserva.addEventListener("submit", function (evento) {

    evento.preventDefault();

    const seleccionados = obtenerAsientosSeleccionados();

    if (seleccionados.length === 0) {
        alertaSinAsientos.classList.remove("d-none");
        return;
    }

    const numerosSeleccionados = seleccionados.map(
        asiento => String(asiento.numero).padStart(2, "0")
    );

    const opcionEquipaje = document.querySelector(
        "input[name='equipajeAdicional']:checked"
    ).value;

    // Misma clave que usa boleto.js, para mantener los datos del viaje.
    localStorage.setItem(
        "reservaBus",
        JSON.stringify({
            ruta: viaje.ruta,
            bus: viaje.bus,
            salida: viaje.salida,
            llegada: viaje.llegada,
            fecha: (reservaBusGuardada && reservaBusGuardada.fecha) || formatearFecha(new Date())
        })
    );

    // Así boleto.js usa el asiento real en vez de uno al azar.
    localStorage.setItem("asientoAsignado", numerosSeleccionados[0]);

    localStorage.setItem(
        "asientosSeleccionados",
        JSON.stringify(numerosSeleccionados)
    );

    localStorage.setItem(
        "equipajeSeleccionado",
        JSON.stringify({
            maletas: Number(opcionEquipaje),
            precio: PRECIOS_EQUIPAJE[opcionEquipaje]
        })
    );

    mensajeGuardado.classList.remove("d-none");
    botonContinuar.disabled = true;

    // Pequeña espera para que se alcance a leer el mensaje.
    setTimeout(function () {
        window.location.href = "pasajero.html";
    }, 900);

});