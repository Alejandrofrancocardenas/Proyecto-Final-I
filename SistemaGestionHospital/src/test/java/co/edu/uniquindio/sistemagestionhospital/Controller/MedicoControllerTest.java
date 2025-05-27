package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MedicoControllerTest {

    private Medico medicoDePrueba;
    private MedicoController medicoController;
    private Paciente pacienteDePrueba; // Para pruebas de historial

    @BeforeEach
    void setUp() {
        // Crear un nuevo médico para cada prueba para asegurar aislamiento
        medicoDePrueba = new Medico("MED_CTRL_Test" + System.nanoTime(), "Dr. Controlador", "ctrl@test.com", "ctrlPass", "Controlología");
        medicoController = new MedicoController(medicoDePrueba);

        pacienteDePrueba = new Paciente("PAC_CTRL_Test" + System.nanoTime(), "Paciente Hist", "ph@test.com", "pacPass", "777123");
        // Asegurar que el paciente tiene un HistorialMedico (el constructor de Paciente debería hacerlo)
        assertNotNull(pacienteDePrueba.getHistorialMedico());
    }

    @Nested
    @DisplayName("Pruebas para agregarEntradaHistorial en MedicoController")
    class AgregarEntradaHistorialTests {

        @Test
        @DisplayName("Debería agregar entrada al historial del paciente a través del médico")
        void agregarEntradaHistorial_valido_retornaTrueYAgregaEntrada() {
            Cita cita = new Cita("C_H_001", LocalDate.now(), LocalTime.NOON, "Consulta",
                    pacienteDePrueba, medicoDePrueba, "Controlología", EstadoCita.COMPLETADA);
            String diagnostico = "Saludable";
            String tratamiento = "Continuar rutina";

            boolean resultado = medicoController.agregarEntradaHistorial(cita, diagnostico, tratamiento);

            assertTrue(resultado, "El registro de historial debería ser exitoso.");
            List<EntradaHistorial> entradas = pacienteDePrueba.getHistorialMedico().getEntradas();
            assertFalse(entradas.isEmpty(), "El historial del paciente no debería estar vacío.");
            assertEquals(1, entradas.size());
            assertEquals(diagnostico, entradas.get(0).getDiagnostico());
            assertEquals(tratamiento, entradas.get(0).getTratamiento());
            assertEquals(cita.getFecha(), entradas.get(0).getFecha());
        }

        @Test
        @DisplayName("Debería retornar false si la cita es nula")
        void agregarEntradaHistorial_citaNull_retornaFalse() {
            boolean resultado = medicoController.agregarEntradaHistorial(null, "D", "T");
            assertFalse(resultado);
        }

        @Test
        @DisplayName("Debería retornar false si el paciente en la cita es nulo")
        void agregarEntradaHistorial_pacienteEnCitaNull_retornaFalse() {
            Cita citaSinPaciente = new Cita("C_H_002", LocalDate.now(), LocalTime.NOON, "Motivo",
                    null, medicoDePrueba, "General", EstadoCita.COMPLETADA);
            boolean resultado = medicoController.agregarEntradaHistorial(citaSinPaciente, "D", "T");
            assertFalse(resultado);
        }
    }

    @Nested
    @DisplayName("Pruebas para agregarHorario en MedicoController")
    class AgregarHorarioTests {

        @Test
        @DisplayName("Debería agregar un horario válido al médico")
        void agregarHorario_valido_retornaTrue() {
            HorarioAtencion horario = new HorarioAtencion("HOR_CTRL_01", DayOfWeek.MONDAY, LocalTime.of(9,0), LocalTime.of(13,0));
            boolean resultado = medicoController.agregarHorario(horario);
            assertTrue(resultado);
            assertEquals(1, medicoDePrueba.getHorarios().size());
            assertTrue(medicoDePrueba.getHorarios().contains(horario));
        }

        @Test
        @DisplayName("No debería agregar un horario duplicado (misma hora inicio y día)")
        void agregarHorario_duplicado_retornaFalse() {
            HorarioAtencion horario1 = new HorarioAtencion("HOR_CTRL_02A", DayOfWeek.TUESDAY, LocalTime.of(10,0), LocalTime.of(12,0));
            medicoController.agregarHorario(horario1);
            HorarioAtencion horario2 = new HorarioAtencion("HOR_CTRL_02B", DayOfWeek.TUESDAY, LocalTime.of(10,0), LocalTime.of(14,0));
            boolean resultado = medicoController.agregarHorario(horario2); // Intenta agregar duplicado
            assertFalse(resultado);
            assertEquals(1, medicoDePrueba.getHorarios().size());
        }
    }

    @Nested
    @DisplayName("Pruebas para eliminarHorario en MedicoController")
    class EliminarHorarioTests {

        @Test
        @DisplayName("Debería eliminar un horario existente por ID")
        void eliminarHorario_existente_retornaTrue() {
            HorarioAtencion horario = new HorarioAtencion("HOR_CTRL_DEL01", DayOfWeek.WEDNESDAY, LocalTime.of(14,0), LocalTime.of(17,0));
            medicoController.agregarHorario(horario);
            assertFalse(medicoDePrueba.getHorarios().isEmpty());

            boolean resultado = medicoController.eliminarHorario("HOR_CTRL_DEL01");
            assertTrue(resultado);
            assertTrue(medicoDePrueba.getHorarios().isEmpty());
        }

        @Test
        @DisplayName("Debería retornar false si el ID del horario no existe")
        void eliminarHorario_noExistente_retornaFalse() {
            boolean resultado = medicoController.eliminarHorario("ID_FALSO_HORARIO");
            assertFalse(resultado);
        }
    }

    // Ejemplo de prueba para getCitas (asumiendo que MedicoController la tiene)
    @Test
    @DisplayName("getCitas debería devolver la lista de citas del médico")
    void getCitas_devuelveListaCorrecta() {
        Cita cita1 = new Cita("C_CTRL_1", LocalDate.now(), LocalTime.of(9,0), "M1", pacienteDePrueba, medicoDePrueba, "E1", EstadoCita.AGENDADA);
        Cita cita2 = new Cita("C_CTRL_2", LocalDate.now(), LocalTime.of(10,0), "M2", pacienteDePrueba, medicoDePrueba, "E2", EstadoCita.AGENDADA);
        medicoDePrueba.agregarCita(cita1); // Agregamos directamente al modelo para preparar
        medicoDePrueba.agregarCita(cita2);

        List<Cita> citasObtenidas = medicoController.getCitas();
        assertNotNull(citasObtenidas);
        assertEquals(2, citasObtenidas.size());
        assertTrue(citasObtenidas.stream().anyMatch(c -> c.getId().equals("C_CTRL_1")));
        assertTrue(citasObtenidas.stream().anyMatch(c -> c.getId().equals("C_CTRL_2")));
    }
}