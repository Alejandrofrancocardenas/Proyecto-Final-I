package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.Controller.HospitalController;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class PacienteViewController {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private TextField txtCedula;
    @FXML
    private TableView<Paciente> tablaPacientes;
    @FXML
    private TableColumn<Paciente, String> colId;
    @FXML
    private TableColumn<Paciente, String> colNombre;
    @FXML
    private TableColumn<Paciente, String> colCorreo;
    @FXML
    private TableColumn<Paciente, String> colCedula;
    @FXML
    private TableColumn<Paciente, String> colContrasena;
    @FXML
    private Label lblMensaje;

    private final HospitalController controlador = HospitalController.getInstance();
    private final ObservableList<Paciente> listaPacientes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colCorreo.setCellValueFactory(cellData -> cellData.getValue().correoProperty());
        colCedula.setCellValueFactory(cellData -> cellData.getValue().cedulaProperty());
        colContrasena.setCellValueFactory(cellData -> cellData.getValue().contrasenaProperty());

        tablaPacientes.setItems(listaPacientes);
        actualizarTabla();

        tablaPacientes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> mostrarDatosPacienteSeleccionado(newSelection)
        );
    }

    private void actualizarTabla() {
        listaPacientes.setAll(controlador.getPacientes());
    }

    private void mostrarDatosPacienteSeleccionado(Paciente paciente) {
        if (paciente != null) {
            txtId.setText(paciente.getId());
            txtNombre.setText(paciente.getNombre());
            txtCorreo.setText(paciente.getCorreo());
            txtContrasena.setText(paciente.getContrasena());
            txtCedula.setText(paciente.getCedula());
        }
    }

    @FXML
    private void registrarPaciente() {
        String id = txtId.getText();
        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        String cedula = txtCedula.getText();

        if (id.isBlank() || nombre.isBlank() || correo.isBlank() || contrasena.isBlank() || cedula.isBlank()) {
            mostrarMensaje("Todos los campos son obligatorios.", true);
            return;
        }

        Paciente paciente = new Paciente(id, nombre, correo, contrasena, cedula);

        if (controlador.registrarPaciente(paciente)) {
            mostrarMensaje("Paciente registrado exitosamente.", false);
            actualizarTabla();
            limpiarCampos();
        } else {
            mostrarMensaje("Ya existe un paciente con ese ID.", true);
        }
    }

    @FXML
    private void modificarPaciente() {
        String id = txtId.getText();
        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        String cedula = txtCedula.getText();

        if (id.isBlank()) {
            mostrarMensaje("El ID del paciente es obligatorio para modificar.", true);
            return;
        }

        boolean modificado = controlador.modificarPaciente(id, nombre, correo, contrasena, cedula);

        if (modificado) {
            mostrarMensaje("Paciente modificado exitosamente.", false);
            actualizarTabla();
            limpiarCampos();
        } else {
            mostrarMensaje("No se encontró un paciente con ese ID.", true);
        }
    }

    @FXML
    private void eliminarPaciente() {
        String correo = txtCorreo.getText();

        if (correo.isBlank()) {
            mostrarMensaje("Debes proporcionar el correo para eliminar.", true);
            return;
        }

        boolean eliminado = controlador.eliminarPaciente(correo);

        if (eliminado) {
            mostrarMensaje("Paciente eliminado exitosamente.", false);
            actualizarTabla();
            limpiarCampos();
        } else {
            mostrarMensaje("No se encontró un paciente con ese correo.", true);
        }
    }

    @FXML
    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();
        txtCorreo.clear();
        txtContrasena.clear();
        txtCedula.clear();
        tablaPacientes.getSelectionModel().clearSelection();
        lblMensaje.setText("");
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: " + (esError ? "red;" : "green;"));
    }
}
