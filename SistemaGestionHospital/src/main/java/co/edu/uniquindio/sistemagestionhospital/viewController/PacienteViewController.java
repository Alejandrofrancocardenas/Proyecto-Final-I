package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.Controller.HospitalController;
import co.edu.uniquindio.sistemagestionhospital.model.Cita;
import co.edu.uniquindio.sistemagestionhospital.model.HistorialMedico;
import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PacienteViewController {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private TextField txtCedula;
    @FXML
    private TableView<Paciente> tablaPacientes;
    @FXML
    private TableColumn<Paciente, String> colId;
    @FXML
    private TableColumn<Paciente, String> colNombre;
    @FXML
    private TableColumn<Paciente, String> colCorreo;
    @FXML
    private TableColumn<Paciente, String> colCedula;
    @FXML
    private TableColumn<Paciente, String> colContrasena;
    @FXML
    private Label lblMensaje;
    @FXML
    private TextField txtFecha;

    @FXML
    private TextField txtHora;

    @FXML
    private TextField txtMotivo;

    // Nuevos botones
    @FXML
    private Button btnGestionarCitas;
    @FXML
    private Button btnVerHistorial;

    private final HospitalController controlador = HospitalController.getInstance();
    private final ObservableList<Paciente> listaPacientes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colCorreo.setCellValueFactory(cellData -> cellData.getValue().correoProperty());
        colCedula.setCellValueFactory(cellData -> cellData.getValue().cedulaProperty());
        colContrasena.setCellValueFactory(cellData -> cellData.getValue().contrasenaProperty());

        tablaPacientes.setItems(listaPacientes);
        actualizarTabla();

        tablaPacientes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    mostrarDatosPacienteSeleccionado(newSelection);
                    if (newSelection != null) {
                        mostrarNotificaciones(newSelection);
                    }
                }
        );
    }

    private void actualizarTabla() {
        listaPacientes.setAll(controlador.getPacientes());
    }

    private void mostrarDatosPacienteSeleccionado(Paciente paciente) {
        if (paciente != null) {
            txtId.setText(paciente.getId());
            txtNombre.setText(paciente.getNombre());
            txtCorreo.setText(paciente.getCorreo());
            txtContrasena.setText(paciente.getContrasena());
            txtCedula.setText(paciente.getCedula());
        }
    }

    @FXML
    private void registrarPaciente() {
        String id = txtId.getText();
        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        String cedula = txtCedula.getText();

        if (id.isBlank() || nombre.isBlank() || correo.isBlank() || contrasena.isBlank() || cedula.isBlank()) {
            mostrarMensaje("Todos los campos son obligatorios.", true);
            return;
        }

        Paciente paciente = new Paciente(id, nombre, correo, contrasena, cedula);

        if (controlador.registrarPaciente(paciente)) {
            mostrarMensaje("Paciente registrado exitosamente.", false);
            actualizarTabla();
            limpiarCampos();
        } else {
            mostrarMensaje("Ya existe un paciente con ese ID.", true);
        }
    }

    @FXML
    private void modificarPaciente() {
        String id = txtId.getText();
        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        String cedula = txtCedula.getText();

        if (id.isBlank()) {
            mostrarMensaje("El ID del paciente es obligatorio para modificar.", true);
            return;
        }

        boolean modificado = controlador.modificarPaciente(id, nombre, correo, contrasena, cedula);

        if (modificado) {
            mostrarMensaje("Paciente modificado exitosamente.", false);
            actualizarTabla();
            limpiarCampos();
        } else {
            mostrarMensaje("No se encontró un paciente con ese ID.", true);
        }
    }

    @FXML
    private void eliminarPaciente() {
        String correo = txtCorreo.getText();

        if (correo.isBlank()) {
            mostrarMensaje("Debes proporcionar el correo para eliminar.", true);
            return;
        }

        boolean eliminado = controlador.eliminarPaciente(correo);

        if (eliminado) {
            mostrarMensaje("Paciente eliminado exitosamente.", false);
            actualizarTabla();
            limpiarCampos();
        } else {
            mostrarMensaje("No se encontró un paciente con ese correo.", true);
        }
    }

    @FXML
    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();
        txtCorreo.clear();
        txtContrasena.clear();
        txtCedula.clear();
        tablaPacientes.getSelectionModel().clearSelection();
        lblMensaje.setText("");
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: " + (esError ? "red;" : "green;"));
    }

    // 🔹 NUEVO: mostrar notificaciones del paciente seleccionado
    private void mostrarNotificaciones(Paciente paciente) {
        List<String> notificaciones = paciente.getNotificaciones();

        if (!notificaciones.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String n : notificaciones) {
                sb.append("- ").append(n).append("\n");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notificaciones");
            alert.setHeaderText("Notificaciones para " + paciente.getNombre());
            alert.setContentText(sb.toString());
            alert.showAndWait();

            notificaciones.clear(); // Vaciar después de mostrarlas
        }
    }

    // 🔹 NUEVO: abrir vista para gestionar citas
    @FXML
    private void gestionarCitas() {
        Paciente pacienteSeleccionado = tablaPacientes.getSelectionModel().getSelectedItem();
        if (pacienteSeleccionado == null) {
            mostrarMensaje("Selecciona un paciente para gestionar sus citas.", true);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CitaView.fxml"));
            Parent root = loader.load();

            // Pasar paciente si lo necesitas
            // CitaViewController controller = loader.getController();
            // controller.setPaciente(pacienteSeleccionado);

            Stage stage = new Stage();
            stage.setTitle("Gestión de Citas");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔹 NUEVO: mostrar historial médico en ventana emergente
    @FXML
    private void verHistorialMedico() {
        Paciente pacienteSeleccionado = tablaPacientes.getSelectionModel().getSelectedItem();
        if (pacienteSeleccionado == null) {
            mostrarMensaje("Selecciona un paciente para ver su historial médico.", true);
            return;
        }

        List<HistorialMedico> historial = pacienteSeleccionado.getHistoriales();
        if (historial.isEmpty()) {
            mostrarMensaje("El historial médico está vacío.", true);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (HistorialMedico entrada : historial) {
            sb.append("- ").append(entrada).append("\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Historial Médico");
        alert.setHeaderText("Historial de " + pacienteSeleccionado.getNombre());
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }

    @FXML
    private void abrirVentanaCitas() {
        Paciente pacienteSeleccionado = tablaPacientes.getSelectionModel().getSelectedItem();
        if (pacienteSeleccionado == null) {
            mostrarMensaje("Selecciona un paciente para gestionar sus citas.", true);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/sistemagestionhospital/view/CitaView.fxml"));
            Parent root = loader.load();

            CitaViewController controladorCita = loader.getController();
            controladorCita.setPaciente(pacienteSeleccionado);

            Stage stage = new Stage();
            stage.setTitle("Gestión de Citas");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            mostrarMensaje("No se pudo abrir la ventana de citas.", true);
            e.printStackTrace();
        }
    }

    @FXML
    private void agendarCita(Paciente paciente, Medico medico, LocalDate fecha, LocalTime hora) {
        String fechaStr = txtFecha.getText();
        String horaStr = txtHora.getText();
        String motivo = txtMotivo.getText();

        if (fechaStr.isBlank() || horaStr.isBlank() || motivo.isBlank()) {
            mostrarMensaje("Completa todos los campos.", true);
            return;
        }

        try {
            fecha = LocalDate.parse(fechaStr);
            hora = LocalTime.parse(horaStr);

            LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);
            if (fechaHora.isBefore(LocalDateTime.now())) {
                mostrarMensaje("No se puede agendar una cita en el pasado.", true);
                return;
            }

            Cita nuevaCita = new Cita(paciente, medico, fecha, hora);

            if (paciente.agregarCita(nuevaCita)) {
                mostrarMensaje("Cita agendada exitosamente.", false);
                actualizarListaCitas(paciente);
                limpiarCampos();
            } else {
                mostrarMensaje("Ya existe una cita en ese horario.", true);
            }
        } catch (Exception e) {
            mostrarMensaje("Formato incorrecto. Usa YYYY-MM-DD y HH:MM", true);
        }
    }

    private void actualizarListaCitas(Paciente paciente) {
        listaCitas.getItems().clear();
        List<Cita> citasOrdenadas = new ArrayList<>(paciente.getCitas());
        citasOrdenadas.sort(Comparator.comparing(Cita::getFecha).thenComparing(Cita::getHora));
        for (Cita cita : citasOrdenadas) {
            listaCitas.getItems().add(cita.toString());
        }
    }

    @FXML
    private ListView<String> listaCitas;

}
