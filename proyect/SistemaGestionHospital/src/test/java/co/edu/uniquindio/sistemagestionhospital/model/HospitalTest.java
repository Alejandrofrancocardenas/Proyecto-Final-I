package co.edu.uniquindio.sistemagestionhospital.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;


import static org.junit.jupiter.api.Assertions.*;

class HospitalTest {

    private Hospital hospital;

    @BeforeEach
    void setUp() {

        hospital = Hospital.getInstance();
        hospital.limpiarInstanciaParaPruebas();
    }

    @Nested
    @DisplayName("Pruebas para Gestión de Pacientes")
    class GestionPacientesTests {

        @Test
        @DisplayName("Agregar un nuevo paciente válido debería retornar true y aumentar la lista")
        void agregarPaciente_nuevoValido_retornaTrueYAumentaLista() {
            Paciente paciente = new Paciente("PAC001", "Juan Perez", "juan.perez@email.com", "pass123", "1094001");
            assertTrue(hospital.agregarPaciente(paciente), "El paciente debería ser agregado exitosamente.");
            assertEquals(1, hospital.getListaPacientes().size(), "La lista de pacientes debería tener 1 elemento.");
            assertNotNull(hospital.obtenerPaciente("PAC001"), "El paciente agregado debería poder obtenerse por ID.");
        }

        @Test
        @DisplayName("Agregar paciente con ID duplicado debería retornar false")
        void agregarPaciente_idDuplicado_retornaFalse() {
            Paciente paciente1 = new Paciente("PAC002", "Ana Gomez", "ana.gomez@email.com", "passAna", "1094002");
            hospital.agregarPaciente(paciente1);

            Paciente paciente2 = new Paciente("PAC002", "Carlos Ruiz", "carlos.ruiz@email.com", "passCarlos", "1094003"); // Mismo ID
            assertFalse(hospital.agregarPaciente(paciente2), "No se debería agregar paciente con ID duplicado.");
            assertEquals(1, hospital.getListaPacientes().size(), "La lista solo debería tener el primer paciente.");
        }

        @Test
        @DisplayName("Agregar paciente con Cédula duplicada debería retornar false")
        void agregarPaciente_cedulaDuplicada_retornaFalse() {
            Paciente paciente1 = new Paciente("PAC003", "Luisa Fer", "luisa.fer@email.com", "passLuisa", "1094004");
            hospital.agregarPaciente(paciente1);

            Paciente paciente2 = new Paciente("PAC004", "Pedro Paramo", "pedro.paramo@email.com", "passPedro", "1094004"); // Misma Cédula
            assertFalse(hospital.agregarPaciente(paciente2), "No se debería agregar paciente con cédula duplicada.");
            assertEquals(1, hospital.getListaPacientes().size());
        }

        @Test
        @DisplayName("Agregar paciente null debería retornar false")
        void agregarPaciente_null_retornaFalse() {
            assertFalse(hospital.agregarPaciente(null));
            assertTrue(hospital.getListaPacientes().isEmpty());
        }

        @Test
        @DisplayName("Obtener paciente por ID existente")
        void obtenerPaciente_idExistente_retornaPaciente() {
            Paciente paciente = new Paciente("PAC005", "Sofia Luna", "sofia.luna@email.com", "passSofia", "1094005");
            hospital.agregarPaciente(paciente);
            Paciente encontrado = hospital.obtenerPaciente("PAC005");
            assertNotNull(encontrado);
            assertEquals("Sofia Luna", encontrado.getNombre());
        }

        @Test
        @DisplayName("Obtener paciente por ID no existente retorna null")
        void obtenerPaciente_idNoExistente_retornaNull() {
            assertNull(hospital.obtenerPaciente("ID_FALSO"));
        }

        @Test
        @DisplayName("Obtener paciente por Cédula existente")
        void obtenerPacientePorCedula_existente_retornaPaciente() {
            Paciente paciente = new Paciente("PAC006", "Mario Bros", "mario@email.com", "peach", "100100");
            hospital.agregarPaciente(paciente);
            Paciente encontrado = hospital.obtenerPacientePorCedula("100100");
            assertNotNull(encontrado);
            assertEquals("Mario Bros", encontrado.getNombre());
        }

        @Test
        @DisplayName("Modificar paciente existente debería retornar true y actualizar datos")
        void modificarPaciente_existente_retornaTrueYActualiza() {
            Paciente paciente = new Paciente("PAC007", "Laura Antigua", "laura.a@email.com", "passAntigua", "1094007");
            hospital.agregarPaciente(paciente);
            boolean modificado = hospital.modificarPaciente("PAC007", "Laura Nueva", "laura.n@email.com", "1094007", "passNueva");
            assertTrue(modificado);
            Paciente actualizado = hospital.obtenerPaciente("PAC007");
            assertNotNull(actualizado);
            assertEquals("Laura Nueva", actualizado.getNombre());
            assertEquals("laura.n@email.com", actualizado.getCorreo());
        }

