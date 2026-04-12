package galeria.model;

public class FiltroProyecto {
	private Integer idFacultad;
	private Integer idPrograma;
	private Integer idMateria;
	private Integer idSemestre;
	private Integer idCategoria;
	private String textoBusqueda;
	private String ordenarPor;
	public FiltroProyecto(Integer idFacultad, Integer idPrograma, Integer idMateria, Integer idSemestre,
			Integer idCategoria, String textoBusqueda, String ordenarPor) {
		super();
		this.idFacultad = idFacultad;
		this.idPrograma = idPrograma;
		this.idMateria = idMateria;
		this.idSemestre = idSemestre;
		this.idCategoria = idCategoria;
		this.textoBusqueda = textoBusqueda;
		this.ordenarPor = ordenarPor;
	}
	public Integer getIdFacultad() {
		return idFacultad;
	}
	public void setIdFacultad(Integer idFacultad) {
		this.idFacultad = idFacultad;
	}
	public Integer getIdPrograma() {
		return idPrograma;
	}
	public void setIdPrograma(Integer idPrograma) {
		this.idPrograma = idPrograma;
	}
	public Integer getIdMateria() {
		return idMateria;
	}
	public void setIdMateria(Integer idMateria) {
		this.idMateria = idMateria;
	}
	public Integer getIdSemestre() {
		return idSemestre;
	}
	public void setIdSemestre(Integer idSemestre) {
		this.idSemestre = idSemestre;
	}
	public Integer getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}
	public String getTextoBusqueda() {
		return textoBusqueda;
	}
	public void setTextoBusqueda(String textoBusqueda) {
		this.textoBusqueda = textoBusqueda;
	}
	public String getOrdenarPor() {
		return ordenarPor;
	}
	public void setOrdenarPor(String ordenarPor) {
		this.ordenarPor = ordenarPor;
	}
	public boolean estaVacio() {
		
	}
	public List<Proyecto> aplicarFiltro() {
		
	}
}
