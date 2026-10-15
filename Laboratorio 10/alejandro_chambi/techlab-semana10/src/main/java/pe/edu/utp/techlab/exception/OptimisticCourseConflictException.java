package pe.edu.utp.techlab.exception;
public class OptimisticCourseConflictException extends RuntimeException { public OptimisticCourseConflictException(){super("El curso fue modificado por otro usuario. Recargue los datos e inténtelo de nuevo.");} }
