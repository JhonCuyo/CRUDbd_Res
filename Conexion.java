import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    private static final String URL      = "jdbc:mysql://localhost:3306/ressol";
    private static final String USUARIO  = "root";
    private static final String PASSWORD = "123456789j";

    public static Connection getConexion() throws Exception {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}
