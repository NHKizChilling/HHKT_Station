package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TrangChu_GUI extends Application {

    private static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("trang-chu.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Trang chủ");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
        primaryStage.show();
    }

    public void changeScene(String fxml) throws Exception {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stage.getScene().setRoot(pane);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
