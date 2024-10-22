/*
 * @ (#) QLyHoaDonController.java       1.0     20/10/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import connectdb.ConnectDB;
import dao.*;
import entity.*;
import gui.TrangChu_GUI;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   20/10/2024
 * version: 1.0
 */
public class QLyHoaDonController implements Initializable {

    @FXML
    private Button btnChiTiet;

    @FXML
    private Button btnInHD;

    @FXML
    private Button btnInLaiVe;

    @FXML
    private Button btnThanhToan;

    @FXML
    private Button btnLamMoi;

    @FXML
    private ComboBox<String> cbLoaiVe;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colGiaGiam;


    @FXML
    private TableColumn<ChiTietHoaDon, String> colTongTienVe;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colGiaVe;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colLoaiCho;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colLoaiVe;

    @FXML
    private TableColumn<HoaDon, String> colMaHD;

    @FXML
    private TableColumn<HoaDon, String> colNgayLapHD;

    @FXML
    private TableColumn<HoaDon, Integer> colSLVe;

    @FXML
    private TableColumn<HoaDon, Integer> colSTT;

    @FXML
    private TableColumn<ChiTietHoaDon, Integer> colSTT1;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colTTVe;

    @FXML
    private TableColumn<HoaDon, String> colThanhTien;

    @FXML
    private TableColumn<HoaDon, String> colKH;

    @FXML
    private TableColumn<HoaDon, String> colTienGiam;

    @FXML
    private TableColumn<HoaDon, String> colTienThue;

    @FXML
    private TableColumn<HoaDon, String> colTienVe;

    @FXML
    private DatePicker dpNgayLapHD;

    @FXML
    private ToggleGroup hoaDon;

    @FXML
    private RadioButton radioAllHD;

    @FXML
    private RadioButton radioHDLuuTam;

    @FXML
    private RadioButton radioHDTrongNgay;

    @FXML
    private TableView<ChiTietHoaDon> tbCTHD;

    @FXML
    private TableView<HoaDon> tbhd;

    @FXML
    private TextField txtGiaGiam;

    @FXML
    private TextField txtGiaVe;

    @FXML
    private TextField txtMaHD;

    @FXML
    private TextField txtMaVe;

    @FXML
    private TextField txtTenHK;

    @FXML
    private TextField txtTenKH;

    @FXML
    private TextField txtThanhTienVe;

    @FXML
    private TextField txtTienGiam;

    @FXML
    private TextField txtTienThue;

    @FXML
    private TextField txtTimKiem;

    @FXML
    private TextField txtTongTien;

    private HoaDon_DAO hoaDon_dao = new HoaDon_DAO();
    private CT_HoaDon_DAO ct_hoaDon_dao = new CT_HoaDon_DAO();
    private CT_LichTrinh_DAO ct_lichTrinh_dao = new CT_LichTrinh_DAO();
    private ChoNgoi_DAO choNgoi_dao = new ChoNgoi_DAO();
    private LichTrinh_DAO lichTrinh_dao = new LichTrinh_DAO();
    private HanhKhach_DAO hanhKhach_dao = new HanhKhach_DAO();
    private Ve_DAO ve_dao = new Ve_DAO();
    private LoaiVe_DAO loaiVe_dao = new LoaiVe_DAO();
    private Toa_DAO toa_dao = new Toa_DAO();
    private LoaiToa_DAO loaiToa_dao = new LoaiToa_DAO();
    private ArrayList<HoaDon> listHD = new ArrayList<>();
    private ArrayList<ChiTietHoaDon> listCTHD = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        NumberFormat nf = DecimalFormat.getCurrencyInstance();
        cbLoaiVe.getItems().addAll("Người lớn", "Trẻ em", "Học sinh, sinh viên", "Người cao tuổi");
        //Bảng cthd
        colSTT1.setCellValueFactory(p -> new SimpleIntegerProperty(tbCTHD.getItems().indexOf(p.getValue()) + 1).asObject());
        colTTVe.setCellValueFactory(param -> {
            Ve ve = param.getValue().getVe();
            LichTrinh lt = new LichTrinh_DAO().getLichTrinhTheoID(ve_dao.getVeTheoID(ve.getMaVe()).getCtlt().getLichTrinh().getMaLichTrinh());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new SimpleStringProperty(  lt.getChuyenTau().getSoHieutau()+ " - " + new Ga_DAO().getGaTheoMaGa(lt.getGaDi().getMaGa()).getTenGa() + " - " + new Ga_DAO().getGaTheoMaGa(lt.getGaDen().getMaGa()).getTenGa() + "\n" + formatter.format(lt.getThoiGianKhoiHanh()) + " - " + formatter.format(lt.getThoiGianDuKienDen()));
        });

