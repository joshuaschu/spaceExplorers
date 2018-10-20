package de.joshuaschulz.space;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    protected void handleInfoButton(ActionEvent event) {
        System.out.println("Button pressed");
    }
}
