package controller;

import dao.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * AppointmentController handles everything on the Appointment.fxml
 */
public class AppointmentController implements Initializable {
    @FXML
    private Label title, status;
    @FXML
    private TableView appointmentTable;
    @FXML
    private TableColumn idCol, titleCol, descriptionCol, locationCol, contactCol, typeCol, startCol, endCol, customerIdCol, userIdCol;
    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    /**
     * goes to AddAppointment.fxml
     * @param actionEvent
     * @throws IOException
     */
    public void newAppointment(ActionEvent actionEvent) throws IOException {
        Main.goTo(actionEvent, "AddAppointment");
    }

    /**
     * confirms with user then deletes the appointment
     * @param actionEvent
     */
    public void deleteAppointment(ActionEvent actionEvent) {
        Appointment deleteAppointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();
        if (deleteAppointment == null){
            status.setText("Status: No appointment selected to delete");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment? Appointment Id: " + deleteAppointment.getAppointment_ID() + " and Type: " +deleteAppointment.getType());
            alert.setTitle("Appointments");
            alert.setHeaderText("Delete Confirmation");
            alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            if (deleteAppointment.delete()) {
                                if (!populateTable(1)) {
                                    status.setText("Status: Failed to Load Appointments");
                                } else {
                                    status.setText("Status: Appointment successfully deleted");
                                }
                            } else {
                                status.setText("Status: An error occurred during deletion of appointment");
                            }
                        }
                    }
            );
        }
    }

    /**
     * goes to the EditAppointment.fxml
     * @param actionEvent
     * @throws IOException
     */
    public void editAppointment(ActionEvent actionEvent) throws IOException {
        Appointment editAppointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();
        if (editAppointment == null){
            status.setText("Status: No appointment selected to edit");
            return;
        }
        Main.setAppointment(editAppointment);
        Main.goTo(actionEvent, "EditAppointment");
    }

    /**
     * Logs out the user goes to Main.fxml, closes db connection, nulls out user, and sets needAlert to true
     * @param actionEvent
     * @throws IOException
     */
    public void logout(ActionEvent actionEvent) throws IOException {
        Main.needAlert = true;
        JDBC.closeConnection();
        User noUser = new User();
        Main.setUser(noUser);
        Main.goTo(actionEvent, "Main");
    }

    /**
     * sets the username in the title and loads the table information for appointments
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title.setText("Appointment View - Welcome User: " + Main.getUser().getUsername());
        if (!populateTable(1)) {
            status.setText("Status: Failed to load appointments");
        }
        try {
            checkAppointmentSoon();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * it checks and displays message when appointment within 15 minutes or tells the user there isn't
     * */
    private void checkAppointmentSoon() throws SQLException {
        if (Main.needAlert) {
            ResultSet appointmentSoon = Appointment.checkAppointmentSoon(Main.getUser().getId(), Time.currentEST());
            if (!appointmentSoon.next()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have no appointments soon.");
                alert.setTitle("Appointments");
                alert.setHeaderText("Appointment Confirmation");
                alert.showAndWait().ifPresent(response -> Main.needAlert = false);

            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have an appointments soon. Appointment: " + appointmentSoon.getInt("Appointment_ID") + " at " + Time.toLocal(appointmentSoon.getString("Start")));
                alert.setTitle("Appointments");
                alert.setHeaderText("Appointment Confirmation");
                alert.showAndWait().ifPresent(response -> Main.needAlert = false);
            }
        }
    }

    /**
     * goes to the Customer.fxml
     * @param actionEvent
     * @throws IOException
     */
    public void customers(ActionEvent actionEvent) throws IOException {
        Main.goTo(actionEvent, "Customer");
    }

    /**
     * goes to the Reports.fxml
     * @param actionEvent
     * @throws IOException
     */
    public void gotToReports(ActionEvent actionEvent) throws IOException {
        Main.goTo(actionEvent, "Reports");
    }

    /**
     * takes input and populates the table with all, month, or week appointments
     * @param view
     * @return
     */
    public boolean populateTable(int view) {
        if (view == 3){
            try {
                appointmentList = AppointmentCollection.getWeekAppointments();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (view == 2) {
            try {
                appointmentList = AppointmentCollection.getMonthAppointments();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                appointmentList = AppointmentCollection.getAllAppointments();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        this.appointmentTable.setItems(appointmentList);
        idCol.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("Start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("End"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("User_ID"));
        return true;
    }

    /**
     * handles radio button change for month view
     * @param actionEvent
     */
    public void monthView(ActionEvent actionEvent) {
        if (!populateTable(2)) {
            status.setText("Status: Failed to load appointments");
        }

    }

    /**
     * handles radio button change for week view
     * @param actionEvent
     */
    public void weekView(ActionEvent actionEvent) {
        if (!populateTable(3)) {
            status.setText("Status: Failed to load appointments");
        }
    }

    /**
     * handles radio button change for all view
     * @param actionEvent
     */
    public void allView(ActionEvent actionEvent) {
        if (!populateTable(1)) {
            status.setText("Status: Failed to load appointments");
        }
    }
}
