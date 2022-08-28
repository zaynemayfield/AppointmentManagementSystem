package model;

import dao.AppointmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AppointmentCollection handles creating collections of appointments
 */
public class AppointmentCollection {
    /**
     * gets all appointments
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        ResultSet appointments = AppointmentDAO.getAllAppointments();
        while (appointments.next()){
            Appointment singleAppointment = new Appointment(appointments);
            allAppointments.add(singleAppointment);
        }

        return allAppointments;
    }

    /**
     * Gets all appointments in next month
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointment> getMonthAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        ResultSet appointments = AppointmentDAO.getMonthAppointments(Time.currentEST());
        while (appointments.next()){
            Appointment singleAppointment = new Appointment(appointments);
            allAppointments.add(singleAppointment);
        }

        return allAppointments;
    }

    /**
     * Gets all appointments in the next week
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointment> getWeekAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        ResultSet appointments = AppointmentDAO.getWeekAppointments(Time.currentEST());
        while (appointments.next()){
            Appointment singleAppointment = new Appointment(appointments);
            allAppointments.add(singleAppointment);
        }

        return allAppointments;
    }

    /**
     * Gets all appointments by contact
     * @param contact
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointment> getContactAppointments(String contact) throws SQLException {
        ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();
        ResultSet appointments = AppointmentDAO.getContactAppointments(contact);
        while (appointments.next()){
            Appointment singleAppointment = new Appointment(appointments);
            contactAppointments.add(singleAppointment);
        }

        return contactAppointments;
    }
}
