package pe.edu.utp.techlab.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cursos")
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "titulo", nullable = false, length = 120, unique = true)
    private String titulo;
    @Column(name = "horas", nullable = false)
    private int horas;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_cursos_categoria"))
    private Category categoria;
    protected Course() {}
    private Course(String titulo, int horas, Category categoria) {
        this.titulo = titulo; this.horas = horas; this.categoria = categoria;
    }
    public static Course crear(String titulo, int horas, Category categoria) {
        return new Course(titulo, horas, categoria);
    }
    public void actualizar(String titulo, int horas, Category categoria) {
        this.titulo = titulo; this.horas = horas; this.categoria = categoria;
    }
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public int getHoras() { return horas; }
    public Category getCategoria() { return categoria; }
}
