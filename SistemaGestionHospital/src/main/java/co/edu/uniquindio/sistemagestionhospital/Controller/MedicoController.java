package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.*;

import java.util.List;

public class MedicoController {

    private Medico medico;

    public MedicoController(Medico medico) {
        this.medico = medico;
    }

    public boolean agregarCita(Cita cita) {
        return medico.agregarCita(cita);
    }

    public boolean cancelarCita(Cita cita) {
        return medico.cancelarCita(cita);
    }

    public boolean registrarHistorialMedico(Cita cita, String diagnostico, String tratamiento) {
        return medico.registrarHistorialMedico(cita, diagnostico, tratamiento);
    }

    public boolean registrarDiagnosticoYTratamiento(Cita cita, String diagnostico, String tratamiento) {
        return medico.registrarDiagnosticoYTratamiento(cita, diagnostico, tratamiento);
    }

    public boolean agregarHorario(HorarioAtencion horario) {
        return medico.agregarHorario(horario);
    }

    public boolean eliminarHorario(HorarioAtencion horario) {
        return medico.eliminarHorario(horario);
    }

    public List<HorarioAtencion> getHorarios() {
        return medico.getHorarios();
    }

    public List<Cita> getCitas() {
        return medico.getCitas();
    }

    public List<String> getNotificaciones() {
        return medico.getNotificaciones();
    }

    public String getEspecialidad() {
        return medico.getEspecialidad();
    }

    public void recibirNotificacion(String mensaje) {
        medico.recibirNotificacion(mensaje);
    }

    public Medico getMedico() {
        return medico;
    }
}
