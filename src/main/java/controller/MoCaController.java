package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import connectdb.ConnectDB;
import dao.NhanVien_DAO;
import entity.NhanVien;
import gui.TrangChu_GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MoCaController implements Initializable {

    @FXML
    private JFXComboBox<String> cbNhanVien;

    @FXML
    private Label lbl_gioBatDau;

    @FXML
    private TextField txt_tienDauCa;

    @FXML
    private TextField txt_ghiChu;

    @FXML
    private JFXButton btn_moCa;

    @FXML
    private JFXButton btn_dangXuat;

    Double tienDauCa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NhanVien nv1 = getData.nv;

        if (nv1 != null) {
            setThongTin(nv1, LocalDateTime.now());
        } else {
            cbNhanVien.setDisable(false);
        }

        cbNhanVien.getItems().addAll(new NhanVien_DAO().getDSNhanVien().stream().map(nv -> nv.getTenNhanVien() + " - " + nv.getMaNhanVien()).toList());

        new AutoCompleteComboBoxListener<>(cbNhanVien);

        cbNhanVien.setOnAction(event -> {
            String maNhanVien = cbNhanVien.getValue().split(" - ")[1];
            NhanVien nv = new NhanVien_DAO().getNhanVien(maNhanVien);
            setThongTin(nv, LocalDateTime.now());
        });

        txt_tienDauCa.setOnAction(event -> {
            if (!txt_tienDauCa.getText().isEmpty()) {
                btn_moCa.fire();
            } else {
                txt_tienDauCa.setPromptText("Vui lòng nhập tiền đầu ca");
                txt_tienDauCa.requestFocus();
            }
        });

        btn_moCa.setOnAction(event -> {
            if (cbNhanVien.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Lỗi");
                alert.setContentText("Vui lòng chọn nhân viên để giao ca");
                alert.showAndWait();
                cbNhanVien.requestFocus();
                return;
            }

            //nếu tiền đầu ca là chữ thì thông báo
            if (!txt_tienDauCa.getText().matches("\\d*")) {
                txt_tienDauCa.setPromptText("Tiền đầu ca phải là số");
                txt_tienDauCa.requestFocus();
                return;
            }


            if (txt_tienDauCa.getText().isEmpty()) {
                txt_tienDauCa.setPromptText("Vui lòng nhập tiền đầu ca");
                txt_tienDauCa.requestFocus();
                return;
            }
            tienDauCa = Double.parseDouble(txt_tienDauCa.getText());

            if (tienDauCa < 1000000) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Lỗi");
                alert.setContentText("Tiền đầu ca phải có ít nhất 1000000đ");
                alert.showAndWait();
                txt_tienDauCa.requestFocus();
                return;
            }

            getData.tienDauCa = tienDauCa;
            getData.gioMoCa = LocalDateTime.now();
            getData.ghiChu = txt_ghiChu.getText();
            if (getData.nv == null) {
                getData.nv = new NhanVien_DAO().getNhanVien(cbNhanVien.getValue().split(" - ")[1]);
            }
            FXMLLoader fxmlLoader = new FXMLLoader(TrangChu_GUI.class.getResource("trang-chu.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            TrangChu_GUI.stage.close();
            TrangChu_GUI.stage.setScene(scene);
            TrangChu_GUI.stage.show();
            TrangChu_GUI.stage.centerOnScreen();
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

        txt_tienDauCa.setOnKeyTyped(event -> {
            //format tiền đầu ca
            if (!txt_tienDauCa.getText().matches("\\d*")) {
                txt_tienDauCa.setText(txt_tienDauCa.getText().replaceAll("[^\\d]", ""));
            }
        });
    }

    public void setThongTin(NhanVien nv, LocalDateTime gioBatDau) {
        cbNhanVien.setValue(nv.getTenNhanVien() + " - " + nv.getMaNhanVien());
        lbl_gioBatDau.setText(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(gioBatDau));
        txt_tienDauCa.requestFocus();
    }
}
