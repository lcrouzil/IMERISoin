package IMERISoin.Controller;

import IMERISoin.MainApp;
import IMERISoin.Model.Drug;
import IMERISoin.Model.Patient;
import IMERISoin.Model.Room;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable, Refresh {

    @FXML
    private Tab clientTab;

    @FXML
    private Tab drugTab;

    @FXML
    private Tab roomTab;

    @FXML
    private MenuItem refreshMenuItem;

    private MainApp mainApp;

    @FXML
    private void refreshAction() {
        refreshData();
        refreshView();
    }

    private final ArrayList<Refresh> controllerList = new ArrayList<>();

    public void setMain(MainApp mainApp) {
        this.mainApp = mainApp;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("../View/clientTab.fxml"));

            AnchorPane anchorClient = loader.load();
            clientTab.setContent(anchorClient);

            PatientController controller = loader.getController();
            controllerList.add(controller);
            controller.setMain(mainApp);

        } catch (IOException iex) {
            iex.printStackTrace();
        }

        try {
//            AnchorPane anchorDrug = FXMLLoader.load(getClass().getResource("../View/drugTab.fxml"));
//            drugTab.setContent(anchorDrug);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("../View/drugTab.fxml"));

            AnchorPane anchorDrug = loader.load();
            drugTab.setContent(anchorDrug);

//            System.out.println(loader.getController().toString());
            DrugController controller = loader.getController();
            controllerList.add(controller);
            controller.setMain(mainApp);

        } catch (IOException iex) {
            iex.printStackTrace();
        }

        try {
//            AnchorPane anchorTab = FXMLLoader.load(getClass().getResource("../View/roomTab.fxml"));
//            roomTab.setContent(anchorTab);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("../View/roomTab.fxml"));

            AnchorPane anchorTab = loader.load();
            roomTab.setContent(anchorTab);

            RoomController controller = loader.getController();
            controllerList.add(controller);
            controller.setMain(mainApp);


        } catch (IOException iex) {
            iex.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        refreshData();
//        refreshView();

        System.out.println("Main controller init!");
    }

    @Override
    public void refreshData() {
        System.out.println("\nMain : START Refresh Data\n");
        for (Refresh controller : controllerList) {
            controller.refreshData();
        }

        System.out.println("\n\n");

        ArrayList<Room> rooms = mainApp.getRoomsData();
        ArrayList<Drug> drugs = mainApp.getDrugsData();
        ArrayList<Patient> patients = mainApp.getPatientsData();

        for (Room room : rooms) {
//            for (Drug drug : drugs) {
//                if (room.getDrug_id() == drug.getId()) {
//                    room.setDrug(drug);
//                }
//            }

//            for (Patient patient : patients) {
//                System.out.println("id : " + room.getPatient_id());
//                Integer patient_id = room.getPatient_id();
//                if (patient_id != null && patient_id == patient.getId()) {
//                    System.out.println(room.getPatient_id() + " == " + patient.getId());
//                }
//            }

        }

        for (Room room : mainApp.getRoomsData()) {
            System.out.println("room drugName : " + room.getDrug().getName());
        }



        System.out.println("\nMain : END Refresh Data\n");


    }

    @Override
    public void refreshView() {
        System.out.println("Main : Refresh View");
        for(Refresh controller : controllerList) {
            System.out.println(controller.getClass());
            controller.refreshView();
        }
    }
}
