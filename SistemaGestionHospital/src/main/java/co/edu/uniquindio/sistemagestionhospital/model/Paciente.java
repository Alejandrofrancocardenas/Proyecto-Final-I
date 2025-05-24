package co.edu.uniquindio.sistemagestionhospital.model;

import java.util.LinkedList;
import java.util.List;

public class Paciente extends Usuario {
    private HistorialMedico historialMedico;
    private LinkedList<Cita> citas;

    public Paciente(String id, String nombre, String correo, String contrasena) {
        super(id, nombre, correo, contrasena);
        this.historialMedico = new HistorialMedico();
        this.citas = new LinkedList<>();
    }
    public void solicitarCita(Cita cita) {
        citas.add(cita);
    }

    public void cancelarCita(Cita cita) {
        citas.remove(cita);
    }

    public List<Cita> getCitas() {
        return citas;
    }
    public HistorialMedico getHistorialMedico() {
        return historialMedico;
    }


    @Override
    public void mostrarMenu() {
        System.out.println("Menu del paciente: Solicitar, Cancelar Cita, ver Historial");

    }
}
