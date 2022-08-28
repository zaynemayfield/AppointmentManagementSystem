package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * UserDAO class handles all the accessing information from the DB for user
 */
public class UserDAO {

    /**
     * Takes username and password and checks it against the db and returns the userId or 0
     * @param username
     * @param password
     * @return
     */
    public static int checkUser(String username, String password) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = JDBC.getConnection().createStatement();
            rs = stmt.executeQuery("SELECT * FROM users WHERE User_Name = '" + username + "';");
            rs.next();
            if (rs.getString("User_Name").equals(username)) {
                if (rs.getString("Password").equals(password)) {
                    return rs.getInt("User_ID");
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return 0;
        }

    }

}
