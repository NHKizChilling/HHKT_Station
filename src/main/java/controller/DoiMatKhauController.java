package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import dao.TaiKhoan_DAO;
import entity.NhanVien;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DoiMatKhauController implements Initializable {

    @FXML
    private JFXButton btnChangePWD;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private JFXCheckBox chkConfirm;

    @FXML
    private Label lblCCCD;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblHoNV;

    @FXML
    private Label lblMaNV;

    @FXML
    private Label lblSDT;

    @FXML
    private PasswordField pwdNew;

    @FXML
    private PasswordField pwdNewConfirm;

    @FXML
    private PasswordField pwdOld;

    private final TaiKhoan_DAO dao = new TaiKhoan_DAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NhanVien nhanVien = getData.nv;
        lblMaNV.setText(nhanVien.getMaNhanVien());
        lblHoNV.setText(nhanVien.getTenNhanVien());
        lblCCCD.setText(nhanVien.getSoCCCD());
        lblSDT.setText(nhanVien.getSdt());
        lblEmail.setText(nhanVien.getEmail());

        btnChangePWD.setOnMouseClicked(e -> {
            String oldPass = pwdOld.getText();
            String newPass = pwdNew.getText();
            String confirmPass = pwdNewConfirm.getText();
            if (oldPass.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập mật khẩu cũ");
                alert.showAndWait();
                pwdOld.requestFocus();
                return;
            }
            if (newPass.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập mật khẩu mới");
                alert.showAndWait();
                pwdNew.requestFocus();
                return;
            } else {
                if (newPass.length() < 6) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Mật khẩu mới phải có ít nhất 6 ký tự");
                    alert.showAndWait();
                    pwdNew.requestFocus();
                    return;
                } else if (newPass.length() > 20) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Mật khẩu mới không được quá 20 ký tự");
                    alert.showAndWait();
                    pwdNew.requestFocus();
                    return;
                } else if (newPass.equals(oldPass)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Mật khẩu mới không được trùng với mật khẩu cũ");
                    alert.showAndWait();
                    pwdNew.requestFocus();
                    return;
                } else if (newPass.matches("^[A-Za-z0-9@_.!?]{6,20}$")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Mật khẩu chỉ chứa số, chữ cái và các ký tự {@, ., _, !, ?}");
                    alert.showAndWait();
                    pwdNew.requestFocus();
                    return;
                }
            }
            if (confirmPass.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng xác nhận mật khẩu mới");
                alert.showAndWait();
                pwdNewConfirm.requestFocus();
                return;
            }
            if (!chkConfirm.isSelected()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng xác nhận thay đổi mật khẩu");
                alert.showAndWait();
                chkConfirm.requestFocus();
                return;
            }
            if (oldPass.equals(dao.getTaiKhoanTheoMaNV(nhanVien.getMaNhanVien()).getMatKhau())) {
                if (newPass.equals(confirmPass)) {
                    if (dao.doiMatKhau(nhanVien.getMaNhanVien(), newPass)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thành công");
                        alert.setHeaderText(null);
                        alert.setContentText("Đổi mật khẩu thành công");
                        alert.showAndWait();
                        pwdOld.clear();
                        pwdNew.clear();
                        pwdNewConfirm.clear();
                        chkConfirm.setSelected(false);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setHeaderText(null);
                        alert.setContentText("Đổi mật khẩu thất bại");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Mật khẩu mới không trùng khớp");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Mật khẩu cũ không đúng");
                alert.showAndWait();
            }
        });

        btnRefresh.setOnMouseClicked(e -> {
            pwdOld.clear();
            pwdNew.clear();
            pwdNewConfirm.clear();
            chkConfirm.setSelected(false);
        });
    }
}
