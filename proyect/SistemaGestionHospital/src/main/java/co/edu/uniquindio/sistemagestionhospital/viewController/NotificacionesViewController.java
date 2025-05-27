package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class NotificacionesViewController {

    @FXML
    private ListView<String> listaNotificaciones;

    private Medico medico;

    public void setMedico(Medico medico) {
        this.medico = medico;
        listaNotificaciones.getItems().addAll(medico.getNotificaciones());
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) listaNotificaciones.getScene().getWindow();
        stage.close();
    }
}
