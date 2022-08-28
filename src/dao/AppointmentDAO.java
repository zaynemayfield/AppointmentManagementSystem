package dao;
import java.sql.*;

/**
 * AppointmentDAO handles all the transactions with the DB
 */
public class AppointmentDAO {
    /**
     * gets all appointments
     * @return
     */
    public static ResultSet getAllAppointments() {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = JDBC.getConnection().createStatement();
            return rs = stmt.executeQuery("SELECT a.Appointment_ID, a.Title, a.Description, a.Location, c.Contact_Name as Contact, a.Type, a.Start, a.End, a.Customer_ID, a.User_ID FROM appointments a JOIN contacts c ON a.Contact_ID = c.Contact_ID;");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return rs;
    }

    /**
     * deletes appointments
     * @param id
     * @return
     */
    public static boolean delete(int id) {
        Statement stmt;
        try {
            stmt = JDBC.getConnection().createStatement();
            String queryAppointments = "DELETE FROM appointments WHERE Appointment_ID = "+id+";";
            stmt.executeUpdate(queryAppointments);
            return true;
        } catch (SQLException ex){
            return false;
        }
    }

    /**
     * updates appointments
     * @param appointment_id
     * @param title
     * @param description
     * @param location
     * @param contact
     * @param type
     * @param start
     * @param duration
     * @param customer_id
     * @param user_id
     * @return
     */
    public static boolean update(int appointment_id, String title, String description, String location, String contact, String type, String start, int duration, int customer_id, int user_id) {
        int contact_id = getContactId(contact);
        if (contact_id == 0){
            return false;
        }
        Connection conn = JDBC.getConnection();
        String updateAppointmentQuery = "UPDATE appointments SET Title=?, Description=?, Location =?, Type=?, Start=?, End=(DATE_ADD('"+start+"',INTERVAL "+duration+" MINUTE)), Customer_ID=?, User_ID=?, Contact_ID=? WHERE Appointment_ID=?;";
        try {
            PreparedStatement ps = conn.prepareStatement(updateAppointmentQuery);

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setString(5, start);
            ps.setInt(6,customer_id);
            ps.setInt(7,user_id);
            ps.setInt(8,contact_id);
            ps.setInt(9, appointment_id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * gets contact_id
     * @param contact
     * @return
     */
    private static int getContactId(String contact) {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = JDBC.getConnection().createStatement();
            rs = stmt.executeQuery("SELECT Contact_ID FROM contacts WHERE Contact_Name ='" + contact +"';");
            rs.next();
            return rs.getInt("Contact_ID");
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
    }

    /**
     * inserts new appointment
     * @param title
     * @param description
     * @param location
     * @param contact
     * @param type
     * @param start
     * @param duration
     * @param customer_id
     * @param user_id
     * @return
     */
    public static boolean insert(String title, String description, String location, String contact, String type, String start, int duration, int customer_id, int user_id) {
        int contact_id = getContactId(contact);
        if (contact_id == 0){
            return false;
        }
        Connection conn = JDBC.getConnection();
        String updateAppointmentQuery = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,(DATE_ADD('"+start+"',INTERVAL "+duration+" MINUTE)),?,?,?);";
        try {
            PreparedStatement ps = conn.prepareStatement(updateAppointmentQuery);

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setString(5, start);
            ps.setInt(6,customer_id);
            ps.setInt(7,user_id);
            ps.setInt(8,contact_id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * checks to see if there is an appointment happening soon for a user
     * @param id
     * @param est
     * @return
     */
    public static ResultSet checkAppointmentSoon(int id, String est) {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = JDBC.getConnection().createStatement();

            return rs = stmt.executeQuery("SELECT Appointment_ID, Start FROM appointments WHERE User_ID ="+id+" AND Start <= DATE_ADD('"+est+"',INTERVAL 15 MINUTE) AND Start > '"+est+"';");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return rs;
    }

    /**
     * gets all appointments in the next week
     * @param currentDateTime
     * @return
     */
    public static ResultSet getWeekAppointments(String currentDateTime) {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = JDBC.getConnection().createStatement();
            return rs = stmt.executeQuery("SELECT a.Appointment_ID, a.Title, a.Description, a.Location, c.Contact_Name as Contact, a.Type, a.Start, a.End, a.Customer_ID, a.User_ID FROM appointments a JOIN contacts c ON a.Contact_ID = c.Contact_ID WHERE a.Start < DATE_ADD('"+currentDateTime+"',INTERVAL 7 DAY) AND a.Start > '"+currentDateTime+"';");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return rs;
    }

    /**
     * gets all appointments in the next month
     * @param currentDateTime
     * @return
     */
    public static ResultSet getMonthAppointments(String currentDateTime) {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = JDBC.getConnection().createStatement();
            return rs = stmt.executeQuery("SELECT a.Appointment_ID, a.Title, a.Description, a.Location, c.Contact_Name as Contact, a.Type, a.Start, a.End, a.Customer_ID, a.User_ID FROM appointments a JOIN contacts c ON a.Contact_ID = c.Contact_ID WHERE a.Start < DATE_ADD('"+currentDateTime+"',INTERVAL 1 MONTH) AND a.Start > '"+currentDateTime+"';");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return rs;
    }

    /**
     * gets all contacts
     * @return
     */
    public static ResultSet getAllContacts() {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = JDBC.getConnection().createStatement();
            return rs = stmt.executeQuery("SELECT Contact_Name as Contact FROM contacts;");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return rs;
    }

    /**
     * gets all user ids
     * @return
     */
    public static ResultSet getAllUserIds() {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = JDBC.getConnection().createStatement();
            return rs = stmt.executeQuery("SELECT User_ID FROM users;");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return rs;
    }

    /**
     * gets all customer ids
     * @return
     */
    public static ResultSet getAllCustomerIds() {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = JDBC.getConnection().createStatement();
            return rs = stmt.executeQuery("SELECT Customer_ID FROM customers;");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return rs;
    }

    /**
     * checks for over lapping appointments for customers
     *
     * @param customer_id
     * @param start
     * @param duration
     * @param appointment_ID
     * @return
     */
    public static ResultSet checkOverLappingAppointment(int customer_id, String start, int duration, int appointment_ID) {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = JDBC.getConnection().createStatement();

            return rs = stmt.executeQuery("SELECT * FROM appointments WHERE Customer_ID = "+customer_id+" AND Appointment_ID != "+appointment_ID+" AND" +
                    " ((Start BETWEEN '"+start+"' AND DATE_ADD('"+start+"',INTERVAL "+duration+" MINUTE)) OR " +
                    "(End BETWEEN '"+start+"' AND DATE_ADD('"+start+"',INTERVAL "+duration+" MINUTE) ) OR " +
                    "Start <= '"+start+"' AND End >= DATE_ADD('"+start+"',INTERVAL "+duration+" MINUTE));");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return rs;
    }

    /**
     * gets appointments for specific contact
     * @param contact
     * @return
     */
    public static ResultSet getContactAppointments(String contact) {
        Statement stmt;
        ResultSet rs = null;
        int contact_id = getContactId(contact);
        if (contact_id == 0){
            return rs;
        }
        try {
            stmt = JDBC.getConnection().createStatement();
            return rs = stmt.executeQuery("SELECT a.Appointment_ID, a.Title, a.Description, a.Location, c.Contact_Name as Contact, a.Type, a.Start, a.End, a.Customer_ID, a.User_ID FROM appointments a JOIN contacts c ON a.Contact_ID = c.Contact_ID WHERE a.Contact_ID = "+contact_id+";");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return rs;
    }

    /**
     * gets appointment types by a specific month
     * @param monthName
     * @return
     */
    public static ResultSet getAppointmentTypesByMonth(String monthName) {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = JDBC.getConnection().createStatement();
            return rs = stmt.executeQuery("SELECT Type FROM appointments WHERE MONTHNAME(Start) = '"+monthName+"'  GROUP BY type;");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return rs;
    }

    /**
     * gets number of appointments by month and type
     * @param typeName
     * @param monthName
     * @return
     */
    public static ResultSet getNumAppointmentTypesByMonth(String typeName, String monthName) {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = JDBC.getConnection().createStatement();
            return rs = stmt.executeQuery("SELECT COUNT(*) AS TypeNum FROM appointments WHERE MONTHNAME(Start) = '"+monthName+"' AND Type = '"+typeName+"';");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return rs;
    }
}
