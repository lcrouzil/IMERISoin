package IMERISoin.Controller;

import IMERISoin.MainApp;
import IMERISoin.MainApp;
import IMERISoin.Model.Drug;
import IMERISoin.Model.Patient;
import IMERISoin.Model.Room;
import IMERISoin.services.HttpServices;
import com.sun.security.ntlm.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;

public class PatientController implements Initializable, Refresh {

    @FXML
    private Button clientNewButton;

    @FXML
    private TextField clientNameField;

    @FXML
    private ChoiceBox<String> clientStatusBox;

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient, String> nameColumn;

    @FXML
    private TableColumn<Patient, String> statusColumn;


    private MainApp mainApp;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientStatusBox.setItems(FXCollections.observableArrayList("Not Specified", "Cured", "Dead", "Sick"));
        clientStatusBox.setValue("Not Specified");

        System.out.println("Patient controller init!");
    }

    @Override
    public void refreshData() {
        mainApp.setPatientsData(new ArrayList<>());
        HttpServices.getPatientList(mainApp.getPatientsData());



    }

    @Override
    public void refreshView() {
        ObservableList<Patient> roomData = FXCollections.observableArrayList();
        roomData.addAll(mainApp.getPatientsData());

        patientTable.setItems(roomData);

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameFx());
//        patientTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameFx());
//        pathTableColumn.setCellValueFactory(cellData -> cellData.getValue().getPathFx());
//        drugTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameFx());
//        nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameFx());
    }

    public void setMain(MainApp mainApp) {
        this.mainApp = mainApp;

//        patientTable.setItems(mainApp.getPatientsData());
//        clientDrugBox.setItems(mainApp.getDrugData());
//        clientRoomBox.setItems(mainApp.getRoomsData());

        refreshData();
        refreshView();

    }
}
