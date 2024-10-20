/*
 * @ (#) TrangChuController.java       1.0     28/09/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import connectdb.ConnectDB;
import dao.LichTrinh_DAO;
import gui.TrangChu_GUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   28/09/2024
 * version: 1.0
 */
public class TrangChuController implements Initializable {

    @FXML
    private AnchorPane paneMain;
    @FXML
    private Button btnFVe;
    @FXML
    private Button btnFNV;
    @FXML
    private Button btnFHK;
    @FXML
    private Button btnFBCTK;
    @FXML
    private Button btnFCT;
    @FXML
    private Label lblTenNhanVien;
    @FXML
    private Label timer;
    @FXML
    private Label lblNgay;

    Time time = new Time(DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
    private String style = null;
    Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1),
                    e -> {
                        time.oneSecondPassed();
                        timer.setText(time.getCurrentTime());
                    }));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblNgay.setText(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.now()));
        timer.setText(time.getCurrentTime());
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        style = btnFVe.getStyle();
        btnFVe.setOnMouseClicked(e -> {
            try {
                ConnectDB.connect();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            new LichTrinh_DAO().updateTrangThaiCT(false);
            chooseFeatureButton(btnFVe);
        });
        btnFNV.setOnMouseClicked(e -> chooseFeatureButton(btnFNV));
        btnFHK.setOnMouseClicked(e -> chooseFeatureButton(btnFHK));
        btnFBCTK.setOnMouseClicked(e -> chooseFeatureButton(btnFBCTK));
        btnFCT.setOnMouseClicked(e -> chooseFeatureButton(btnFCT));
        TrangChu_GUI.nv = getData.nv;
        lblTenNhanVien.setText("Chào, " + TrangChu_GUI.nv.getChucVu() + " " + TrangChu_GUI.nv.getTenNhanVien());
    }

    @FXML
    protected void showBanVeGUI() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("ban-ve.fxml"));
        double width = paneMain.getWidth();
        double height = paneMain.getHeight();
        try {
            paneMain.getChildren().clear();
            paneMain.getChildren().add(loader.load());
            paneMain.setPrefSize(width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void showNhanVienGUI() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("nhan-vien.fxml"));
        double width = paneMain.getWidth();
        double height = paneMain.getHeight();
        try {
            paneMain.getChildren().clear();
            paneMain.getChildren().add(loader.load());
            paneMain.setPrefSize(width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Home Page");
        alert.show();
    }

    @FXML
    protected void onBtnTKClick() {
        try {
            FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("thong-ke.fxml"));
            double width = paneMain.getWidth();
            double height = paneMain.getHeight();
            paneMain.getChildren().clear();
            paneMain.getChildren().add(loader.load());
            paneMain.setPrefSize(width, height);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void dangXuat() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Đăng xuất");
        alert.setContentText("Bạn có chắc chắn muốn đăng xuất?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            ConnectDB.disconnect();
            getData.nv = null;
            System.exit(0);
        }
    }

    @FXML
    protected void openUserManual() {
        try {
            File file = new File(getClass().getResource("/home.html").toURI());
            Desktop.getDesktop().open(file);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private void chooseFeatureButton(Button btnChosed) {
        List<Button> dsF = List.of(btnFVe, btnFNV, btnFHK, btnFBCTK, btnFCT);
        dsF.forEach(btn -> {
            if (btn.equals(btnChosed)) {
                btn.setStyle(btn.getStyle() + "-fx-background-color: skyblue;-fx-border-color: blue;");
            } else {
                btn.setStyle(style);
            }
        });
    }
}
