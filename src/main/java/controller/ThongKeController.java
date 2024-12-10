/*
 * @ (#) ThongKeController.java       1.0     01/10/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import dao.CT_HoaDon_DAO;
import dao.CT_LichTrinh_DAO;
import dao.HoaDon_DAO;
import dao.Ve_DAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.Ve;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

import javax.swing.text.Style;


/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   01/10/2024
 * version: 1.0
 */
public class ThongKeController implements Initializable {

    @FXML
    private ComboBox<String> cb_thongKe;

    @FXML
    private ComboBox<String> cb_ngay;

    @FXML
    private ComboBox<String> cb_thang;

    @FXML
    private ComboBox<String> cb_nam;

    @FXML
    private Button btn_search;

    @FXML
    private ComboBox<String> cb_sumNam;

    @FXML
    private Label lbl_soVeBan;

    @FXML
    private Label lbl_soVeTra;

    @FXML
    private Label lbl_soVeDoi;

    @FXML
    private Label lbl_doanhThu;

    @FXML
    private Label lbl_tongDoanhThu;

    @FXML
    private Label lbl_tongVeBan;

    @FXML
    private Label lbl_taoBaoCao;

    @FXML
    private BarChart<String, Number> barChart_doanhThu;

    HoaDon_DAO hoaDon_dao;
    CT_LichTrinh_DAO ctLichTrinh_dao;
    Ve_DAO ve_dao;
    CT_HoaDon_DAO ctHoaDon_dao;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        initDAO();
        initComponents();

        cb_thongKe.setOnAction(e -> {
            String selected = cb_thongKe.getValue();
            LocalDateTime now = LocalDateTime.now();
            if (selected != null) {
                switch (selected) {
                    case "Ngày" -> {
                        cb_ngay.setDisable(false);
                        cb_thang.setDisable(false);

                        cb_ngay.setValue(String.valueOf(now.getDayOfMonth()));
                        cb_thang.setValue(String.valueOf(now.getMonthValue()));
                        cb_nam.setValue(String.valueOf(now.getYear()));

                        renderInfo(now, 1);
                    }
                    case "Tháng" -> {
                        cb_ngay.setDisable(true);
                        cb_thang.setDisable(false);

                        cb_thang.setValue(String.valueOf(now.getMonthValue()));
                        cb_nam.setValue(String.valueOf(now.getYear()));

                        renderInfo(now, 2);
                    }
                    case "Năm" -> {
                        cb_ngay.setDisable(true);
                        cb_thang.setDisable(true);

                        cb_nam.setValue(String.valueOf(now.getYear()));

                        renderInfo(now, 3);
                    }
                }
            }
        });

        cb_thang.setOnAction(e -> {
            String month = cb_thang.getValue();
            String year = cb_nam.getValue();
            if (year == null) {
                year = "2021";
            }

            if (month != null) {
                cb_ngay.getItems().clear();
                int daysInMonth = switch (month) {
                    case "4", "6", "9", "11" -> 30;
                    case "2" -> isNamNhuan(Integer.parseInt(year)) ? 29 : 28;
                    default -> 31;
                };
                for (int i = 1; i <= daysInMonth; i++) {
                    cb_ngay.getItems().add(String.valueOf(i));
                }
            }
        });

        btn_search.setOnAction(e -> {
            String selected = cb_thongKe.getValue();
            if (selected != null) {
                switch (selected) {
                    case "Ngày" -> {
                        int day = Integer.parseInt(cb_ngay.getValue());
                        int month = Integer.parseInt(cb_thang.getValue());
                        int year = Integer.parseInt(cb_nam.getValue());
                        renderInfo(LocalDateTime.of(year, month, day, 0, 0), 1);
                    }
                    case "Tháng" -> {
                        int month = Integer.parseInt(cb_thang.getValue());
                        int year = Integer.parseInt(cb_nam.getValue());
                        renderInfo(LocalDateTime.of(year, month, 1, 0, 0), 2);
                    }
                    case "Năm" -> {
                        int year = Integer.parseInt(cb_nam.getValue());
                        renderInfo(LocalDateTime.of(year, 1, 1, 0, 0), 3);
                    }
                }
            }
        });

        cb_sumNam.setOnAction(e -> {
            String year = cb_sumNam.getValue();
            if (year != null) {
                renderChart(year);
            }
        });

