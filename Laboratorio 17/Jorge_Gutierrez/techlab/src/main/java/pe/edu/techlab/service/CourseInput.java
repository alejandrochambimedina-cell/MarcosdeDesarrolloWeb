package pe.edu.techlab.service;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record CourseInput(
    @NotBlank @Size(max=120) String name,
    @NotNull @DecimalMin("0.00") @Digits(integer=6, fraction=2) BigDecimal price,
    @NotNull @Positive Long categoryId) {}
