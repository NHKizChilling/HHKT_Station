/*
 * @ (#) HoaDonController.java       1.0     18/10/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import com.itextpdf.text.DocumentException;
import dao.*;
import entity.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   18/10/2024
 * version: 1.0
 */
public class HoaDonController implements Initializable {
    @FXML
    private TableView<ChiTietHoaDon> tbCTHD;
    @FXML
    private TableColumn<ChiTietHoaDon, String> colGiaGiam;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colGiaVe;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colLoaiCho;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colLoaiVe;

    @FXML
    private TableColumn<ChiTietHoaDon, Integer> colSTT;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colTTVe;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colThanhTien;

    @FXML
    private TextField txtMaHD;
    @FXML
    private TextField txtMaNV;
    @FXML
    private TextField txtNgayLapHD;
    @FXML
    private TextField txtKH;
    @FXML
    private TextField txtSDT;
    @FXML
    private TextField txtTenNV;
    @FXML
    private TextField txtThanhTien;
    @FXML
    private TextField txtTienKH;
    @FXML
    private TextField txtTienTra;
    @FXML
    private Button btnInHD;
    @FXML
    private Button btnLuuTamHD;
    @FXML
    private Button btnThanhToan;
    @FXML
    private Button btnGia1;

    @FXML
    private Button btnGia2;

    @FXML
    private Button btnGia3;

    @FXML
    private Button btnGia4;

    private ArrayList<Ve> dsve;
    private ArrayList<ChiTietHoaDon> dscthd;
    private double tongTien = 0;
    private double tongGiamGia = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Tạo timeline xác đinhj thời gian xóa hóa đơn lưu tạm quá 15 phút không thanh toán
//        Timeline timeline = new Timeline(
//                new KeyFrame(Duration.seconds(1),
//                        e -> {
//                            ArrayList<HoaDon> dsHD = new HoaDon_DAO().getDSHDLuuTam();
//                            for (HoaDon hd : dsHD) {
//                                if (hd.getNgayLapHoaDon().plusMinutes(15).isBefore(LocalDateTime.now())) {
//                                    ArrayList<ChiTietHoaDon> dsCTHD = new CT_HoaDon_DAO().getCT_HoaDon(hd.getMaHoaDon());
//                                    for (ChiTietHoaDon cthd : dsCTHD) {
//                                        new CT_LichTrinh_DAO().updateCTLT(cthd.getVe().getCtlt(), true);
//                                    }
//                                    new HoaDon_DAO().delete(hd);
//                                }
//                            }
//                        }));
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.play();
        //Tạo thời gian chạy ngược cho thời gian còn lại của hóa đơn lưu tạm
//        Timeline timeline1 = new Timeline(
//                new KeyFrame(Duration.seconds(1),
//                        e -> {
//                            ArrayList<HoaDon> dsHD = new HoaDon_DAO().getDSHDLuuTam();
//                            for (HoaDon hd : dsHD) {
//                                if (hd.getNgayLapHoaDon().plusMinutes(15).isAfter(LocalDateTime.now())) {
//                                    long time = 15 * 60 - (LocalDateTime.now().getSecond() - hd.getNgayLapHoaDon().getSecond());
//                                    long minute = time / 60;
//                                    long second = time % 60;
//                                    String timeLeft = minute + ":" + second;
//                                    hd.setThoiGianConLai(timeLeft);
//                                    new HoaDon_DAO().update(hd);
//                                }
//                            }
//                        }));
//        timeline1.setCycleCount(Timeline.INDEFINITE);
//        timeline1.play();


