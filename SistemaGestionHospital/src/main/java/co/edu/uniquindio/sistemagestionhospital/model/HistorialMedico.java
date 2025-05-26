package co.edu.uniquindio.sistemagestionhospital.model;

import java.time.LocalDate;

public class HistorialMedico {
    private LocalDate fecha;
    private String diagnostico;
    private String tratamiento;
    private Medico medico;
    private Paciente paciente;
    private String descripcion;

    public HistorialMedico(String diagnostico, String tratamiento, Medico medico, Paciente paciente, String descripcion) {
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.medico = medico;
        this.paciente = paciente;
        this.descripcion = descripcion;
        this.fecha = LocalDate.now();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
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

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Fecha: " + fecha + ", Diagnostico: " + diagnostico + ", Tratamiento: " + tratamiento;
    }
}
