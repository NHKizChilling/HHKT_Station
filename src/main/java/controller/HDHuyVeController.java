package controller;

import com.jfoenix.controls.JFXButton;
import dao.CT_HoaDon_DAO;
import dao.HoaDon_DAO;
import dao.KhachHang_DAO;
import dao.Ve_DAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.Ve;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class HDHuyVeController implements Initializable {

    @FXML
    private TextField txt_ten;

    @FXML
    private TextField txt_cccd;

    @FXML
    private TextField txt_sdt;

    @FXML
    private TableView<Ve> tbl_dsVe;

    @FXML
    private TableColumn<Ve, String> col_stt;

    @FXML
    private TableColumn<Ve, String> col_hoTen;

    @FXML
    private TableColumn<Ve, String> col_thongTinVe;

    @FXML
    private TableColumn<Ve, String> col_tienVe;

    @FXML
    private TableColumn<Ve, String> col_lePhi;

    @FXML
    private TableColumn<Ve, String> col_tienTra;

    @FXML
    private Label lbl_tongSoVe;

    @FXML
    private Label lbl_tongTienVe;

    @FXML
    private Label lbl_tongLePhi;

    @FXML
    private Label lbl_tongTienTra;

    @FXML
    private Button btn_backTraVe;

    @FXML
    private JFXButton btn_xacNhan;

    private Ve_DAO ve_dao;
    private KhachHang_DAO khachHang_dao;
    private HoaDon_DAO hoaDon_dao;
    private CT_HoaDon_DAO ct_hoaDon_dao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO
    }

    private void renderTable() {

    }

    private void renderLabel() {

    }

    private boolean checkInput() {
        return true;
    }

    private void initDAO() {
        ve_dao = new Ve_DAO();
        khachHang_dao = new KhachHang_DAO();
        hoaDon_dao = new HoaDon_DAO();
        ct_hoaDon_dao = new CT_HoaDon_DAO();
    }
}
