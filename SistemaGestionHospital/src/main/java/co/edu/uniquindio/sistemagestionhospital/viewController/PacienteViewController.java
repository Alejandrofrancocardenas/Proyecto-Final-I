package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.model.Hospital;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PacienteViewController implements Initializable {

    private Hospital hospital = Hospital.getInstance();

    @FXML private TextField txtId, txtNombre, txtCorreo, txtCedula;
    @FXML private PasswordField txtContrasena;
    @FXML private TableView<Paciente> tablaPacientes;
    @FXML private TableColumn<Paciente, String> colId, colNombre, colCorreo, colCedula, colContrasena;
    @FXML private Label lblMensaje;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colCorreo.setCellValueFactory(cellData -> cellData.getValue().correoProperty());
        colCedula.setCellValueFactory(cellData -> cellData.getValue().cedulaProperty());
        colContrasena.setCellValueFactory(cellData -> cellData.getValue().contrasenaProperty());

        actualizarListaPacientes();

        tablaPacientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtId.setText(newSel.getId());
                txtNombre.setText(newSel.getNombre());
                txtCorreo.setText(newSel.getCorreo());
                txtContrasena.setText(newSel.getContrasena());
                txtCedula.setText(newSel.getCedula());
            }
        });
    }


    @FXML
    private void registrarPaciente() {
        Paciente nuevo = new Paciente(txtId.getText(), txtNombre.getText(), txtCorreo.getText(), txtContrasena.getText(), txtCedula.getText());
        if (hospital.registrarPaciente(nuevo)) {
            mostrarMensaje("Paciente registrado correctamente", false);
            actualizarListaPacientes();
            limpiarCampos();
        } else {
            mostrarMensaje("Ya existe un paciente con ese ID", true);
        }
    }

    @FXML
    private void eliminarPaciente() {
        Paciente seleccionado = tablaPacientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            hospital.eliminarPaciente(seleccionado.getId());
            actualizarListaPacientes();
            limpiarCampos();
        }
    }

    @FXML
    private void modificarPaciente() {
        Paciente seleccionado = tablaPacientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            seleccionado.actualizarDatos(txtNombre.getText(), txtCorreo.getText(), txtContrasena.getText());
            seleccionado.setId(txtId.getText());
            actualizarListaPacientes();
            limpiarCampos();
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
    }

    private void mostrarMensaje(String mensaje, boolean error) {
        Alert alert = new Alert(error ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void actualizarListaPacientes() {
        tablaPacientes.setItems(FXCollections.observableArrayList(hospital.getListaPacientes()));
        tablaPacientes.refresh();
    }

    @FXML
    private void abrirVentanaCitas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/sistemagestionhospital/view/CitaView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Gestión de Citas");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void verHistorialMedico(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/sistemagestionhospital/view/HistorialView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Historial Médico");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
