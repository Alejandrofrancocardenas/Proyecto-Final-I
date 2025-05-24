package co.edu.uniquindio.sistemagestionhospital.model;

import java.util.ArrayList;

public class Medico extends Usuario implements Notificable {

    private String especialidad;
    private ArrayList<Cita> citas;
    private ArrayList<String> notificaciones;

    public Medico(String id, String nombre, String correo, String contrasena, String especialidad) {
        super(nombre, id, correo, contrasena);
        this.especialidad = especialidad;
        this.citas = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }

    @Override
    public void mostrarMenu() {
        // Implementar si es necesario
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

    public boolean registrarDiagnosticoYTratamiento(Cita cita, String diagnostico, String tratamiento) {
        if (!citas.contains(cita)) {
            return false; // El médico no tiene esta cita
        }

        HistorialMedico historial = new HistorialMedico(this, cita.getPaciente(), diagnostico, tratamiento);
        cita.setHistorialMedico(historial);
        cita.setEstado(EstadoCita.COMPLETADA);
        cita.getPaciente().agregarHistorial(historial);
        return true;
    }



    public ArrayList<Cita> getCitas() {
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

    public ArrayList<String> getNotificaciones() {
        return notificaciones;
    }
}
