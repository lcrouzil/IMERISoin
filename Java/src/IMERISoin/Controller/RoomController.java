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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class RoomController implements Initializable, Refresh {

    @FXML
    private Button buttonRoom1;

    @FXML
    private Button buttonRoom2;

    @FXML
    private Button buttonRoom3;

    @FXML
    private Button buttonRoom4;

    @FXML
    private ChoiceBox<Room> roomChoice;

    @FXML
    private ChoiceBox<Patient> patientChoice;

    @FXML
    private ChoiceBox<Drug> drugChoice;

    @FXML
    private TableView<Room> roomTableView;

    @FXML
    private TableColumn<Room, String> idTableColumn;

    @FXML
    private TableColumn<Room, String> patientTableColumn;

    @FXML
    private TableColumn<Room, String> pathTableColumn;

    @FXML
    private TableColumn<Room, String> drugTableColumn;

    @FXML
    private TableColumn<Room, String> nameTableColumn;

    private MainApp mainApp;

    @FXML
    private void pushRoom1(ActionEvent event) {
        event.consume();
        HttpServices.addOrder(1);
        System.out.println("send to room 1");
    }

    @FXML
    private void pushRoom2(ActionEvent event) {
        event.consume();
        HttpServices.addOrder(2);

        System.out.println("send to room 2");
    }

    @FXML
    private void pushRoom3(ActionEvent event) {
        event.consume();
//        sendRoom(3);
        HttpServices.addOrder(3);
        System.out.println("send to room 3");
    }

    @FXML
    private void pushRoom4(ActionEvent event) {
        event.consume();
//        sendRoom(4);
        HttpServices.addOrder(4);
        System.out.println("send to room 4");
    }

    @FXML
    private void setPatientAction(ActionEvent event) {
        event.consume();

        System.out.println(roomChoice.getValue().getId());
        System.out.println(patientChoice.getValue().getId());
    }

    @FXML
    private void setMedicineAction(ActionEvent event) {
        event.consume();

        System.out.println(roomChoice.getValue().getId());
        System.out.println(drugChoice.getValue().getId());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println("Room controller init!");
    }

    @Override
    public void refreshData() {
        mainApp.setRoomsData(new ArrayList<>());
        HttpServices.getObjectRoom(mainApp.getRoomsData());

    }

    @Override
    public void refreshView() {

        ObservableList<Room> roomData = FXCollections.observableArrayList();
        roomData.addAll(mainApp.getRoomsData());

        roomChoice.setItems(roomData);
        roomTableView.setItems(roomData);

        idTableColumn.setCellValueFactory(cellData -> cellData.getValue().getIdFx());
        patientTableColumn.setCellValueFactory(cellData -> cellData.getValue().getPatient().getIdFx());
        drugTableColumn.setCellValueFactory(cellData -> cellData.getValue().getDrug().getNameFx());
        pathTableColumn.setCellValueFactory(cellData -> cellData.getValue().getPathFx());
        nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameFx());

        drugChoice.setItems(FXCollections.observableArrayList(mainApp.getDrugsData()));
        patientChoice.setItems(FXCollections.observableArrayList(mainApp.getPatientsData()));
    }

    public void setMain(MainApp mainApp) {
        this.mainApp = mainApp;

        refreshData();
        refreshView();
    }
}
