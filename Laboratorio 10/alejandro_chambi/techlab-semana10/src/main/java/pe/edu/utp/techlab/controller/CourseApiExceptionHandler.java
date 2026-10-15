package pe.edu.utp.techlab.controller;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.techlab.exception.*;
@RestControllerAdvice(assignableTypes=CourseController.class)
public class CourseApiExceptionHandler {
 @ExceptionHandler(InvalidCourseDataException.class) public ResponseEntity<ProblemDetail> invalid(InvalidCourseDataException e){return problem(HttpStatus.BAD_REQUEST,"Datos inválidos",e.getMessage());}
 @ExceptionHandler(CourseNotFoundException.class) public ResponseEntity<ProblemDetail> missing(CourseNotFoundException e){return problem(HttpStatus.NOT_FOUND,"Curso no encontrado",e.getMessage());}
 @ExceptionHandler({DuplicateCourseTitleException.class,DataIntegrityViolationException.class}) public ResponseEntity<ProblemDetail> conflict(RuntimeException e){return problem(HttpStatus.CONFLICT,"Conflicto de datos",e instanceof DuplicateCourseTitleException?e.getMessage():"La operación incumple una restricción de la base de datos");}
 @ExceptionHandler({OptimisticCourseConflictException.class,ObjectOptimisticLockingFailureException.class}) public ResponseEntity<ProblemDetail> optimistic(RuntimeException e){return problem(HttpStatus.CONFLICT,"Conflicto de concurrencia",e.getMessage());}
 private ResponseEntity<ProblemDetail> problem(HttpStatus s,String t,String d){ProblemDetail p=ProblemDetail.forStatusAndDetail(s,d);p.setTitle(t);return ResponseEntity.status(s).body(p);}
}
