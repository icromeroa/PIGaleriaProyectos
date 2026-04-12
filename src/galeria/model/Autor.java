package galeria.model;

public class Autor {
	private int idAutor;
	private String nombreAutor;
	private String correo;
	public Autor(int idAutor, String nombreAutor, String correo) {
		super();
		this.idAutor = idAutor;
		this.nombreAutor = nombreAutor;
		this.correo = correo;
	}
	public String getNombreAutor() {
		return nombreAutor;
	}
	public void setNombreAutor(String nombreAutor) {
		this.nombreAutor = nombreAutor;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
}
