package co.edu.uniquindio.sistemagestionhospital.model;

import java.util.LinkedList;
import java.util.List;

public class Medico extends Usuario implements Notificable {

    private String especialidad;
    private LinkedList<Cita> citas;
    private LinkedList<String> notificaciones;

    public Medico(String id,String nombre, String correo, String contrasena, String especialidad) {
        super(nombre, id,correo, contrasena);
        this.especialidad = especialidad;
        this.citas = new LinkedList<>();
    }

    @Override
    public void mostrarMenu() {

    }

    public void agregarCita(Cita cita) {
        citas.add(cita);
    }

    public void cancelarCita(Cita cita) {
        if (citas.contains(cita)) {
            cita.cancelar();
        }
    }

    public void registrarDiagnostico(Cita cita, String diagnostico, String tratamiento) {
        if (citas.contains(cita)) {
            HistorialMedico historial = new HistorialMedico(this, cita.getPaciente(), diagnostico, tratamiento);
            cita.getPaciente().agregarHistorial(historial);
            cita.setEstado(EstadoCita.COMPLETADA);
        }
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public void recibirNotificacion(String mensaje) {
       notificaciones.add(mensaje);
    }
}
