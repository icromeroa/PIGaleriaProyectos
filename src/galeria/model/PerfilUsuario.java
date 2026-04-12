package galeria.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PerfilUsuario {
	private Usuario usuario;
	private int cantidadGuardados;
	private int cantidadValoraciones;
	private List<Proyecto> listaProyectosSubidos = new ArrayList<>()
	private Date fechaRegistro;
	public PerfilUsuario(Usuario usuario, int cantidadGuardados, int cantidadValoraciones,
			List<Proyecto> listaProyectosSubidos, Date fechaRegistro) {
		super();
		this.usuario = usuario;
		this.cantidadGuardados = cantidadGuardados;
		this.cantidadValoraciones = cantidadValoraciones;
		this.listaProyectosSubidos = listaProyectosSubidos;
		this.fechaRegistro = fechaRegistro;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public int getCantidadGuardados() {
		return cantidadGuardados;
	}
	public int getCantidadValoraciones() {
		return cantidadValoraciones;
	}
	public List<Proyecto> getListaProyectosSubidos() {
		return listaProyectosSubidos;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	
}
