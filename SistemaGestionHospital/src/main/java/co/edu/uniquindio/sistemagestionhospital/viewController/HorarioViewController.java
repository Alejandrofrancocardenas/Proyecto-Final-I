package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.Controller.MedicoController;
import co.edu.uniquindio.sistemagestionhospital.model.HorarioAtencion;
import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class HorarioViewController {

    @FXML
    private ListView<HorarioAtencion> listaHorarios;

    @FXML
    private TextField txtDia, txtInicio, txtFin;

    private MedicoController medicoController;

    public void setMedico(Medico medico) {
        this.medicoController = new MedicoController(medico);
        actualizarLista();
    }

    private void actualizarLista() {
        listaHorarios.setItems(FXCollections.observableArrayList(medicoController.getHorarios()));
    }

    @FXML
    private void agregarHorario() {
        try {
            DayOfWeek dia = DayOfWeek.valueOf(txtDia.getText().toUpperCase());
            LocalTime inicio = LocalTime.parse(txtInicio.getText());
            LocalTime fin = LocalTime.parse(txtFin.getText());

            HorarioAtencion horario = new HorarioAtencion(dia, inicio, fin);
            if (medicoController.agregarHorario(horario)) {
                mostrarAlerta("Horario agregado", false);
                actualizarLista();
            } else {
                mostrarAlerta("El horario ya existe.", true);
            }
        } catch (Exception e) {
            mostrarAlerta("Error en el formato. Usa: Día (LUNES), Hora (HH:mm)", true);
        }
    }

    @FXML
    private void eliminarHorario() {
        HorarioAtencion seleccionado = listaHorarios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            medicoController.eliminarHorario(seleccionado);
            actualizarLista();
        }
    }

    private void mostrarAlerta(String mensaje, boolean error) {
        Alert alert = new Alert(error ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
