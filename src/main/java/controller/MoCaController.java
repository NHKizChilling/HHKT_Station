package controller;

import connectdb.ConnectDB;
import entity.NhanVien;
import gui.TrangChu_GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class MoCaController implements Initializable {

    @FXML
    private TextField txt_tenNV;

    @FXML
    private Label lbl_gioBatDau;

    @FXML
    private TextField txt_tienDauCa;

    @FXML
    private TextField txt_ghiChu;

    @FXML
    private Button btn_moCa;

    @FXML
    private Button btn_dangXuat;

    private NhanVien nv;

    Double tienDauCa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nv = getData.nv;

        setThongTin(nv, LocalDateTime.now());

        btn_moCa.setOnAction(event -> {
            if (txt_tienDauCa.getText().isEmpty()) {
                System.out.println("Vui lòng nhập tiền đầu ca");
                return;
            }
            tienDauCa = Double.parseDouble(txt_tienDauCa.getText());

            getData.tienDauCa = tienDauCa;
            getData.gioMoCa = LocalDateTime.now();
            getData.ghiChu = txt_ghiChu.getText();

            FXMLLoader fxmlLoader = new FXMLLoader(TrangChu_GUI.class.getResource("trang-chu.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            TrangChu_GUI.stage.setScene(scene);
            TrangChu_GUI.stage.show();
            TrangChu_GUI.stage.centerOnScreen();

            // Đóng cửa sổ hiện tại
            Stage currentStage = (Stage) btn_moCa.getScene().getWindow();
            currentStage.close();
        });

        btn_dangXuat.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Đăng xuất");
            alert.setContentText("Bạn có chắc chắn muốn đăng xuất?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                ConnectDB.disconnect();
                getData.nv = null;
                getData.hd = null;
                getData.kh = null;
                getData.lt = null;
                getData.ltkh = null;
                getData.dsve = null;
                getData.dscthd = null;
                System.exit(0);
            }
        });
    }

    public void setThongTin(NhanVien nv, LocalDateTime gioBatDau) {
        txt_tenNV.setText(nv.getTenNhanVien());
        lbl_gioBatDau.setText(gioBatDau.getDayOfMonth() + "/" + gioBatDau.getMonthValue() + "/" + gioBatDau.getYear() + " " + gioBatDau.getHour() + ":" + gioBatDau.getMinute());
    }
}
