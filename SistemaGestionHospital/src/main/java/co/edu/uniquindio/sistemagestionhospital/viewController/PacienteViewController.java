package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.Controller.HospitalController;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PacienteViewController implements Initializable {

    private final HospitalController hospitalController = HospitalController.getInstance(); // O inyectado si es singleton

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
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnHistorial;
    @FXML
    private Button btnLimpiarCampos;

    @FXML
    public void initialize() {
        btnRegistrar.setOnAction(event -> registrarPaciente());
        btnModificar.setOnAction(event -> modificarPaciente());
        btnEliminar.setOnAction(event -> eliminarPaciente());
        btnHistorial.setOnAction(event -> verHistorial());
    }

    @FXML
    private void registrarPaciente() {
        String id = txtId.getText();
        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        String cedula = txtCedula.getText();

        Paciente paciente = new Paciente(id, nombre, correo, contrasena, cedula);
        hospitalController.registrarPaciente(paciente);

        mostrarMensaje("Paciente registrado con éxito.");
        limpiarCampos();
    }

    @FXML
    private void modificarPaciente() {
        String id = txtId.getText();
        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        String cedula = txtCedula.getText();

        boolean modificado = hospitalController.modificarPaciente(id, nombre, correo, contrasena, cedula);
        if (modificado) {
            mostrarMensaje("Paciente modificado con éxito.");
        } else {
            mostrarMensaje("No se encontró el paciente.");
        }
        limpiarCampos();
    }

    @FXML
    private void eliminarPaciente() {
        String correo = txtCorreo.getText();
        boolean eliminado = hospitalController.eliminarPaciente(correo);
        if (eliminado) {
            mostrarMensaje("Paciente eliminado con éxito.");
        } else {
            mostrarMensaje("No se encontró el paciente con ese correo.");
        }
        limpiarCampos();
    }

    private void verHistorial() {
        String id = txtId.getText();
        Paciente paciente = hospitalController.buscarPacientePorId(id);
        if (paciente != null) {
            StringBuilder historial = new StringBuilder();
            paciente.getCitas().forEach(c -> historial.append(c.toString()).append("\n"));
            mostrarMensaje("Historial:\n" + historial.toString());
        } else {
            mostrarMensaje("Paciente no encontrado.");
        }
    }

    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();
        txtCorreo.clear();
        txtContrasena.clear();
        txtCedula.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnRegistrar.setOnAction(event -> registrarPaciente());
        btnModificar.setOnAction(event -> modificarPaciente());
        btnEliminar.setOnAction(event -> eliminarPaciente());
        btnHistorial.setOnAction(event -> verHistorial());
        btnLimpiarCampos.setOnAction(event -> limpiarCampos());
    }
}
