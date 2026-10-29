package com.example.demo.mapper;

import org.springframework.stereotype.Component;
import com.example.demo.dto.CourseDto;
import com.example.demo.entity.Course;

@Component
public class CourseMapper {
    public CourseDto toDto(Course course) {
        return new CourseDto(course.getId(), course.getTitulo(), course.getHoras());
    }
}
