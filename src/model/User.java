package model;

import dao.UserDAO;

/**
 * User class handles the user object
 */
public class User {

    private String username;
    private String password;
    private int id = 0;

    /**
     * Constructor for making a user
     * @param username
     * @param password
     * @throws Exception
     */
    public User(String username, String password) throws Exception {
        this.username = username;
        this.password = password;
        int id = UserDAO.checkUser(username, password);
        if (id != 0){
            this.id = id;
        } else {
            this.username = null;
            this.password = null;
            throw new Exception("Username or Password Incorrect");
        }

    }

    /**
     * empty constructor
     */
    public User() {

    }

    /**
     * gets id
     * @return
     */
    public int getId() {
        return this.id;
    }

    /**
     * gets username
     * @return
     */
    public String getUsername() {
        return this.username;
    }
}
