package galeria.model;

import java.util.Date;

public class RegistroVisualizacion {
	private int idVista;
	private Usuario usuario;
	private Proyecto proyecto;
	private Date fechaVista;
	private String ipSesion;
	public RegistroVisualizacion(int idVista, Usuario usuario, Proyecto proyecto, Date fechaVista, String ipSesion) {
		super();
		this.idVista = idVista;
		this.usuario = usuario;
		this.proyecto = proyecto;
		this.fechaVista = fechaVista;
		this.ipSesion = ipSesion;
	}
	public int getIdVista() {
		return idVista;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public Proyecto getProyecto() {
		return proyecto;
	}
	public Date getFechaVista() {
		return fechaVista;
	}
	public String getIpSesion() {
		return ipSesion;
	}
}
