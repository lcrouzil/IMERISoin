package IMERISoin.Controller;

import IMERISoin.MainApp;
import IMERISoin.Model.Drug;
import IMERISoin.Model.Room;
import IMERISoin.services.HttpServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

    @FXML
    private void pushRoom1(ActionEvent event) {
        event.consume();
        HttpServices.addOrder(1, 1);
        System.out.println("send to room 1");
    }

    @FXML
    private void pushRoom2(ActionEvent event) {
        event.consume();
        HttpServices.addOrder(2, 1);

        System.out.println("send to room 2");
    }

    @FXML
    private void pushRoom3(ActionEvent event) {
        event.consume();
//        sendRoom(3);
        HttpServices.addOrder(3, 1);
        System.out.println("send to room 3");
    }

    @FXML
    private void pushRoom4(ActionEvent event) {
        event.consume();
//        sendRoom(4);
        HttpServices.addOrder(4, 1);
        System.out.println("send to room 4");
    }

    private MainApp mainApp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Room controller init!");
    }

    /**
     * @param room_id id of room
     */
    private void sendRoom(int room_id) {

    }

    @Override
    public void refreshData() {
        mainApp.setRoomsData(new ArrayList<>());
        ArrayList<Room> rooms = mainApp.getRoomsData();
        HttpServices.getRoomList(rooms);


        ObservableList<Room> roomData = FXCollections.observableArrayList();
        roomData.addAll(rooms);

        roomTableView.setItems(roomData);

        idTableColumn.setCellValueFactory(cellData -> cellData.getValue().getIdFx());
//        patientTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameFx());
        pathTableColumn.setCellValueFactory(cellData -> cellData.getValue().getPathFx());
//        drugTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameFx());
        nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameFx());
    }

    public void setMain(MainApp mainApp) {
        this.mainApp = mainApp;

        refreshData();
    }
}
