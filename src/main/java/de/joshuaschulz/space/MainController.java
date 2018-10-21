package de.joshuaschulz.space;

import com.jfoenix.controls.JFXTreeTableView;
import de.joshuaschulz.connection.APIRequestHandler;
import de.joshuaschulz.connection.AsyncAPICall;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainController implements Initializable {
    public JFXTreeTableView nearestObjects;

    private ExecutorService executor = Executors.newCachedThreadPool();
    private HashMap<String,String> apiResults;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        apiResults = new HashMap<>();
    }
    private void fillNearestObjects(){

    }
    private boolean fetchAPI(String baseURL, Map<String,String> params){
        //TODO: complete after connectionUtils are fixed
        try {
            executor.execute(new APIRequestHandler(new AsyncAPICall() {
                @Override
                public void onSuccess(String result) {

                }

                @Override
                public void onFailure(Exception exception) {

                }
            }));
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
