package galeria.model;

import java.util.Date;

public class Guardado {
		private int idGuardado;
		private Usuario usuario;
		private Proyecto proyecto;
		private Date fechaGuardado;
		private double nota;
		public Guardado(int idGuardado, Usuario usuario, Proyecto proyecto, Date fechaGuardado, double nota) {
			super();
		this.idGuardado = idGuardado;
		this.usuario = usuario;
		this.proyecto = proyecto;
		this.fechaGuardado = fechaGuardado;
		this.nota = nota;
	}
	public int getIdGuardado() {
		return idGuardado;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public Proyecto getProyecto() {
		return proyecto;
	}
	public Date getFechaGuardado() {
		return fechaGuardado;
	}
	public double getNota() {
		return nota;
	}
	public void setNota(double nota) {
		this.nota = nota;
	}
	
}
