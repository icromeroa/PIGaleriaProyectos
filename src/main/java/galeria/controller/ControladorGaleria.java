package galeria.controller;

import galeria.dao.*;
import galeria.model.*;

import java.util.ArrayList;
import java.util.List;

public class ControladorGaleria {

    private final ProyectoDAO  proyectoDAO  = new ProyectoDAO();
    private final FacultadDAO  facultadDAO  = new FacultadDAO();
    private final ProgramaDAO  programaDAO  = new ProgramaDAO();
    private final MateriaDAO   materiaDAO   = new MateriaDAO();
    private final SemestreDAO  semestreDAO  = new SemestreDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final AutorDAO     autorDAO     = new AutorDAO();
    private final RecursoDAO   recursoDAO   = new RecursoDAO();

    // ── PROYECTOS ──────────────────────────────────────────
    public void agregarProyecto(Proyecto p) {
        proyectoDAO.insertarProyecto(p);
        for (Autor a : p.getListaAutores())
            autorDAO.insertarAutor(a, p.getIdProyecto());
        for (Recurso r : p.getListaRecursos())
            recursoDAO.insertarRecurso(r, p.getIdProyecto());
    }

    public void editarProyecto(Proyecto p) {
        proyectoDAO.actualizarProyecto(p);
    }

    public void eliminarProyecto(int idProyecto) {
        proyectoDAO.eliminarProyecto(idProyecto);
    }

    public List<Proyecto> consultarCatalogo() {
        return proyectoDAO.listarTodos();
    }

    public Proyecto buscarProyectoPorId(int id) {
        return proyectoDAO.buscarPorId(id);
    }

    public List<Proyecto> filtrarProyectos(FiltroProyecto filtro) {
        List<Proyecto> todos = proyectoDAO.listarTodos();
        if (filtro.estaVacio()) return todos;
        List<Proyecto> resultado = new ArrayList<>();
        for (Proyecto p : todos) {
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

    // ── PARÁMETROS ACADÉMICOS ──────────────────────────────
    public List<Facultad>  listarFacultades()  { return facultadDAO.listar(); }
    public List<Programa>  listarProgramas()   { return programaDAO.listar(); }
    public List<Materia>   listarMaterias()    { return materiaDAO.listar(); }
    public List<Semestre>  listarSemestres()   { return semestreDAO.listar(); }
    public List<Categoria> listarCategorias()  { return categoriaDAO.listar(); }

    public void agregarFacultad(Facultad f)    { facultadDAO.insertarFacultad(f); }
    public void editarFacultad(Facultad f)     { facultadDAO.actualizarFacultad(f); }
    public void eliminarFacultad(int id)       { facultadDAO.eliminarFacultad(id); }

    public void agregarPrograma(Programa p)    { programaDAO.insertarPrograma(p); }
    public void editarPrograma(Programa p)     { programaDAO.actualizarPrograma(p); }
    public void eliminarPrograma(int id)       { programaDAO.eliminarPrograma(id); }

    public void agregarMateria(Materia m)      { materiaDAO.insertarMateria(m); }
    public void editarMateria(Materia m)       { materiaDAO.actualizarMateria(m); }
    public void eliminarMateria(int id)        { materiaDAO.eliminarMateria(id); }

    public void agregarSemestre(Semestre s)    { semestreDAO.insertarSemestre(s); }
    public void editarSemestre(Semestre s)     { semestreDAO.actualizarSemestre(s); }
    public void eliminarSemestre(int id)       { semestreDAO.eliminarSemestre(id); }

    public void agregarCategoria(Categoria c)  { categoriaDAO.insertarCategoria(c); }
    public void editarCategoria(Categoria c)   { categoriaDAO.actualizarCategoria(c); }
    public void eliminarCategoria(int id)      { categoriaDAO.eliminarCategoria(id); }
}