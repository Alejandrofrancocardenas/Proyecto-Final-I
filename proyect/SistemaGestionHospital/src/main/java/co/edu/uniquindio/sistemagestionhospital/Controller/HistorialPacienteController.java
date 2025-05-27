package co.edu.uniquindio.sistemagestionhospital.Controller;


import co.edu.uniquindio.sistemagestionhospital.model.Paciente;
import co.edu.uniquindio.sistemagestionhospital.model.EntradaHistorial;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import java.util.List;

public class HistorialPacienteController {

    @FXML
    private TextArea txtHistorial;


    private Paciente paciente;

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;

        if (txtHistorial != null) {
            txtHistorial.clear();
        }
        if (this.paciente != null) {
            cargarHistorial();
        } else {
            if (txtHistorial != null) {
                txtHistorial.setText("No se ha seleccionado ningún paciente.");
            }
        }
    }

    private void cargarHistorial() {

        if (paciente == null) {
            txtHistorial.setText("Error: Paciente no disponible para cargar historial.");
            return;
        }

        List<EntradaHistorial> entradas = paciente.getEntradasDelHistorial();

        if (entradas == null || entradas.isEmpty()) {
            txtHistorial.setText("El paciente no tiene entradas registradas en su historial médico.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Historial Médico de: ").append(paciente.getNombre()).append("\n");
        sb.append("==================================================\n\n");

        for (EntradaHistorial entradaIndividual : entradas) {
            if (entradaIndividual != null) {
                sb.append("Fecha: ").append(entradaIndividual.getFecha() != null ? entradaIndividual.getFecha().toString() : "N/A").append("\n");
                sb.append("Diagnóstico: ").append(entradaIndividual.getDiagnostico() != null ? entradaIndividual.getDiagnostico() : "N/A").append("\n");
                sb.append("Tratamiento: ").append(entradaIndividual.getTratamiento() != null ? entradaIndividual.getTratamiento() : "N/A").append("\n\n");
            }
        }

        txtHistorial.setText(sb.toString());
    }

}