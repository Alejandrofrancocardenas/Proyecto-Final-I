package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class HospitalController {

    private static HospitalController instancia;

    private final ArrayList<Paciente> pacientes;
    private final ArrayList<Medico> medicos;
    private final ArrayList<Cita> citas;
    private final ArrayList<Sala> salas;

    private HospitalController() {
        this.pacientes = new ArrayList<>();
        this.medicos = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.salas = new ArrayList<>();
    }

    public static HospitalController getInstance() {
        if (instancia == null) {
            instancia = new HospitalController();
        }
        return instancia;
    }

    // === PACIENTES ===

    public boolean registrarPaciente(Paciente paciente) {
        if (buscarPacientePorId(paciente.getId()) != null) return false;
        pacientes.add(paciente);
        return true;
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

    public boolean eliminarPaciente(String correo) {
        return pacientes.removeIf(p -> p.getCorreo().equals(correo));
    }

    public Paciente buscarPacientePorId(String id) {
        for (Paciente paciente : pacientes) {
            if (paciente.getId().equals(id)) return paciente;
        }
        return null;
    }

    public ArrayList<Paciente> getPacientes() {
        return pacientes;
    }

    // === MÉDICOS ===

    public boolean registrarMedico(Medico medico) {
        if (buscarMedicoPorId(medico.getId()) != null) return false;
        medicos.add(medico);
        return true;
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

    public boolean eliminarMedico(String correo) {
        return medicos.removeIf(m -> m.getCorreo().equals(correo));
    }

    public Medico buscarMedicoPorId(String idMedico) {
        for (Medico medico : medicos) {
            if (medico.getId().equals(idMedico)) return medico;
        }
        return null;
    }

    public ArrayList<Medico> getMedicos() {
        return medicos;
    }

    // === CITAS ===

    public void asignarCita(Cita cita) {
        citas.add(cita);
        cita.getPaciente().agregarCita(cita);
        cita.getMedico().agregarCita(cita);
    }

    public Cita buscarCitaPorId(String idCita) {
        for (Paciente paciente : pacientes) {
            for (Cita cita : paciente.getCitas()) {
                if (cita.getId().equals(idCita)) return cita;
            }
        }
        return null;
    }

    public ArrayList<Cita> getCitas() {
        return citas;
    }

    public long calcularOcupacion() {
        return citas.stream()
                .filter(cita -> cita.getEstado() == EstadoCita.AGENDADA)
                .count();
    }

    public void generarReporteCitas() {
        System.out.println("===== Reporte de Citas =====");
        for (Cita cita : citas) {
            System.out.println(cita);
        }
    }

    public boolean registrarDiagnostico(String idMedico, String idCita, String diagnostico, String tratamiento) {
        Medico medico = buscarMedicoPorId(idMedico);
        Cita cita = buscarCitaPorId(idCita);
        return medico != null && cita != null &&
                cita.getMedico().getId().equals(idMedico) &&
                medico.registrarDiagnosticoYTratamiento(cita, diagnostico, tratamiento);
    }

    public ArrayList<Cita> verDisponibilidadMedico(Medico medico) {
        return new ArrayList<>(medico.getCitas());
    }

    // === SALAS ===

    public boolean registrarSala(Sala sala) {
        if (buscarSalaPorId(sala.getId()) != null) return false;
        salas.add(sala);
        return true;
    }

    public boolean modificarSala(String id, String nuevoNombre, int nuevaCapacidad) {
        Sala sala = buscarSalaPorId(id);
        if (sala == null) return false;
        sala.setNombre(nuevoNombre);
        sala.setCapacidad(nuevaCapacidad);
        return true;
    }

    public boolean eliminarSala(String id) {
        Sala sala = buscarSalaPorId(id);
        return sala != null && salas.remove(sala);
    }

    public Sala buscarSalaPorId(String id) {
        for (Sala sala : salas) {
            if (sala.getId().equals(id)) return sala;
        }
        return null;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    // === HORARIOS ===

    public boolean asignarHorarioAMedico(String idMedico, HorarioAtencion horario) {
        Medico medico = buscarMedicoPorId(idMedico);
        return medico != null && medico.agregarHorario(horario);
    }

    public boolean asignarSalaAHorario(String idMedico, String dia, String horaInicio, Sala sala) {
        Medico medico = buscarMedicoPorId(idMedico);
        if (medico != null) {
            DayOfWeek diaSemana = DayOfWeek.valueOf(dia.toUpperCase());

            for (HorarioAtencion horario : medico.getHorarios()) {
                if (horario.getDia().equals(diaSemana) && horario.getHoraInicio().equals(horaInicio)) {
                    for (Medico otroMedico : medicos) {
                        for (HorarioAtencion otroHorario : otroMedico.getHorarios()) {
                            if (otroHorario.getDia().equals(diaSemana) &&
                                    otroHorario.getHoraInicio().equals(horaInicio) &&
                                    sala.equals(otroHorario.getSala())) {
                                medico.recibirNotificacion("No se pudo asignar la sala '" + sala.getNombre()
                                        + "' el " + dia + " a las " + horaInicio + " porque ya está ocupada.");
                                return false;
                            }
                        }
                    }

                    horario.setSala(List.of(sala));  // Asigna sala
                    medico.recibirNotificacion("Se te ha asignado la sala '" + sala.getNombre()
                            + "' el " + dia + " a las " + horaInicio + ".");
                    return true;
                }
            }
        }
        return false;
    }

    public List<HorarioAtencion> obtenerHorariosDeMedico(String idMedico) {
        Medico medico = buscarMedicoPorId(idMedico);
        return (medico != null) ? medico.getHorarios() : new ArrayList<>();
    }

    public boolean eliminarHorarioDeMedico(String idMedico, HorarioAtencion horario) {
        Medico medico = buscarMedicoPorId(idMedico);
        return medico != null && medico.eliminarHorario(horario);
    }
}
