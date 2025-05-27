package co.edu.uniquindio.sistemagestionhospital.viewController;

import co.edu.uniquindio.sistemagestionhospital.Controller.HospitalController;
import co.edu.uniquindio.sistemagestionhospital.model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class CitaViewController implements Initializable {

    @FXML
    private ComboBox<Medico> cbMedico;

    @FXML
    private ComboBox<Paciente> cbPaciente;

    @FXML
    private TextField txtFecha;

    @FXML
    private TextField txtHora;

    @FXML
    private ListView<Cita> listaCitas;
    @FXML
    private TextField txtMotivo;

    @FXML
    private TextField txtEspecialidad;
    
    private final Hospital hospital = Hospital.getInstance();
    private Paciente paciente;
    private Medico medicoActual;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarPacientesYMedicos();

        cbPaciente.setConverter(new StringConverter<>() {
            @Override
            public String toString(Paciente paciente) {
                return paciente != null ? paciente.getNombre() + " (" + paciente.getId() + ")" : "";
            }

            @Override
            public Paciente fromString(String string) {
                return null;
            }
        });

        cbMedico.setConverter(new StringConverter<>() {
            @Override
            public String toString(Medico medico) {
                return medico != null ? medico.getNombre() + " (" + medico.getId() + ")" : "";
            }

            @Override
            public Medico fromString(String string) {
                return null;
            }
        });

        cbPaciente.setOnAction(event -> {
            Paciente seleccionado = cbPaciente.getValue();
            if (seleccionado != null) {
                this.paciente = seleccionado;
                actualizarListaCitas();
            }
        });
    }

    private void cargarPacientesYMedicos() {
        cbPaciente.setItems(FXCollections.observableArrayList(hospital.getListaPacientes()));
        cbMedico.setItems(FXCollections.observableArrayList(hospital.getListaMedicos()));
    }
    private void actualizarListaCitas() {

        if (listaCitas == null) {
            System.err.println("Error: listaCitas no está inicializada.");
            return;
        }
        listaCitas.getItems().clear();

        if (paciente != null) {
            List<Cita> citasDelPaciente = paciente.getCitasProgramadas(); // <<< CAMBIO AQUÍ

            if (citasDelPaciente != null) {
                citasDelPaciente.stream()
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparing(Cita::getFecha, Comparator.nullsLast(Comparator.naturalOrder()))
                                .thenComparing(Cita::getHora, Comparator.nullsLast(Comparator.naturalOrder())))
                        .forEach(listaCitas.getItems()::add);
            }
        }
    }
    @FXML
    private void agendarCita() {

        Medico medicoSeleccionado = cbMedico.getValue();
        String fechaTexto = txtFecha.getText();
        String horaTexto = txtHora.getText();
        String motivo = txtMotivo.getText();
        String especialidad = txtEspecialidad.getText();

        if (paciente == null) {
            mostrarMensaje("No hay un paciente seleccionado para agendar la cita.", true);
            return;
        }
        if (medicoSeleccionado == null) {
            mostrarMensaje("Por favor, seleccione un médico.", true);
            return;
        }
        if (fechaTexto.isBlank() || horaTexto.isBlank() || motivo.isBlank()) {
            // Considera si la especialidad puede ser opcional o si se obtiene del médico
            mostrarMensaje("Por favor, complete todos los campos obligatorios: fecha, hora y motivo.", true);
            return;
        }


        try {
            LocalDate fecha = LocalDate.parse(fechaTexto);
            LocalTime hora = LocalTime.parse(horaTexto);


            String idCita = "CITA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            EstadoCita estadoInicial = EstadoCita.AGENDADA;

            Cita nuevaCita = new Cita(
                    idCita,
                    fecha,
                    hora,
                    motivo,
                    paciente,
                    medicoSeleccionado,
                    especialidad,
                    estadoInicial
            );
            if (paciente.agregarCita(nuevaCita)) {

                mostrarMensaje("Cita agendada exitosamente con ID: " + nuevaCita.getId(), false);
                actualizarListaCitas();
                limpiarCamposDeCita();
            } else {

                mostrarMensaje("No se pudo agregar la cita a la lista del paciente (posible duplicado para este paciente).", true);
            }
        } catch (DateTimeParseException e) {
            mostrarMensaje("Formato incorrecto de fecha u hora. Use AAAA-MM-DD y HH:mm", true);
        } catch (Exception e) {
            mostrarMensaje("Ocurrió un error inesperado al agendar la cita: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }
    private void limpiarCamposDeCita() {

        txtFecha.clear();
        txtHora.clear();
        txtMotivo.clear();
        txtEspecialidad.clear();
        txtFecha.requestFocus();
    }




        private Paciente pacienteActual;
        private Cita citaActualOSeleccionada;
        private HospitalController hospitalController;


        public void initData(Paciente paciente, Cita cita) {
            this.pacienteActual = paciente;
            this.citaActualOSeleccionada = cita;
            this.hospitalController = HospitalController.getInstance();

        }

        @FXML
        private void handleCancelarCitaAction(ActionEvent event) {
            if (pacienteActual == null) {
                mostrarAlerta("Error: No se ha identificado al paciente.", Alert.AlertType.ERROR);
                return;
            }
            if (citaActualOSeleccionada == null) {
                mostrarAlerta("Error: No hay una cita seleccionada para cancelar.", Alert.AlertType.ERROR);
                return;
            }
            if (citaActualOSeleccionada.getId() == null) {
                mostrarAlerta("Error: La cita no tiene un ID válido para cancelar.", Alert.AlertType.ERROR);
                return;
            }

            // Lógica de confirmación (opcional pero recomendada)
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Cancelación");
            confirmacion.setHeaderText("¿Está seguro de que desea cancelar esta cita?");
            confirmacion.setContentText("Cita ID: " + citaActualOSeleccionada.getId() +
                    "\nFecha: " + citaActualOSeleccionada.getFecha() +
                    "\nHora: " + citaActualOSeleccionada.getHora());

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {


                boolean canceladaPorPaciente = pacienteActual.cancelarCita(citaActualOSeleccionada.getId());


                if (canceladaPorPaciente) {


                    if (hospitalController.cancelarCitaYNotificar(citaActualOSeleccionada.getId())) {
                        mostrarAlerta("Cita cancelada exitosamente y sistema notificado.", Alert.AlertType.INFORMATION);

                    } else {
                        mostrarAlerta("Cita cancelada de su lista, pero hubo un problema al procesar la cancelación en el sistema.", Alert.AlertType.WARNING);
                    }

                } else {
                    mostrarAlerta("No se pudo cancelar la cita de su lista (posiblemente ya no existía o el estado no lo permitía).", Alert.AlertType.ERROR);
                }
            }
        }
        private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
            Alert alert = new Alert(tipo);
            alert.setTitle(tipo.toString());
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        }

        private void mostrarMensaje(String mensaje, boolean esError) {
        Alert alert = new Alert(esError ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(esError ? "Error" : "Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    public void setMedico(Medico medico) {
        this.medicoActual = medico;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        cbPaciente.setValue(paciente); // para que se muestre en el ComboBox
        actualizarListaCitas();
    }

    private Medico obtenerMedicoActual() {
        return this.medicoActual;
    }
}
