package galeria.model;
import java.util.Date;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
	private int idUsuario;
	private String nombre;
	private String apellido;
	private String correo;
	private String clave;
	private String avatarURL;
	private boolean esAdmin;

	private List<Guardado> listaGuardados = new ArrayList<>();
	private List<RegistroVisualizacion> historialVistas = new ArrayList<>();

	public Usuario(int idUsuario, String nombre, String apellido, String correo, String clave, String avatarURL,
	               boolean esAdmin, List<Guardado> listaGuardados, List<RegistroVisualizacion> historialVistas) {
		super();
		this.idUsuario = idUsuario;
		this.nombre = nombre;
		this.apellido = apellido;
		this.correo = correo;
		this.clave = clave;
		this.avatarURL = avatarURL;
		this.esAdmin = esAdmin;
		this.listaGuardados = listaGuardados;
		this.historialVistas = historialVistas;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getAvatarURL() {
		return avatarURL;
	}

	public void setAvatarURL(String avatarURL) {
		this.avatarURL = avatarURL;
	}

	public boolean getEsAdmin() {
		return esAdmin;
	}

	public void setEsAdmin(boolean esAdmin) {
		this.esAdmin = esAdmin;
	}

	public List<Guardado> getListaGuardados() {
		return listaGuardados;
	}

	public List<RegistroVisualizacion> getHistorialVistas() {
		return historialVistas;
	}

	public String getClave() {
		return clave;
	}

	public void setListaGuardados(List<Guardado> listaGuardados) {
		this.listaGuardados = listaGuardados;
	}

	public void setHistorialVistas(List<RegistroVisualizacion> historialVistas) {
		this.historialVistas = historialVistas;
	}

	private String encriptarClave(String clave) {
		return "ENC_" + clave.hashCode();
	}

	public boolean autenticar(String correo, String clave) {
		return this.correo.equals(correo) && this.clave.equals(encriptarClave(clave));
	}

	public void guardarProyecto(Proyecto p) {
		for (Guardado g : listaGuardados) {
			if (g.getProyecto().getIdProyecto() == p.getIdProyecto()) {
				System.out.println("[INFO] Ya tienes este proyecto guardado.");
				return;
			}
		}
		listaGuardados.add(new Guardado(0, this, p, new Date(), 0.0));
		System.out.println("[OK] Proyecto guardado: " + p.getTitulo());
	}

	public void eliminarProyecto(int idProyecto) {
		listaGuardados.removeIf(g -> g.getProyecto().getIdProyecto() == idProyecto);
		System.out.println("Proyecto con ID " + idProyecto + " eliminado de guardados.");
	}

	public void verPerfil() {
		System.out.println("--- PERFIL DE USUARIO ---");
		System.out.println("Nombre: " + nombre + " " + apellido);
		System.out.println("Correo: " + correo);
		System.out.println("Proyectos Guardados: " + listaGuardados.size());
		System.out.println("Vistas en historial: " + historialVistas.size());
	}

	public void eliminarMiCuenta() {
		this.listaGuardados.clear();
		this.historialVistas.clear();
		this.nombre = "Cuenta_Eliminada";
		this.correo = "";
		this.clave = "";
		System.out.println("LOG: La cuenta del usuario ID " + this.idUsuario + " ha sido desactivada.");
	}

	public void valorarProyecto(Proyecto p, int puntuacion) {
		if (puntuacion < 1 || puntuacion > 5) {
			System.out.println("[ERROR] La puntuacion debe ser entre 1 y 5.");
			return;
		}
		Valoracion v = new Valoracion(0, this, p, puntuacion, new Date());
		p.agregarValoracion(v);
		System.out.println("[OK] Valoracion de " + puntuacion + "/5 registrada en '" + p.getTitulo() + "'.");
	}

	public void registrarVista(Proyecto p) {
		p.contadorVistas();
		RegistroVisualizacion vista = new RegistroVisualizacion(0, this, p, new Date(), "127.0.0.1");
		this.historialVistas.add(vista);
		System.out.println("[OK] Vista registrada en: " + p.getTitulo());
	}
}