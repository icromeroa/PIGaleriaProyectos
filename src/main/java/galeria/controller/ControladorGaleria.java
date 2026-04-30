package galeria.controller;

import java.galeria.model.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorGaleria {

    private List<Proyecto>  proyectos   = new ArrayList<>();
    private List<Facultad>  facultades  = new ArrayList<>();
    private List<Programa>  programas   = new ArrayList<>();
    private List<Materia>   materias    = new ArrayList<>();
    private List<Semestre>  semestres   = new ArrayList<>();
    private List<Categoria> categorias  = new ArrayList<>();

    // ── PROYECTOS ──────────────────────────────────────────
    public void agregarProyecto(Proyecto p) {
        proyectos.add(p);
        System.out.println("[OK] Proyecto agregado: " + p.getTitulo());
    }

    public void editarProyecto(int idProyecto, Proyecto nuevo) {
        for (int i = 0; i < proyectos.size(); i++) {
            if (proyectos.get(i).getIdProyecto() == idProyecto) {
                proyectos.set(i, nuevo);
                System.out.println("[OK] Proyecto editado: " + nuevo.getTitulo());
                return;
            }
        }
        System.out.println("[ERROR] Proyecto no encontrado con ID: " + idProyecto);
    }

    public void eliminarProyecto(int idProyecto) {
        boolean eliminado = proyectos.removeIf(p -> p.getIdProyecto() == idProyecto);
        System.out.println(eliminado
            ? "[OK] Proyecto eliminado con ID: " + idProyecto
            : "[ERROR] Proyecto no encontrado con ID: " + idProyecto);
    }

    public List<Proyecto> consultarCatalogo() {
        return proyectos;
    }

    public Proyecto buscarProyectoPorId(int id) {
        for (Proyecto p : proyectos)
            if (p.getIdProyecto() == id) return p;
        return null;
    }

    public List<Proyecto> filtrarProyectos(FiltroProyecto filtro) {
        if (filtro.estaVacio()) return proyectos;
        List<Proyecto> resultado = new ArrayList<>();
        for (Proyecto p : proyectos) {
            boolean cumple = true;
            if (filtro.getIdFacultad()  != null && (p.getFacultad()  == null || p.getFacultad().getIdFacultad()   != filtro.getIdFacultad()))  cumple = false;
            if (filtro.getIdPrograma()  != null && (p.getPrograma()  == null || p.getPrograma().getIdPrograma()   != filtro.getIdPrograma()))  cumple = false;
            if (filtro.getIdMateria()   != null && (p.getMateria()   == null || p.getMateria().getIdMateria()     != filtro.getIdMateria()))   cumple = false;
            if (filtro.getIdSemestre()  != null && (p.getSemestre()  == null || p.getSemestre().getIdSemestre()   != filtro.getIdSemestre()))  cumple = false;
            if (filtro.getIdCategoria() != null && (p.getCategoria() == null || p.getCategoria().getIdCategoria() != filtro.getIdCategoria())) cumple = false;
            if (filtro.getTextoBusqueda() != null && !filtro.getTextoBusqueda().isEmpty()) {
                String texto = filtro.getTextoBusqueda().toLowerCase();
                if (!p.getTitulo().toLowerCase().contains(texto) &&
                    !p.getResumen().toLowerCase().contains(texto)) cumple = false;
            }
            if (cumple) resultado.add(p);
        }
        return resultado;
    }

    public void solicitarDescarga(Proyecto p) {
        System.out.println("[DESCARGA] Archivo: " + p.getArchivoURL());
    }

    // ── FACULTADES ─────────────────────────────────────────
    public void agregarFacultad(String nombreFacultad) {
        int id = facultades.size() + 1;
        facultades.add(new Facultad(id, nombreFacultad));
        System.out.println("[OK] Facultad agregada: " + nombreFacultad);
    }

    public void editarFacultad(int idFacultad, String nuevoNombre) {
        for (Facultad f : facultades) {
            if (f.getIdFacultad() == idFacultad) {
                f.setNombreFacultad(nuevoNombre);
                System.out.println("[OK] Facultad editada: " + nuevoNombre);
                return;
            }
        }
        System.out.println("[ERROR] Facultad no encontrada: " + idFacultad);
    }

    public void eliminarFacultad(int idFacultad) {
        boolean ok = facultades.removeIf(f -> f.getIdFacultad() == idFacultad);
        System.out.println(ok ? "[OK] Facultad eliminada." : "[ERROR] Facultad no encontrada.");
    }

    public List<Facultad> listarFacultades() { return facultades; }

    // ── PROGRAMAS ──────────────────────────────────────────
    public void agregarPrograma(String nombrePrograma, int idFacultad) {
        Facultad f = buscarFacultadPorId(idFacultad);
        if (f == null) { System.out.println("[ERROR] Facultad no encontrada."); return; }
        int id = programas.size() + 1;
        programas.add(new Programa(id, nombrePrograma));
        System.out.println("[OK] Programa agregado: " + nombrePrograma);
    }

    public void editarPrograma(int idPrograma, String nuevoNombre) {
        for (Programa p : programas) {
            if (p.getIdPrograma() == idPrograma) {
                p.setNombrePrograma(nuevoNombre);
                System.out.println("[OK] Programa editado: " + nuevoNombre);
                return;
            }
        }
        System.out.println("[ERROR] Programa no encontrado.");
    }

    public void eliminarPrograma(int idPrograma) {
        boolean ok = programas.removeIf(p -> p.getIdPrograma() == idPrograma);
        System.out.println(ok ? "[OK] Programa eliminado." : "[ERROR] Programa no encontrado.");
    }

    public List<Programa> listarProgramas() { return programas; }

    // ── MATERIAS ───────────────────────────────────────────
    public void agregarMateria(String nombreMateria, int idPrograma) {
        int id = materias.size() + 1;
        materias.add(new Materia(id, nombreMateria));
        System.out.println("[OK] Materia agregada: " + nombreMateria);
    }

    public void editarMateria(int idMateria, String nuevoNombre) {
        for (Materia m : materias) {
            if (m.getIdMateria() == idMateria) {
                m.setNombreMateria(nuevoNombre);
                System.out.println("[OK] Materia editada: " + nuevoNombre);
                return;
            }
        }
        System.out.println("[ERROR] Materia no encontrada.");
    }

    public void eliminarMateria(int idMateria) {
        boolean ok = materias.removeIf(m -> m.getIdMateria() == idMateria);
        System.out.println(ok ? "[OK] Materia eliminada." : "[ERROR] Materia no encontrada.");
    }

    public List<Materia> listarMaterias() { return materias; }

    // ── SEMESTRES ──────────────────────────────────────────
    public void agregarSemestre(int anio, int periodo, int idMateria) {
        int id = semestres.size() + 1;
        semestres.add(new Semestre(id, anio, periodo));
        System.out.println("[OK] Semestre agregado: " + anio + "-" + periodo);
    }

    public void editarSemestre(int idSemestre, int nuevoAnio, int nuevoPeriodo) {
        for (Semestre s : semestres) {
            if (s.getIdSemestre() == idSemestre) {
                s.setAnio(nuevoAnio);
                s.setPeriodo(nuevoPeriodo);
                System.out.println("[OK] Semestre editado: " + nuevoAnio + "-" + nuevoPeriodo);
                return;
            }
        }
        System.out.println("[ERROR] Semestre no encontrado.");
    }

    public void eliminarSemestre(int idSemestre) {
        boolean ok = semestres.removeIf(s -> s.getIdSemestre() == idSemestre);
        System.out.println(ok ? "[OK] Semestre eliminado." : "[ERROR] Semestre no encontrado.");
    }

    public List<Semestre> listarSemestres() { return semestres; }

    // ── CATEGORIAS ─────────────────────────────────────────
    public void agregarCategoria(String nombre, String descripcion) {
        int id = categorias.size() + 1;
        categorias.add(new Categoria(id, nombre, descripcion, ""));
        System.out.println("[OK] Categoria agregada: " + nombre);
    }

    public void editarCategoria(int idCategoria, String nuevoNombre) {
        for (Categoria c : categorias) {
            if (c.getIdCategoria() == idCategoria) {
                c.setNombreCategoria(nuevoNombre);
                System.out.println("[OK] Categoria editada: " + nuevoNombre);
                return;
            }
        }
        System.out.println("[ERROR] Categoria no encontrada.");
    }

    public void eliminarCategoria(int idCategoria) {
        boolean ok = categorias.removeIf(c -> c.getIdCategoria() == idCategoria);
        System.out.println(ok ? "[OK] Categoria eliminada." : "[ERROR] Categoria no encontrada.");
    }

    public List<Categoria> listarCategorias() { return categorias; }

    // ── HELPERS INTERNOS ───────────────────────────────────
    private Facultad buscarFacultadPorId(int id) {
        for (Facultad f : facultades)
            if (f.getIdFacultad() == id) return f;
        return null;
    }

    public void gestionarParametros() {
        System.out.println("[INFO] Gestionando parametros del sistema.");
    }
}