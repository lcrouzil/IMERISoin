package IMERISoin.Controller;

import IMERISoin.MainApp;
import IMERISoin.MainApp;
import IMERISoin.Model.Drug;
import IMERISoin.Model.Patient;
import IMERISoin.Model.Room;
import com.sun.security.ntlm.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML
    private Button clientNewButton;

    @FXML
    private TextField clientNameField;

    @FXML
    private ChoiceBox<String> clientStatusBox;

    @FXML
    private ChoiceBox<Drug> clientDrugBox;

    @FXML
    private ChoiceBox<Room> clientRoomBox;

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient, String> nameColumn;

    @FXML
    private TableColumn<Patient, String> statusColumn;

    @FXML
    private TableColumn<Patient, String> drugColumn;

    @FXML
    private TableColumn<Patient, String> roomColumn;

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

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().getStatus());
        drugColumn.setCellValueFactory(cellData -> cellData.getValue().getDrug().getNameFx());
//        roomColumn.setCellValueFactory(cellData -> cellData.getValue().getRoom().getName());

        clientStatusBox.setItems(FXCollections.observableArrayList("healed", "no effect", "dead", "sick"));
        clientDrugBox.setItems(FXCollections.observableArrayList());
    }

    public void setMain(MainApp mainApp) {
        this.mainApp = mainApp;

        patientTable.setItems(mainApp.getPatientsData());
        clientDrugBox.setItems(mainApp.getDrugData());
        clientRoomBox.setItems(mainApp.getRoomsData());

    }
}
