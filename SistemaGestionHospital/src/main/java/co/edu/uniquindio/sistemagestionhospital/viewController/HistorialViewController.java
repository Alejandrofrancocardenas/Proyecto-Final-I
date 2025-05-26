package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.model.HistorialMedico;
import co.edu.uniquindio.sistemagestionhospital.model.Hospital;
import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class HistorialViewController {
private Hospital hospital;


public void setHospital(Hospital hospital) {
    this.hospital = hospital;
}
    @FXML
    private ListView<HistorialMedico> listaHistorial;

    @FXML
    private TextField txtDiagnostico;

    @FXML
    private TextField txtTratamiento;

    @FXML
    private TextField txtDescripcion;

    private Paciente paciente;
    private Medico medico;

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        actualizarListaHistorial();
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    @FXML
    private void agregarHistorialMedico() {
        String diagnostico = txtDiagnostico.getText();
        String tratamiento = txtTratamiento.getText();
        String descripcion = txtDescripcion.getText();

        if (diagnostico.isBlank() || tratamiento.isBlank() || descripcion.isBlank() || paciente == null || medico == null) {
            mostrarMensaje("Completa todos los campos y asegúrate de que hay un médico y paciente seleccionado.", true);
            return;
        }

        HistorialMedico nuevo = new HistorialMedico(diagnostico, tratamiento, medico, paciente, descripcion);
        paciente.getHistoriales().add(nuevo);
        mostrarMensaje("Entrada agregada al historial.", false);
        actualizarListaHistorial();
        limpiarCampos();
    }

    private void actualizarListaHistorial() {
        listaHistorial.getItems().clear();
        if (paciente != null) {
            listaHistorial.getItems().addAll(paciente.getHistoriales());
        }
    }

    private void limpiarCampos() {
        txtDiagnostico.clear();
        txtTratamiento.clear();
        txtDescripcion.clear();
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        Alert alert = new Alert(esError ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
