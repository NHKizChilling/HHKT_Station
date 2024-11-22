package controller;

import com.jfoenix.controls.JFXButton;
import dao.LoaiVe_DAO;
import entity.LoaiVe;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class TTHanhKhachController implements Initializable {
    @FXML
    private JFXButton btnLamMoi;

    @FXML
    private JFXButton btnHoanTat;

    @FXML
    private ComboBox<String> cbLoaiVe;

    @FXML
    private DatePicker dpNgaySinh;

    @FXML
    private Label lblTTCN;

    @FXML
    private TextField txtSoCCCD;

    @FXML
    private TextField txtTenHK;

    private LoaiVe_DAO loaiVe_dao = new LoaiVe_DAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbLoaiVe.getItems().addAll(loaiVe_dao.getAllLoaiVe().stream().map(LoaiVe::getTenLoaiVe).toList());
        //enable dpNgaySinh khi chọn loại vé Người cao tuổi hoặc Trẻ em
        cbLoaiVe.setOnAction(e -> {
            if (cbLoaiVe.getValue().equals("Người cao tuổi") || cbLoaiVe.getValue().equals("Trẻ em")) {
                dpNgaySinh.setDisable(false);
                if (cbLoaiVe.getValue().equals("Trẻ em")) {
                    txtSoCCCD.setDisable(true);
                } else {
                    txtSoCCCD.setDisable(false);
                }
            } else {
                dpNgaySinh.setDisable(true);
                txtSoCCCD.setDisable(false);
            }
        });

        btnLamMoi.setOnAction(e -> {
            txtTenHK.clear();
            txtSoCCCD.clear();
            dpNgaySinh.setValue(null);
            cbLoaiVe.setValue(null);
        });
    }
}
