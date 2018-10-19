package de.joshuaschulz.space;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage primaryStage) throws Exception {
        Font font= Font.loadFont(getClass().getResource("/fonts/Fools-Errand.ttf").toExternalForm(), 18);
        Parent root = FXMLLoader.load(this.getClass().getResource("/views/main.fxml"));
        primaryStage.setTitle("Space Explorers");
        Button btn = new Button();
        btn.setText("Say 'Hallo World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                System.out.println("Hello World");
            }
        });
        Scene scene = new Scene(root,1464,1000);


        String css = this.getClass().getResource("/stylesheets/Style.css").toExternalForm();
        scene.getStylesheets().add
                (css);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
