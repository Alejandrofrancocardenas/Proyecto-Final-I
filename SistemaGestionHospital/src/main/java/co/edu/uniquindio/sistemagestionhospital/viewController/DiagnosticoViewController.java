package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.model.Cita;
import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DiagnosticoViewController {

    @FXML
    private ListView<Cita> listaCitas;

    @FXML
    private TextArea txtDiagnostico, txtTratamiento;

    private Medico medico;

    public void setMedico(Medico medico) {
        this.medico = medico;
        listaCitas.getItems().setAll(medico.getCitas());
    }

    @FXML
    private void guardarDiagnosticoTratamiento() {
        Cita citaSeleccionada = listaCitas.getSelectionModel().getSelectedItem();
        String diagnostico = txtDiagnostico.getText();
        String tratamiento = txtTratamiento.getText();

        if (citaSeleccionada == null || diagnostico.isBlank() || tratamiento.isBlank()) {
            mostrarMensaje("Completa todos los campos y selecciona una cita.", true);
            return;
        }

        boolean exito = medico.registrarDiagnosticoYTratamiento(citaSeleccionada, diagnostico, tratamiento);

        if (exito) {
            mostrarMensaje("Diagnóstico y tratamiento registrados correctamente.", false);
            txtDiagnostico.clear();
            txtTratamiento.clear();
        } else {
            mostrarMensaje("No se pudo registrar. Verifica que la cita pertenezca al médico.", true);
        }
    }

    private void mostrarMensaje(String mensaje, boolean error) {
        Alert alert = new Alert(error ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
