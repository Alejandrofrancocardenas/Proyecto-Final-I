package co.edu.uniquindio.sistemagestionhospital.model;

import java.util.LinkedList;
import java.util.List;

public class Paciente extends Usuario implements Notificable {

    private String cedula;
    private LinkedList<Cita> citas;
    private LinkedList<HistorialMedico> historiales;
    private LinkedList<String> notificaciones;

    public Paciente(String id,String nombre, String correo, String contrasena, String cedula) {
        super(nombre,id, correo, contrasena);
        this.cedula = cedula;
        this.citas = new LinkedList<>();
        this.historiales = new LinkedList<>();
        this.notificaciones = new LinkedList<>();
    }

    @Override
    public void mostrarMenu() {

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

    public List<String> getNotificaciones() {
        return notificaciones;
    }

    // Otros getters
    public String getCedula() {
        return cedula;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public List<HistorialMedico> getHistoriales() {
        return historiales;
    }
    @Override
    public void recibirNotificacion( String mensaje){
        notificaciones.add(mensaje);
    }
}
