/* ===== MENÚ RESPONSIVE + CIERRE DE SESIÓN - JAVASCRIPT ===== */
/* Se usa en todas las páginas (lo carga el fragmento "footer"). */

(function () {

    const botonMenu = document.querySelector(".menu-icon");
    const menuPrincipal = document.querySelector(".menu");

    if (botonMenu && menuPrincipal) {

        botonMenu.addEventListener("click", function () {
            menuPrincipal.classList.toggle("activo");
        });

        // Cierra el menú al elegir una opción (útil en celular).
        menuPrincipal.querySelectorAll("a").forEach(function (enlace) {
            enlace.addEventListener("click", function () {
                menuPrincipal.classList.remove("activo");
            });
        });

        // Cierra el menú si se hace clic fuera de él.
        document.addEventListener("click", function (evento) {
            const clicDentro =
                menuPrincipal.contains(evento.target) ||
                botonMenu.contains(evento.target);

            if (!clicDentro) {
                menuPrincipal.classList.remove("activo");
            }
        });
    }

    // Al cerrar sesión se borra todo lo que quedó guardado del proceso de reserva.
    const CLAVES_RESERVA = [
        "rutaSeleccionada",
        "servicioSeleccionado",
        "reservaBus",
        "asientosSeleccionados",
        "equipajeSeleccionado",
        "boletoEmitido",
        "datosPasajero"
    ];

    document.querySelectorAll("[data-cerrar-sesion]").forEach(function (enlace) {
        enlace.addEventListener("click", function () {
            CLAVES_RESERVA.forEach(function (clave) {
                localStorage.removeItem(clave);
            });
        });
    });

})();
