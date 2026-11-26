package pe.edu.techlab.service;
import pe.edu.techlab.domain.*;
import pe.edu.techlab.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
@Service
@Transactional(readOnly=true)
public class CourseService {
    private final CourseRepository courses;
    private final CategoryRepository categories;
    public CourseService(CourseRepository courses, CategoryRepository categories) {
        this.courses=courses; this.categories=categories;
    }
    public List<CourseView> list() {
        return courses.findAllByOrderByNameAsc().stream().map(this::view).toList();
    }
    public List<CategoryView> categories() {
        return categories.findAll().stream()
            .map(c -> new CategoryView(c.getId(), c.getName())).toList();
    }
    public CourseView get(long id) { return view(require(id)); }
    @Transactional
    public CourseView create(CourseInput input) {
        return view(courses.save(new Course(input.name(), input.price(), category(input))));
    }
    @Transactional
    public CourseView update(long id, CourseInput input) {
        Course course=require(id);
        course.change(input.name(), input.price(), category(input));
        return view(course);
    }
    @Transactional
    public void delete(long id) { courses.delete(require(id)); }
    private Course require(long id) {
        return courses.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND,"Curso no encontrado"));
    }
    private Category category(CourseInput input) {
        return categories.findById(input.categoryId()).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.BAD_REQUEST,"Categoría inexistente"));
    }
    private CourseView view(Course c) {
        return new CourseView(c.getId(),c.getName(),c.getPrice(),
            c.getCategory().getId(),c.getCategory().getName());
    }
}
