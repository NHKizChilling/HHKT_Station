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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO
        initDAO();
        initComponent();

        btn_backTraVe.setOnAction(actionEvent -> {
            // đóng cửa số trả vé
            Scene scene = btn_backTraVe.getScene();
            scene.getWindow().hide();
        });

        btn_xacNhan.setOnAction(actionEvent -> {
            if (checkInput()) {
                String ten = txt_ten.getText();
                String cccd = txt_cccd.getText();
                String sdt = txt_sdt.getText();

                KhachHang kh = khachHang_dao.getKHTheoCCCD(cccd);
//                if (kh == null) {
//                    // thông báo
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("Người hủy vé phải là người mua vé hoặc là chủ vé");
//                    return;
//                }

                HoaDon hoaDon = new HoaDon();
                hoaDon.setKhachHang(kh);
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
                hoaDon.setTongGiamGia(0 - tongLePhi);
                hoaDon.setKhuyenMai(new KhuyenMai(null));

                if (hoaDon_dao.update(hoaDon)) {
                    try {
                        printPDF.inHDHuy(hoaDon);
                        for (Ve ve : listVe) {
                            ChiTietHoaDon ctHoaDon = new ChiTietHoaDon();
                            ctHoaDon.setHoaDon(getData.hd);
                            ctHoaDon.setVe(ve);
                            ChiTietHoaDon ctHoaDonCu = ct_hoaDon_dao.getCT_HoaDonTheoMaVe(ve.getMaVe());
                            ctHoaDon.setGiaVe(0 - ctHoaDonCu.getGiaVe()); // giá vé = giá vé cũ
                            ctHoaDon.setGiaGiam(0 - mapLePhi.get(ve.getMaVe())); // giảm giá = lệ phí
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
            Ve ve = ve_dao.getVeTheoID(p.getValue().getMaVe());
            KhachHang kh = khachHang_dao.getKhachHangTheoMaKH(ve.getKhachHang().getMaKH());
            return new SimpleStringProperty(kh.getTenKH());
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
            return new SimpleStringProperty(ctHoaDon.getGiaVe() - ctHoaDon.getGiaGiam() + "");
        });

        col_lePhi.setCellValueFactory(p -> {
            Ve ve = ve_dao.getVeTheoID(p.getValue().getMaVe());
            return new SimpleStringProperty(mapLePhi.get(ve.getMaVe()) + "");
        });

        col_tienTra.setCellValueFactory(p -> {
            Ve ve = ve_dao.getVeTheoID(p.getValue().getMaVe());
            ChiTietHoaDon ctHoaDon = ct_hoaDon_dao.getCT_HoaDonTheoMaVe(ve.getMaVe());
            return new SimpleStringProperty(ctHoaDon.getGiaVe() - ctHoaDon.getGiaGiam() + mapLePhi.get(ve.getMaVe()) + "");
        });
    }

    private void renderLabel() {

        for (Ve ve : listVe) {
            ChiTietHoaDon ctHoaDon = ct_hoaDon_dao.getCT_HoaDonTheoMaVe(ve.getMaVe());
            tongTienVe += ctHoaDon.getGiaVe() - ctHoaDon.getGiaGiam();
            tongLePhi += mapLePhi.get(ve.getMaVe());
            tongTienTra += ctHoaDon.getGiaVe() - ctHoaDon.getGiaGiam() + mapLePhi.get(ve.getMaVe());
        }

        lbl_tongSoVe.setText("Tổng số vé: " + tongSoVe + " vé");
        lbl_tongTienVe.setText("Tổng tiền vé: " + tongTienVe + "VNĐ");
        lbl_tongLePhi.setText("Tổng lệ phí: " + tongLePhi + "VNĐ");
        lbl_tongTienTra.setText("Tổng tiền trả: " + tongTienTra + "VNĐ");
    }

    private boolean checkInput() {
        String cccd = txt_cccd.getText();
        String sdt = txt_sdt.getText();

        System.out.println(cccd);
        System.out.println(sdt);
        System.out.println(txt_ten.getText());

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

    private void initDAO() {
        ve_dao = new Ve_DAO();
        khachHang_dao = new KhachHang_DAO();
        hoaDon_dao = new HoaDon_DAO();
        ct_hoaDon_dao = new CT_HoaDon_DAO();
        loaiVe_dao = new LoaiVe_DAO();

        listVe = getData.dsve;
        mapLePhi = getData.mapLePhi;
    }
}
