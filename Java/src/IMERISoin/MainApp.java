package IMERISoin;

import IMERISoin.Controller.MainController;
import IMERISoin.Model.Drug;
import IMERISoin.Model.Patient;
import IMERISoin.Model.Room;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainApp extends Application {

    private Stage primaryStage;
    private Parent rootLayout;

    private final ObservableList<Patient> patientsData = FXCollections.observableArrayList();
    private final ObservableList<Drug> drugData = FXCollections.observableArrayList();
    private final ObservableList<Room> roomsData = FXCollections.observableArrayList();

    /**
     *
     */
    public MainApp() {
        ArrayList<Room> rooms = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            rooms.add(new Room(i + 1));
        }

        roomsData.addAll(rooms);

        ArrayList<Drug> drugs = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            String name = "Drug n" + i;
            drugs.add(new Drug(i + 1, name));
        }

        for (Drug drug : drugs) {
            drugData.add(drug);
            System.out.println(drug.getName());
        }

        ArrayList<Patient> patients = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            String name = "Patient n" + (i + 1);
            patients.add(new Patient(i + 1, name, "sick", drugs.get(0), rooms.get(0)));
        }

        for (Patient patient : patients) {
            patientsData.add(patient);
            System.out.println(patient.getName());
        }
    }

    /**
     * Returns the data as an observable list of Patient.
     * @return ObservableList
     */
    public ObservableList<Patient> getPatientsData() {
        return patientsData;
    }

    public ObservableList<Drug> getDrugData() {
        return drugData;
    }

    public ObservableList<Room> getRoomsData() {
        return roomsData;
    }


    /**
     * @param primaryStage Stage
     * @throws Exception an Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        urlCo();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("IMERISoin");

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("View/main.fxml"));


            rootLayout = (Parent) loader.load();
            MainController controller = loader.getController();
            controller.setMain(this);


            primaryStage.setScene(new Scene(rootLayout, 1200, 900));
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


    public void urlCo() {
        String url = "http://10.3.7.21:8000";
        URL aurl = null;
        String codeHTML = "";

        try {
            aurl = new URL(url);
            URLConnection con = aurl.openConnection();
            con.setConnectTimeout(60000);

            System.out.println(con.getContentType());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        launch(args);
    }
}
