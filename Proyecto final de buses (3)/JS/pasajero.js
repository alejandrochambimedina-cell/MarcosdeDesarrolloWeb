document.addEventListener('DOMContentLoaded', () => {
    const mapaAsientos = document.getElementById('mapa-asientos');
    const inputAsiento = document.getElementById('asiento-num');
    const form = document.getElementById('form-asientos');
    const tabla = document.getElementById('tabla-asientos');

    let asientoSeleccionado = null;

    let registros = [
        { dni: '74839201', nombre: 'Carlos Ruiz', asiento: 'A02', equipaje: 'Bodega Estándar (hasta 20kg)', maletas: 1 },
        { dni: '10928374', nombre: 'Ana Torres', asiento: 'A05', equipaje: 'Equipaje de mano (Gratis)', maletas: 1 }
    ];

    const asientosOcupados = ['A02', 'A05', 'A10'];

    // Generar mapa dinámico de 16 asientos
    function generarMapa() {
        if (!mapaAsientos) return;
        mapaAsientos.innerHTML = '';

        for (let i = 1; i <= 16; i++) {
            const num = i < 10 ? `A0${i}` : `A${i}`;
            const seat = document.createElement('div');
            seat.className = 'seat';
            seat.textContent = num;

            if (asientosOcupados.includes(num)) {
                seat.classList.add('ocupado');
            } else {
                seat.addEventListener('click', () => seleccionarAsiento(seat, num));
            }

            mapaAsientos.appendChild(seat);
        }
    }

    function seleccionarAsiento(element, numero) {
        document.querySelectorAll('.seat.seleccionado').forEach(s => s.classList.remove('seleccionado'));
        element.classList.add('seleccionado');
        asientoSeleccionado = numero;
        inputAsiento.value = numero;
    }

    function renderizarTabla() {
        if (!tabla) return;
        tabla.innerHTML = '';

        registros.forEach(r => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${r.dni}</td>
                <td>${r.nombre}</td>
                <td><strong>${r.asiento}</strong></td>
                <td>${r.equipaje}</td>
                <td>${r.maletas}</td>
                <td><span style="color: #16a34a; font-weight: bold;">Confirmado</span></td>
            `;
            tabla.appendChild(tr);
        });
    }

    if (form) {
        form.addEventListener('submit', (e) => {
            e.preventDefault();

            if (!asientoSeleccionado) {
                alert('Por favor selecciona un asiento del mapa.');
                return;
            }

            const nuevoRegistro = {
                dni: document.getElementById('dni').value,
                nombre: document.getElementById('nombre').value,
                asiento: asientoSeleccionado,
                equipaje: document.getElementById('tipo-equipaje').value,
                maletas: document.getElementById('maletas').value
            };

            registros.push(nuevoRegistro);
            asientosOcupados.push(asientoSeleccionado);

            renderizarTabla();
            generarMapa();
            form.reset();
            inputAsiento.value = '';
            asientoSeleccionado = null;

            alert('¡Asiento y equipaje registrados con éxito!');
        });
    }

    generarMapa();
    renderizarTabla();
});