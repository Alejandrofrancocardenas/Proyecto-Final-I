package co.edu.uniquindio.sistemagestionhospital.Controller;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;


public class PacienteController {

    private final HospitalController hospitalController;

    public PacienteController() {

        this.hospitalController = HospitalController.getInstance();
    }

    public boolean registrarPaciente(Paciente paciente) {
        if (paciente == null) {
            System.err.println("Error en PacienteController: El objeto Paciente a registrar es nulo.");
            return false;
        }

        Paciente pacienteRegistrado = hospitalController.registrarPaciente(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getCorreo(),
                paciente.getContrasena(),
                paciente.getCedula()
        );
        return pacienteRegistrado != null;
    }

    public boolean actualizarDatos(String id, String nuevoNombre, String nuevoCorreo, String nuevaContrasena, String nuevaCedula) {
        return hospitalController.modificarPaciente(id, nuevoNombre, nuevoCorreo, nuevaCedula, nuevaContrasena);
    }

    public Paciente obtenerPacientePorCorreo(String correo) {

        for (Paciente p : hospitalController.getPacientes()) {
            if (p.getCorreo().equals(correo)) {
                return p;
            }
        }
        return null;
    }
}