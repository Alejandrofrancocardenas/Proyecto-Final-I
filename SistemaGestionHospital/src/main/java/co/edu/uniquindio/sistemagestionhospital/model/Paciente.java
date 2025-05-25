package co.edu.uniquindio.sistemagestionhospital.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class Paciente extends Usuario implements Notificable {

    private final StringProperty cedula;
    private final ArrayList<Cita> citas;
    private final ArrayList<HistorialMedico> historiales;
    private final ArrayList<String> notificaciones;

    public Paciente(String id, String nombre, String correo, String contrasena, String cedula) {
        super(nombre, id, correo, contrasena);
        this.cedula = new SimpleStringProperty(cedula);
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
        return citas.remove(cita);
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

    // Getters y Setters para propiedades
    public String getCedula() {
        return cedula.get();
    }

    public void setCedula(String cedula) {
        this.cedula.set(cedula);
    }

    public StringProperty cedulaProperty() {
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
