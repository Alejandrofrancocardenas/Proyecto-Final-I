package co.edu.uniquindio.sistemagestionhospital.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public class Paciente extends Usuario implements Notificable {
    private String cedula;
    private HistorialMedico historialMedico;
    private List<Notificacion> notificaciones;
    private List<Cita> citasProgramadas;
    private List<Notificacion> notificacionesInternas;
    public Paciente(String id, String nombre, String correo, String contrasena, String cedula) {
        super(nombre, id, correo, contrasena);
        this.cedula = cedula;
        this.historialMedico = new HistorialMedico();
        this.notificaciones = new ArrayList<>();
        this.citasProgramadas = new ArrayList<>();
        this.notificacionesInternas = new ArrayList<>();
    }

    public String getCedula() {
        return cedula;
    }
public void setHistorialMedico(HistorialMedico historialMedico) {
        this.historialMedico = historialMedico;
}
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public HistorialMedico getHistorialMedico() {
        return historialMedico;
    }


    public List<EntradaHistorial> getEntradasDelHistorial() {
        if (this.historialMedico != null) {
            return this.historialMedico.getEntradas();
        }
        return new ArrayList<>();
    }

    public List<Cita> getCitasProgramadas() {
        return citasProgramadas;
    }

    @Override
    public void recibirNotificacion(String mensaje) {
        if (mensaje != null && !mensaje.isEmpty()) {
            Notificacion nuevaNotificacion = new Notificacion("NOTIF-" + System.currentTimeMillis(), mensaje, LocalDateTime.now());
            this.notificaciones.add(nuevaNotificacion);
            System.out.println("Paciente " + getNombre() + " recibió notificación: " + mensaje);
        }
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public Cita solicitarCita(String motivo, LocalDate fecha, LocalTime hora, String especialidad, Medico medico,EstadoCita estado) {
        if (motivo == null || motivo.trim().isEmpty() || fecha == null || hora == null || especialidad == null || especialidad.trim().isEmpty() || medico == null) {
            System.out.println("Error: Datos incompletos para solicitar la cita.");
            return null;
        }

        String idCita = "CITA-" + System.currentTimeMillis();


        Cita nuevaCita = new Cita(idCita, fecha, hora, motivo, this ,medico,especialidad, estado);

        this.agregarCita(nuevaCita);
        System.out.println("Cita solicitada por " + getNombre() + " (ID Cita: " + nuevaCita.getId() + ") para " + especialidad +
                " con " + medico.getNombre() + " el " + fecha + " a las " + hora);
        return nuevaCita;
    }

    public boolean agregarCita(Cita nuevaCita) { // Cambiado a devolver boolean
        if (nuevaCita == null) {
            System.out.println("Intento de agregar una cita nula.");
            return false; // No se agregó
        }
        if (this.citasProgramadas.contains(nuevaCita)) { // Asume que Cita.equals() está bien implementado
            System.out.println("La cita (ID: " + nuevaCita.getId() + ") ya existe en la lista del paciente.");
            return false; // Ya existe, no se agregó de nuevo (o se podría considerar éxito si ya estaba)
        }

        boolean agregada = this.citasProgramadas.add(nuevaCita);
        if (agregada) {
            // Lógica de notificación
            if (nuevaCita.getFecha() != null && nuevaCita.getHora() != null && nuevaCita.getMedico() != null && nuevaCita.getMedico().getNombre() != null) {
                recibirNotificacion("Se ha programado una nueva cita para el " + nuevaCita.getFecha() +
                        " a las " + nuevaCita.getHora() +
                        " con el Dr./Dra. " + nuevaCita.getMedico().getNombre() + ".");
            } else {
                recibirNotificacion("Se ha programado una nueva cita (detalles incompletos en la notificación).");
            }
        }
        return agregada; // Devuelve el resultado de la operación add
    }

    public boolean cancelarCita(String idCita) {
        if (idCita == null || idCita.trim().isEmpty()) {
            System.err.println("Paciente " + getNombre() + ": ID de cita inválido para cancelar.");
            return false;
        }

        boolean removida = this.citasProgramadas.removeIf(c -> idCita.equals(c.getId()));
        if (removida) {
            System.out.println("Paciente " + getNombre() + ": Cita ID " + idCita + " cancelada/removida de su lista.");
        } else {
            System.out.println("Paciente " + getNombre() + ": Cita ID " + idCita + " no encontrada en su lista para cancelar.");
        }
        return removida;
    }



    public List<String> getMensajesDeNotificaciones() {
        List<String> mensajes = new ArrayList<>();
        if (this.notificaciones != null) {
            for (Notificacion notif : this.notificaciones) {
                // Asumiendo que Notificacion tiene getMensaje()
                if (notif.getMensaje() != null) {
                    mensajes.add(notif.getMensaje());
                }
            }
        }
        return mensajes;
    }

    public List<Notificacion> getNotificacionesCompletas() {
        return new ArrayList<>(this.notificacionesInternas);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Paciente paciente = (Paciente) o;
        return Objects.equals(cedula, paciente.cedula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cedula);
    }
}