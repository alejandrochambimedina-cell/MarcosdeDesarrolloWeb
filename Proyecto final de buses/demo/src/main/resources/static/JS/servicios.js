/* ======== SERVICIOS - JAVASCRIPT ================== */

/* ========== INFORMACIÓN DE LOS SERVICIOS ================ */

const servicios = [

    {
        nombre: "Estándar",
        precio: 85,
        clase: "estandar"
    },

    {
        nombre: "VIP",
        precio: 110,
        clase: "vip"
    },

    {
        nombre: "Premium",
        precio: 145,
        clase: "premium"
    }

];

/* =========== ELEMENTOS DEL DOM =============== */

const cards = document.querySelectorAll(".servicio-card");
const botonesSeleccionar = document.querySelectorAll(".btn-servicio");

/* ======= MOSTRAR SERVICIOS EN CONSOLA ================ */

console.log("Servicios disponibles:");
servicios.forEach(servicio => {
    console.log(
        servicio.nombre + " - S/. " + servicio.precio
    );
});

/* ========= OBTENER SERVICIO ================= */

function buscarServicio(nombre) {
    return servicios.find(
        servicio => servicio.nombre === nombre
    );
}

/* ========= MOSTRAR INFORMACIÓN DEL SERVICIO =============== */

function mostrarServicio(nombre) {
    const servicio = buscarServicio(nombre);
    if (!servicio) {
        return;
    }
    console.log("Servicio seleccionado:");
    console.log(
        servicio.nombre +
        " - S/. " +
        servicio.precio
    );
}

/* ========= EVENTO EN LAS CARDS =============== */

cards.forEach(card => {
    card.addEventListener("click", function () {
        const claseServicio =
            this.classList[1];
        const servicio =
            servicios.find(
                item => item.clase === claseServicio
            );
        if (servicio) {
            mostrarServicio(servicio.nombre);
        }
    });
});

/* =========== SELECCIONAR SERVICIO =============== */
/* Flujo: Servicios -> Buses -> Reserva -> Boleto.
   Si no hay sesión iniciada, primero se pide iniciar sesión. */

botonesSeleccionar.forEach(boton => {
    boton.addEventListener("click", function (evento) {
        evento.preventDefault();

        // Cada modal tiene una clase que identifica el servicio (estandar-modal, vip-modal, premium-modal)
        const contenidoModal = this.closest(".modal-content");
        if (!contenidoModal) {
            return;
        }

        const servicio = servicios.find(
            item => contenidoModal.classList.contains(item.clase + "-modal")
        );
        if (!servicio) {
            return;
        }

        // Sin sesión: se envía a iniciar sesión (el servidor muestra el aviso).
        if (document.body.dataset.autenticado !== "true") {
            window.location.href = "/login?requerido";
            return;
        }

        localStorage.setItem(
            "servicioSeleccionado",
            JSON.stringify(servicio)
        );

        // Cambió el servicio: se descartan los pasos posteriores de una reserva anterior.
        localStorage.removeItem("asientosSeleccionados");
        localStorage.removeItem("equipajeSeleccionado");
        localStorage.removeItem("boletoEmitido");

        window.location.href = "/buses";
    });
});

/* ============ RECUPERAR SERVICIO GUARDADO ================== */

const servicioGuardado = localStorage.getItem("servicioSeleccionado");
if (servicioGuardado) {
    const servicio =
        JSON.parse(servicioGuardado);
    console.log(
        "Último servicio seleccionado:",
        servicio.nombre
    );
}

/* ======== MAP ============= */

const nombresServicios =
    servicios.map(
        servicio => servicio.nombre
    );
console.log(
    "Nombres de servicios:",
    nombresServicios
);

/* ========== FILTER ================== */

const serviciosPremium =
    servicios.filter(
        servicio => servicio.precio >= 110
    );
console.log(
    "Servicios superiores:",
    serviciosPremium
);

/* =========== TOTAL DE PRECIOS ================= */

const precios =
    servicios.map(
        servicio => servicio.precio
    );
const precioTotal =
    precios.reduce(
        (total, precio) => total + precio,
        0
    );
console.log(
    "Suma de precios:",
    precioTotal
);

/* ========== MENSAJE DE CARGA =================== */

console.log(
    "Página de servicios cargada correctamente."
);

// ========================================
// ACORDEÓN DE INFORMACIÓN
// ========================================

const botonesAcordeon = document.querySelectorAll(".acordeon-boton");

botonesAcordeon.forEach(boton => {

    boton.addEventListener("click", () => {

        const item = boton.parentElement;

        // Cerrar los demás
        document.querySelectorAll(".acordeon-item").forEach(otroItem => {

            if (otroItem !== item) {
                otroItem.classList.remove("activo");
            }

        });

        // Abrir o cerrar el seleccionado
        item.classList.toggle("activo");

    });

});