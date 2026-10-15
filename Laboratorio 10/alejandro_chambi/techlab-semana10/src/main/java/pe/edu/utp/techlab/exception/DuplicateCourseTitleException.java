package pe.edu.utp.techlab.exception;
public class DuplicateCourseTitleException extends RuntimeException { public DuplicateCourseTitleException(String t){super("Ya existe un curso con el título "+t);} }
