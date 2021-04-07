package IMERISoin.Controller;

import IMERISoin.MainApp;
import IMERISoin.Model.Drug;
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
    private TextField clientNameField;

    @FXML
    private TextField weekField;

    @FXML
    private ChoiceBox<String> clientStatusBox;

    @FXML
    private ChoiceBox<Room> roomStatusBox;

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient, String> nSSColumn;

    @FXML
    private TableColumn<Patient, String> statusColumn;

    @FXML
    private TableColumn<Patient, String> weekColumn;

    @FXML
    private TableColumn<Patient, String> roomColumn;

    @FXML
    private TableColumn<Patient, String> drugColumn;

    @FXML
    private TextArea consoleUser;

    @FXML
    private ChoiceBox<Drug> drugChoice;

    private MainApp mainApp;

    @FXML
    private void setMedicineAction(ActionEvent event) {
        event.consume();

        System.out.println(roomStatusBox.getValue().getId());
        System.out.println(drugChoice.getValue().getId());

        try {
            if (roomStatusBox.getValue() == null) {
                System.out.println("room not selected, if you dont have room add in");
                consoleUser.setText("room not selected, if you dont have room add in");
            }

            Integer room = roomStatusBox.getValue().getId();
            Integer week = weekField.getText().equals("") ? null : Integer.parseInt(weekField.getText());
            Integer drug = drugChoice.getValue().getId();

            System.out.println("PatientID : " + drug + " room : " + room + " week : " + week);

            if (week != null) {
                System.out.println("week is not null");
                HttpServices.setRoomMedicine(room, drug, week);
            } else {
                HttpServices.setRoomMedicine(room, drug);
            }

            refreshData();
            refreshView();


        } catch (NumberFormatException e) {
            System.out.println(e.getMessage() + " not valid number");
            consoleUser.setText(e.getMessage() + " not valid number");
        }

        System.out.println();

    }

    @FXML
    private void newPatientAction(ActionEvent event) throws NumberFormatException {
        event.consume();

        try {

            if (roomStatusBox.getValue() == null) {
                System.out.println("room not selected, if you dont have room add in");
                consoleUser.setText("room not selected, if you dont have room add in");
            }

            Integer patientID = Integer.parseInt(clientNameField.getText());
            Integer room = roomStatusBox.getValue().getId();
            Integer week = weekField.getText().equals("") ? null : Integer.parseInt(weekField.getText());

            System.out.println("PatientID : " + patientID + " room : " + room + " week : " + week);

            String returnStatus = null;
            if (week != null) {
                System.out.println("week is not null");
                returnStatus = HttpServices.addPatient(patientID, room, week);
            } else {
                returnStatus = HttpServices.addPatient(patientID, room);
            }

            refreshData();
            refreshView();

            if (returnStatus != null){
                consoleUser.setText(returnStatus);
            }

        } catch (NumberFormatException e) {
            System.out.println(e.getMessage() + " not valid number");
            consoleUser.setText(e.getMessage() + " not valid number");
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
            System.out.println(e.getMessage() + " not valid number");
            consoleUser.setText(e.getMessage() + " not valid number");
        }
    }

    @Override
    public void refreshData() {
        mainApp.setPatientsData(new ArrayList<>());
        HttpServices.getPatientList(mainApp.getPatientsData());
    }

    @Override
    public void refreshView() {
        consoleUser.setText("");

        ObservableList<Room> roomData = FXCollections.observableArrayList();
        roomData.addAll(mainApp.getRoomsData());
        roomStatusBox.setItems(roomData);

        ObservableList<Patient> patientData = FXCollections.observableArrayList();
        patientData.addAll(mainApp.getPatientsData());
        patientTable.setItems(patientData);

        ObservableList<Drug> drugData = FXCollections.observableArrayList();
        drugData.addAll(mainApp.getDrugsData());
        drugChoice.setItems(drugData);

        nSSColumn.setCellValueFactory(cellData -> cellData.getValue().getIdFx());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().getStatusFx());
        weekColumn.setCellValueFactory(cellData -> cellData.getValue().getWeekFx());
        roomColumn.setCellValueFactory(cellData -> cellData.getValue().getRoomFx());
        drugColumn.setCellValueFactory(cellData -> cellData.getValue().getDrugFx());
    }

    public void setMain(MainApp mainApp) {
        this.mainApp = mainApp;

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
