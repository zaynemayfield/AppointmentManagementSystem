package controller;

import dao.AppointmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;
import model.Appointment;
import model.AppointmentCollection;
import model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * ReportsController handles everything from Reports.fxml
 */
public class ReportsController implements Initializable {
    @FXML
    private Label statusAppt, statusContact, numCustomers, numAppt;
    @FXML
    private TableView contactTable;
    @FXML
    private TableColumn idCol, titleCol, descriptionCol, typeCol, startCol, endCol, customerIdCol;
    @FXML
    private ComboBox contactName, month, type;

    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    /**
     * goes back to Appointment.fxml
     * @param actionEvent
     * @throws IOException
     */
    public void gotToAppointment(ActionEvent actionEvent) throws IOException {
        Main.goTo(actionEvent, "Appointment");
    }

    /**
     * handles getting types when month is changed
     * @param actionEvent
     */
    public void handleMonth(ActionEvent actionEvent) {
        String monthName = String.valueOf(month.getValue());
        try {
            ResultSet typeByMonth = AppointmentDAO.getAppointmentTypesByMonth(monthName);
            type.getItems().clear();
            while (typeByMonth.next()) {
                type.getItems().addAll(typeByMonth.getString("Type"));
            }
        } catch (SQLException ex) {
            statusAppt.setText("Status: Failed to gets types");
        }
    }

    /**
     * handles getting number of appointments when type is changed
     * @param actionEvent
     */
    public void handleType(ActionEvent actionEvent) {
        String typeName = String.valueOf(type.getValue());
        String monthName = String.valueOf(month.getValue());
        try {
            ResultSet typeNumByMonth = AppointmentDAO.getNumAppointmentTypesByMonth(typeName, monthName);
                typeNumByMonth.next();
                numAppt.setText(typeNumByMonth.getString("TypeNum"));
        } catch (SQLException ex) {
            statusAppt.setText("Status: Failed to get number of appoints in " +monthName+" with type "+typeName);

        }
    }

    /**
     * handles calling for the table to be populated when contact is changed
     * @param actionEvent
     */
    public void handleContact(ActionEvent actionEvent) {
        String contact = String.valueOf(contactName.getValue());
        if (!populateTable(contact)) {
            statusContact.setText("Status: Failed to load appointments");
        }
    }

    /**
     * initializes the total customers report and sets the months and contacts
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            totalCustomerReport();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setMonths();
        setContacts();

    }

    /**
     * sets the contacts in the combobox
     */
    private void setContacts() {
        try {
            ResultSet allContacts = AppointmentDAO.getAllContacts();
            while (allContacts.next()) {
                contactName.getItems().addAll(allContacts.getString("Contact"));
            }
        } catch (SQLException ex) {
            statusContact.setText("Status: Failed to set Contacts");
        }
    }

    /**
     * sets the months in the combobox
     */
    private void setMonths() {
        String[] months = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};
        for (String i: months){
            month.getItems().add(i);
        }
    }

    /**
     * sets the total number of customers to the label
     * @throws SQLException
     */
    private void totalCustomerReport() throws SQLException {
        int totalCustomers = Customer.getTotalCustomers();
        numCustomers.setText(String.valueOf(totalCustomers));
    }

    /**
     * populates the table with appointments from specific contacts
     * @param contact
     * @return
     */
    public boolean populateTable(String contact) {
            try {
                appointmentList = AppointmentCollection.getContactAppointments(contact);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        this.contactTable.setItems(appointmentList);
        idCol.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("Start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("End"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        return true;
    }
}
