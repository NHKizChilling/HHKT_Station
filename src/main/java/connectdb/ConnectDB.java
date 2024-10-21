package connectdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    public static Connection connection = null;
    private static ConnectDB instance = new ConnectDB();

    public static Connection getConnection() {
        return connection;
    }
    public static void connect() throws SQLException {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=QLBanVeTau";
        String username = "sa";
        String password = "sapassword";


        connection = DriverManager.getConnection(url, username, password);

    }
    public static ConnectDB getInstance() {
        return instance;
    }

    public static void disconnect() {

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            connect();
            System.out.println("Kết nối thành công");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
