package co.edu.uniquindio.sistemagestionhospital.Controller;

import co.edu.uniquindio.sistemagestionhospital.model.Cita;
import co.edu.uniquindio.sistemagestionhospital.model.HistorialMedico;
import co.edu.uniquindio.sistemagestionhospital.model.Medico;

import java.util.ArrayList;
import java.util.List;

public class MedicoController {

    private List<Medico> listaMedicos = new ArrayList<>();

    public boolean registrarMedico(Medico medico) {
        if (buscarMedico(medico.getId()) != null) return false;
        listaMedicos.add(medico);
        return true;
    }

    public Medico buscarMedico(String id) {
        for (Medico m : listaMedicos) {
            if (m.getId().equals(id)) return m;
        }
        return null;
    }

    public boolean eliminarMedico(String id) {
        Medico m = buscarMedico(id);
        if (m != null) {
            listaMedicos.remove(m);
            return true;
        }
        return false;
    }

    public boolean agregarCitaAMedico(String idMedico, Cita cita) {
        Medico m = buscarMedico(idMedico);
        return m != null && m.agregarCita(cita);
    }

    public boolean cancelarCitaAMedico(String idMedico, Cita cita) {
        Medico m = buscarMedico(idMedico);
        return m != null && m.cancelarCita(cita);
    }

    public boolean registrarHistorial(String idMedico, Cita cita, String diagnostico, String tratamiento) {
        Medico m = buscarMedico(idMedico);
        return m != null && m.registrarHistorialMedico(cita, diagnostico, tratamiento);
    }

    public List<Medico> getListaMedicos() {
        return listaMedicos;
    }
}