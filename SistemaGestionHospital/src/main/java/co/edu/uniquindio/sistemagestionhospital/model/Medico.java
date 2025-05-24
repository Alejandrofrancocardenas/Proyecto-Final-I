package co.edu.uniquindio.sistemagestionhospital.model;

public class Medico extends Usuario {
    public Medico(String id, String nombre, String correo, String contrasena) {
        super(id, nombre, correo, contrasena);
    }

    @Override
    public void mostrarMenu() {

    }
}
