package dao;
import java.sql.*;
import java.time.ZoneId;

/**
 * Class that handles the database connection
 */
public class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost:3306/";
    private static final String databaseName = "client_schedule";
    private static ZoneId zone = ZoneId.systemDefault();
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone="+zone;
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static String password = "Passw0rd!";
    private static Connection connection = null;

    /**
     * Allows connection to the database with the preconfigured information with connection, username and password
     */
    public static void makeConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
        } catch(ClassNotFoundException e) {
            System.out.println("Error:" + e.getMessage());
        } catch(SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * Gets the connection for use
     * @return
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Closes the connection to the DB
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}