package galeria.model;

public class Materia {
	private int idMateria;
	private String nombreMateria;
	public Materia(int idMateria, String nombreMateria) {
		super();
		this.idMateria = idMateria;
		this.nombreMateria = nombreMateria;
	}
	public String getNombreMateria() {
		return nombreMateria;
	}
	public void setNombreMateria(String nombreMateria) {
		this.nombreMateria = nombreMateria;
	}
	public int getIdMateria() {
		return idMateria;
	}
	
}
