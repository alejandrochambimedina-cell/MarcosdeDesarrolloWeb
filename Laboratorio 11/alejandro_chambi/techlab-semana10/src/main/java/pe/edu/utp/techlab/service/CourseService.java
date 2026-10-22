package pe.edu.utp.techlab.service;

import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pe.edu.utp.techlab.dto.*;
import pe.edu.utp.techlab.entity.*;
import pe.edu.utp.techlab.exception.*;
import pe.edu.utp.techlab.mapper.CourseMapper;
import pe.edu.utp.techlab.repository.*;

@Service
@Validated
@Transactional(readOnly = true)
public class CourseService {
    private static final int MAX_QUERY_LENGTH = 60;
    private final CourseRepository courses;
    private final CategoryRepository categories;
    private final CourseMapper mapper;
    public CourseService(CourseRepository courses, CategoryRepository categories, CourseMapper mapper) {
        this.courses = courses; this.categories = categories; this.mapper = mapper;
    }
    public List<CourseDto> buscar(String texto, Long categoriaId) {
        String q = Optional.ofNullable(texto).orElse("").strip();
        if (q.length() > MAX_QUERY_LENGTH) throw new InvalidCourseDataException("La búsqueda admite hasta 60 caracteres");
        if (categoriaId != null && !categories.existsById(categoriaId)) throw new CategoryNotFoundException(categoriaId);
        List<Course> found;
        if (categoriaId == null) found = q.isBlank() ? courses.findAllByOrderByIdAsc() : courses.findByTituloContainingIgnoreCaseOrderByIdAsc(q);
        else found = q.isBlank() ? courses.findByCategoria_IdOrderByIdAsc(categoriaId) : courses.findByTituloContainingIgnoreCaseAndCategoria_IdOrderByIdAsc(q, categoriaId);
        return found.stream().map(mapper::toDto).toList();
    }
    public Optional<CourseDto> buscarPorId(long id) { return courses.findDetailById(id).map(mapper::toDto); }
    public List<CategoryDto> listarCategorias() { return categories.findAllByOrderByNombreAsc().stream().map(c -> new CategoryDto(c.getId(), c.getNombre())).toList(); }
    public long cantidad() { return courses.count(); }
    public int totalHoras() { Long n = courses.sumarHoras(); return Math.toIntExact(n == null ? 0 : n); }
    @Transactional
    public CourseDto crear(@Valid @NotNull CourseInputDto input) {
        Data data = normalizar(input);
        if (courses.existsByTituloIgnoreCase(data.titulo)) throw new DuplicateCourseTitleException(data.titulo);
        Category category = categories.findById(data.categoriaId).orElseThrow(() -> new CategoryNotFoundException(data.categoriaId));
        try { return mapper.toDto(courses.saveAndFlush(Course.crear(data.titulo, data.horas, category))); }
        catch (DataIntegrityViolationException e) { throw new DuplicateCourseTitleException(data.titulo); }
    }
    @Transactional
    public CourseDto actualizar(long id, @Valid @NotNull CourseInputDto input) {
        Course course = courses.findDetailById(id).orElseThrow(() -> new CourseNotFoundException(id));
        Data data = normalizar(input);
        if (courses.existsByTituloIgnoreCaseAndIdNot(data.titulo, id)) throw new DuplicateCourseTitleException(data.titulo);
        Category category = categories.findById(data.categoriaId).orElseThrow(() -> new CategoryNotFoundException(data.categoriaId));
        course.actualizar(data.titulo, data.horas, category);
        try { courses.flush(); return mapper.toDto(course); }
        catch (DataIntegrityViolationException e) { throw new DuplicateCourseTitleException(data.titulo); }
    }
    @Transactional
    public void eliminar(long id) { Course course = courses.findById(id).orElseThrow(() -> new CourseNotFoundException(id)); courses.delete(course); }
    private Data normalizar(CourseInputDto in) {
        String title = Optional.ofNullable(in.getTitulo()).orElse("").strip().replaceAll("\\s+", " ");
        if (title.isBlank()) throw new InvalidCourseDataException("El título es obligatorio");
        if (title.length() > 120) throw new InvalidCourseDataException("El título admite hasta 120 caracteres");
        Integer hours = in.getHoras();
        if (hours == null || hours < 1 || hours > 500) throw new InvalidCourseDataException("Las horas deben estar entre 1 y 500");
        Long categoryId = in.getCategoriaId();
        if (categoryId == null) throw new InvalidCourseDataException("Seleccione una categoría");
        return new Data(title, hours, categoryId);
    }
    private record Data(String titulo, int horas, long categoriaId) {}
}
