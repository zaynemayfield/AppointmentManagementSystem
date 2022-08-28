package model;

import dao.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;

/**
 * CustomerCollection deals with making collections of customers
 */
public class CustomerCollection {
    /**
     * gets data from DAO and sends to Customer then to create a list of customers
     * @return
     * @throws Exception
     */
    public static ObservableList<Customer> getAllCustomers() throws Exception {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        ResultSet customers = CustomerDAO.getAllCustomers();
            while (customers.next()){
                Customer singleCustomer = new Customer(customers);
                allCustomers.add(singleCustomer);
            }

        return allCustomers;
    }
}
