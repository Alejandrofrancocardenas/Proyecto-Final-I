package co.edu.uniquindio.sistemagestionhospital.Controller; // Asegúrate que el paquete sea correcto

import co.edu.uniquindio.sistemagestionhospital.model.HorarioAtencion;
import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.UUID;

public class HorarioViewController implements Initializable {

    @FXML
    private ComboBox<DayOfWeek> cbDia;

    @FXML
    private TextField txtHoraInicio;

    @FXML
    private TextField txtHoraFin;

    @FXML
    private ListView<HorarioAtencion> listaHorarios;

    private Medico medico;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbDia.setItems(FXCollections.observableArrayList(DayOfWeek.values()));
        txtHoraInicio.setPromptText("HH:mm");
        txtHoraFin.setPromptText("HH:mm");
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
        if (this.medico != null) {
            actualizarListaHorarios();
        } else {

            listaHorarios.setItems(FXCollections.observableArrayList());
            mostrarAlerta("No se ha seleccionado ningún médico para gestionar horarios.");
        }
    }

    @FXML
    private void agregarHorario() {
        if (medico == null) {
            mostrarAlerta("Error: No hay un médico seleccionado.");
            return;
        }

        DayOfWeek dia = cbDia.getValue();
        String horaInicioStr = txtHoraInicio.getText();
        String horaFinStr = txtHoraFin.getText();

        if (dia == null) {
            mostrarAlerta("Por favor, seleccione un día de la semana.");
            return;
        }
        if (horaInicioStr.isEmpty() || horaFinStr.isEmpty()) {
            mostrarAlerta("Por favor, ingrese la hora de inicio y fin.");
            return;
        }

        try {
            LocalTime horaInicio = LocalTime.parse(horaInicioStr);
            LocalTime horaFin = LocalTime.parse(horaFinStr);

            if (horaInicio.isAfter(horaFin) || horaInicio.equals(horaFin)) {
                mostrarAlerta("Datos inválidos: La hora de inicio no puede ser posterior o igual a la hora de fin.");
                return;
            }

            String idHorario = UUID.randomUUID().toString();

            String diaComoString;
            if (dia != null) {
                diaComoString = dia.name();
            } else {
                mostrarAlerta("Error interno: Día no seleccionado al intentar convertir a String.");
                return;
            }

            HorarioAtencion nuevoHorario = new HorarioAtencion(idHorario, dia, horaInicio, horaFin);

            if (medico.agregarHorario(nuevoHorario)) {
                mostrarInfo("Horario agregado exitosamente.");
                actualizarListaHorarios();
                limpiarCamposEntrada();
            } else {
                mostrarAlerta("No se pudo agregar el horario. Puede que ya exista o haya un conflicto con otro horario.");
            }

        } catch (DateTimeParseException e) {
            mostrarAlerta("Formato de hora inválido. Use el formato HH:mm (ej. 09:00, 14:30).");
        } catch (Exception e) {
            mostrarAlerta("Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarHorario() {
        if (medico == null) {
            mostrarAlerta("Error: No hay un médico seleccionado.");
            return;
        }

        HorarioAtencion seleccionado = listaHorarios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {

            if (seleccionado.getId() == null) {
                mostrarAlerta("Error: El horario seleccionado no tiene un ID para eliminarlo.");
                return;
            }
            if (medico.eliminarHorario(seleccionado.getId())) {
                mostrarInfo("Horario eliminado exitosamente.");
                actualizarListaHorarios();
            } else {
                mostrarAlerta("No se pudo eliminar el horario seleccionado.");
            }
        } else {
            mostrarAlerta("Por favor, seleccione un horario de la lista para eliminar.");
        }
    }

    private void actualizarListaHorarios() {
        if (medico != null) {

            listaHorarios.setItems(FXCollections.observableArrayList(medico.getHorarios()));
        } else {
            listaHorarios.setItems(FXCollections.observableArrayList()); // Lista vacía si no hay médico
        }
    }

    private void limpiarCamposEntrada() {
        cbDia.getSelectionModel().clearSelection();
        txtHoraInicio.clear();
        txtHoraFin.clear();
        cbDia.requestFocus();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}