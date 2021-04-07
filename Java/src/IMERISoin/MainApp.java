package IMERISoin;

import IMERISoin.Controller.MainController;
import IMERISoin.Model.Drug;
import IMERISoin.Model.Patient;
import IMERISoin.Model.Room;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

public class MainApp extends Application {

    private Stage primaryStage;
    private Parent rootLayout;

    public boolean clockEnd = true;
    public static Timer pullDataTimer = null;

    private ArrayList<Drug> drugsData = new ArrayList<>();
    private ArrayList<Room> roomsData = new ArrayList<>();
    private ArrayList<Patient> patientsData = new ArrayList<>();

    /**
     *
     */
    public MainApp() {
        System.out.println("\n\nMain\n\n");
        JsonElement jsonElement = new JsonParser().parse("{\"id\":1,\"name\":\"medicament 3\"}");

        System.out.println(jsonElement.toString());

        Drug drug = new Gson().fromJson(jsonElement, Drug.class);

        System.out.println(drug);

    }


    /**
     * @param primaryStage Stage
     * @throws Exception an Exception
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("IMERISoin");

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("View/main.fxml"));


            rootLayout = (Parent) loader.load();
            MainController controller = loader.getController();
            controller.setMain(this);


            primaryStage.setScene(new Scene(rootLayout, 1400, 900));
            primaryStage.show();
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {

                    if(pullDataTimer != null) {
                        pullDataTimer.cancel();
                        System.out.println("Timer cancel");
                    }
                }

            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "MainApp{" +
                "primaryStage=" + primaryStage +
                ", rootLayout=" + rootLayout +
                ", patientsData=" + patientsData +
                '}';
    }

    public static void main(String[] args) {
        launch(args);
    }


    public ArrayList<Drug> getDrugsData() {
        return drugsData;
    }

    public void setDrugsData(ArrayList<Drug> drugsData) {
        this.drugsData = drugsData;
    }

    public ArrayList<Room> getRoomsData() {
        return roomsData;
    }

    public void setRoomsData(ArrayList<Room> roomsData) {
        this.roomsData = roomsData;
    }

    public ArrayList<Patient> getPatientsData() {
        return patientsData;
    }

    public void setPatientsData(ArrayList<Patient> drugsData) {
        this.patientsData = drugsData;
    }
}
