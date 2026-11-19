
package com.example.demo;

import com.example.demo.dto.CourseInputDto;
import com.example.demo.entity.Category;
import com.example.demo.exception.InvalidCourseDataException;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.service.CourseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository repository;

    @Mock
    private CourseMapper mapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void crearConTituloVacioLanzaError() {
        CourseInputDto input = new CourseInputDto("", 20, 1L);

        assertThrows(
                InvalidCourseDataException.class,
                () -> courseService.crear(input)
        );

        verifyNoInteractions(repository, mapper, categoryRepository);
    }

    @Test
    void crearConHorasFueraDeRangoLanzaError() {
        CourseInputDto input = new CourseInputDto("Java", 0, 1L);

        assertThrows(
                InvalidCourseDataException.class,
                () -> courseService.crear(input)
        );

        verifyNoInteractions(repository, mapper, categoryRepository);
    }

    @Test
    void crearConTituloDemasiadoLargoLanzaError() {
        String titulo = "A".repeat(121);
        CourseInputDto input = new CourseInputDto(titulo, 20, 1L);

        assertThrows(
                InvalidCourseDataException.class,
                () -> courseService.crear(input)
        );

        verifyNoInteractions(repository, mapper, categoryRepository);
    }
}
