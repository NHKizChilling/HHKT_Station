/*
 * @ (#) DangNhapController.java       1.0     28/09/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import connectdb.ConnectDB;
import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import entity.NhanVien;
import entity.TaiKhoan;
import gui.TrangChu_GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
        FXMLLoader fxmlLoader1 = new FXMLLoader(TrangChu_GUI.class.getResource("trang-chu.fxml"));
        Scene scene1 = new Scene(fxmlLoader1.load());
        TrangChu_GUI.stage.setScene(scene1);
        TrangChu_GUI.stage.show();
        TrangChu_GUI.stage.centerOnScreen();
        return;
//        String manv = txtTK.getText();
//        String pass1 = pwd.getText();
//        String pass2 = txtPwd.getText();
//        if(manv == null || manv.isEmpty()) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText(null);
//            alert.setTitle("Đăng nhập");
//            alert.setContentText("Vui lòng nhập mã nhân viên!");
//            alert.showAndWait();
//            txtTK.requestFocus();
//            return;
//        } else if (pass1 == null || pass1.isEmpty()) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText(null);
//            alert.setTitle("Đăng nhập");
//            alert.setContentText("Vui lòng mật khẩu!");
//            alert.showAndWait();
//            if(pwd.isVisible()) {
//                pwd.requestFocus();
//            } else {
//                txtPwd.requestFocus();
//            }
//            return;
//        }
//        else {
//            TaiKhoan taiKhoan = new TaiKhoan_DAO().getTaiKhoanTheoMaNV(manv);
//            if(taiKhoan == null) {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setHeaderText(null);
//                alert.setTitle("Đăng nhập");
//                alert.setContentText("Sai tên tài khoản!");
//                alert.showAndWait();
//                txtTK.requestFocus();
//                return;
//            } else {
//                if(taiKhoan.getMatKhau().equals(pass1)) {
//                    if(taiKhoan.getTrangThaiTK().equals("Đang hoạt động")) {
//                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                        alert.setHeaderText(null);
//                        alert.setTitle("Đăng nhập");
//                        alert.setContentText("Đăng nhập thành công");
//                        alert.show();
//                        getData.nv = new NhanVien_DAO().getNhanVien(manv);
//                        FXMLLoader fxmlLoader = new FXMLLoader(TrangChu_GUI.class.getResource("trang-chu.fxml"));
//                        Scene scene = new Scene(fxmlLoader.load());
//                        TrangChu_GUI.stage.setScene(scene);
//                        TrangChu_GUI.stage.show();
//                        TrangChu_GUI.stage.centerOnScreen();
//                        return;
//                    } else {
//                        Alert alert = new Alert(Alert.AlertType.ERROR);
//                        alert.setHeaderText(null);
//                        alert.setTitle("Đăng nhập");
//                        alert.setContentText("Tài khoản đã bị khóa!");
//                        alert.showAndWait();
//                        txtTK.requestFocus();
//                        return;
//                    }
//                } else {
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setHeaderText(null);
//                    alert.setTitle("Đăng nhập");
//                    alert.setContentText("Sai mật khẩu!");
//                    alert.showAndWait();
//                    if(pwd.isVisible()) {
//                        pwd.requestFocus();
//                    } else {
//                        txtPwd.requestFocus();
//                    }
//                    return;
//                }
//            }
//        }
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
            iShowPwd.setIcon(FontAwesomeIcons.EYE);
            if (pwd.isFocused()) {
                txtPwd.requestFocus();
            }
        } else {
            pwd.setVisible(true);
            txtPwd.setVisible(false);
            iShowPwd.setIcon(FontAwesomeIcons.EYE_SLASH);
            if (txtPwd.isFocused()) {
                pwd.requestFocus();
            }
        }
    }
}
