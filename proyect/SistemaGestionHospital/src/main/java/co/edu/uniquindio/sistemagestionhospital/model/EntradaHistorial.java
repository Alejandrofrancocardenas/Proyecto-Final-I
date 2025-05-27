package co.edu.uniquindio.sistemagestionhospital.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EntradaHistorial {
    private LocalDate fecha;
    private String diagnostico;
    private String tratamiento;


    public EntradaHistorial(LocalDate fecha, String diagnostico, String tratamiento) {
        this.fecha = fecha;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
    }

    public LocalDate getFecha() { return fecha; }
    public String getDiagnostico() { return diagnostico; }
    public String getTratamiento() { return tratamiento; }



    private List<EntradaHistorial> entradas;

    public void HistorialMedico() {
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
        return new ArrayList<>(entradas); // Devuelve copia
    }

    @Override
    public String toString() {

        if (entradas.isEmpty()) {
            return "No hay entradas en el historial médico.";
        }
        StringBuilder sb = new StringBuilder();
        for (EntradaHistorial entrada : entradas) {
            sb.append("Fecha: ").append(entrada.getFecha() != null ? entrada.getFecha().toString() : "N/A")
                    .append(", Diagnóstico: ").append(entrada.getDiagnostico())
                    .append(", Tratamiento: ").append(entrada.getTratamiento()).append("\n");
        }
        return sb.toString();
    }
}