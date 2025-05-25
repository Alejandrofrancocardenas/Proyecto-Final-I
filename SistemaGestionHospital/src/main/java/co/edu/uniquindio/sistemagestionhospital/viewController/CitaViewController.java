package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.model.Cita;
import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class CitaViewController {

    private Paciente paciente;
    private Medico medico;
    private LocalDate fecha;
    private LocalTime hora;


    @FXML
    private TextField txtFecha, txtMotivo;

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        actualizarListaCitas();
    }

    private void actualizarListaCitas() {
        listaCitas.getItems().clear();
        for (Cita cita : paciente.getCitas()) {
            listaCitas.getItems().add(cita);
        }
    }

    @FXML
    private void agendarCita() {
        Medico medico = comboMedicos.getValue(); // Se obtiene del ComboBox
        String fechaTexto = txtFecha.getText();
        String horaTexto = txtHora.getText();
        String motivo = txtMotivo.getText();

        if (medico == null || fechaTexto.isBlank() || horaTexto.isBlank() || motivo.isBlank()) {
            mostrarMensaje("Completa todos los campos.", true);
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

    private void mostrarMensaje(String mensaje, boolean esError) {
        Alert alert = new Alert(esError ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private ListView<Cita> listaCitas;



    @FXML
    private void cancelarCita() {
        Cita seleccionada = listaCitas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;

        paciente.cancelarCita(seleccionada);
        actualizarListaCitas();
    }



    @FXML
    private ComboBox<Medico> comboMedicos;

    @FXML
    private TextField txtHora;
}
