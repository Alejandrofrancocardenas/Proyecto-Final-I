package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.Controller.HospitalController;
import co.edu.uniquindio.sistemagestionhospital.model.Cita;
import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;
import co.edu.uniquindio.sistemagestionhospital.model.EstadoCita; // Asume que tienes este enum

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GestionCitasController implements Initializable {

    @FXML private DatePicker dpFiltroFechaCita;
    @FXML private ComboBox<Medico> comboFiltroMedicoCita;
    @FXML private ComboBox<Paciente> comboFiltroPacienteCita;
    @FXML private TableView<Cita> tablaCitas;
    @FXML private TableColumn<Cita, String> colIdCita;
    @FXML private TableColumn<Cita, LocalDate> colFechaCita;
    @FXML private TableColumn<Cita, LocalTime> colHoraCita;
    @FXML private TableColumn<Cita, String> colPacienteCita;
    @FXML private TableColumn<Cita, String> colMedicoCita;
    @FXML private TableColumn<Cita, String> colEspecialidadCita;
    @FXML private TableColumn<Cita, String> colEstadoCita;
    @FXML private TableColumn<Cita, String> colMotivoCita;

    @FXML private Button btnModificarCita;
    @FXML private Button btnCancelarCitaAdmin;

    private HospitalController hospitalController;
    private ObservableList<Cita> listaObservableCitas;
    private List<Cita> todasLasCitasOriginales;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.hospitalController = HospitalController.getInstance();
        this.listaObservableCitas = FXCollections.observableArrayList();

        configurarTablaCitas();
        cargarTodasLasCitas();
        cargarFiltros();

        tablaCitas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean seleccionada = newVal != null;
            btnModificarCita.setDisable(!seleccionada);
            btnCancelarCitaAdmin.setDisable(!seleccionada);
        });
    }

    private void configurarTablaCitas() {
        colIdCita.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFechaCita.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHoraCita.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colMotivoCita.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colEspecialidadCita.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        colEstadoCita.setCellValueFactory(new PropertyValueFactory<>("estado"));


        colPacienteCita.setCellValueFactory(cellData -> {
            Paciente p = cellData.getValue().getPaciente();
            return new javafx.beans.property.SimpleStringProperty(p != null ? p.getNombre() : "N/A");
        });
        colMedicoCita.setCellValueFactory(cellData -> {
            Medico m = cellData.getValue().getMedico();
            return new javafx.beans.property.SimpleStringProperty(m != null ? m.getNombre() : "N/A");
        });
        tablaCitas.setItems(listaObservableCitas);
    }

    private void cargarTodasLasCitas() {
        todasLasCitasOriginales = hospitalController.getCitas();
        if (todasLasCitasOriginales != null) {
            listaObservableCitas.setAll(todasLasCitasOriginales);
        } else {
            listaObservableCitas.clear();
        }
    }

    private void cargarFiltros() {
        List<Medico> medicos = hospitalController.getMedicos();
        if (medicos != null) {
            comboFiltroMedicoCita.setItems(FXCollections.observableArrayList(medicos));
            comboFiltroMedicoCita.setConverter(new StringConverter<Medico>() {
                @Override public String toString(Medico medico) { return medico != null ? medico.getNombre() : ""; }
                @Override public Medico fromString(String string) { return null; }
            });
        }
        List<Paciente> pacientes = hospitalController.getPacientes();
        if (pacientes != null) {
            comboFiltroPacienteCita.setItems(FXCollections.observableArrayList(pacientes));
            comboFiltroPacienteCita.setConverter(new StringConverter<Paciente>() {
                @Override public String toString(Paciente paciente) { return paciente != null ? paciente.getNombre() : ""; }
                @Override public Paciente fromString(String string) { return null; }
            });
        }
    }

    @FXML
    void handleAplicarFiltrosCitas(ActionEvent event) {
        if(todasLasCitasOriginales == null) return;

        List<Cita> citasFiltradas = todasLasCitasOriginales.stream()
                .filter(cita -> dpFiltroFechaCita.getValue() == null || cita.getFecha().equals(dpFiltroFechaCita.getValue()))
                .filter(cita -> comboFiltroMedicoCita.getValue() == null || (cita.getMedico() != null && cita.getMedico().equals(comboFiltroMedicoCita.getValue())))
                .filter(cita -> comboFiltroPacienteCita.getValue() == null || (cita.getPaciente() != null && cita.getPaciente().equals(comboFiltroPacienteCita.getValue())))
                .collect(Collectors.toList());
        listaObservableCitas.setAll(citasFiltradas);
    }

    @FXML
    void handleLimpiarFiltrosCitas(ActionEvent event) {
        dpFiltroFechaCita.setValue(null);
        comboFiltroMedicoCita.getSelectionModel().clearSelection();
        comboFiltroPacienteCita.getSelectionModel().clearSelection();
        if (todasLasCitasOriginales != null) {
            listaObservableCitas.setAll(todasLasCitasOriginales);
        }
    }


    @FXML
    void handleAgendarNuevaCita(ActionEvent event) {

        System.out.println("Lógica para abrir formulario de agendar nueva cita...");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/sistemagestionhospital/view/paciente/AgendarCitaView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Agendar Nueva Cita");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            cargarTodasLasCitas();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el formulario de agendar cita.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleModificarCita(ActionEvent event) {
        Cita seleccionada = tablaCitas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Seleccione una cita para modificar.", Alert.AlertType.WARNING);
            return;
        }

        System.out.println("Lógica para modificar cita: " + seleccionada.getId());
        mostrarAlerta("Funcionalidad 'Modificar Cita' no implementada completamente.", Alert.AlertType.INFORMATION);
    }

    @FXML
    void handleCancelarCitaAdmin(ActionEvent event) {
        Cita seleccionada = tablaCitas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Seleccione una cita para cancelar.", Alert.AlertType.WARNING);
            return;
        }
        if (!"Programada".equals(String.valueOf(seleccionada.getEstado()).toUpperCase()) && !"AGENDADA".equals(String.valueOf(seleccionada.getEstado()).toUpperCase())) {
            mostrarAlerta("Solo se pueden cancelar citas en estado PROGRAMADA o AGENDADA.", Alert.AlertType.WARNING);
            return;
        }


        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Cancelación");
        confirmacion.setHeaderText("¿Está seguro de que desea cancelar la cita ID: " + seleccionada.getId() + "?");
        confirmacion.setContentText("Paciente: " + seleccionada.getPaciente().getNombre() + "\nMédico: " + seleccionada.getMedico().getNombre());

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean cancelada = hospitalController.cancelarCitaYNotificar(seleccionada.getId());
            if (cancelada) {
                mostrarAlerta("Cita cancelada exitosamente.", Alert.AlertType.INFORMATION);
                cargarTodasLasCitas();
            } else {
                mostrarAlerta("Error al cancelar la cita.", Alert.AlertType.ERROR);
            }
        }
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(tipo.toString());
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}