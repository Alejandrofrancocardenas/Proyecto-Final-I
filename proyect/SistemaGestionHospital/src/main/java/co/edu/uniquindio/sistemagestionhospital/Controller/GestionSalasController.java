package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.Controller.HospitalController;
import co.edu.uniquindio.sistemagestionhospital.model.Sala;
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

public class GestionSalasController implements Initializable {

    @FXML private TableView<Sala> tablaSalas;
    @FXML private TableColumn<Sala, String> colIdSala;
    @FXML private TableColumn<Sala, String> colNombreSala;
    @FXML private TableColumn<Sala, Integer> colCapacidadSala;

    @FXML private TextField txtIdSala;
    @FXML private TextField txtNombreSala;
    @FXML private TextField txtCapacidadSala;

    @FXML private Button btnAgregarSala;
    @FXML private Button btnActualizarSala;
    @FXML private Button btnEliminarSala;
    @FXML private Button btnLimpiarCamposSala;

    private HospitalController hospitalController;
    private ObservableList<Sala> listaObservableSalas;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.hospitalController = HospitalController.getInstance();
        this.listaObservableSalas = FXCollections.observableArrayList();

        colIdSala.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreSala.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCapacidadSala.setCellValueFactory(new PropertyValueFactory<>("capacidad"));

        tablaSalas.setItems(listaObservableSalas);
        cargarSalas();

        tablaSalas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDatosSalaEnFormulario(newSelection);
                btnActualizarSala.setDisable(false);
                btnEliminarSala.setDisable(false);
                txtIdSala.setEditable(false);
            } else {
                limpiarFormulario();
            }
        });
        limpiarFormulario(); // Estado inicial
    }

    private void cargarSalas() {
        listaObservableSalas.clear();
        List<Sala> salas = hospitalController.getSalas();
        if (salas != null) {
            listaObservableSalas.addAll(salas);
        }
    }

    private void cargarDatosSalaEnFormulario(Sala sala) {
        txtIdSala.setText(sala.getId());
        txtNombreSala.setText(sala.getNombre());
        txtCapacidadSala.setText(String.valueOf(sala.getCapacidad()));
    }

    @FXML
    void handleAgregarSala(ActionEvent event) {
        String id = txtIdSala.getText().trim();
        String nombre = txtNombreSala.getText().trim();
        String capacidadStr = txtCapacidadSala.getText().trim();

        if (nombre.isEmpty() || capacidadStr.isEmpty()) {
            mostrarAlerta("Nombre y capacidad son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        int capacidad;
        try {
            capacidad = Integer.parseInt(capacidadStr);
            if (capacidad <= 0) {
                mostrarAlerta("La capacidad debe ser un número positivo.", Alert.AlertType.WARNING);
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Capacidad debe ser un número válido.", Alert.AlertType.WARNING);
            return;
        }

        if (id.isEmpty()){
            id = "SALA-" + UUID.randomUUID().toString().substring(0,4).toUpperCase();
        } else {
            if (hospitalController.buscarSalaPorId(id) != null){
                mostrarAlerta("El ID de sala '" + id + "' ya existe.", Alert.AlertType.ERROR);
                return;
            }
        }

        boolean agregado = hospitalController.registrarSala(id, nombre, capacidad);
        if (agregado) {
            mostrarAlerta("Sala agregada exitosamente.", Alert.AlertType.INFORMATION);
            cargarSalas();
            limpiarFormulario();
        } else {
            mostrarAlerta("Error al agregar la sala.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleActualizarSala(ActionEvent event) {
        System.out.println(">>> GestionSalasController: handleActualizarSala INVOCADO."); // Punto de depuración

        Sala salaSeleccionada = tablaSalas.getSelectionModel().getSelectedItem();
        if (salaSeleccionada == null) {
            // Esto no debería pasar si el botón solo se habilita cuando hay una selección,
            // pero es una buena guarda.
            mostrarAlerta("Por favor, seleccione una sala de la lista para actualizar.", Alert.AlertType.WARNING);
            return;
        }
        System.out.println("Sala seleccionada para actualizar: ID " + salaSeleccionada.getId());


        String nombre = txtNombreSala.getText().trim();
        String capacidadStr = txtCapacidadSala.getText().trim();

        if (nombre.isEmpty() || capacidadStr.isEmpty()) {
            mostrarAlerta("Nombre y capacidad son obligatorios para actualizar.", Alert.AlertType.WARNING);
            return;
        }
        int capacidad;
        try {
            capacidad = Integer.parseInt(capacidadStr);
            if (capacidad <= 0) {
                mostrarAlerta("La capacidad debe ser un número positivo.", Alert.AlertType.WARNING);
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Capacidad debe ser un número válido.", Alert.AlertType.WARNING);
            return;
        }

        System.out.println("Datos para actualizar: ID=" + salaSeleccionada.getId() + ", Nuevo Nombre=" + nombre + ", Nueva Capacidad=" + capacidad);
        boolean actualizado = hospitalController.modificarSala(salaSeleccionada.getId(), nombre, capacidad);

        if (actualizado) {
            mostrarAlerta("Sala actualizada exitosamente.", Alert.AlertType.INFORMATION);
            cargarSalas(); // Refrescar la tabla
            // limpiarFormulario(); // Limpiar y deshabilitar botones de nuevo
        } else {
            mostrarAlerta("Error al actualizar la sala. Verifique la consola o si el nuevo nombre ya existe.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleEliminarSala(ActionEvent event) {
        Sala salaSeleccionada = tablaSalas.getSelectionModel().getSelectedItem();
        if (salaSeleccionada == null) {
            mostrarAlerta("Seleccione una sala de la lista para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Está seguro de que desea eliminar la sala: " + salaSeleccionada.getNombre() + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean eliminado = hospitalController.eliminarSala(salaSeleccionada.getId());
            if (eliminado) {
                mostrarAlerta("Sala eliminada exitosamente.", Alert.AlertType.INFORMATION);
                cargarSalas();
                limpiarFormulario();
            } else {
                mostrarAlerta("Error al eliminar la sala (podría estar en uso o no existir).", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void handleLimpiarCamposSala(ActionEvent event) {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtIdSala.clear();
        txtNombreSala.clear();
        txtCapacidadSala.clear();
        tablaSalas.getSelectionModel().clearSelection();
        btnActualizarSala.setDisable(true);
        btnEliminarSala.setDisable(true);
        txtIdSala.setEditable(true);
        txtIdSala.requestFocus();
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(tipo.toString());
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}