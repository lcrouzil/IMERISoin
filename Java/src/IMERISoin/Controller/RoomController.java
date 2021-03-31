package IMERISoin.Controller;

import IMERISoin.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;


public class RoomController implements Initializable {

    @FXML
    private Button buttonRoom1;

    @FXML
    private Button buttonRoom2;

    @FXML
    private Button buttonRoom3;

    @FXML
    private Button buttonRoom4;

    @FXML
    private void pushRoom1(ActionEvent event) {
        event.consume();
        sendRoom(1);

        System.out.println("send to room 1");
    }

    @FXML
    private void pushRoom2(ActionEvent event) {
        event.consume();
        sendRoom(2);

        System.out.println("send to room 2");
    }

    @FXML
    private void pushRoom3(ActionEvent event) {
        event.consume();
        sendRoom(3);

        System.out.println("send to room 3");
    }

    @FXML
    private void pushRoom4(ActionEvent event) {
        event.consume();
        sendRoom(4);

        System.out.println("send to room 4");
    }

    private MainApp mainApp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(buttonRoom1.getId());
        System.out.println(buttonRoom2.getId());
        System.out.println(buttonRoom3.getId());
        System.out.println(buttonRoom4.getId());


    }

    /**
     * @param room_id id of room
     */
    private void sendRoom(int room_id) {

    }

    public void setMain(MainApp mainApp) {
        this.mainApp = mainApp;

//        patientTable.setItems(mainApp.getPatientsData());
    }
}
