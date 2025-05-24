package co.edu.uniquindio.sistemagestionhospital.model;

import java.util.ArrayList;
import java.util.List;

public class Paciente extends Usuario implements Notificable {

    private String cedula;
    private ArrayList<Cita> citas;
    private ArrayList<HistorialMedico> historiales;
    private ArrayList<String> notificaciones;

    public Paciente(String id, String nombre, String correo, String contrasena, String cedula) {
        super(nombre, id, correo, contrasena);
        this.cedula = cedula;
        this.citas = new ArrayList<>();
        this.historiales = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }

    @Override
    public void mostrarMenu() {
        // Implementación futura si se necesita
    }

    public void actualizarDatos(String nuevoNombre, String nuevoCorreo, String nuevaContrasena) {
        setNombre(nuevoNombre);
        setCorreo(nuevoCorreo);
        setContrasena(nuevaContrasena);
    }

    public void solicitarCita(Cita cita) {
        citas.add(cita);
        recibirNotificacion("Cita solicitada para el día " + cita.getFecha());
    }

    public boolean agregarCita(Cita cita) {
        if (citas.contains(cita)) {
            return false;
        }
        citas.add(cita);
        return true;
    }

    public boolean cancelarCita(Cita cita) {
        return citas.remove(cita); // Solo si existe
    }

    public List<HistorialMedico> consultarHistorialMedico() {
        return historiales;
    }

    public void agregarHistorial(HistorialMedico historial) {
        historiales.add(historial);
    }

    @Override
    public void recibirNotificacion(String mensaje) {
        notificaciones.add(mensaje);
    }

    // Getters
    public String getCedula() {
        return cedula;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public List<HistorialMedico> getHistoriales() {
        return historiales;
    }

    public List<String> getNotificaciones() {
        return notificaciones;
    }
}
