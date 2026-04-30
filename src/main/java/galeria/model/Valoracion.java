package galeria.model;

import java.util.Date;

public class Valoracion {
	private int idValoracion;
	private Usuario usuario;
	private Proyecto proyecto;
	private int puntuacion;
	private Date fechaValoracion;
	pugblic Valoracion(int idValoracion, Usuario usuario, Proyecto proyecto, int puntuacion, Date fechaValoracion) {
		super();
		this.idValoracion = idValoracion;
		this.usuario = usuario;
		this.proyecto = proyecto;
		this.puntuacion = puntuacion;
		this.fechaValoracion = fechaValoracion;
	}
	public int getIdValoracion() {
		return idValoracion;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Proyecto getProyecto() {
		return proyecto;
	}
	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}
	public int getPuntuacion() {
		return puntuacion;
	}
	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}
	public Date getFechaValoracion() {
		return fechaValoracion;
	}
	public void setFechaValoracion(Date fechaValoracion) {
		this.fechaValoracion = fechaValoracion;
	}
	
}
