package co.edu.uniquindio.sistemagestionhospital.model;

import java.io.Serializable;

/**
 * Interfaz que representa a un objeto que puede recibir notificaciones.
 */
public interface Notificable extends Serializable {

    /**
     * Método que debe implementar cualquier clase que quiera recibir notificaciones.
     * @param mensaje Contenido de la notificación.
     */
    void recibirNotificacion(String mensaje);
}
