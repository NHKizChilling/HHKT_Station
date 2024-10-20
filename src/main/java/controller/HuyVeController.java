package controller;

import dao.*;
import entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HuyVeController implements Initializable {

    @FXML
    private ComboBox<String> cb_search;

    @FXML
    private TextField txt_search;

    @FXML
    private Button btn_search;

    @FXML
    private TableView<Ve> tbl_thongTinVe;

    @FXML
    private TableColumn<Ve, String> col_maVe;

    @FXML
    private TableColumn<Ve, String> col_maHK;

    @FXML
    private TableColumn<Ve, String> col_tinhTrangVe;

    @FXML
    private TableColumn<Ve, String> col_khuHoi;

    @FXML
    private TableColumn<Ve, String> col_maSoCho;

    @FXML
    private TableColumn<Ve, String> col_maLichTrinh;

    @FXML
    private TableColumn<Ve, LocalDate> col_dob;

    @FXML
    private TableColumn<Ve, String> col_tenHK;

    @FXML
    private TableColumn<Ve, String> col_cccd;

    @FXML
    private Button btn_huyVe;

    @FXML
    private TextField txt_maHK;

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_sdt;

    @FXML
    private TextField txt_cccd;

    @FXML
    private TextField txt_tenHK;

    @FXML
    private TextField txt_ngayLap;

    @FXML
    private TextField txt_gaDi;

    @FXML
    private TextField txt_gaDen;

    @FXML
    private TextField txt_giaVe;

    @FXML
    private Button btn_lamMoi;

    @FXML
    private TextField txt_chietKhau;

    @FXML
    private TextField txt_hoanTien;

    @FXML
    private Label label_thongBao;

    private Ve_DAO ve_dao;
    private HanhKhach_DAO hanhKhach_dao;
    private CT_HoaDon_DAO ct_hoaDon_dao;
    private HoaDon_DAO hoaDon_dao;
    private LichTrinh_DAO lichTrinh_dao;
    private boolean isHuy = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ve_dao = new Ve_DAO();
        hanhKhach_dao = new HanhKhach_DAO();
        ct_hoaDon_dao = new CT_HoaDon_DAO();
        hoaDon_dao = new HoaDon_DAO();
        lichTrinh_dao = new LichTrinh_DAO();


        cb_search.getItems().addAll("Mã hành khách", "Số CCCD", "Số điện thoại");

        btn_search.setOnAction(e -> {
            String search = txt_search.getText();
            String type = cb_search.getValue();
            if (type == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Vui lòng chọn loại tìm kiếm");
                alert.show();
                return;
            }
            if (search.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Vui lòng nhập thông tin tìm kiếm");
                alert.show();
                return;
            }
            ArrayList<Ve> listVe = ve_dao.getDsTheoMaHK(search);
            renderTable(listVe);
        });

        btn_lamMoi.setOnAction(e -> {
            txt_search.clear();
            cb_search.getSelectionModel().clearSelection();
            tbl_thongTinVe.getItems().clear();

            txt_maHK.clear();
            txt_email.clear();
            txt_sdt.clear();
            txt_cccd.clear();
            txt_tenHK.clear();
            txt_ngayLap.clear();
            txt_gaDi.clear();
            txt_gaDen.clear();
            txt_giaVe.clear();
            txt_chietKhau.clear();
            txt_hoanTien.clear();
            label_thongBao.setText("");
        });

        tbl_thongTinVe.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Ve ve = tbl_thongTinVe.getSelectionModel().getSelectedItem();
                String maHKTmp = ve.getMaHK().getMaHanhKhach();
                HanhKhach hk = hanhKhach_dao.getHanhKhachTheoMa(maHKTmp);
                txt_maHK.setText(hk.getMaHanhKhach());
                txt_email.setText(hk.getEmail());
                txt_sdt.setText(hk.getSdt());
                txt_cccd.setText(hk.getSoCCCD());
                txt_tenHK.setText(hk.getTenHanhKhach());

                //ngày lập hóa đơn được lấy bằng cách lấy mã hóa đơn từ chi tiết hóa đơn sau
                //sau đó lấy ngày lập hóa đơn từ hóa đơn
                ChiTietHoaDon ctHoaDon = ct_hoaDon_dao.timTheoMaVe(ve.getMaVe());
                HoaDon hd = hoaDon_dao.getHoaDonTheoMa(ctHoaDon.getHoaDon().getMaHoaDon());

                // lấy giá vé
                txt_giaVe.setText(String.valueOf(ctHoaDon.getGiaVe()));

                LocalDateTime ngayLap = hd.getNgayLapHoaDon();
                // ngày theo format dd/MM/yyyy HH:mm
                txt_ngayLap.setText(ngayLap.getDayOfMonth() + "/" + ngayLap.getMonthValue() + "/" + ngayLap.getYear() + " " + ngayLap.getHour() + ":" + ngayLap.getMinute());

                LichTrinh lt = lichTrinh_dao.getLichTrinhTheoID(ve.getMaLT().getMaLichTrinh());
                txt_gaDi.setText(lt.getGaDi().getTenGa());
                txt_gaDen.setText(lt.getGaDen().getTenGa());

                LocalDateTime tgKhoiHanh = lt.getThoiGianKhoiHanh();
                LocalDateTime now = LocalDateTime.now();
                // nếu trả trước 4h thì hủy được, chiết khấu là 20%
                if (tgKhoiHanh.isAfter(now) && tgKhoiHanh.minusHours(4).isBefore(now)) {
                    txt_chietKhau.setText("20%");
                    txt_hoanTien.setText(String.valueOf(ctHoaDon.getGiaVe() * 0.8));
                    isHuy = true;
                } else {
                    label_thongBao.setText("Vé không thể hủy");
                }
            }
        });

        btn_huyVe.setOnAction(e -> {
            if (isHuy) {
                Ve ve = tbl_thongTinVe.getSelectionModel().getSelectedItem();
                if (ve != null) {
                    boolean result = ve_dao.updateTinhTrangVe(ve.getMaVe(), "Đã hủy");
                    if (result) {
                        label_thongBao.setText("Hủy vé thành công");
                        isHuy = false;
                    } else {
                        label_thongBao.setText("Hủy vé thất bại");
                    }
                } else {
                    label_thongBao.setText("Vui lòng chọn vé cần hủy");
                }
            } else if (txt_maHK.getText().isEmpty()) {
                label_thongBao.setText("Vui lòng chọn vé cần hủy");
            } else {
                label_thongBao.setText("Vé không thể hủy");
            }
        });
    }

    public void renderTable(ArrayList<Ve> listVe) {
        // TODO
        ObservableList<Ve> list = FXCollections.observableArrayList(listVe);
        tbl_thongTinVe.setItems(list);
        col_maVe.setCellValueFactory(new PropertyValueFactory<>("maVe"));
        col_maHK.setCellValueFactory(new PropertyValueFactory<>("maHK"));
        col_maSoCho.setCellValueFactory(new PropertyValueFactory<>("maSoCho"));
        col_maLichTrinh.setCellValueFactory(new PropertyValueFactory<>("maLT"));
        col_tinhTrangVe.setCellValueFactory(new PropertyValueFactory<>("tinhTrangVe"));
        col_khuHoi.setCellValueFactory(new PropertyValueFactory<>("khuHoi"));
        col_dob.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        col_tenHK.setCellValueFactory(new PropertyValueFactory<>("tenHK"));
        col_cccd.setCellValueFactory(new PropertyValueFactory<>("soCCCD"));
    }
}
