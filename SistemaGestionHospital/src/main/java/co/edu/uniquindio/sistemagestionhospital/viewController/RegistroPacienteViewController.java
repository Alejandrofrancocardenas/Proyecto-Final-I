package co.edu.uniquindio.sistemagestionhospital.viewController; // Asegúrate de que el paquete sea correcto

import co.edu.uniquindio.sistemagestionhospital.Controller.HospitalController;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistroPacienteViewController {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtContrasena;
    @FXML
    private TextField txtCedula;

    private final HospitalController hospitalController = HospitalController.getInstance();

    @FXML
    void registrarPaciente(ActionEvent event) {
        String id = txtId.getText();
        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        String cedula = txtCedula.getText();

        if (id.isEmpty() || nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || cedula.isEmpty()) {
            mostrarAlerta("Error de Registro", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        if (!id.matches("\\d+")) {
            mostrarAlerta("Error de Formato", "El ID debe contener solo números.", Alert.AlertType.ERROR);
            return;
        }

        if (!cedula.matches("\\d+")) {
            mostrarAlerta("Error de Formato", "La Cédula debe contener solo números.", Alert.AlertType.ERROR);
            return;
        }

        if (!correo.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
            mostrarAlerta("Error de Formato", "El correo electrónico no tiene un formato válido.", Alert.AlertType.ERROR);
            return;
        }


        try {
            Paciente nuevoPaciente = hospitalController.registrarPaciente(id, nombre, correo, contrasena, cedula);

            if (nuevoPaciente != null) {
                mostrarAlerta("Registro Exitoso", "El paciente " + nuevoPaciente.getNombre() + " ha sido registrado con el ID: " + nuevoPaciente.getId(), Alert.AlertType.INFORMATION);
                limpiarCampos();

                Stage stage = (Stage) txtId.getScene().getWindow();
                stage.close();
            } else {
                // Esto podría ocurrir si el ID ya existe, pero la lógica de HospitalController lo maneja.
                // Asegúrate de que HospitalController devuelva null o una excepción en ese caso.
                mostrarAlerta("Error de Registro", "No se pudo registrar el paciente. Es posible que el ID o la cédula ya estén en uso.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            // Captura cualquier otra excepción que pueda ocurrir durante el registro
            mostrarAlerta("Error Interno", "Ocurrió un error al intentar registrar el paciente: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtCorreo.setText("");
        txtContrasena.setText("");
        txtCedula.setText("");
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}