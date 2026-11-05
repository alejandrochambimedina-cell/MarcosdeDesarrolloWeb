
package com.example.demo;

import com.example.demo.entity.Course;
import com.example.demo.repository.CourseRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class CourseRepositoryIntegrationTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void consultarCursosDesdeLaBaseDeDatos() {
        List<Course> cursos = courseRepository.findAllByOrderByIdAsc();

        assertNotNull(cursos);
        assertTrue(cursos.size() >= 4);
    }

    @Test
    void contarCursosDesdeLaBaseDeDatos() {
        long cantidad = courseRepository.count();

        assertTrue(cantidad >= 0);
    }
}
