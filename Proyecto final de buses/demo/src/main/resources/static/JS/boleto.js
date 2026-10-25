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
    if (faseVacia) faseVacia.hidden = fase !== faseVacia;
    if (faseDatos) faseDatos.hidden = fase !== faseDatos;
    if (faseBoleto) faseBoleto.hidden = fase !== faseBoleto;
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


/* ===== RECUPERAR RESERVAS GUARDADAS EN H2 ===== */

async function cargarReservasGuardadas() {
    try {
        const respuesta = await fetch("/api/reservas", {
            method: "GET",
            credentials: "same-origin"
        });

        if (respuesta.status === 401) {
            window.location.href = "/login";
            return;
        }

        if (!respuesta.ok) {
            throw new Error("No se pudieron cargar tus reservas.");
        }

        const reservas = await respuesta.json();

        if (!Array.isArray(reservas) || reservas.length === 0) {
            mostrarFase(faseVacia);
            return;
        }

        const reserva = reservas[0];

        const boleto = {
            id: reserva.id,
            codigo: reserva.codigo,
            ruta: reserva.ruta,
            fecha: reserva.fechaViaje,
            bus: reserva.bus,
            salida: reserva.horaSalida,
            llegada: reserva.horaLlegada,
            servicio: reserva.servicio,
            asientos: String(reserva.asientos || "")
                .split(",")
                .map(asiento => asiento.trim())
                .filter(Boolean),
            maletas: reserva.maletas,
            total: Number(reserva.total),
            pasajero: {
                nombres: reserva.nombresPasajero,
                apellidos: reserva.apellidosPasajero,
                dni: reserva.dniPasajero,
                telefono: reserva.telefonoPasajero,
                correo: reserva.correoPasajero
            }
        };

        // Los datos vienen de la base de datos.
        pintarBoleto(boleto);
        mostrarFase(faseBoleto);

    } catch (error) {
        console.error("Error al recuperar reservas:", error);
        alert("No se pudieron cargar tus reservas. Inténtalo nuevamente.");
    }
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
    // Al confirmar: guardar primero en el servidor y después mostrar el boleto.
    formPasajero.addEventListener("submit", async function (evento) {
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

        const botonConfirmar = formPasajero.querySelector(
            'button[type="submit"]'
        );

        if (botonConfirmar) botonConfirmar.disabled = true;

        try {
            const respuesta = await fetch("/api/reservas", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                credentials: "same-origin",
                body: JSON.stringify({
                    ruta: viaje.ruta,
                    fecha: viaje.fecha,
                    bus: viaje.bus,
                    salida: viaje.salida,
                    llegada: viaje.llegada,
                    servicio: servicio.nombre,
                    asientos: asientos,
                    maletas: equipaje.maletas,
                    total: totales.total,
                    pasajero: pasajero
                })
            });

            if (!respuesta.ok) {
                const mensaje = await respuesta.text();
                throw new Error(mensaje || "No se pudo guardar la reserva.");
            }

            const resultado = await respuesta.json();

            const boleto = {
                codigo: resultado.codigo,
                id: resultado.id,
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

            localStorage.setItem("datosPasajero", JSON.stringify(pasajero));
            localStorage.setItem("boletoEmitido", JSON.stringify(boleto));

            pintarBoleto(boleto);
            mostrarFase(faseBoleto);
            window.scrollTo({ top: 0, behavior: "smooth" });

        } catch (error) {
            alert("No se pudo guardar la reserva: " + error.message);
        } finally {
            if (botonConfirmar) botonConfirmar.disabled = false;
        }
    });
}

/* ========== DECIDIR QUÉ ESTADO MOSTRAR ================ */

function inicializarBoleto() {
    const boletoGuardado = leer("boletoEmitido");

    if (boletoGuardado) {
        // Mostrar el boleto emitido.
        pintarBoleto(boletoGuardado);
        mostrarFase(faseBoleto);

    } else if (hayReserva) {
        // Mostrar el formulario para una nueva reserva.
        mostrarFase(faseDatos);
        prepararFormulario();

    } else {
        // Recuperar las reservas guardadas en la base de datos.
        cargarReservasGuardadas();
    }
}

inicializarBoleto();

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
