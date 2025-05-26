package co.edu.uniquindio.sistemagestionhospital.model;

import java.util.ArrayList;
import java.util.List;
public class Medico extends Usuario implements Notificable {

    private String especialidad;
    private ArrayList<Cita> citas;
    private ArrayList<String> notificaciones;
    private List<HorarioAtencion> horarios;

    public Medico(String id, String nombre, String correo, String contrasena, String especialidad) {
        super(nombre, id, correo, contrasena);
        this.especialidad = especialidad;
        this.citas = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
        this.horarios = new ArrayList<>();
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
    public boolean agregarHorario(HorarioAtencion horario) {
        if (!horarios.contains(horario)) {
            horarios.add(horario);
            return true;
        }
        return false;
    }

    public boolean eliminarHorario(HorarioAtencion horario) {
        return horarios.remove(horario);
    }

    public List<HorarioAtencion> getHorarios() {
        return horarios;
    }

    public boolean registrarHistorialMedico(Cita cita, String diagnostico, String tratamiento) {
        if (!citas.contains(cita)) {
            return false;
        }

        HistorialMedico historial = new HistorialMedico(diagnostico, tratamiento, this, cita.getPaciente(), "Historial generado a partir de la cita.");


        cita.setEstado(EstadoCita.COMPLETADA);
        cita.setHistorialMedico(historial);
        return true;
    }

    public boolean registrarDiagnosticoYTratamiento(Cita cita, String diagnostico, String tratamiento) {
        if (!citas.contains(cita)) {
            return false; // El médico no tiene esta cita
        }

        HistorialMedico historial = new HistorialMedico(diagnostico, tratamiento, this, cita.getPaciente(), "Historial generado a partir de la cita.");

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
    @Override
    public String toString() {
        return nombre + " - " + especialidad;
    }

}
