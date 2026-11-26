package pe.edu.techlab.repository;
import pe.edu.techlab.domain.Course;
import org.springframework.data.jpa.repository.*;
import java.util.List;
public interface CourseRepository extends JpaRepository<Course, Long> {
    @EntityGraph(attributePaths="category")
    List<Course> findAllByOrderByNameAsc();
}
