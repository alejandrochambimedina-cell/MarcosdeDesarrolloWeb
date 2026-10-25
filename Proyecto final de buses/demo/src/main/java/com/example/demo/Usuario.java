
package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    @Column(nullable = false, length = 32)
    private String sal;

    @Column(nullable = false, length = 64)
    private String claveHash;

    // Constructor requerido por JPA
    public Usuario() {
    }

    // Conservamos el constructor utilizado por UsuarioService
    public Usuario(String nombres, String apellidos, String correo,
                   String sal, String claveHash) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.sal = sal;
        this.claveHash = claveHash;
    }

    public Long getId() {
        return id;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public String getSal() {
        return sal;
    }

    public String getClaveHash() {
        return claveHash;
    }
}
