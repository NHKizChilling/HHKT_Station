/*
 * @ (#) DangNhapController.java       1.0     28/09/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import connectdb.ConnectDB;
import dao.KhuyenMai_DAO;
import dao.LichTrinh_DAO;
import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import entity.NhanVien;
import entity.TaiKhoan;
import gui.TrangChu_GUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;

/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   28/09/2024
 * version: 1.0
 */
public class DangNhapController {

    @FXML
    private TextField txtTK;
    @FXML
    private PasswordField pwd;
    @FXML
    private TextField txtPwd;
    @FXML
    private FontAwesomeIcon iShowPwd;

    @FXML
    protected void changeInputPass() {
        txtPwd.setText(pwd.getText());
    }
    @FXML
    protected void changeInputTextPass() {
        pwd.setText(txtPwd.getText());
    }
    @FXML
    protected void onLoginButtonClick() throws SQLException, IOException {
        ConnectDB.connect();

//        FXMLLoader fxmlLoader = new FXMLLoader(TrangChu_GUI.class.getResource("trang-chu.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        TrangChu_GUI.stage.setScene(scene);
//        TrangChu_GUI.stage.show();
//        TrangChu_GUI.stage.centerOnScreen();
        String manv = txtTK.getText();
        String pass1 = pwd.getText();
        String pass2 = txtPwd.getText();
        if(manv == null || manv.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Đăng nhập");
            alert.setContentText("Vui lòng nhập mã nhân viên!");
            alert.showAndWait();
            txtTK.requestFocus();
            return;
        } else if (pass1 == null || pass1.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Đăng nhập");
            alert.setContentText("Vui lòng mật khẩu!");
            alert.showAndWait();
            if(pwd.isVisible()) {
                pwd.requestFocus();
            } else {
                txtPwd.requestFocus();
            }
            return;
        }
        else {
            TaiKhoan taiKhoan = new TaiKhoan_DAO().getTaiKhoanTheoMaNV(manv);
            if(taiKhoan == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Đăng nhập");
                alert.setContentText("Sai tên tài khoản!");
                alert.showAndWait();
                txtTK.requestFocus();
                return;
            } else {
                if(taiKhoan.getMatKhau().equals(pass1)) {
                    if(taiKhoan.getTrangThaiTK().equals("Đang hoạt động")) {

                        getData.nv = new NhanVien_DAO().getNhanVien(manv);
                        new LichTrinh_DAO().updateTrangThaiCT(false);
                        new KhuyenMai_DAO().kichHoatKhuyenMai();
                        new KhuyenMai_DAO().khoaKhuyenMai();
//                        Stage stg = new Stage();
//                        FXMLLoader fxmlLoader1 = new FXMLLoader(TrangChu_GUI.class.getResource("loader.fxml"));
//                        Scene scene1 = new Scene(fxmlLoader1.load());
//                        stg.setScene(scene1);
//                        stg.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
//                        ProgressBar progressBar = (ProgressBar) stg.getScene().lookup("#progressBar");
//                        Label lblLoading = (Label) stg.getScene().lookup("#lblLoading");
//                        TrangChu_GUI.stage.close();
//                        stg.show();

                        FXMLLoader fxmlLoader = new FXMLLoader(TrangChu_GUI.class.getResource("trang-chu.fxml"));
                        Scene scene = new Scene(fxmlLoader.load());
                        TrangChu_GUI.stage.setScene(scene);
                        TrangChu_GUI.stage.show();
                        TrangChu_GUI.stage.centerOnScreen();
//                        Timeline timeline = new Timeline(
//                                new KeyFrame(Duration.seconds(0.5),
//                                        e -> {
//                                            progressBar.setProgress(progressBar.getProgress() + 0.1);
//                                            if (Math.round(progressBar.getProgress() * 100.0) % 20 == 0) {
//                                                lblLoading.setText("Loading.." + Math.round(progressBar.getProgress() * 100.0) + "%");
//                                            } else {
//
//                                                lblLoading.setText("Loading..." + Math.round(progressBar.getProgress() * 100.0) + "%");
//                                            }
//                                        }));
//
//                        timeline.setCycleCount(10);
//                        timeline.play();
//                        timeline.setOnFinished(e -> {
//                                    stg.close();
//                                    try {
//                                        Scene scene = new Scene(fxmlLoader.load());
//                                        TrangChu_GUI.stage.setScene(scene);
//                                        TrangChu_GUI.stage.show();
//                                        TrangChu_GUI.stage.centerOnScreen();
//                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                                        alert.setHeaderText(null);
//                                        alert.setTitle("Đăng nhập");
//                                        alert.setContentText("Đăng nhập thành công");
//                                        alert.show();
//                                    } catch (IOException ioException) {
//                                        ioException.printStackTrace();
//                                    }
//                                });
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setTitle("Đăng nhập");
                        alert.setContentText("Tài khoản đã bị khóa!");
                        alert.showAndWait();
                        txtTK.requestFocus();
                        return;
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("Đăng nhập");
                    alert.setContentText("Sai mật khẩu!");
                    alert.showAndWait();
                    if(pwd.isVisible()) {
                        pwd.requestFocus();
                    } else {
                        txtPwd.requestFocus();
                    }
                    return;
                }
            }
        }
    }

    @FXML
    protected void onResetButtonClick(){
        txtTK.setText(null);
        pwd.setText(null);
        txtPwd.setText(null);
        txtTK.requestFocus();
    }
    @FXML
    protected void onQuitButtonClick() throws Exception {
        NhanVien nv = null;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn thoát?");
        alert.setTitle("Đăng nhập");
        if (alert.showAndWait().get().getText().equals("OK")) {
            System.exit(0);
        }
    }

    @FXML
    protected void onClickShowPassword() {
        if(pwd.isVisible()) {
            pwd.setVisible(false);
            txtPwd.setVisible(true);
            txtPwd.setText(pwd.getText());
            iShowPwd.setIcon(FontAwesomeIcons.EYE);
            if (pwd.isFocused()) {
                txtPwd.requestFocus();
            }
        } else {
            pwd.setVisible(true);
            txtPwd.setVisible(false);
            pwd.setText(txtPwd.getText());
            iShowPwd.setIcon(FontAwesomeIcons.EYE_SLASH);
            if (txtPwd.isFocused()) {
                pwd.requestFocus();
            }
        }
    }

    @FXML
    protected void quenMatKhau() {
        try {
            ConnectDB.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        //Tạo alert nhập mã nhân viên và số cccd
        alert.setTitle("Quên mật khẩu");
        alert.setHeaderText(null);
        alert.setContentText("Nhập mã nhân viên và số CCCD để lấy lại mật khẩu");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField txtMaNV = new TextField();
        txtMaNV.setPromptText("Mã nhân viên");
        TextField txtCCCD = new TextField();
        txtCCCD.setPromptText("Số CCCD");
        Label label = new Label("Nhập thông tin để lấy lại mật khẩu:");
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        grid.add(label, 0, 0, 2, 1);
        grid.add(new Label("Mã nhân viên:"), 0, 1);
        grid.add(new Label("Số CCCD:"), 0, 2);
        grid.add(txtMaNV, 1, 1);
        grid.add(txtCCCD, 1, 2);
        alert.getDialogPane().setContent(grid);
        //set icon
        ImageView imageView = new ImageView(new Image("file:src/main/resources/img/logo.png"));
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        alert.setGraphic(imageView);
        alert.showAndWait();
        if (alert.getResult().getText().equals("OK")) {
            String manv = txtMaNV.getText();
            String cccd = txtCCCD.getText();
            if (manv == null || manv.isEmpty()) {
                                txtMaNV.requestFocus();
                return;
            } else if (cccd == null || cccd.isEmpty()) {

                txtCCCD.requestFocus();
                return;
            } else if (new NhanVien_DAO().getNhanVien(manv) == null) {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setHeaderText(null);
                alert1.setTitle("Quên mật khẩu");
                alert1.setContentText("Mã nhân viên không tồn tại!");
                alert1.showAndWait();
                return;
            } else {
                if (new NhanVien_DAO().getNhanVien(manv).getSoCCCD().equals(cccd)) {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setHeaderText(null);
                    alert1.setTitle("Quên mật khẩu");
                    alert1.setContentText("Mật khẩu của bạn là: " + new TaiKhoan_DAO().getTaiKhoanTheoMaNV(manv).getMatKhau());
                    alert1.showAndWait();
                }
            }
        }
    }
}
