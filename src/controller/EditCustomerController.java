package controller;

import dao.CustomerDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.Main;
import model.Customer;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * EditCustomerController handles everything from EditCustomer.fxml
 */
public class EditCustomerController {
    @FXML
    private Label status;
    @FXML
    private TextField id, name, address, postalCode, phone;
    @FXML
    private ComboBox country, region;
    private Customer customer;

    /**
     * sends data for verification, then sends data to customer model to get updated in the db then directs to Customer.fxml
     * @param actionEvent
     * @throws IOException
     */
    public void saveCustomer(ActionEvent actionEvent) throws IOException {
        if (isNotNull()){
            this.customer.setCustomer_ID(Integer.parseInt(id.getText()));
            this.customer.setCustomer_Name(name.getText());
            this.customer.setAddress(address.getText());
            this.customer.setPostal_Code(postalCode.getText());
            this.customer.setPhone(phone.getText());
            this.customer.setDivision(String.valueOf(region.getValue()));
            this.customer.setCountry(String.valueOf(country.getValue()));
            if (this.customer.update()) {
                Main.goTo(actionEvent, "Customer");
            } else {
                status.setText("Status: Customer failed to update");
            }
        }

    }

    /**
     * sets all of the customer data into the forms
     * @param editCustomer
     */
    public void getCustomer(Customer editCustomer) {
        this.customer = editCustomer;
        id.setText(String.valueOf(editCustomer.getCustomer_ID()));
        name.setText(editCustomer.getCustomer_Name());
        address.setText(editCustomer.getAddress());
        postalCode.setText(editCustomer.getPostal_Code());
        phone.setText(editCustomer.getPhone());
        try {
            ResultSet allCountries = CustomerDAO.getAllCountries();
            while (allCountries.next()) {
                country.getItems().addAll(allCountries.getString("Country"));
            }
            country.getSelectionModel().select(editCustomer.getCountry());
        } catch (SQLException ex) {

        }
        try {
            ResultSet allDivisions = CustomerDAO.getDivisionsInCountry(editCustomer.getCountry());
            while (allDivisions.next()) {
                region.getItems().addAll(allDivisions.getString("Division"));
            }
            region.getSelectionModel().select(editCustomer.getDivision());
        } catch (SQLException ex) {

        }
    }

    /**
     * checks all the data for null
     * @return
     */
    public boolean isNotNull(){
        if (name.getText().isEmpty()){
            status.setText("Status: Name is empty");
            return false;
        } else if (address.getText().isEmpty()) {
            status.setText("Status: Address is empty");
            return false;
        } else if (postalCode.getText().isEmpty()) {
            status.setText("Status: Postal code is empty");
            return false;
        } else if (phone.getText().isEmpty()) {
            status.setText("Status: Phone is empty");
            return false;
        } else {
            return true;
        }
    }
    /**
     * handles cancel button and goes to Customer.fxml
     * @param actionEvent
     * @throws IOException
     */
    public void goToCustomer(ActionEvent actionEvent) throws IOException {
        Main.goTo(actionEvent, "Customer");
    }
    /**
     * handles updating the first level division when country is selected
     * @param actionEvent
     */
    public void updateRegion(ActionEvent actionEvent) {
        try {
            ResultSet allDivisions = CustomerDAO.getDivisionsInCountry(String.valueOf(country.getValue()));
            region.getItems().clear();
            while (allDivisions.next()) {
                region.getItems().addAll(allDivisions.getString("Division"));
            }
            region.getSelectionModel().select(0);
        } catch (SQLException ex) {
            System.out.println("Failed to update region");
        }
    }
}
