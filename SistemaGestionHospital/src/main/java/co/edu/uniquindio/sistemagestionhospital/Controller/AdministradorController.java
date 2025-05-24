// AdministradorController.java
package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.Administrador;
import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;

import java.util.ArrayList;
import java.util.List;

public class AdministradorController {

    private List<Administrador> administradores = new ArrayList<>();
    private List<Paciente> pacientes = new ArrayList<>();
    private List<Medico> medicos = new ArrayList<>();

    public boolean registrarAdministrador(Administrador admin) {
        if (buscarAdministrador(admin.getId()) != null) return false;
        administradores.add(admin);
        return true;
    }

    public Administrador buscarAdministrador(String id) {
        for (Administrador a : administradores) {
            if (a.getId().equals(id)) return a;
        }
        return null;
    }

    public boolean registrarPaciente(Paciente paciente) {
        if (buscarPaciente(paciente.getId()) != null) return false;
        pacientes.add(paciente);
        return true;
    }

    public boolean eliminarPaciente(String id) {
        Paciente p = buscarPaciente(id);
        return p != null && pacientes.remove(p);
    }

    public boolean registrarMedico(Medico medico) {
        if (buscarMedico(medico.getId()) != null) return false;
        medicos.add(medico);
        return true;
    }

    public boolean eliminarMedico(String id) {
        Medico m = buscarMedico(id);
        return m != null && medicos.remove(m);
    }

    public Paciente buscarPaciente(String id) {
        for (Paciente p : pacientes) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    public Medico buscarMedico(String id) {
        for (Medico m : medicos) {
            if (m.getId().equals(id)) return m;
        }
        return null;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public List<Administrador> getAdministradores() {
        return administradores;
    }
}
