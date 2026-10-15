package pe.edu.utp.techlab.controller;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.techlab.dto.*;
import pe.edu.utp.techlab.exception.*;
import pe.edu.utp.techlab.service.CourseService;
@Controller @RequestMapping("/cursos")
public class CourseViewController {
 private static final int MAX_QUERY_LENGTH=60; private final CourseService service;
 public CourseViewController(CourseService s){service=s;}
 @GetMapping public String listar(@RequestParam(defaultValue="") String q,Model m){String query=normalizarConsulta(q);m.addAttribute("cursos",service.buscar(query));m.addAttribute("q",query);return "cursos/lista";}
 @GetMapping("/resumen") public String resumen(Model m){m.addAttribute("cantidadCursos",service.cantidad());m.addAttribute("totalHoras",service.totalHoras());return "cursos/resumen";}
 @GetMapping("/{id}") public String detalle(@PathVariable long id,Model m){m.addAttribute("curso",service.buscarPorId(id).orElseThrow(()->notFound(id)));return "cursos/detalle";}
 @GetMapping("/nuevo") public String nuevo(Model m){return mostrar(new CourseInputDto(),null,"Nuevo curso","Registrar curso",null,m);}
 @PostMapping public String crear(@ModelAttribute("form") CourseInputDto form,Model m,RedirectAttributes r){
  try{CourseDto c=service.crear(form);r.addFlashAttribute("mensaje","Curso registrado correctamente");return "redirect:/cursos/"+c.id();}
  catch(InvalidCourseDataException|DuplicateCourseTitleException e){return mostrar(form,null,"Nuevo curso","Registrar curso",e.getMessage(),m);}
 }
 @GetMapping("/{id}/editar") public String editar(@PathVariable long id,Model m){CourseDto c=service.buscarPorId(id).orElseThrow(()->notFound(id));return mostrar(new CourseInputDto(c.titulo(),c.horas(),c.version()),id,"Editar curso","Guardar cambios",null,m);}
 @PostMapping("/{id}") public String actualizar(@PathVariable long id,@ModelAttribute("form") CourseInputDto form,Model m,RedirectAttributes r){
  try{service.actualizar(id,form);r.addFlashAttribute("mensaje","Curso actualizado correctamente");return "redirect:/cursos/"+id;}
  catch(InvalidCourseDataException|DuplicateCourseTitleException|OptimisticCourseConflictException e){return mostrar(form,id,"Editar curso","Guardar cambios",e.getMessage(),m);}
  catch(CourseNotFoundException e){throw notFound(id);}
 }
 @PostMapping("/{id}/eliminar") public String eliminar(@PathVariable long id,RedirectAttributes r){try{service.eliminar(id);r.addFlashAttribute("mensaje","Curso eliminado correctamente");return "redirect:/cursos";}catch(CourseNotFoundException e){throw notFound(id);}}
 private String mostrar(CourseInputDto f,Long id,String title,String button,String error,Model m){m.addAttribute("form",f);m.addAttribute("cursoId",id);m.addAttribute("tituloPagina",title);m.addAttribute("textoBoton",button);m.addAttribute("error",error);return "cursos/formulario";}
 private String normalizarConsulta(String q){String s=q.strip();if(s.length()>MAX_QUERY_LENGTH)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La búsqueda admite hasta 60 caracteres");return s;}
 private ResponseStatusException notFound(long id){return new ResponseStatusException(HttpStatus.NOT_FOUND,"No existe un curso con el identificador "+id);}
}
