package galeria.model;

import java.util.Date;

public class Recurso {
	private int idRecurso;
	private String nombreRecurso;
	private String url;
	private String tipo;
	private Date fechaSubida;
	public Recurso(int idRecurso, String nombreRecurso, String url, String tipo, Date fechaSubida) {
		super();
		this.idRecurso = idRecurso;
		this.nombreRecurso = nombreRecurso;
		this.url = url;
		this.tipo = tipo;
		this.setFechaSubida(fechaSubida);
	}
	public int getIdRecurso() {
		return idRecurso;
	}
	public String getNombreRecurso() {
		return nombreRecurso;
	}
	public String getUrl() {
		return url;
	}
	public String getTipo() {
		return tipo;
	}
	public Date getFechaSubida() {
		return fechaSubida;
	}
	public void setFechaSubida(Date fechaSubida) {
		this.fechaSubida = fechaSubida;
	}
	
}
