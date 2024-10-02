package gui;

import controller.TrangChuController;
import entity.NhanVien;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;

public class TrangChu_GUI extends Application {

    public static NhanVien nv;
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        nv = null;
        stage = new Stage();
        Parent root = FXMLLoader.load(TrangChu_GUI.class.getResource("dang-nhap.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Trang chủ");
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
        stage.setResizable(false);
        stage.show();
    }
}
