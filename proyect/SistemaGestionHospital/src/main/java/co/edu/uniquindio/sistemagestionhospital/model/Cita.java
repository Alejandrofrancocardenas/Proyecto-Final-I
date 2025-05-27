package co.edu.uniquindio.sistemagestionhospital.model; // O tu paquete

import java.time.LocalDate;
import java.time.LocalTime;

public class Cita {
    private String id;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private Paciente paciente;
    private Medico medico;
    private EstadoCita estado;
    private String especialidad;

    public Cita(String id, LocalDate fecha, LocalTime hora, String motivo,
                Paciente paciente, Medico medico, String especialidad, EstadoCita estado) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.paciente = paciente;
        this.medico = medico;
        this.especialidad = especialidad;
        this.estado = estado;
    }

    public Paciente getPaciente() {
        return paciente;
    }


    public String getId() { return id; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }
    public String getMotivo() { return motivo; }
    public Medico getMedico() { return medico; }
    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }




    public HistorialMedico getHistorialMedicoDelPaciente() {
        if (this.paciente != null) {
            return this.paciente.getHistorialMedico();
        }
        return null;
    }


    @Override
    public String toString() {

        return "Cita: " + id + " - " + fecha + " " + hora + " con " + (medico != null ? medico.getNombre() : "N/A") +
                " para " + (paciente != null ? paciente.getNombre() : "N/A") + " (" + estado + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cita cita = (Cita) o;
        return java.util.Objects.equals(id, cita.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }
}