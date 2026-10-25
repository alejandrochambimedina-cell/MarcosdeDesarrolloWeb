
package com.example.demo;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/reservas")
public class ReservaApiController {

        private final ReservaService reservaService;
        private final UsuarioRepository usuarioRepository;

        public ReservaApiController(
                        ReservaService reservaService,
                        UsuarioRepository usuarioRepository) {
                this.reservaService = reservaService;
                this.usuarioRepository = usuarioRepository;
        }

        // Consultar las reservas del usuario que inició sesión
        @GetMapping
        public ResponseEntity<?> listarReservas(HttpSession session) {

                Usuario usuarioSesion = (Usuario) session.getAttribute(
                                AuthController.SESION_USUARIO);

                if (usuarioSesion == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .body("Debes iniciar sesión para consultar tus reservas.");
                }

                List<Reserva> reservas = reservaService
                                .listarPorUsuario(usuarioSesion.getId());

                List<ReservaDetalleRespuesta> resultado = reservas.stream()
                                .map(r -> new ReservaDetalleRespuesta(
                                                r.getId(),
                                                r.getCodigo(),
                                                r.getRuta(),
                                                r.getFechaViaje(),
                                                r.getBus(),
                                                r.getHoraSalida(),
                                                r.getHoraLlegada(),
                                                r.getServicio(),
                                                r.getAsientos(),
                                                r.getMaletas(),
                                                r.getTotal(),
                                                r.getNombresPasajero(),
                                                r.getApellidosPasajero(),
                                                r.getDniPasajero(),
                                                r.getTelefonoPasajero(),
                                                r.getCorreoPasajero()))
                                .toList();

                return ResponseEntity.ok(resultado);
        }

        // Guardar una nueva reserva
        @PostMapping
        public ResponseEntity<?> guardar(
                        @Valid @RequestBody ReservaSolicitud solicitud,
                        HttpSession session) {

                Usuario usuarioSesion = (Usuario) session.getAttribute(
                                AuthController.SESION_USUARIO);

                if (usuarioSesion == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .body("Debes iniciar sesión para guardar una reserva.");
                }

                if (solicitud == null
                                || solicitud.ruta == null
                                || solicitud.bus == null
                                || solicitud.fecha == null
                                || solicitud.salida == null
                                || solicitud.llegada == null
                                || solicitud.servicio == null
                                || solicitud.asientos == null
                                || solicitud.asientos.isEmpty()
                                || solicitud.pasajero == null) {

                        return ResponseEntity.badRequest()
                                        .body("Faltan datos obligatorios de la reserva.");
                }

                if (solicitud.pasajero.nombres == null
                                || solicitud.pasajero.nombres.isBlank()
                                || solicitud.pasajero.apellidos == null
                                || solicitud.pasajero.apellidos.isBlank()
                                || solicitud.pasajero.dni == null
                                || !solicitud.pasajero.dni.matches("\\d{8}")
                                || solicitud.pasajero.telefono == null
                                || !solicitud.pasajero.telefono.matches("\\d{9}")
                                || solicitud.pasajero.correo == null
                                || solicitud.pasajero.correo.isBlank()
                                || solicitud.total == null
                                || solicitud.total.signum() < 0
                                || solicitud.maletas < 0
                                || solicitud.maletas > 2) {

                        return ResponseEntity.badRequest()
                                        .body("Los datos del pasajero o de la reserva no son válidos.");
                }

                Usuario usuario = usuarioRepository.findById(usuarioSesion.getId())
                                .orElse(null);

                if (usuario == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .body("No se encontró la cuenta de usuario.");
                }

                Reserva reserva = new Reserva();
                reserva.setUsuario(usuario);
                reserva.setRuta(solicitud.ruta);
                reserva.setBus(solicitud.bus);
                reserva.setFechaViaje(solicitud.fecha);
                reserva.setHoraSalida(solicitud.salida);
                reserva.setHoraLlegada(solicitud.llegada);
                reserva.setServicio(solicitud.servicio);
                reserva.setAsientos(String.join(", ", solicitud.asientos));
                reserva.setMaletas(solicitud.maletas);
                reserva.setTotal(solicitud.total);
                reserva.setNombresPasajero(solicitud.pasajero.nombres.trim());
                reserva.setApellidosPasajero(solicitud.pasajero.apellidos.trim());
                reserva.setDniPasajero(solicitud.pasajero.dni);
                reserva.setTelefonoPasajero(solicitud.pasajero.telefono);
                reserva.setCorreoPasajero(solicitud.pasajero.correo.trim());
                reserva.setCodigo("RP-" + UUID.randomUUID()
                                .toString().substring(0, 12).toUpperCase());

                Reserva guardada = reservaService.guardar(reserva);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(new ReservaRespuesta(
                                                guardada.getCodigo(),
                                                guardada.getId()));
        }

        // Datos recibidos para registrar una reserva
        public static class ReservaSolicitud {

                @NotBlank
                @Size(max = 150)
                public String ruta;

                @NotBlank
                @Size(max = 20)
                public String fecha;

                @NotBlank
                @Size(max = 150)
                public String bus;

                @NotBlank
                @Size(max = 20)
                public String salida;

                @NotBlank
                @Size(max = 20)
                public String llegada;

                @NotBlank
                @Size(max = 100)
                public String servicio;

                @NotNull
                @Size(min = 1, max = 10)
                public List<@NotBlank String> asientos;

                @Min(0)
                @Max(2)
                public int maletas;

                @NotNull
                @DecimalMin("0.00")
                public BigDecimal total;

                @NotNull
                @Valid
                public PasajeroSolicitud pasajero;
        }

        // Datos del pasajero
        public static class PasajeroSolicitud {

                @NotBlank
                @Size(max = 150)
                public String nombres;

                @NotBlank
                @Size(max = 150)
                public String apellidos;

                @NotBlank
                @Pattern(regexp = "\\d{8}")
                public String dni;

                @NotBlank
                @Pattern(regexp = "\\d{9}")
                public String telefono;

                @NotBlank
                @Email
                @Size(max = 150)
                public String correo;
        }

        // Respuesta después de guardar
        public record ReservaRespuesta(
                        String codigo,
                        Long id) {
        }

        // Datos que se devuelven al consultar las reservas
        public record ReservaDetalleRespuesta(
                        Long id,
                        String codigo,
                        String ruta,
                        String fechaViaje,
                        String bus,
                        String horaSalida,
                        String horaLlegada,
                        String servicio,
                        String asientos,
                        int maletas,
                        BigDecimal total,
                        String nombresPasajero,
                        String apellidosPasajero,
                        String dniPasajero,
                        String telefonoPasajero,
                        String correoPasajero) {
        }
}
