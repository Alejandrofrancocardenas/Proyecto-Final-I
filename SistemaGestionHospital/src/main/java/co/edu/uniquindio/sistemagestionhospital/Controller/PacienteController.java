package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.Cita;
import co.edu.uniquindio.sistemagestionhospital.model.HistorialMedico;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;

import java.util.ArrayList;
import java.util.List;

public class PacienteController {

    private List<Paciente> listaPacientes = new ArrayList<>();

    public boolean registrarPaciente(Paciente paciente) {
        if (buscarPaciente(paciente.getId()) != null) {
            return false;
        }
        listaPacientes.add(paciente);
        return true;
    }

    public Paciente buscarPaciente(String id) {
        for (Paciente p : listaPacientes) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public boolean actualizarDatos(String id, String nuevoNombre, String nuevoCorreo, String nuevaContrasena) {
        Paciente paciente = buscarPaciente(id);
        if (paciente == null) return false;
        paciente.actualizarDatos(nuevoNombre, nuevoCorreo, nuevaContrasena);
        return true;
    }

    public boolean solicitarCita(String idPaciente, Cita cita) {
        Paciente paciente = buscarPaciente(idPaciente);
        if (paciente == null) return false;
        paciente.solicitarCita(cita);
        return true;
    }

    public boolean agregarCita(String idPaciente, Cita cita) {
        Paciente paciente = buscarPaciente(idPaciente);
        return paciente != null && paciente.agregarCita(cita);
    }

    public boolean cancelarCita(String idPaciente, Cita cita) {
        Paciente paciente = buscarPaciente(idPaciente);
        return paciente != null && paciente.cancelarCita(cita);
    }

    public List<HistorialMedico> consultarHistorial(String idPaciente) {
        Paciente paciente = buscarPaciente(idPaciente);
        return paciente != null ? paciente.consultarHistorialMedico() : new ArrayList<>();
    }

    public List<String> obtenerNotificaciones(String idPaciente) {
        Paciente paciente = buscarPaciente(idPaciente);
        return paciente != null ? paciente.getNotificaciones() : new ArrayList<>();
    }

    public List<Paciente> getListaPacientes() {
        return listaPacientes;
    }
}