        @Test
        @DisplayName("Modificar paciente con nueva cédula que ya existe en otro debería retornar false")
        void modificarPaciente_cedulaNuevaDuplicada_retornaFalse() {
            hospital.agregarPaciente(new Paciente("PAC008", "Paciente Uno", "uno@mail.com", "123", "111"));
            hospital.agregarPaciente(new Paciente("PAC009", "Paciente Dos", "dos@mail.com", "123", "222"));
            assertFalse(hospital.modificarPaciente("PAC009", "Paciente Dos Modificado", "dos.mod@mail.com", "111", "pass"));
        }

        @Test
        @DisplayName("Eliminar paciente existente debería retornar true y removerlo de la lista")
        void eliminarPaciente_existente_retornaTrueYRemueve() {
            Paciente paciente = new Paciente("PAC010", "Victima Test", "victima@email.com", "passVictima", "1094010");
            hospital.agregarPaciente(paciente);
            assertEquals(1, hospital.getListaPacientes().size());
            assertTrue(hospital.eliminarPaciente("PAC010"));
            assertTrue(hospital.getListaPacientes().isEmpty());
            assertNull(hospital.obtenerPaciente("PAC010"));
        }

        @Test
        @DisplayName("Eliminar paciente no existente debería retornar false")
        void eliminarPaciente_noExistente_retornaFalse() {
            assertFalse(hospital.eliminarPaciente("ID_FALSO"));
        }

        @Test
        @DisplayName("getListaPacientes inicialmente vacía")
        void getListaPacientes_inicialmenteVacia() {
            assertTrue(hospital.getListaPacientes().isEmpty());
        }
    }

    @Nested
    @DisplayName("Pruebas para Gestión de Médicos")
    class GestionMedicosTests {

        @Test
        @DisplayName("Agregar nuevo médico válido")
        void agregarMedico_valido_retornaTrue() {
            Medico medico = new Medico("MED001", "Dr. Strange", "strange@avengers.com", "timeStone", "Neurocirugía");
            assertTrue(hospital.agregarMedico(medico));
            assertEquals(1, hospital.getListaMedicos().size());
            assertNotNull(hospital.obtenerMedico("MED001"));
        }

        @Test
        @DisplayName("Agregar médico con ID duplicado retorna false")
        void agregarMedico_idDuplicado_retornaFalse() {
            hospital.agregarMedico(new Medico("MED002", "Dr. Ana", "ana@mail.com", "123", "Pediatra"));
            assertFalse(hospital.agregarMedico(new Medico("MED002", "Dr. Otro", "otro@mail.com", "456", "General")));
            assertEquals(1, hospital.getListaMedicos().size());
        }

        @Test
        @DisplayName("Obtener médico por ID existente")
        void obtenerMedico_existente_retornaMedico() {
            Medico medico = new Medico("MED003", "Dr. House", "house@example.com", "lupus", "Diagnóstico");
            hospital.agregarMedico(medico);
            Medico encontrado = hospital.obtenerMedico("MED003");
            assertNotNull(encontrado);
            assertEquals("Dr. House", encontrado.getNombre());
        }

        @Test
        @DisplayName("Modificar médico existente")
        void modificarMedico_existente_actualizaDatos() {
            Medico medico = new Medico("MED004", "Dr. Original", "orig@mail.com", "pass", "Cardio");
            hospital.agregarMedico(medico);
            assertTrue(hospital.modificarMedico("MED004", "Dr. Modificado", "mod@mail.com", "newpass", "Cirugía"));
            Medico modificado = hospital.obtenerMedico("MED004");
            assertEquals("Dr. Modificado", modificado.getNombre());
            assertEquals("Cirugía", modificado.getEspecialidad());
        }

        @Test
        @DisplayName("Modificar médico sin cambiar contraseña si el campo está vacío")
        void modificarMedico_contrasenaVacia_noCambiaContrasena() {
            String contrasenaOriginal = "passOriginal";
            Medico medico = new Medico("MED005", "Dr. PassTest", "pass@mail.com", contrasenaOriginal, "Testologia");
            hospital.agregarMedico(medico);

            assertTrue(hospital.modificarMedico("MED005", "Dr. PassTest", "pass@mail.com", "", "Testologia"));
            assertEquals(contrasenaOriginal, hospital.obtenerMedico("MED005").getContrasena());

            assertTrue(hospital.modificarMedico("MED005", "Dr. PassTest", "pass@mail.com", null, "Testologia"));
            assertEquals(contrasenaOriginal, hospital.obtenerMedico("MED005").getContrasena());
        }


