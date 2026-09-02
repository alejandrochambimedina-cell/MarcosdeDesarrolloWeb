/* ===== MENÚ RESPONSIVE - JAVASCRIPT ===== */
/* Se usa en todas las páginas del sitio para abrir y cerrar
   el menú cuando se muestra el botón de hamburguesa (celular). */

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