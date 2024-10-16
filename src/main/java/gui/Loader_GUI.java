package gui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Loader_GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = new AnchorPane();
        primaryStage.setTitle("Loader");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

    }
}
