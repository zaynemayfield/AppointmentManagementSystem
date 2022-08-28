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
import model.HourAdjustInterface;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * EditAppointmentController handles everything from EditAppointment.fxml Lambda Expression in this class
 */
public class EditAppointmentController implements Initializable {

    @FXML
    private TextField appointmentId,title, description, location, type, duration;
    @FXML
    private ComboBox contact, customer, user, hour, minute;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label status;

    private Appointment appointment;
    private int[] hours = new int[]{8,9,10,11,12,13,14,15,16,17,18,19,20,21};

    /**
     * sends data to see if null or inappropriate, gets data and constructs start date, checks if date is too soon or
     * overlaps and then sends for updating appointment
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void updateAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        if (isNotNull()){
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
                status.setText("Status: Date and time before current date and time. See admin about revising past appointments");
                return;
            }
            appointment.setStart(start);
            long durationLong = Integer.parseInt(duration.getText());
            String closingDate = tempDate+ " 22:00:00";
            if (Time.toESTZoned(start).plusMinutes(durationLong).isAfter(Time.closingDate(closingDate))){
                status.setText("Status: Appointment goes past closing time");
                return;
            }
            appointment.setDuration(Integer.parseInt(duration.getText()));
            appointment.setCustomer_ID(Integer.valueOf(String.valueOf(customer.getValue())));
            appointment.setUser_ID(Integer.valueOf(String.valueOf(user.getValue())));
            appointment.setContact(String.valueOf(contact.getValue()));
            if (!appointment.checkOverLappingAppointment()){
                if (appointment.update()) {
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
     * gets contacts
     */
    private void getContacts() {
        try {
            ResultSet allContacts = AppointmentDAO.getAllContacts();
            while (allContacts.next()) {
                contact.getItems().addAll(allContacts.getString("Contact"));
            }
            contact.getSelectionModel().select(this.appointment.getContact());
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
            user.getSelectionModel().select(String.valueOf(this.appointment.getUser_ID()));
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
            customer.getSelectionModel().select(String.valueOf(this.appointment.getCustomer_ID()));
        } catch (SQLException ex) {

        }
    }
    /**
     * sets minutes
     * */
    private void setMinutes() {
        String[] minutes = new String[]{"00","10","20","30","40","50"};
        for (String i: minutes){
            minute.getItems().add(i);
        }
        minute.getSelectionModel().select(Time.getMinute(this.appointment.getStart()));
    }
    /**
     * sets hours in combobox
     */

    private void setHours(HourAdjustInterface convertLocalToEST, int[] hours) {
        for (int i : hours) {
            this.hour.getItems().add(convertLocalToEST.adjustHour(i));
        }
        this.hour.getSelectionModel().select(Time.getHour(this.appointment.getStart()));
    }
    /**
     * LAMBDA EXPRESSION This uses the HourAdjustment Interface and makes a formula for converting hours to timezone specific hours
     * then pushes the object to a function setHours to run through the array with the formula, later a 12 hour forumla could be use or something else
     * initializes hours, minutes, customer ids, user ids, and contacts and all selected appointment and fills out the forms
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.appointment = Main.getAppointment();
        appointmentId.setText(String.valueOf(this.appointment.getAppointment_ID()));
        title.setText(this.appointment.getTitle());
        description.setText(this.appointment.getDescription());
        location.setText(this.appointment.getLocation());
        type.setText(this.appointment.getType());
        duration.setText(String.valueOf(Time.getDuration(this.appointment.getStart(),this.appointment.getEnd())));
        datePicker.setValue(Time.getDate(this.appointment.getStart()));
        //Lambda Expression
        HourAdjustInterface convertLocalToEST = (n) -> ((n+Time.getOffSet())%24);
        //HourAdjustInterface convertLocalToEST12 = (n) -> ((n+Time.getOffSet())%12);
        setHours(convertLocalToEST, this.hours);
        setMinutes();
        getCustomerIds();
        getUserIds();
        getContacts();

        String tempDate = String.valueOf(datePicker.getValue());
        String tempHour = String.valueOf(hour.getValue());
        if (parseInt(tempHour, "Hour") <10){
            tempHour = "0"+tempHour;
        }
        String tempMinute = String.valueOf(minute.getValue());
        String start = tempDate+" "+tempHour+":"+tempMinute+":00";
        if (Time.toESTZoned(start).isBefore(Time.currentESTZoned())){
            status.setText("Status: Cannot change appointments that have started or in the past in order to preserve records");
            title.setEditable(false);
            title.setDisable(true);
            description.setEditable(false);
            description.setDisable(true);
            location.setEditable(false);
            location.setDisable(true);
            type.setEditable(false);
            type.setDisable(true);
            duration.setEditable(false);
            duration.setDisable(true);
            datePicker.setEditable(false);
            datePicker.setDisable(true);
            contact.setEditable(false);
            contact.setDisable(true);
            user.setEditable(false);
            user.setDisable(true);
            customer.setEditable(false);
            customer.setDisable(true);
            hour.setEditable(false);
            hour.setDisable(true);
            minute.setEditable(false);
            minute.setDisable(true);
            return;
        }

    }
}
