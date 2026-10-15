package pe.edu.utp.techlab.persistence;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.techlab.dto.*;
import pe.edu.utp.techlab.exception.*;
import pe.edu.utp.techlab.service.CourseService;
@SpringBootTest
@Transactional
class CourseCrudIntegrationTests {
 @Autowired CourseService service;
 @Test void cicloCrudPersisteYValida(){
  CourseDto c=service.crear(new CourseInputDto("Curso temporal de prueba",24));
  assertNotNull(c.id()); assertEquals(24,c.horas());
  CourseDto updated=service.actualizar(c.id(),new CourseInputDto("Curso temporal editado",32,c.version()));
  assertEquals(32,updated.horas());
  assertTrue(service.buscarPorId(c.id()).isPresent());
  service.eliminar(c.id());
  assertTrue(service.buscarPorId(c.id()).isEmpty());
 }
 @Test void rechazaHorasFueraDeRango(){assertThrows(InvalidCourseDataException.class,()->service.crear(new CourseInputDto("Horas inválidas",0)));}
 @Test void rechazaTituloDuplicado(){assertThrows(DuplicateCourseTitleException.class,()->service.crear(new CourseInputDto("Spring Boot",12)));}
 @Test void rechazaActualizacionConVersionAntigua(){
  CourseDto c=service.crear(new CourseInputDto("Curso versionado prueba",10));
  CourseDto changed=service.actualizar(c.id(),new CourseInputDto("Curso versionado cambiado",11,c.version()));
  assertThrows(OptimisticCourseConflictException.class,()->service.actualizar(c.id(),new CourseInputDto("Curso con versión vieja",12,c.version())));
 }
}
