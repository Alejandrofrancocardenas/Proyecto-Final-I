package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.Cita;
import co.edu.uniquindio.sistemagestionhospital.model.EstadoCita;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdministradorDashboardController implements Initializable {

    @FXML
    private StackPane contenedorPrincipalAdmin;

    @FXML
    private Label lblInfoUsuarioAdmin;

    @FXML
    private Label lblFechaHoraAdmin;

    private HospitalController hospitalController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.hospitalController = HospitalController.getInstance();

        configurarActualizacionFechaHora();

        cargarVistaEnContenedor("/co/edu/uniquindio/sistemagestionhospital/view/BienvenidaAdminView.fxml");

    }

    private void configurarActualizacionFechaHora() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            lblFechaHoraAdmin.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    @FXML
    private void irAGestionPacientes(ActionEvent event) {
        System.out.println("Cargando gestión de pacientes...");
        cargarVistaEnContenedor("/co/edu/uniquindio/sistemagestionhospital/view/GestionPacientesView.fxml");
    }

    @FXML
    private void irAGestionMedicos(ActionEvent event) {
        System.out.println("Cargando gestión de médicos...");
        cargarVistaEnContenedor("/co/edu/uniquindio/sistemagestionhospital/view/GestionMedicosView.fxml");
    }

    @FXML
    private void irAGestionCitas(ActionEvent event) {
        System.out.println("Cargando gestión de citas...");
        cargarVistaEnContenedor("/co/edu/uniquindio/sistemagestionhospital/view/GestionCitasView.fxml");
    }

    @FXML
    private void irAGestionSalas(ActionEvent event) {
        System.out.println("Cargando gestión de salas...");

        cargarVistaEnContenedor("/co/edu/uniquindio/sistemagestionhospital/view/GestionSalasView.fxml");
    }

    @FXML
    private void irAGestionHorariosMedicos(ActionEvent event) {
        System.out.println("Cargando gestión de horarios de médicos...");
        cargarVistaEnContenedor("/co/edu/uniquindio/sistemagestionhospital/view/GestionHorariosView.fxml");
    }


    @FXML
    private void verReporteCitas(ActionEvent event) {
        System.out.println("AdministradorDashboardController: Mostrando reporte de todas las citas...");

        if (hospitalController == null) {
            mostrarAlerta("Error: HospitalController no inicializado.", Alert.AlertType.ERROR);
            return;
        }
        if (contenedorPrincipalAdmin == null) {
            mostrarAlerta("Error: Contenedor principal no disponible.", Alert.AlertType.ERROR);
            return;
        }

        List<Cita> todasLasCitas = hospitalController.obtenerTodasLasCitasParaReporte();

        TableView<Cita> tablaReporteCitas = new TableView<>();

        TableColumn<Cita, String> colId = new TableColumn<>("ID Cita");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Cita, LocalDate> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        TableColumn<Cita, LocalTime> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));

        TableColumn<Cita, String> colPaciente = new TableColumn<>("Paciente");
        colPaciente.setCellValueFactory(cellData -> {
            Cita cita = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                    (cita != null && cita.getPaciente() != null) ? cita.getPaciente().getNombre() : "N/A"
            );
        });
        colPaciente.setPrefWidth(150);


        TableColumn<Cita, String> colMedico = new TableColumn<>("Médico");
        colMedico.setCellValueFactory(cellData -> {
            Cita cita = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                    (cita != null && cita.getMedico() != null) ? cita.getMedico().getNombre() : "N/A"
            );
        });
        colMedico.setPrefWidth(150);

        TableColumn<Cita, String> colEspecialidad = new TableColumn<>("Especialidad");
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        colEspecialidad.setPrefWidth(120);


        TableColumn<Cita, EstadoCita> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        TableColumn<Cita, String> colMotivo = new TableColumn<>("Motivo");
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colMotivo.setPrefWidth(200);


        tablaReporteCitas.getColumns().addAll(colId, colFecha, colHora, colPaciente, colMedico, colEspecialidad, colEstado, colMotivo);
        tablaReporteCitas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        if (todasLasCitas != null && !todasLasCitas.isEmpty()) {
            tablaReporteCitas.setItems(FXCollections.observableArrayList(todasLasCitas));
        } else {
            tablaReporteCitas.setPlaceholder(new Label("No hay citas registradas en el sistema para mostrar."));
        }

        VBox layoutReporte = new VBox(10, new Label("Reporte General de Citas"), tablaReporteCitas);
        layoutReporte.setPadding(new Insets(10));
        contenedorPrincipalAdmin.getChildren().setAll(layoutReporte);
    }

    @FXML
    private void verEstadisticas(ActionEvent event) {
        System.out.println("Mostrando estadísticas...");
        mostrarAlerta("Funcionalidad de Estadísticas en desarrollo.", Alert.AlertType.INFORMATION);
    }


    @FXML
    private void abrirConfiguracion(ActionEvent event) {
        System.out.println("Abriendo configuración del sistema...");
        mostrarAlerta("Funcionalidad de Configuración en desarrollo.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        System.out.println("Cerrando sesión de administrador...");
        Stage stage = (Stage) lblInfoUsuarioAdmin.getScene().getWindow();
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


    private void cargarVistaEnContenedor(String fxmlPath) {
        try {
            if (fxmlPath == null || fxmlPath.trim().isEmpty()){
                contenedorPrincipalAdmin.getChildren().setAll(new Label("Ruta de FXML no especificada."));
                return;
            }

            contenedorPrincipalAdmin.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPath), "No se encontró el FXML: " + fxmlPath));
            Parent vista = loader.load();
            contenedorPrincipalAdmin.getChildren().add(vista);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            Label lblError = new Label("Error al cargar la vista: " + fxmlPath + "\n" + e.getMessage());
            contenedorPrincipalAdmin.getChildren().setAll(lblError);

        }
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(tipo == Alert.AlertType.ERROR ? "Error" : "Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}