package co.edu.uniquindio.sistemagestionhospital.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Notificacion implements Serializable {
    private String id;
    private String mensaje;
    private LocalDateTime fechaHora;
    private boolean leida;

    public Notificacion(String id, String mensaje, LocalDateTime fechaHora) {
        this.id = id;
        this.mensaje = mensaje;
        this.fechaHora = fechaHora;
        this.leida = false;
    }

    public String getId() {
        return id;
    }

    public String getMensaje() {
        return mensaje;
    }


    public void setLeida(boolean leida) {
        this.leida = leida;
    }


    @Override
    public String toString() {
        return "Notificacion{" +
                "id='" + id + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", fechaHora=" + fechaHora +
                ", leida=" + leida +
                '}';
    }
}