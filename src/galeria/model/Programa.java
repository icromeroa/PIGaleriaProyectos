package galeria.model;

public class Programa {
	private int idPrograma;
	private String nombrePrograma;
	public Programa(int idPrograma, String nombrePrograma) {
		super();
		this.idPrograma = idPrograma;
		this.nombrePrograma = nombrePrograma;
	}
	public String getNombrePrograma() {
		return nombrePrograma;
	}
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}
	public int getIdPrograma() {
		return idPrograma;
	}

	public void setIdPrograma(int idPrograma) {
		this.idPrograma = idPrograma;
	}
}
