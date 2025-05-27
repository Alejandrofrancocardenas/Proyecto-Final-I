package co.edu.uniquindio.sistemagestionhospital.Controller;


import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import javafx.collections.FXCollections;

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

public class GestionMedicosController implements Initializable {

    @FXML
    private TableView<Medico> tablaMedicos;
    @FXML
    private TableColumn<Medico, String> colIdMedico;
    @FXML
    private TableColumn<Medico, String> colNombreMedico;
    @FXML
    private TableColumn<Medico, String> colEspecialidadMedico;
    @FXML
    private TableColumn<Medico, String> colCorreoMedico;

    @FXML
    private TextField txtIdMedico;
    @FXML
    private TextField txtNombreMedico;
    @FXML
    private TextField txtCorreoMedico;
    @FXML
    private PasswordField txtContrasenaMedico;
    @FXML
    private TextField txtEspecialidadMedico;

    @FXML
    private Button btnActualizarMedico;
    @FXML
    private Button btnEliminarMedico;
    @FXML
    private Button btnLimpiarCampos;

    private HospitalController hospitalController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.hospitalController = HospitalController.getInstance();

        colIdMedico.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreMedico.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEspecialidadMedico.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        colCorreoMedico.setCellValueFactory(new PropertyValueFactory<>("correo"));

        tablaMedicos.setItems(FXCollections.observableArrayList());
        cargarMedicos();


        tablaMedicos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDatosMedicoEnFormulario(newSelection);
                btnActualizarMedico.setDisable(false);
                btnEliminarMedico.setDisable(false);
                txtIdMedico.setEditable(false);
            } else {
                limpiarFormulario();
            }
        });
        limpiarFormulario();
    }

    private void cargarMedicos() {
        System.out.println("GestionMedicosController: Iniciando cargarMedicos()...");
        List<Medico> medicosDelHospital = hospitalController.getMedicos(); // Llama al método depurado

        if (medicosDelHospital != null) {
            System.out.println("GestionMedicosController: Médicos recibidos para la tabla: " + medicosDelHospital.size());
            tablaMedicos.setItems(FXCollections.observableArrayList(medicosDelHospital));
        } else {
            System.err.println("GestionMedicosController: La lista de médicos del hospitalController es null.");
            tablaMedicos.setItems(FXCollections.observableArrayList()); // Vaciar la tabla
        }
        System.out.println("GestionMedicosController: Tabla de médicos en UI actualizada con " + tablaMedicos.getItems().size() + " elementos.");
    }
    private void cargarDatosMedicoEnFormulario(Medico medico) {
        txtIdMedico.setText(medico.getId());
        txtNombreMedico.setText(medico.getNombre());
        txtCorreoMedico.setText(medico.getCorreo());
        txtEspecialidadMedico.setText(medico.getEspecialidad());
        txtContrasenaMedico.clear();
    }

    @FXML
    void handleAgregarMedico(ActionEvent event) {

        String id = txtIdMedico.getText().trim();
        String nombre = txtNombreMedico.getText().trim();
        String correo = txtCorreoMedico.getText().trim();
        String contrasena = txtContrasenaMedico.getText().trim();
        String especialidad = txtEspecialidadMedico.getText().trim();

        System.out.println("GestionMedicosController: Datos del formulario: ID=" + id + ", Nombre=" + nombre + ", Correo=" + correo + ", Especialidad=" + especialidad);
        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || especialidad.isEmpty()) {
            mostrarAlerta("Todos los campos son obligatorios (excepto ID si se genera automáticamente).", Alert.AlertType.WARNING);
            return;
        }

        boolean idFueAutogenerado = false;
        if (id.isEmpty()){
            id = "MED-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
            idFueAutogenerado = true;
            System.out.println("GestionMedicosController: ID de médico autogenerado: " + id);
        }

        if (!idFueAutogenerado && hospitalController.buscarMedicoPorId(id) != null){ //
            mostrarAlerta("El ID de médico '" + id + "' ya existe. Por favor, ingrese uno diferente o deje el campo ID vacío para autogenerarlo.", Alert.AlertType.ERROR);
            return;
        }


        System.out.println("GestionMedicosController: Llamando a hospitalController.registrarMedico con ID: " + id + ", Nombre: " + nombre);


        Medico medicoAgregado = hospitalController.registrarMedico(id, nombre, correo, contrasena, especialidad);

        if (medicoAgregado != null) {
            mostrarAlerta("Médico '" + medicoAgregado.getNombre() + "' agregado exitosamente.", Alert.AlertType.INFORMATION);
            cargarMedicos();
            limpiarFormulario();
        } else {
            mostrarAlerta("Error al agregar el médico. Verifique que el ID o correo no estén duplicados, o revise la consola para más detalles.", Alert.AlertType.ERROR);
        }
    }
    @FXML
    void handleActualizarMedico(ActionEvent event) {
        Medico medicoSeleccionado = tablaMedicos.getSelectionModel().getSelectedItem();
        if (medicoSeleccionado == null) {
            mostrarAlerta("Seleccione un médico de la lista para actualizar.", Alert.AlertType.WARNING);
            return;
        }

        String nombre = txtNombreMedico.getText().trim();
        String correo = txtCorreoMedico.getText().trim();
        String contrasenaNueva = txtContrasenaMedico.getText().trim();
        String especialidad = txtEspecialidadMedico.getText().trim();

        if (nombre.isEmpty() || correo.isEmpty() || especialidad.isEmpty()) {
            mostrarAlerta("Nombre, correo y especialidad son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        boolean actualizado = hospitalController.modificarMedico(
                medicoSeleccionado.getId(),
                nombre,
                correo,
                contrasenaNueva,
                especialidad
        );

        if (actualizado) {
            mostrarAlerta("Médico actualizado exitosamente.", Alert.AlertType.INFORMATION);
            cargarMedicos();
            limpiarFormulario();
        } else {
            mostrarAlerta("Error al actualizar el médico.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleEliminarMedico(ActionEvent event) {
        Medico medicoSeleccionado = tablaMedicos.getSelectionModel().getSelectedItem();
        if (medicoSeleccionado == null) {
            mostrarAlerta("Seleccione un médico de la lista para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Está seguro de que desea eliminar al médico: " + medicoSeleccionado.getNombre() + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean eliminado = hospitalController.eliminarMedico(medicoSeleccionado.getId());
            if (eliminado) {
                mostrarAlerta("Médico eliminado exitosamente.", Alert.AlertType.INFORMATION);
                cargarMedicos();
                limpiarFormulario();
            } else {
                mostrarAlerta("Error al eliminar el médico (podría tener citas asignadas u otro impedimento).", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void handleLimpiarCampos(ActionEvent event) {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtIdMedico.clear();
        txtNombreMedico.clear();
        txtCorreoMedico.clear();
        txtContrasenaMedico.clear();
        txtEspecialidadMedico.clear();
        tablaMedicos.getSelectionModel().clearSelection();
        btnActualizarMedico.setDisable(true);
        btnEliminarMedico.setDisable(true);
        txtIdMedico.setEditable(true);
        txtIdMedico.requestFocus();
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(tipo.toString().substring(0,1).toUpperCase() + tipo.toString().substring(1).toLowerCase());
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}