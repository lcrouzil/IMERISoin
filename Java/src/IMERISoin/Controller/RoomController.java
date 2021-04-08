package IMERISoin.Controller;

import IMERISoin.MainApp;
import IMERISoin.Model.Drug;
import IMERISoin.Model.Order;
import IMERISoin.Model.Patient;
import IMERISoin.Model.Room;
import IMERISoin.services.HttpServices;
import com.sun.org.apache.xpath.internal.operations.Or;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML RoomController Class
 *
 * @author Alexis DEVLEESCHAUWER
 */
public class RoomController extends MainController implements Refresh {

    @FXML
    private Button buttonRoom1;

    @FXML
    private Button buttonRoom2;

    @FXML
    private Button buttonRoom3;

    @FXML
    private Button buttonRoom4;

    @FXML
    private TextField weekField;

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

    @FXML
    private TableView<Order> orderTableView;

    @FXML
    private TableColumn<Order, String> idOrderTableColumn;

    @FXML
    private TableColumn<Order, String> dateOrderTableColumn;

    @FXML
    private TableColumn<Order, String> roomOrderTableColumn;

    @FXML
    private TableColumn<Order, String> statusOrderTableColumn;

    @FXML
    private TableColumn<Order, String> drugOrderTableColumn;


    private ArrayList<Order> orderList = new ArrayList<>();

    private MainApp mainApp;

    /**
     * add order for room 1
     * @param event javafx event
     */
    @FXML
    private void pushRoom1(ActionEvent event) {
        event.consume();
        HttpServices.addOrder(1);
        System.out.println("send to room 1");
        
        refreshData();
        refreshView();
    }

    /**
     * add order for room 2
     * @param event javafx event
     */
    @FXML
    private void pushRoom2(ActionEvent event) {
        event.consume();
        HttpServices.addOrder(2);

        System.out.println("send to room 2");
        refreshData();
        refreshView();
    }

    /**
     * add order for room 3
     * @param event javafx event
     */
    @FXML
    private void pushRoom3(ActionEvent event) {
        event.consume();
//        sendRoom(3);
        HttpServices.addOrder(3);
        System.out.println("send to room 3");
        refreshData();
        refreshView();
    }

    /**
     * add order for room 4
     * @param event javafx event
     */
    @FXML
    private void pushRoom4(ActionEvent event) {
        event.consume();
//        sendRoom(4);
        HttpServices.addOrder(4);
        System.out.println("send to room 4");
        refreshData();
        refreshView();
    }

    /**
     * refresh data in Controller
     */
    @Override
    public void refreshData() {
        mainApp.setRoomsData(new ArrayList<>());
        HttpServices.getRoomList(mainApp.getRoomsData());

        orderList = new ArrayList<>();
        HttpServices.getOrderList(orderList);
    }

    /**
     * refresh Table in Controller
     */
    @Override
    public void refreshTable() {
        ObservableList<Room> roomData = FXCollections.observableArrayList();
        roomData.addAll(mainApp.getRoomsData());
        roomTableView.setItems(roomData);

        idTableColumn.setCellValueFactory(cellData -> cellData.getValue().getIdFx());
        pathTableColumn.setCellValueFactory(cellData -> cellData.getValue().getPathFx());
        nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameFx());

        ObservableList<Order> ordersData = FXCollections.observableArrayList();
        ordersData.addAll(orderList);
        orderTableView.setItems(ordersData);

        idOrderTableColumn.setCellValueFactory(cellData -> cellData.getValue().getIdFx());
        roomOrderTableColumn.setCellValueFactory(cellData -> cellData.getValue().getRoomFx());
        drugOrderTableColumn.setCellValueFactory(cellData -> cellData.getValue().getDrugFx());
        statusOrderTableColumn.setCellValueFactory(cellData -> cellData.getValue().getStatusFx());
        dateOrderTableColumn.setCellValueFactory(cellData -> cellData.getValue().getDateFx());
    }

    /**
     * refresh all View in Controller
     */
    @Override
    public void refreshView() {
        refreshTable();

    }

    /**
     * set main
     * @param mainApp main Instance
     */
    public void setMain(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
