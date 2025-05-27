package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.Controller.HospitalController;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class GestionPacientesController implements Initializable {

    @FXML private TableView<Paciente> tablaPacientes;
    @FXML private TableColumn<Paciente, String> colIdPaciente;
    @FXML private TableColumn<Paciente, String> colCedulaPaciente;
    @FXML private TableColumn<Paciente, String> colNombrePaciente;
    @FXML private TableColumn<Paciente, String> colCorreoPaciente;

    @FXML private TextField txtIdPaciente;
    @FXML private TextField txtCedulaPaciente;
    @FXML private TextField txtNombrePaciente;
    @FXML private TextField txtCorreoPaciente;
    @FXML private PasswordField txtContrasenaPaciente;


    @FXML private Button btnActualizarPaciente;
    @FXML private Button btnEliminarPaciente;


    private HospitalController hospitalController;
    private ObservableList<Paciente> listaObservablePacientes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.hospitalController = HospitalController.getInstance();
        this.listaObservablePacientes = FXCollections.observableArrayList();

        colIdPaciente.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCedulaPaciente.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombrePaciente.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreoPaciente.setCellValueFactory(new PropertyValueFactory<>("correo"));

        tablaPacientes.setItems(listaObservablePacientes);
        cargarPacientes();

        tablaPacientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDatosPacienteEnFormulario(newSelection);
                btnActualizarPaciente.setDisable(false);
                btnEliminarPaciente.setDisable(false);
                txtIdPaciente.setEditable(false);
                txtCedulaPaciente.setEditable(false);
            } else {
                limpiarFormulario();
            }
        });
        limpiarFormulario();
    }

    private void cargarPacientes() {
        listaObservablePacientes.clear();
        List<Paciente> pacientes = hospitalController.getPacientes();
        if (pacientes != null) {
            listaObservablePacientes.addAll(pacientes);
        }
    }

    private void cargarDatosPacienteEnFormulario(Paciente paciente) {
        txtIdPaciente.setText(paciente.getId());
        txtCedulaPaciente.setText(paciente.getCedula());
        txtNombrePaciente.setText(paciente.getNombre());
        txtCorreoPaciente.setText(paciente.getCorreo());
        txtContrasenaPaciente.clear();
    }

    @FXML
    void handleAgregarPaciente(ActionEvent event) {
        String id = txtIdPaciente.getText().trim();
        String cedula = txtCedulaPaciente.getText().trim();
        String nombre = txtNombrePaciente.getText().trim();
        String correo = txtCorreoPaciente.getText().trim();
        String contrasena = txtContrasenaPaciente.getText().trim();

        if (cedula.isEmpty() || nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta("Todos los campos son obligatorios (excepto ID si es autogenerado).", Alert.AlertType.WARNING);
            return;
        }

        if (id.isEmpty()){
            id = "PAC-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        } else {
            if (hospitalController.buscarPacientePorId(id) != null){
                mostrarAlerta("El ID de paciente '" + id + "' ya existe.", Alert.AlertType.ERROR);
                return;
            }
        }

        if (hospitalController.buscarPacientePorCedula(cedula) != null) {
            mostrarAlerta("La cédula '" + cedula + "' ya está registrada para otro paciente.", Alert.AlertType.ERROR);
            return;
        }

        Paciente nuevoPaciente = hospitalController.registrarPaciente(id, nombre, correo, contrasena, cedula);
        if (nuevoPaciente != null) {
            mostrarAlerta("Paciente agregado exitosamente.", Alert.AlertType.INFORMATION);
            cargarPacientes();
            limpiarFormulario();
        } else {
            mostrarAlerta("Error al agregar el paciente (verifique consola, ID, cédula o correo duplicado).", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleActualizarPaciente(ActionEvent event) {
        Paciente pacienteSeleccionado = tablaPacientes.getSelectionModel().getSelectedItem();
        if (pacienteSeleccionado == null) {
            mostrarAlerta("Seleccione un paciente de la lista para actualizar.", Alert.AlertType.WARNING);
            return;
        }

        String nombre = txtNombrePaciente.getText().trim();
        String correo = txtCorreoPaciente.getText().trim();
        String contrasena = txtContrasenaPaciente.getText().trim();


        if (nombre.isEmpty() || correo.isEmpty()) {
            mostrarAlerta("Nombre y correo son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        String contrasenaParaActualizar = contrasena.isEmpty() ? pacienteSeleccionado.getContrasena() : contrasena;

        boolean actualizado = hospitalController.modificarPaciente(pacienteSeleccionado.getId(), nombre, correo, pacienteSeleccionado.getCedula(), contrasenaParaActualizar);
        if (actualizado) {
            mostrarAlerta("Paciente actualizado exitosamente.", Alert.AlertType.INFORMATION);
            cargarPacientes();
            limpiarFormulario();
        } else {
            mostrarAlerta("Error al actualizar el paciente.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleEliminarPaciente(ActionEvent event) {
        Paciente pacienteSeleccionado = tablaPacientes.getSelectionModel().getSelectedItem();
        if (pacienteSeleccionado == null) {
            mostrarAlerta("Seleccione un paciente de la lista para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Está seguro de que desea eliminar al paciente: " + pacienteSeleccionado.getNombre() + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer y podría afectar citas asociadas.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean eliminado = hospitalController.eliminarPaciente(pacienteSeleccionado.getId());
            if (eliminado) {
                mostrarAlerta("Paciente eliminado exitosamente.", Alert.AlertType.INFORMATION);
                cargarPacientes();
                limpiarFormulario();
            } else {
                mostrarAlerta("Error al eliminar el paciente.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void handleLimpiarCamposPaciente(ActionEvent event) {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtIdPaciente.clear();
        txtCedulaPaciente.clear();
        txtNombrePaciente.clear();
        txtCorreoPaciente.clear();
        txtContrasenaPaciente.clear();
        tablaPacientes.getSelectionModel().clearSelection();
        btnActualizarPaciente.setDisable(true);
        btnEliminarPaciente.setDisable(true);
        txtIdPaciente.setEditable(true);
        txtCedulaPaciente.setEditable(true);
        txtIdPaciente.requestFocus();
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(tipo.toString());
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}