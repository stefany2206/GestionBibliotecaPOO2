import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/bibliotecaDB";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errooooooor al conectar con la base de datos:\n" + e.getMessage());
            return null;
        }
    }
}
