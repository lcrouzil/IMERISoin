package IMERISoin.Controller;

import IMERISoin.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * FXML MainController Class
 *
 * @author Alexis DEVLEESCHAUWER
 */
public class MainController implements Refresh {

    @FXML
    private Tab clientTab;

    @FXML
    private Tab drugTab;

    @FXML
    private Tab roomTab;

    @FXML
    private MenuItem refreshMenuItem;

    private MainApp mainApp;

    private final ArrayList<Refresh> controllerList = new ArrayList<>();

    /**
     * create Thread for refresh action
     */
    @FXML
    public void refreshAction() {
        new Thread(() -> {
            refreshData();
            refreshView();
        }).start();
    }

    /**
     * show popup settings
     * todo Finish is an good idea
     * @param event javafx event
     */
    @FXML
    public void settingsShowPopup(ActionEvent event) {
        event.consume();

        System.out.println("POPUP");
        Popup popup = new Popup();

    }


    /**
     * setter class main and init all other controller
     * @param mainApp main Instance
     */
    public void setMain(MainApp mainApp) {
        this.mainApp = mainApp;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("../View/clientTab.fxml"));

            AnchorPane anchorClient = loader.load();
            clientTab.setContent(anchorClient);

            PatientController controller = loader.getController();
            controllerList.add(controller);
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

            DrugController controller = loader.getController();
            controllerList.add(controller);
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
            controllerList.add(controller);
            controller.setMain(mainApp);


        } catch (IOException iex) {
            iex.printStackTrace();
        }

        refreshAction();
        startGettingData();
    }

    /**
     * launch timer call thread for get data and refresh
     */
    private void startGettingData() {

        MainApp.pullDataTimer = new Timer();
        MainApp.pullDataTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                new Thread(() -> {
                    refreshData();
                    refreshTable();
                }).start();

            }
        }, 500, 500);

    }

    /**
     * refresh Data in all controller
     */
    @Override
    public void refreshData() {

        System.out.println("Main : START Refresh Data");

        for (Refresh controller : controllerList) {
            controller.refreshData();
        }
    }

    /**
     * refresh Table View in all controller
     */
    @Override
    public void refreshTable() {
        System.out.println("Main : Refresh Table");

        for (Refresh controller : controllerList) {
            controller.refreshTable();
        }

    }

    /**
     * refresh all View in all controller
     */
    @Override
    public void refreshView() {
        System.out.println("Main : Refresh View");

        for (Refresh controller : controllerList) {
            controller.refreshView();
        }
    }

}
