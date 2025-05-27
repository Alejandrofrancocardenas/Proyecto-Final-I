package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.*; // Asegúrate que todos tus modelos están aquí

import java.time.LocalDate;
import java.time.LocalTime; // Añadido por si Cita lo necesita para ordenar
import java.util.ArrayList;
import java.util.Comparator; // Añadido para ordenar citas
import java.util.List;
import java.util.stream.Collectors;

public class HospitalController {

    private static HospitalController instancia;
    private final Hospital hospital;

    private HospitalController() {
        this.hospital = Hospital.getInstance();
        System.out.println(">>> HospitalController Singleton INSTANCE CREADA - HashCode: " + this.hashCode());
    }

    public static synchronized HospitalController getInstance() {
        if (instancia == null) {
            instancia = new HospitalController();
        }
        return instancia;
    }

    public Paciente registrarPaciente(String id, String n, String c, String pass, String ced) {
        Paciente nuevoPaciente = new Paciente(id, n, c, pass, ced);
        return hospital.agregarPaciente(nuevoPaciente) ? nuevoPaciente : null;
    }
    public Paciente buscarPacientePorId(String id) { return hospital.obtenerPaciente(id); }
    public Paciente buscarPacientePorCedula(String cedula) { return hospital.obtenerPacientePorCedula(cedula); }
    public boolean modificarPaciente(String id, String nN, String nC, String nCed, String nPass) { return hospital.modificarPaciente(id, nN, nC, nCed, nPass); }
    public boolean eliminarPaciente(String id) { return hospital.eliminarPaciente(id); }
    public List<Paciente> getPacientes() { return hospital.getListaPacientes(); }


    public Medico registrarMedico(String id, String nombre, String correo, String contrasena, String especialidad) {
        System.out.println(">>> HospitalController: registrarMedico para ID: " + id + ", Nombre: " + nombre);
        Medico nuevoMedico = new Medico(id, nombre, correo, contrasena, especialidad);
        if (this.hospital.agregarMedico(nuevoMedico)) {
            System.out.println("HospitalController: Médico '" + nombre + "' (HashCode: " + nuevoMedico.hashCode() + ") registrado y devuelto.");
            return nuevoMedico;
        }
        System.err.println("HospitalController: Falló agregarMedico en el modelo Hospital para '" + nombre + "'. Ya podría existir o hubo otro error.");
        return null;
    }
    public Medico buscarMedicoPorId(String id) {
        System.out.println(">>> HospitalController: buscarMedicoPorId(" + id + ")");
        return hospital.obtenerMedico(id);
    }
    public boolean modificarMedico(String id, String nNombre, String nCorreo, String nContrasena, String nEspecialidad) {
        return hospital.modificarMedico(id, nNombre, nCorreo, nContrasena, nEspecialidad);
    }
    public boolean eliminarMedico(String id) { return hospital.eliminarMedico(id); }
    public List<Medico> getMedicos() {
        List<Medico> medicos = hospital.getListaMedicos();
        System.out.println(">>> HospitalController: getMedicos() - Obtenidos " + (medicos != null ? medicos.size() : "null") + " médicos del modelo Hospital.");
        return medicos;
    }


    public boolean asignarHorarioAMedico(String medicoId, HorarioAtencion horario) {
        Medico medico = hospital.obtenerMedico(medicoId);
        return (medico != null) && medico.agregarHorario(horario);
    }
    public List<HorarioAtencion> obtenerHorariosDeMedico(String medicoId) {
        Medico medico = hospital.obtenerMedico(medicoId);
        return (medico != null) ? medico.getHorarios() : new ArrayList<>();
    }
    public boolean eliminarHorarioDeMedico(String medicoId, String idHorario) {
        Medico medico = hospital.obtenerMedico(medicoId);
        return (medico != null) && medico.eliminarHorario(idHorario);
    }


    public boolean asignarCita(Cita citaCompleta) {
        if (citaCompleta == null || citaCompleta.getPaciente() == null || citaCompleta.getMedico() == null) {
            System.err.println("HospitalController: asignarCita - Datos de cita incompletos (paciente o médico nulos)."); return false;
        }

        boolean agregadaAlHospital = this.hospital.agregarCita(citaCompleta);
        if (!agregadaAlHospital) {
            System.err.println("HospitalController: No se pudo agregar cita ID " + citaCompleta.getId() + " al registro central (posible ID duplicado).");
            return false;
        }
        System.out.println("HospitalController: Cita ID " + citaCompleta.getId() + " agregada al registro central.");

        Medico medicoDeLaCitaEnModelo = this.hospital.obtenerMedico(citaCompleta.getMedico().getId());
        if (medicoDeLaCitaEnModelo == null) {
            System.err.println("HospitalController.asignarCita: MÉDICO ASOCIADO A LA CITA (ID: "+citaCompleta.getMedico().getId()+") NO ENCONTRADO EN EL MODELO HOSPITAL.");
            return false;
        }
        medicoDeLaCitaEnModelo.agregarCita(citaCompleta);

        Paciente pacienteDeLaCitaEnModelo = this.hospital.obtenerPaciente(citaCompleta.getPaciente().getId());
        if(pacienteDeLaCitaEnModelo != null) {
            pacienteDeLaCitaEnModelo.agregarCita(citaCompleta);
        } else {
            System.err.println("HospitalController.asignarCita: PACIENTE ASOCIADO A LA CITA (ID: "+citaCompleta.getPaciente().getId()+") NO ENCONTRADO EN EL MODELO HOSPITAL.");
        }

        enviarNotificacion(citaCompleta.getPaciente(), "Su cita con Dr. " + medicoDeLaCitaEnModelo.getNombre() + " ha sido agendada para " + citaCompleta.getFecha());
        enviarNotificacion(medicoDeLaCitaEnModelo, "Nueva cita agendada con " + (pacienteDeLaCitaEnModelo != null ? pacienteDeLaCitaEnModelo.getNombre() : "N/A") + " para " + citaCompleta.getFecha());
        return true;
    }

