package co.edu.uniquindio.sistemagestionhospital.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MedicoTest {

    private Medico medico;
    private Paciente pacientePrueba;

    @BeforeEach
    void setUp() {

        medico = new Medico("MED_Test" + System.nanoTime(), "Dr. Gregory House", "house@example.com", "lupus", "Diagnóstico");
        pacientePrueba = new Paciente("PAC_Test" + System.nanoTime(), "John Doe", "john.doe@email.com", "password", "123456");
    }

    @Nested
    @DisplayName("Pruebas para Gestión de Horarios")
    class GestionHorariosTests {
        @Test
        @DisplayName("agregarHorario válido debería retornar true y añadirlo a la lista")
        void agregarHorario_valido_retornaTrueYAñade() {
            HorarioAtencion horario = new HorarioAtencion("H001", DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0));
            assertTrue(medico.agregarHorario(horario), "Debería poder agregar un horario válido.");
            assertEquals(1, medico.getHorarios().size(), "La lista de horarios debería tener 1 elemento.");
            assertTrue(medico.getHorarios().contains(horario), "El horario agregado debería estar en la lista.");
        }

        @Test
        @DisplayName("agregarHorario nulo debería retornar false")
        void agregarHorario_nulo_retornaFalse() {
            assertFalse(medico.agregarHorario(null));
            assertTrue(medico.getHorarios().isEmpty());
        }

        @Test
        @DisplayName("agregarHorario con día o hora de inicio nulos debería retornar false")
        void agregarHorario_diaOHoraInicioNulos_retornaFalse() {
            HorarioAtencion horarioSinDia = new HorarioAtencion("H002_A", null, LocalTime.of(9,0), LocalTime.of(10,0));
            HorarioAtencion horarioSinInicio = new HorarioAtencion("H002_B", DayOfWeek.TUESDAY, null, LocalTime.of(10,0));
            assertFalse(medico.agregarHorario(horarioSinDia), "No debería agregar horario sin día.");
            assertFalse(medico.agregarHorario(horarioSinInicio), "No debería agregar horario sin hora de inicio.");
            assertTrue(medico.getHorarios().isEmpty());
        }

        @Test
        @DisplayName("agregarHorario duplicado exacto (mismo día y hora inicio) debería retornar false")
        void agregarHorario_duplicadoExacto_retornaFalse() {
            HorarioAtencion horario1 = new HorarioAtencion("H003", DayOfWeek.WEDNESDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
            medico.agregarHorario(horario1);
            HorarioAtencion horario2_duplicado = new HorarioAtencion("H004", DayOfWeek.WEDNESDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
            assertFalse(medico.agregarHorario(horario2_duplicado), "No debería agregar un horario duplicado (mismo día y hora inicio).");
            assertEquals(1, medico.getHorarios().size());
        }

        @Test
        @DisplayName("eliminarHorario existente por ID debería retornar true y removerlo")
        void eliminarHorario_existentePorId_retornaTrueYRemueve() {
            HorarioAtencion horario = new HorarioAtencion("H_DEL001", DayOfWeek.THURSDAY, LocalTime.of(14, 0), LocalTime.of(16, 0));
            medico.agregarHorario(horario);
            assertEquals(1, medico.getHorarios().size());

            assertTrue(medico.eliminarHorario("H_DEL001"));
            assertTrue(medico.getHorarios().isEmpty());
        }

        @Test
        @DisplayName("eliminarHorario no existente por ID debería retornar false")
        void eliminarHorario_noExistentePorId_retornaFalse() {
            assertFalse(medico.eliminarHorario("ID_FALSO_HORARIO"));
        }

        @Test
        @DisplayName("eliminarHorario con ID nulo o vacío debería retornar false")
        void eliminarHorario_idNuloOVacio_retornaFalse() {
            assertFalse(medico.eliminarHorario(null));
            assertFalse(medico.eliminarHorario("   "));
        }
    }

    @Nested
    @DisplayName("Pruebas para Gestión de Citas del Médico")
    class GestionCitasMedicoTests {

        @Test
        @DisplayName("agregarCita válida debería retornar true y añadirla a citasAsignadas")
        void agregarCita_valida_retornaTrueYAñade() {
            Cita cita = new Cita("C_MED001", LocalDate.now().plusDays(1), LocalTime.of(10,0), "Consulta General", pacientePrueba, medico, medico.getEspecialidad(), EstadoCita.AGENDADA);
            assertTrue(medico.agregarCita(cita), "Debería poder agregar una cita válida.");
            assertEquals(1, medico.getCitas().size());
            assertTrue(medico.getCitas().stream().anyMatch(c -> c.getId().equals("C_MED001")));
        }

        @Test
        @DisplayName("agregarCita nula debería retornar false")
        void agregarCita_nula_retornaFalse() {
            assertFalse(medico.agregarCita(null));
            assertTrue(medico.getCitas().isEmpty());
        }

        @Test
        @DisplayName("agregarCita con ID duplicado debería retornar false")
        void agregarCita_idDuplicado_retornaFalse() {
            Cita cita1 = new Cita("C_MED002", LocalDate.now().plusDays(2), LocalTime.of(11,0), "Revisión", pacientePrueba, medico, medico.getEspecialidad(), EstadoCita.AGENDADA);
            medico.agregarCita(cita1);
            Cita cita2_mismoId = new Cita("C_MED002", LocalDate.now().plusDays(3), LocalTime.of(12,0), "Otra Consulta", pacientePrueba, medico, medico.getEspecialidad(), EstadoCita.AGENDADA);
            assertFalse(medico.agregarCita(cita2_mismoId), "No debería agregar cita con ID duplicado a la lista del médico.");
            assertEquals(1, medico.getCitas().size());
        }


        @Test
        @DisplayName("cancelarCita existente por ID debería retornar true y removerla")
        void cancelarCita_existentePorId_retornaTrueYRemueve() {
            Cita cita = new Cita("C_MED_DEL001", LocalDate.now().plusDays(4), LocalTime.of(9,30), "Control", pacientePrueba, medico, medico.getEspecialidad(), EstadoCita.AGENDADA);
            medico.agregarCita(cita);
            assertEquals(1, medico.getCitas().size());

            assertTrue(medico.cancelarCita("C_MED_DEL001"), "Cancelar cita existente por ID debería retornar true.");
            assertTrue(medico.getCitas().isEmpty(), "La lista de citas del médico debería estar vacía después de cancelar.");
        }

        @Test
        @DisplayName("cancelarCita no existente por ID debería retornar false")
        void cancelarCita_noExistentePorId_retornaFalse() {
            assertFalse(medico.cancelarCita("ID_CITA_FALSA"));
        }
    }

    @Nested
    @DisplayName("Pruebas para Notificaciones del Médico")
    class NotificacionesMedicoTests {
        @Test
        @DisplayName("recibirNotificacion debería agregar una notificación")
        void recibirNotificacion_valida_agregaNotificacion() {
            String mensaje = "Nueva cita asignada para mañana.";
            medico.recibirNotificacion(mensaje);

            assertFalse(medico.getNotificacionesCompletas().isEmpty(), "La lista de notificaciones completas no debería estar vacía.");
            assertEquals(1, medico.getNotificacionesCompletas().size());
            assertEquals(mensaje, medico.getNotificacionesCompletas().get(0).getMensaje());

            assertFalse(medico.getNotificaciones().isEmpty(), "La lista de mensajes de notificación no debería estar vacía.");
            assertEquals(mensaje, medico.getNotificaciones().get(0));
        }

        @Test
        @DisplayName("recibirNotificacion con mensaje nulo no debería agregar nada")
        void recibirNotificacion_nulaOVacia_noAgrega() {
            medico.recibirNotificacion(null);
            assertTrue(medico.getNotificacionesCompletas().isEmpty());
            medico.recibirNotificacion("");
            assertTrue(medico.getNotificacionesCompletas().isEmpty());
        }
    }

    @Nested
    @DisplayName("Pruebas para equals y hashCode de Medico")
    class EqualsHashCodeMedicoTests {
        @Test
        @DisplayName("Dos médicos con mismo ID de Usuario son iguales (asumiendo super.equals() se basa en ID)")
        void testEquals_mismoIdUsuario_sonIguales() {

            Medico medico1 = new Medico("ID_MED_EQ01", "Dr. Alfa", "alfa@h.com", "pass", "Cardiología");
            Medico medico2 = new Medico("ID_MED_EQ01", "Dr. Alfa", "alfa@h.com", "pass", "Cardiología");
            assertEquals(medico1, medico2);
            assertEquals(medico1.hashCode(), medico2.hashCode());
        }

        @Test
        @DisplayName("Dos médicos con mismo ID de Usuario pero diferente especialidad son diferentes")
        void testEquals_mismoIdUsuarioDiferenteEspecialidad_sonDiferentes() {
            Medico medico1 = new Medico("ID_MED_EQ02", "Dr. Beta", "beta@h.com", "pass", "Cardiología");
            Medico medico2 = new Medico("ID_MED_EQ02", "Dr. Beta", "beta@h.com", "pass", "Pediatría");
            assertNotEquals(medico1, medico2);
        }

        @Test
        @DisplayName("Dos médicos con diferente ID de Usuario (misma especialidad) son diferentes")
        void testEquals_diferenteIdUsuarioMismaEspecialidad_sonDiferentes() {
            Medico medico1 = new Medico("ID_MED_EQ03", "Dr. Gama", "gama@h.com", "pass", "Oncología");
            Medico medico2 = new Medico("ID_MED_EQ04", "Dr. Gama", "gama@h.com", "pass", "Oncología");
            assertNotEquals(medico1, medico2);
        }
    }

    @Nested
    @DisplayName("Pruebas para Historial Médico (delegación)")
    class HistorialMedicoDelegacionTests {
        @Test
        @DisplayName("agregarEntradaHistorial debería llamar a agregarEntrada en el historial del paciente")
        void agregarEntradaHistorial_delegaCorrectamente() {
            Cita cita = new Cita("C_HIST01", LocalDate.now(), LocalTime.NOON, "Chequeo", pacientePrueba, medico, "General", EstadoCita.COMPLETADA);
            // El paciente ya tiene un historialMedico vacío por defecto.
            assertTrue(medico.agregarEntradaHistorial(cita, "Sano", "Ninguno"));
            assertEquals(1, pacientePrueba.getHistorialMedico().getEntradas().size());
            assertEquals("Sano", pacientePrueba.getHistorialMedico().getEntradas().get(0).getDiagnostico());
        }

        @Test
        @DisplayName("agregarEntradaHistorial con cita o paciente nulo debería fallar")
        void agregarEntradaHistorial_datosNulos_retornaFalse() {
            assertFalse(medico.agregarEntradaHistorial(null, "Diagnóstico", "Tratamiento"));

            Cita citaSinPaciente = new Cita("C_HIST02", LocalDate.now(), LocalTime.NOON, "Motivo", null, medico, "General", EstadoCita.COMPLETADA);
            assertFalse(medico.agregarEntradaHistorial(citaSinPaciente, "Diagnóstico", "Tratamiento"));
        }
    }


    @Test
    void testHashCode_consistencia() {
        Medico m1 = new Medico("IDH1", "Medico Hash", "mh@test.com", "p", "Especial");
        int hc1 = m1.hashCode();
        int hc2 = m1.hashCode();
        assertEquals(hc1, hc2);
    }
}