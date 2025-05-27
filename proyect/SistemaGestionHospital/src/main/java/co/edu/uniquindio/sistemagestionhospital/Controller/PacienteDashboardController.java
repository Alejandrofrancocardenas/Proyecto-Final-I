package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.*;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;


public class PacienteDashboardController implements Initializable {

    @FXML
    private StackPane contenedorPrincipalPaciente;
    @FXML
    private Label lblBienvenidaPaciente;
    @FXML
    private Label lblInfoUsuarioPaciente;
    @FXML
    private Label lblFechaHoraPaciente;

    private Paciente pacienteLogueado;
    private HospitalController hospitalController;



    public PacienteDashboardController() {
        System.out.println(">>> NUEVA INSTANCIA de PacienteDashboardController CREADA - HashCode: " + this.hashCode());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.hospitalController = HospitalController.getInstance();
        System.out.println(">>> PacienteDashboardController initialize() - HashCode: " + this.hashCode());
        configurarActualizacionFechaHora();
        cargarVistaBienvenida();
    }

    public void setPacienteLogueado(Paciente paciente) {
        System.out.println(">>> PacienteDashboardController: setPacienteLogueado INVOCADO con Paciente: " +
                (paciente != null ? paciente.getNombre() + " (ID: " + paciente.getId() + "), HashCode ObjPaciente: " + paciente.hashCode()
                        : "NULL") + ", HashCode este Cont: " + this.hashCode());
        this.pacienteLogueado = paciente;

        if (this.pacienteLogueado != null) {
            if (lblBienvenidaPaciente != null) {
                lblBienvenidaPaciente.setText("Bienvenido/a, " + this.pacienteLogueado.getNombre());
            }
            if (lblInfoUsuarioPaciente != null) {
                lblInfoUsuarioPaciente.setText("Paciente: " + this.pacienteLogueado.getNombre() + " (ID: " + this.pacienteLogueado.getId() + ")");
            }
            System.out.println(">>> PacienteDashboardController: Llamando a verMisCitas para: " + this.pacienteLogueado.getNombre());
            verMisCitas(null);
        } else {
            System.err.println(">>> PacienteDashboardController: setPacienteLogueado recibió un paciente nulo.");
            if (lblBienvenidaPaciente != null) lblBienvenidaPaciente.setText("Panel de Paciente");
            if (lblInfoUsuarioPaciente != null) lblInfoUsuarioPaciente.setText("Paciente: No identificado");
            cargarVistaBienvenidaSiPacienteNoIdentificado();
        }
    }

    private void cargarVistaBienvenidaSiPacienteNoIdentificado() {
        if (contenedorPrincipalPaciente != null) {
            Label lblMensaje = new Label("Error: No se ha podido identificar al paciente.");
            lblMensaje.setStyle("-fx-font-size: 16px; -fx-padding: 20px;");
            contenedorPrincipalPaciente.getChildren().setAll(lblMensaje);
        }
    }

    private void configurarActualizacionFechaHora() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            if (lblFechaHoraPaciente != null) {
                lblFechaHoraPaciente.setText(LocalDateTime.now().format(formatter));
            }
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    private void cargarVistaBienvenida() {
        if (contenedorPrincipalPaciente != null) {
            Label lblBienvenida = new Label("Bienvenido a tu Panel de Paciente. Selecciona una opción del menú.");
            lblBienvenida.setStyle("-fx-font-size: 16px; -fx-padding: 20px;");
            contenedorPrincipalPaciente.getChildren().setAll(lblBienvenida);
        }
    }



