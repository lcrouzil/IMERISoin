package IMERISoin.Controller;

import IMERISoin.MainApp;
import IMERISoin.Model.Drug;
import IMERISoin.Model.Patient;
import IMERISoin.Model.Room;
import IMERISoin.services.HttpServices;
import com.sun.jmx.snmp.tasks.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * FXML PatientController Class
 *
 * @author Alexis DEVLEESCHAUWER
 */
public class PatientController extends MainController implements Refresh {

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

    /**
     * set Drug for room with or without week
     * @param event javafx event
     */
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

    /**
     * @param event javafx event
     * @throws NumberFormatException throw if not int
     */
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

            refreshView();

            if (returnStatus != null) {
                consoleUser.setText(returnStatus);
            }

        } catch (
                NumberFormatException e) {
            System.out.println(e.getMessage() + " not valid number");
            consoleUser.setText(e.getMessage() + " not valid number");
        }

        System.out.println();

    }

    /**
     *
     * @param event javafx event
     * @throws NumberFormatException throw if not int
     */
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

    /**
     * refresh data in controller
     */
    @Override
    public void refreshData() {
        mainApp.setPatientsData(new ArrayList<>());
        HttpServices.getPatientList(mainApp.getPatientsData());
    }

    /**
     * refresh table View in controller
     */
    @Override
    public void refreshTable() {
        ObservableList<Patient> patientData = FXCollections.observableArrayList();
        patientData.addAll(mainApp.getPatientsData());
        patientTable.setItems(patientData);

        nSSColumn.setCellValueFactory(cellData -> cellData.getValue().getIdFx());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().getStatusFx());
        weekColumn.setCellValueFactory(cellData -> cellData.getValue().getWeekFx());
        roomColumn.setCellValueFactory(cellData -> cellData.getValue().getRoomFx());
        drugColumn.setCellValueFactory(cellData -> cellData.getValue().getDrugFx());
    }

    /**
     * refresh all Views in controller
     */
    @Override
    public void refreshView() {
        refreshTable();
        consoleUser.setText("");

        ObservableList<Room> roomData = FXCollections.observableArrayList();
        roomData.addAll(mainApp.getRoomsData());
        roomStatusBox.setItems(roomData);

        ObservableList<Drug> drugData = FXCollections.observableArrayList();
        drugData.addAll(mainApp.getDrugsData());
        drugChoice.setItems(drugData);

    }

    /**
     * @param mainApp main Instance
     */
    public void setMain(MainApp mainApp) {
        this.mainApp = mainApp;

        clientStatusBox.setItems(FXCollections.observableArrayList("Not Specified", "Cured", "Dead", "Sick"));
        clientStatusBox.setValue("Not Specified");

    }
}
