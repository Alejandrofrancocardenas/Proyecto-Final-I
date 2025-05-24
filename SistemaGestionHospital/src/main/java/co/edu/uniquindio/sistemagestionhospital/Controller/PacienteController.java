// PacienteController.java
package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.Cita;
import co.edu.uniquindio.sistemagestionhospital.model.HistorialMedico;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;

import java.util.ArrayList;
import java.util.List;

public class PacienteController {

    private List<Paciente> listaPacientes = new ArrayList<>();

    public boolean registrarPaciente(Paciente paciente) {
        if (buscarPaciente(paciente.getId()) != null) return false;
        listaPacientes.add(paciente);
        return true;
    }

    public Paciente buscarPaciente(String id) {
        for (Paciente p : listaPacientes) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    public boolean actualizarDatos(String id, String nuevoNombre, String nuevoCorreo, String nuevaContrasena) {
        Paciente p = buscarPaciente(id);
        if (p == null) return false;
        p.actualizarDatos(nuevoNombre, nuevoCorreo, nuevaContrasena);
        return true;
    }

    public boolean solicitarCita(String idPaciente, Cita cita) {
        Paciente p = buscarPaciente(idPaciente);
        if (p == null) return false;
        p.solicitarCita(cita);
        return true;
    }

    public boolean cancelarCita(String idPaciente, Cita cita) {
        Paciente p = buscarPaciente(idPaciente);
        return p != null && p.cancelarCita(cita);
    }

    public List<HistorialMedico> consultarHistorial(String idPaciente) {
        Paciente p = buscarPaciente(idPaciente);
        return p != null ? p.consultarHistorialMedico() : new ArrayList<>();
    }

    public List<Paciente> getListaPacientes() {
        return listaPacientes;
    }
}
