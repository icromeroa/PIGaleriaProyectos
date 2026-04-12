package galeria.model;

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
	
	private String encriptarClave(String clave) {
		return "ENC_" + clave.hashCode();
	}
	
	public boolean autenticar(String correo, String clave) {
		
	}
	
	public void guardarProyecto(Proyecto p) {
		
	}
	
	public void eliminarProyecto(int idProyecto) {
		
	}
	
	public void verPerfil() {
		
	}
	
	public void eliminarMiCuenta() {
		
	}
	
	public void valorarProyecto() {
		
	}
}
