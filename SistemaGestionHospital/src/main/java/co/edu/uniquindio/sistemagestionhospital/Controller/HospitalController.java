package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.*;

import java.util.ArrayList;

public class HospitalController {

    private final ArrayList<Paciente> pacientes;
    private final ArrayList<Medico> medicos;
    private final ArrayList<Cita> citas;

    public HospitalController() {
        this.pacientes = new ArrayList<>();
        this.medicos = new ArrayList<>();
        this.citas = new ArrayList<>();
    }

    public void registrarPaciente(Paciente paciente) {
        pacientes.add(paciente);
    }

    public void registrarMedico(Medico medico) {
        medicos.add(medico);
    }

    public boolean eliminarPaciente(String correo) {
        return pacientes.removeIf(p -> p.getCorreo().equals(correo));
    }

    public boolean eliminarMedico(String correo) {
        return medicos.removeIf(m -> m.getCorreo().equals(correo));
    }

    public ArrayList<Cita> verDisponibilidadMedico(Medico medico) {
        return new ArrayList<>(medico.getCitas());
    }

    public void asignarCita(Cita cita) {
        citas.add(cita);
        cita.getPaciente().agregarCita(cita);
        cita.getMedico().agregarCita(cita);
    }

    public void generarReporteCitas() {
        System.out.println("===== Reporte de Citas =====");
        for (Cita cita : citas) {
            System.out.println(cita);
        }
    }

    public long calcularOcupacion() {
        return citas.stream()
                .filter(cita -> cita.getEstado() == EstadoCita.AGENDADA)
                .count();
    }

    public ArrayList<Paciente> getPacientes() {
        return pacientes;
    }

    public ArrayList<Medico> getMedicos() {
        return medicos;
    }

    public ArrayList<Cita> getCitas() {
        return citas;
    }
}
