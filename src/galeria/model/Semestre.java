package galeria.model;

public class Semestre {
	private int idSemestre;
	private int anio;
	private int periodo;
	public Semestre(int idSemestre, int anio, int periodo) {
		super();
		this.idSemestre = idSemestre;
		this.anio = anio;
		this.periodo = periodo;
	}
	public int getAnio() {
		return anio;
	}
	public void setAnio(int anio) {
		this.anio = anio;
	}
	public int getPeriodo() {
		return periodo;
	}
	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}
	public int getIdSemestre() {
		return idSemestre;
	}
	
}
