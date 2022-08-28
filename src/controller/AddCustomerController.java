package controller;

import dao.CustomerDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.Main;
import model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * AddCustomerController handles everything from AddCustomer.fxml
 */
public class AddCustomerController implements Initializable {
    @FXML
    private Label status;
    @FXML
    private TextField name, address, postalCode, phone;
    @FXML
    private ComboBox country, region;

    /**
     * sends data for verification, then sends data to customer model to get inserted into db then directs to Customer.fxml
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void saveCustomer(ActionEvent actionEvent) throws IOException, SQLException {
        if (isNotNull()){
            Customer customer = new Customer();
            customer.setCustomer_Name(name.getText());
            customer.setAddress(address.getText());
            customer.setPostal_Code(postalCode.getText());
            customer.setPhone(phone.getText());
            customer.setDivision(String.valueOf(region.getValue()));
            customer.setCountry(String.valueOf(country.getValue()));
            if (customer.insert()) {
                Main.goTo(actionEvent, "Customer");
            } else {
                status.setText("Status: Customer failed to create");
            }
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
        } else if (country.getSelectionModel().isEmpty()) {
            status.setText("Status: Country is empty");
            return false;
        } else if (region.getSelectionModel().isEmpty()) {
            status.setText("Status: Region is empty");
            return false;
        } else {
        return true;
        }
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
            //region.getSelectionModel().select(0);
        } catch (SQLException ex) {
            System.out.println("Failed to update region");
        }
    }

    /**
     * initializes country combobox
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ResultSet allCountries = CustomerDAO.getAllCountries();
            while (allCountries.next()) {
                country.getItems().addAll(allCountries.getString("Country"));
            }
        } catch (SQLException ex) {

        }
    }
}
