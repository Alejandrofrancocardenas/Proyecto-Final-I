package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.*;

import java.util.ArrayList;

public class PacienteController {

    private ArrayList<Paciente> pacientes;

    public PacienteController() {
        this.pacientes = new ArrayList<>();
    }

    public boolean registrarPaciente(Paciente paciente) {
        if (buscarPaciente(paciente.getId()) == null) {
            pacientes.add(paciente);
            return true;
        }
        return false;
    }

    public Paciente buscarPaciente(String id) {
        for (Paciente p : pacientes) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public boolean actualizarDatosPaciente(String id, String nombre, String correo, String contrasena) {
        Paciente paciente = buscarPaciente(id);
        if (paciente != null) {
            paciente.actualizarDatos(nombre, correo, contrasena);
            return true;
        }
        return false;
    }

    public boolean eliminarPaciente(String id) {
        Paciente paciente = buscarPaciente(id);
        if (paciente != null) {
            pacientes.remove(paciente);
            return true;
        }
        return false;
    }

    public ArrayList<Paciente> getPacientes() {
        return pacientes;
    }
}
