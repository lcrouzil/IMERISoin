package IMERISoin.Controller;

import IMERISoin.MainApp;
import IMERISoin.Model.Patient;
import IMERISoin.Model.Room;
import IMERISoin.services.HttpServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PatientController implements Initializable, Refresh {

    @FXML
    private Button clientNewButton;

    @FXML
    private TextField clientNameField;

    @FXML
    private ChoiceBox<String> clientStatusBox;

    @FXML
    private ChoiceBox<Room> roomStatusBox;

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient, String> nSSColumn;

    @FXML
    private TableColumn<Patient, String> nameColumn;

    @FXML
    private TableColumn<Patient, String> statusColumn;

    private MainApp mainApp;

    @FXML
    private void newPatientAction(ActionEvent event) throws NumberFormatException {
        event.consume();

        try {
            Integer patientID = Integer.parseInt(clientNameField.getText());
            Integer room = roomStatusBox.getValue().getId();

            System.out.println("PatientID : " + patientID + " room : " + room);

            HttpServices.addPatient(patientID, room);

            refreshData();
            refreshView();
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage() + " not number");
        }

        System.out.println();

    }

    @FXML
    private void setPatientStatusAction(ActionEvent event) throws NumberFormatException {
        event.consume();

        try {
            int patientID = Integer.parseInt(clientNameField.getText());
            String status = clientStatusBox.getValue();

            if (status.equals("Not Specified")) {
                status = null;
            }

            System.out.println(patientID + " " + status);

            HttpServices.setPatientCondition(patientID, status);

            refreshData();
            refreshView();
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage() + " not number");

        }
    }

    @Override
    public void refreshData() {
        mainApp.setPatientsData(new ArrayList<>());
        HttpServices.getPatientList(mainApp.getPatientsData());
    }

    @Override
    public void refreshView() {
        ObservableList<Room> roomData = FXCollections.observableArrayList();
        roomData.addAll(mainApp.getRoomsData());
        roomStatusBox.setItems(roomData);

        ObservableList<Patient> patientData = FXCollections.observableArrayList();
        patientData.addAll(mainApp.getPatientsData());
        patientTable.setItems(patientData);

        nSSColumn.setCellValueFactory(cellData -> cellData.getValue().getIdFx());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().getStatusFx());
    }

    public void setMain(MainApp mainApp) {
        this.mainApp = mainApp;

//        patientTable.setItems(mainApp.getPatientsData());
//        clientDrugBox.setItems(mainApp.getDrugData());
//        clientRoomBox.setItems(mainApp.getRoomsData());

        refreshData();
        refreshView();
    }

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
}
