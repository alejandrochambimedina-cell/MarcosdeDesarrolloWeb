package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cursos")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false, length = 120)
    private String titulo;

    @Column(name = "horas", nullable = false)
    private int horas;

    protected Course() {
    }

    public static Course crear(String titulo, int horas) {
        Course curso = new Course();
        curso.titulo = titulo;
        curso.horas = horas;
        return curso;
    }

    public void actualizar(String titulo, int horas) {
        this.titulo = titulo;
        this.horas = horas;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getHoras() {
        return horas;
    }
}
