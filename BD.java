import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BD {
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_apostas";
    private static final String LOGIN = "root";
    private static final String SENHA = "sua_senha_aqui";
    private Connection connection;

    public boolean getConnection() {
        try {
            connection = DriverManager.getConnection(URL, LOGIN, SENHA);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Connection getConn() { return this.connection; }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}