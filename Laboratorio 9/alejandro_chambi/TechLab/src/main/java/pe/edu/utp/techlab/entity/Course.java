package pe.edu.utp.techlab.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cursos")
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "titulo", nullable = false, length = 120)
    private String titulo;
    @Column(name = "horas", nullable = false)
    private int horas;
    protected Course() {}
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public int getHoras() { return horas; }
}
