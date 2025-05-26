package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.ResourceBundle;

public class CitaViewController implements Initializable {

    @FXML
    private ComboBox<Medico> cbMedico;

    @FXML
    private ComboBox<Paciente> cbPaciente;

    @FXML
    private TextField txtFecha;

    @FXML
    private TextField txtHora;

    @FXML
    private ListView<Cita> listaCitas;

    private final Hospital hospital = Hospital.getInstance();
    private Paciente paciente;
    private Medico medicoActual;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarPacientesYMedicos();

        cbPaciente.setConverter(new StringConverter<>() {
            @Override
            public String toString(Paciente paciente) {
                return paciente != null ? paciente.getNombre() + " (" + paciente.getId() + ")" : "";
            }

            @Override
            public Paciente fromString(String string) {
                return null;
            }
        });

        cbMedico.setConverter(new StringConverter<>() {
            @Override
            public String toString(Medico medico) {
                return medico != null ? medico.getNombre() + " (" + medico.getId() + ")" : "";
            }

            @Override
            public Medico fromString(String string) {
                return null;
            }
        });

        cbPaciente.setOnAction(event -> {
            Paciente seleccionado = cbPaciente.getValue();
            if (seleccionado != null) {
                this.paciente = seleccionado;
                actualizarListaCitas();
            }
        });
    }

    private void cargarPacientesYMedicos() {
        cbPaciente.setItems(FXCollections.observableArrayList(hospital.getListaPacientes()));
        cbMedico.setItems(FXCollections.observableArrayList(hospital.getListaMedicos()));
    }

    private void actualizarListaCitas() {
        listaCitas.getItems().clear();
        if (paciente != null) {
            paciente.getCitas().stream()
                    .sorted(Comparator.comparing(Cita::getFecha).thenComparing(Cita::getHora))
                    .forEach(listaCitas.getItems()::add);
        }
    }

    @FXML
    private void agendarCita() {
        Medico medico = cbMedico.getValue();
        String fechaTexto = txtFecha.getText();
        String horaTexto = txtHora.getText();

        if (paciente == null || medico == null || fechaTexto.isBlank() || horaTexto.isBlank()) {
            mostrarMensaje("Completa todos los campos y selecciona un paciente.", true);
            return;
        }

        try {
            LocalDate fecha = LocalDate.parse(fechaTexto);
            LocalTime hora = LocalTime.parse(horaTexto);

            Cita nuevaCita = new Cita(paciente, medico, fecha, hora);
            if (paciente.agregarCita(nuevaCita)) {
                mostrarMensaje("Cita agendada exitosamente.", false);
                actualizarListaCitas();
            } else {
                mostrarMensaje("Ya existe una cita en ese horario.", true);
            }
        } catch (Exception e) {
            mostrarMensaje("Formato incorrecto de fecha u hora. Usa yyyy-MM-dd y HH:mm", true);
        }
    }

    @FXML
    private void cancelarCita() {
        Cita seleccionada = listaCitas.getSelectionModel().getSelectedItem();
        if (seleccionada != null && paciente != null) {
            paciente.cancelarCita(seleccionada);
            actualizarListaCitas();
        }
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        Alert alert = new Alert(esError ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void setMedico(Medico medico) {
        this.medicoActual = medico;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        cbPaciente.setValue(paciente); // para que se muestre en el ComboBox
        actualizarListaCitas();
    }

    private Medico obtenerMedicoActual() {
        return this.medicoActual;
    }
}
