package de.joshuaschulz.space;

import de.joshuaschulz.connection.APIRequestHandler;
import de.joshuaschulz.connection.AsyncAPICall;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
        Parent root = FXMLLoader.load(this.getClass().getResource("/views/main.fxml"));
        primaryStage.setTitle("Space Explorers");
        Scene scene = new Scene(root,1464,1000);

        String css = this.getClass().getResource("/stylesheets/Style.css").toExternalForm();
        scene.getStylesheets().add
                (css);
        primaryStage.setScene(scene);
        primaryStage.show();
        loadBackground(root);
    }

    private void loadBackground(Parent parent) {
        try {
            executor.execute(new APIRequestHandler(new AsyncAPICall() {
                @Override
                public void onSuccess(String result) {
                    System.out.println(result);
                }
            }));

        }catch (Exception e){
            e.printStackTrace();
        }
        executor.shutdown();
    }
}
