package co.edu.uniquindio.sistemagestionhospital.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Usuario {
    protected StringProperty nombre;
    protected StringProperty id;
    protected StringProperty correo;
    protected StringProperty contrasena;

    public Usuario(String nombre, String id, String correo, String contrasena) {
        this.nombre = new SimpleStringProperty(nombre);
        this.id = new SimpleStringProperty(id);
        this.correo = new SimpleStringProperty(correo);
        this.contrasena = new SimpleStringProperty(contrasena);
    }

    // Getters y setters
    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getCorreo() {
        return correo.get();
    }

    public void setCorreo(String correo) {
        this.correo.set(correo);
    }

    public StringProperty correoProperty() {
        return correo;
    }

    public String getContrasena() {
        return contrasena.get();
    }

    public void setContrasena(String contrasena) {
        this.contrasena.set(contrasena);
    }

    public StringProperty contrasenaProperty() {
        return contrasena;
    }

    public abstract void mostrarMenu();
}
