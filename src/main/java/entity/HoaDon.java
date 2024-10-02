package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class HoaDon {
    private String maHoaDon;
    private NhanVien nhanVien;
    private LocalDateTime ngayLapHoaDon;
    private HanhKhach hanhKhach;
    private double tongTien;
    private double tongGiamGia;

    public HoaDon() {
    }

    public HoaDon(String maHoaDon) {
        setMaHoaDon(maHoaDon);
    }

    public HoaDon(String maHoaDon, NhanVien nhanVien, LocalDateTime ngayLapHoaDon,
                  HanhKhach hanhKhach) {
        setMaHoaDon(maHoaDon);
        setNhanVien(nhanVien);
        setNgayLapHoaDon(ngayLapHoaDon);
        setHanhKhach(hanhKhach);
    }

    public HoaDon(String maHoaDon, NhanVien nhanVien, LocalDateTime ngayLapHoaDon,
                  HanhKhach hanhKhach, double tongTien, double giamGia) {
        setMaHoaDon(maHoaDon);
        setNhanVien(nhanVien);
        setNgayLapHoaDon(ngayLapHoaDon);
        setHanhKhach(hanhKhach);
        setTongTien(tongTien);
        setTongGiamGia(giamGia);
    }

    public double tinhTongTien(ArrayList<ChiTietHoaDon> dsChiTietHoaDon) {
        tongTien = 0;
        double phiDichVu = dsChiTietHoaDon.size() * 2000;

        for (ChiTietHoaDon chiTietHoaDon : dsChiTietHoaDon) {
            tongTien += chiTietHoaDon.tinhGiaVe();
        }
        tongTien += phiDichVu;
        return tongTien;
    }

    public double tinhTongGiamGia(ArrayList<ChiTietHoaDon> dsChiTietHoaDon) {
        tongGiamGia = 0;
        for (ChiTietHoaDon chiTietHoaDon : dsChiTietHoaDon) {
            tongGiamGia += chiTietHoaDon.tinhGiaGiam();
        }
        return tongGiamGia;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public double getTongGiamGia() {
        return tongGiamGia;
    }

    public void setTongGiamGia(double tongGiamGia) {
        this.tongGiamGia = tongGiamGia;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    // mã hóa đơn bao gồm: HD+ngày lập hóa đơn+6 chữ số tăng dần
    public void setMaHoaDon(String maHoaDon) {
        if (maHoaDon == null || maHoaDon.isBlank()) {
            throw new IllegalArgumentException("Mã hóa đơn không hợp lệ");
        }
        if (!maHoaDon.matches("HD[0-9]{12}$")) {
            throw new IllegalArgumentException("Mã hóa đơn không hợp lệ");
        }
        this.maHoaDon = maHoaDon;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        if (nhanVien == null) {
            throw new IllegalArgumentException("Nhân viên không hợp lệ");
        }
        this.nhanVien = nhanVien;
    }

    public LocalDateTime getNgayLapHoaDon() {
        return ngayLapHoaDon;
    }

    public void setNgayLapHoaDon(LocalDateTime ngayLapHoaDon) {
        if (ngayLapHoaDon == null) {
            throw new IllegalArgumentException("Ngày lập hóa đơn không hợp lệ");
        }
        if (ngayLapHoaDon.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Ngày lập hóa đơn không hợp lệ");
        }
        this.ngayLapHoaDon = ngayLapHoaDon;
    }

    public HanhKhach getHanhKhach() {
        return hanhKhach;
    }

    public void setHanhKhach(HanhKhach hanhKhach) {
        if (hanhKhach == null) {
            throw new IllegalArgumentException("Hành khách không hợp lệ");
        }
        this.hanhKhach = hanhKhach;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HoaDon hoaDon)) return false;
        return Objects.equals(getMaHoaDon(), hoaDon.getMaHoaDon());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMaHoaDon());
    }
}
