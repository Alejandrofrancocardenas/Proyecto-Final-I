package co.edu.uniquindio.sistemagestionhospital.model;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class HorarioAtencion implements Serializable {
    private String id;
    private DayOfWeek diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    public HorarioAtencion(String id, DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        this.id = id;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DayOfWeek getDiaSemana() { // <-- Método getDiaSemana()
        return diaSemana;
    }

    public void setDiaSemana(DayOfWeek diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HorarioAtencion that = (HorarioAtencion) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "HorarioAtencion{" +
                "id='" + id + '\'' +
                ", diaSemana='" + diaSemana + '\'' +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                '}';
    }
}