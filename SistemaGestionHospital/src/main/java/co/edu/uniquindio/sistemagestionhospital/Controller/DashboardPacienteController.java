package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.Cita;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;
import co.edu.uniquindio.sistemagestionhospital.model.EntradaHistorial; // Para ver historial
import co.edu.uniquindio.sistemagestionhospital.model.EstadoCita; // Para cancelar
import co.edu.uniquindio.sistemagestionhospital.model.Medico; // Para solicitar cita

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.Comparator;


public class DashboardPacienteController implements Initializable {

    @FXML
    private StackPane contenedorPrincipalPaciente;
    @FXML
    private Label lblBienvenidaPaciente;
    @FXML
    private Label lblFechaHoraPaciente;

    private Paciente pacienteLogueado;
    private HospitalController hospitalController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.hospitalController = HospitalController.getInstance();
        configurarActualizacionFechaHora();
        cargarVistaBienvenida();
    }

    private void configurarActualizacionFechaHora() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            lblFechaHoraPaciente.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    private void cargarVistaBienvenida() {
        Label lblBienvenida = new Label("Bienvenido a tu Panel de Paciente. Selecciona una opción del menú.");
        lblBienvenida.setStyle("-fx-font-size: 16px;");
        contenedorPrincipalPaciente.getChildren().setAll(lblBienvenida);
    }



    @FXML
    private void verMisCitas(ActionEvent event) {
        if (pacienteLogueado == null) {
            mostrarAlerta("No se ha identificado al paciente.", Alert.AlertType.ERROR);
            return;
        }
        System.out.println("Mostrando mis citas...");

        TableView<Cita> tablaCitas = new TableView<>();
        TableColumn<Cita, String> colIdCita = new TableColumn<>("ID Cita");
        colIdCita.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Cita, LocalDate> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        TableColumn<Cita, LocalTime> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));

        TableColumn<Cita, String> colMedico = new TableColumn<>("Médico");
        colMedico.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getMedico() != null ? cellData.getValue().getMedico().getNombre() : "N/A"
        ));

        TableColumn<Cita, String> colEspecialidad = new TableColumn<>("Especialidad");
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));

        TableColumn<Cita, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));


        tablaCitas.getColumns().addAll(colIdCita, colFecha, colHora, colMedico, colEspecialidad, colEstado);

        List<Cita> misCitas = pacienteLogueado.getCitasProgramadas(); // Usa el método de Paciente.java
        if (misCitas != null) {

            misCitas.sort(Comparator.comparing(Cita::getFecha, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Cita::getHora, Comparator.nullsLast(Comparator.naturalOrder())));
            tablaCitas.setItems(FXCollections.observableArrayList(misCitas));
        }

        Button btnCancelarCita = new Button("Cancelar Cita Seleccionada");
        btnCancelarCita.setOnAction(e -> {
            Cita citaSeleccionada = tablaCitas.getSelectionModel().getSelectedItem();
            if (citaSeleccionada != null) {

                if (EstadoCita.AGENDADA.equals(citaSeleccionada.getEstado()) || EstadoCita.AGENDADA.equals(citaSeleccionada.getEstado())) {
                    confirmarCancelacionCita(citaSeleccionada);
                } else {
                    mostrarAlerta("Solo se pueden cancelar citas en estado AGENDADA o PROGRAMADA. Esta cita está " + citaSeleccionada.getEstado() + ".", Alert.AlertType.WARNING);
                }
            } else {
                mostrarAlerta("Por favor, seleccione una cita para cancelar.", Alert.AlertType.WARNING);
            }
        });

        VBox layoutCitas = new VBox(10, new Label("Mis Citas"), tablaCitas, btnCancelarCita);
        layoutCitas.setPadding(new Insets(10));
        contenedorPrincipalPaciente.getChildren().setAll(layoutCitas);
    }

    private void confirmarCancelacionCita(Cita citaACancelar) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Cancelación");
        confirmacion.setHeaderText("¿Está seguro de que desea cancelar la siguiente cita?");
        confirmacion.setContentText("Médico: " + citaACancelar.getMedico().getNombre() + "\nFecha: " + citaACancelar.getFecha() + "\nHora: " + citaACancelar.getHora());

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {

            boolean canceladaPorPaciente = pacienteLogueado.cancelarCita(String.valueOf(citaACancelar));

            if (canceladaPorPaciente) {

                if (hospitalController.cancelarCitaYNotificar(citaACancelar.getId())) {
                    mostrarAlerta("Cita cancelada exitosamente y médico notificado.", Alert.AlertType.INFORMATION);
                } else {
                    mostrarAlerta("Cita cancelada de su lista, pero hubo un problema al notificar al sistema/médico.", Alert.AlertType.WARNING);
                }
                verMisCitas(null); // Refrescar la lista de citas
            } else {
                mostrarAlerta("No se pudo cancelar la cita de su lista (posiblemente ya no existía o el estado no lo permitía).", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void solicitarNuevaCita(ActionEvent event) {
        if (pacienteLogueado == null) {
            mostrarAlerta("No se ha identificado al paciente.", Alert.AlertType.ERROR);
            return;
        }
        System.out.println("Abriendo formulario para solicitar nueva cita...");


        Dialog<Cita> dialog = new Dialog<>();
        dialog.setTitle("Solicitar Nueva Cita");
        dialog.setHeaderText("Complete los datos para su nueva cita");

        ButtonType solicitarButtonType = new ButtonType("Solicitar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(solicitarButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<Medico> cbMedicos = new ComboBox<>();

        List<Medico> medicosDisponibles = hospitalController.getMedicos();
        if(medicosDisponibles != null) {
            cbMedicos.setItems(FXCollections.observableArrayList(medicosDisponibles));

            cbMedicos.setConverter(new javafx.util.StringConverter<Medico>() {
                @Override public String toString(Medico medico) {
                    return medico != null ? medico.getNombre() + " (" + medico.getEspecialidad() + ")" : "";
                }
                @Override public Medico fromString(String string) {
                    return null; }
            });
        }


        DatePicker dpFecha = new DatePicker(LocalDate.now().plusDays(1));
        TextField txtHora = new TextField();
        txtHora.setPromptText("HH:mm (ej. 14:30)");
        TextArea txtMotivo = new TextArea();
        txtMotivo.setPromptText("Motivo de la consulta");
        txtMotivo.setPrefRowCount(3);

        grid.add(new Label("Médico:"), 0, 0);
        grid.add(cbMedicos, 1, 0);
        grid.add(new Label("Fecha:"), 0, 1);
        grid.add(dpFecha, 1, 1);
        grid.add(new Label("Hora:"), 0, 2);
        grid.add(txtHora, 1, 2);
        grid.add(new Label("Motivo:"), 0, 3);
        grid.add(txtMotivo, 1, 3);

        dialog.getDialogPane().setContent(grid);


        Node solicitarButton = dialog.getDialogPane().lookupButton(solicitarButtonType);
        solicitarButton.setDisable(true);
        Runnable validateInputs = () -> solicitarButton.setDisable(
                cbMedicos.getValue() == null || dpFecha.getValue() == null ||
                        txtHora.getText().trim().isEmpty() || txtMotivo.getText().trim().isEmpty()
        );
        cbMedicos.valueProperty().addListener((obs, oldV, newV) -> validateInputs.run());
        dpFecha.valueProperty().addListener((obs, oldV, newV) -> validateInputs.run());
        txtHora.textProperty().addListener((obs, oldV, newV) -> validateInputs.run());
        txtMotivo.textProperty().addListener((obs, oldV, newV) -> validateInputs.run());


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == solicitarButtonType) {
                try {
                    String idCita = "CITA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                    Medico medicoSeleccionado = cbMedicos.getValue();
                    LocalDate fechaSeleccionada = dpFecha.getValue();
                    LocalTime horaSeleccionada = LocalTime.parse(txtHora.getText().trim());
                    String motivoCita = txtMotivo.getText().trim();
                    String especialidadCita = medicoSeleccionado.getEspecialidad(); // Tomar del médico
                    EstadoCita estadoInicial = EstadoCita.AGENDADA; // O SOLICITADA si necesita confirmación

                    return new Cita(idCita, fechaSeleccionada, horaSeleccionada, motivoCita,
                            pacienteLogueado, medicoSeleccionado, especialidadCita, estadoInicial);
                } catch (DateTimeParseException e) {
                    mostrarAlerta("Formato de hora incorrecto. Use HH:mm.", Alert.AlertType.ERROR);
                    return null;
                } catch (Exception e) {
                    mostrarAlerta("Error creando la solicitud de cita: " + e.getMessage(), Alert.AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });

        Optional<Cita> result = dialog.showAndWait();
        result.ifPresent(nuevaCita -> {

            if (pacienteLogueado.agregarCita(nuevaCita)) {
                if (hospitalController.asignarCita(nuevaCita)) {
                    mostrarAlerta("Cita solicitada y agendada exitosamente. ID: " + nuevaCita.getId(), Alert.AlertType.INFORMATION);
                    verMisCitas(null); // Refrescar lista
                } else {
                    mostrarAlerta("No se pudo agendar la cita en el sistema (verifique disponibilidad del médico o contacte al hospital).", Alert.AlertType.ERROR);
                    pacienteLogueado.cancelarCita(String.valueOf(nuevaCita));
                }
            } else {
                mostrarAlerta("Error al agregar la cita a su lista personal.", Alert.AlertType.ERROR);
            }
        });
    }


    @FXML
    private void verMiHistorialMedico(ActionEvent event) {
        if (pacienteLogueado == null) {
            mostrarAlerta("No se ha identificado al paciente.", Alert.AlertType.ERROR);
            return;
        }
        System.out.println("Mostrando mi historial médico...");

        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/co/edu/uniquindio/sistemagestionhospital/view/HistorialPacienteView.fxml")));
            Parent vistaHistorial = loader.load();

            HistorialPacienteController controllerHistorial = loader.getController();
            controllerHistorial.setPaciente(this.pacienteLogueado);
            contenedorPrincipalPaciente.getChildren().setAll(vistaHistorial);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar su historial médico: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void verMisNotificaciones(ActionEvent event) {
        if (pacienteLogueado == null) {
            mostrarAlerta("No se ha identificado al paciente.", Alert.AlertType.ERROR);
            return;
        }
        System.out.println("Mostrando mis notificaciones...");
        // Utilizar el método de Paciente para obtener List<String>
        List<String> notificaciones = pacienteLogueado.getMensajesDeNotificaciones();

        ListView<String> listaNotificacionesView = new ListView<>();
        if (notificaciones != null && !notificaciones.isEmpty()) {
            listaNotificacionesView.setItems(FXCollections.observableArrayList(notificaciones));
        } else {
            listaNotificacionesView.setPlaceholder(new Label("No tiene notificaciones nuevas."));
        }
        contenedorPrincipalPaciente.getChildren().setAll(new VBox(10, new Label("Mis Notificaciones"), listaNotificacionesView));
    }

    @FXML
    private void actualizarMisDatos(ActionEvent event) {
        if (pacienteLogueado == null) {
            mostrarAlerta("No se ha identificado al paciente.", Alert.AlertType.ERROR);
            return;
        }
        System.out.println("Abriendo formulario para actualizar mis datos...");
        mostrarAlerta("Funcionalidad 'Actualizar Mis Datos' aún no implementada.", Alert.AlertType.INFORMATION);
    }


    @FXML
    private void cerrarSesion(ActionEvent event) {
        System.out.println("Cerrando sesión de paciente...");
        this.pacienteLogueado = null;

        Stage stage = (Stage) lblBienvenidaPaciente.getScene().getWindow();
        stage.close();

        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/co/edu/uniquindio/sistemagestionhospital/view/MainView.fxml")));
            Parent root = loader.load();
            Stage mainStage = new Stage();
            mainStage.setTitle("Sistema de Gestión Hospitalaria");
            mainStage.setScene(new Scene(root));
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al volver a la ventana principal.", Alert.AlertType.ERROR);
        }
    }


    public interface ContratoPaciente {
        void setPaciente(Paciente paciente);
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(tipo == Alert.AlertType.ERROR ? "Error" : (tipo == Alert.AlertType.WARNING ? "Advertencia" : "Información"));
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}