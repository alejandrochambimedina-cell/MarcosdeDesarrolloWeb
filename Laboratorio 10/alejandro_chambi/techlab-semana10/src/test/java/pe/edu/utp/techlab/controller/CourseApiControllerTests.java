package pe.edu.utp.techlab.controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.utp.techlab.dto.*;
import pe.edu.utp.techlab.exception.*;
import pe.edu.utp.techlab.service.CourseService;
@WebMvcTest(CourseController.class)
class CourseApiControllerTests {
 @Autowired MockMvc mvc;
 @MockitoBean CourseService service;
 @Test void postCorrectoDevuelve201YLocation() throws Exception {
  given(service.crear(any(CourseInputDto.class))).willReturn(new CourseDto(44L,"Java profesional",24,0));
  mvc.perform(post("/api/v1/cursos").contentType("application/json").content("{\"titulo\":\"Java profesional\",\"horas\":24}"))
   .andExpect(status().isCreated()).andExpect(header().string("Location","http://localhost/api/v1/cursos/44")).andExpect(jsonPath("$.id").value(44));
 }
 @Test void horasInvalidasDevuelven400ProblemDetail() throws Exception {
  given(service.crear(any(CourseInputDto.class))).willThrow(new InvalidCourseDataException("Las horas deben estar entre 1 y 500"));
  mvc.perform(post("/api/v1/cursos").contentType("application/json").content("{\"titulo\":\"X\",\"horas\":0}"))
   .andExpect(status().isBadRequest()).andExpect(content().contentTypeCompatibleWith("application/problem+json"));
 }
 @Test void deleteDevuelve204() throws Exception {
  willDoNothing().given(service).eliminar(44L);
  mvc.perform(delete("/api/v1/cursos/44")).andExpect(status().isNoContent()).andExpect(content().string(""));
 }
}
