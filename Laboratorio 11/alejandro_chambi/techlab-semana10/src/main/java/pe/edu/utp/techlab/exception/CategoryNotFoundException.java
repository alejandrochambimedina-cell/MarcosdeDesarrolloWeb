package pe.edu.utp.techlab.exception;
public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(long id) { super("No existe una categoría con el identificador " + id); }
}
