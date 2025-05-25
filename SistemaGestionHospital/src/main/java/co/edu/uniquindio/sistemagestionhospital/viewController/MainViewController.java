package co.edu.uniquindio.sistemagestionhospital.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainViewController {

    @FXML
    private Button btnPacientes;

    @FXML
    private Button btnMedicos;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    void gestionarPacientes(ActionEvent event) {
        System.out.println("Abriendo ventana de pacientes...");
        // Aquí puedes cargar otro FXML si deseas
    }

    @FXML
    void gestionarMedicos(ActionEvent event) {
        System.out.println("Abriendo ventana de médicos...");
    }

    @FXML
    void cerrarSesion(ActionEvent event) {
        System.out.println("Sesión cerrada");
        System.exit(0); // Cierra la app
    }
}
