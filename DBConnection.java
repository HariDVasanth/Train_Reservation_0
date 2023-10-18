import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "Hariharan9@");
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        connection.close();
    }
}
