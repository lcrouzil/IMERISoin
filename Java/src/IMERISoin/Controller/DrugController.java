package IMERISoin.Controller;

import IMERISoin.MainApp;
import IMERISoin.Model.Drug;
import IMERISoin.services.HttpServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * FXML DrugController Class
 *
 * @author Alexis DEVLEESCHAUWER
 */
public class DrugController extends MainController implements Initializable, Refresh{

    @FXML
    private TextField drugNameField;

    @FXML
    private TextField idNameField;

    @FXML
    private Button drugNewButton;

    @FXML
    private TableView<Drug> drugTableView;

    @FXML
    private TableColumn<Drug, String> idTableColumn;

    @FXML
    private TableColumn<Drug, String> nameTableColumn;


    private MainApp mainApp;

    /**
     * button for add new Drug
     * @param event javafx event
     * @throws NumberFormatException throw if convert not int
     */
    @FXML
    private void pushButtonNewDrug(ActionEvent event) throws NumberFormatException {
        event.consume();

        try {
            int drugId = Integer.parseInt(idNameField.getText());
            String drugName = drugNameField.getText();

            System.out.println(drugId + " " + drugName);

            HttpServices.addDrug(drugId, drugName);

            refreshAction();
        } catch (NumberFormatException e) {
            e.printStackTrace();
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

    /**
     * refresh data Propriety of controller
     */
    public void refreshData() {
        mainApp.setDrugsData(new ArrayList<>());
        HttpServices.getDrugList(mainApp.getDrugsData());

    }

    /**
     * refresh Table View
     */
    @Override
    public void refreshTable() {
        ObservableList<Drug> drugData = FXCollections.observableArrayList();
        drugData.addAll(mainApp.getDrugsData());

        drugTableView.setItems(drugData);

        idTableColumn.setCellValueFactory(cellData -> cellData.getValue().getIdFx());
        nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameFx());
    }

    /**
     * refresh All View
     */
    public void refreshView() {
        refreshTable();
    }


    /**
     * set Main class
     * @param mainApp main Class
     */
    public void setMain(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
