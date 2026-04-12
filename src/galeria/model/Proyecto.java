package galeria.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Proyecto {
	private int idProyecto;
	private String titulo;
	private String resumen;
	private String archivoURL;
	private String portadaURL;
	private int cantidadVistas;
	private int cantidadGuardados;
	private Date fechaSubida;
	private List<Autor> listaAutores = new ArrayList<>();
	private List<Recurso> listaRecursos = new ArrayList<>();
	private Facultad facultad;
	private Programa programa;
	private Materia materia;
	private Semestre semestre;
	private Categoria categoria;
	public Proyecto(int idProyecto, String titulo, String resumen, String archivoURL, String portadaURL,
			int cantidadVistas, int cantidadGuardados, Date fechaSubida, List<Autor> listaAutores,
			List<Recurso> listaRecursos, Facultad facultad, Programa programa, Materia materia, Semestre semestre,
			Categoria categoria) {
		super();
		this.idProyecto = idProyecto;
		this.titulo = titulo;
		this.resumen = resumen;
		this.archivoURL = archivoURL;
		this.portadaURL = portadaURL;
		this.cantidadVistas = cantidadVistas;
		this.cantidadGuardados = cantidadGuardados;
		this.fechaSubida = fechaSubida;
		this.listaAutores = listaAutores;
		this.listaRecursos = listaRecursos;
		this.facultad = facultad;
		this.programa = programa;
		this.materia = materia;
		this.semestre = semestre;
		this.categoria = categoria;
	}
	public int getIdProyecto() {
		return idProyecto;
	}
	public void setIdProyecto(int idProyecto) {
		this.idProyecto = idProyecto;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getResumen() {
		return resumen;
	}
	public void setResumen(String resumen) {
		this.resumen = resumen;
	}
	public String getArchivoURL() {
		return archivoURL;
	}
	public void setArchivoURL(String archivoURL) {
		this.archivoURL = archivoURL;
	}
	public String getPortadaURL() {
		return portadaURL;
	}
	public void setPortadaURL(String portadaURL) {
		this.portadaURL = portadaURL;
	}
	public int getCantidadVistas() {
		return cantidadVistas;
	}
	public void setCantidadVistas(int cantidadVistas) {
		this.cantidadVistas = cantidadVistas;
	}
	public int getCantidadGuardados() {
		return cantidadGuardados;
	}
	public void setCantidadGuardados(int cantidadGuardados) {
		this.cantidadGuardados = cantidadGuardados;
	}
	public Date getFechaSubida() {
		return fechaSubida;
	}
	public void setFechaSubida(Date fechaSubida) {
		this.fechaSubida = fechaSubida;
	}
	public List<Autor> getListaAutores() {
		return listaAutores;
	}
	public void setListaAutores(List<Autor> listaAutores) {
		this.listaAutores = listaAutores;
	}
	public List<Recurso> getListaRecursos() {
		return listaRecursos;
	}
	public void setListaRecursos(List<Recurso> listaRecursos) {
		this.listaRecursos = listaRecursos;
	}
	public Facultad getFacultad() {
		return facultad;
	}
	public void setFacultad(Facultad facultad) {
		this.facultad = facultad;
	}
	public Programa getPrograma() {
		return programa;
	}
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	public Materia getMateria() {
		return materia;
	}
	public void setMateria(Materia materia) {
		this.materia = materia;
	}
	public Semestre getSemestre() {
		return semestre;
	}
	public void setSemestre(Semestre semestre) {
		this.semestre = semestre;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public void registrarVista() {
		
	}
	public float valoracionPromedio() {
		
	}
	public boolean esDestacado() {
		
	}
	public void visualizarDetalle() {
		
	}
	public void descargarArchivo() {
		
	}
	
}
