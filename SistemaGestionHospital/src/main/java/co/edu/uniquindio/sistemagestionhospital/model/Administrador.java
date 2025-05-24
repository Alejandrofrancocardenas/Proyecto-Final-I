package co.edu.uniquindio.sistemagestionhospital.model;

public class Administrador extends Usuario {

    public Administrador(String id, String nombre, String correo, String contrasena) {
        super(id, nombre, correo, contrasena);
    }

    @Override
    public void mostrarMenu() {

    }
}
