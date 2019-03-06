package de.joshuaschulz.space;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.joshuaschulz.connection.APIRequest;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application {
    private ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage primaryStage) throws Exception {
        Font font= Font.loadFont(getClass().getResource("/fonts/Fools-Errand.ttf").toExternalForm(), 18);
        Parent parent = FXMLLoader.load(this.getClass().getResource("/views/loading.fxml"));
        primaryStage.setTitle("Space Explorers");
        Scene scene = new Scene(parent,1464,1000);

        String css = this.getClass().getResource("/stylesheets/Style.css").toExternalForm();
        scene.getStylesheets().add
                (css);
        primaryStage.setScene(scene);
        primaryStage.show();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                Parent innerRoot = loadBackground();
                Scene innerScene = new Scene(innerRoot,1464,1000,true);
                innerScene.getStylesheets().add
                        (css);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        primaryStage.setScene(innerScene);
                    }
                });
            }
        });
        executor.shutdown();
    }

    private Parent loadBackground()  {
        try {
            Parent parent = FXMLLoader.load(this.getClass().getResource("/views/startup.fxml"));
            APIRequest apiRequest = new APIRequest("https://api.nasa.gov/planetary/apod");
            String result = apiRequest.performAPICall();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson( result, JsonObject.class);
            String element = jsonObject.get("url").toString();
            parent.setStyle("-fx-background-image: url("+element+")");
            return parent;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
