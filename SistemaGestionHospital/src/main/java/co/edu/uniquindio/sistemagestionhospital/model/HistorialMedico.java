package co.edu.uniquindio.sistemagestionhospital.model;

import java.util.ArrayList;
import java.util.LinkedList;

public class HistorialMedico {
    private LinkedList<String> diagnosticos;

    public HistorialMedico() {
        diagnosticos = new LinkedList<>();
    }
    public void agregarDiagnostico(String diagnostico) {
        diagnosticos.add(diagnostico);
    }
    public LinkedList<String> getDiagnosticos() {
    return diagnosticos;}
}
