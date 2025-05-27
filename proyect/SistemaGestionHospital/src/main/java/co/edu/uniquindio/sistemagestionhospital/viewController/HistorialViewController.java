package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.model.Cita;
import co.edu.uniquindio.sistemagestionhospital.model.EstadoCita;
import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class HistorialViewController {

    @FXML
    private ListView<String> listaHistoriales;

    private Medico medico;

    public void setMedico(Medico medico) {
        this.medico = medico;
        cargarHistoriales();
    }

    private void cargarHistoriales() {
        listaHistoriales.getItems().clear();
        for (Cita cita : medico.getCitas()) {
            if (cita.getEstado().equals(EstadoCita.COMPLETADA)) {
                cita.getHistorialMedicoDelPaciente();
            }
        }
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) listaHistoriales.getScene().getWindow();
        stage.close();
    }
}