        lbl_taoBaoCao.setOnMouseClicked(e -> {
            // TODO: Tạo báo cáo
            ghiFileExcel();
        });
    }

    private void initComponents() {
        LocalDate now = LocalDate.now();
        initComboBox(now);
        renderInfo(LocalDateTime.now(), 1); // Mặc định hiển thị thông kê theo ngày
        renderChart(String.valueOf(now.getYear()));
    }

    private void initDAO() {
        hoaDon_dao = new HoaDon_DAO();
        ctLichTrinh_dao = new CT_LichTrinh_DAO();
        ve_dao = new Ve_DAO();
        ctHoaDon_dao = new CT_HoaDon_DAO();
    }

    private void initComboBox(LocalDate date) {
        cb_thongKe.getItems().addAll("Ngày", "Tháng", "Năm");
        cb_ngay.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31");
        cb_thang.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");

        cb_nam.getItems().addAll("2020", "2021", "2022", "2023", "2024");
        cb_sumNam.getItems().addAll("2020", "2021", "2022", "2023", "2024");

        // Lấy ngày hôm nay để set mặc định cho combobox
        cb_ngay.setValue(String.valueOf(date.getDayOfMonth()));
        cb_thang.setValue(String.valueOf(date.getMonthValue()));
        cb_nam.setValue(String.valueOf(date.getYear()));
        cb_sumNam.setValue(String.valueOf(date.getYear()));

        //Tự động thêm năm vào combobox
        int currentYear = LocalDate.now().getYear();
        int lastYear = Integer.parseInt(cb_nam.getItems().get(cb_nam.getItems().size() - 1));
        for (int i = lastYear + 1; i <= currentYear; i++) {
            cb_nam.getItems().add(String.valueOf(i));
            cb_sumNam.getItems().add(String.valueOf(i));
        }
    }

    // input là ngày/tháng/năm, type = 1: ngày, 2: tháng, 3: năm để xác định loại thống kê
    private void renderInfo(LocalDateTime date, int type) {
        int soVeBan = 0; // Số vé đã bán
        int soVeTra = 0; // Số vé đã trả
        int soVeDoi = 0; // Số vé đã đổi

        double doanhThu = 0; // Doanh thu
        ArrayList<HoaDon> listHoaDon = new ArrayList<>();

        switch (type) {
            // nếu type = 1 thì lấy danh sách hóa đơn theo ngày
            case 1 -> listHoaDon = hoaDon_dao.getDSHDTheoNgay(date);
            // nếu type = 2 thì lấy danh sách hóa đơn theo tháng
            case 2 -> listHoaDon = hoaDon_dao.getDSHDTheoThang(date.getMonthValue(), date.getYear());
            // nếu type = 3 thì lấy danh sách hóa đơn theo năm
            case 3 -> listHoaDon = hoaDon_dao.getDSHDTheoNam(String.valueOf(date.getYear()));
        }


        // Lặp qua từng hóa đơn để lấy chi tiết hóa đơn
        for (HoaDon hoaDon : listHoaDon) {
            // Lấy chi tiết hóa đơn theo mã hóa đơn
            for (ChiTietHoaDon cthd : ctHoaDon_dao.getCT_HoaDon(hoaDon.getMaHoaDon())) {
                // Lấy vé theo mã vé
                Ve ve = ve_dao.getVeTheoID(cthd.getVe().getMaVe());
                // Lấy tình trạng vé và cập nhật số vé bán, trả, đổi
                switch (ve.getTinhTrangVe()) {
                    case "DaBan" -> soVeBan++;
                    case "DaDoi" -> soVeDoi++;
                    case "DaHuy" -> soVeTra++;
                }
            }
            // Tính doanh thu
            doanhThu += hoaDon.getTongTien();
        }

        lbl_soVeBan.setText(soVeBan + " Vé");
        lbl_soVeTra.setText(soVeTra + " Vé");
        lbl_soVeDoi.setText(soVeDoi + " Vé");

        // format tiền
        lbl_doanhThu.setText(String.format("%,.0f", doanhThu) + " VNĐ");
    }

    private void renderChart(String year) {
        double tongDoanhThu = 0;
        int tongVeBan = 0;

        // Lấy danh sách hóa đơn theo năm
        ArrayList<HoaDon> listHoaDon = hoaDon_dao.getDSHDTheoNam(year);
        // Lặp qua từng hóa đơn để lấy chi tiết hóa đơn
        for (HoaDon hoaDon : listHoaDon) {
            // Lấy chi tiết hóa đơn theo mã hóa đơn
            String maHoaDon = hoaDon.getMaHoaDon();
            for (ChiTietHoaDon cthd : ctHoaDon_dao.getCT_HoaDon(maHoaDon)) {
                // Tính tổng vé bán
                Ve ve = ve_dao.getVeTheoID(cthd.getVe().getMaVe());
                if (ve.getTinhTrangVe().equals("DaBan") || ve.getTinhTrangVe().equals("DaDoi")) {
                    tongVeBan++;
                }
            }
            // Tính tổng doanh thu
            tongDoanhThu += hoaDon.getTongTien();
        }

        lbl_tongDoanhThu.setText(String.format("%,.0f", tongDoanhThu) + " VNĐ");
        lbl_tongVeBan.setText(tongVeBan + " Vé");

        // tạo BarChart với cột X là tháng, cột Y là doanh thu
        XYChart.Series<String, Number> set = new XYChart.Series<>();
        int months = 12;
        if(year.equals(String.valueOf(LocalDate.now().getYear()))){
            months = LocalDate.now().getMonthValue();
        }
        for (int i = 1; i <= months; i++) {
            double doanhThuThang = 0;
            for (HoaDon hoaDon : listHoaDon) {
                if (hoaDon.getNgayLapHoaDon().getMonthValue() == i) {
                    doanhThuThang += hoaDon.getTongTien();
                }
            }
            set.getData().add(new XYChart.Data<>(String.valueOf(i), doanhThuThang));
        }
        barChart_doanhThu.getData().addAll(set);
        // set tên cột
        barChart_doanhThu.getXAxis().setLabel("Tháng");
        barChart_doanhThu.getYAxis().setLabel("Doanh thu");
        barChart_doanhThu.setTitle("Biểu đồ doanh thu theo tháng");
    }

    private boolean isNamNhuan(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private void ghiFileExcel() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Thống kê doanh thu");

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);

        CellStyle style1 = workbook.createCellStyle();
        style1.setBorderRight(BorderStyle.THIN);
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setBorderBottom(BorderStyle.THIN);
        style1.setBorderTop(BorderStyle.THIN);

        CellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
