package de.joshuaschulz.space;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.joshuaschulz.connection.APIRequestHandler;
import de.joshuaschulz.connection.AsyncAPICall;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppController implements Initializable {
    private ExecutorService executor = Executors.newCachedThreadPool();

    private JsonObject resultBackground;
    private Gson gson = new Gson();

    public Label startUpInfo;
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        startUpInfo.setVisible(false);
        try {
            executor.execute(new APIRequestHandler(new AsyncAPICall() {
                @Override
                public void onSuccess(String result) {
                    resultBackground = gson.fromJson(result,JsonObject.class);
                }

                @Override
                public void onFailure(Exception exception) {
                    exception.printStackTrace();
                }
            }));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleInfoButton(ActionEvent event) {
        startUpInfo.setText(resultBackground.get("title").toString());
        startUpInfo.setVisible(true);
    }
}
