package pe.edu.utp.techlab.mapper;

import org.springframework.stereotype.Component;
import pe.edu.utp.techlab.dto.CourseDto;
import pe.edu.utp.techlab.dto.CourseInputDto;
import pe.edu.utp.techlab.entity.Course;

@Component
public class CourseMapper {

    public CourseDto toDto(Course course) {
        return new CourseDto(
            course.getId(),
            course.getTitulo(),
            course.getHoras()
        );
    }

    public Course toEntity(CourseInputDto input) {
        return new Course(input.getTitulo().strip(), input.getHoras());
    }

    public CourseInputDto toInput(Course course) {
        return new CourseInputDto(course.getTitulo(), course.getHoras());
    }
}