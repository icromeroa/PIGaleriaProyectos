package galeria.model;

public class Categoria {
	private int idCategoria;
	private String nombreCategoria;
	private String descripcion;
	private String icono;
	public Categoria(int idCategoria, String nombreCategoria, String descripcion, String icono) {
		super();
		this.idCategoria = idCategoria;
		this.nombreCategoria = nombreCategoria;
		this.descripcion = descripcion;
		this.icono = icono;
	}
	public String getNombreCategoria() {
		return nombreCategoria;
	}
	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getIdCategoria() {
		return idCategoria;
	}
	public String getIcono() {
		return icono;
	}
	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}
	public void setIcono(String icono) {
		this.icono = icono;
	}
	
}
