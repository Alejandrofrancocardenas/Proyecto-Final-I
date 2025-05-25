package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.*;

import java.util.ArrayList;

public class HospitalController {

    private final ArrayList<Paciente> pacientes;
    private final ArrayList<Medico> medicos;
    private final ArrayList<Cita> citas;

    public HospitalController() {
        this.pacientes = new ArrayList<>();
        this.medicos = new ArrayList<>();
        this.citas = new ArrayList<>();
    }

    public void registrarPaciente(Paciente paciente) {
        pacientes.add(paciente);
    }

    public void registrarMedico(Medico medico) {
        medicos.add(medico);
    }

    public boolean eliminarPaciente(String correo) {
        return pacientes.removeIf(p -> p.getCorreo().equals(correo));
    }

    public boolean eliminarMedico(String correo) {
        return medicos.removeIf(m -> m.getCorreo().equals(correo));
    }

    public ArrayList<Cita> verDisponibilidadMedico(Medico medico) {
        return new ArrayList<>(medico.getCitas());
    }

    public void asignarCita(Cita cita) {
        citas.add(cita);
        cita.getPaciente().agregarCita(cita);
        cita.getMedico().agregarCita(cita);
    }
    public boolean modificarMedico(String id, String nuevoNombre, String nuevoCorreo, String nuevaContrasena, String nuevaEspecialidad) {
        Medico medico = buscarMedicoPorId(id);
        if (medico != null) {
            medico.setNombre(nuevoNombre);
            medico.setCorreo(nuevoCorreo);
            medico.setContrasena(nuevaContrasena);
            medico.setEspecialidad(nuevaEspecialidad);
            return true;
        }
        return false;
    }

    public boolean modificarPaciente(String id, String nuevoNombre, String nuevoCorreo, String nuevaContrasena, String nuevaCedula) {
        Paciente paciente = buscarPacientePorId(id);
        if (paciente != null) {
            paciente.setNombre(nuevoNombre);
            paciente.setCorreo(nuevoCorreo);
            paciente.setContrasena(nuevaContrasena);
            paciente.setCedula(nuevaCedula);
            return true;
        }
        return false;
    }

    public Paciente buscarPacientePorId(String id) {
        for (Paciente paciente : pacientes) {
            if (paciente.getId().equals(id)) {
                return paciente;
            }
        }
        return null;
    }

    public void generarReporteCitas() {
        System.out.println("===== Reporte de Citas =====");
        for (Cita cita : citas) {
            System.out.println(cita);
        }
    }

    public long calcularOcupacion() {
        return citas.stream()
                .filter(cita -> cita.getEstado() == EstadoCita.AGENDADA)
                .count();
    }
    public Cita buscarCitaPorId(String idCita) {
        for (Paciente paciente : pacientes) {
            for (Cita cita : paciente.getCitas()) {
                if (cita.getId().equals(idCita)) {
                    return cita;
                }
            }
        }
        return null;
    }
    public Medico buscarMedicoPorId(String idMedico) {
        for (Medico medico : medicos) {
            if (medico.getId().equals(idMedico)) {
                return medico;
            }
        }
        return null; // No se encontró ningún médico con ese ID
    }


    public boolean registrarDiagnostico(String idMedico, String idCita, String diagnostico, String tratamiento) {
        Medico medico = buscarMedicoPorId(idMedico);
        if (medico == null) {
            return false; // Médico no encontrado
        }

        Cita cita = buscarCitaPorId(idCita);
        if (cita == null || !cita.getMedico().getId().equals(idMedico)) {
            return false; // La cita no existe o no le pertenece al médico
        }

        return medico.registrarDiagnosticoYTratamiento(cita, diagnostico, tratamiento);
    }


    public ArrayList<Paciente> getPacientes() {
        return pacientes;
    }

    public ArrayList<Medico> getMedicos() {
        return medicos;
    }

    public ArrayList<Cita> getCitas() {
        return citas;
    }
}
