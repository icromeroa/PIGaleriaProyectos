package galeria.controller;

import galeria.model.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorLanding {

    private ControladorGaleria controladorGaleria;

    public ControladorLanding(ControladorGaleria controladorGaleria) {
        this.controladorGaleria = controladorGaleria;
    }

    public List<Proyecto> getProyectosDestacados() {
        List<Proyecto> destacados = new ArrayList<>();
        for (Proyecto p : controladorGaleria.consultarCatalogo())
            if (p.esDestacado()) destacados.add(p);
        return destacados;
    }

    public List<Proyecto> getTopPorVistas(int n) {
        List<Proyecto> lista = new ArrayList<>(controladorGaleria.consultarCatalogo());
        lista.sort((a, b) -> b.getCantidadVistas() - a.getCantidadVistas());
        return lista.size() > n ? lista.subList(0, n) : lista;
    }

    public List<Proyecto> getTopPorValoracion(int n) {
        List<Proyecto> lista = new ArrayList<>(controladorGaleria.consultarCatalogo());
        lista.sort((a, b) -> Float.compare(b.valoracionPromedio(), a.valoracionPromedio()));
        return lista.size() > n ? lista.subList(0, n) : lista;
    }

    public List<Proyecto> getSubidosRecientemente(int n) {
        List<Proyecto> lista = new ArrayList<>(controladorGaleria.consultarCatalogo());
        lista.sort((a, b) -> {
            if (a.getFechaSubida() == null || b.getFechaSubida() == null) return 0;
            return b.getFechaSubida().compareTo(a.getFechaSubida());
        });
        return lista.size() > n ? lista.subList(0, n) : lista;
    }

    public Estadisticas getEstadisticasGenerales() {
        List<Proyecto> todos = controladorGaleria.consultarCatalogo();
        int totalVistas = 0;
        int totalGuardados = 0;
        for (Proyecto p : todos) {
            totalVistas    += p.getCantidadVistas();
            totalGuardados += p.getCantidadGuardados();
        }
        return new Estadisticas(
                todos.size(),
                0,
                controladorGaleria.listarFacultades().size(),
                totalVistas,
                totalGuardados,
                0
        );
    }
}