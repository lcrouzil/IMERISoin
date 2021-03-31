package IMERISoin;

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

import java.util.ArrayList;

public class Main extends Application {

    private ObservableList<Patient> patientsData = FXCollections.observableArrayList();

    /**
     *
     */
    public Main() {
        ArrayList<Room> rooms = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            rooms.add(new Room(i + 1));
        }

        ArrayList<Drug> drugs = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            String name = "Drug n" + i;
            drugs.add(new Drug(i + 1, name));
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


    /**
     * @param primaryStage Stage
     * @throws Exception an Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("View/main.fxml"));
        primaryStage.setTitle("IMERISoin");
        primaryStage.setScene(new Scene(root, 1200, 900));
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
