package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Window;
import javafx.util.StringConverter;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private ComboBox<Medico> comboSeleccionarMedico;

    private HospitalController hospitalController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.hospitalController = HospitalController.getInstance();
        System.out.println(">>> MainController initialize() - HashCode: " + this.hashCode());
        if (comboSeleccionarMedico != null) {
            cargarMedicosEnComboBox();
        } else {
            System.err.println("MainController.initialize(): comboSeleccionarMedico es null. Verifica FXML y @FXML.");
        }
    }

    private void cargarMedicosEnComboBox() {

        if (comboSeleccionarMedico == null) {
            System.err.println("MainController.cargarMedicosEnComboBox(): comboSeleccionarMedico es NULL.");
            return;
        }
        List<Medico> medicos = hospitalController.getMedicos();
        if (medicos != null) {
            comboSeleccionarMedico.setItems(FXCollections.observableArrayList(medicos));
            comboSeleccionarMedico.setConverter(new StringConverter<Medico>() {
                @Override
                public String toString(Medico medico) {
                    return medico != null ? medico.getNombre() + " (ID: " + medico.getId() + ")" : "Seleccione...";
                }
                @Override
                public Medico fromString(String string) { return null; }
            });
            if (medicos.isEmpty()) {
                comboSeleccionarMedico.setPromptText("No hay médicos registrados");
            } else {
                comboSeleccionarMedico.setPromptText("Seleccione un médico");
            }
        } else {
            if(comboSeleccionarMedico != null) {
                comboSeleccionarMedico.setItems(FXCollections.observableArrayList());
                comboSeleccionarMedico.setPromptText("Error al cargar médicos");
            }
        }
    }

    @FXML
    private void abrirVistaAdministrador(ActionEvent event) {
        System.out.println("Botón 'Entrar como Administrador' clickeado.");
        String fxmlPath = "/co/edu/uniquindio/sistemagestionhospital/view/DashboardAdministradorView.fxml";

        Stage adminStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPath), "No FXML: " + fxmlPath));
            Parent root = loader.load();
            adminStage.setTitle("Panel de Administrador");
            adminStage.setScene(new Scene(root));
            Window owner = (event != null && event.getSource() instanceof Node) ? ((Node) event.getSource()).getScene().getWindow() : null;
            if(owner != null) adminStage.initOwner(owner);
            adminStage.initModality(Modality.WINDOW_MODAL);
            adminStage.showAndWait();
            if (comboSeleccionarMedico != null) {
                System.out.println("MainController: Ventana de Administrador cerrada. Refrescando ComboBox de médicos...");
                cargarMedicosEnComboBox();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaError("Error al cargar vista 'Panel de Administrador'. FXML: " + fxmlPath + ". Error: " + e.getMessage());
        }
    }

    @FXML
    private void abrirVistaMedico(ActionEvent event) {
        System.out.println("Botón 'Entrar como Médico' clickeado.");
        if (comboSeleccionarMedico == null) {
            mostrarAlertaError("Error interno: Componente de selección de médico no disponible."); return;
        }
        cargarMedicosEnComboBox();

        Medico medicoSeleccionado = comboSeleccionarMedico.getValue();
        if (medicoSeleccionado == null) {
            mostrarAlertaError("Por favor, seleccione un médico de la lista."); return;
        }
        System.out.println(">>> MainController.abrirVistaMedico: Médico SELECCIONADO: '" + medicoSeleccionado.getNombre() + "' (ID: " + medicoSeleccionado.getId() + ", HashCode: " + medicoSeleccionado.hashCode() + ")");

        cargarVistaGeneral(
                "/co/edu/uniquindio/sistemagestionhospital/view/DashboardMedicoView.fxml", // O DashboardMedicoView.fxml
                "Panel de Médico - " + medicoSeleccionado.getNombre(),
                event, false, medicoSeleccionado, false);
    }


    // --- MÉTODO MODIFICADO PARA NO USAR PACIENTE DE PRUEBA ---
    @FXML
    private void abrirVistaPaciente(ActionEvent event) {
        System.out.println("Botón 'Acceder como Paciente' clickeado.");

        Paciente pacienteParaDashboard = null;

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Acceso Paciente");
        dialog.setHeaderText("Identificación del Paciente");
        dialog.setContentText("Por favor, ingrese su ID o Cédula:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().isBlank()) {
            String identificador = result.get().trim();


            pacienteParaDashboard = this.hospitalController.buscarPacientePorId(identificador);
            if (pacienteParaDashboard == null) {
                pacienteParaDashboard = this.hospitalController.buscarPacientePorCedula(identificador);
            }

            if (pacienteParaDashboard != null) {
                System.out.println("MainController: Paciente encontrado: " + pacienteParaDashboard.getNombre());

                cargarVistaGeneral(
                        "/co/edu/uniquindio/sistemagestionhospital/view/PacienteDashboardView.fxml",
                        "Panel de Paciente - " + pacienteParaDashboard.getNombre(),
                        event,
                        false,
                        pacienteParaDashboard // Se pasa el paciente encontrado
                );
            } else {
                System.out.println("MainController: Paciente con identificador '" + identificador + "' NO encontrado.");
                mostrarAlertaError("Paciente con identificador '" + identificador + "' no encontrado en el sistema.");
                // No se abre el dashboard si no se encontró
            }
        } else {
            System.out.println("MainController: No se ingresó identificador de paciente o se canceló el diálogo.");

        }
    }


    @FXML
    private void abrirRegistroPacienteView(ActionEvent event) {
        System.out.println("Botón 'Registrar Nuevo Paciente' clickeado.");
        cargarVistaGeneral("/co/edu/uniquindio/sistemagestionhospital/view/RegistroPacienteView.fxml",
                "Registro Nuevo Paciente", event, false, null, true); // Modal
    }

    // Método genérico cargarVistaGeneral (como lo tenías, pero asegúrate de los casts)
    private void cargarVistaGeneral(String fxmlPath, String title, ActionEvent event, boolean cerrarVentanaActual, Object dataParaControlador, boolean... esModalOpcional) {
        try {
            System.out.println("MainController: Cargando vista: " + fxmlPath);
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPath), "No FXML: " + fxmlPath));
            Parent root = loader.load();

            Object loadedController = loader.getController();
            if (dataParaControlador != null && loadedController != null) {
                System.out.println("MainController: Intentando pasar datos a: " + loadedController.getClass().getSimpleName());
                if (loadedController instanceof DashboardMedicoController && dataParaControlador instanceof Medico) {
                    ((DashboardMedicoController) loadedController).setMedicoLogueado((Medico) dataParaControlador);
                } else if (loadedController instanceof PacienteDashboardController && dataParaControlador instanceof Paciente) {
                    ((PacienteDashboardController) loadedController).setPacienteLogueado((Paciente) dataParaControlador);
                }

            }

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            boolean esModal = esModalOpcional.length > 0 && esModalOpcional[0];
            if (esModal) {
                Window ventanaPrincipal = (event != null && event.getSource() instanceof Node) ? ((Node) event.getSource()).getScene().getWindow() : null;
                if (ventanaPrincipal != null) stage.initOwner(ventanaPrincipal);
                stage.initModality(Modality.WINDOW_MODAL);
            }

            if (cerrarVentanaActual && event != null && event.getSource() instanceof Node) {
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
                stage.show();
            } else if (esModal && !title.equals("Panel de Administrador")) { // No hacer showAndWait si es admin para que el combo se refresque
                stage.showAndWait();
            } else {
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaError("Error al cargar vista '" + title + "'. FXML: " + fxmlPath + ". Error: " + e.getMessage());
        }
    }

    private void mostrarAlertaError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}