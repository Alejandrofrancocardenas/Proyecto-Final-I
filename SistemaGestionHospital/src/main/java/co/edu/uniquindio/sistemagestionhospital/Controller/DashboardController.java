package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.Hospital;
import co.edu.uniquindio.sistemagestionhospital.viewController.CitaViewController;
import co.edu.uniquindio.sistemagestionhospital.viewController.PacienteViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    private Hospital hospital;


    @FXML
    private void gestionarPacientes(ActionEvent event) {
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
    private void abrirHistorialMedico() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HistorialView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Historial Médico");
            stage.setScene(new Scene(root));
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

}
