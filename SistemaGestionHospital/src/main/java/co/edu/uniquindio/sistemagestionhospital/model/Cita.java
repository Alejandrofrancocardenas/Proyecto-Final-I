package co.edu.uniquindio.sistemagestionhospital.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class Cita {

    private String id;
    private Paciente paciente;
    private Medico medico;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoCita estado;

    public Cita(Paciente paciente, Medico medico, LocalDate fecha, LocalTime hora) {
        this.id = UUID.randomUUID().toString();
        this.paciente = paciente;
        this.medico = medico;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = EstadoCita.AGENDADA;
    }

    // Métodos
    public void cancelar() {
        this.estado = EstadoCita.CANCELADA;
    }

    public void completar() {
        this.estado = EstadoCita.COMPLETADA;
    }

    public boolean esActiva() {
        return this.estado == EstadoCita.AGENDADA;
    }

    // Getters y Setters
    public String getId() {
        return id;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Cita [ID=" + id + ", Fecha=" + fecha + ", Hora=" + hora +
                ", Estado=" + estado + ", Paciente=" + paciente.getNombre() +
                ", Medico=" + medico.getNombre() + "]";
    }
}
