package co.edu.uniquindio.sistemagestionhospital.model;
import co.edu.uniquindio.sistemagestionhospital.Controller.HospitalController;
import java.util.LinkedList;

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
    public void eliminarPaciente(HospitalController controller, Paciente paciente) {
        controller.getPacientes().remove(paciente);
    }


    public void eliminarMedico(HospitalController controller, Medico medico) {
        controller.getMedicos().remove(medico);
    }

    public LinkedList<Cita> verDisponibilidadMedico(Medico medico) {
        return medico.getCitas();
    }


    public void asignarCita(HospitalController controller, Cita cita) {
        controller.getCitas().add(cita);
        cita.getPaciente().agregarCita(cita);
        cita.getMedico().agregarCita(cita);
    }


    public void generarReporteCitas(HospitalController controller) {
        System.out.println("----- Reporte de Citas -----");
        for (Cita cita : controller.getCitas()) {
            System.out.println(cita);
        }
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
