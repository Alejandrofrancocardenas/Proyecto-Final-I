package co.edu.uniquindio.sistemagestionhospital.model;

public class HistorialMedico {

    private String diagnostico;
    private String tratamiento;
    private Medico medico;
    private Paciente paciente;

    public HistorialMedico(Medico medico, Paciente paciente, String diagnostico, String tratamiento) {
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.medico = medico;
        this.paciente = paciente;
    }
    public Paciente getPaciente(){
        return paciente;
    }
    public void setPaciente(Paciente paciente){
        this.paciente = paciente;
    }
    public Medico getMedico(){
        return medico;
    }
    public void setMedico(Medico medico){
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
}
