package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.model.Cita;
import co.edu.uniquindio.sistemagestionhospital.model.HistorialMedico;
import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;

public class CitaViewController {

    private Paciente paciente;

    @FXML
    private TextField txtFecha;

    @FXML
    private TextField txtHora;

    @FXML
    private ListView<Cita> listaCitas;

    @FXML
    private ComboBox<Medico> comboMedicos;
    @FXML
    private ListView<String> listaHistorial;
    @FXML
    private TextField txtNuevaEntradaHistorial;

    private void actualizarHistorialMedico() {
        listaHistorial.getItems().clear();
        paciente.getHistoriales().forEach(historial ->
                listaHistorial.getItems().add(historial.toString())
        );
    }
    @FXML

    private void agregarHistorialMedico(Medico medico,Paciente paciente,String diagnostico,String tratamiento) {
        String entrada = txtNuevaEntradaHistorial.getText();
        if (entrada == null || entrada.isBlank()) {
            mostrarMensaje("La entrada no puede estar vacía.", true);
            return;
        }

        HistorialMedico nuevoHistorial = new HistorialMedico(medico,paciente,diagnostico,tratamiento);
        paciente.getHistoriales().add(nuevoHistorial);
        txtNuevaEntradaHistorial.clear();
        actualizarHistorialMedico();
        mostrarMensaje("Entrada añadida al historial médico.", false);
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        actualizarListaCitas();
    }

    private void actualizarListaCitas() {
        listaCitas.getItems().clear();
        paciente.getCitas().stream()
                .sorted(Comparator.comparing(Cita::getFecha).thenComparing(Cita::getHora))
                .forEach(cita -> listaCitas.getItems().add(cita));
    }


    @FXML
    private void agendarCita() {
        Medico medico = comboMedicos.getValue();
        String fechaTexto = txtFecha.getText();
        String horaTexto = txtHora.getText();

        if (medico == null || fechaTexto.isBlank() || horaTexto.isBlank()) {
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

    @FXML
    private void cancelarCita() {
        Cita seleccionada = listaCitas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;

        paciente.cancelarCita(seleccionada);
        actualizarListaCitas();
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        Alert alert = new Alert(esError ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
