package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DrugController implements Initializable {

    @FXML
    private TextField drugNameField;

    @FXML
    private Button drugNewButton;

    @FXML
    private void printHelloWorld(ActionEvent event) {
        event.consume();
        String drugName = drugNameField.getText();


        if (!drugName.equals("")) {
            System.out.println(drugName);
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Drug controller init!");

    }
}
