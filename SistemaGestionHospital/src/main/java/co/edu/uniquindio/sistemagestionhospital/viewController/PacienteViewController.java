package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.Controller.PacienteController;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;
import co.edu.uniquindio.sistemagestionhospital.model.Cita; // Importar Cita si se usa
import co.edu.uniquindio.sistemagestionhospital.model.HistorialMedico; // Importar HistorialMedico si se usa

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.util.ArrayList; // Para Listas
import java.util.List; // Para Listas


public class PacienteViewController {


    private PacienteController pacienteController;


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

    public PacienteViewController() {

        this.pacienteController = new PacienteController();
    }

    // Método que se llama cuando se activa el botón de registro de paciente en la UI
    @FXML
    void registrarPaciente(ActionEvent event) {

        String id = txtId.getText();
        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        String cedula = txtCedula.getText();


        if (id.isEmpty() || nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || cedula.isEmpty()) {
            mostrarAlerta("Error de Registro", "Todos los campos son obligatorios. Por favor, rellene todos los datos.", Alert.AlertType.ERROR);
            return;
        }


        Paciente nuevoPaciente = new Paciente(id, nombre, correo, contrasena, cedula);

        boolean registrado = pacienteController.registrarPaciente(nuevoPaciente);


        if (registrado) {
            mostrarAlerta("Registro Exitoso", "Paciente registrado correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos(); // Limpiar los campos después de un registro exitoso
        } else {

            mostrarAlerta("Error de Registro", "No se pudo registrar el paciente. Es posible que el ID o la cédula ya existan.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtCorreo.setText("");
        txtContrasena.setText("");
        txtCedula.setText("");
    }

}