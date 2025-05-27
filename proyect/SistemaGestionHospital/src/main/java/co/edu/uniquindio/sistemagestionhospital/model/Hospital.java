package co.edu.uniquindio.sistemagestionhospital.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Hospital implements Serializable {

    private static Hospital instance;

    private final List<Paciente> listaPacientes;
    private final List<Medico> listaMedicos;
    private final List<Cita> listaCitasHospital;
    private final List<Sala> listaSalas;

    private Paciente pacienteSesion;
    private Medico medicoSesion;

    private Hospital() {
        this.listaPacientes = new ArrayList<>();
        this.listaMedicos = new ArrayList<>();
        this.listaCitasHospital = new ArrayList<>();
        this.listaSalas = new ArrayList<>();
        this.pacienteSesion = null;
        this.medicoSesion = null;
        System.out.println(">>> Hospital Singleton INSTANCE CREADA - HashCode: " + this.hashCode());
    }

    public static synchronized Hospital getInstance() {
        if (instance == null) {
            instance = new Hospital();
        }
        return instance;
    }
    public void limpiarInstanciaParaPruebas() {
        this.listaPacientes.clear();
        this.listaMedicos.clear();
        this.listaCitasHospital.clear();
        this.listaSalas.clear();
        this.pacienteSesion = null;
        this.medicoSesion = null;
        System.out.println(">>> Hospital (Modelo): Instancia limpiada para pruebas.");
    }



    public Usuario autenticarUsuario(String correo, String contrasena) {
        Optional<Paciente> p = listaPacientes.stream()
                .filter(usr -> usr.getCorreo().equalsIgnoreCase(correo) && usr.getContrasena().equals(contrasena))
                .findFirst();
        if (p.isPresent()) return p.get();

        Optional<Medico> m = listaMedicos.stream()
                .filter(usr -> usr.getCorreo().equalsIgnoreCase(correo) && usr.getContrasena().equals(contrasena))
                .findFirst();
        if (m.isPresent()) return m.get();

        if ("admin@hospital.com".equalsIgnoreCase(correo) && "admin123".equals(contrasena)) {
            return new Administrador("admin001", "Administrador Principal", "admin@hospital.com", "admin123");
        }
        return null;
    }


    public List<Paciente> getListaPacientes() {
        return new ArrayList<>(listaPacientes);
    }

    public boolean agregarPaciente(Paciente paciente) {
        if (paciente == null || paciente.getId() == null || paciente.getCedula() == null) return false;
        if (listaPacientes.stream().anyMatch(p -> p.getId().equals(paciente.getId()) || p.getCedula().equals(paciente.getCedula()))) {
            System.err.println("Hospital (Modelo): Paciente con ID o Cédula duplicada: " + paciente.getId() + "/" + paciente.getCedula());
            return false;
        }
        return listaPacientes.add(paciente);
    }

    public Paciente obtenerPaciente(String id) {
        return listaPacientes.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public Paciente obtenerPacientePorCedula(String cedula) {
        return listaPacientes.stream().filter(p -> p.getCedula().equals(cedula)).findFirst().orElse(null);
    }

    public boolean modificarPaciente(String id, String nNombre, String nCorreo, String nCedula, String nContrasena) {
        Paciente p = obtenerPaciente(id);
        if (p != null) {
            if (!p.getCedula().equals(nCedula) && obtenerPacientePorCedula(nCedula) != null) {
                System.err.println("Hospital (Modelo): Modificar Paciente - Nueva cédula " + nCedula + " ya existe.");
                return false;
            }
            p.setNombre(nNombre);
            p.setCorreo(nCorreo);
            p.setCedula(nCedula);
            p.setContrasena(nContrasena);
            return true;
        }
        return false;
    }

    public boolean eliminarPaciente(String id) {
        return listaPacientes.removeIf(p -> p.getId().equals(id));
    }


    public List<Medico> getListaMedicos() {
        System.out.println(">>> Hospital (Modelo): getListaMedicos() - Devolviendo " + this.listaMedicos.size() + " médicos. HashCodes:");
        for (Medico m : this.listaMedicos) {
            System.out.println("    En lista Hospital: " + m.getNombre() + " (ID: " + m.getId() + ", HashCode: " + m.hashCode() + ")");
        }
        return new ArrayList<>(this.listaMedicos);
    }

    public boolean agregarMedico(Medico medico) {
        if (medico == null || medico.getId() == null) {
            System.err.println("Hospital (Modelo): Intento de agregar médico nulo o sin ID. FRACASO.");
            return false;
        }
        System.out.println("Hospital (Modelo): Verificando para agregar médico ID: " + medico.getId() + ", Nombre: " + medico.getNombre());
        for (Medico mExistente : this.listaMedicos) {
            if (mExistente.getId().equals(medico.getId())) {
                System.err.println("Hospital (Modelo): MÉDICO ID DUPLICADO: " + medico.getId() + ". No se agregó.");
                return false;
            }
        }
        boolean agregado = this.listaMedicos.add(medico);
        if (agregado) {
            System.out.println(">>> Hospital (Modelo): Médico '" + medico.getNombre() + "' (ID: " + medico.getId() + ", HashCode: " + medico.hashCode() + ") AÑADIDO a listaMedicos. Tamaño ahora: " + listaMedicos.size());
        } else {
            System.err.println("Hospital (Modelo): ArrayList.add() devolvió false para médico " + medico.getNombre() + ". FRACASO.");
        }
        return agregado;
    }

    public Medico obtenerMedico(String id) {
        if (id == null) return null;
        Optional<Medico> medicoOpt = listaMedicos.stream().filter(m -> id.equals(m.getId())).findFirst();
        if (medicoOpt.isPresent()) {
            Medico m = medicoOpt.get();
            System.out.println(">>> Hospital (Modelo): obtenerMedico(ID: " + id + ") - Encontrado: " + m.getNombre() + " (HashCode: " + m.hashCode() + ")");
            return m;
        }
        System.out.println(">>> Hospital (Modelo): obtenerMedico(ID: " + id + ") - NO Encontrado.");
        return null;
    }

    public boolean modificarMedico(String id, String nNombre, String nCorreo, String nContrasena, String nEspecialidad) {
        Medico m = obtenerMedico(id);
        if (m != null) {
            m.setNombre(nNombre);
            m.setCorreo(nCorreo);
            if (nContrasena != null && !nContrasena.trim().isEmpty()) m.setContrasena(nContrasena);
            m.setEspecialidad(nEspecialidad);
            return true;
        }
        return false;
    }

    public boolean eliminarMedico(String id) {
        return listaMedicos.removeIf(m -> m.getId().equals(id));
    }

    public List<Cita> getCitas() {
        return new ArrayList<>(listaCitasHospital);
    }

    public boolean agregarCita(Cita cita) {
        if (cita == null || cita.getId() == null) return false;
        if (this.listaCitasHospital.stream().anyMatch(c -> c.getId().equals(cita.getId()))) {
            System.err.println("Hospital (Modelo): Cita con ID duplicado: " + cita.getId() + ". No se agregó a la lista central.");
            return false;
        }
        this.listaCitasHospital.add(cita);
        System.out.println(">>> Hospital (Modelo): Cita ID " + cita.getId() + " AGREGADA a listaCitasHospital. Tamaño ahora: " + listaCitasHospital.size());
        return true;
    }

    public Cita obtenerCita(String id) {
        return listaCitasHospital.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }


    public List<Sala> getListaSalas() {
        return new ArrayList<>(listaSalas);
    }

    public boolean agregarSala(Sala sala) {
        if (sala == null || sala.getId() == null || sala.getNombre() == null) {
            System.err.println("Hospital (Modelo): Intento de agregar sala nula o con ID/Nombre nulo.");
            return false;
        }


        for (Sala sExistente : this.listaSalas) {
            if (sExistente.getId().equals(sala.getId())) {
                System.err.println("Hospital (Modelo): Sala con ID duplicado: " + sala.getId() + ". No se agregó.");
                return false;
            }
        }
        for (Sala sExistente : this.listaSalas) {
            if (sExistente.getNombre().equalsIgnoreCase(sala.getNombre())) {
                System.err.println("Hospital (Modelo): Sala con Nombre duplicado (ignorando caso): '" + sala.getNombre() + "'. No se agregó.");
                return false;
            }
        }

        boolean agregado = this.listaSalas.add(sala);
        if (agregado) {
            System.out.println("Hospital (Modelo): Sala '" + sala.getNombre() + "' (ID: " + sala.getId() + ") AÑADIDA. Tamaño ahora: " + listaSalas.size());
        }
        return agregado;
    }

    public Sala obtenerSala(String id) {
        return listaSalas.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    public boolean modificarSala(String id, String nuevoNombre, int nuevaCapacidad) {
        Sala sala = obtenerSala(id);
        if (sala != null) {

            boolean nombreEnUsoPorOtraSala = listaSalas.stream()
                    .anyMatch(s -> !s.getId().equals(id) && s.getNombre().equalsIgnoreCase(nuevoNombre));
            if (nombreEnUsoPorOtraSala) {
                System.err.println("Hospital (Modelo): El nuevo nombre de sala '" + nuevoNombre + "' ya está en uso por otra sala. No se actualizó.");
                return false;
            }
            System.out.println("Hospital (Modelo): Actualizando Sala ID " + id + " a Nombre: " + nuevoNombre + ", Capacidad: " + nuevaCapacidad);
            sala.setNombre(nuevoNombre);
            sala.setCapacidad(nuevaCapacidad);
            return true;
        }
        System.err.println("Hospital (Modelo): Sala ID " + id + " no encontrada para modificar. No se actualizó.");
        return false;
    }

    public boolean eliminarSala(String id) {
        return listaSalas.removeIf(s -> s.getId().equals(id));
    }
}