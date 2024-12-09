/*
 * @ (#) PrintPDF.java       1.0     16/10/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.Barcode;
import dao.*;
import entity.*;

import java.awt.*;
import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   16/10/2024
 * version: 1.0
 */

public class PrintPDF {
    public PrintPDF(){

    }

    public void inVe(ArrayList<Ve> dsve) throws IOException, DocumentException {

        // Tạo một document
        Document document = new Document(new Rectangle(250, 550));
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/pdf/" + "dsve-" + getData.hd.getMaHoaDon() + ".pdf"));
        document.addLanguage("vi");
        document.open();
        //Cho phép viết tiếng Việt không lỗi mất chữ có dấu
        BaseFont bf = BaseFont.createFont("src/main/resources/pdf/00182-UTM-Times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        // Tạo font
        Font fontTitle = new Font(bf, 12, Font.BOLD);
        Font fontContent = new Font(bf, 11, Font.NORMAL);
        Font fontContentB = new Font(bf, 11, Font.BOLD);
        //set cùng 1 size margin cho các page
        //cho nội dung căn giữa
        for (Ve ve : dsve) {
            Paragraph p = new Paragraph("CÔNG TY CỔ PHẦN VẬN TẢI ĐƯỜNG SẮT HHKT", fontTitle);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            Paragraph p1 = new Paragraph("VÉ TÀU", new Font(bf, 12, Font.NORMAL));
            p1.setAlignment(Element.ALIGN_CENTER);
            document.add(p1);
            //Xuống thêm 1 dòng
            Barcode barcode = new Barcode128();
            barcode.setCode(ve.getMaVe());
            Image barcodeImage = barcode.createAwtImage(Color.BLACK, Color.WHITE);
            com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(barcodeImage, null);
            img.setAlignment(Element.ALIGN_CENTER);
            document.add(img);
            Paragraph p2 = new Paragraph("Mã vé/TicketID: " + ve.getMaVe(), fontContent);
            p2.setAlignment(Element.ALIGN_CENTER);
            document.add(p2);
            //Tạo para có Ga Đi          Ga Đến
            Paragraph p3 = new Paragraph();
            p3.setAlignment(Element.ALIGN_CENTER);
            p3.setFont(fontContent);
            p3.add(new Chunk("Ga đi/From"));
            p3.add(new Chunk("          "));
            p3.add(new Chunk("Ga đến/To"));
            document.add(p3);
            //Tên ga
            LichTrinh lt = new LichTrinh_DAO().getLichTrinhTheoID(ve.getCtlt().getLichTrinh().getMaLichTrinh());
            String gaDi = new Ga_DAO().getGaTheoMaGa(lt.getGaDi().getMaGa()).getTenGa();
            String gaDen = new Ga_DAO().getGaTheoMaGa(lt.getGaDen().getMaGa()).getTenGa();
            Paragraph p4 = new Paragraph(gaDi.toUpperCase() + "           " + gaDen.toUpperCase(), fontTitle);
            p4.setAlignment(Element.ALIGN_CENTER);
            document.add(p4);

            document.add(new Paragraph(" "));
            //Tạo para có ngày giờ
            Paragraph p5 = new Paragraph("Tàu/Train: ", fontContent);
            p5.setAlignment(Element.ALIGN_JUSTIFIED);
            p5.add(new Chunk(lt.getChuyenTau().getSoHieutau(), fontContentB));
            //tạo margin bottom
            p5.setSpacingAfter(5);
            document.add(p5);
            //Tạo para có ngày giờ
            Paragraph p6 = new Paragraph("Ngày/Date: ", fontContent);
            p6.setAlignment(Element.ALIGN_LEFT);
            p6.add(new Chunk(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(lt.getThoiGianKhoiHanh()), fontContentB));
            p6.setSpacingAfter(5);
            document.add(p6);
            //Tạo para có ngày giờ
            Paragraph p7 = new Paragraph("Giờ/Time: ", fontContent);
            p7.setAlignment(Element.ALIGN_LEFT);
            p7.add(new Chunk(DateTimeFormatter.ofPattern("HH:mm").format(lt.getThoiGianKhoiHanh()), fontContentB));
            p7.setSpacingAfter(5);
            document.add(p7);
            //Tạo para có Toa
            ChoNgoi choNgoi = new ChoNgoi_DAO().getChoNgoiTheoMa(ve.getCtlt().getChoNgoi().getMaChoNgoi());
            Toa toa = new Toa_DAO().getToaTheoID(choNgoi.getToa().getMaToa());
            Paragraph p8 = new Paragraph("Toa/Coach: " + toa.getSoSTToa() +  "     Chỗ/Seat: " + choNgoi.getSttCho(), fontContent);
            p8.setAlignment(Element.ALIGN_LEFT);
            p8.setSpacingAfter(5);
            document.add(p8);
            //Tạo para có Loại chỗ
            LoaiToa loaiToa = new LoaiToa_DAO().getLoaiToaTheoMa(toa.getLoaiToa().getMaLoaiToa());
            Paragraph p9 = new Paragraph("Loại chỗ/Class: ", fontContent);
            p9.setAlignment(Element.ALIGN_LEFT);
            p9.setSpacingAfter(5);
            p9.add(new Chunk(loaiToa.getTenLoaiToa(),fontContentB));
            document.add(p9);
            //Tạo para có Loại vé
            LoaiVe loaiVe = new LoaiVe_DAO().getLoaiVeTheoMa(ve.getLoaiVe().getMaLoaiVe());
            Paragraph p10 = new Paragraph("Loại vé/Ticket: ", fontContent);
            p10.setAlignment(Element.ALIGN_LEFT);
            p10.setSpacingAfter(5);
            p10.add(new Chunk(loaiVe.getTenLoaiVe(), fontContentB));
            document.add(p10);
            //Tạo para có Họ tên
            Paragraph p11 = new Paragraph("Họ tên/Name: ", fontContent);
            p11.setAlignment(Element.ALIGN_LEFT);
            p11.setSpacingAfter(5);
            p11.add(new Chunk(ve.getTenHanhKhach(), fontContentB));
            document.add(p11);
            //Tạo para có Giấy tờ
            Paragraph p12 = new Paragraph("Giấy tờ/Passport: ", fontContent);
            p12.setAlignment(Element.ALIGN_LEFT);
            p12.setSpacingAfter(5);
            p12.add(new Chunk(ve.getSoCCCD(), fontContentB));
            document.add(p12);
            //Tạo para có Địa chỉ
            Paragraph p13 = new Paragraph("Giá/Price: ", fontContent);
            p13.setAlignment(Element.ALIGN_LEFT);
            p13.setSpacingAfter(5);
            p13.add(new Chunk(new DecimalFormat("#,### VNĐ").format(new CT_HoaDon_DAO().getCT_HoaDonTheoMaVe(ve.getMaVe()).getGiaVe()), fontContentB));

            document.add(p13);
            StringBuilder text = new StringBuilder();
            for (int i = 0; i < document.getPageSize().getWidth()/10; i++) {
                text.append("-");
            }
            Paragraph p14 = new Paragraph(text + "\nHotline: 1900 1000\n" +
                    "Website: www.vetau.com.vn", fontContent);
            p14.setAlignment(Element.ALIGN_CENTER);
            document.add(p14);
            Paragraph luuY = new Paragraph(text + "\nLưu ý: Hành khách khi đi tàu nhớ mang theo giấy tờ định danh để soát vé trước khi lên tàu", fontContent);
            luuY.setAlignment(Element.ALIGN_CENTER);
            document.add(luuY);
            if (dsve.indexOf(ve) != dsve.size() - 1) {
                document.newPage();
            }
        }

        try {
            document.close();
            Desktop.getDesktop().open(new File("src/main/resources/pdf/" + "dsve-" + getData.hd.getMaHoaDon() + ".pdf"));
        }catch (Exception e){
            System.out.println("Tạo vé thất bại!");
        }
    }

    public void inHoaDon(HoaDon hoaDon) throws IOException, DocumentException {
        String filename = hoaDon.getMaHoaDon() + ".pdf";

            // Create a new document
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/pdf/" + filename));
        BaseFont bf = BaseFont.createFont("src/main/resources/pdf/00182-UTM-Times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        // Open the document
        document.open();
        //Add logo
        com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance("src/main/resources/img/logo.png");
        img.setAlignment(Element.ALIGN_LEFT);
        img.scaleAbsolute(50, 50);
        document.add(img);
        //Add no invoice left side
        Paragraph noInvoice = new Paragraph("Số: " + hoaDon.getMaHoaDon(), new Font(bf, 10, Font.BOLD));
        noInvoice.setAlignment(Element.ALIGN_RIGHT);
        document.add(noInvoice);
        // Define fonts
        Font boldFont = new Font(bf, 10, Font.BOLD);
        Font regularFont = new Font(bf, 10, Font.NORMAL);
        Font smallFont = new Font(bf, 8, Font.NORMAL);
        Paragraph title = new Paragraph("HÓA ĐƠN GIÁ TRỊ GIA TĂNG", new Font(bf, 12, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        document.add(new Paragraph(" "));
        Paragraph date = new Paragraph("Ngày " + hoaDon.getNgayLapHoaDon().getDayOfMonth() + " tháng " + hoaDon.getNgayLapHoaDon().getMonthValue() + " năm " + hoaDon.getNgayLapHoaDon().getYear(), regularFont);
        date.setAlignment(Element.ALIGN_RIGHT);
        document.add(date);
        // Add company details
        Paragraph companyInfo = new Paragraph("Đơn vị bán hàng: CÔNG TY CỔ PHẦN VẬN TẢI ĐƯỜNG SẮT HHKT", boldFont);
        companyInfo.setAlignment(Element.ALIGN_LEFT);
        document.add(companyInfo);

        // Add company additional info (MST, address, phone, etc.)
        document.add(new Paragraph("MST: 0000000                       Điện thoại: 19000000", regularFont));
        document.add(new Paragraph("Địa chỉ: Số 12A Nguyễn Văn Bảo, Phường 4, Quận Gò Vấp, Thành phố Hồ Chí Minh, Việt Nam", regularFont));
        document.add(new Paragraph(" "));
        // Add buyer info
        document.add(new Paragraph("\nHọ tên người mua hàng: " + new KhachHang_DAO().getKhachHangTheoMaKH(hoaDon.getKhachHang().getMaKH()).getTenKH(), boldFont));
        document.add(new Paragraph("Hình thức thanh toán: Tiền mặt", regularFont));
        document.add(new Paragraph(" "));

        // Create table for ticket details
        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        //Căn giữa tiêu đề bảng
        table.setWidths(new int[]{5, 15, 25, 10, 5, 10, 10, 10, 10});

        // Add headers
        addTableHeader(table, boldFont, "STT", "Mã vé", "Tên dịch vụ", "ĐVT", "Số lượng", "Đơn giá", "Thành tiền chưa có thuế", "Thuế GTGT", "TT có thuế");
        ArrayList<ChiTietHoaDon> dscthd = new CT_HoaDon_DAO().getCT_HoaDon(hoaDon.getMaHoaDon());
        NumberFormat df = NumberFormat.getCurrencyInstance(Locale.of("vi", "VN"));;
        // Add ticket row 1
        int dem = 0;
        double tong = 0;
        for(ChiTietHoaDon cthd : dscthd){
            Ve ve = new Ve_DAO().getVeTheoID(cthd.getVe().getMaVe());
            LoaiVe loaiVe = new LoaiVe_DAO().getLoaiVeTheoMa(ve.getLoaiVe().getMaLoaiVe());
            LichTrinh lt = new LichTrinh_DAO().getLichTrinhTheoID(ve.getCtlt().getLichTrinh().getMaLichTrinh());
            ChiTietLichTrinh ctlt = new CT_LichTrinh_DAO().getCTLTTheoCN(ve.getCtlt().getLichTrinh().getMaLichTrinh(), ve.getCtlt().getChoNgoi().getMaChoNgoi());
            ChoNgoi choNgoi = new ChoNgoi_DAO().getChoNgoiTheoMa(ve.getCtlt().getChoNgoi().getMaChoNgoi());
            Toa toa = new Toa_DAO().getToaTheoID(choNgoi.getToa().getMaToa());
            addTableRow(table, regularFont, ++dem + "", cthd.getVe().getMaVe(),
                        "Vé HK: " + lt.getGaDi().getMaGa() + "-" + lt.getGaDen().getMaGa() +
                        "-" + lt.getChuyenTau().getSoHieutau() + "-" + dtf.format(lt.getThoiGianKhoiHanh()) +
                        "-" + choNgoi.getSttCho() + "-" + toa.getSoSTToa() + "-" + toa.getLoaiToa().getMaLoaiToa(),
                        "Vé", "1", df.format(ctlt.getGiaCho()), df.format(cthd.getGiaVe() - 2000), "10%",
                        df.format((cthd.getGiaVe() - 2000) * 1.1));


            tong += cthd.getGiaVe() - 2000;
        }
        // Add ticket row 2
        addTableRow(table, regularFont, dem + 1 + "", "", "Phí bảo hiểm hành khách", "Người", dem + "", "2.000", df.format(dem * 2000L), "KCT", df.format(dem * 2000L));

        if (hoaDon.getKhuyenMai().getMaKM() != null) {
            PdfPCell kmCell = new PdfPCell(new Phrase("Khuyến mãi: " + hoaDon.getKhuyenMai().getMoTa(), boldFont));
            kmCell.setColspan(6);
            kmCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(kmCell);
            table.addCell(new PdfPCell(new Phrase(df.format(tong * hoaDon.getKhuyenMai().getMucKM()), regularFont)));
            table.addCell(new PdfPCell(new Phrase("KCT", regularFont)));
            table.addCell(new PdfPCell(new Phrase(df.format(tong * hoaDon.getKhuyenMai().getMucKM()), regularFont)));
            tong = tong * (1 - hoaDon.getKhuyenMai().getMucKM());
        }
        PdfPCell totalCellPerTaxType = new PdfPCell(new Phrase("Tổng cộng: ", boldFont));
        totalCellPerTaxType.setColspan(6);
        totalCellPerTaxType.setRowspan(2);
        totalCellPerTaxType.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(totalCellPerTaxType);
        table.addCell(new PdfPCell(new Phrase(df.format(tong), regularFont)));
        table.addCell(new PdfPCell(new Phrase("10%", regularFont)));
        table.addCell(new PdfPCell(new Phrase(df.format(tong * 1.1), regularFont)));

        table.addCell(new PdfPCell(new Phrase(df.format(2000L * dem), regularFont)));
        table.addCell(new PdfPCell(new Phrase("KCT", regularFont)));
        table.addCell(new PdfPCell(new Phrase(df.format(2000L * dem), regularFont)));
        // Add total row
        PdfPCell totalCell = new PdfPCell(new Phrase("Tổng cộng theo từng loại thuế suất: ", boldFont));
        totalCell.setColspan(6);
        totalCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(totalCell);
        table.addCell(new PdfPCell(new Phrase(df.format(tong + dem * 2000), regularFont)));
        table.addCell(new PdfPCell(new Phrase(df.format(tong * 0.1), regularFont)));

        table.addCell(new PdfPCell(new Phrase(df.format(tong * 1.1 + dem * 2000), regularFont)));

        // Add table to document
        document.add(table);


        // Close the document
        try {
            document.close();
            Desktop.getDesktop().open(new File("src/main/resources/pdf/" + filename));
        }catch (Exception e){
            System.out.println("Tạo hóa đơn thất bại!");
        }

    }

    public void inKetCa(NhanVien nv, LocalDateTime gioBatDau, LocalDateTime gioKetThuc, double tienDauCa,
                        double tongTien, double tienBanVe, double tienTraVe, double tienTraVeDoi, double tienThuVeDoi, String ghiChu)
                        throws IOException, DocumentException {
    String filename = "KetCa_" + nv.getMaNhanVien() + "_" + gioKetThuc.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(Locale.of("vi", "VN"));


    // Create a new document
    Document document = new Document(PageSize.A4);
    PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/pdf/" + filename));
    BaseFont bf = BaseFont.createFont("src/main/resources/pdf/00182-UTM-Times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

    // Open the document
    document.open();

    // Add title
    Font titleFont = new Font(bf, 14, Font.BOLD);
    Paragraph title = new Paragraph("BÁO CÁO CA LÀM", titleFont);
    title.setAlignment(Element.ALIGN_CENTER);
    document.add(title);

    // Add shift information
    Font regularFont = new Font(bf, 12, Font.NORMAL);
    document.add(new Paragraph("BẮT ĐẦU CA", regularFont));
    document.add(new Paragraph("Nhân viên: " + nv.getTenNhanVien(), regularFont));
    document.add(new Paragraph("Mã nhân viên: " + nv.getMaNhanVien(), regularFont));
    document.add(new Paragraph("Giờ bắt đầu ca: " + gioBatDau.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), regularFont));
    document.add(new Paragraph("Tiền đầu ca: " + currencyVN.format(tienDauCa), regularFont));
    document.add(new Paragraph("Ghi chú đầu ca: " + getData.ghiChu, regularFont));

    // Add a line separator
    document.add(new Paragraph("------------------------------------------------------------"));

    // Add summary of sales
    document.add(new Paragraph("CUỐI CA", regularFont));
    document.add(new Paragraph("Giờ kết thúc ca: " + gioKetThuc.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), regularFont));
    document.add(new Paragraph("Tổng kết doanh thu:", regularFont));
    document.add(new Paragraph("Tổng tiền bán vé: " + currencyVN.format(tienBanVe), regularFont));
    document.add(new Paragraph("Tổng tiền trả vé: " + currencyVN.format(tienTraVe), regularFont));
    document.add(new Paragraph("Tổng tiền trả vé đổi: " + currencyVN.format(tienTraVeDoi), regularFont));
    document.add(new Paragraph("Tổng tiền thu vé đổi: " + currencyVN.format(tienThuVeDoi), regularFont));
    document.add(new Paragraph("Tổng tiền cuối ca: " + currencyVN.format(tongTien), regularFont));
    // add note
    document.add(new Paragraph("Ghi chú cuối ca: " + ghiChu, regularFont));
    // Close the document
    try {
        document.close();
        Desktop.getDesktop().open(new File("src/main/resources/pdf/" + filename));
    } catch (Exception e) {
        throw new RuntimeException("Tạo báo cáo kết ca thất bại!");
    }
}

    public void inHDHuy(HoaDon hoaDon) throws IOException, DocumentException {
        String filename = hoaDon.getMaHoaDon() + ".pdf";

        // Create a new document
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/pdf/" + filename));
        BaseFont bf = BaseFont.createFont("src/main/resources/pdf/00182-UTM-Times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        // Open the document
        document.open();
        //Add logo
        com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance("src/main/resources/img/logo.png");
        img.setAlignment(Element.ALIGN_LEFT);
        img.scaleAbsolute(50, 50);
        document.add(img);
        //Add no invoice left side
        Paragraph noInvoice = new Paragraph("Số: " + hoaDon.getMaHoaDon(), new Font(bf, 10, Font.BOLD));
        noInvoice.setAlignment(Element.ALIGN_RIGHT);
        document.add(noInvoice);
        // Define fonts
        Font boldFont = new Font(bf, 10, Font.BOLD);
        Font regularFont = new Font(bf, 10, Font.NORMAL);
        Font smallFont = new Font(bf, 8, Font.NORMAL);
        Paragraph title = new Paragraph("HÓA ĐƠN GIÁ TRỊ GIA TĂNG", new Font(bf, 12, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        document.add(new Paragraph(" "));
        Paragraph date = new Paragraph("Ngày " + hoaDon.getNgayLapHoaDon().getDayOfMonth() + " tháng " + hoaDon.getNgayLapHoaDon().getMonthValue() + " năm " + hoaDon.getNgayLapHoaDon().getYear(), regularFont);
        date.setAlignment(Element.ALIGN_RIGHT);
        document.add(date);
        // Add company details
        Paragraph companyInfo = new Paragraph("Đơn vị bán hàng: CÔNG TY CỔ PHẦN VẬN TẢI ĐƯỜNG SẮT HHKT", boldFont);
        companyInfo.setAlignment(Element.ALIGN_LEFT);
        document.add(companyInfo);

        // Add company additional info (MST, address, phone, etc.)
        document.add(new Paragraph("MST: 0000000                       Điện thoại: 19000000", regularFont));
        document.add(new Paragraph("Địa chỉ: Số 12A Nguyễn Văn Bảo, Phường 4, Quận Gò Vấp, Thành phố Hồ Chí Minh, Việt Nam", regularFont));
        document.add(new Paragraph(" "));
        // Add buyer info
        KhachHang kh = new KhachHang_DAO().getKhachHangTheoMaKH(hoaDon.getKhachHang().getMaKH());

        document.add(new Paragraph("\nHọ tên người hủy vé: " + kh.getTenKH(), boldFont));
        document.add(new Paragraph("Số điện thoại: " + kh.getSoCCCD(), regularFont));
        document.add(new Paragraph("Số CCCD/CMND: " + kh.getSdt(), regularFont));
        document.add(new Paragraph("Thời gian hủy vé: " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now()), regularFont));

        // Staff info
        NhanVien nv = getData.nv;
        document.add(new Paragraph("\nNhân viên lập hóa đơn: " + nv.getTenNhanVien(), boldFont));

        document.add(new Paragraph(" "));
        document.add(new Paragraph("DANH SÁCH VÉ HỦY", boldFont));
        document.add(new Paragraph(" "));


        // Create table for ticket details
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        //Căn giữa tiêu đề bảng
        table.setWidths(new int[]{5, 15, 20, 30, 10, 10, 10});

        // Add headers
        addTableHeader(table, boldFont, "STT", "Mã vé", "Tên hành khách", "Thông tin vé", "Tiền vé(VNĐ)", "Lệ phí trả vé", "Tiền trả");
        // Add ticket row 1
        ArrayList<ChiTietHoaDon> dscthd = new CT_HoaDon_DAO().getCT_HoaDon(hoaDon.getMaHoaDon());
        int count = 0;
        double tongTienVe = 0;
        double tongLePhi = 0;
        double tongTienTra = 0;

        for (ChiTietHoaDon cthd : dscthd) {
            Ve ve = new Ve_DAO().getVeTheoID(cthd.getVe().getMaVe());
            LoaiVe loaiVe = new LoaiVe_DAO().getLoaiVeTheoMa(ve.getLoaiVe().getMaLoaiVe());
            LichTrinh lt = new LichTrinh_DAO().getLichTrinhTheoID(ve.getCtlt().getLichTrinh().getMaLichTrinh());
            ChiTietLichTrinh ctlt = new CT_LichTrinh_DAO().getCTLTTheoCN(ve.getCtlt().getLichTrinh().getMaLichTrinh(), ve.getCtlt().getChoNgoi().getMaChoNgoi());
            ChoNgoi choNgoi = new ChoNgoi_DAO().getChoNgoiTheoMa(ve.getCtlt().getChoNgoi().getMaChoNgoi());
            Ga gaDi = new Ga_DAO().getGaTheoMaGa(lt.getGaDi().getMaGa());
            Ga gaDen = new Ga_DAO().getGaTheoMaGa(lt.getGaDen().getMaGa());

            double tienVe = Math.round(cthd.getGiaVe() * -1);
            double lePhi = Math.round(cthd.getGiaGiam());
            double tienTra = Math.round(cthd.getGiaVe() * -1 - cthd.getGiaGiam());


            addTableRow(table, regularFont, ++count + "", cthd.getVe().getMaVe(), kh.getTenKH(),
                        gaDi.getTenGa() + "-" + gaDen.getTenGa() + "\nGiờ khởi hành: " + dtf.format(lt.getThoiGianKhoiHanh()) +
                        "\nLoại vé: " + loaiVe.getTenLoaiVe(), new DecimalFormat("#,###").format(tienVe),
                        new DecimalFormat("#,###").format(lePhi),
                        new DecimalFormat("#,###").format(tienTra));

            tongTienVe += tienVe;
            tongLePhi += lePhi;
            tongTienTra -= tienTra;
        }

        // Add table to document
        document.add(table);

        // Add total row

        document.add(new Paragraph("Tổng số vé: " + count + "vé", regularFont));
        document.add(new Paragraph("\nTổng tiền vé: " + new DecimalFormat("#,###").format(tongTienVe) + "VNĐ", regularFont));
        document.add(new Paragraph("\nTổng lệ phí: " + new DecimalFormat("#,###").format(tongLePhi) + "VNĐ", regularFont));
        document.add(new Paragraph("\nTổng tiền trả: " + new DecimalFormat("#,###").format(-1 * tongTienTra) + "VNĐ", regularFont));

        // Close the document
        try {
            document.close();
            Desktop.getDesktop().open(new File("src/main/resources/pdf/" + filename));
        } catch (Exception e) {
            System.out.println("Tạo hóa đơn thất bại!");
        }

        getData.hd = null;
        getData.dsve = null;
        getData.mapLePhi = null;

    }

    private static void addTableHeader(PdfPTable table, Font font, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
    }

    // Function to add rows to table
    private static void addTableRow(PdfPTable table, Font font, String... columns) {
        for (String column : columns) {
            PdfPCell cell = new PdfPCell(new Phrase(column, font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

}
