package co.edu.uniquindio.sistemagestionhospital.Controller;
import co.edu.uniquindio.sistemagestionhospital.model.*;

import java.util.LinkedList;

public class HospitalController {
    private LinkedList<Paciente> pacientes;
    private LinkedList<Medico> medicos;
    private LinkedList<Administrador> administradores;
    private LinkedList<Cita> citas;

    public HospitalController() {
        pacientes = new LinkedList<>();
        medicos = new LinkedList<>();
        administradores = new LinkedList<>();
        citas = new LinkedList<>();
    }
    public void registrarPaciente(Paciente paciente) {
        pacientes.add(paciente);
    }
    public void registrarMedico(Medico medico) {
        medicos.add(medico);
    }
    public void registrarAdministrador(Administrador administrador) {
        administradores.add(administrador);
    }
    public LinkedList<Paciente> getPacientes() {
        return pacientes;
    }
    public LinkedList<Medico> getMedicos() {
        return medicos;
    }
    public LinkedList<Administrador> getAdministradores() {
        return administradores;
    }
    public LinkedList<Cita> getCitas() {
        return citas;
    }
    public void cancelarCita(Cita cita){
        citas.add(cita);
        cita.getPaciente().cancelarCita(cita);
        cita.cancelar();
    }

    public Usuario login (String correo, String contrasena) {
        for (Paciente p : pacientes)
            if (p.getCorreo().equals(correo) && p.getContrasena().equals(contrasena)) return p;

        for (Medico m : medicos)
            if (m.getCorreo().equals(correo) && m.getContrasena().equals(contrasena)) return m;

        for (Administrador a : administradores)
            if (a.getCorreo().equals(correo) && a.getContrasena().equals(contrasena)) return a;

        return null;

    }
}
