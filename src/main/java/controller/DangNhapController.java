/*
 * @ (#) DangNhapController.java       1.0     28/09/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import gui.TrangChu_GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    protected void onLoginButtonClick() {
        if(txtTK.getText().isEmpty() || pwd.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập tên tài khoản");
            alert.showAndWait();
            txtTK.requestFocus();
            return;
        } else if(txtTK.getText().equals("admin") && pwd.getText().equals("admin")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Đăng nhập thành công");
            alert.show();
            return;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Sai tên tài khoản hoặc mật khẩu");
            alert.showAndWait();
            txtTK.requestFocus();
            return;
        }
    }

    @FXML
    protected void onResetButtonClick(){
        txtTK.setText(null);
        pwd.setText(null);
    }
    @FXML
    protected void onQuitButtonClick() throws Exception {
        TrangChu_GUI trangChuGui = new TrangChu_GUI();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        if (alert.showAndWait().get().getText().equals("OK")) {
            trangChuGui.changeScene("trang-chu.fxml");
        }
    }
}
