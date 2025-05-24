package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.Cita;
import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;

public class HospitalController {

    private LinkedList<Paciente> pacientes;
    private LinkedList<Medico> medicos;
    private LinkedList<Cita> citas;

    public HospitalController() {
        pacientes = new LinkedList<>();
        medicos = new LinkedList<>();
        citas = new LinkedList<>();
    }


    public void registrarPaciente(Paciente paciente) {
        pacientes.add(paciente);
    }

    public void registrarMedico(Medico medico) {
        medicos.add(medico);
    }

    public void registrarCita(Cita cita) {
        citas.add(cita);
        cita.getPaciente().agregarCita(cita);
        cita.getMedico().agregarCita(cita);
    }

    public Paciente buscarPacientePorCorreo(String correo) {
        for (Paciente p : pacientes) {
            if (p.getCorreo().equalsIgnoreCase(correo)) {
                return p;
            }
        }
        return null;
    }

    public Medico buscarMedicoPorCorreo(String correo) {
        for (Medico m : medicos) {
            if (m.getCorreo().equalsIgnoreCase(correo)) {
                return m;
            }
        }
        return null;
    }

    public boolean agendarCita(Paciente paciente, Medico medico, LocalDate fecha, LocalTime hora) {
        for (Cita c : citas) {
            if (c.getMedico().equals(medico) && c.getFecha().equals(fecha)) {
                return false;
            }

        }

        Cita nuevaCita = new Cita(paciente, medico, fecha,hora);
        citas.add(nuevaCita);
        paciente.getCitas().add(nuevaCita);
        medico.getCitas().add(nuevaCita);

        paciente.recibirNotificacion("Se ha agendado su cita con el Dr. " + paciente.getNombre() + " para el " + fecha.toString());
        medico.recibirNotificacion("Nueva cita agendada con el paciente " + medico.getNombre() + " para el " + fecha);

        return true;
    }




        public LinkedList<Paciente> getPacientes () {
            return pacientes;
        }

        public LinkedList<Medico> getMedicos () {
            return medicos;
        }

        public LinkedList<Cita> getCitas () {
            return citas;
        }
    }
