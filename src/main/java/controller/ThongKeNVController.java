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
    private BarChart<String, Number> barChart_chuyenTau;

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

        // Tự động thêm năm vào combobox
        int currentYear = LocalDate.now().getYear();
        String lastYearStr = cb_nam.getItems().isEmpty() ? "0" : cb_nam.getItems().get(cb_nam.getItems().size() - 1);
        int lastYear = Integer.parseInt(lastYearStr);
        for (int i = lastYear + 1; i <= currentYear; i++) {
            cb_nam.getItems().add(String.valueOf(i));
        }
    }

    private void renderNhanVien(LocalDate date, int type) { //type: 1: thống kê theo tháng, 2: thống kê theo năm
        ArrayList<NhanVien> listNhanVien = nhanVien_dao.getDSNhanVien();
        lbl_soNv.setText(listNhanVien.size() + " nhân viên");

        ArrayList<HoaDon> listHoaDon = (type == 1)
                ? hoaDon_dao.getDSHDTheoThang(date.getMonthValue(), date.getYear())
                : hoaDon_dao.getDSHDTheoNam(String.valueOf(date.getYear()));

        // nếu ds hóa đơn rỗng null thì return
        if (listHoaDon == null || listHoaDon.isEmpty()) {
            return;
        }

        HashMap<String, Double> mapNV = new HashMap<>();
        for (HoaDon hoaDon : listHoaDon) {
            String tenNhanVien = hoaDon.getNhanVien().getTenNhanVien();
            double doanhThu = hoaDon.getTongTien();
            mapNV.put(tenNhanVien, mapNV.getOrDefault(tenNhanVien, 0.0) + doanhThu);
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        mapNV.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
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
        ArrayList<LichTrinh> listLichTrinh = lichTrinh_dao.getDSLichTrinhTheoTrangThai(true);
        int soChuyenTau = listLichTrinh.size();
        lbl_soChuyenTau.setText(soChuyenTau + " chuyến tàu");


        ArrayList<Ve> dsVe = new ArrayList<>(ve_dao.getVeTheoTinhTrang("DaBan"));
        dsVe.addAll(ve_dao.getVeTheoTinhTrang("DaDoi"));

        HashMap<String, Integer> mapChuyenTau = new HashMap<>();

        for (Ve ve : dsVe) {
            String maLichTrinh = ve.getCtlt().getLichTrinh().getMaLichTrinh();
            LichTrinh lichTrinh = lichTrinh_dao.getLichTrinhTheoID(maLichTrinh);
            Ga diemDen = ga_dao.getGaTheoMaGa(lichTrinh.getGaDen().getMaGa());
            String tenGaDen = diemDen.getTenGa();
            mapChuyenTau.put(tenGaDen, mapChuyenTau.getOrDefault(tenGaDen, 0) + 1);
        }

        // Hiển thị số chuyến tàu lên biểu đồ BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        mapChuyenTau.forEach((key, value) -> {
            if (key != null && value != null) {
                series.getData().add(new XYChart.Data<>(key, value));
            }
        });

        barChart_chuyenTau.getData().clear();
        barChart_chuyenTau.getData().add(series);
        barChart_chuyenTau.setTitle("Số chuyến tàu đến các ga");
        barChart_chuyenTau.getXAxis().setLabel("Điểm đến");
        barChart_chuyenTau.getYAxis().setLabel("Xu hướng mua vé");
    }
}
