const formPasajero = document.getElementById("formPasajero");
const mensajeExito = document.getElementById("mensajeExito");

formPasajero.addEventListener("submit", function (evento) {
    evento.preventDefault();

    if (!formPasajero.checkValidity()) {
        evento.stopPropagation();
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
        contactoEmergencia: document.getElementById("contactoEmergencia").value.trim()
    };

    // Guarda temporalmente los datos para utilizarlos en la página del boleto.
    localStorage.setItem("datosPasajero", JSON.stringify(pasajero));

    mensajeExito.textContent =
        "Datos guardados correctamente. Puedes continuar con tu reserva.";
    mensajeExito.classList.remove("d-none");

    window.scrollTo({
        top: document.body.scrollHeight,
        behavior: "smooth"
    });
});

// Permite escribir únicamente números en DNI y teléfono.
document.getElementById("dni").addEventListener("input", function () {
    this.value = this.value.replace(/\D/g, "").slice(0, 8);
});

document.getElementById("telefono").addEventListener("input", function () {
    this.value = this.value.replace(/\D/g, "").slice(0, 9);
});

// Recupera los datos si el pasajero ya había llenado el formulario.
const datosGuardados = localStorage.getItem("datosPasajero");

if (datosGuardados) {
    const pasajero = JSON.parse(datosGuardados);

    document.getElementById("nombres").value = pasajero.nombres || "";
    document.getElementById("apellidos").value = pasajero.apellidos || "";
    document.getElementById("dni").value = pasajero.dni || "";
    document.getElementById("fechaNacimiento").value = pasajero.fechaNacimiento || "";
    document.getElementById("sexo").value = pasajero.sexo || "";
    document.getElementById("telefono").value = pasajero.telefono || "";
    document.getElementById("correo").value = pasajero.correo || "";
    document.getElementById("contactoEmergencia").value =
        pasajero.contactoEmergencia || "";
}