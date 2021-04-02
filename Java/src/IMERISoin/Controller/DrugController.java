package IMERISoin.Controller;

import IMERISoin.MainApp;
import IMERISoin.Model.Drug;
import IMERISoin.Model.Patient;
import IMERISoin.Model.Room;
import IMERISoin.services.HttpServices;
import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DrugController extends MainController implements Initializable, Refresh{

    @FXML
    private TextField drugNameField;

    @FXML
    private Button drugNewButton;

    @FXML
    private TableView<Drug> drugTableView;

    @FXML
    private TableColumn<Drug, String> idTableColumn;

    @FXML
    private TableColumn<Drug, String> nameTableColumn;


    private MainApp mainApp;

    @FXML
    private void pushButtonNewDrug(ActionEvent event) {
        event.consume();
        String drugName = drugNameField.getText();

//        apiPush("addMedicine/", drugName);

        if (!drugName.equals("")) {
            System.out.println(drugName);
        }
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

        System.out.println("Drug controller init!");
    }

    public void refreshData() {
        mainApp.setDrugsData(new ArrayList<>());
        HttpServices.getDrugList(mainApp.getDrugsData());

        System.out.println(mainApp.getDrugsData());
    }

    public void refreshView() {
//        System.out.println(mainApp.getDrugsData());

        ObservableList<Drug> drugData = FXCollections.observableArrayList();
        drugData.addAll(mainApp.getDrugsData());

        drugTableView.setItems(drugData);

        idTableColumn.setCellValueFactory(cellData -> cellData.getValue().getIdFx());
        nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameFx());
    }

    public void setMain(MainApp mainApp) {
        this.mainApp = mainApp;

        refreshData();
        refreshView();
    }
}
