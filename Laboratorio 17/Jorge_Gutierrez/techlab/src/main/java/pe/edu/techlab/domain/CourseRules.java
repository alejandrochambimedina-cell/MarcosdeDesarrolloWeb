package pe.edu.techlab.domain;
import java.math.BigDecimal;
public final class CourseRules {
    private CourseRules() {}
    public static String normalizeName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");
        String clean = name.strip();
        if (clean.length() > 120)
            throw new IllegalArgumentException("Máximo 120 caracteres");
        return clean;
    }
    public static BigDecimal validatePrice(BigDecimal price) {
        if (price == null || price.signum() < 0 ||
            price.compareTo(new BigDecimal("999999.99")) > 0 ||
            price.stripTrailingZeros().scale() > 2)
            throw new IllegalArgumentException("Precio inválido");
        return price.setScale(2);
    }
}
