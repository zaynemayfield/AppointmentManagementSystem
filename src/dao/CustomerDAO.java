package dao;

import java.sql.*;

/**
 * CustomerDAO class deals with transactions of the db for customer
 */
public class CustomerDAO {
    /**
     * gets all customers from db
     * @return
     * @throws Exception
     */
    public static ResultSet getAllCustomers() throws Exception {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = JDBC.getConnection().createStatement();
             return rs = stmt.executeQuery("SELECT c.Customer_ID, c.Customer_Name, c.Address,c.Postal_Code,c.Phone,f.Division, countries.Country FROM customers c JOIN first_level_divisions f ON c.Division_ID = f.Division_ID JOIN countries ON f.Country_ID = countries.Country_ID;");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return rs;
    }

    /**
     * gets first level division from db according to country
     * @param country
     * @return
     * @throws SQLException
     */
    public static ResultSet getDivisionsInCountry(String country) throws SQLException {
        Statement stmt;
        ResultSet rs = null;
        stmt = JDBC.getConnection().createStatement();
        return rs = stmt.executeQuery("SELECT Division FROM first_level_divisions f JOIN countries c ON c.Country_ID = f.Country_ID WHERE c.Country ='" +country+"';");

    }

    /**
     * gets all countries from db
     * @return
     * @throws SQLException
     */
    public static ResultSet getAllCountries() throws SQLException {
        Statement stmt;
        ResultSet rs = null;
        stmt = JDBC.getConnection().createStatement();
        return rs = stmt.executeQuery("SELECT Country FROM countries;");

    }

    /**
     * deletes customer from db where id
     * @param id
     * @return
     */
    public static boolean delete(int id) {
        Statement stmt;
        try {
            stmt = JDBC.getConnection().createStatement();
            String queryAppointments = "DELETE FROM appointments WHERE Customer_ID = " + id +";";
            String queryCustomers = "DELETE FROM customers WHERE Customer_ID = " + id +";";
            stmt.executeUpdate(queryAppointments);
            stmt.executeUpdate(queryCustomers);
            return true;
        } catch (SQLException ex){
            return false;
        }

    }

    /**
     * updates customer in db
     * @param customer_id
     * @param customer_name
     * @param address
     * @param postal_code
     * @param phone
     * @param division
     * @param country
     * @return
     */
    public static boolean update(int customer_id, String customer_name, String address, String postal_code, String phone, String division, String country) {
        int division_id = getDivisionId(division);
        if (division_id == 0){
            return false;
        }

        Connection conn = JDBC.getConnection();
        String updateCustomerQuery = "UPDATE customers SET Customer_Name=?, Address=?, Postal_Code=?, Phone=?, Division_ID=? WHERE Customer_ID=?;";
        try {
            PreparedStatement ps = conn.prepareStatement(updateCustomerQuery);
            ps.setString(1, customer_name);
            ps.setString(2, address);
            ps.setString(3,postal_code);
            ps.setString(4,phone);
            ps.setInt(5,division_id);
            ps.setInt(6,customer_id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * gets division id
     * @param division
     * @return
     */
    private static int getDivisionId(String division) {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = JDBC.getConnection().createStatement();
            rs = stmt.executeQuery("SELECT Division_ID FROM first_level_divisions WHERE Division ='" + division +"';");
            rs.next();
            return rs.getInt("Division_ID");
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }

    }

    /**
     * inserts new customer into db
     * @param customer_name
     * @param address
     * @param postal_code
     * @param phone
     * @param division
     * @param country
     * @return
     */
    public static boolean insert(String customer_name, String address, String postal_code, String phone, String division, String country) {
        int division_id = getDivisionId(division);
        if (division_id == 0){
            return false;
        }

        Connection conn = JDBC.getConnection();
        String updateCustomerQuery = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?,?,?,?,?);";
        try {
            PreparedStatement ps = conn.prepareStatement(updateCustomerQuery);
            ps.setString(1, customer_name);
            ps.setString(2, address);
            ps.setString(3,postal_code);
            ps.setString(4,phone);
            ps.setInt(5,division_id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * gets the total number of customers
     * @return
     */
    public static ResultSet getTotalCustomers() {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = JDBC.getConnection().createStatement();
            return rs = stmt.executeQuery("SELECT COUNT(*) as totalCustomers FROM customers;");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return rs;
    }
}