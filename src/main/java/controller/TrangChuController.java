/*
 * @ (#) TrangChuController.java       1.0     28/09/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import connectdb.ConnectDB;
import dao.*;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.Ve;
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
import java.util.ArrayList;
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
    private Button btnFQLHD;
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

    private Ve_DAO ve_dao = new Ve_DAO();
    private HoaDon_DAO hd_dao = new HoaDon_DAO();
    private CT_LichTrinh_DAO ctlt_dao = new CT_LichTrinh_DAO();

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
        paneMain.getChildren().clear();
        try {
            ConnectDB.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("gioi-thieu.fxml"));
            double width = paneMain.getWidth();
            double height = paneMain.getHeight();
            paneMain.getChildren().clear();
            paneMain.getChildren().add(loader.load());
            paneMain.setPrefSize(width, height);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        btnFQLHD.setOnMouseClicked(e -> chooseFeatureButton(btnFQLHD));
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

        ArrayList<HoaDon> dsHD = hd_dao.getDSHDLuuTam();
        for (HoaDon hd : dsHD) {
            //Xóa hóa đơn lưu hơn 15 phút
            if (hd.getNgayLapHoaDon().plusMinutes(15).isBefore(LocalDateTime.now())) {
                ArrayList<ChiTietHoaDon> dsCTHD = new CT_HoaDon_DAO().getCT_HoaDon(hd.getMaHoaDon());
                for (ChiTietHoaDon cthd : dsCTHD) {
                    if (cthd != null) {
                        Ve ve = ve_dao.getVeTheoID(cthd.getVe().getMaVe());
                        ve_dao.updateTinhTrangVe(ve.getMaVe(), "DaHuy");
                        ctlt_dao.updateCTLT(ve.getCtlt(), true);
                    }
                }
            }
        }
    }

    @FXML
    protected void showHoaDonGUI() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("qly-hoa-don.fxml"));
        double width = paneMain.getWidth();
        double height = paneMain.getHeight();
        paneMain.getChildren().clear();
        try {
            paneMain.getChildren().add(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        paneMain.setPrefSize(width, height);
    }

    @FXML
    protected void showNhanVienGUI() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("nhan-vien.fxml"));
        double width = paneMain.getWidth();
        double height = paneMain.getHeight();
        paneMain.getChildren().clear();
        try {
            paneMain.getChildren().add(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        paneMain.setPrefSize(width, height);
    }

    @FXML
    protected void showHKGUI() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("hanh-khach.fxml"));
        double width = paneMain.getWidth();
        double height = paneMain.getHeight();
        paneMain.getChildren().clear();
        try {
            paneMain.getChildren().add(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onClick() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("gioi-thieu.fxml"));
        double width = paneMain.getWidth();
        double height = paneMain.getHeight();
        paneMain.getChildren().clear();
        try {
            paneMain.getChildren().add(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        paneMain.setPrefSize(width, height);
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
            getData.hd = null;
            getData.hk = null;
            getData.lt = null;
            getData.ltkh = null;
            getData.dsve = null;
            getData.dscthd = null;
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
        List<Button> dsF = List.of(btnFVe, btnFQLHD, btnFNV, btnFHK, btnFBCTK, btnFCT);
        dsF.forEach(btn -> {
            if (btn.equals(btnChosed)) {
                btn.setStyle(btn.getStyle() + "-fx-background-color: lightgray;-fx-border-color: blue;");
            } else {
                btn.setStyle(style);
            }
        });
    }
}
