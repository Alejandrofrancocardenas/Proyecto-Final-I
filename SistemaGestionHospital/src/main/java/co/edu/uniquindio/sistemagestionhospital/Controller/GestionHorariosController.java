package co.edu.uniquindio.sistemagestionhospital.Controller;


import co.edu.uniquindio.sistemagestionhospital.model.HorarioAtencion;
import co.edu.uniquindio.sistemagestionhospital.model.Medico;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class GestionHorariosController implements Initializable {

    @FXML private ComboBox<Medico> comboMedicosHorario;
    @FXML private ListView<HorarioAtencion> listaHorariosMedico;
    @FXML private ComboBox<DayOfWeek> cbDiaHorario;
    @FXML private TextField txtHoraInicioHorario;
    @FXML private TextField txtHoraFinHorario;
    @FXML private Button btnEliminarHorarioSeleccionado;

    private HospitalController hospitalController;
    private Medico medicoSeleccionadoParaHorario;
    private ObservableList<HorarioAtencion> observableListaHorarios;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.hospitalController = HospitalController.getInstance();
        this.observableListaHorarios = FXCollections.observableArrayList();
        listaHorariosMedico.setItems(observableListaHorarios);

        cbDiaHorario.setItems(FXCollections.observableArrayList(DayOfWeek.values()));

        cargarComboMedicos();

        comboMedicosHorario.valueProperty().addListener((obs, oldMedico, newMedico) -> {
            medicoSeleccionadoParaHorario = newMedico;
            cargarHorariosDelMedicoSeleccionado();
        });

        listaHorariosMedico.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            btnEliminarHorarioSeleccionado.setDisable(newVal == null);
            if (newVal != null) {
                cbDiaHorario.setValue(newVal.getDiaSemana());
                txtHoraInicioHorario.setText(newVal.getHoraInicio().toString());
                txtHoraFinHorario.setText(newVal.getHoraFin().toString());
            }
        });
        btnEliminarHorarioSeleccionado.setDisable(true);
    }

    private void cargarComboMedicos() {
        List<Medico> medicos = hospitalController.getMedicos();
        if (medicos != null) {
            comboMedicosHorario.setItems(FXCollections.observableArrayList(medicos));
            comboMedicosHorario.setConverter(new StringConverter<Medico>() {
                @Override public String toString(Medico medico) { return medico != null ? medico.getNombre() + " (" + medico.getEspecialidad() + ")" : "";}
                @Override public Medico fromString(String string) { return null; }
            });
        }
    }

    private void cargarHorariosDelMedicoSeleccionado() {
        observableListaHorarios.clear();
        if (medicoSeleccionadoParaHorario != null) {
            // Usar el método de HospitalController para obtener horarios de un médico específico
            List<HorarioAtencion> horarios = hospitalController.obtenerHorariosDeMedico(medicoSeleccionadoParaHorario.getId());
            if (horarios != null) {
                observableListaHorarios.addAll(horarios);
            }
        }
    }

    @FXML
    void handleAgregarHorarioMedico(ActionEvent event) {
        if (medicoSeleccionadoParaHorario == null) {
            mostrarAlerta("Por favor, seleccione un médico primero.", Alert.AlertType.WARNING);
            return;
        }
        DayOfWeek dia = cbDiaHorario.getValue();
        String inicioStr = txtHoraInicioHorario.getText().trim();
        String finStr = txtHoraFinHorario.getText().trim();

        if (dia == null || inicioStr.isEmpty() || finStr.isEmpty()) {
            mostrarAlerta("Complete todos los campos del horario (Día, Inicio, Fin).", Alert.AlertType.WARNING);
            return;
        }

        try {
            LocalTime inicio = LocalTime.parse(inicioStr);
            LocalTime fin = LocalTime.parse(finStr);

            if (inicio.isAfter(fin) || inicio.equals(fin)) {
                mostrarAlerta("La hora de inicio debe ser anterior a la hora de fin.", Alert.AlertType.WARNING);
                return;
            }

            String idHorario = "HOR-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
            HorarioAtencion nuevoHorario = new HorarioAtencion(idHorario, dia, inicio, fin);

            boolean agregado = hospitalController.asignarHorarioAMedico(medicoSeleccionadoParaHorario.getId(), nuevoHorario);

            if (agregado) {
                mostrarAlerta("Horario agregado exitosamente al médico " + medicoSeleccionadoParaHorario.getNombre(), Alert.AlertType.INFORMATION);
                cargarHorariosDelMedicoSeleccionado(); // Refrescar lista
                handleLimpiarCamposFormHorario(null);
            } else {
                mostrarAlerta("No se pudo agregar el horario (posible duplicado o conflicto).", Alert.AlertType.ERROR);
            }

        } catch (DateTimeParseException e) {
            mostrarAlerta("Formato de hora incorrecto. Use HH:mm.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleEliminarHorarioSeleccionado(ActionEvent event) {
        HorarioAtencion horarioSeleccionado = listaHorariosMedico.getSelectionModel().getSelectedItem();
        if (medicoSeleccionadoParaHorario == null || horarioSeleccionado == null) {
            mostrarAlerta("Seleccione un médico y un horario de la lista para eliminar.", Alert.AlertType.WARNING);
            return;
        }
        if (horarioSeleccionado.getId() == null) {
            mostrarAlerta("Error: El horario seleccionado no tiene un ID válido.", Alert.AlertType.ERROR);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Eliminar horario: " + horarioSeleccionado.toString() + " del Dr./Dra. " + medicoSeleccionadoParaHorario.getNombre() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean eliminado = hospitalController.eliminarHorarioDeMedico(medicoSeleccionadoParaHorario.getId(), horarioSeleccionado.getId());
            if (eliminado) {
                mostrarAlerta("Horario eliminado exitosamente.", Alert.AlertType.INFORMATION);
                cargarHorariosDelMedicoSeleccionado(); // Refrescar
            } else {
                mostrarAlerta("Error al eliminar el horario.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void handleLimpiarCamposFormHorario(ActionEvent event) {
        cbDiaHorario.getSelectionModel().clearSelection();
        txtHoraInicioHorario.clear();
        txtHoraFinHorario.clear();
        listaHorariosMedico.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(tipo.toString());
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}