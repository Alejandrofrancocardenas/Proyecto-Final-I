package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.Hospital;
import co.edu.uniquindio.sistemagestionhospital.model.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PacienteControllerTest {

    private HospitalController hospitalController;
    private PacienteController pacienteController;
    private Hospital hospitalModelo;

    @BeforeEach
    void setUp() {
        hospitalModelo = Hospital.getInstance();
        hospitalModelo.limpiarInstanciaParaPruebas(); // Esencial para pruebas sin mocks

        hospitalController = HospitalController.getInstance(); // Obtiene la instancia que usa el Hospital reseteado
        pacienteController = new PacienteController(); // Asume constructor por defecto que usa HospitalController.getInstance()
    }

    @Nested
    @DisplayName("Pruebas para registrarPaciente")
    class RegistrarPacienteTests {

        @Test
        @DisplayName("Debería registrar un paciente válido y devolver true")
        void registrarPaciente_valido_devuelveTrue() {
            Paciente pacienteNuevo = new Paciente("PAC100", "Nuevo Paciente", "nuevo@pac.com", "pass", "100100");
            boolean resultado = pacienteController.registrarPaciente(pacienteNuevo);
            assertTrue(resultado);
            assertNotNull(hospitalModelo.obtenerPaciente("PAC100"));
        }

        @Test
        @DisplayName("No debería registrar paciente con ID duplicado y devolver false")
        void registrarPaciente_idDuplicado_devuelveFalse() {
            hospitalController.registrarPaciente("PAC101", "Existente Uno", "e1@pac.com", "pass", "101101");
            Paciente pacienteDuplicado = new Paciente("PAC101", "Intento Duplicado", "d1@pac.com", "pass", "102102");
            boolean resultado = pacienteController.registrarPaciente(pacienteDuplicado);
            assertFalse(resultado);
            assertEquals(1, hospitalModelo.getListaPacientes().size());
        }

        @Test
        @DisplayName("Debería devolver false si el paciente a registrar es null")
        void registrarPaciente_pacienteNull_devuelveFalse() {
            boolean resultado = pacienteController.registrarPaciente(null);
            assertFalse(resultado);
        }
    }

    @Nested
    @DisplayName("Pruebas para actualizarDatos")
    class ActualizarDatosTests {

        @Test
        @DisplayName("Debería actualizar datos de un paciente existente y devolver true")
        void actualizarDatos_existenteValido_devuelveTrue() {
            hospitalController.registrarPaciente("PAC200", "Original Nombre", "orig@pac.com", "pass", "200200");
            boolean resultado = pacienteController.actualizarDatos("PAC200", "Nombre Actualizado", "act@pac.com", "newpass", "200200");
            assertTrue(resultado);
            Paciente actualizado = hospitalModelo.obtenerPaciente("PAC200");
            assertEquals("Nombre Actualizado", actualizado.getNombre());
            assertEquals("act@pac.com", actualizado.getCorreo());
        }

        @Test
        @DisplayName("Debería devolver false si se intenta actualizar un paciente no existente")
        void actualizarDatos_noExistente_devuelveFalse() {
            boolean resultado = pacienteController.actualizarDatos("ID_FALSO", "Nombre", "correo", "pass", "ced");
            assertFalse(resultado);
        }

        @Test
        @DisplayName("No debería actualizar si la nueva cédula ya existe en otro paciente y devolver false")
        void actualizarDatos_cedulaDuplicadaEnOtro_devuelveFalse() {
            hospitalController.registrarPaciente("PAC201", "Paciente Uno", "uno@pac.com", "p1", "201201");
            hospitalController.registrarPaciente("PAC202", "Paciente Dos", "dos@pac.com", "p2", "202202");
            boolean resultado = pacienteController.actualizarDatos("PAC201", "P Uno Mod", "uno_mod@pac.com", "p1_mod", "202202");
            assertFalse(resultado);
            assertEquals("Paciente Uno", hospitalModelo.obtenerPaciente("PAC201").getNombre()); // No debió cambiar
        }
    }

    @Nested
    @DisplayName("Pruebas para obtenerPacientePorCorreo")
    class ObtenerPacientePorCorreoTests {

        @Test
        @DisplayName("Debería devolver el paciente correcto si el correo existe")
        void obtenerPacientePorCorreo_existente_devuelvePaciente() {
            String correoBuscado = "laura.unica@test.com";
            hospitalController.registrarPaciente("PAC300", "Laura Unica", correoBuscado, "pass", "300300");
            hospitalController.registrarPaciente("PAC301", "Otro Pac", "otro@test.com", "pass", "301301");

            Paciente encontrado = pacienteController.obtenerPacientePorCorreo(correoBuscado);
            assertNotNull(encontrado);
            assertEquals("PAC300", encontrado.getId());
            assertEquals("Laura Unica", encontrado.getNombre());
        }

        @Test
        @DisplayName("Debería devolver null si el correo no se encuentra")
        void obtenerPacientePorCorreo_noExistente_devuelveNull() {
            hospitalController.registrarPaciente("PAC302", "Algun Pac", "algun@test.com", "pass", "302302");
            Paciente encontrado = pacienteController.obtenerPacientePorCorreo("correo.inexistente@test.com");
            assertNull(encontrado);
        }

        @Test
        @DisplayName("Debería devolver null si el correo a buscar es null o vacío")
        void obtenerPacientePorCorreo_correoInvalido_devuelveNull() {
            assertNull(pacienteController.obtenerPacientePorCorreo(null));
            assertNull(pacienteController.obtenerPacientePorCorreo("  "));
        }
    }
}