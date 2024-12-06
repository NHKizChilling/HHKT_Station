package controller;

import dao.*;
import entity.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ThongKeNVController implements Initializable {

    @FXML
    private ComboBox<String> cb_thongKe;

    @FXML
    private ComboBox<String> cb_thang;

    @FXML
    private ComboBox<String> cb_nam;

    @FXML
    private Button btn_search;

    @FXML
    private Label lbl_soNv;

    @FXML
    private Label lbl_taoBaoCaoNV;

    @FXML
    private BarChart<String, Number> barChart_top5nv;

    @FXML
    private Label lbl_soChuyenTau;

    @FXML
    private Label lbl_taoBaoCaoChuyenTau;

    @FXML
    private ScatterChart<?, ?> scatter_chuyenTau;

    private NhanVien_DAO nhanVien_dao;
    private HoaDon_DAO hoaDon_dao;
    private CT_HoaDon_DAO ct_hoaDon_dao;
    private Ve_DAO ve_dao;
    private LichTrinh_DAO lichTrinh_dao;
    private CT_LichTrinh_DAO ct_lichTrinh_dao;
    private Ga_DAO ga_dao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDate currentDate = LocalDate.now();
        initDAO();
        initComponent(currentDate);

        cb_thongKe.setOnAction(event -> {
            if (cb_thongKe.getValue().equals("Tháng")) {
                cb_thang.setDisable(false);
                cb_nam.setDisable(false);
            } else {
                cb_thang.setDisable(true);
                cb_nam.setDisable(false);
            }
        });

        btn_search.setOnAction(event -> {
            if (cb_thongKe.getValue().equals("Tháng")) {
                LocalDate date = LocalDate.of(Integer.parseInt(cb_nam.getValue()), Integer.parseInt(cb_thang.getValue()), 1);
                renderNhanVien(date, 1);
                renderChuyenTau();
            } else {
                LocalDate date = LocalDate.of(Integer.parseInt(cb_nam.getValue()), 1, 1);
                renderNhanVien(date, 2);
                renderChuyenTau();
            }
        });

        lbl_taoBaoCaoNV.setOnMouseClicked(event -> {
            JOptionPane.showMessageDialog(null, "Tạo báo cáo nhân viên thành công");
        });

        lbl_taoBaoCaoChuyenTau.setOnMouseClicked(event -> {
            JOptionPane.showMessageDialog(null, "Tạo báo cáo chuyến tàu thành công");
        });
    }

    private void initComponent(LocalDate currentDate) {
        initComboBox(currentDate);
        renderNhanVien(currentDate, 1);
        renderChuyenTau();
    }

    private void initDAO() {
        nhanVien_dao = new NhanVien_DAO();
        hoaDon_dao = new HoaDon_DAO();
        ve_dao = new Ve_DAO();
        lichTrinh_dao = new LichTrinh_DAO();
        ct_lichTrinh_dao = new CT_LichTrinh_DAO();
        ct_hoaDon_dao = new CT_HoaDon_DAO();
        ga_dao = new Ga_DAO();
    }

    private void initComboBox(LocalDate date) {
        cb_thongKe.getItems().addAll("Tháng", "Năm");
        cb_thang.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12");
        cb_nam.getItems().addAll("2020", "2021", "2022", "2023", "2024");

        cb_thongKe.setValue("Tháng");
        cb_thang.setValue(String.valueOf(date.getMonthValue()));
        cb_nam.setValue(String.valueOf(date.getYear()));

        //Tự động thêm năm vào combobox
        int currentYear = LocalDate.now().getYear();
        int lastYear = Integer.parseInt(cb_nam.getItems().getLast());
        for (int i = lastYear + 1; i <= currentYear; i++) {
            cb_nam.getItems().add(String.valueOf(i));
        }
    }

    private void renderNhanVien(LocalDate date, int type) { //type: 1: thống kê theo tháng, 2: thống kê theo năm
        ArrayList<NhanVien> listNhanVien = nhanVien_dao.getDSNhanVien();
        int soNv = listNhanVien.size();
        lbl_soNv.setText(soNv + " nhân viên");

        ArrayList<HoaDon> listHoaDon;
        if (type == 1) { //1: thống kê theo tháng
            listHoaDon = hoaDon_dao.getDSHDTheoThang(date.getMonthValue(), date.getYear());
        } else { //2: thống kê theo năm
            listHoaDon = hoaDon_dao.getDSHDTheoNam(String.valueOf(date.getYear()));
        }

        // Lưu Tên nhân viên và doanh thu vào HashMap
        HashMap<String, Double> mapNV = new HashMap<>();
        for (HoaDon hoaDon : listHoaDon) {
            String tenNhanVien = hoaDon.getNhanVien().getTenNhanVien();
            double doanhThu = hoaDon.getTongTien();
            mapNV.put(tenNhanVien, mapNV.getOrDefault(tenNhanVien, 0.0) + doanhThu);
        }

        // Sắp xếp theo doanh thu giảm dần bằng biểu thức lambda
        mapNV.entrySet().stream().sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()));

        // Hiển thị top 5 nhân viên lên biểu đồ BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        mapNV.entrySet().stream()
                .sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
                .limit(5)
                .forEach(entry -> series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue())));

        barChart_top5nv.getData().clear();
        barChart_top5nv.getData().add(series);
        barChart_top5nv.setTitle("Top 5 nhân viên có doanh thu cao nhất");
        barChart_top5nv.getXAxis().setLabel("Nhân viên");
        barChart_top5nv.getYAxis().setLabel("Doanh thu");
    }

    private void renderChuyenTau() { //type: 1: thống kê theo tháng, 2: thống kê theo năm
        ArrayList<LichTrinh> dsLichTrinh = lichTrinh_dao.getDSLichTrinhTheoTrangThai(true);
        lbl_soChuyenTau.setText(dsLichTrinh.size() + " chuyến tàu");

        //Lưu những điểm đến và số lần đi qua vào HashMap
        HashMap<String, Integer> mapChuyenTau = new HashMap<>();
        for (LichTrinh lichTrinh : dsLichTrinh) {
            ArrayList<ChiTietLichTrinh> dsCTLT = ct_lichTrinh_dao.getCtltTheoMaLichTrinh(lichTrinh.getMaLichTrinh());
            String maLichTrinh = lichTrinh.getMaLichTrinh();
            String maGaDen = lichTrinh.getGaDen().getMaGa();
            String tenGaDen = ga_dao.getGaTheoMaGa(maGaDen).getTenGa();
            // thêm tên chuyến tàu vào map với value là 0
            mapChuyenTau.put(tenGaDen, 0);
            for (ChiTietLichTrinh ctlt : dsCTLT) {
                if (ctlt.isTrangThai()) {
                    mapChuyenTau.put(tenGaDen, mapChuyenTau.get(tenGaDen) + 1);
                }
            }
        }

        // Hiển thị số lần đi qua của các chuyến tàu lên biểu đồ ScatterChart
        XYChart.Series series = new XYChart.Series();
        mapChuyenTau.forEach((k, v) -> series.getData().add(new XYChart.Data(k, v)));

        scatter_chuyenTau.getData().clear();
        scatter_chuyenTau.getData().add(series);
        scatter_chuyenTau.setTitle("Xu hướng điểm đến của hành khách");
        scatter_chuyenTau.getXAxis().setLabel("Điểm đến");
        scatter_chuyenTau.getYAxis().setLabel("Số lần tới");
    }
}
