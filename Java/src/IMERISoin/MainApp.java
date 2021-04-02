package IMERISoin;

import IMERISoin.Controller.MainController;
import IMERISoin.Model.Drug;
import IMERISoin.Model.Patient;
import IMERISoin.Model.Room;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MainApp extends Application {

    private Stage primaryStage;
    private Parent rootLayout;

//    private final ObservableList<Patient> patientsData = FXCollections.observableArrayList();
//    private final ObservableList<Drug> drugData = FXCollections.observableArrayList();
//    private final ObservableList<Room> roomsData = FXCollections.observableArrayList();

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
        } catch (IOException e) {
            e.printStackTrace();
        }


//        FXMLLoader loader = new FXMLLoader();
//        Parent root = loader.load(getClass().getResource("View/main.fxml"));
//
//        MainController controller = loader.getController();
//        System.out.println("controller is : " + controller);
//        controller.setMain(this);


//        primaryStage.setScene(new Scene(root, 1200, 900));
//        primaryStage.show();
    }

    @Override
    public String toString() {
        return "MainApp{" +
                "primaryStage=" + primaryStage +
                ", rootLayout=" + rootLayout +
                ", patientsData=" + patientsData +
                '}';
    }

    public void refreshAll() {
        System.out.println("refresh all");
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
