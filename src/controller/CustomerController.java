package controller;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Main;
import model.Customer;
import javafx.scene.control.*;
import model.CustomerCollection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * CustomerController handles everything on Customer.fxml
 */
public class CustomerController implements Initializable {
    @FXML
    private TableView customerTable;
    @FXML
    private TableColumn idCol,nameCol,addressCol,postalCodeCol,phoneCol,countryCol,regionCol;

    ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @FXML
    private Label status;

    private Stage stage;
    private Scene scene;
    private Parent root;
    public void newCustomer(ActionEvent actionEvent) throws IOException {
        Main.goTo(actionEvent, "AddCustomer");
    }

    /**
     * Confirms deletion of the customer then deletes appointments then customer (A2a)
     * @param actionEvent
     */
    public void deleteCustomer(ActionEvent actionEvent) {
        Customer deleteCustomer = (Customer) customerTable.getSelectionModel().getSelectedItem();
        if (deleteCustomer == null){
            status.setText("Status: No customer selected to delete");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer? All appointments associated with this customer will be deleted as well.");
            alert.setTitle("Customers");
            alert.setHeaderText("Delete Confirmation");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (deleteCustomer.delete()) {
                        if (!populateTable()) {
                            status.setText("Status: Failed to Load Customers");
                        } else {
                            status.setText("Status: Customer and associated appointments successfully deleted");
                        }
                    } else {
                        status.setText("Status: An error occurred during deletion of customer");
                    }
                }
            }
            );
        }
    }

    /**
     * Checks if customer selected is null, if not sends customer to EditCustomer to getCustomer (A2b)
     * @param actionEvent
     * @throws IOException
     */
    public void editCustomer(ActionEvent actionEvent) throws IOException {
        Customer editCustomer = (Customer) customerTable.getSelectionModel().getSelectedItem();
        if (editCustomer == null){
            status.setText("Status: No customer selected to edit");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditCustomer.fxml"));
        root = loader.load();
        EditCustomerController editCustomerController = loader.getController();
        editCustomerController.getCustomer(editCustomer);

        scene = new Scene(root);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * goes back to Appointment.fxml
     * @param actionEvent
     * @throws IOException
     */
    public void goToAppointment(ActionEvent actionEvent) throws IOException {
        Main.goTo(actionEvent, "Appointment");
    }

    /**
     * initializes the customer table
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!populateTable()) {
            status.setText("Status: Failed to load customers");
        }
}

    /**
     * method for getting and populating customer table
     * @return
     */
    public boolean populateTable() {
        try {
            customerList = CustomerCollection.getAllCustomers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        customerTable.setItems(customerList);
        idCol.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("Postal_Code"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("Country"));
        regionCol.setCellValueFactory(new PropertyValueFactory<>("Division"));
        return true;
    }
}
