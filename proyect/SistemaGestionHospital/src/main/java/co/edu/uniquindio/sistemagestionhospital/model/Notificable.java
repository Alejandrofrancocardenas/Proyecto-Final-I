package co.edu.uniquindio.sistemagestionhospital.model;

import java.io.Serializable;


public interface Notificable extends Serializable {

    void recibirNotificacion(String mensaje);
}
