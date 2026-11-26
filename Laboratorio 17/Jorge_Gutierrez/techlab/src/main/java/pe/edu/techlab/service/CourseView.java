package pe.edu.techlab.service;
import java.math.BigDecimal;
public record CourseView(Long id, String name, BigDecimal price,
                         Long categoryId, String categoryName) {}