    public boolean cancelarCitaYNotificar(String idCita) {
        Cita cita = hospital.obtenerCita(idCita);
        if (cita != null && (EstadoCita.AGENDADA.equals(cita.getEstado()) || EstadoCita.AGENDADA.equals(cita.getEstado()))) {
            cita.setEstado(EstadoCita.CANCELADA);

            Paciente p = (cita.getPaciente() != null) ? hospital.obtenerPaciente(cita.getPaciente().getId()) : null;
            Medico m = (cita.getMedico() != null) ? hospital.obtenerMedico(cita.getMedico().getId()) : null;

            if(p != null) p.cancelarCita(idCita);
            if(m != null) m.cancelarCita(idCita);

            if (p != null) enviarNotificacion(p, "Su cita del " + cita.getFecha() + " ha sido CANCELADA.");
            if (m != null) enviarNotificacion(m, "Cita con " + (p!=null? p.getNombre():"N/A") + " para " + cita.getFecha() + " ha sido CANCELADA.");
            System.out.println("HospitalController: Cita ID " + idCita + " cancelada y notificada.");
            return true;
        }
        System.err.println("HospitalController: No se pudo cancelar cita ID " + idCita + " (no encontrada o estado no válido: " + (cita != null ? cita.getEstado() : "Cita es null") + ").");
        return false;
    }

    public List<Cita> getCitas() { return hospital.getCitas(); }

    public List<Cita> obtenerTodasLasCitasParaReporte() {
        List<Cita> todasLasCitas = hospital.getCitas();
        if (todasLasCitas != null) {
            System.out.println("HospitalController: Obtenidas " + todasLasCitas.size() + " citas para el reporte.");
            todasLasCitas.sort(Comparator.comparing(Cita::getFecha, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Cita::getHora, Comparator.nullsLast(Comparator.naturalOrder())));
            return todasLasCitas;
        }
        return new ArrayList<>();
    }

    public boolean actualizarEstadoCita(String idCita, EstadoCita nuevoEstado) {
        Cita cita = this.hospital.obtenerCita(idCita);
        if(cita != null) {
            cita.setEstado(nuevoEstado);
            System.out.println("HospitalController: Estado de cita ID " + idCita + " actualizado a " + nuevoEstado);
            return true;
        }
        System.err.println("HospitalController: No se encontró cita ID " + idCita + " para actualizar estado.");
        return false;
    }
    public boolean registrarEntradaHistorialMedico(String pacienteId, LocalDate fecha, String diagnostico, String tratamiento) {
        if (pacienteId == null || fecha == null || diagnostico == null || diagnostico.trim().isEmpty() || tratamiento == null || tratamiento.trim().isEmpty()) {
            System.err.println("HospitalController: Datos inválidos para registrar entrada en historial médico.");
            return false;
        }

        Paciente paciente = hospital.obtenerPaciente(pacienteId); // Usa el método de Hospital para buscar al paciente

        if (paciente == null) {
            System.err.println("HospitalController: No se encontró paciente con ID '" + pacienteId + "' para registrar historial.");
            return false;
        }

        HistorialMedico historialDelPaciente = paciente.getHistorialMedico();
        if (historialDelPaciente == null) {
            // Esto no debería pasar si el constructor de Paciente siempre inicializa un HistorialMedico.
            // Pero si puede ser null, podrías querer inicializarlo aquí:
            // System.err.println("HospitalController: Paciente '" + paciente.getNombre() + "' no tiene un HistorialMedico. Creando uno nuevo.");
            // historialDelPaciente = new HistorialMedico();
            // paciente.setHistorialMedico(historialDelPaciente); // Paciente necesitaría un setter
            // O simplemente fallar:
            System.err.println("HospitalController: Paciente '" + paciente.getNombre() + "' no tiene un objeto HistorialMedico inicializado.");
            return false;
        }

        // Delega la adición de la entrada al objeto HistorialMedico del paciente
        // Asumiendo que HistorialMedico.agregarEntrada(LocalDate, String, String) existe y devuelve boolean
        boolean exito = historialDelPaciente.agregarEntrada(fecha, diagnostico, tratamiento);
        if (exito) {
            System.out.println("HospitalController: Entrada de historial registrada para paciente ID " + pacienteId + " en fecha " + fecha);
        } else {
            System.err.println("HospitalController: Falló la adición de la entrada al objeto HistorialMedico para paciente ID " + pacienteId);
        }
        return exito;
    }

    public boolean registrarSala(String id, String n, int cap) { return hospital.agregarSala(new Sala(id, n, cap)); }
    public Sala buscarSalaPorId(String id) { return hospital.obtenerSala(id); }
    public boolean modificarSala(String id, String nN, int nCap) { return hospital.modificarSala(id, nN, nCap); }
    public boolean eliminarSala(String id) { return hospital.eliminarSala(id); }
    public List<Sala> getSalas() { return hospital.getListaSalas(); }


    public void enviarNotificacion(Notificable receptor, String mensaje) {
        if (receptor != null && mensaje != null && !mensaje.isEmpty()) receptor.recibirNotificacion(mensaje);
    }

}