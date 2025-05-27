package co.edu.uniquindio.sistemagestionhospital.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistorialMedico {
    private List<EntradaHistorial> entradas;

    public HistorialMedico() { // Constructor
        this.entradas = new ArrayList<>();
    }

    public boolean agregarEntrada(LocalDate fecha, String diagnostico, String tratamiento) {
        if (fecha == null || diagnostico == null || diagnostico.trim().isEmpty() || tratamiento == null || tratamiento.trim().isEmpty()) {
            System.err.println("HistorialMedico: Datos inválidos para agregar entrada.");
            return false;
        }
        EntradaHistorial nuevaEntrada = new EntradaHistorial(fecha, diagnostico, tratamiento);
        return this.entradas.add(nuevaEntrada);
    }

    public List<EntradaHistorial> getEntradas() {
        return new ArrayList<>(entradas);

    }
}