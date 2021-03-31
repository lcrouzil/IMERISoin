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

    /**
     * @param event event
     */
    private void printHelloWorld(ActionEvent event) {
        event.consume();
        String drugName = drugNameField.getText();


        if (!drugName.equals("")) {
            System.out.println(drugName);
        }


    }

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
        nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameFx());

        System.out.println("Drug controller init!");

    }

    public void setMain(MainApp mainApp) {
        this.mainApp = mainApp;

        drugTableView.setItems(mainApp.getDrugData());
    }
}
