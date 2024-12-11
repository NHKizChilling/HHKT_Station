package controller;

import com.itextpdf.text.DocumentException;
import com.jfoenix.controls.JFXButton;
import dao.*;
import entity.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
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
    private LoaiVe_DAO loaiVe_dao;

    private int tongSoVe = 0;
    private double tongTienVe = 0;
    private double tongLePhi = 0;
    private double tongTienTra = 0;

    private ArrayList<Ve> listVe;
    private HashMap<String, Double> mapLePhi;
    private final PrintPDF printPDF = new PrintPDF();
    private final NumberFormat currencyVN = NumberFormat.getCurrencyInstance(Locale.of("vi", "VN"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO
        initDAO();
        initComponent();

        btn_backTraVe.setOnAction(actionEvent -> {
            // đóng cửa số trả vé
            Scene scene = btn_backTraVe.getScene();
            scene.getWindow().hide();

            lamMoi();
        });

        btn_xacNhan.setOnAction(actionEvent -> {
            if (checkInput()) {
                String ten = txt_ten.getText();
                String cccd = txt_cccd.getText();
                String sdt = txt_sdt.getText();

                KhachHang khHuyVe = khachHang_dao.getKHTheoCCCD(cccd);
                if (khHuyVe == null) {
                    khHuyVe = khachHang_dao.getKhachHangTheoSDT(sdt);
                }

                if (!kiemTraKH(khHuyVe)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText("Không thể hủy vé");
                    alert.setContentText("Khách hàng không trùng khách hàng đã mua vé hoặc trùng thông tin hành khách trên vé");
                    alert.showAndWait();
                    return;
                }

                HoaDon hoaDon = new HoaDon();
                hoaDon.setKhachHang(khHuyVe);
                hoaDon.setNhanVien(getData.nv);
                hoaDon.setNgayLapHoaDon(LocalDateTime.now());
                hoaDon.setTrangThai(false);

                if (hoaDon_dao.createTempInvoice(hoaDon)) {
                    getData.hd = hoaDon_dao.getHoaDonVuaTao();
                } else {
                    // thông báo
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText("Không thể tạo hóa đơn");
                    alert.showAndWait();
                    return;
                }

                hoaDon = getData.hd;
                hoaDon.setTrangThai(true);
                hoaDon.setTongTien(0 - tongTienTra);
                hoaDon.setTongGiamGia(tongLePhi); // tổng giảm giá = tổng lệ phí
                hoaDon.setKhuyenMai(new KhuyenMai(null));

                if (hoaDon_dao.update(hoaDon)) {
                    try {
                        for (Ve ve : listVe) {
                            ChiTietHoaDon ctHoaDon = new ChiTietHoaDon();
                            ctHoaDon.setHoaDon(new HoaDon(hoaDon.getMaHoaDon()));
                            ctHoaDon.setVe(ve);
                            ChiTietHoaDon ctHoaDonCu = ct_hoaDon_dao.getCT_HoaDonTheoMaVe(ve.getMaVe());
                            ctHoaDon.setGiaVe(0 - ctHoaDonCu.getGiaVe()); // giá vé = giá vé cũ
                            ctHoaDon.setGiaGiam(mapLePhi.get(ve.getMaVe())); // giảm giá = lệ phí
                            if (ct_hoaDon_dao.create(ctHoaDon)) {
                                ve.setTinhTrangVe("DaHuy");
                                ve_dao.update(ve);
                            } else {
                                // thông báo
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Lỗi");
                                alert.setHeaderText("Không thể cập nhật chi tiết hóa đơn");
                                alert.showAndWait();
                                return;
                            }
                            printPDF.inHDHuy(hoaDon);
                            lamMoi();
                        }
                    } catch (IOException | DocumentException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // thông báo
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText("Không thể cập nhật hóa đơn");
                    alert.showAndWait();
                    return;
                }


                // đóng cửa số trả vé
                Scene scene = btn_xacNhan.getScene();
                scene.getWindow().hide();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Dữ liệu không hợp lệ");
                alert.setContentText("Vui lòng kiểm tra lại thông tin khách hàng");
                alert.showAndWait();
            }
        });

        txt_cccd.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                searchKhachHang();
            }
        });
        txt_sdt.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                searchKhachHang();
            }
        });

    }

    private void initComponent() {
        listVe = getData.dsve;
        mapLePhi = getData.mapLePhi;


        renderTable();
        renderLabel();
    }


    private void renderTable() {
        tongSoVe = listVe.size();

        ObservableList<Ve> list = FXCollections.observableArrayList(listVe);
        tbl_dsVe.setItems(list);
        tbl_dsVe.setItems(list);
        col_stt.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(tbl_dsVe.getItems().indexOf(p.getValue()) + 1 + ""));
        col_hoTen.setCellValueFactory(p -> {
            String ten = p.getValue().getTenHanhKhach();
            return new SimpleStringProperty(ten);
        });

        col_thongTinVe.setCellValueFactory(p -> {
            Ve ve = ve_dao.getVeTheoID(p.getValue().getMaVe());
            LoaiVe lv = loaiVe_dao.getLoaiVeTheoMa(ve.getLoaiVe().getMaLoaiVe());
            LichTrinh lt = new LichTrinh_DAO().getLichTrinhTheoID(ve.getCtlt().getLichTrinh().getMaLichTrinh());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new SimpleStringProperty(new Ga_DAO().getGaTheoMaGa(lt.getGaDi().getMaGa()).getTenGa() + " - " +
                    new Ga_DAO().getGaTheoMaGa(lt.getGaDen().getMaGa()).getTenGa() + " \n" +
                    "Giờ khởi hành: \n" + lt.getThoiGianKhoiHanh().format(formatter) + " \n" +
                    lv.getTenLoaiVe());
        });

        col_tienVe.setCellValueFactory(p -> {
            Ve ve = ve_dao.getVeTheoID(p.getValue().getMaVe());
            ChiTietHoaDon ctHoaDon = ct_hoaDon_dao.getCT_HoaDonTheoMaVe(ve.getMaVe());
            return new SimpleStringProperty(currencyVN.format(ctHoaDon.getGiaVe() - ctHoaDon.getGiaGiam()));
        });

        col_lePhi.setCellValueFactory(p -> {
            Ve ve = ve_dao.getVeTheoID(p.getValue().getMaVe());
            return new SimpleStringProperty(currencyVN.format(mapLePhi.get(ve.getMaVe())));
        });

        col_tienTra.setCellValueFactory(p -> {
            Ve ve = ve_dao.getVeTheoID(p.getValue().getMaVe());
            ChiTietHoaDon ctHoaDon = ct_hoaDon_dao.getCT_HoaDonTheoMaVe(ve.getMaVe());
            return new SimpleStringProperty(currencyVN.format(ctHoaDon.getGiaVe() - ctHoaDon.getGiaGiam() - mapLePhi.get(ve.getMaVe())));
        });
    }

    private void renderLabel() {

        for (Ve ve : listVe) {
            ChiTietHoaDon ctHoaDon = ct_hoaDon_dao.getCT_HoaDonTheoMaVe(ve.getMaVe());
            tongTienVe += ctHoaDon.getGiaVe() - ctHoaDon.getGiaGiam();
            tongLePhi += mapLePhi.get(ve.getMaVe());
            tongTienTra += ctHoaDon.getGiaVe() - ctHoaDon.getGiaGiam() - mapLePhi.get(ve.getMaVe());
        }

        tongTienVe = Math.round(tongTienVe * 1000.0) / 1000.0;
        tongLePhi = Math.round(tongLePhi * 1000.0) / 1000.0;
        tongTienTra = Math.round(tongTienTra * 1000.0) / 1000.0;

        lbl_tongSoVe.setText("Tổng số vé: " + tongSoVe);
        lbl_tongTienVe.setText("Tổng tiền vé: " + currencyVN.format(tongTienVe));
        lbl_tongLePhi.setText("Tổng lệ phí: " + currencyVN.format(tongLePhi));
        lbl_tongTienTra.setText("Tổng tiền trả: " + currencyVN.format(tongTienTra));
    }

    private boolean checkInput() {
        String cccd = txt_cccd.getText();
        String sdt = txt_sdt.getText();

        if (cccd.isEmpty() || sdt.isEmpty()) {
            return false;
        }

        if(!cccd.matches("[0-9]{9}|[0-9]{12}")) {
            return false;
        }

        return sdt.matches("[0-9]{10}|[0-9]{11}");
    }

    private void searchKhachHang() {
        String cccd = txt_cccd.getText();
        String sdt = txt_sdt.getText();

        if (!cccd.isEmpty() || !sdt.isEmpty()) {
            KhachHang kh = khachHang_dao.getKHTheoCCCD(cccd);
            if (kh == null) {
                kh = khachHang_dao.getKhachHangTheoSDT(sdt);
            }

            if (kh != null) {
                txt_ten.setText(kh.getTenKH());
                txt_sdt.setText(kh.getSdt());
                txt_cccd.setText(kh.getSoCCCD());
            } else {
                txt_ten.clear();
                txt_sdt.clear();
                txt_cccd.clear();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Không tìm thấy hành khách");
            }
        }
    }

    private boolean kiemTraKH(KhachHang kh) {
    if (kh == null) {;
        return false;
    }

    ChiTietHoaDon ctHoaDon = ct_hoaDon_dao.getCT_HoaDonTheoMaVe(listVe.getFirst().getMaVe());
    HoaDon hd = hoaDon_dao.getHoaDonTheoMa(ctHoaDon.getHoaDon().getMaHoaDon());
    KhachHang tmp = khachHang_dao.getKhachHangTheoMaKH(hd.getKhachHang().getMaKH());

    if (!tmp.getMaKH().equals(kh.getMaKH())) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Không trùng khách hàng");
        return true;
    }

    return listVe.stream()
            .map(ve -> khachHang_dao.getKhachHangTheoMaKH(ve.getKhachHang().getMaKH()))
            .anyMatch(tmpKH -> tmpKH.getMaKH().equals(kh.getMaKH()));
}

    private void initDAO() {
        ve_dao = new Ve_DAO();
        khachHang_dao = new KhachHang_DAO();
        hoaDon_dao = new HoaDon_DAO();
        ct_hoaDon_dao = new CT_HoaDon_DAO();
        loaiVe_dao = new LoaiVe_DAO();

        listVe = getData.dsve;
        mapLePhi = getData.mapLePhi;
    }

    private void lamMoi() {
        txt_ten.clear();
        txt_cccd.clear();
        txt_sdt.clear();
    }
}
