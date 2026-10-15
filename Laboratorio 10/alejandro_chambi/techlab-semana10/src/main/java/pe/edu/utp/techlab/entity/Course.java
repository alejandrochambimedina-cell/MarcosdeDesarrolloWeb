package pe.edu.utp.techlab.entity;
import jakarta.persistence.*;
@Entity @Table(name="cursos")
public class Course {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="titulo",nullable=false,length=120,unique=true) private String titulo;
 @Column(name="horas",nullable=false) private int horas;
 @Version @Column(name="version",nullable=false) private long version;
 protected Course() {}
 private Course(String titulo,int horas){this.titulo=titulo;this.horas=horas;}
 public static Course crear(String titulo,int horas){return new Course(titulo,horas);}
 public void actualizar(String titulo,int horas){this.titulo=titulo;this.horas=horas;}
 public Long getId(){return id;} public String getTitulo(){return titulo;}
 public int getHoras(){return horas;} public long getVersion(){return version;}
}
