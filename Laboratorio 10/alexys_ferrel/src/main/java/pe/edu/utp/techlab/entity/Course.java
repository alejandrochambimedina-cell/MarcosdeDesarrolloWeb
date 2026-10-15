package pe.edu.utp.techlab.entity;

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

    @Column(name = "titulo", nullable = false, length = 120, unique = true)
    private String titulo;

    @Column(name = "horas", nullable = false)
    private int horas;

    protected Course() {
    }

    public Course(String titulo, int horas) {
        this.titulo = titulo;
        this.horas = horas;
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