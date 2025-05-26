package co.edu.uniquindio.sistemagestionhospital.model;

import java.util.ArrayList;
import java.util.List;

public class Hospital {

    private static final Hospital instancia = new Hospital(); // Singleton

    private Paciente paciente;
    private Medico medico;
    private List<Paciente> listaPacientes;
    private List<Medico> listaMedicos;

    public Hospital() {
        this.listaPacientes = new ArrayList<>();
        this.listaMedicos = new ArrayList<>();
    }

    public static Hospital getInstance() {
        return instancia;
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

    public List<Paciente> getListaPacientes() {
        return listaPacientes;
    }

    public List<Medico> getListaMedicos() {
        return listaMedicos;
    }

    public void agregarPaciente(Paciente paciente) {
        listaPacientes.add(paciente);
    }

    public void agregarMedico(Medico medico) {
        listaMedicos.add(medico);
    }

    public boolean registrarPaciente(Paciente paciente) {
        for (Paciente p : listaPacientes) {
            if (p.getId().equals(paciente.getId())) {
                return false; // Ya existe un paciente con ese ID
            }
        }
        listaPacientes.add(paciente);
        return true;
    }

    public boolean eliminarPaciente(String id) {
        return listaPacientes.removeIf(p -> p.getId().equals(id));
    }
}
