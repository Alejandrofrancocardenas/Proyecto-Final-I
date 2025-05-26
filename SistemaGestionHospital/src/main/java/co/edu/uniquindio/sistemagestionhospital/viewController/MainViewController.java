package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.Controller.DashboardController;
import co.edu.uniquindio.sistemagestionhospital.model.Hospital;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewController {

    @FXML
    private Button btnPacientes;

    @FXML
    private Button btnMedicos;

    @FXML
    private Button btnCerrarSesion;


    @FXML
    private void abrirGestionPacientes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/sistemagestionhospital/view/PacienteView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Gestión de Pacientes");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void gestionarMedicos(ActionEvent event) {
        System.out.println("Abriendo ventana de médicos...");
    }

    @FXML
    void gestionarAdministrador(ActionEvent event) {
        System.out.println("Abriendo ventana de administrador...");
    }

    @FXML
    void cerrarSesion(ActionEvent event) {
        System.out.println("Sesión cerrada");
        System.exit(0); // Cierra la app
    }

}
