package co.edu.uniquindio.sistemagestionhospital.model;

import java.util.Objects;

public abstract class Usuario { // Es abstracta porque tiene mostrarMenu() abstracto
    private String nombre;
    private String id;
    private String correo;
    private String contrasena;

    public Usuario(String nombre, String id, String correo, String contrasena) {
        this.nombre = nombre;
        this.id = id;
        this.correo = correo;
        this.contrasena = contrasena;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;
        return Objects.equals(getId(), usuario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}