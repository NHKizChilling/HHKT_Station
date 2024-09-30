/*
 * @ (#) DangNhapController.java       1.0     28/09/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import connectdb.ConnectDB;
import dao.TaiKhoan_DAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import entity.TaiKhoan;
import gui.TrangChu_GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
    protected void onLoginButtonClick() throws SQLException {
        ConnectDB.connect();
        String manv = txtTK.getText();
        String pass1 = pwd.getText();
        String pass2 = txtPwd.getText();
        if(manv == null || manv.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Vui lòng nhập mã nhân viên!");
            alert.showAndWait();
            txtTK.requestFocus();
            return;
        } else if (pass1 == null || pass1.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Vui lòng mật khẩu!");
            alert.showAndWait();
            pwd.requestFocus();
            return;
        }
        else {
            TaiKhoan taiKhoan = new TaiKhoan_DAO().getTaiKhoanTheoMaNV(manv);
            if(taiKhoan == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setContentText("Sai tên tài khoản!");
                alert.showAndWait();
                txtTK.requestFocus();
                return;
            } else {
                if(taiKhoan.getMatKhau().equals(pass1)) {
                    if(taiKhoan.getTrangThaiTK().equals("Đang hoạt động")) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Đăng nhập thành công");
                        alert.show();
                        try {
                            new TrangChu_GUI().changeScene("trang-chu.fxml");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setContentText("Tài khoản đã bị khóa!");
                        alert.showAndWait();
                        txtTK.requestFocus();
                        return;
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setContentText("Sai mật khẩu!");
                    alert.showAndWait();
                    pwd.requestFocus();
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
    }
    @FXML
    protected void onQuitButtonClick() throws Exception {
        TrangChu_GUI trangChuGui = new TrangChu_GUI();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        if (alert.showAndWait().get().getText().equals("OK")) {
            trangChuGui.changeScene("trang-chu.fxml");
        }
    }

    @FXML
    protected void onClickShowPassword() {
        if(pwd.isVisible()) {
            pwd.setVisible(false);
            txtPwd.setVisible(true);
            iShowPwd.setIcon(FontAwesomeIcons.EYE);
        } else {
            pwd.setVisible(true);
            txtPwd.setVisible(false);
            iShowPwd.setIcon(FontAwesomeIcons.EYE_SLASH);
        }
    }
}
