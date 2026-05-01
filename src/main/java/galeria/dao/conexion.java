package galeria.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class conexion {

    public static Connection conectar() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/galeria_db",
                "root",
                "s7jeriKo8"
            );
            System.out.println("conexion exitosa ");

        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
        return con;
    }

    public static void main(String[] args) {
        conectar();
    }
}