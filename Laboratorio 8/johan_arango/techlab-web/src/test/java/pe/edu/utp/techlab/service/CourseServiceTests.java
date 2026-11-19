package pe.edu.utp.techlab.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class CourseServiceTests {

    private final CourseService service = new CourseService();

    @Test
    void calculaResumenConLosDatosBase() {
        assertThat(service.cantidad()).isEqualTo(3);
        assertThat(service.totalHoras()).isEqualTo(48);
    }

    @Test
    void buscaSinDistinguirMayusculas() {
        assertThat(service.buscar("spring"))
                .extracting("titulo")
                .containsExactly("Spring Boot");
    }

    @Test
    void filtraPorHorasMinimasYTexto() {
        assertThat(service.buscar("", 16))
                .extracting("titulo")
                .containsExactly("Bootstrap", "Spring Boot");

        assertThat(service.buscar("boot", 16))
                .extracting("titulo")
                .containsExactly("Bootstrap", "Spring Boot");

        assertThat(service.buscar("boot", 18))
                .extracting("titulo")
                .containsExactly("Spring Boot");
    }

}
