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
import entity.Ve;

import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   16/10/2024
 * version: 1.0
 */

public class PrintPDF {
    private Ve ve;
    public PrintPDF(Ve ve) throws IOException, DocumentException {
    this.ve = ve;
        // Thay thế các giá trị mẫu bằng dữ liệu thực tế
        String maVe = "118074543";
        String gaDi = "SÀI GÒN";
        String gaDen = "TUY HOÀ";
        // ...

        // Tạo một document
        Document document = new Document(new Rectangle(250, 450));
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/pdf/ve.pdf"));
        document.addLanguage("vi");
        document.open();
        //Cho phép viết tiếng Việt không lỗi mất chữ có dấu
        BaseFont bf = BaseFont.createFont("src/main/resources/pdf/00182-UTM-Times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        // Tạo font
        Font fontTitle = new Font(bf, 12, Font.BOLD);
        Font fontContent = new Font(bf, 11);
        document.setMargins(10, 10, 10, 10);
        //cho nội dung căn giữa
        Paragraph p = new Paragraph("CÔNG TY CỔ PHẦN VẬN TẢI ĐƯỜNG SẮT SÀI GÒN", fontTitle);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        Paragraph p1 = new Paragraph("VÉ TÀU", new Font(bf, 12, Font.NORMAL));
        p1.setAlignment(Element.ALIGN_CENTER);
        document.add(p1);
        //Xuống thêm 1 dòng
        Barcode barcode = new Barcode128();
        barcode.setCode(maVe);
        Image barcodeImage = barcode.createAwtImage(Color.BLACK, Color.WHITE);
        com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(barcodeImage, null);
        img.setAlignment(Element.ALIGN_CENTER);
        document.add(img);
        Paragraph p2 = new Paragraph("Mã vé/TicketID: " + maVe, fontContent);
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
        Paragraph p4 = new Paragraph(gaDi + "          " + gaDen, fontTitle);
        p4.setAlignment(Element.ALIGN_CENTER);
        document.add(p4);

        document.add(new Paragraph(" "));
        //Tạo para có ngày giờ
        Paragraph p5 = new Paragraph("Tàu/Train:", fontContent);
        p5.setAlignment(Element.ALIGN_JUSTIFIED);
        p5.add(new Chunk("SE1"));
        //tạo margin bottom
        p5.setSpacingAfter(5);
        document.add(p5);
        //Tạo para có ngày giờ
        Paragraph p6 = new Paragraph("Ngày/Date:", fontContent);
        p6.setAlignment(Element.ALIGN_LEFT);
        p6.add(new Chunk("20/10/2024"));
        p6.setSpacingAfter(5);
        document.add(p6);
        //Tạo para có ngày giờ
        Paragraph p7 = new Paragraph("Giờ/Time:", fontContent);
        p7.setAlignment(Element.ALIGN_LEFT);
        p7.add(new Chunk("20:00"));
        p7.setSpacingAfter(5);
        document.add(p7);
        //Tạo para có Toa
        Paragraph p8 = new Paragraph("Toa/Coach: " + "A1" + "Chỗ/Seat: 25", fontContent);
        p8.setAlignment(Element.ALIGN_LEFT);
        p8.setSpacingAfter(5);
        document.add(p8);
        //Tạo para có Loại chỗ
        Paragraph p9 = new Paragraph("Loại chỗ/Class: " + "Ghế cứng", fontContent);
        p9.setAlignment(Element.ALIGN_LEFT);
        p9.setSpacingAfter(5);
        document.add(p9);
        //Tạo para có Loại vé
        Paragraph p10 = new Paragraph("Loại vé/Ticket: " + "Vé người lớn", fontContent);
        p10.setAlignment(Element.ALIGN_LEFT);
        p10.setSpacingAfter(5);
        document.add(p10);
        //Tạo para có Họ tên
        Paragraph p11 = new Paragraph("Họ tên/Name: " + "Nguyễn Văn A", fontContent);
        p11.setAlignment(Element.ALIGN_LEFT);
        p11.setSpacingAfter(5);
        document.add(p11);
        //Tạo para có Giấy tờ
        Paragraph p12 = new Paragraph("Giấy tờ/Passport: " + "0123456789", fontContent);
        p12.setAlignment(Element.ALIGN_LEFT);
        p12.setSpacingAfter(5);
        document.add(p12);
        //Tạo para có Địa chỉ
        Paragraph p13 = new Paragraph("Giá/Price: " + "123 Nguyễn Văn A", fontContent);
        p13.setAlignment(Element.ALIGN_LEFT);
        p13.setSpacingAfter(5);
        document.add(p13);

        try {
            document.close();
            System.out.println("Tạo file PDF thành công!");
            Desktop.getDesktop().open(new File("src/main/resources/pdf/ve.pdf"));
        }catch (Exception e){
            System.out.println("Tạo file PDF thất bại!");
        }
    }

    public static void main(String[] args) throws IOException, DocumentException {
        Ve ve = new Ve();
        new PrintPDF(ve);
    }
}
