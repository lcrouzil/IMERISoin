package IMERISoin.Controller;

import IMERISoin.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Tab clientTab;

    @FXML
    private Tab drugTab;

    @FXML
    private Tab roomTab;

    private MainApp mainApp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        System.out.println(clientTab.getText());
//        FXMLLoader loader = new FXMLLoader();


    }

    public void setMain(MainApp mainApp) {
        this.mainApp = mainApp;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("../View/clientTab.fxml"));

            AnchorPane anchorClient = loader.load();
            clientTab.setContent(anchorClient);

            ClientController controller = loader.getController();
            controller.setMain(mainApp);

        } catch (IOException iex) {
            iex.printStackTrace();
        }

        try {
//            AnchorPane anchorDrug = FXMLLoader.load(getClass().getResource("../View/drugTab.fxml"));
//            drugTab.setContent(anchorDrug);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("../View/drugTab.fxml"));

            AnchorPane anchorDrug = loader.load();
            drugTab.setContent(anchorDrug);

//            System.out.println(loader.getController().toString());
            DrugController controller = loader.getController();
            System.out.println(controller);
            controller.setMain(mainApp);

        } catch (IOException iex) {
            iex.printStackTrace();
        }

        try {
//            AnchorPane anchorTab = FXMLLoader.load(getClass().getResource("../View/roomTab.fxml"));
//            roomTab.setContent(anchorTab);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("../View/roomTab.fxml"));

            AnchorPane anchorTab = loader.load();
            roomTab.setContent(anchorTab);

            RoomController controller = loader.getController();
            controller.setMain(mainApp);


        } catch (IOException iex) {
            iex.printStackTrace();
        }

    }
}
