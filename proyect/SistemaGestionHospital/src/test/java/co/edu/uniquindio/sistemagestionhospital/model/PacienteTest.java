package co.edu.uniquindio.sistemagestionhospital.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PacienteTest {

    private Paciente paciente;
    private Medico medicoPrueba;
    private HistorialMedico historialMedicoDelPaciente;

    @BeforeEach
    void setUp() {

        paciente = new Paciente("PAC_Test" + System.nanoTime(), "Carlos Prueba", "carlos.prueba@email.com", "testPass123", "1099999000");
        medicoPrueba = new Medico("MED_Test" + System.nanoTime(), "Dra. Ana Ejemplo", "ana.ejemplo@email.com", "docPass456", "General");

        historialMedicoDelPaciente = paciente.getHistorialMedico();
        assertNotNull(historialMedicoDelPaciente, "El paciente debería tener un historial médico inicializado.");
    }

    @Nested
    @DisplayName("Pruebas para Historial Médico del Paciente")
    class HistorialMedicoPacienteTests {

        @Test
        @DisplayName("getEntradasDelHistorial con historial vacío debería devolver lista vacía")
        void getEntradasDelHistorial_historialVacio_devuelveListaVacia() {
            List<EntradaHistorial> entradas = paciente.getEntradasDelHistorial();
            assertNotNull(entradas, "La lista de entradas no debería ser nula.");
            assertTrue(entradas.isEmpty(), "La lista de entradas debería estar vacía para un historial nuevo.");
        }

        @Test
        @DisplayName("getEntradasDelHistorial después de agregar entradas")
        void getEntradasDelHistorial_conEntradas_devuelveEntradasCorrectas() {

            LocalDate fechaEntrada1 = LocalDate.now().minusDays(5);
            String diagnostico1 = "Gripe común";
            String tratamiento1 = "Reposo y líquidos";
            historialMedicoDelPaciente.agregarEntrada(fechaEntrada1, diagnostico1, tratamiento1);

            LocalDate fechaEntrada2 = LocalDate.now().minusDays(2);
            String diagnostico2 = "Revisión";
            String tratamiento2 = "Continuar tratamiento";
            historialMedicoDelPaciente.agregarEntrada(fechaEntrada2, diagnostico2, tratamiento2);

            List<EntradaHistorial> entradas = paciente.getEntradasDelHistorial();
            assertNotNull(entradas);
            assertEquals(2, entradas.size(), "Debería haber dos entradas en el historial.");
            assertEquals(diagnostico1, entradas.get(0).getDiagnostico());
            assertEquals(fechaEntrada2, entradas.get(1).getFecha());
        }

        @Test
        @DisplayName("getEntradasDelHistorial si historialMedico es null en Paciente (caso extremo, no debería pasar con el constructor actual)")
        void getEntradasDelHistorial_historialMedicoNull_devuelveListaVacia() {
            paciente.setHistorialMedico(null); // Forzar el caso (no recomendado en diseño normal)
            List<EntradaHistorial> entradas = paciente.getEntradasDelHistorial();
            assertNotNull(entradas);
            assertTrue(entradas.isEmpty());
        }
    }

    @Nested
    @DisplayName("Pruebas para Notificaciones del Paciente")
    class NotificacionesPacienteTests {
        @Test
        @DisplayName("recibirNotificacion debería agregar una notificación a la lista")
        void recibirNotificacion_mensajeValido_agregaNotificacion() {
            String mensaje = "Su cita ha sido confirmada.";
            paciente.recibirNotificacion(mensaje);
            List<Notificacion> notificaciones = paciente.getNotificaciones();
            assertNotNull(notificaciones);
            assertEquals(1, notificaciones.size(), "Debería haber una notificación.");
            assertEquals(mensaje, notificaciones.get(0).getMensaje(), "El mensaje de la notificación no coincide.");
        }

        @Test
        @DisplayName("recibirNotificacion con mensaje nulo no debería agregar notificación")
        void recibirNotificacion_mensajeNull_noAgregaNotificacion() {
            paciente.recibirNotificacion(null);
            assertTrue(paciente.getNotificaciones().isEmpty());
        }

        @Test
        @DisplayName("recibirNotificacion con mensaje vacío no debería agregar notificación")
        void recibirNotificacion_mensajeVacio_noAgregaNotificacion() {
            paciente.recibirNotificacion("");
            assertTrue(paciente.getNotificaciones().isEmpty());
        }
    }


    @Nested
    @DisplayName("Pruebas para Gestión de Citas del Paciente")
    class GestionCitasPacienteTests {

        @Test
        @DisplayName("solicitarCita con datos válidos debería crearla y añadirla")
        void solicitarCita_valido_creaYAñade() {
            Cita cita = paciente.solicitarCita("Dolor de muela", LocalDate.now().plusDays(5), LocalTime.of(14, 30), "Odontología", medicoPrueba, EstadoCita.AGENDADA);
            assertNotNull(cita, "La cita no debería ser nula.");
            assertEquals("Dolor de muela", cita.getMotivo());
            assertEquals(paciente, cita.getPaciente());
            assertEquals(medicoPrueba, cita.getMedico());
            assertEquals(EstadoCita.AGENDADA, cita.getEstado()); // Verifica el estado pasado
            assertTrue(paciente.getCitasProgramadas().contains(cita), "La cita debería estar en la lista del paciente.");
            assertEquals(1, paciente.getCitasProgramadas().size());
        }

        @Test
        @DisplayName("solicitarCita con médico nulo debería retornar null")
        void solicitarCita_medicoNull_retornaNull() {
            Cita cita = paciente.solicitarCita("Consulta", LocalDate.now().plusDays(1), LocalTime.of(9,0), "General", null, EstadoCita.AGENDADA);
            assertNull(cita);
            assertTrue(paciente.getCitasProgramadas().isEmpty());
        }

        @Test
        @DisplayName("agregarCita válida a lista vacía")
        void agregarCita_validaAListaVacia_retornaTrue() {

            Cita cita1 = new Cita("CITA_EXT01", LocalDate.now().plusDays(7), LocalTime.of(15,0), "Examen", paciente, medicoPrueba, "Laboratorio", EstadoCita.AGENDADA);
            assertTrue(paciente.agregarCita(cita1), "Debería poder agregar una cita nueva."); // agregarCita debería devolver boolean
            assertEquals(1, paciente.getCitasProgramadas().size());
        }

        @Test
        @DisplayName("agregarCita duplicada (mismo objeto) no debería añadirla de nuevo")
        void agregarCita_mismaInstancia_noAñade() {
            Cita cita1 = paciente.solicitarCita("Motivo", LocalDate.now().plusDays(1), LocalTime.NOON, "Esp", medicoPrueba, EstadoCita.AGENDADA);
            assertEquals(1, paciente.getCitasProgramadas().size());
            paciente.agregarCita(cita1);
            assertEquals(1, paciente.getCitasProgramadas().size(), "No debería agregar la misma instancia de cita dos veces.");
        }

        @Test
        @DisplayName("agregarCita con objeto Cita nulo no debería modificar la lista")
        void agregarCita_citaNull_noModificaLista() {
            int tamanoAntes = paciente.getCitasProgramadas().size();
            paciente.agregarCita(null);
            assertEquals(tamanoAntes, paciente.getCitasProgramadas().size());
        }


        @Test
        @DisplayName("cancelarCita existente por ID debería retornar true y removerla")
        void cancelarCita_existentePorId_retornaTrueYRemueve() {
                Cita cita = paciente.solicitarCita("Dolor", LocalDate.now().plusDays(10), LocalTime.of(16,0), "Urgencias", medicoPrueba, EstadoCita.AGENDADA);
            assertNotNull(cita);
            String idCita = cita.getId();
            assertEquals(1, paciente.getCitasProgramadas().size());

            assertTrue(paciente.cancelarCita(idCita));
            assertTrue(paciente.getCitasProgramadas().isEmpty());
        }

        @Test
        @DisplayName("cancelarCita no existente por ID debería retornar false")
        void cancelarCita_noExistentePorId_retornaFalse() {
            assertFalse(paciente.cancelarCita("ID_INEXISTENTE"));
        }
    }

    @Nested
    @DisplayName("Pruebas para equals y hashCode de Paciente")
    class EqualsHashCodePacienteTests {

        @Test
        @DisplayName("Dos pacientes con mismo ID y cédula (y superclase) son iguales")
        void testEquals_mismosDatos_sonIguales() {
            Paciente paciente1 = new Paciente("ID001", "Juan Test", "juan@test.com", "pass", "12345");
            Paciente paciente2 = new Paciente("ID001", "Juan Test", "juan@test.com", "pass", "12345");
            assertEquals(paciente1, paciente2, "Pacientes con mismos datos de Usuario y misma cédula deberían ser iguales.");
            assertEquals(paciente1.hashCode(), paciente2.hashCode(), "HashCodes deberían ser iguales para objetos iguales.");
        }

        @Test
        @DisplayName("Dos pacientes con diferente cédula (mismo ID de Usuario) son diferentes")
        void testEquals_diferenteCedula_sonDiferentes() {
            Paciente paciente1 = new Paciente("ID002", "Ana Prueba", "ana@test.com", "pass", "67890");
            Paciente paciente2 = new Paciente("ID002", "Ana Prueba", "ana@test.com", "pass", "00000");
            assertNotEquals(paciente1, paciente2, "Pacientes con diferente cédula deberían ser diferentes.");
        }

        @Test
        @DisplayName("Dos pacientes con diferente ID de Usuario (misma cédula) son diferentes")
        void testEquals_diferenteIdUsuario_sonDiferentes() {
            Paciente paciente1 = new Paciente("ID003", "Luis Test", "luis@test.com", "pass", "11223");
            Paciente paciente2 = new Paciente("ID004", "Luis Test", "luis@test.com", "pass", "11223");
            assertNotEquals(paciente1, paciente2, "Pacientes con diferente ID de Usuario deberían ser diferentes.");
        }

        @Test
        @DisplayName("Un paciente no es igual a null")
        void testEquals_conNull_retornaFalse() {
            Paciente paciente1 = new Paciente("ID005", "Test Null", "null@test.com", "pass", "33445");
            assertNotEquals(null, paciente1); // También puedes usar assertFalse(paciente1.equals(null))
        }

        @Test
        @DisplayName("Un paciente no es igual a un objeto de otra clase")
        void testEquals_conOtraClase_retornaFalse() {
            Paciente paciente1 = new Paciente("ID006", "Test Clase", "clase@test.com", "pass", "55667");
            Object objetoExtraño = new Object();
            assertNotEquals(paciente1, objetoExtraño);
        }
    }
}