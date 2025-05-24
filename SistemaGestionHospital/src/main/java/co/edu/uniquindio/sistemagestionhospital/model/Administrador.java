package co.edu.uniquindio.sistemagestionhospital.model;
import co.edu.uniquindio.sistemagestionhospital.Controller.HospitalController;

import java.util.ArrayList;
import java.util.List;

public class Administrador extends Usuario {

    public Administrador(String id,String nombre, String correo, String contrasena) {
        super(nombre, correo, id,contrasena);
    }

    public void registrarPaciente(HospitalController controller, Paciente paciente) {
        controller.registrarPaciente(paciente);
    }


    public void registrarMedico(HospitalController controller, Medico medico) {
        controller.registrarMedico(medico);
    }
    public boolean eliminarPaciente(String correo, HospitalController controller) {
        for (Paciente p : controller.getPacientes()) {
            if (p.getCorreo().equals(correo)) {
                controller.getPacientes().remove(p);
                return true;
            }
        }
        return false;
    }
    public boolean eliminarMedico(String correo, HospitalController controller) {
        for (Medico m : controller.getMedicos()) {
            if (m.getCorreo().equals(correo)) {
                controller.getMedicos().remove(m);
                return true;
            }
        }
        return false;
    }

    public List<Cita> verDisponibilidadMedico(Medico medico) {
        return medico.getCitas();
    }


    public boolean asignarCita(HospitalController controller, Cita cita) {
        if (controller.getCitas().contains(cita)) return false;

        controller.getCitas().add(cita);
        cita.getPaciente().agregarCita(cita);
        cita.getMedico().agregarCita(cita);
        return true;
    }



    public List<String> generarReporteCitas(HospitalController controller) {
        List<String> reporte = new ArrayList<>();
        for (Cita cita : controller.getCitas()) {
            reporte.add(cita.toString());
        }
        return reporte;
    }


    public long calcularOcupacion(HospitalController controller) {
        return controller.getCitas().stream()
                .filter(cita -> cita.getEstado() == EstadoCita.AGENDADA)
                .count();
    }

    @Override
    public void mostrarMenu() {

    }
}
