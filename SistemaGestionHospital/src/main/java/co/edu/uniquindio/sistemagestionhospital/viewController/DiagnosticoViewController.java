package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.model.Cita;
import co.edu.uniquindio.sistemagestionhospital.model.EstadoCita;
import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class DiagnosticoViewController {

    @FXML
    private ComboBox<Cita> comboCitas;

    @FXML
    private TextArea txtDiagnostico, txtTratamiento;

    @FXML
    private Label lblMensaje;

    private Medico medico;

    public void setMedico(Medico medico) {
        this.medico = medico;
        cargarCitasPendientes();
    }

    private void cargarCitasPendientes() {

        if (medico == null || comboCitas == null) {

            System.err.println("Error: Médico o ComboBox no inicializado en cargarCitasPendientes.");
            return;
        }

        comboCitas.getItems().clear();
        List<Cita> citasDelMedico = medico.getCitas();

        if (citasDelMedico != null) {
            for (Cita cita : citasDelMedico) {
                if (cita != null && cita.getEstado() != null) {
                    if (cita.getEstado().equals(EstadoCita.AGENDADA)) {
                        comboCitas.getItems().add(cita);
                    }
                }
            }
        }
    }
    @FXML
    private void registrarDiagnostico() {
        Cita citaSeleccionada = comboCitas.getValue();
        String diagnostico = txtDiagnostico.getText();
        String tratamiento = txtTratamiento.getText();

        if (citaSeleccionada == null || diagnostico.isBlank() || tratamiento.isBlank()) {
            lblMensaje.setText(" Todos los campos son obligatorios.");
            lblMensaje.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        boolean exito = medico.agregarEntradaHistorial(citaSeleccionada, diagnostico, tratamiento);

        if (exito) {
            lblMensaje.setText(" Diagnóstico registrado correctamente.");
            lblMensaje.setTextFill(javafx.scene.paint.Color.GREEN);
            cargarCitasPendientes();
            txtDiagnostico.clear();
            txtTratamiento.clear();
        } else {
            lblMensaje.setText(" Error al registrar diagnóstico.");
            lblMensaje.setTextFill(javafx.scene.paint.Color.RED);
        }
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtDiagnostico.getScene().getWindow();
        stage.close();
    }
}
