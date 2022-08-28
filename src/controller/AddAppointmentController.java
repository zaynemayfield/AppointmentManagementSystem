package controller;

import dao.AppointmentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.Main;
import model.Appointment;
import model.Time;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * AddAppointmentController class handles everything on AddAppointment.fxml Lambda Expression in this class
 */
public class AddAppointmentController implements Initializable {
    @FXML
    private TextField title, description, location, type, duration;
    @FXML
    private ComboBox contact, customer, user, hour, minute;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label status;

    private final String[] mins = new String[]{"00","10","20","30","40","50"};

    /**
     * sends data to see if null or inappropriate, gets data and constructs start date, checks if date is too soon or
     * overlaps and then sends for inserting appointment
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void saveAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        if (isNotNull()){
            Appointment appointment = new Appointment();
            appointment.setTitle(title.getText());
            appointment.setDescription(description.getText());
            appointment.setLocation(location.getText());
            appointment.setType(type.getText());
            String tempDate = String.valueOf(datePicker.getValue());
            String tempHour = String.valueOf(hour.getValue());
            if (parseInt(tempHour, "Hour") <10){
                tempHour = "0"+tempHour;
            }
            String tempMinute = String.valueOf(minute.getValue());

            String start = tempDate+" "+tempHour+":"+tempMinute+":00";
            if (Time.toESTZoned(start).isBefore(Time.currentESTZoned())){
                status.setText("Status: Date and time before current date and time");
                return;
            }
            appointment.setStart(start);
            long durationLong = Integer.parseInt(duration.getText());
            String closingDate = tempDate + " 22:00:00";
            if (Time.toESTZoned(start).plusMinutes(durationLong).isAfter(Time.closingDate(closingDate))){
                status.setText("Status: Appointment goes past closing time");
                return;
            }
            appointment.setDuration(Integer.parseInt(duration.getText()));
            appointment.setCustomer_ID(Integer.valueOf(String.valueOf(customer.getValue())));
            appointment.setUser_ID(Integer.valueOf(String.valueOf(user.getValue())));
            appointment.setContact(String.valueOf(contact.getValue()));
            if (!appointment.checkOverLappingAppointment()){
                if (appointment.insert()) {
                    Main.goTo(actionEvent, "Appointment");
                } else {
                    status.setText("Status: Appointment failed to create");
                }
            } else {
                status.setText("Status: Appointment Overlaps with another for this customer");
            }

        }
    }

    /**
     * checks if data is null and also appropriate
     * @return
     */
    private boolean isNotNull() {
        if (title.getText().isEmpty()){
            status.setText("Status: Title is empty");
        } else if (description.getText().isEmpty()) {
            status.setText("Status: Description is empty");
        } else if (location.getText().isEmpty()) {
            status.setText("Status: Location is empty");
        } else if (type.getText().isEmpty()) {
            status.setText("Status: Type is empty");
        } else if (String.valueOf(datePicker.getValue()).isEmpty()) {
            status.setText("Status: Date is empty");
        } else if (String.valueOf(hour.getValue()).isEmpty()) {
            status.setText("Status: Hour is empty");
        } else if (String.valueOf(minute.getValue()).isEmpty()) {
            status.setText("Status: Minute is empty");
        } else if (duration.getText().isEmpty()) {
            status.setText("Status: Duration is empty");
        } else if (parseInt(duration.getText(), "Duration") <= 0) {
            status.setText("Status: Duration is less than 0");
        } else if (parseInt(duration.getText(), "Duration") > 60) {
            status.setText("Status: Duration is more than the maximum of 60 minutes");
        } else if (String.valueOf(customer.getValue()).isEmpty()) {
            status.setText("Status: Customer ID is empty");
        } else if (String.valueOf(user.getValue()).isEmpty()) {
            status.setText("Status: User ID is empty");
        } else if (String.valueOf(contact.getValue()).isEmpty()) {
            status.setText("Status: Contact is Empty");
        }
        return true;
    }

    /**
     * checks if a number is a number
     * @param num
     * @param item
     * @return
     */
    public Integer parseInt(String num, String item) {
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException e) {
            status.setText("Status: "+item+" needs to be a number");
            return null;
        }
    }

    /**
     * goes to the Appointment.fxml when cancel button is clicked
     * @param actionEvent
     * @throws IOException
     */
    public void goToAppointment(ActionEvent actionEvent) throws IOException {
        Main.goTo(actionEvent, "Appointment");
    }

    /**
     * LAMBDA EXPRESSION this uses the Arrays.stream to do a forEach on the mins array to add each item to the combo box
     * This would normally take several lines to accomplish but by using the Lambda expression it's done very easily and quickly
     * initializes hours, minutes, customer ids, user ids, and contacts
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setHours();
        //Lambda Expression
        Arrays.stream(mins).forEach(min -> minute.getItems().add(min));
        getCustomerIds();
        getUserIds();
        getContacts();

    }

    /**
     * gets contacts
     */
    private void getContacts() {
        try {
            ResultSet allContacts = AppointmentDAO.getAllContacts();
            while (allContacts.next()) {
                contact.getItems().addAll(allContacts.getString("Contact"));
            }
        } catch (SQLException ex) {

        }
    }

    /**
     * gets user ids
     */
    private void getUserIds() {
        try {
            ResultSet allUserIds = AppointmentDAO.getAllUserIds();
            while (allUserIds.next()) {
                user.getItems().addAll(allUserIds.getString("User_ID"));
            }
        } catch (SQLException ex) {

        }
    }

    /**
     * gets customer ids
     */
    private void getCustomerIds() {
        try {
            ResultSet allCustomerIds = AppointmentDAO.getAllCustomerIds();
            while (allCustomerIds.next()) {
                customer.getItems().addAll(allCustomerIds.getString("Customer_ID"));
            }
        } catch (SQLException ex) {

        }
    }

    /**
     * sets hours in combobox
     */
    private void setHours() {
        int[] hours = new int[]{8,9,10,11,12,13,14,15,16,17,18,19,20,21};
        int est = Time.estOffSet;
        int local = Time.offSet;
        int modify = (est-local)*-1;
        for (int i : hours) {
            hour.getItems().add((i+modify)%24);
        }
    }

}
