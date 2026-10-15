package pe.edu.utp.techlab.service;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.techlab.dto.*;
import pe.edu.utp.techlab.entity.Course;
import pe.edu.utp.techlab.exception.*;
import pe.edu.utp.techlab.mapper.CourseMapper;
import pe.edu.utp.techlab.repository.CourseRepository;
@Service
@Transactional(readOnly=true)
public class CourseService {
 private static final int MAX_QUERY_LENGTH=60, MAX_TITLE_LENGTH=120, MIN_HOURS=1, MAX_HOURS=500;
 private final CourseRepository repository; private final CourseMapper mapper;
 public CourseService(CourseRepository r,CourseMapper m){repository=r;mapper=m;}
 public List<CourseDto> buscar(String texto){
  String criterio=Optional.ofNullable(texto).orElse("").strip();
  if(criterio.length()>MAX_QUERY_LENGTH) throw new InvalidCourseDataException("La búsqueda admite hasta 60 caracteres");
  List<Course> es=criterio.isBlank()?repository.findAllByOrderByIdAsc():repository.findByTituloContainingIgnoreCaseOrderByIdAsc(criterio);
  return es.stream().map(mapper::toDto).toList();
 }
 public Optional<CourseDto> buscarPorId(long id){return repository.findById(id).map(mapper::toDto);}
 public long cantidad(){return repository.count();}
 public int totalHoras(){Long n=repository.sumarHoras();return Math.toIntExact(n==null?0:n);}
 @Transactional
 public CourseDto crear(CourseInputDto input){
  Data d=normalizar(input);
  if(repository.existsByTituloIgnoreCase(d.titulo))throw new DuplicateCourseTitleException(d.titulo);
  try{return mapper.toDto(repository.saveAndFlush(Course.crear(d.titulo,d.horas)));}
  catch(DataIntegrityViolationException e){throw new DuplicateCourseTitleException(d.titulo);}
 }
 @Transactional
 public CourseDto actualizar(long id,CourseInputDto input){
  Course entity=repository.findById(id).orElseThrow(()->new CourseNotFoundException(id));
  Data d=normalizar(input);
  if(repository.existsByTituloIgnoreCaseAndIdNot(d.titulo,id))throw new DuplicateCourseTitleException(d.titulo);
  if(input.getVersion()!=null && input.getVersion()!=entity.getVersion())throw new OptimisticCourseConflictException();
  entity.actualizar(d.titulo,d.horas);
  try {repository.flush(); return mapper.toDto(entity);}
  catch(ObjectOptimisticLockingFailureException e){throw new OptimisticCourseConflictException();}
  catch(DataIntegrityViolationException e){throw new DuplicateCourseTitleException(d.titulo);}
 }
 @Transactional
 public void eliminar(long id){Course e=repository.findById(id).orElseThrow(()->new CourseNotFoundException(id));repository.delete(e);repository.flush();}
 private Data normalizar(CourseInputDto in){
  if(in==null)throw new InvalidCourseDataException("Debe enviar los datos del curso");
  String t=Optional.ofNullable(in.getTitulo()).orElse("").strip().replaceAll("\\s+"," ");
  if(t.isBlank())throw new InvalidCourseDataException("El título es obligatorio");
  if(t.length()>MAX_TITLE_LENGTH)throw new InvalidCourseDataException("El título admite hasta 120 caracteres");
  Integer h=in.getHoras();
  if(h==null)throw new InvalidCourseDataException("La cantidad de horas es obligatoria");
  if(h<MIN_HOURS||h>MAX_HOURS)throw new InvalidCourseDataException("Las horas deben estar entre 1 y 500");
  return new Data(t,h);
 }
 private record Data(String titulo,int horas){}
}
