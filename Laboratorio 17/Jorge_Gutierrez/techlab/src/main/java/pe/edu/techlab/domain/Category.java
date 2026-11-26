package pe.edu.techlab.domain;
import jakarta.persistence.*;
@Entity @Table(name="categories")
public class Category {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, length=80, unique=true)
    private String name;
    protected Category() {}
    public Long getId() { return id; }
    public String getName() { return name; }
}
