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

botonesSeleccionar.forEach(boton => {
    boton.addEventListener("click", function (evento) {
        evento.preventDefault();
        const modal =
            this.closest(".modal");
        const titulo =
            modal.querySelector("h2");
        if (!titulo) {
            return;
        }
        const nombreServicio =
            titulo.textContent
                .replace("Servicio ", "")
                .trim();
        const servicio =
            buscarServicio(nombreServicio);
        if (!servicio) {
            return;
        }

        /* CONFIRMACIÓN */

        const confirmar = confirm(
            "¿Deseas seleccionar el servicio " +
            servicio.nombre +
            " por S/. " +
            servicio.precio +
            "?"
        );

        if (confirmar) {
            localStorage.setItem(
                "servicioSeleccionado",
                JSON.stringify(servicio)
            );
            alert(
                "Has seleccionado el servicio " +
                servicio.nombre +
                "."
            );
            window.location.href =
                "../HTML/reserva.html";
        }
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