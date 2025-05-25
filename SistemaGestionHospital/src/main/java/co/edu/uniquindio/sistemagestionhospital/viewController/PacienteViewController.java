package co.edu.uniquindio.sistemagestionhospital.viewController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacienteViewController {

    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtContrasena;
    @FXML private TextField txtCedula;

    @FXML private Button btnRegistrar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnHistorial;

    // Lista temporal de pacientes
    private List<Paciente> listaPacientes = new ArrayList<>();

    @FXML
    public void initialize() {
        btnRegistrar.setOnAction(e -> registrarPaciente());
        btnModificar.setOnAction(e -> modificarPaciente());
        btnEliminar.setOnAction(e -> eliminarPaciente());
        btnHistorial.setOnAction(e -> mostrarHistorial());
    }

    private void registrarPaciente() {
        String id = txtId.getText();
        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        String cedula = txtCedula.getText();

        if (id.isEmpty() || nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || cedula.isEmpty()) {
            mostrarAlerta("Todos los campos son obligatorios.");
            return;
        }

        // Validar que no exista un paciente con el mismo ID
        Optional<Paciente> pacienteExistente = listaPacientes.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (pacienteExistente.isPresent()) {
            mostrarAlerta("Ya existe un paciente con ese ID.");
            return;
        }

        Paciente paciente = new Paciente(id, nombre, correo, contrasena, cedula);
        listaPacientes.add(paciente);
        mostrarAlerta("Paciente registrado con éxito.");
        limpiarCampos();
    }

    private void modificarPaciente() {
        String id = txtId.getText();

        Optional<Paciente> pacienteOptional = listaPacientes.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (pacienteOptional.isPresent()) {
            Paciente paciente = pacienteOptional.get();
            paciente.setNombre(txtNombre.getText());
            paciente.setCorreo(txtCorreo.getText());
            paciente.setContrasena(txtContrasena.getText());
            paciente.setCedula(txtCedula.getText());
            mostrarAlerta("Paciente modificado exitosamente.");
        } else {
            mostrarAlerta("Paciente no encontrado.");
        }
    }

    private void eliminarPaciente() {
        String id = txtId.getText();

        boolean eliminado = listaPacientes.removeIf(p -> p.getId().equals(id));

        if (eliminado) {
            mostrarAlerta("Paciente eliminado.");
            limpiarCampos();
        } else {
            mostrarAlerta("Paciente no encontrado.");
        }
    }

    private void mostrarHistorial() {
        mostrarAlerta("Funcionalidad de historial aún no implementada.");
    }

    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();
        txtCorreo.clear();
        txtContrasena.clear();
        txtCedula.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
