package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HospitalControllerTest {

    private HospitalController hospitalController;
    private Hospital hospitalModelo;

    @BeforeEach
    void setUp() {
        hospitalModelo = Hospital.getInstance();
        hospitalModelo.limpiarInstanciaParaPruebas();
        hospitalController = HospitalController.getInstance();

    }

    @Nested
    @DisplayName("Pruebas de Gestión de Pacientes en HospitalController")
    class GestionPacientesControllerTests {

        @Test
        @DisplayName("registrarPaciente válido")
        void registrarPaciente_valido_llamaModeloYDevuelvePaciente() {
            Paciente pacienteRegistrado = hospitalController.registrarPaciente("PAC001", "Juan Test", "juan@test.com", "pass", "123");
            assertNotNull(pacienteRegistrado);
            assertEquals("PAC001", pacienteRegistrado.getId());
            Paciente pacienteEnModelo = hospitalModelo.obtenerPaciente("PAC001");
            assertNotNull(pacienteEnModelo);
            assertEquals("Juan Test", pacienteEnModelo.getNombre());
        }

        @Test
        @DisplayName("registrarPaciente con ID duplicado")
        void registrarPaciente_idDuplicado_devuelveNull() {
            hospitalController.registrarPaciente("PAC002", "Ana Primera", "ana@test.com", "pass", "456");
            Paciente pacienteDuplicado = hospitalController.registrarPaciente("PAC002", "Ana Segunda", "ana2@test.com", "pass2", "789");
            assertNull(pacienteDuplicado);
            assertEquals(1, hospitalModelo.getListaPacientes().size());
        }

        @Test
        @DisplayName("buscarPacientePorId existente")
        void buscarPacientePorId_existente_devuelvePaciente() {
            hospitalController.registrarPaciente("PAC003", "Carlos Buscado", "carlos@test.com", "pass", "101");
            Paciente pacienteEncontrado = hospitalController.buscarPacientePorId("PAC003");
            assertNotNull(pacienteEncontrado);
            assertEquals("Carlos Buscado", pacienteEncontrado.getNombre());
        }

        @Test
        @DisplayName("buscarPacientePorId no existente")
        void buscarPacientePorId_noExistente_devuelveNull() {
            assertNull(hospitalController.buscarPacientePorId("ID_FALSO"));
        }

        @Test
        @DisplayName("getPacientes devuelve lista")
        void getPacientes_devuelveListaDelModelo() {
            hospitalController.registrarPaciente("P1", "P N1", "p1@m.c", "p", "c1");
            hospitalController.registrarPaciente("P2", "P N2", "p2@m.c", "p", "c2");
            List<Paciente> listaObtenida = hospitalController.getPacientes();
            assertEquals(2, listaObtenida.size());
        }

        @Test
        @DisplayName("modificarPaciente existente")
        void modificarPaciente_existente_actualizaEnModelo() {
            hospitalController.registrarPaciente("PACMOD01", "Nombre Viejo", "viejo@mail.com", "pass", "CED01");
            assertTrue(hospitalController.modificarPaciente("PACMOD01", "Nombre Nuevo", "nuevo@mail.com", "CED01", "newpass"));
            Paciente pacienteActualizado = hospitalModelo.obtenerPaciente("PACMOD01");
            assertEquals("Nombre Nuevo", pacienteActualizado.getNombre());
        }

        @Test
        @DisplayName("eliminarPaciente existente")
        void eliminarPaciente_existente_remueveDelModelo() {
            hospitalController.registrarPaciente("PACDEL01", "A Borrar", "del@mail.com", "pass", "DEL01");
            assertTrue(hospitalController.eliminarPaciente("PACDEL01"));
            assertNull(hospitalModelo.obtenerPaciente("PACDEL01"));
        }
    }

    @Nested
    @DisplayName("Pruebas de Gestión de Médicos en HospitalController")
    class GestionMedicosControllerTests {
        @Test
        @DisplayName("registrarMedico nuevo")
        void registrarMedico_nuevo_agregaYDevuelveMedico() {
            Medico medicoRegistrado = hospitalController.registrarMedico("MED001", "Dr. Test", "dr@test.com", "pass", "Cardio");
            assertNotNull(medicoRegistrado);
            assertEquals("MED001", medicoRegistrado.getId());
            assertNotNull(hospitalModelo.obtenerMedico("MED001"));
        }

        @Test
        @DisplayName("registrarMedico con ID existente")
        void registrarMedico_idExistente_devuelveNull() {
            hospitalController.registrarMedico("MED002", "Dr. Uno", "uno@h.com", "p", "E");
            Medico medicoDuplicado = hospitalController.registrarMedico("MED002", "Dr. Dos", "dos@h.com", "p2", "E2");
            assertNull(medicoDuplicado);
        }
    }

    @Nested
    @DisplayName("Pruebas de Gestión de Citas en HospitalController")
    class GestionCitasControllerTests {
        private Paciente pacientePrueba;
        private Medico medicoPrueba;

        @BeforeEach
        void setUpCitasContext() {
            pacientePrueba = hospitalController.registrarPaciente("PAC-CITA", "P Cita", "pc@mail.com", "p", "c7");
            medicoPrueba = hospitalController.registrarMedico("MED-CITA", "M Cita", "mc@mail.com", "p", "Gen");
            assertNotNull(pacientePrueba);
            assertNotNull(medicoPrueba);
        }

        @Test
        @DisplayName("asignarCita válida")
        void asignarCita_valida_agregaCorrectamente() {
            Cita cita = new Cita("CITA001", LocalDate.now().plusDays(1), LocalTime.NOON, "Consulta", pacientePrueba, medicoPrueba, "General", EstadoCita.AGENDADA);
            assertTrue(hospitalController.asignarCita(cita));
            assertNotNull(hospitalModelo.obtenerCita("CITA001"));
            Medico medicoConCita = hospitalModelo.obtenerMedico(medicoPrueba.getId());
            assertTrue(medicoConCita.getCitas().stream().anyMatch(c -> c.getId().equals("CITA001")));
            Paciente pacienteConCita = hospitalModelo.obtenerPaciente(pacientePrueba.getId());
            assertTrue(pacienteConCita.getCitasProgramadas().stream().anyMatch(c -> c.getId().equals("CITA001")));
        }

        @Test
        @DisplayName("cancelarCitaYNotificar")
        void cancelarCitaYNotificar_valida_actualizaYNotifica() {
            Cita cita = new Cita("CITA002", LocalDate.now().plusDays(2), LocalTime.of(14,0), "Seguimiento", pacientePrueba, medicoPrueba, "General", EstadoCita.AGENDADA);
            hospitalController.asignarCita(cita);

            assertTrue(hospitalController.cancelarCitaYNotificar("CITA002"));
            Cita citaCancelada = hospitalModelo.obtenerCita("CITA002");
            assertNotNull(citaCancelada);
            assertEquals(EstadoCita.CANCELADA, citaCancelada.getEstado());
        }
    }

    @Nested
    @DisplayName("Pruebas para Historial Médico en HospitalController")
    class HistorialMedicoControllerTests {
        @Test
        @DisplayName("registrarEntradaHistorialMedico con paciente válido")
        void registrarEntradaHistorialMedico_pacienteValido_agregaEntrada() {
            Paciente p = hospitalController.registrarPaciente("PAC-HIST", "Hist Pac", "hp@mail.com", "p", "h1");
            LocalDate fecha = LocalDate.now();
            String diagnostico = "Fiebre";
            String tratamiento = "Descanso";

            assertTrue(hospitalController.registrarEntradaHistorialMedico(p.getId(), fecha, diagnostico, tratamiento));
            Paciente pacienteConHistorial = hospitalModelo.obtenerPaciente(p.getId());
            assertFalse(pacienteConHistorial.getHistorialMedico().getEntradas().isEmpty());
            assertEquals(diagnostico, pacienteConHistorial.getHistorialMedico().getEntradas().get(0).getDiagnostico());
        }
    }
    // ... Añade más pruebas para los métodos restantes (Salas, autenticarUsuario, Horarios, etc.)
    // siguiendo el mismo patrón.
}