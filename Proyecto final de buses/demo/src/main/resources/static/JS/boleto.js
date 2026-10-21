/* ===== BOLETO - JAVASCRIPT =====
   Paso final del flujo: Servicios -> Buses -> Reserva -> BOLETO.
   Esta página tiene 3 estados:
     1. Sin reserva en curso   -> invita a empezar por Servicios.
     2. Reserva sin boleto     -> pide los datos del pasajero (último paso).
     3. Boleto emitido         -> muestra resumen, precio y el boleto. */

/* ========== LEER LO GUARDADO EN LOS PASOS ANTERIORES ================ */

function leer(clave) {
    return JSON.parse(localStorage.getItem(clave) || "null");
}

const servicio = leer("servicioSeleccionado");     // { nombre, precio, clase }
const viaje = leer("reservaBus");                  // { ruta, bus, salida, llegada, fecha, ... }
const asientos = leer("asientosSeleccionados");    // ["05", "06"]
const equipaje = leer("equipajeSeleccionado");     // { maletas, precio }
const boletoEmitido = leer("boletoEmitido");       // boleto ya generado (si existe)

/* ========== CONTROL DE ESTADOS ================ */

const faseVacia = document.getElementById("faseVacia");
const faseDatos = document.getElementById("faseDatos");
const faseBoleto = document.getElementById("faseBoleto");

function mostrarFase(fase) {
    faseVacia.hidden = fase !== faseVacia;
    faseDatos.hidden = fase !== faseDatos;
    faseBoleto.hidden = fase !== faseBoleto;
}

const hayReserva = servicio && viaje && asientos && asientos.length > 0 && equipaje;

/* ========== TEXTOS Y CÁLCULOS AUXILIARES ================ */

function texto(id, valor) {
    document.getElementById(id).textContent = valor;
}

function descripcionEquipaje(maletas) {
    if (maletas === 0) {
        return "Solo equipaje de mano";
    }
    return "Equipaje de mano + " + maletas + (maletas === 1 ? " maleta" : " maletas");
}

function calcularTotales() {
    const precioAsientos = servicio.precio * asientos.length;
    return {
        precioAsientos: precioAsientos,
        precioEquipaje: equipaje.precio,
        total: precioAsientos + equipaje.precio
    };
}

function generarCodigoReserva(bus, fecha) {
    const iniciales = bus
        .split(" ")
        .map(palabra => palabra.charAt(0).toUpperCase())
        .join("");

    const partes = fecha.split("/");
    const sufijo = String(Math.floor(Math.random() * 90) + 10);

    return iniciales + partes[0] + partes[1] + partes[2].slice(-2) + sufijo;
}

/* ========== ESTADO 3: PINTAR EL BOLETO EMITIDO ================ */

function pintarBoleto(boleto) {

    const nombreCompleto = boleto.pasajero.nombres + " " + boleto.pasajero.apellidos;
    const listaAsientos = boleto.asientos.join(", ");

    // Resumen de reserva
    texto("resumenRuta", boleto.ruta);
    texto("resumenFecha", boleto.fecha);
    texto("resumenBus", boleto.bus);
    texto("resumenPasajero", nombreCompleto);
    texto("resumenSalida", boleto.salida);
    texto("resumenLlegada", boleto.llegada);
    texto("resumenServicio", boleto.servicio);
    texto("resumenAsiento", listaAsientos);
    texto("resumenEquipaje", descripcionEquipaje(boleto.maletas));

    // Precio
    texto(
        "etiquetaPasaje",
        "Pasaje (" + boleto.asientos.length + (boleto.asientos.length === 1 ? " asiento" : " asientos") +
        " × S/. " + boleto.precioAsiento + ")"
    );
    texto("precioPasaje", "S/. " + (boleto.precioAsiento * boleto.asientos.length));
    texto("precioEquipaje", "S/. " + boleto.precioEquipaje);
    texto("precioTotal", "S/. " + boleto.total);

    // Ticket
    texto("ticketBus", boleto.bus.toUpperCase());
    texto("ticketRuta", boleto.ruta);
    texto("ticketFecha", boleto.fecha);
    texto("ticketSalida", boleto.salida);
    texto("ticketLlegada", boleto.llegada);
    texto("ticketPasajero", nombreCompleto);
    texto("ticketDni", boleto.pasajero.dni);
    texto("ticketAsiento", listaAsientos);
    texto("ticketServicio", boleto.servicio);
    texto("ticketCodigo", boleto.codigo);
}

