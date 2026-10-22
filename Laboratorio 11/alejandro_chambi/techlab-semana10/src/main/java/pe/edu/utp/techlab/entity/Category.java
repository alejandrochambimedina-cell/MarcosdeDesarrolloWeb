package pe.edu.utp.techlab.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias")
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", nullable = false, length = 80, unique = true)
    private String nombre;
    protected Category() {}
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
}
