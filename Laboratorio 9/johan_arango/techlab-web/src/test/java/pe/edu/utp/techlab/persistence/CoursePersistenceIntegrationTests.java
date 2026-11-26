package pe.edu.utp.techlab.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pe.edu.utp.techlab.dto.CourseDto;
import pe.edu.utp.techlab.repository.CourseRepository;
import pe.edu.utp.techlab.service.CourseService;

@SpringBootTest
class CoursePersistenceIntegrationTests {

    @Autowired
    private CourseService service;

    @Autowired
    private CourseRepository repository;

    @Test
    void flywayCargaElCatalogoInicial() {
        assertEquals(3L, service.cantidad());
        assertEquals(48, service.totalHoras());

        List<String> titulos = service.buscar("").stream()
                .map(CourseDto::titulo)
                .toList();

        assertEquals(
                List.of("HTML y CSS", "Bootstrap", "Spring Boot"),
                titulos);
    }

    @Test
    void repositorioBuscaSinDistinguirMayusculas() {
        var resultado = repository.findByTituloContainingIgnoreCaseOrderByIdAsc("boot");
        assertEquals(2, resultado.size());
        assertEquals("Bootstrap", resultado.get(0).getTitulo());
        assertEquals("Spring Boot", resultado.get(1).getTitulo());
    }

}