    @FXML
    private void verMisCitas(ActionEvent event) {
        if (pacienteLogueado == null) {
            mostrarAlerta("No se ha identificado al paciente para mostrar sus citas.", Alert.AlertType.ERROR);
            return;
        }
        System.out.println(">>> PacienteDashboardController: Mostrando Mis Citas para " + pacienteLogueado.getNombre());

        TableView<Cita> tablaCitas = new TableView<>();
        configurarColumnasTablaCitas(tablaCitas);

        List<Cita> misCitas = pacienteLogueado.getCitasProgramadas();
        if (misCitas != null) {
            misCitas.sort(Comparator.comparing(Cita::getFecha, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Cita::getHora, Comparator.nullsLast(Comparator.naturalOrder())));
            tablaCitas.setItems(FXCollections.observableArrayList(misCitas));
            System.out.println(">>> PacienteDashboardController: #Citas obtenidas para " + pacienteLogueado.getNombre() + ": " + misCitas.size());
        } else {
            System.out.println(">>> PacienteDashboardController: La lista de citas del paciente es null.");
        }

        Button btnCancelarCita = new Button("Cancelar Cita Seleccionada");
        btnCancelarCita.setOnAction(e -> {
            Cita citaSeleccionada = tablaCitas.getSelectionModel().getSelectedItem();
            if (citaSeleccionada != null) {
                EstadoCita estadoActual = citaSeleccionada.getEstado();
                if (EstadoCita.AGENDADA.equals(estadoActual) || EstadoCita.AGENDADA.equals(estadoActual)) {
                    confirmarCancelacionCita(citaSeleccionada);
                } else {
                    mostrarAlerta("Solo se pueden cancelar citas en estado AGENDADA o PROGRAMADA. Esta cita está " + estadoActual + ".", Alert.AlertType.WARNING);
                }
            } else {
                mostrarAlerta("Por favor, seleccione una cita para cancelar.", Alert.AlertType.WARNING);
            }
        });

        VBox layoutCitas = new VBox(10, new Label("Mis Citas"), tablaCitas, btnCancelarCita);
        layoutCitas.setPadding(new Insets(10));
        if (contenedorPrincipalPaciente != null) {
            contenedorPrincipalPaciente.getChildren().setAll(layoutCitas);
        } else {
            System.err.println("PacienteDashboardController: contenedorPrincipalPaciente es null en verMisCitas.");
        }
    }

