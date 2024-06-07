import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/projectfinale";
    private static final String USER = "postgres";
    private static final String PASSWORD = "2468";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                System.out.println("Conexão estabelecida com sucesso!");
            } else {
                System.out.println("Falha ao estabelecer conexão!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
