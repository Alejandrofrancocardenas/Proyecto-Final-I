package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.Controller.MedicoController;
import co.edu.uniquindio.sistemagestionhospital.model.HorarioAtencion;
import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.UUID;

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

        if (medicoController == null) {

            mostrarAlerta("Error: El controlador del médico no está disponible.", Alert.AlertType.ERROR);
            return;
        }

        String diaTexto = txtDia.getText();
        String inicioTexto = txtInicio.getText();
        String finTexto = txtFin.getText();

        if (diaTexto.isBlank() || inicioTexto.isBlank() || finTexto.isBlank()) {
            // LLAMADA A mostrarAlerta
            mostrarAlerta("Por favor, complete todos los campos: Día, Hora de Inicio y Hora de Fin.", Alert.AlertType.WARNING); // Cambiado a WARNING para diferenciar
            return;
        }

        try {
            DayOfWeek dia;
            try {
                dia = DayOfWeek.valueOf(diaTexto.trim().toUpperCase());
            } catch (IllegalArgumentException e) {

                mostrarAlerta("Día inválido. Use el nombre completo del día (ej. LUNES, MARTES).", Alert.AlertType.ERROR);
                return;
            }

            LocalTime inicio = LocalTime.parse(inicioTexto.trim());
            LocalTime fin = LocalTime.parse(finTexto.trim());

            if (inicio.isAfter(fin) || inicio.equals(fin)) {

                mostrarAlerta("La hora de inicio no puede ser posterior o igual a la hora de fin.", Alert.AlertType.WARNING); // Cambiado a WARNING
                return;
            }

            String idHorario = UUID.randomUUID().toString();
            HorarioAtencion nuevoHorario = new HorarioAtencion(idHorario, dia, inicio, fin); // Asume constructor (String, DayOfWeek, LocalTime, LocalTime)

            if (medicoController.agregarHorario(nuevoHorario)) { // Asume que MedicoController.agregarHorario devuelve boolean

                mostrarAlerta("Horario agregado exitosamente con ID: " + nuevoHorario.getId(), Alert.AlertType.INFORMATION);
                actualizarLista();
                limpiarCamposHorario();
            } else {

                mostrarAlerta("No se pudo agregar el horario. Puede que ya exista o haya un conflicto.", Alert.AlertType.ERROR);
            }
        } catch (DateTimeParseException e) {

            mostrarAlerta("Error en el formato de la hora. Use el formato HH:mm (ej. 09:00, 14:30).", Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Ocurrió un error inesperado al agregar el horario: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminarHorario() {
        if (medicoController == null) {
            mostrarAlerta("Error: Controlador de médico no inicializado.", Alert.AlertType.ERROR);
            return;
        }

        HorarioAtencion horarioSeleccionado = listaHorarios.getSelectionModel().getSelectedItem();

        if (horarioSeleccionado != null) {
            if (horarioSeleccionado.getId() == null || horarioSeleccionado.getId().trim().isEmpty()) {
                mostrarAlerta("Error: El horario seleccionado no tiene un ID válido para eliminar.", Alert.AlertType.ERROR);
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Eliminación");
            confirmacion.setHeaderText("¿Está seguro de que desea eliminar el siguiente horario?");
            confirmacion.setContentText(horarioSeleccionado.toString());

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {

                boolean eliminado = medicoController.eliminarHorario(horarioSeleccionado.getId());

                if (eliminado) {
                    mostrarAlerta("Horario eliminado exitosamente.", Alert.AlertType.INFORMATION);
                    actualizarLista(); // Refresca la ListView o TableView
                } else {
                    mostrarAlerta("No se pudo eliminar el horario (puede que ya no exista o hubo un error).", Alert.AlertType.ERROR);
                }
            }
        } else {
            mostrarAlerta("Por favor, seleccione un horario de la lista para eliminar.", Alert.AlertType.WARNING);
        }
    }
    private void limpiarCamposHorario() {
        txtDia.clear();
        txtInicio.clear();
        txtFin.clear();
        txtDia.requestFocus();
    }
    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(tipo.toString());
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
