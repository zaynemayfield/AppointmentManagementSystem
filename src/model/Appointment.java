package model;

import dao.AppointmentDAO;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Appointment class handles creating appointment objects
 */
public class Appointment {
    private int Appointment_ID, Customer_ID, User_ID, Duration;
    private String Title, Description, Location, Contact, Type, Start, End;

    /**
     * Constructor
     * @param appointment
     * @throws SQLException
     */
    public Appointment(ResultSet appointment) throws SQLException {
        this.Appointment_ID = appointment.getInt("Appointment_ID");
        this.Title = appointment.getString("Title");
        this.Description = appointment.getString("Description");
        this.Location = appointment.getString("Location");
        this.Contact = appointment.getString("Contact");
        this.Type = appointment.getString("Type");
        try {
            this.Start = appointment.getString("Start");
            this.End = appointment.getString("End");
        } catch (Exception e) {
            System.out.println("Failed on the Dates");
        }

        this.Customer_ID = appointment.getInt("Customer_ID");
        this.User_ID = appointment.getInt("User_ID");
    }

    /**
     * empty constructor
     */
    public Appointment() {
        this.Appointment_ID = 0;
    }

    /**
     * This sets of the data to send to the DAO to check if appointments for a specific user is coming up
     * @param id
     * @param est
     * @return
     */
    public static ResultSet checkAppointmentSoon(int id, String est) {
        ResultSet rs = AppointmentDAO.checkAppointmentSoon(id, est);
        return rs;
    }

    /**
     * Sends appointment id to dao to get it deleted
     * @return
     */
    public boolean delete() {
        if (AppointmentDAO.delete(this.Appointment_ID)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sends all information to be updated by DAO
     * @return
     */
    public boolean update() {
        if (AppointmentDAO.update(this.Appointment_ID, this.Title, this.Description, this.Location, this.Contact, this.Type, this.Start, this.Duration, this.Customer_ID, this.User_ID)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sends all information for a new appointment to be inserted into db
     * @return
     */
    public boolean insert() {
        if (AppointmentDAO.insert(this.Title, this.Description, this.Location, this.Contact, this.Type, this.Start, this.Duration, this.Customer_ID, this.User_ID)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * gets appointment id
     * @return
     */
    public int getAppointment_ID() {
        return Appointment_ID;
    }

    /**
     * gets customer id
     * @return
     */
    public int getCustomer_ID() {
        return Customer_ID;
    }

    /**
     * sets customer id
     * @param customer_ID
     */
    public void setCustomer_ID(int customer_ID) {
        Customer_ID = customer_ID;
    }

    /**
     * gets user id
     * @return
     */
    public int getUser_ID() {
        return User_ID;
    }

    /**
     * sets user id
     * @param user_ID
     */
    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    /**
     * gets title
     * @return
     */
    public String getTitle() {
        return Title;
    }

    /**
     * sets title
     * @param title
     */
    public void setTitle(String title) {
        Title = title;
    }

    /**
     * gets description
     * @return
     */
    public String getDescription() {
        return Description;
    }

    /**
     * sets description
     * @param description
     */
    public void setDescription(String description) {
        Description = description;
    }

    /**
     * gets location
     * @return
     */
    public String getLocation() {
        return Location;
    }

    /**
     * sets location
     * @param location
     */
    public void setLocation(String location) {
        Location = location;
    }

    /**
     * gets contact name
     * @return
     */
    public String getContact() {
        return Contact;
    }

    /**
     * sets contact name
     * @param contact
     */
    public void setContact(String contact) {
        Contact = contact;
    }

    /**
     * gets type
     * @return
     */
    public String getType() {
        return Type;
    }

    /**
     * sets type
     * @param type
     */
    public void setType(String type) {
        Type = type;
    }

    /**
     * gets start time
     * @return
     */
    public String getStart() {
        return Time.toLocal(Start);
    }

    /**
     * sets start time
     * @param start
     */
    public void setStart(String start) {
        Start = Time.toEST(start);
    }

    /**
     * gets end time
     * @return
     */
    public String getEnd() {
        return Time.toLocal(End);
    }

    /**
     * sets duration
     * @param duration
     */
    public void setDuration(int duration){
        Duration = duration;
    }

    /**
     * Gets information to send to DAO to see if a customer already has an appointment for that time and day
     * @return
     * @throws SQLException
     */
    public boolean checkOverLappingAppointment() throws SQLException {
        ResultSet rs = AppointmentDAO.checkOverLappingAppointment(Customer_ID, Start, Duration, Appointment_ID);
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
    }
}
