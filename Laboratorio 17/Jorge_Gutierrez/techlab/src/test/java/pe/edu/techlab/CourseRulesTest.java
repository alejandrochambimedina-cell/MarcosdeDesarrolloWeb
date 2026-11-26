package pe.edu.techlab;
import pe.edu.techlab.domain.CourseRules;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class CourseRulesTest {
    @Test void normaliza() { assertEquals("Java",CourseRules.normalizeName(" Java ")); }
    @Test void precioNegativo() { assertThrows(IllegalArgumentException.class,
        () -> CourseRules.validatePrice(new BigDecimal("-1"))); }
    @Test void precioValido() { assertEquals(new BigDecimal("12.30"),
        CourseRules.validatePrice(new BigDecimal("12.3"))); }
}
