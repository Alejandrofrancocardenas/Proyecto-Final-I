package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.Controller.PacienteController;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.Optional;

public class EditarPacienteViewController {

    @FXML
    private Label lblId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtCedula;
    @FXML
    private PasswordField pwdNuevaContrasena;
    @FXML
    private PasswordField pwdConfirmarNuevaContrasena;

    private PacienteController pacienteController;
    private Paciente pacienteActual;

    public EditarPacienteViewController() {
        this.pacienteController = new PacienteController();
    }
    public void setPaciente(Paciente paciente) {
        this.pacienteActual = paciente;
        if (pacienteActual != null) {
            lblId.setText(pacienteActual.getId());
            txtNombre.setText(pacienteActual.getNombre());
            txtCorreo.setText(pacienteActual.getCorreo());
            txtCedula.setText(pacienteActual.getCedula());
        }
    }

    @FXML
    private void guardarCambios(ActionEvent event) {
        if (pacienteActual == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se ha cargado ningún paciente.");
            return;
        }

        String nuevoNombre = txtNombre.getText();
        String nuevoCorreo = txtCorreo.getText();
        String nuevaContrasena = pwdNuevaContrasena.getText();
        String confirmarNuevaContrasena = pwdConfirmarNuevaContrasena.getText();
        String nuevaCedula = txtCedula.getText();

        if (nuevoNombre.isEmpty() || nuevoCorreo.isEmpty() || nuevaCedula.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", "El nombre, correo y cédula no pueden estar vacíos.");
            return;
        }

        String contrasenaAUsar = pacienteActual.getContrasena();
        if (!nuevaContrasena.isEmpty()) {
            if (!nuevaContrasena.equals(confirmarNuevaContrasena)) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Contraseña", "Las nuevas contraseñas no coinciden.");
                return;
            }
            contrasenaAUsar = nuevaContrasena; // Usar la nueva contraseña hasheada
        }

        // Llamar al método de actualización en el controlador
        boolean actualizado = pacienteController.actualizarDatos(
                pacienteActual.getId(), nuevoNombre, nuevoCorreo, contrasenaAUsar, nuevaCedula);

        if (actualizado) {

            pacienteActual.setNombre(nuevoNombre);
            pacienteActual.setCorreo(nuevoCorreo);
            pacienteActual.setCedula(nuevaCedula);
            if (!nuevaContrasena.isEmpty()) {
                pacienteActual.setContrasena(contrasenaAUsar);
            }

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Datos actualizados correctamente.");
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron actualizar los datos.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}