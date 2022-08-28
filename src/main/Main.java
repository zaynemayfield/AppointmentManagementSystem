package main;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import dao.JDBC;
import model.Appointment;
import model.User;
import java.io.IOException;
import java.util.Locale;

/**
 * <pre>
 * Main class begins the app and loads Main.fxml and sets the title.
 *
 * javadocs are located in javadocs folder inside C195 Project folder
 *
 * </pre>
 * */

/**
 *
 * @author Zayne Mayfield
 */
public class Main extends Application {

    private static User user;
    private static Stage stage;
    private static Scene scene;
    private static Parent root;

    public static boolean needAlert = true;
    private static Appointment appointment;

    /**
     * This method allows all the different scenes to go to different scenes by calling this static method
     * @param actionEvent
     * @param location
     * @throws IOException
     */
    public static void goTo(ActionEvent actionEvent, String location) throws IOException {
        String view = "/view/" + location + ".fxml";
        root = FXMLLoader.load(Main.class.getResource(view));
        scene = new Scene(root);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets the user, so it can be access later until login when it is removed
     * @param user
     */
    public static void setUser(User user) {
        Main.user = user;
    }

    /**
     * Gets the user
     * @return
     */
    public static User getUser () {
        return Main.user;
    }

    /**
     * Sets the appointment so it can be access from anywhere
     * @param appointment
     */
    public static void setAppointment(Appointment appointment) { Main.appointment = appointment;}

    /**
     * Returns the appointment when needed
     * @return
     */
    public static Appointment getAppointment() {return Main.appointment;}


    /**
     *Starts the app and sets the stage, title, scene
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Appointment Management System");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Main start method, checks if language is other than en or fr and then defaults to en
     * @param args
     */
    public static void main(String[] args) {
        JDBC.makeConnection();

        //Locale.setDefault(new Locale("fr"));
        //Locale.setDefault(new Locale("de"));
        Locale locale = Locale.getDefault();

        if (locale.getLanguage().equals("en") || locale.getLanguage().equals("fr")){
            System.out.println(locale.getLanguage());
            System.out.println("It is French or English");
        } else {
            Locale.setDefault(new Locale("en"));
            System.out.println("It is NOT French or English");
        }

        launch();
    }
}