        NumberFormat df = DecimalFormat.getCurrencyInstance();
        NhanVien nhanVien = getData.nv;
        HoaDon hd = getData.hd;
        txtMaNV.setText(nhanVien.getMaNhanVien());
        txtTenNV.setText(nhanVien.getTenNhanVien());
        txtMaHD.setText(getData.hd.getMaHoaDon());
        txtNgayLapHD.setText(getData.hd.getNgayLapHoaDon().toString());
        txtKH.setText(getData.hk.getTenHanhKhach());
        txtSDT.setText(getData.hk.getSdt());
        dscthd = new ArrayList<>();
        dsve = getData.dsve;
        for (Ve ve : dsve) {
            ChiTietHoaDon cthd = new ChiTietHoaDon(hd, ve);
            dscthd.add(cthd);
            tongTien += (cthd.getGiaVe() - 2000) * 1.1 + 2000;
            tongGiamGia += cthd.getGiaGiam();
        }
        tongTien = Math.round(tongTien / 1000) * 1000;
        tongGiamGia = Math.round(tongGiamGia / 1000) * 1000;
        tbCTHD.getItems().addAll(dscthd);
        colTTVe.setCellValueFactory(new PropertyValueFactory<ChiTietHoaDon,String>("ve"));
        colLoaiCho.setCellValueFactory(new PropertyValueFactory<ChiTietHoaDon,String>("ve"));
        colLoaiVe.setCellValueFactory(new PropertyValueFactory<ChiTietHoaDon,String>("ve"));
        colGiaVe.setCellValueFactory(new PropertyValueFactory<ChiTietHoaDon,String>("ve"));
        colGiaGiam.setCellValueFactory(new PropertyValueFactory<ChiTietHoaDon,String>("giaGiam"));
        colThanhTien.setCellValueFactory(new PropertyValueFactory<ChiTietHoaDon,String>("giaVe"));

        colSTT.setCellValueFactory(column -> new SimpleIntegerProperty(tbCTHD.getItems().indexOf(column.getValue()) + 1).asObject());

        colTTVe.setCellValueFactory(param -> {
            Ve ve = param.getValue().getVe();
            LichTrinh lt = new LichTrinh_DAO().getLichTrinhTheoID(ve.getCtlt().getLichTrinh().getMaLichTrinh());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new SimpleStringProperty(  lt.getChuyenTau().getSoHieutau()+ " - " + new Ga_DAO().getGaTheoMaGa(lt.getGaDi().getMaGa()).getTenGa() + " - " + new Ga_DAO().getGaTheoMaGa(lt.getGaDen().getMaGa()).getTenGa() + "\n" + formatter.format(lt.getThoiGianKhoiHanh()) + " - " + formatter.format(lt.getThoiGianDuKienDen()));
        });

        colLoaiCho.setCellValueFactory(param -> {
            Ve ve = param.getValue().getVe();
            ChoNgoi cn = new ChoNgoi_DAO().getChoNgoiTheoMa(ve.getCtlt().getChoNgoi().getMaChoNgoi());
            return new SimpleStringProperty(new LoaiToa_DAO().getLoaiToaTheoMa(new Toa_DAO().getToaTheoID(cn.getToa().getMaToa()).getLoaiToa().getMaLoaiToa()).getTenLoaiToa());
        });

        colLoaiVe.setCellValueFactory(param -> {
            Ve ve = param.getValue().getVe();

            return new SimpleStringProperty(ve.getLoaiVe().getTenLoaiVe());
        });

        colGiaVe.setCellValueFactory(param -> {
            return new SimpleStringProperty(df.format(param.getValue().getVe().getCtlt().getGiaCho()));
        });

        colGiaGiam.setCellValueFactory(param -> new SimpleStringProperty(df.format(param.getValue().getGiaGiam())));

