package model;

import dao.CustomerDAO;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Customer class handles creating customers
 */
public class Customer {
    private int Customer_ID;
    private String Customer_Name, Address, Postal_Code, Phone, Division, Country;

    /**
     * Constructor
     * @param customer
     * @throws SQLException
     */
    public Customer(ResultSet customer) throws SQLException {
        this.Customer_ID = customer.getInt("Customer_ID");
        this.Customer_Name = customer.getString("Customer_Name");
        this.Address = customer.getString("Address");
        this.Postal_Code = customer.getString("Postal_Code");
        this.Phone = customer.getString("Phone");
        this.Division = customer.getString("Division");
        this.Country = customer.getString("Country");

    }

    /**
     * empty constructor
     */
    public Customer() {

    }

    /**
     * gets total amount of customers
     * @return
     * @throws SQLException
     */
    public static int getTotalCustomers() throws SQLException {
        ResultSet totalCustomers = CustomerDAO.getTotalCustomers();
        totalCustomers.next();
        return totalCustomers.getInt("totalCustomers");
    }

    /**
     * gets customer name
     * @return
     */
    public String getCustomer_Name() {
        return this.Customer_Name;
    }

    /**
     * sets customer name
     * @param v
     */
    public void setCustomer_Name(String v){
        this.Customer_Name = v;
    }

    /**
     * gets customer id
     * @return
     */
    public int getCustomer_ID(){
        return this.Customer_ID;
    }

    /**
     * sets customer id
     * @param v
     */
    public void setCustomer_ID(int v){
        this.Customer_ID = v;
    }

    /**
     * gets address
     * @return
     */
    public String getAddress(){
        return this.Address;
    }

    /**
     * sets address
     * @param v
     */
    public void setAddress(String v){
        this.Address = v;
    }

    /**
     * gets postal code
     * @return
     */
    public String getPostal_Code(){
        return this.Postal_Code;
    }

    /**
     * sets postal code
     * @param v
     */
    public void setPostal_Code(String v){
        this.Postal_Code = v;
    }

    /**
     * get phone number
     * @return
     */
    public String getPhone(){
        return this.Phone;
    }

    /**
     * sets phone number
     * @param v
     */
    public void setPhone(String v){
        this.Phone = v;
    }

    /**
     * get first level division
     * @return
     */
    public String getDivision(){
        return this.Division;
    }

    /**
     * sets first level division
     * @param v
     */
    public void setDivision(String v){
        this.Division = v;
    }

    /**
     * get country
     * @return
     */
    public String getCountry(){
        return this.Country;
    }

    /**
     * sets country
     * @param v
     */
    public void setCountry(String v){
        this.Country = v;
    }

    /**
     * sends id to delete customer for db
     * @return
     */
    public boolean delete() {
        if (CustomerDAO.delete(this.Customer_ID)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * sends data to DAO to update customer in the db
     * @return
     */
    public boolean update() {
        if (CustomerDAO.update(this.Customer_ID,this.Customer_Name,this.Address,this.Postal_Code,this.Phone,this.Division,this.Country)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * sends information to DAO to insert new customer
     * @return
     */
    public boolean insert() {
        if (CustomerDAO.insert(this.Customer_Name,this.Address,this.Postal_Code,this.Phone,this.Division,this.Country)){
            return true;
        } else {
            return false;
        }
    }
}