        colLoaiCho.setCellValueFactory(param -> {
            Ve ve = param.getValue().getVe();
            ChoNgoi cn = new ChoNgoi_DAO().getChoNgoiTheoMa(ve_dao.getVeTheoID(ve.getMaVe()).getCtlt().getChoNgoi().getMaChoNgoi());
            return new SimpleStringProperty(new LoaiToa_DAO().getLoaiToaTheoMa(new Toa_DAO().getToaTheoID(cn.getToa().getMaToa()).getLoaiToa().getMaLoaiToa()).getTenLoaiToa());
        });

        colLoaiVe.setCellValueFactory(param -> {
            Ve ve = ve_dao.getVeTheoID(param.getValue().getVe().getMaVe());

            return new SimpleStringProperty(loaiVe_dao.getLoaiVeTheoMa(ve.getLoaiVe().getMaLoaiVe()).getTenLoaiVe());
        });

        colGiaVe.setCellValueFactory(param -> {
            Ve ve = ve_dao.getVeTheoID(param.getValue().getVe().getMaVe());
            ChiTietLichTrinh ctlt = ct_lichTrinh_dao.getCTLTTheoCN(ve.getCtlt().getLichTrinh().getMaLichTrinh(), ve.getCtlt().getChoNgoi().getMaChoNgoi());
            return new SimpleStringProperty(nf.format(ctlt.getGiaCho()));
        });

        colGiaGiam.setCellValueFactory(param -> new SimpleStringProperty(nf.format(param.getValue().getGiaGiam())));

        colTongTienVe.setCellValueFactory(param -> {
            return new SimpleStringProperty(nf.format(param.getValue().getGiaVe()));
        });
        tbCTHD.setOnMouseClicked(event -> {
            ChiTietHoaDon cthd = tbCTHD.getSelectionModel().getSelectedItem();
            txtMaVe.setText(cthd.getVe().getMaVe());
            txtTenHK.setText(ve_dao.getVeTheoID(cthd.getVe().getMaVe()).getTenHanhKhach());
            cbLoaiVe.setValue(loaiVe_dao.getLoaiVeTheoMa(ve_dao.getVeTheoID(cthd.getVe().getMaVe()).getLoaiVe().getMaLoaiVe()).getTenLoaiVe());
            txtGiaVe.setText(nf.format(cthd.getGiaVe()));
            txtGiaGiam.setText(nf.format(cthd.getGiaGiam()));
            txtThanhTienVe.setText(nf.format(cthd.getGiaVe() - cthd.getGiaGiam()));
            btnInLaiVe.setDisable(false);
        });
        //Bảng hd
        colSTT.setCellValueFactory(p -> new SimpleIntegerProperty(tbhd.getItems().indexOf(p.getValue()) + 1).asObject());
        colMaHD.setCellValueFactory(new PropertyValueFactory<HoaDon,String>("maHoaDon"));
        colNgayLapHD.setCellValueFactory(p -> new SimpleStringProperty(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(p.getValue().getNgayLapHoaDon())));
        colKH.setCellValueFactory(p -> new SimpleStringProperty(hanhKhach_dao.getHanhKhachTheoMa(p.getValue().getHanhKhach().getMaHanhKhach()).getTenHanhKhach()));
        colSLVe.setCellValueFactory(p -> {
            listCTHD = ct_hoaDon_dao.getCT_HoaDon(p.getValue().getMaHoaDon());
            return new SimpleIntegerProperty(listCTHD.size()).asObject();
        });
        colTienVe.setCellValueFactory(p -> {
            listCTHD = ct_hoaDon_dao.getCT_HoaDon(p.getValue().getMaHoaDon());
            double tongTien = 0;
            for (ChiTietHoaDon cthd : listCTHD) {
                tongTien += cthd.getGiaVe();
            }
            return new SimpleStringProperty(nf.format(tongTien));
        });
        colTienGiam.setCellValueFactory(p -> new SimpleStringProperty(nf.format(p.getValue().getTongGiamGia())));
        colTienThue.setCellValueFactory(p -> new SimpleStringProperty(nf.format(p.getValue().getTongTien() * 0.1)));
        colThanhTien.setCellValueFactory(p -> new SimpleStringProperty(nf.format(p.getValue().getTongTien())));
        tbhd.setOnMouseClicked(event -> {
            HoaDon hd = tbhd.getSelectionModel().getSelectedItem();
            if (hd != null) {
                txtMaHD.setText(hd.getMaHoaDon());
                txtTenKH.setText(hanhKhach_dao.getHanhKhachTheoMa(hd.getHanhKhach().getMaHanhKhach()).getTenHanhKhach());
                dpNgayLapHD.setValue(hd.getNgayLapHoaDon().toLocalDate());
                txtTongTien.setText(nf.format(hd.getTongTien()));
                txtTienGiam.setText(nf.format(hd.getTongGiamGia()));
                txtTienThue.setText(nf.format(hd.getTongTien() * 0.1));
                btnChiTiet.setDisable(false);
                btnInHD.setDisable(false);
                if (radioHDLuuTam.isSelected()) {
                    btnThanhToan.setDisable(false);
                    btnInHD.setDisable(true);
                } else {
                    btnThanhToan.setDisable(true);
                }
            }
        });

