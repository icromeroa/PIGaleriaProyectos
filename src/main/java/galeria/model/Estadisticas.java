package galeria.model;

public class Estadisticas {
	private int totalProyectos;
	private int totalUsuarios;
	private int totalFacultades;
	private int totalVistas;
	private int totalGuardados;
	private int proyectoMasVisto;
	public Estadisticas(int totalProyectos, int totalUsuarios, int totalFacultades, int totalVistas, int totalGuardados,
			int proyectoMasVisto) {
		super();
		this.totalProyectos = totalProyectos;
		this.totalUsuarios = totalUsuarios;
		this.totalFacultades = totalFacultades;
		this.totalVistas = totalVistas;
		this.totalGuardados = totalGuardados;
		this.proyectoMasVisto = proyectoMasVisto;
	}
	public int getTotalProyectos() {
		return totalProyectos;
	}
	public int getTotalUsuarios() {
		return totalUsuarios;
	}
	public int getTotalFacultades() {
		return totalFacultades;
	}
	public int getTotalVistas() {
		return totalVistas;
	}
	public int getTotalGuardados() {
		return totalGuardados;
	}
	public int getProyectoMasVisto() {
		return proyectoMasVisto;
	}
	
}