/* ========== ESTADO 2: FORMULARIO DE DATOS DEL PASAJERO ================ */

function prepararFormulario() {

    const totales = calcularTotales();

    // Resumen corto de lo reservado
    texto("datosRuta", viaje.ruta);
    texto("datosFecha", viaje.fecha);
    texto("datosBus", viaje.bus + " (" + viaje.salida + " - " + viaje.llegada + ")");
    texto("datosServicio", servicio.nombre);
    texto("datosAsientos", asientos.join(", "));
    texto("datosTotal", "S/. " + totales.total);

    const formPasajero = document.getElementById("formPasajero");

    // Solo números en DNI (8) y teléfono (9).
    document.getElementById("dni").addEventListener("input", function () {
        this.value = this.value.replace(/\D/g, "").slice(0, 8);
    });

    document.getElementById("telefono").addEventListener("input", function () {
        this.value = this.value.replace(/\D/g, "").slice(0, 9);
    });

    // Si el usuario ya había llenado sus datos en una reserva anterior, se recuperan.
    const guardados = leer("datosPasajero");

    if (guardados) {
        document.getElementById("nombres").value = guardados.nombres || document.getElementById("nombres").value;
        document.getElementById("apellidos").value = guardados.apellidos || document.getElementById("apellidos").value;
        document.getElementById("dni").value = guardados.dni || "";
        document.getElementById("fechaNacimiento").value = guardados.fechaNacimiento || "";
        document.getElementById("sexo").value = guardados.sexo || "";
        document.getElementById("telefono").value = guardados.telefono || "";
        document.getElementById("correo").value = guardados.correo || document.getElementById("correo").value;
        document.getElementById("emergencia").value = guardados.contactoEmergencia || "";
    }

    // Al confirmar: se validan los datos, se genera el boleto y se muestra.
    formPasajero.addEventListener("submit", function (evento) {

        evento.preventDefault();

        if (!formPasajero.checkValidity()) {
            formPasajero.classList.add("was-validated");
            return;
        }

        const pasajero = {
            nombres: document.getElementById("nombres").value.trim(),
            apellidos: document.getElementById("apellidos").value.trim(),
            dni: document.getElementById("dni").value.trim(),
            fechaNacimiento: document.getElementById("fechaNacimiento").value,
            sexo: document.getElementById("sexo").value,
            telefono: document.getElementById("telefono").value.trim(),
            correo: document.getElementById("correo").value.trim(),
            contactoEmergencia: document.getElementById("emergencia").value.trim()
        };

        localStorage.setItem("datosPasajero", JSON.stringify(pasajero));

        // Se guarda una "foto" del boleto: no cambia aunque se modifique la reserva después.
        const boleto = {
            codigo: generarCodigoReserva(viaje.bus, viaje.fecha),
            ruta: viaje.ruta,
            fecha: viaje.fecha,
            bus: viaje.bus,
            salida: viaje.salida,
            llegada: viaje.llegada,
            servicio: servicio.nombre,
            precioAsiento: servicio.precio,
            asientos: asientos,
            maletas: equipaje.maletas,
            precioEquipaje: totales.precioEquipaje,
            total: totales.total,
            pasajero: pasajero
        };

        localStorage.setItem("boletoEmitido", JSON.stringify(boleto));

        pintarBoleto(boleto);
        mostrarFase(faseBoleto);
        window.scrollTo({ top: 0, behavior: "smooth" });
    });
}

/* ========== DECIDIR QUÉ ESTADO MOSTRAR ================ */

if (boletoEmitido) {
    pintarBoleto(boletoEmitido);
    mostrarFase(faseBoleto);
} else if (hayReserva) {
    prepararFormulario();
    mostrarFase(faseDatos);
} else {
    mostrarFase(faseVacia);
}

/* ========== BOTONES DEL BOLETO ================ */

document.getElementById("botonImprimir")
    .addEventListener("click", function () {
        window.print();
    });

// "Nueva reserva": se borra lo de la reserva anterior (los datos del pasajero
// se conservan para no volver a escribirlos) y se empieza de nuevo en Servicios.
document.getElementById("botonNuevaReserva")
    .addEventListener("click", function () {
        [
            "rutaSeleccionada",
            "servicioSeleccionado",
            "reservaBus",
            "asientosSeleccionados",
            "equipajeSeleccionado",
            "boletoEmitido"
        ].forEach(function (clave) {
            localStorage.removeItem(clave);
        });
    });
