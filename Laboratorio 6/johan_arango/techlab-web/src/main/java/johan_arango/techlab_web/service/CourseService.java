package johan_arango.techlab_web.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Service;

import johan_arango.techlab_web.dto.CourseDto;

@Service
public class CourseService {

    private final List<CourseDto> cursos = List.of(
        new CourseDto(1L, "HTML y CSS", 12),
        new CourseDto(2L, "Bootstrap", 16),
        new CourseDto(3L, "Spring Boot", 20)
    );

    public List<CourseDto> buscar(String consulta) {

        String q = consulta.strip().toLowerCase(Locale.ROOT);

        return cursos.stream()
                .filter(c -> c.titulo()
                        .toLowerCase(Locale.ROOT)
                        .contains(q))
                .toList();
    }

    public Optional<CourseDto> buscarPorId(long id) {

        return cursos.stream()
                .filter(c -> c.id() == id)
                .findFirst();
    }
}