        @Test
        @DisplayName("Eliminar médico existente")
        void eliminarMedico_existente_loRemueve() {
            Medico medico = new Medico("MED006", "Dr. Borrable", "delete@mail.com", "123", "General");
            hospital.agregarMedico(medico);
            assertTrue(hospital.eliminarMedico("MED006"));
            assertNull(hospital.obtenerMedico("MED006"));
            assertTrue(hospital.getListaMedicos().isEmpty());
        }
    }


    @Nested
    @DisplayName("Pruebas para Gestión de Citas")
    class GestionCitasTests {
        private Paciente pacientePrueba;
        private Medico medicoPrueba;

        @BeforeEach
        void setUpCitas() {
            pacientePrueba = new Paciente("PAC-CITA", "Paciente Citas", "pac@cita.com", "123", "777");
            medicoPrueba = new Medico("MED-CITA", "Medico Citas", "med@cita.com", "123", "General");
            hospital.agregarPaciente(pacientePrueba);
            hospital.agregarMedico(medicoPrueba);
        }

        @Test
        @DisplayName("Agregar nueva cita válida")
        void agregarCita_valida_retornaTrue() {
            Cita cita = new Cita("CITA001", LocalDate.now().plusDays(1), LocalTime.of(10,0), "Consulta", pacientePrueba, medicoPrueba, "General", EstadoCita.AGENDADA);
            assertTrue(hospital.agregarCita(cita));
            assertEquals(1, hospital.getCitas().size());
        }

        @Test
        @DisplayName("Agregar cita con ID duplicado retorna false")
        void agregarCita_idDuplicado_retornaFalse() {
            hospital.agregarCita(new Cita("CITA002", LocalDate.now().plusDays(1), LocalTime.of(10,0), "Consulta 1", pacientePrueba, medicoPrueba, "General", EstadoCita.AGENDADA));
            assertFalse(hospital.agregarCita(new Cita("CITA002", LocalDate.now().plusDays(2), LocalTime.of(11,0), "Consulta 2", pacientePrueba, medicoPrueba, "General", EstadoCita.AGENDADA)));
            assertEquals(1, hospital.getCitas().size());
        }

        @Test
        @DisplayName("Obtener cita por ID existente")
        void obtenerCita_existente_retornaCita() {
            Cita cita = new Cita("CITA003", LocalDate.now().plusDays(1), LocalTime.of(10,0), "Dolor", pacientePrueba, medicoPrueba, "General", EstadoCita.AGENDADA);
            hospital.agregarCita(cita);
            Cita encontrada = hospital.obtenerCita("CITA003");
            assertNotNull(encontrada);
            assertEquals("Dolor", encontrada.getMotivo());
        }


    }



    @Nested
    @DisplayName("Pruebas para Gestión de Salas")
    class GestionSalasTests {
        @Test
        @DisplayName("Agregar nueva sala válida")
        void agregarSala_valida_retornaTrue() {
            Sala sala = new Sala("SALA01", "Quirofano 1", 1);
            assertTrue(hospital.agregarSala(sala));
            assertEquals(1, hospital.getListaSalas().size());
        }

        @Test
        @DisplayName("Agregar sala con ID duplicado retorna false")
        void agregarSala_idDuplicado_retornaFalse() {
            hospital.agregarSala(new Sala("SALA02", "Sala A", 1));
            assertFalse(hospital.agregarSala(new Sala("SALA02", "Sala B", 1)));
            assertEquals(1, hospital.getListaSalas().size());
        }

        @Test
        @DisplayName("Agregar sala con Nombre duplicado retorna false")
        void agregarSala_nombreDuplicado_retornaFalse() {
            hospital.agregarSala(new Sala("SALA03", "Consultorio X", 1));
            assertFalse(hospital.agregarSala(new Sala("SALA04", "Consultorio X", 1))); // Mismo nombre, diferente ID
            assertEquals(1, hospital.getListaSalas().size());
        }

        @Test
        @DisplayName("Modificar sala existente")
        void modificarSala_existente_actualizaDatos() {
            Sala sala = new Sala("SALA05", "Sala Original", 5);
            hospital.agregarSala(sala);
            assertTrue(hospital.modificarSala("SALA05", "Sala Modificada", 10));
            Sala modificada = hospital.obtenerSala("SALA05");
            assertEquals("Sala Modificada", modificada.getNombre());
            assertEquals(10, modificada.getCapacidad());
        }

        @Test
        @DisplayName("Modificar sala a nombre ya existente en otra sala retorna false")
        void modificarSala_nombreNuevoDuplicado_retornaFalse() {
            hospital.agregarSala(new Sala("SALA06", "Sala Uno", 1));
            hospital.agregarSala(new Sala("SALA07", "Sala Dos", 1));
            assertFalse(hospital.modificarSala("SALA07", "Sala Uno", 2)); // Intenta poner nombre de SALA06
        }
    }
}