package galeria.model;

public class Facultad {
	private int idFacultad;
	private String nombreFacultad;
	public Facultad(int idFacultad, String nombreFacultad) {
		super();
		this.idFacultad = idFacultad;
		this.nombreFacultad = nombreFacultad;
	}
	public String getNombreFacultad() {
		return nombreFacultad;
	}
	public void setNombreFacultad(String nombreFacultad) {
		this.nombreFacultad = nombreFacultad;
	}
	public int getIdFacultad() {
		return idFacultad;
	}
	public void setIdFacultad(int idFacultad) {
		this.idFacultad = idFacultad;
	}
}
