package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.Main;
import model.User;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * MainController handles the login page
 */
public class MainController implements Initializable {
    @FXML
    private Label status, location, language, title, statusLabel;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;

    ResourceBundle rb = ResourceBundle.getBundle("translation/Login");
    /**
     * @param actionEvent Takes username and password, checks if null, then create user and check against db and provides error if not correct (A1a)
     * */
    public void login(ActionEvent actionEvent) {
        if (username.getText().isEmpty()){
            status.setText(rb.getString("Username") + " " + rb.getString("blank"));
            return;
        } else if (password.getText().isEmpty()) {
            status.setText(rb.getString("Password") + " " + rb.getString("blank"));
            return;
        }
        try {
            User user = new User(username.getText(),password.getText());
            recordLogin(username.getText(), "successful log-in");
            Main.setUser(user);
            Main.goTo(actionEvent, "Appointment");

        } catch (Exception e) {
            status.setText(rb.getString("Login") + " " + rb.getString("failed"));
            recordLogin(username.getText(), "invalid log-in");
        }

    }

    /**
     * Records logins and attempted logins to login_activity.txt
     * @param username
     * @param msg
     */
    private void recordLogin(String username, String msg) {
//        try {
//            File log = new File("src/login_activity.txt");
//            if (log.createNewFile()) {
//                System.out.println("File created: " + log.getName());
//            } else {
//                System.out.println("File already exists.");
//            }
//        } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
        try {
            FileWriter writeLog = new FileWriter("src/login_activity.txt",true);
            writeLog.write("\nUser " + username + " " + msg + " at " + Instant.now().toString()  + " UTC");
            writeLog.close();
        } catch (IOException e) {
            System.out.println("Error when recording login log");
            e.printStackTrace();
        }
    }
    /**
     * Initializes the ZoneID and sets the location, setups the language and displays language (A1b, A1c, A1d)
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId zone = ZoneId.systemDefault();
        location.setText(rb.getString("Location") + " : " + zone);
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().equals("en")) {
            language.setText("Language: English");
        } else if (locale.getLanguage().equals("fr")) {
            language.setText("Langue : Français");
            status.setText(rb.getString("Ready"));
            statusLabel.setText(rb.getString("Status"));
            title.setText(rb.getString("Company") + " " + rb.getString("Login"));
            password.setPromptText(rb.getString("Password"));
            username.setPromptText(rb.getString("Username"));
            loginButton.setText(rb.getString("Login"));
        }
    }
}
