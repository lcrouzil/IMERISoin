package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML
    private Button myButton;

    @FXML
    private TextField myTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myTextField.setText("test");

    }
}
