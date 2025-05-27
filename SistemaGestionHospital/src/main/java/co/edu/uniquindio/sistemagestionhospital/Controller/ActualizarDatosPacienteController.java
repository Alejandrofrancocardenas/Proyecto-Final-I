package co.edu.uniquindio.sistemagestionhospital.Controller;


import co.edu.uniquindio.sistemagestionhospital.model.Paciente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ActualizarDatosPacienteController {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField txtNuevaContrasena;
    @FXML
    private PasswordField txtConfirmarContrasena;
    @FXML
    private Label lblMensaje;

    private Paciente pacienteActual;
    private HospitalController hospitalController;

    public void initialize() {
        this.hospitalController = HospitalController.getInstance();
        lblMensaje.setText("");
    }

    public void setPaciente(Paciente paciente) {
        this.pacienteActual = paciente;
        if (pacienteActual != null) {
            txtNombre.setText(pacienteActual.getNombre());
            txtCorreo.setText(pacienteActual.getCorreo());
        } else {
            mostrarAlerta("Error: No se recibió la información del paciente.", Alert.AlertType.ERROR);

            ((Stage) txtNombre.getScene().getWindow()).close();
        }
    }

    @FXML
    private void handleGuardarCambios(ActionEvent event) {
        if (pacienteActual == null) {
            lblMensaje.setText("Error: No hay paciente para actualizar.");
            return;
        }

        String nuevoNombre = txtNombre.getText().trim();
        String nuevoCorreo = txtCorreo.getText().trim();
        String nuevaContrasena = txtNuevaContrasena.getText();
        String confirmarContrasena = txtConfirmarContrasena.getText();

        if (nuevoNombre.isEmpty() || nuevoCorreo.isEmpty()) {
            lblMensaje.setText("Nombre y correo no pueden estar vacíos.");
            return;
        }


        String contrasenaFinal = pacienteActual.getContrasena();
        if (!nuevaContrasena.isEmpty()) {
            if (!nuevaContrasena.equals(confirmarContrasena)) {
                lblMensaje.setText("Las nuevas contraseñas no coinciden.");
                return;
            }
            if (nuevaContrasena.length() < 6) { // Ejemplo de validación de longitud
                lblMensaje.setText("La nueva contraseña debe tener al menos 6 caracteres.");
                return;
            }
            contrasenaFinal = nuevaContrasena;
        }
        boolean exito = hospitalController.modificarPaciente(
                pacienteActual.getId(),
                nuevoNombre,
                nuevoCorreo,
                pacienteActual.getCedula(),
                contrasenaFinal
        );

        if (exito) {

            mostrarAlerta("Datos actualizados exitosamente.", Alert.AlertType.INFORMATION);


            pacienteActual.setNombre(nuevoNombre);
            pacienteActual.setCorreo(nuevoCorreo);
            if (!nuevaContrasena.isEmpty()) {
                pacienteActual.setContrasena(contrasenaFinal);
            }

            cerrarVentana();
        } else {
            lblMensaje.setText("Error al actualizar los datos. Verifique la consola.");
        }
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(tipo == Alert.AlertType.ERROR ? "Error" : "Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}