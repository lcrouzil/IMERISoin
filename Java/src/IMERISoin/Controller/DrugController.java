package IMERISoin.Controller;

import IMERISoin.MainApp;
import IMERISoin.Model.Drug;
import IMERISoin.Model.Patient;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
        String sURL = "http://10.3.6.197:8000/Patients/listMedicines";

//        URL aurl = null;
        String codeHTML = "";

        try {
            URL url = new URL(sURL);
//            URLConnection con = aurl.openConnection();
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            System.out.println(request.getResponseCode());
            if (request.getResponseCode() == 200) {
                JsonParser jp = new JsonParser(); //from gson
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
                JsonObject rootObj = root.getAsJsonObject(); //May be an array, may be an object.
                JsonObject medicines = rootObj.get("medicines").getAsJsonObject(); //just grab the zipcode




                System.out.println(medicines.toString());
            }

//            con.setConnectTimeout(60000);

//            System.out.println(con.getContent());

        } catch (Exception e) {
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
//        nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameFx());

        System.out.println("Drug controller init!");

    }

    public void refreshData() {
        drugTableView.setItems(mainApp.getDrugData());
        apiGetDrug();

        nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameFx());
    }

    public void setMain(MainApp mainApp) {
        this.mainApp = mainApp;

        refreshData();
    }
}
