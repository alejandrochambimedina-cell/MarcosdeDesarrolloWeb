package pe.edu.techlab.domain;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="courses")
public class Course {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, length=120)
    private String name;
    @Column(nullable=false, precision=8, scale=2)
    private BigDecimal price;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="category_id", nullable=false)
    private Category category;
    protected Course() {}
    public Course(String name, BigDecimal price, Category category) {
        change(name, price, category);
    }
    public void change(String name, BigDecimal price, Category category) {
        this.name = CourseRules.normalizeName(name);
        this.price = CourseRules.validatePrice(price);
        this.category = java.util.Objects.requireNonNull(category);
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public Category getCategory() { return category; }
}
