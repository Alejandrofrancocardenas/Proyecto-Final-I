package co.edu.uniquindio.sistemagestionhospital.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class HistorialMedico implements Serializable {

    private Medico medico;
    private Paciente paciente;
    private String diagnostico;
    private String tratamiento;
    private LocalDateTime fecha;

    // Constructor con todos los atributos
    public HistorialMedico(Medico medico, Paciente paciente, String diagnostico, String tratamiento) {
        this.medico = medico;
        this.paciente = paciente;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.fecha = LocalDateTime.now(); // se registra automáticamente la fecha actual
    }

    // Getters y Setters
    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
