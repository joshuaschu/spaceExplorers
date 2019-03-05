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
import de.joshuaschulz.space.objects.Asteroid;
import de.joshuaschulz.space.objects.GST;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainController implements Initializable {
    public JFXTreeTableView<Asteroid> nearestObjects;
    public JFXTreeTableColumn<Asteroid,String> nameColumn;
    public JFXTreeTableColumn<Asteroid,String> closesApproach;
    public JFXTreeTableColumn<Asteroid,String> isHazardous;
    public JFXTreeTableView<GST>  geomagneticStorms;
    public JFXTreeTableColumn<GST,String> dateColumn;
    public JFXTreeTableColumn<GST,String> timeColumn;
    public JFXTreeTableColumn<GST,String> categoryColumn;
    public ImageView epicImage;

    private ExecutorService executor = Executors.newCachedThreadPool();
    private Gson gson = new Gson();
    private HashMap<String,JsonObject> apiResults;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        apiResults = new HashMap<>();
        fillNearestObjects();
        fillGMS();
       showEPICImage();
        executor.shutdown();
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
                                                      parseLongInt(array.get(i).getAsJsonObject().get("close_approach_data").getAsJsonArray().get(0).getAsJsonObject().get("miss_distance").getAsJsonObject().get("kilometers").toString())+" km",
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
    }
    private void fillGMS(){
       dateColumn.setCellValueFactory(param -> param.getValue().getValue().date);
       timeColumn.setCellValueFactory(param -> param.getValue().getValue().time);
       categoryColumn.setCellValueFactory(param -> param.getValue().getValue().category);
       HashMap<String,String> params= new HashMap<>();
       params.put("startDate",getDateMinus1Year());
       params.put("endDate", getCurrentDate());
        ObservableList<GST> gsts = FXCollections.observableArrayList();
       try{
           executor.execute(new APIRequestHandler("https://api.nasa.gov/DONKI/GST",params, new AsyncAPICall() {
               @Override
               public void onSuccess(String result) {
                   JsonArray array = gson.fromJson(result,JsonArray.class);
                   for (int i = 0; i < array.size(); i++) {
                       gsts.add(new GST(parseTimestampToDate(array.get(i).getAsJsonObject().get("startTime").toString()),
                                        parseTimestampToTime(array.get(i).getAsJsonObject().get("startTime").toString()),
                                        array.get(i).getAsJsonObject().get("allKpIndex").getAsJsonArray().get(0).getAsJsonObject().get("kpIndex").toString()));
                   }
                   final TreeItem<GST> root = new RecursiveTreeItem<GST>(gsts,RecursiveTreeObject::getChildren);
                   Platform.runLater(()->{
                       geomagneticStorms.setRoot(root);
                       geomagneticStorms.setShowRoot(false);
                   });
               }
               @Override
               public void onFailure(Exception exception) {
                   apiResults.put("nearestObjects",null);
               }
           }));
       }catch (Exception e){

       }
    }

    private void showEPICImage(){
        Image image = new Image("https://epic.gsfc.nasa.gov/archive/natural/2015/10/31/png/epic_1b_20151031074844.png");
        epicImage.setImage(image);
        epicImage.setFitWidth(900);
        epicImage.setPreserveRatio(true);
    }

    //Helper Methods
    private String getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
    private String getDateMinus1Year(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1); // to get previous year add -1
        Date nextYear = cal.getTime();
        DateFormat newDate = new SimpleDateFormat("yyyy-MM-dd");
        return newDate.format(nextYear);
    }
    private String parseLongInt(String longInt){
        //ToDO: handle longer decimal places
        StringBuilder result = new StringBuilder();
        longInt=longInt.substring(1,longInt.length()-1);
        longInt=longInt.replace('.',',');
        while(longInt.length()>=4){
            result.insert(0,longInt.substring(longInt.length()-3));
            result.insert(0,".");
            longInt=longInt.substring(0,longInt.length()-3);
        }
        result.insert(0,longInt);
        return result.toString();
    }
    private String parseTimestampToDate(String timestamp){
        //TODO: parse date do not hardcode
        StringBuilder result = new StringBuilder();
        result.append(timestamp.substring(9,11));
        result.append(".");
        result.append(timestamp.substring(6,8));
        result.append(".");
        result.append(timestamp.substring(1,5));
        return result.toString();
    }
    private String parseTimestampToTime(String timestamp){
        //TODO: parse time do not hardcode
        return timestamp.substring(12,17)+" UTC";
    }
}
