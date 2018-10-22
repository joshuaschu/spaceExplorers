package de.joshuaschulz.space;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import de.joshuaschulz.connection.APIRequestHandler;
import de.joshuaschulz.connection.AsyncAPICall;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainController implements Initializable {
    public JFXTreeTableView<Asteroid> nearestObjects;
    public JFXTreeTableColumn<Asteroid,String> nameColumn;
    public JFXTreeTableColumn<Asteroid,String> closesApproach;
    public JFXTreeTableColumn<Asteroid,String> isHazardous;

    private ExecutorService executor = Executors.newCachedThreadPool();
    private Gson gson = new Gson();
    private HashMap<String,JsonObject> apiResults;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        apiResults = new HashMap<>();
        fillNearestObjects();
    }

    private void fillNearestObjects(){
        nameColumn.setCellValueFactory(param -> param.getValue().getValue().name);
        closesApproach.setCellValueFactory(param -> param.getValue().getValue().approach);
        isHazardous.setCellValueFactory(param -> param.getValue().getValue().hazardous);
        HashMap<String,String> params= new HashMap<>();
        params.put("datailed","true");
        ObservableList<Asteroid> spaceObjects = FXCollections.observableArrayList();
        try {
            executor.execute(new APIRequestHandler("https://api.nasa.gov/neo/rest/v1/feed/today", params, new AsyncAPICall() {
                @Override
                public void onSuccess(String result) {
                    JsonObject jsonObject = gson.fromJson(result,JsonObject.class);
                    apiResults.put("nearestObjects",jsonObject);
                    JsonElement values = jsonObject.get("near_earth_objects");
                    JsonObject objectJson = values.getAsJsonObject();
                    JsonArray array = objectJson.getAsJsonArray(getCurrentDate());
                    for (int i = 0; i < array.size(); i++) {
                        spaceObjects.add(new Asteroid(array.get(i).getAsJsonObject().get("name").toString(),
                                                      parseLongInt(array.get(i).getAsJsonObject().get("close_approach_data").getAsJsonArray().get(0).getAsJsonObject().get("miss_distance").getAsJsonObject().get("kilometers").toString()),
                                                      array.get(i).getAsJsonObject().get("is_potentially_hazardous_asteroid").toString()));
                    }
                    final TreeItem<Asteroid> root = new RecursiveTreeItem<Asteroid>(spaceObjects,RecursiveTreeObject::getChildren);
                    Platform.runLater(()->{
                        nearestObjects.setRoot(root);
                        nearestObjects.setShowRoot(false);
                    });
                }

                @Override
                public void onFailure(Exception exception) {
                    apiResults.put("nearestObjects",null);
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
        executor.shutdown();
        getCurrentDate();
    }
    private String getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
    private String parseLongInt(String longInt){
        StringBuilder result = new StringBuilder();
        longInt=longInt.substring(1,longInt.length()-1);
        while(longInt.length()>=4){
            result.insert(0,longInt.substring(longInt.length()-3));
            result.insert(0,".");
            longInt=longInt.substring(0,longInt.length()-3);
        }
        result.insert(0,longInt);
        return result.toString();
    }
    class Asteroid extends RecursiveTreeObject<Asteroid>{
        StringProperty name;
        StringProperty approach;
        StringProperty hazardous;

        public Asteroid(String name, String approach, String hazardous){
            this.name = new SimpleStringProperty(name);
            this.approach = new SimpleStringProperty(approach);
            this.hazardous = new SimpleStringProperty(hazardous);
        }
    }
}
