package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.Cita;
import co.edu.uniquindio.sistemagestionhospital.model.HorarioAtencion;
import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import co.edu.uniquindio.sistemagestionhospital.model.Notificacion; // Para getNotificacionesCompletas
import java.util.Collections;
import java.util.List;


public class MedicoController {

    private Medico medico;


    public MedicoController(Medico medico) {
        this.medico = medico;
        if (this.medico == null) {
            System.err.println("ALERTA en MedicoController(lógica): Creado con un objeto Medico nulo. Las operaciones dependientes del médico fallarán.");
        } else {
            System.out.println("MedicoController (lógica): Instanciado para el médico: " + this.medico.getNombre() +
                    " (ID: " + this.medico.getId() + ", HashCode: " + this.medico.hashCode() + ")");
        }
    }

    public Medico getMedico() {
        return medico;
    }


    public List<Cita> getCitas() {
        if (medico == null) {
            System.err.println("MedicoController: No se puede obtener citas, el médico de este controlador es nulo.");
            return Collections.emptyList();
        }

        return medico.getCitas();
    }

    public boolean agregarEntradaHistorial(Cita cita, String diagnostico, String tratamiento) {
        if (medico == null) {
            System.err.println("MedicoController: No se puede agregar entrada al historial, el médico de este controlador es nulo.");
            return false;
        }
        // Llama al método agregarEntradaHistorial de la instancia de Medico (modelo)
        return medico.agregarEntradaHistorial(cita, diagnostico, tratamiento);
    }

    public boolean agregarHorario(HorarioAtencion horario) {
        if (medico == null) {
            System.err.println("MedicoController: No se puede agregar horario, el médico de este controlador es nulo.");
            return false;
        }
        return medico.agregarHorario(horario);
    }

    public boolean eliminarHorario(String idHorario) { // Acepta String ID
        if (medico == null) {
            System.err.println("MedicoController: No se puede eliminar horario, el médico de este controlador es nulo.");
            return false;
        }
        return medico.eliminarHorario(idHorario);
    }

    public List<HorarioAtencion> getHorarios() {
        if (medico == null) {
            System.err.println("MedicoController: No se puede obtener horarios, el médico de este controlador es nulo.");
            return Collections.emptyList();
        }
        return medico.getHorarios(); // Asume que Medico.java tiene getHorarios()
    }

    public List<String> getNotificaciones() {
        if (medico == null) {
            System.err.println("MedicoController: No se puede obtener notificaciones, el médico de este controlador es nulo.");
            return Collections.emptyList();
        }
        return medico.getNotificaciones(); // Asume que Medico.java tiene getNotificaciones() que devuelve List<String>
    }


    public String getEspecialidad() {
        if (medico == null) {
            System.err.println("MedicoController: No se puede obtener especialidad, el médico de este controlador es nulo.");
            return "Especialidad no disponible";
        }
        return medico.getEspecialidad();
    }
}