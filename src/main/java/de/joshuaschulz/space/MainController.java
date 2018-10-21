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
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainController implements Initializable {
    public JFXTreeTableView<SpaceObject> nearestObjects;
    public JFXTreeTableColumn<SpaceObject,String> nameColumn;
    public JFXTreeTableColumn<SpaceObject,String> closesApproach;
    public JFXTreeTableColumn<SpaceObject,String> isHazardous;

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
        ObservableList<SpaceObject> spaceObjects = FXCollections.observableArrayList();
        try {
            executor.execute(new APIRequestHandler("https://api.nasa.gov/neo/rest/v1/feed/today", params, new AsyncAPICall() {
                @Override
                public void onSuccess(String result) {
                    JsonObject jsonObject = gson.fromJson(result,JsonObject.class);
                    apiResults.put("nearestObjects",jsonObject);
                    JsonElement values = jsonObject.get("near_earth_objects");
                    JsonObject objectJson = values.getAsJsonObject();
                    JsonArray array = objectJson.getAsJsonArray("2018-10-21");
                    for (int i = 0; i < array.size(); i++) {
                        spaceObjects.add(new SpaceObject(array.get(i).getAsJsonObject().get("name").toString(),
                                                         array.get(i).getAsJsonObject().get("close_approach_data").getAsJsonArray().get(0).getAsJsonObject().get("miss_distance").getAsJsonObject().get("kilometers").toString(),
                                                         array.get(i).getAsJsonObject().get("is_potentially_hazardous_asteroid").toString()));
                    }
                    final TreeItem<SpaceObject> root = new RecursiveTreeItem<SpaceObject>(spaceObjects,RecursiveTreeObject::getChildren);
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
    }

    private boolean fetchAPI(String baseURL, Map<String,String> params){
        if(params!=null) {
            try {
                executor.execute(new APIRequestHandler(baseURL, params, new AsyncAPICall() {
                    @Override
                    public void onSuccess(String result) {
                    //    JsonObject jsonObject = gson.fromJson(result,JsonObject.class);
                     //   apiResults.put(baseURL,jsonObject);
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        apiResults.put(baseURL,null);
                    }
                }));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }else {
            try {
                executor.execute(new APIRequestHandler(baseURL, new AsyncAPICall() {
                    @Override
                    public void onSuccess(String result) {
                        JsonObject jsonObject = gson.fromJson(result,JsonObject.class);
                        apiResults.put(baseURL,jsonObject);
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        apiResults.put(baseURL,null);
                    }
                }));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }
    class SpaceObject extends RecursiveTreeObject<SpaceObject>{
        StringProperty name;
        StringProperty approach;
        StringProperty hazardous;

        public SpaceObject(String name, String approach, String hazardous){
            this.name = new SimpleStringProperty(name);
            this.approach = new SimpleStringProperty(approach);
            this.hazardous = new SimpleStringProperty(hazardous);
        }
    }
}
