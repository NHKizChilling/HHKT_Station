/*
 * @ (#) GioiThieuController.java       1.0     20/10/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import dao.CT_HoaDon_DAO;
import dao.HoaDon_DAO;
import dao.LichTrinh_DAO;
import dao.Ve_DAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.LichTrinh;
import entity.Ve;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   20/10/2024
 * version: 1.0
 */
public class GioiThieuController implements Initializable {
    @FXML
    private BarChart<String, Integer> chartChuyenTau;

    @FXML
    private Label lblDoanhThu;

    @FXML
    private Label lblSLChuyenTau;

    @FXML
    private Label lblSLVe;

    private final LichTrinh_DAO lt_dao = new LichTrinh_DAO();
    private final Ve_DAO ve_dao = new Ve_DAO();
    private final HoaDon_DAO hd_dao = new HoaDon_DAO();
    private final CT_HoaDon_DAO cthd = new CT_HoaDon_DAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ArrayList<LichTrinh> list = lt_dao.traCuuDSLichTrinhTheoNgay(LocalDate.now());
        list.removeIf(lt -> !lt.getThoiGianKhoiHanh().isAfter(LocalDateTime.now()));
        ObservableList<XYChart.Data<String, Integer>> data = FXCollections.observableArrayList();
        Set<String> set = new HashSet<>();
        int dem = 0;
        for (LichTrinh lt : list) {
            if (!lt.isTinhTrang()) {
                continue;
            }
            if (set.contains(lt.getChuyenTau().getSoHieutau())) {
                continue;
            }
            data.add(new XYChart.Data<>(lt.getChuyenTau().getSoHieutau(), lt_dao.getSoLuongChoConTrong(lt.getMaLichTrinh())));
            set.add(lt.getChuyenTau().getSoHieutau());
            dem++;
        }
        ArrayList<HoaDon> dshd = hd_dao.getHoaDonTheoNV(getData.nv.getMaNhanVien(), LocalDateTime.now());
        int slVe = 0;
        double doanhThu = 0;
        for (HoaDon hd : dshd) {
            if (!hd.isTrangThai()) {
                continue;
            }
            ArrayList<ChiTietHoaDon> dscthd = cthd.getCT_HoaDon(hd.getMaHoaDon());
            for (ChiTietHoaDon ct : dscthd) {
                Ve ve = ve_dao.getVeTheoID(ct.getVe().getMaVe());
                if (ve.getTinhTrangVe().equals("DaBan")) {
                    slVe++;
                }
            }
            doanhThu += hd.getTongTien();
        }
        lblSLVe.setText(slVe + "");
        lblDoanhThu.setText(DecimalFormat.getCurrencyInstance(Locale.of("vi", "VN")).format(doanhThu/1000 * 1000));
        lblSLChuyenTau.setText(dem + "");
        chartChuyenTau.getData().add(new XYChart.Series<>("Số lượng chỗ ngồi còn trống", data));
    }
}
