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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DashboardMedicoController implements Initializable {

    @FXML
    private StackPane contenedorPrincipalMedico;
    @FXML
    private Label lblBienvenidaMedico;
    @FXML
    private Label lblInfoUsuarioMedico;
    @FXML
    private Label lblFechaHoraMedico;

    private Medico medicoLogueado;
    private HospitalController hospitalController;
    private MedicoController medicoControllerPropio;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.hospitalController = HospitalController.getInstance();
        System.out.println(">>> MedicoDashboardController initialize() - HashCode: " + this.hashCode());
        configurarActualizacionFechaHora();
        cargarVistaBienvenida();
    }

    public void setMedicoLogueado(Medico medico) {
        System.out.println(">>> MedicoDashboardController: setMedicoLogueado INVOCADO con Médico: " +
                (medico != null ? medico.getNombre() + " (ID: " + medico.getId() + "), HashCode ObjMedico: " + medico.hashCode() : "NULL") +
                ", HashCode este Cont: " + this.hashCode());
        this.medicoLogueado = medico;

        if (this.medicoLogueado != null) {
            this.medicoControllerPropio = new MedicoController(this.medicoLogueado);

            if (lblBienvenidaMedico != null)
                lblBienvenidaMedico.setText("Bienvenido/a Dr./Dra. " + this.medicoLogueado.getNombre());
            if (lblInfoUsuarioMedico != null)
                lblInfoUsuarioMedico.setText("Médico: " + this.medicoLogueado.getNombre() + " (Especialidad: " + this.medicoLogueado.getEspecialidad() + ")");

            System.out.println(">>> MedicoDashboardController: Llamando a verMisCitasProgramadas para: " + this.medicoLogueado.getNombre());
            verMisCitasProgramadas(null);
        } else {
            System.err.println(">>> MedicoDashboardController: setMedicoLogueado recibió un médico nulo.");
            if (lblBienvenidaMedico != null) lblBienvenidaMedico.setText("Panel de Médico");
            if (lblInfoUsuarioMedico != null) lblInfoUsuarioMedico.setText("Médico: No identificado");
            cargarVistaBienvenidaSiMedicoNoIdentificado();
        }
    }

    private void cargarVistaBienvenidaSiMedicoNoIdentificado() {
        if (contenedorPrincipalMedico != null) {
            Label lblMensaje = new Label("Error: No se ha podido identificar al médico.");
            lblMensaje.setStyle("-fx-font-size: 16px; -fx-padding: 20px;");
            contenedorPrincipalMedico.getChildren().setAll(lblMensaje);
        }
    }

    private void configurarActualizacionFechaHora() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            if (lblFechaHoraMedico != null) lblFechaHoraMedico.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    private void cargarVistaBienvenida() {
        if (contenedorPrincipalMedico != null) {
            Label lblBienvenida = new Label("Bienvenido al Panel de Médico. Seleccione una opción del menú.");
            lblBienvenida.setStyle("-fx-font-size: 16px; -fx-padding: 20px;");
            contenedorPrincipalMedico.getChildren().setAll(lblBienvenida);
        }
    }

    private void configurarColumnasTablaCitas(TableView<Cita> tablaCitas) {
        TableColumn<Cita, String> colIdCita = new TableColumn<>("ID");
        colIdCita.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Cita, LocalDate> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        TableColumn<Cita, LocalTime> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        TableColumn<Cita, String> colPaciente = new TableColumn<>("Paciente");
        colPaciente.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                (cellData.getValue() != null && cellData.getValue().getPaciente() != null) ? cellData.getValue().getPaciente().getNombre() : "N/A"
        ));
        TableColumn<Cita, String> colEspecialidad = new TableColumn<>("Especialidad Cita");
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        TableColumn<Cita, EstadoCita> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tablaCitas.getColumns().setAll(colIdCita, colFecha, colHora, colPaciente, colEspecialidad, colEstado);
        tablaCitas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    private void verMisCitasProgramadas(ActionEvent event) {
        if (medicoLogueado == null) {
            mostrarAlerta("Médico no identificado.", Alert.AlertType.ERROR);
            return;
        }
        if (medicoControllerPropio == null) {
            mostrarAlerta("Controlador de médico no inicializado.", Alert.AlertType.ERROR);
            return;
        }

        System.out.println(">>> MedicoDashboardController.verMisCitas: Mostrando citas para Dr./Dra. " + medicoLogueado.getNombre() + " (HashCode ObjMedico: " + medicoLogueado.hashCode() + ")");

        TableView<Cita> tablaCitas = new TableView<>();
        configurarColumnasTablaCitas(tablaCitas);

        List<Cita> misCitas = medicoControllerPropio.getCitas();
        System.out.println(">>> MedicoDashboardController.verMisCitas: #Citas obtenidas para " + medicoLogueado.getNombre() + ": " + (misCitas != null ? misCitas.size() : "null"));
        if (misCitas != null) {
            misCitas.sort(Comparator.comparing(Cita::getFecha, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Cita::getHora, Comparator.nullsLast(Comparator.naturalOrder())));
            tablaCitas.setItems(FXCollections.observableArrayList(misCitas));
        }

        Button btnCancelarCita = new Button("Cancelar Cita Seleccionada");
        btnCancelarCita.setOnAction(e -> {
            Cita citaSeleccionada = tablaCitas.getSelectionModel().getSelectedItem();
            if (citaSeleccionada != null) {
                EstadoCita estadoActual = citaSeleccionada.getEstado();
                if (EstadoCita.AGENDADA.equals(estadoActual) || EstadoCita.AGENDADA.equals(estadoActual)) {
                    boolean canceladaSistema = hospitalController.cancelarCitaYNotificar(citaSeleccionada.getId());
                    if (canceladaSistema) {
                        mostrarAlerta("Cita cancelada exitosamente.", Alert.AlertType.INFORMATION);
                        verMisCitasProgramadas(null);
                    } else {
                        mostrarAlerta("No se pudo cancelar la cita en el sistema.", Alert.AlertType.ERROR);
                    }
                } else {
                    mostrarAlerta("Solo se pueden cancelar citas AGENDADAS o PROGRAMADAS. Estado: " + estadoActual, Alert.AlertType.WARNING);
                }
            } else {
                mostrarAlerta("Seleccione una cita para cancelar.", Alert.AlertType.WARNING);
            }
        });
        Button btnCompletarCita = new Button("Completar y Registrar Historial");
        btnCompletarCita.setOnAction(e -> {
            Cita citaSeleccionada = tablaCitas.getSelectionModel().getSelectedItem();
            if (citaSeleccionada != null) {
                EstadoCita estadoActual = citaSeleccionada.getEstado();
                if (EstadoCita.AGENDADA.equals(estadoActual) || EstadoCita.AGENDADA.equals(estadoActual)) {
                    abrirDialogoRegistrarHistorial(citaSeleccionada);
                } else {
                    mostrarAlerta("Esta cita no se puede completar (Estado: " + estadoActual + ").", Alert.AlertType.WARNING);
                }
            } else {
                mostrarAlerta("Seleccione una cita para completar.", Alert.AlertType.WARNING);
            }
        });
        HBox botonesLayout = new HBox(10, btnCompletarCita, btnCancelarCita);
        botonesLayout.setPadding(new Insets(10, 0, 0, 0));
        VBox layoutCitas = new VBox(10, new Label("Mis Citas (" + medicoLogueado.getNombre() + ")"), tablaCitas, botonesLayout);
        layoutCitas.setPadding(new Insets(10));
        if (contenedorPrincipalMedico != null) contenedorPrincipalMedico.getChildren().setAll(layoutCitas);
        else
            System.err.println("DashboardMedicoController: contenedorPrincipalMedico es null en verMisCitasProgramadas.");
    }

    private void abrirDialogoRegistrarHistorial(Cita cita) {
        if (medicoLogueado == null || medicoControllerPropio == null) {
            mostrarAlerta("Error de sesión.", Alert.AlertType.ERROR);
            return;
        }
        if (cita == null || cita.getPaciente() == null) {
            mostrarAlerta("Cita/Paciente incompleto.", Alert.AlertType.ERROR);
            return;
        }
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Registrar Atención");
        dialog.setHeaderText("Cita: " + cita.getPaciente().getNombre() + " (" + cita.getFecha() + " " + cita.getHora() + ")");
        ButtonType guardarButtonType = new ButtonType("Guardar y Completar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextArea txtDiagnostico = new TextArea();
        txtDiagnostico.setPromptText("Diagnóstico...");
        txtDiagnostico.setWrapText(true);
        txtDiagnostico.setPrefRowCount(5);
        TextArea txtTratamiento = new TextArea();
        txtTratamiento.setPromptText("Tratamiento...");
        txtTratamiento.setWrapText(true);
        txtTratamiento.setPrefRowCount(5);
        grid.add(new Label("Diagnóstico:"), 0, 0);
        grid.add(txtDiagnostico, 1, 0);
        grid.add(new Label("Tratamiento:"), 0, 1);
        grid.add(txtTratamiento, 1, 1);
        dialog.getDialogPane().setContent(grid);
        Node guardarButtonNode = dialog.getDialogPane().lookupButton(guardarButtonType);
        guardarButtonNode.setDisable(true);
        Runnable validateInputs = () -> guardarButtonNode.setDisable(txtDiagnostico.getText().trim().isEmpty() || txtTratamiento.getText().trim().isEmpty());
        txtDiagnostico.textProperty().addListener((obs, o, n) -> validateInputs.run());
        txtTratamiento.textProperty().addListener((obs, o, n) -> validateInputs.run());
        dialog.setResultConverter(dialogButton -> dialogButton == guardarButtonType);
        Optional<Boolean> resultadoDialogo = dialog.showAndWait();
        resultadoDialogo.ifPresent(guardarPresionado -> {
            if (guardarPresionado) {
                String diagnostico = txtDiagnostico.getText().trim();
                String tratamiento = txtTratamiento.getText().trim();
                boolean historialRegistrado = medicoControllerPropio.agregarEntradaHistorial(cita, diagnostico, tratamiento);
                if (historialRegistrado) {
                    boolean estadoCitaActualizado = hospitalController.actualizarEstadoCita(cita.getId(), EstadoCita.COMPLETADA);
                    if (estadoCitaActualizado) {
                        mostrarAlerta("Atención registrada y cita completada.", Alert.AlertType.INFORMATION);
                        verMisCitasProgramadas(null);
                    } else mostrarAlerta("Historial OK, error al actualizar estado cita.", Alert.AlertType.WARNING);
                } else mostrarAlerta("Error al registrar historial médico.", Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    private void gestionarMisHorarios(ActionEvent event) {
        if (medicoLogueado == null) {
            mostrarAlerta("Médico no identificado.", Alert.AlertType.ERROR);
            return;
        }
        System.out.println("Cargando gestión de horarios para " + medicoLogueado.getNombre());
        try {
            String fxmlPath = "/co/edu/uniquindio/sistemagestionhospital/view/GestionHorariosMedico.fxml";
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPath), "No FXML: " + fxmlPath));
            Parent vistaHorarios = loader.load();
            HorarioViewController controllerHorarios = loader.getController();
            if (controllerHorarios != null) controllerHorarios.setMedico(this.medicoLogueado);
            else {
                mostrarAlerta("Error controlador horarios.", Alert.AlertType.ERROR);
                return;
            }
            if (contenedorPrincipalMedico != null) contenedorPrincipalMedico.getChildren().setAll(vistaHorarios);
            else System.err.println("Error: contenedorPrincipalMedico null en gestionarMisHorarios.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar gestión de horarios: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void buscarPaciente(ActionEvent event) { // Asegúrate que esté anotado con @FXML
        System.out.println(">>> MedicoDashboardController: buscarPaciente() INVOCADO."); // PRIMER PUNTO DE DEPURACIÓN

        if (medicoLogueado == null) {
            System.err.println(">>> MedicoDashboardController.buscarPaciente: medicoLogueado es NULL. No se puede continuar.");
            mostrarAlerta("Error de sesión: No se ha identificado al médico.", Alert.AlertType.ERROR);
            return; // Sale del método
        }
        if (hospitalController == null) {
            System.err.println(">>> MedicoDashboardController.buscarPaciente: hospitalController es NULL. No se puede continuar.");
            mostrarAlerta("Error interno: Controlador principal no disponible.", Alert.AlertType.ERROR);
            return; // Sale del método
        }


        System.out.println("Abriendo diálogo para búsqueda de paciente...");
        TextInputDialog dialog = new TextInputDialog(); // Sin valor por defecto
        dialog.setTitle("Buscar Paciente");
        dialog.setHeaderText("Buscar Historial Clínico de Paciente por Identificador");
        dialog.setContentText("Ingrese ID o Cédula del Paciente:");


        Optional<String> result = dialog.showAndWait();


        if (result.isPresent() && !result.get().isBlank()) {
            String identificador = result.get().trim();
            System.out.println(">>> MedicoDashboardController.buscarPaciente: Identificador ingresado: " + identificador);

            Paciente pacienteBuscado = hospitalController.buscarPacientePorId(identificador);
            if (pacienteBuscado == null) {
                System.out.println(">>> MedicoDashboardController.buscarPaciente: No encontrado por ID, intentando por Cédula...");
                pacienteBuscado = hospitalController.buscarPacientePorCedula(identificador);
            }

            if (pacienteBuscado != null) {
                System.out.println(">>> MedicoDashboardController.buscarPaciente: Paciente encontrado: " + pacienteBuscado.getNombre() + ". Cargando historial...");
                cargarVistaHistorialPaciente(pacienteBuscado); // Llama al método para mostrar el historial
            } else {
                System.out.println(">>> MedicoDashboardController.buscarPaciente: Paciente NO encontrado con identificador: " + identificador);
                mostrarAlerta("Paciente no encontrado con el identificador: " + identificador, Alert.AlertType.WARNING);
            }
        } else {

            System.out.println(">>> MedicoDashboardController.buscarPaciente: Búsqueda cancelada o identificador vacío.");

        }
    }

    // El método cargarVistaHistorialPaciente también es crucial
    private void cargarVistaHistorialPaciente(Paciente paciente) {
        if (contenedorPrincipalMedico == null) {
            System.err.println(">>> MedicoDashboardController.cargarVistaHistorialPaciente: contenedorPrincipalMedico es NULL.");
            mostrarAlerta("Error interno al intentar mostrar el historial.", Alert.AlertType.ERROR);
            return;
        }
        if (paciente == null) {
            System.err.println(">>> MedicoDashboardController.cargarVistaHistorialPaciente: Paciente a mostrar es NULL.");
            mostrarAlerta("Error interno: No se especificó el paciente para ver el historial.", Alert.AlertType.ERROR);
            return;
        }

        try {
            System.out.println(">>> MedicoDashboardController.cargarVistaHistorialPaciente: Cargando FXML para historial de " + paciente.getNombre());
            String fxmlPathHistorial = "/co/edu/uniquindio/sistemagestionhospital/view/HistorialPacienteView.fxml";
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPathHistorial), "No se encontró FXML: " + fxmlPathHistorial));
            Parent vistaHistorial = loader.load();

            HistorialPacienteController controllerHistorial = loader.getController();
            if (controllerHistorial != null) {
                System.out.println(">>> MedicoDashboardController.cargarVistaHistorialPaciente: Pasando paciente al HistorialPacienteController.");
                controllerHistorial.setPaciente(paciente); // Pasa el paciente al controlador del historial
            } else {
                mostrarAlerta("Error al inicializar el controlador del historial del paciente.", Alert.AlertType.ERROR);
                return;
            }

            contenedorPrincipalMedico.getChildren().setAll(vistaHistorial);
            System.out.println(">>> MedicoDashboardController.cargarVistaHistorialPaciente: Vista de historial cargada en contenedor.");
        } catch (Exception e) { // Captura NullPointerException de getResource, IOException de load, etc.
            e.printStackTrace();
            mostrarAlerta("Error al cargar el historial del paciente: " + e.getMessage() +
                    (e.getCause() != null ? "\nCausa: " + e.getCause().getMessage() : ""), Alert.AlertType.ERROR);
        }
    }

    // Método de alerta (asegúrate que esté definido)
    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        // ... (configuración de la alerta) ...
        alert.setTitle(tipo.toString());
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void verMisNotificaciones(ActionEvent event) {
        System.out.println(">>> MedicoDashboardController: verMisNotificaciones() INVOCADO.");

        if (medicoLogueado == null) {
            System.err.println(">>> MedicoDashboardController.verMisNotificaciones: medicoLogueado es NULL.");
            mostrarAlerta("Error de sesión: No se ha identificado al médico.", Alert.AlertType.ERROR);
            return;
        }
        if (medicoControllerPropio == null) {
            System.err.println(">>> MedicoDashboardController.verMisNotificaciones: medicoControllerPropio es NULL.");
            mostrarAlerta("Error interno: Controlador de lógica del médico no disponible.", Alert.AlertType.ERROR);
            return;
        }
        if (contenedorPrincipalMedico == null) {
            System.err.println(">>> MedicoDashboardController.verMisNotificaciones: contenedorPrincipalMedico es NULL.");
            mostrarAlerta("Error interno: El área principal para mostrar contenido no está disponible.", Alert.AlertType.ERROR);
            return;
        }

        System.out.println("Mostrando mis notificaciones para Dr./Dra. " + medicoLogueado.getNombre());

        List<String> notificaciones = medicoControllerPropio.getNotificaciones();
        System.out.println(">>> MedicoDashboardController.verMisNotificaciones: #Notificaciones obtenidas: " + (notificaciones != null ? notificaciones.size() : "null"));

        ListView<String> listaNotificacionesView = new ListView<>();
        if (notificaciones != null && !notificaciones.isEmpty()) {
            listaNotificacionesView.setItems(FXCollections.observableArrayList(notificaciones));
            System.out.println(">>> MedicoDashboardController.verMisNotificaciones: Notificaciones cargadas en ListView.");
        } else {
            listaNotificacionesView.setPlaceholder(new Label("No tiene notificaciones nuevas."));
            System.out.println(">>> MedicoDashboardController.verMisNotificaciones: No hay notificaciones o la lista es nula.");
        }

        VBox layoutNotificaciones = new VBox(10, new Label("Mis Notificaciones"), listaNotificacionesView);
        layoutNotificaciones.setPadding(new Insets(10));

        contenedorPrincipalMedico.getChildren().setAll(layoutNotificaciones); // Actualiza el panel central
        System.out.println(">>> MedicoDashboardController.verMisNotificaciones: Contenedor principal actualizado con la lista de notificaciones.");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        System.out.println(">>> DashboardMedicoController: Cerrando sesión de Dr./Dra. " + (medicoLogueado != null ? medicoLogueado.getNombre() : "No Identificado"));


        this.medicoLogueado = null;
        this.medicoControllerPropio = null;



        Stage stageActual = null;

        if (lblBienvenidaMedico != null && lblBienvenidaMedico.getScene() != null) {
            stageActual = (Stage) lblBienvenidaMedico.getScene().getWindow();
        } else if (contenedorPrincipalMedico != null && contenedorPrincipalMedico.getScene() != null) {
            stageActual = (Stage) contenedorPrincipalMedico.getScene().getWindow();
        } else if (event.getSource() instanceof Node) {

            try {
                stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            } catch (ClassCastException e) {
                System.err.println("Advertencia: event.getSource() no es un Node, no se puede obtener Stage directamente desde él.");
            }
        }


        if (stageActual != null) {
            System.out.println("Cerrando ventana del dashboard del médico.");
            stageActual.close();
        } else {
            System.err.println("Error: No se pudo obtener la ventana actual del dashboard del médico para cerrarla.");

        }

        try {
            System.out.println("Abriendo ventana principal (MainView)...");
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/co/edu/uniquindio/sistemagestionhospital/view/MainView.fxml"),
                    "No se pudo encontrar MainView.fxml"
            ));
            Parent root = loader.load();
            Stage mainStage = new Stage();
            mainStage.setTitle("Sistema de Gestión Hospitalaria");
            mainStage.setScene(new Scene(root));
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error fatal al intentar volver a la ventana principal: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (NullPointerException e) {
            e.printStackTrace();
            mostrarAlerta("Error fatal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


}

