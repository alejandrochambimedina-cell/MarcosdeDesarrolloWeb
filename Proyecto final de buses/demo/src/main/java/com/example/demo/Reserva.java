
package com.example.demo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "reservas")
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String ruta;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String bus;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String fechaViaje;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String horaSalida;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String horaLlegada;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String servicio;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String asientos;

    @Min(0)
    @Max(2)
    @Column(nullable = false)
    private int maletas;

    @NotNull
    @DecimalMin("0.00")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String nombresPasajero;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String apellidosPasajero;

    @NotBlank
    @Pattern(regexp = "\\d{8}")
    @Column(nullable = false, length = 8)
    private String dniPasajero;

    @NotBlank
    @Pattern(regexp = "\\d{9}")
    @Column(nullable = false, length = 20)
    private String telefonoPasajero;

    @NotBlank
    @Email
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String correoPasajero;

    @NotBlank
    @Size(max = 40)
    @Column(nullable = false, unique = true, length = 40)
    private String codigo;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    public Reserva() {
    }

    @PrePersist
    public void antesDeGuardar() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public String getFechaViaje() {
        return fechaViaje;
    }

    public void setFechaViaje(String fechaViaje) {
        this.fechaViaje = fechaViaje;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(String horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getAsientos() {
        return asientos;
    }

    public void setAsientos(String asientos) {
        this.asientos = asientos;
    }

    public int getMaletas() {
        return maletas;
    }

    public void setMaletas(int maletas) {
        this.maletas = maletas;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getNombresPasajero() {
        return nombresPasajero;
    }

    public void setNombresPasajero(String nombresPasajero) {
        this.nombresPasajero = nombresPasajero;
    }

    public String getApellidosPasajero() {
        return apellidosPasajero;
    }

    public void setApellidosPasajero(String apellidosPasajero) {
        this.apellidosPasajero = apellidosPasajero;
    }

    public String getDniPasajero() {
        return dniPasajero;
    }

    public void setDniPasajero(String dniPasajero) {
        this.dniPasajero = dniPasajero;
    }

    public String getTelefonoPasajero() {
        return telefonoPasajero;
    }

    public void setTelefonoPasajero(String telefonoPasajero) {
        this.telefonoPasajero = telefonoPasajero;
    }

    public String getCorreoPasajero() {
        return correoPasajero;
    }

    public void setCorreoPasajero(String correoPasajero) {
        this.correoPasajero = correoPasajero;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}
