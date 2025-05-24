package co.edu.uniquindio.sistemagestionhospital.model;

import java.util.Date;

public class Cita {

    private Date fecha;
    private Paciente paciente;
    private Medico medico;
    private EstadoCita estadoCita;
    private String motivo;

    public Cita(Date fecha, Paciente paciente, Medico medico) {
        this.fecha = fecha;
        this.paciente = paciente;
        this.medico = medico;
        this.estadoCita = EstadoCita.AGENDADA;

    }

    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public Paciente getPaciente() {
        return paciente;
    }
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    public Medico getMedico() {
        return medico;
    }
    public void setMedico(Medico medico) {
        this.medico = medico;
    }
    public EstadoCita getEstadoCita() {
        return estadoCita;
    }
    public void setestado(EstadoCita estadoCita) {
        this.estadoCita = estadoCita;
    }
    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    @Override
    public String toString(){
        return "Cita con el Dr. " + medico.getNombre() + " el " + fecha + " | Estado: "+estadoCita;
    }


}
