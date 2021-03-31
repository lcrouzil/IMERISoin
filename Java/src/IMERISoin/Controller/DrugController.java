package IMERISoin.Controller;

import IMERISoin.MainApp;
import IMERISoin.Model.Drug;
import IMERISoin.Model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

public class DrugController implements Initializable {

    @FXML
    private TextField drugNameField;

    @FXML
    private Button drugNewButton;

    @FXML
    private TableView<Drug> drugTableView;

    @FXML
    private TableColumn<Drug, String> nameTableColumn;

    private MainApp mainApp;

    @FXML
    private void pushButtonNewDrug(ActionEvent event) {
        event.consume();
        String drugName = drugNameField.getText();

        apiPush("addMedicine/", drugName);

        if (!drugName.equals("")) {
            System.out.println(drugName);
        }
    }

    private void apiPush(String uri, String data) {
        String url = "http://10.3.6.197:8000/" + uri + data;
        System.out.println(url);
        URL aurl = null;
        String codeHTML = "";

        try {
            aurl = new URL(url);
            URLConnection con = aurl.openConnection();
            con.setConnectTimeout(60000);

            System.out.println(con.getContentType());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void apiGetDrug() {
        String url = "http://10.3.6.197:8000/"
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
//        nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameFx());

        System.out.println("Drug controller init!");

    }

    public void refreshData() {


        drugTableView.setItems(mainApp.getDrugData());
        nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameFx());
    }

    public void setMain(MainApp mainApp) {
        this.mainApp = mainApp;

        refreshData();
    }
}
