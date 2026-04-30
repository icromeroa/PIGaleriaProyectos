package galeria.dao;

import galeria.model.Usuario;

public class PruebaLogin {
    public static void main(String[] args) {
        UsuarioDAO dao = new UsuarioDAO();

        System.out.println("=== INICIANDO PRUEBA DE LOGIN ===");
        
        // Datos reales de tu script SQL
        String correoPrueba = "admin@gmail.com";
        String clavePrueba = "1234";

        Usuario u = dao.login(correoPrueba, clavePrueba);

        if (u != null) {
            System.out.println("\n[OK] ¡LOGIN EXITOSO!");
            System.out.println("---------------------------");
            // Usamos System.out.println simple para evitar errores de métodos
            System.out.println("¡Bienvenido al sistema!");
            System.out.println("ID del Usuario: " + u.getIdUsuario());
            System.out.println("---------------------------");
        } else {
            System.out.println("\n[X] ERROR: No se pudo iniciar sesión.");
            System.out.println("Verifica que XAMPP esté encendido y los datos sean correctos.");
        }
        
        System.out.println("\n=== FIN DE LA PRUEBA ===");
    }
}