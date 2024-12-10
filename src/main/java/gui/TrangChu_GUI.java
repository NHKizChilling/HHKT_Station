package gui;

import connectdb.ConnectDB;
import controller.TrangChuController;
import entity.NhanVien;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Objects;

public class TrangChu_GUI extends Application {

    public static NhanVien nv;
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        nv = null;
        stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(TrangChu_GUI.class.getResource("dang-nhap.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Trang chủ");
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
        stage.show();
        stage.setOnCloseRequest(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Thoát");
            alert.setContentText("Bạn có chắc chắn muốn thoát?");
            alert.showAndWait();
            if (alert.getResult().getText().equals("OK")) {
                System.exit(0);
                try {
                    if (ConnectDB.getConnection() != null) {
                        ConnectDB.disconnect();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                e.consume();
            }
        });


    }

    public static void main(String[] args) {
        launch(args);
    }
}