        txtTimKiem.setOnKeyReleased(event -> {
            if (txtTimKiem.getText() != null && !txtTimKiem.getText().isEmpty()) {
                radioAllHD.setDisable(false);
                radioHDLuuTam.setDisable(true);
                radioHDTrongNgay.setDisable(false);
            } else {
                radioAllHD.setDisable(true);
                radioHDLuuTam.setDisable(false);
                radioHDTrongNgay.setDisable(false);
            }
            if (event.getCode().toString().equals("ENTER")) {
                timKiemHD(null);
            }
        });

        radioHDLuuTam.setOnAction(event -> {
            lamMoi();
            listHD = hoaDon_dao.getDSHDLuuTam();
            listHD.removeIf(hd -> hd.isTrangThai() || hd.getNgayLapHoaDon().plusMinutes(15).isBefore(LocalDateTime.now()));
            tbhd.getItems().clear();
            tbhd.getItems().addAll(listHD);
        });

        radioHDTrongNgay.setOnAction(event -> {
            if (txtTimKiem.getText() == null || txtTimKiem.getText().isEmpty())  {
                lamMoi();
                listHD = hoaDon_dao.getDSHDTheoNgay(LocalDateTime.now());
                tbhd.getItems().clear();
                tbhd.getItems().addAll(listHD);
            }
        });

