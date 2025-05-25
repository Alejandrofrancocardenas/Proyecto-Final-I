package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.*;

import java.util.List;

public class AdministradorController {

    private Administrador administrador;
    private HospitalController hospitalController;

    public AdministradorController(Administrador administrador, HospitalController hospitalController) {
        this.administrador = administrador;
        this.hospitalController = hospitalController;
    }

    public void registrarPaciente(Paciente paciente) {
        administrador.registrarPaciente(hospitalController, paciente);
    }

    public void registrarMedico(Medico medico) {
        administrador.registrarMedico(hospitalController, medico);
    }

    public boolean eliminarPaciente(String correo) {
        return administrador.eliminarPaciente(correo, hospitalController);
    }

    public boolean eliminarMedico(String correo) {
        return administrador.eliminarMedico(correo, hospitalController);
    }

    public boolean registrarSala(Sala sala) {
        return administrador.registrarSala(sala);
    }

    public boolean modificarSala(String id, String nuevoNombre, int nuevaCapacidad) {
        return administrador.modificarSala(id, nuevoNombre, nuevaCapacidad);
    }

    public boolean eliminarSala(String id) {
        return administrador.eliminarSala(id);
    }

    public boolean asignarHorario(Medico medico, String horario) {
        return administrador.asignarHorario(medico, horario);
    }

    public String consultarHorario(Medico medico) {
        return administrador.consultarHorario(medico);
    }

    public List<Cita> verDisponibilidadMedico(Medico medico) {
        return administrador.verDisponibilidadMedico(medico);
    }

    public boolean asignarCita(Cita cita) {
        return administrador.asignarCita(hospitalController, cita);
    }

    public List<String> generarReporteCitas() {
        return administrador.generarReporteCitas(hospitalController);
    }

    public long calcularOcupacion() {
        return administrador.calcularOcupacion(hospitalController);
    }

    public List<Paciente> getPacientes() {
        return hospitalController.getPacientes();
    }

    public List<Medico> getMedicos() {
        return hospitalController.getMedicos();
    }

    public List<Sala> getSalas() {
        return hospitalController.getSalas();
    }

    public List<Cita> getCitas() {
        return hospitalController.getCitas();
    }
}
