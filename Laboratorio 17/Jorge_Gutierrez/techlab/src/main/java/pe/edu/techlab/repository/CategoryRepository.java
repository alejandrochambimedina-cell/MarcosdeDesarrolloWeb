package pe.edu.techlab.repository;
import pe.edu.techlab.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoryRepository extends JpaRepository<Category, Long> {}
