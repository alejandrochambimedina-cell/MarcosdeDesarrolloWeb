package com.example.demo;

import java.io.Serializable;

/**
 * Usuario registrado en RutaPerú.
 * Por ahora vive en memoria; en el Avance 3 pasará a ser una entidad JPA (@Entity).
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nombres;
    private final String apellidos;
    private final String correo;
    private final String sal;
    private final String claveHash;

    public Usuario(String nombres, String apellidos, String correo, String sal, String claveHash) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.sal = sal;
        this.claveHash = claveHash;
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