    private void configurarColumnasTablaCitas(TableView<Cita> tablaCitas) {
        TableColumn<Cita, String> colIdCita = new TableColumn<>("ID Cita");
        colIdCita.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Cita, LocalDate> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        TableColumn<Cita, LocalTime> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        TableColumn<Cita, String> colMedico = new TableColumn<>("Médico");
        colMedico.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                (cellData.getValue() != null && cellData.getValue().getMedico() != null) ? cellData.getValue().getMedico().getNombre() : "N/A"
        ));
        TableColumn<Cita, String> colEspecialidad = new TableColumn<>("Especialidad");
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        TableColumn<Cita, EstadoCita> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tablaCitas.getColumns().setAll(colIdCita, colFecha, colHora, colMedico, colEspecialidad, colEstado);
        tablaCitas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void confirmarCancelacionCita(Cita citaACancelar) {
        if (pacienteLogueado == null || hospitalController == null) {
            mostrarAlerta("Error de sistema, no se puede cancelar la cita.", Alert.AlertType.ERROR); return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Cancelación");
        confirmacion.setHeaderText("¿Está seguro de que desea cancelar esta cita?");
        confirmacion.setContentText("Médico: " + (citaACancelar.getMedico() != null ? citaACancelar.getMedico().getNombre() : "N/A") +
                "\nFecha: " + citaACancelar.getFecha() + "\nHora: " + citaACancelar.getHora());

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {

            boolean canceladaLocalmente = pacienteLogueado.cancelarCita(citaACancelar.getId());

            if (canceladaLocalmente) {

                if (hospitalController.cancelarCitaYNotificar(citaACancelar.getId())) {
                    mostrarAlerta("Cita cancelada exitosamente y sistema notificado.", Alert.AlertType.INFORMATION);
                } else {
                    mostrarAlerta("Cita cancelada de su lista, pero hubo un problema al procesar la cancelación en el sistema.", Alert.AlertType.WARNING);
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
            mostrarAlerta("Debe estar identificado como paciente para solicitar una cita.", Alert.AlertType.ERROR);
            return;
        }
        System.out.println("Abriendo formulario para solicitar nueva cita para: " + pacienteLogueado.getNombre());

        Dialog<Cita> dialog = new Dialog<>();
        dialog.setTitle("Solicitar Nueva Cita");
        dialog.setHeaderText("Complete los datos para su nueva cita, " + pacienteLogueado.getNombre());
        ButtonType solicitarButtonType = new ButtonType("Solicitar Cita", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(solicitarButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));
        ComboBox<Medico> cbMedicos = new ComboBox<>();
        List<Medico> medicosDisponibles = hospitalController.getMedicos();
        if(medicosDisponibles != null) {
            cbMedicos.setItems(FXCollections.observableArrayList(medicosDisponibles));
            cbMedicos.setConverter(new StringConverter<Medico>() {
                @Override public String toString(Medico medico) { return medico != null ? medico.getNombre() + " (" + medico.getEspecialidad() + ")" : ""; }
                @Override public Medico fromString(String string) { return null; }
            });
        }
        DatePicker dpFecha = new DatePicker(LocalDate.now().plusDays(1));
        TextField txtHora = new TextField(); txtHora.setPromptText("HH:mm");
        TextArea txtMotivo = new TextArea(); txtMotivo.setPromptText("Motivo de la consulta"); txtMotivo.setPrefRowCount(3);

        grid.add(new Label("Médico/Especialidad:"), 0, 0); grid.add(cbMedicos, 1, 0);
        grid.add(new Label("Fecha:"), 0, 1); grid.add(dpFecha, 1, 1);
        grid.add(new Label("Hora:"), 0, 2); grid.add(txtHora, 1, 2);
        grid.add(new Label("Motivo:"), 0, 3); grid.add(txtMotivo, 1, 3);
        dialog.getDialogPane().setContent(grid);

        Node solicitarButtonNode = dialog.getDialogPane().lookupButton(solicitarButtonType);
        solicitarButtonNode.setDisable(true);
        Runnable validateInputs = () -> solicitarButtonNode.setDisable(
                cbMedicos.getValue() == null || dpFecha.getValue() == null ||
                        txtHora.getText().trim().isEmpty() || txtMotivo.getText().trim().isEmpty()
        );
        cbMedicos.valueProperty().addListener((obs, o, n) -> validateInputs.run());
        dpFecha.valueProperty().addListener((obs, o, n) -> validateInputs.run());
        txtHora.textProperty().addListener((obs, o, n) -> validateInputs.run());
        txtMotivo.textProperty().addListener((obs, o, n) -> validateInputs.run());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == solicitarButtonType) {
                try {
                    Medico medicoSeleccionado = cbMedicos.getValue();

                    return pacienteLogueado.solicitarCita(
                            txtMotivo.getText().trim(),
                            dpFecha.getValue(),
                            LocalTime.parse(txtHora.getText().trim()),
                            medicoSeleccionado.getEspecialidad(),
                            medicoSeleccionado,
                            EstadoCita.AGENDADA // Se pasa el estado inicial
                    );
                } catch (DateTimeParseException e) {
                    mostrarAlerta("Formato de hora incorrecto. Use HH:mm.", Alert.AlertType.ERROR); return null;
                } catch (Exception e) {
                    mostrarAlerta("Error creando la solicitud: " + e.getMessage(), Alert.AlertType.ERROR); e.printStackTrace(); return null;
                }
            }
            return null;
        });

        Optional<Cita> result = dialog.showAndWait();
        result.ifPresent(citaAgregadaAlPaciente -> {
            if (citaAgregadaAlPaciente == null) {
                System.err.println("PacienteDashboardController: Paciente.solicitarCita devolvió null."); return;
            }
            System.out.println("PacienteDashboardController: Cita creada y agregada al paciente: " + citaAgregadaAlPaciente.getId());
            if (hospitalController.asignarCita(citaAgregadaAlPaciente)) { // AsignarCita en HospitalController es crucial
                mostrarAlerta("Cita solicitada y agendada exitosamente. ID: " + citaAgregadaAlPaciente.getId(), Alert.AlertType.INFORMATION);
                verMisCitas(null);
            } else {
                mostrarAlerta("No se pudo agendar la cita en el sistema (verifique disponibilidad o contacte al hospital).", Alert.AlertType.ERROR);
                pacienteLogueado.cancelarCita(citaAgregadaAlPaciente.getId()); // Revertir con ID
            }
        });
    }


    @FXML
    private void verMiHistorialMedico(ActionEvent event) {
        if (pacienteLogueado == null) {
            mostrarAlerta("No se ha identificado al paciente.", Alert.AlertType.ERROR); return;
        }
        cargarVistaEnContenedor("/co/edu/uniquindio/sistemagestionhospital/view/HistorialPacienteView.fxml", this.pacienteLogueado);
    }

    @FXML
    private void verMisNotificaciones(ActionEvent event) {
        if (pacienteLogueado == null) {
            mostrarAlerta("No se ha identificado al paciente.", Alert.AlertType.ERROR); return;
        }
        List<String> notificaciones = pacienteLogueado.getMensajesDeNotificaciones(); // Usa el método de Paciente
        ListView<String> listaNotificacionesView = new ListView<>();
        if (notificaciones != null && !notificaciones.isEmpty()) {
            listaNotificacionesView.setItems(FXCollections.observableArrayList(notificaciones));
        } else {
            listaNotificacionesView.setPlaceholder(new Label("No tiene notificaciones."));
        }
        VBox layout = new VBox(10, new Label("Mis Notificaciones"), listaNotificacionesView);
        layout.setPadding(new Insets(10));
        if(contenedorPrincipalPaciente != null) contenedorPrincipalPaciente.getChildren().setAll(layout);
    }

    @FXML
    private void actualizarMisDatos(ActionEvent event) {
        if (pacienteLogueado == null) {
            mostrarAlerta("No se ha identificado al paciente.", Alert.AlertType.ERROR); return;
        }

        cargarVistaEnContenedor("/co/edu/uniquindio/sistemagestionhospital/view/ActualizarDatosPacienteView.fxml", this.pacienteLogueado);
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        System.out.println("Cerrando sesión de paciente...");
        this.pacienteLogueado = null;
        Stage stage = null;
        if (lblBienvenidaPaciente != null && lblBienvenidaPaciente.getScene() != null) {
            stage = (Stage) lblBienvenidaPaciente.getScene().getWindow();
        } else if (contenedorPrincipalPaciente != null && contenedorPrincipalPaciente.getScene() != null) {
            stage = (Stage) contenedorPrincipalPaciente.getScene().getWindow();
        }
        if (stage != null) stage.close();
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/co/edu/uniquindio/sistemagestionhospital/view/MainView.fxml")));
            Stage mainStage = new Stage();
            mainStage.setTitle("Sistema de Gestión Hospitalaria");
            mainStage.setScene(new Scene(root));
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al volver a la ventana principal.", Alert.AlertType.ERROR);
        }
    }


    private void cargarVistaEnContenedor(String fxmlPath, Paciente pacienteContexto) {
        if (contenedorPrincipalPaciente == null) {
            System.err.println("Error: contenedorPrincipalPaciente es nulo en PacienteDashboardController.");
            mostrarAlerta("Error interno al intentar mostrar la vista.", Alert.AlertType.ERROR); return;
        }
        try {
            contenedorPrincipalPaciente.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPath), "No se encontró FXML: " + fxmlPath));
            Parent vista = loader.load();

            Object controller = loader.getController();
            if (pacienteContexto != null) {
                if (controller instanceof ContratoPaciente) {
                    ((ContratoPaciente) controller).setPaciente(pacienteContexto);
                } else if (controller instanceof HistorialPacienteController) {
                    ((HistorialPacienteController) controller).setPaciente(pacienteContexto);
                } else if (controller instanceof ActualizarDatosPacienteController) {
                    ((ActualizarDatosPacienteController) controller).setPaciente(pacienteContexto);
                }

            }
            contenedorPrincipalPaciente.getChildren().add(vista);
        } catch (Exception e) {
            e.printStackTrace();
            Label lblError = new Label("Error al cargar la vista: " + fxmlPath + "\n" + e.getMessage());
            contenedorPrincipalPaciente.getChildren().setAll(lblError);
        }
    }


    public interface ContratoPaciente {
        void setPaciente(Paciente paciente);
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(tipo.name());
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}