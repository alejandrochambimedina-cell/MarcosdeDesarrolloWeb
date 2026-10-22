package pe.edu.utp.techlab.validation;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import jakarta.validation.*;
import org.junit.jupiter.api.*;
import pe.edu.utp.techlab.dto.CourseInputDto;

class CourseInputDtoValidationTests {
    private ValidatorFactory factory;
    private Validator validator;
    @BeforeEach void setUp() { factory = Validation.buildDefaultValidatorFactory(); validator = factory.getValidator(); }
    @AfterEach void tearDown() { factory.close(); }
    @Test void rejectsBlankTitleAndInvalidHoursAndMissingCategory() {
        Set<ConstraintViolation<CourseInputDto>> violations = validator.validate(new CourseInputDto("   ", 0, null));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titulo")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("horas")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("categoriaId")));
    }
    @Test void acceptsValidCourseInput() {
        assertTrue(validator.validate(new CourseInputDto("Spring avanzado", 24, 2L)).isEmpty());
    }
}
