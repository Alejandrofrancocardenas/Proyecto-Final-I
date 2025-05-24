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

    public boolean agregarCita(Cita cita) {
        if (citas.contains(cita)) {
            return false; // Ya existe
        }
        citas.add(cita);
        return true;
    }


    public boolean cancelarCita(Cita cita) {
        if (citas.contains(cita)) {
            cita.cancelar();
            return true;
        }
        return false;
    }
    public boolean registrarHistorialMedico(Cita cita, String diagnostico, String tratamiento) {
        if (!citas.contains(cita)) {
            return false;
        }

        HistorialMedico historial = new HistorialMedico(this, cita.getPaciente(), diagnostico, tratamiento);
        cita.setEstado(EstadoCita.COMPLETADA);
        cita.setHistorialMedico(historial);
        return true;
    }

    public void registrarDiagnostico(Cita cita, String diagnostico, String tratamiento) {
        if (citas.contains(cita)) {
            HistorialMedico historial = new HistorialMedico(this, cita.getPaciente(), diagnostico, tratamiento);
            cita.getPaciente().agregarHistorial(historial);
            cita.setEstado(EstadoCita.COMPLETADA);
        }
    }

    public LinkedList<Cita> getCitas() {
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
