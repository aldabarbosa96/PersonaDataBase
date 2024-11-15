package personas;

import java.util.Date;

public class Persona {
    private long id;
    private String nombre;
    private String apellido1, apellido2;
    private Date fechaNaciemiento;
    private boolean vive;//igual puede ser útil en un futuro
    private String direccion;
    private String email;
    private String telefono;
    private String rol;


    public Persona(long id, String nombre, String apellido1, String apellido2, Date fechaNaciemiento, String direccion, String email, String telefono, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.fechaNaciemiento = fechaNaciemiento;
        this.direccion = direccion;
        this.email = email;
        this.telefono = telefono;
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public Date getFechaNaciemiento() {
        return fechaNaciemiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public void setFechaNaciemiento(Date fechaNaciemiento) {
        this.fechaNaciemiento = fechaNaciemiento;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