//
//        CellStyle style3 = workbook.createCellStyle();
//        style3.setBorderRight(BorderStyle.THIN);
//        style3.setBorderLeft(BorderStyle.THIN);
//        style3.setBorderTop(BorderStyle.THIN);
//
//        CellStyle style4 = workbook.createCellStyle();
//        style4.setBorderRight(BorderStyle.THIN);
//        style4.setBorderLeft(BorderStyle.THIN);
//        style4.setBorderBottom(BorderStyle.THIN);
//
//        CellStyle style5 = workbook.createCellStyle();
//        style5.setBorderRight(BorderStyle.THIN);
//        style5.setBorderLeft(BorderStyle.THIN);
//        style5.setBorderBottom(BorderStyle.THIN);
//        style5.setBorderTop(BorderStyle.THIN);
// Chỉnh độ rộng cột
        sheet.setColumnWidth(0, 2000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);

// Tạo header row
        String[] headers = {"STT", "Tháng", "Doanh thu"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        Row rowTongCong = sheet.createRow(1);
        Cell cellTongCong = rowTongCong.createCell(0);
        cellTongCong.setCellValue("Tổng cộng");
        cellTongCong.setCellStyle(style2);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));

        ArrayList<HoaDon> listHoaDon = hoaDon_dao.getDSHDTheoNam(cb_sumNam.getValue());
        double tongDoanhThu = 0;
        int currentRowIndex = 2;

        for (int i = 0; i < listHoaDon.size(); i++) {
            HoaDon hoaDon = listHoaDon.get(i);

            boolean isNewMonth = (i == 0 || hoaDon.getNgayLapHoaDon().getMonthValue() != listHoaDon.get(i - 1).getNgayLapHoaDon().getMonthValue());

            if (isNewMonth) {
                Row row = sheet.createRow(currentRowIndex);

                Cell cellSTT = row.createCell(0);
                cellSTT.setCellValue(currentRowIndex - 1);
                cellSTT.setCellStyle(style1);

                Cell cellThang = row.createCell(1);
                cellThang.setCellValue(hoaDon.getNgayLapHoaDon().getMonthValue());
                cellThang.setCellStyle(style1);

                tongDoanhThu = 0;

                for (HoaDon hd : listHoaDon) {
                    if (hd.getNgayLapHoaDon().getMonthValue() == hoaDon.getNgayLapHoaDon().getMonthValue()) {
                        tongDoanhThu += hd.getTongTien();
                    }
                }

                Cell cellDoanhThu = row.createCell(2);
                cellDoanhThu.setCellValue(tongDoanhThu);
                cellDoanhThu.setCellStyle(style1);

                currentRowIndex++;
            }
        }

        // Tính tổng cộng
        double sum = 0;
        for (HoaDon hd : listHoaDon) {
            sum += hd.getTongTien();
        }

        Cell cellTongCongValue = rowTongCong.createCell(2);
        cellTongCongValue.setCellValue(sum);
        cellTongCongValue.setCellStyle(style2);

        try (FileOutputStream fileOut = new FileOutputStream("ThongKeDoanhThu.xlsx")) {
            workbook.write(fileOut);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}