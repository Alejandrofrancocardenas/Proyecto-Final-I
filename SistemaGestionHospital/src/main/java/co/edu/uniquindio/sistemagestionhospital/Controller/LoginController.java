package co.edu.uniquindio.sistemagestionhospital.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private void iniciarSesion() {
        String usuario = txtUsuario.getText();
        String contrasena = txtContrasena.getText();

        // Ejemplo simple de autenticación
        if (usuario.equals("admin") && contrasena.equals("1234")) {
            try {
                // Cargar Dashboard.fxml
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/sistemagestionhospital/Dashboard.fxml"));
                Parent dashboardRoot = fxmlLoader.load();

                // Obtener la ventana actual y cambiar la escena
                Stage stage = (Stage) txtUsuario.getScene().getWindow();
                stage.setScene(new Scene(dashboardRoot));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error al cargar el panel principal.");
            }
        } else {
            mostrarAlerta("Usuario o contraseña incorrectos");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
