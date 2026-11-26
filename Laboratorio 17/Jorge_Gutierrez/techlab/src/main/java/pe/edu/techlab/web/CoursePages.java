package pe.edu.techlab.web;
import pe.edu.techlab.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
@Controller
public class CoursePages {
    private final CourseService service;
    public CoursePages(CourseService service) { this.service=service; }
    @GetMapping("/") String home() { return "redirect:/courses"; }
    @GetMapping("/courses") String list(Model model, org.springframework.security.core.Authentication auth) {
        model.addAttribute("admin",auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        model.addAttribute("courses",service.list()); return "courses";
    }
    @GetMapping("/courses/new") String newCourse(Model model) {
        model.addAttribute("course",new CourseInput("",null,null));
        return form(model,null);
    }
    @GetMapping("/courses/{id}/edit") String edit(@PathVariable long id, Model model) {
        CourseView c=service.get(id);
        model.addAttribute("course",new CourseInput(c.name(),c.price(),c.categoryId()));
        return form(model,id);
    }
    @PostMapping("/courses") String create(@Valid @ModelAttribute("course") CourseInput input,
        BindingResult errors, Model model) {
        if (!errors.hasErrors()) {
            try { service.create(input); return "redirect:/courses"; }
            catch (ResponseStatusException e) { if(e.getStatusCode()!=HttpStatus.BAD_REQUEST) throw e;
                errors.rejectValue("categoryId","invalid","Categoría inexistente"); }
        }
        return form(model,null);
    }
    @PostMapping("/courses/{id}/edit") String update(@PathVariable long id,
        @Valid @ModelAttribute("course") CourseInput input, BindingResult errors, Model model) {
        if (!errors.hasErrors()) {
            try { service.update(id,input); return "redirect:/courses"; }
            catch (ResponseStatusException e) { if(e.getStatusCode()!=HttpStatus.BAD_REQUEST) throw e;
                errors.rejectValue("categoryId","invalid","Categoría inexistente"); }
        }
        return form(model,id);
    }
    @PostMapping("/courses/{id}/delete") String delete(@PathVariable long id) {
        service.delete(id); return "redirect:/courses";
    }
    private String form(Model model, Long id) {
        model.addAttribute("courseId",id);
        model.addAttribute("categories",service.categories()); return "form";
    }
}