        btnInHD.setOnAction(event -> {
            HoaDon hd = tbhd.getSelectionModel().getSelectedItem();
            try {
                new PrintPDF().inHoaDon(hd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        btnInLaiVe.setOnAction(event -> {
            ChiTietHoaDon cthd = tbCTHD.getSelectionModel().getSelectedItem();

            try {
                new PrintPDF().inVe(ve_dao.getVeTheoID(cthd.getVe().getMaVe()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        btnChiTiet.setOnAction(event -> {
            HoaDon hd = tbhd.getSelectionModel().getSelectedItem();
            listCTHD = ct_hoaDon_dao.getCT_HoaDon(hd.getMaHoaDon());
            tbCTHD.getItems().clear();
            tbCTHD.getItems().addAll(listCTHD);
        });

        btnThanhToan.setOnAction(event -> {
            HoaDon hd = tbhd.getSelectionModel().getSelectedItem();
            getData.hd = hd;
            getData.hk = hanhKhach_dao.getHanhKhachTheoMa(hd.getHanhKhach().getMaHanhKhach());
            ArrayList<ChiTietHoaDon> dscthd = ct_hoaDon_dao.getCT_HoaDon(hd.getMaHoaDon());
            getData.dscthd = dscthd;
            ArrayList<Ve> dsve = new ArrayList<>();
            for (ChiTietHoaDon cthd : dscthd) {
                Ve ve = ve_dao.getVeTheoID(cthd.getVe().getMaVe());
                ve.setCtlt(ct_lichTrinh_dao.getCTLTTheoCN(ve.getCtlt().getLichTrinh().getMaLichTrinh(), ve.getCtlt().getChoNgoi().getMaChoNgoi()));
                ve.setLoaiVe(loaiVe_dao.getLoaiVeTheoMa(ve.getLoaiVe().getMaLoaiVe()));
                dsve.add(ve);
            }
            getData.dsve = dsve;
            try {
                AnchorPane acpHoaDon = FXMLLoader.load(TrangChu_GUI.class.getResource("hoa-don.fxml"));
                Stage stgHoaDon = new Stage();
                stgHoaDon.setTitle("Thanh toán");
                stgHoaDon.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
                stgHoaDon.setScene(new Scene(acpHoaDon));
                stgHoaDon.sizeToScene();
                stgHoaDon.show();
                Button btnLuuTam = (Button) stgHoaDon.getScene().lookup("#btnLuuTamHD");
                btnLuuTam.setDisable(true);
                stgHoaDon.setOnCloseRequest(e1 -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Xác nhận");
                    alert.setHeaderText(null);
                    alert.setContentText("Bạn có chắc chắn muốn thoát?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        HoaDon hoaDon = hoaDon_dao.getHoaDonVuaTao();
                        if (!hoaDon.isTrangThai() && hoaDon.getTongTien() == 0) {
                            hoaDon_dao.delete(hoaDon);
                        }
                        stgHoaDon.close();
                        radioAllHD.setSelected(true);
                        lamMoi();
                    }
                });

                Button btnBack = (Button) acpHoaDon.lookup("#btnBackBanVe");
                btnBack.setOnMouseClicked(e1 -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Xác nhận");
                    alert.setHeaderText(null);
                    alert.setContentText("Bạn có chắc chắn muốn thoát?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        HoaDon hoaDon = hoaDon_dao.getHoaDonVuaTao();
                        if (!hoaDon.isTrangThai() && hoaDon.getTongTien() == 0) {
                            hoaDon_dao.delete(hoaDon);
                        }
                        stgHoaDon.close();
                        radioAllHD.setSelected(true);
                        lamMoi();
                    }
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            btnInHD.setDisable(false);
        });

        btnLamMoi.setOnAction(event -> {
            lamMoi();
            //hủy chọn tất cả radio button
            radioAllHD.setSelected(true);
        });
    }

    protected void lamMoi() {
        txtMaHD.clear();
        txtTenKH.clear();
        txtTongTien.clear();
        txtTienGiam.clear();
        txtTienThue.clear();
        txtTimKiem.clear();
        txtMaVe.clear();
        txtTenHK.clear();
        txtGiaVe.clear();
        txtGiaGiam.clear();
        txtThanhTienVe.clear();
        dpNgayLapHD.setValue(null);
        cbLoaiVe.setValue(cbLoaiVe.getPromptText());
        tbhd.getItems().clear();
        tbCTHD.getItems().clear();
        btnChiTiet.setDisable(true);
        btnInHD.setDisable(true);
        btnThanhToan.setDisable(true);
        btnInLaiVe.setDisable(true);
        txtTimKiem.requestFocus();
    }

    @FXML
    protected void timKiemHD(MouseEvent event) {
        String ma = txtTimKiem.getText();
        // tim kiem hoa don theo ma
        if (ma == null || ma.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập mã hóa đơn");
            alert.show();
            txtTimKiem.requestFocus();
            return;
        } else {
            if (radioHDTrongNgay.isSelected()) {
                HoaDon hd = hoaDon_dao.getHoaDonTheoMa(ma);
                if (hd == null) {
                    listHD = hoaDon_dao.getHoaDonTheoKH(ma);
                    listHD.removeIf(hd1 -> !hd1.getNgayLapHoaDon().toLocalDate().isEqual(LocalDateTime.now().toLocalDate()));
                    if (listHD.size() <= 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setHeaderText(null);
                        alert.setContentText("Không tìm thấy hóa đơn");
                        alert.show();
                        lamMoi();
                        return;
                    } else {
                        lamMoi();
                        tbhd.getItems().addAll(listHD);
                    }
                } else {
                    lamMoi();
                    if (hd.getNgayLapHoaDon().toLocalDate().isEqual(LocalDateTime.now().toLocalDate())) {
                        tbhd.getItems().add(hd);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setHeaderText(null);
                        alert.setContentText("Không tìm thấy hóa đơn");
                        alert.show();
                        lamMoi();
                        radioAllHD.setSelected(true);
                        return;
                    }
                }
            } else {
                HoaDon hd = hoaDon_dao.getHoaDonTheoMa(ma);
                if (hd == null) {
                    listHD = hoaDon_dao.getHoaDonTheoKH(ma);
                    if (listHD.size() <= 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setHeaderText(null);
                        alert.setContentText("Không tìm thấy hóa đơn");
                        alert.show();
                        lamMoi();
                        return;
                    } else {
                        lamMoi();
                        tbhd.getItems().addAll(listHD);
                    }
                } else {
                    lamMoi();
                    tbhd.getItems().addAll(listHD);
                }
            }
        }
    }
}
