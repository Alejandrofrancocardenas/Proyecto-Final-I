package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.Medico;
import co.edu.uniquindio.sistemagestionhospital.viewController.DiagnosticoViewController;
import co.edu.uniquindio.sistemagestionhospital.viewController.HorarioViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardMedicoController {
    @FXML
    private void abrirDiagnosticoView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/sistemagestionhospital/view/DiagnosticoView.fxml"));
            Parent root = loader.load();

            DiagnosticoViewController controller = loader.getController();
            controller.setMedico(medicoActual); // médico previamente autenticado

            Stage stage = new Stage();
            stage.setTitle("Registrar Diagnóstico");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Medico medicoActual;

    public void setMedico(Medico medico) {
        this.medicoActual = medico;
    }
    @FXML
    private void abrirHorarioView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/sistemagestionhospital/view/HorarioView.fxml"));
            Parent root = loader.load();

            HorarioViewController controller = loader.getController();
            controller.setMedico(medicoActual);

            Stage stage = new Stage();
            stage.setTitle("Gestión de Horarios");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
