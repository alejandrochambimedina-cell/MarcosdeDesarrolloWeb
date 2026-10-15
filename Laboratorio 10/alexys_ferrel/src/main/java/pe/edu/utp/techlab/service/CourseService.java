package pe.edu.utp.techlab.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.techlab.dto.CourseDto;
import pe.edu.utp.techlab.dto.CourseInputDto;
import pe.edu.utp.techlab.entity.Course;
import pe.edu.utp.techlab.exception.CourseNotFoundException;
import pe.edu.utp.techlab.exception.DuplicateCourseException;
import pe.edu.utp.techlab.mapper.CourseMapper;
import pe.edu.utp.techlab.repository.CourseRepository;

@Service
@Transactional(readOnly = true)
public class CourseService {
    private final CourseRepository repository;
    private final CourseMapper mapper;

    public CourseService(CourseRepository repository, CourseMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CourseDto> buscar(String texto) {
        String criterio = Optional.ofNullable(texto).orElse("").strip();
        List<Course> entidades = criterio.isBlank()
                ? repository.findAllByOrderByIdAsc()
                : repository.findByTituloContainingIgnoreCaseOrderByIdAsc(criterio);
        return entidades.stream().map(mapper::toDto).toList();
    }

    public Optional<CourseDto> buscarPorId(long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Transactional
    public CourseDto crear(CourseInputDto input) {
        String tituloLimpio = input.getTitulo().strip();
        if (repository.existsByTituloIgnoreCase(tituloLimpio)) {
            throw new DuplicateCourseException("Ya existe un curso con el título: " + tituloLimpio);
        }
        input.setTitulo(tituloLimpio);
        Course curso = mapper.toEntity(input);
        Course guardado = repository.save(curso);
        return mapper.toDto(guardado);
    }

    @Transactional
    public CourseDto actualizar(long id, CourseInputDto input) {
        Course curso = repository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Curso no encontrado con ID: " + id));
        String tituloLimpio = input.getTitulo().strip();
        if (repository.existsByTituloIgnoreCaseAndIdNot(tituloLimpio, id)) {
            throw new DuplicateCourseException("Ya existe otro curso con el título: " + tituloLimpio);
        }
        curso.actualizar(tituloLimpio, input.getHoras());
        return mapper.toDto(curso);
    }

    @Transactional
    public void eliminar(long id) {
        if (!repository.existsById(id)) {
            throw new CourseNotFoundException("Curso no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    public long cantidad() {
        return repository.count();
    }

    public int totalHoras() {
        Long total = repository.sumarHoras();
        return Math.toIntExact(total == null ? 0L : total);
    }
}