        colThanhTien.setCellValueFactory(param -> {
            return new SimpleStringProperty(df.format(param.getValue().getGiaVe()));
        });
        txtThanhTien.setText(df.format(tongTien) + "");
        btnGia1.setOnAction(event -> {
            txtTienKH.setText(btnGia1.getText());
            try {
                txtTienTra.setText(Double.parseDouble(txtTienKH.getText()) - df.parse(txtThanhTien.getText()).doubleValue() + "");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        btnGia2.setOnAction(event -> {
            txtTienKH.setText(btnGia2.getText());
            try {
                txtTienTra.setText(Double.parseDouble(txtTienKH.getText()) - df.parse(txtThanhTien.getText()).doubleValue() + "");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        btnGia3.setOnAction(event -> {
            txtTienKH.setText(btnGia3.getText());
            try {
                txtTienTra.setText(Double.parseDouble(txtTienKH.getText()) - df.parse(txtThanhTien.getText()).doubleValue() + "");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        btnGia4.setOnAction(event -> {
            txtTienKH.setText(btnGia4.getText());
            try {
                txtTienTra.setText(Double.parseDouble(txtTienKH.getText()) - df.parse(txtThanhTien.getText()).doubleValue() + "");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        if (!txtThanhTien.getText().isEmpty() && txtTienKH.getText() != null) {
            double t = tongTien/1000;
            t = Math.round(t);

            btnGia1.setText(df.format(Math.round(t) * 1000 / 10 * 10));
            btnGia2.setText(df.format(Math.round(t) * 2000 / 10 * 10));
            btnGia3.setText(df.format(Math.round(t) * 3000 / 10 * 10));
            btnGia4.setText(df.format(Math.round(t) * 4000 / 10 * 10));
        }


        btnLuuTamHD.setOnAction(event -> {
            ArrayList<HoaDon> temp_invoices = new HoaDon_DAO().getDSHDLuuTam().stream().filter(h -> !h.getNgayLapHoaDon().plusMinutes(15).isAfter(LocalDateTime.now())).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            temp_invoices = temp_invoices.stream().filter(h -> !(new CT_HoaDon_DAO().getCT_HoaDon(h.getMaHoaDon()).size() == 0)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            if (temp_invoices.size() >= 5) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Lỗi lưu tạm hóa đơn");
                alert.setContentText("Chỉ được lưu tạm 5 hóa đơn");
                alert.showAndWait();
                return;
            }
            hd.setTongTien(tongTien);
            hd.setTongGiamGia(tongGiamGia);
            hd.setTrangThai(false);
            getData.hd = hd;
            if(new HoaDon_DAO().update(hd)) {
                ArrayList<Ve> list = new ArrayList<>();
                ArrayList<ChiTietHoaDon> listcthd_new = new ArrayList<>();
                for (Ve ve : dsve) {
                    new CT_LichTrinh_DAO().updateCTLT(ve.getCtlt(), false);
                    new Ve_DAO().create(ve);
                    Ve v_new = new Ve_DAO().getLaiVe(ve);
                    ve.setMaVe(v_new.getMaVe());
                    list.add(ve);
                    listcthd_new.add(new ChiTietHoaDon(hd, ve));
                }
                for (ChiTietHoaDon cthd : dscthd) {
                    new CT_HoaDon_DAO().create(cthd);
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Lưu tạm hóa đơn thành công");
                alert.showAndWait();
                getData.dsve = list;
                getData.dscthd = listcthd_new;
            }
        });

        btnThanhToan.setOnAction(event -> {
            if (txtTienKH.getText().isEmpty() || txtTienKH.getText() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Lỗi thanh toán");
                alert.setContentText("Vui lòng chọn số tiền khách hàng trả");
                alert.showAndWait();
            } else {
                hd.setTongTien(tongTien);
                hd.setTongGiamGia(tongGiamGia);
                hd.setTrangThai(true);
                getData.hd = hd;
                if(new HoaDon_DAO().update(hd)) {
                    ArrayList<Ve> list = new ArrayList<>();
                    ArrayList<ChiTietHoaDon> listcthd_new = new ArrayList<>();
                    for (Ve ve : dsve) {
                        new CT_LichTrinh_DAO().updateCTLT(ve.getCtlt(), false);
                        new Ve_DAO().create(ve);
                        Ve v_new = new Ve_DAO().getLaiVe(ve);
                        ve.setMaVe(v_new.getMaVe());
                        list.add(ve);
                        listcthd_new.add(new ChiTietHoaDon(hd, ve));
                    }
                    for (ChiTietHoaDon cthd : listcthd_new) {
                        if (new CT_HoaDon_DAO().getCT_HoaDon(cthd.getHoaDon().getMaHoaDon()).size() == 0) {
                            new CT_HoaDon_DAO().create(cthd);
                        }
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText("Thanh toán thành công");
                    btnInHD.setDisable(false);
                    btnThanhToan.setDisable(true);
                    btnLuuTamHD.setDisable(true);
                    alert.showAndWait();
                    getData.dsve = list;
                    getData.dscthd = listcthd_new;
                }
            }
        });

        btnInHD.setOnAction(event -> {
            PrintPDF printPDF = new PrintPDF();
            try {
                printPDF.inHoaDon(getData.hd);
                ArrayList<Ve> list = getData.dsve;
                for (Ve ve : list) {
                    printPDF.inVe(ve);
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("In hóa đơn thành công");
                alert.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
