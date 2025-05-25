package co.edu.uniquindio.sistemagestionhospital.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HorarioAtencion {

    private DayOfWeek dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private List<Sala> salas;
    public HorarioAtencion(DayOfWeek dia, LocalTime horaInicio, LocalTime horaFin) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.salas = new ArrayList<>();
    }
public List<Sala> getSala() {
        return salas;
}
public void setSala(List<Sala> sala) {
        this.salas = sala;
}
    public DayOfWeek getDia() {
        return dia;
    }

    public void setDia(DayOfWeek dia) {
        this.dia = dia;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    @Override
    public String toString() {
        return dia + ": " + horaInicio + " - " + horaFin;
    }
}
