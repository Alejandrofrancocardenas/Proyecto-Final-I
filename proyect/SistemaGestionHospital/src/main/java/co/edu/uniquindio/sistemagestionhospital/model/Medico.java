package co.edu.uniquindio.sistemagestionhospital.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Medico extends Usuario implements Notificable {
    private String especialidad;
    private List<HorarioAtencion> horariosAtencion;
    private List<Cita> citasAsignadas;
    private List<Notificacion> notificacionesInternas;

    public Medico(String id, String nombre, String correo, String contrasena, String especialidad) {
        super(nombre, id, correo, contrasena);
        this.especialidad = especialidad;
        this.horariosAtencion = new ArrayList<>();
        this.citasAsignadas = new ArrayList<>();
        this.notificacionesInternas = new ArrayList<>();
        System.out.println(">>> Medico MODELO: Creado '" + getNombre() + "' (ID: " + getId() + ", HashCode: " + this.hashCode() + ")");
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public List<HorarioAtencion> getHorarios() {
        return new ArrayList<>(horariosAtencion);
    }

    public boolean agregarHorario(HorarioAtencion horario) {
        if (horario == null || horario.getDiaSemana() == null || horario.getHoraInicio() == null) return false;
        for (HorarioAtencion ha : horariosAtencion) {
            if (ha.getDiaSemana().equals(horario.getDiaSemana()) && ha.getHoraInicio().equals(horario.getHoraInicio())) {
                return false;
            }
        }
        return horariosAtencion.add(horario);
    }

    public boolean eliminarHorario(String idHorario) {
        if (idHorario == null) return false;
        return horariosAtencion.removeIf(h -> idHorario.equals(h.getId()));
    }

    public List<Cita> getCitas() {
        System.out.println(">>> Medico MODELO: '" + getNombre() + "' (HashCode: " + this.hashCode() + ") - getCitas() llamado. Devolviendo " + this.citasAsignadas.size() + " citas de su lista 'citasAsignadas'.");

        return new ArrayList<>(this.citasAsignadas);
    }

    public boolean agregarCita(Cita cita) {
        if (cita == null || cita.getId() == null) {
            System.err.println("Medico " + getNombre() + ": agregarCita - cita o ID de cita es null");
            return false;
        }
        if (this.citasAsignadas.stream().anyMatch(c -> c.getId().equals(cita.getId()))) {
            System.out.println(">>> Medico MODELO: '" + getNombre() + "' (HashCode: " + this.hashCode() + ") - Cita ID " + cita.getId() + " YA ESTÁ ASIGNADA a este médico.");
            return false;
        }
        boolean agregada = this.citasAsignadas.add(cita);
        if (agregada) {
            System.out.println(">>> Medico MODELO: '" + getNombre() + "' (HashCode: " + this.hashCode() + ") - Cita ID " + cita.getId() + " AGREGADA a sus citasAsignadas. Total ahora: " + this.citasAsignadas.size());
        }
        return agregada;
    }

    public boolean cancelarCita(String idCita) { // Acepta ID
        if (idCita == null || idCita.trim().isEmpty()) return false;
        return this.citasAsignadas.removeIf(c -> idCita.equals(c.getId()));
    }

    public boolean agregarEntradaHistorial(Cita citaParaHistorial, String diagnostico, String tratamiento) {
        if (citaParaHistorial == null || citaParaHistorial.getPaciente() == null || citaParaHistorial.getFecha() == null ||
                diagnostico == null || diagnostico.trim().isEmpty() || tratamiento == null || tratamiento.trim().isEmpty()) {
            System.err.println("Medico " + getNombre() + ": Datos inválidos para agregarEntradaHistorial.");
            return false;
        }
        Paciente paciente = citaParaHistorial.getPaciente();
        LocalDate fechaDeEntrada = citaParaHistorial.getFecha();
        HistorialMedico hm = paciente.getHistorialMedico();
        if (hm == null) {
            System.err.println("Medico " + getNombre() + ": Paciente " + paciente.getNombre() + " sin HistorialMedico inicializado.");
            return false;
        }
        return hm.agregarEntrada(fechaDeEntrada, diagnostico, tratamiento);
    }

    @Override
    public void recibirNotificacion(String mensaje) {
        if (mensaje != null && !mensaje.isEmpty()) {
            this.notificacionesInternas.add(new Notificacion("NOTIF-MED-" + System.currentTimeMillis(), mensaje, LocalDateTime.now()));
        }
    }

    public List<String> getNotificaciones() {
        List<String> mensajes = new ArrayList<>();
        if (this.notificacionesInternas != null) {
            for (Notificacion notif : this.notificacionesInternas) {
                if (notif.getMensaje() != null) mensajes.add(notif.getMensaje());
            }
        }
        return mensajes;
    }

    public List<Notificacion> getNotificacionesCompletas() {
        return new ArrayList<>(notificacionesInternas);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) {
            return false;
        }
        Medico medico = (Medico) o;

        return Objects.equals(getEspecialidad(), medico.getEspecialidad());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEspecialidad());
    }
}