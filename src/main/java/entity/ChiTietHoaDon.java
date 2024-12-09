package entity;

import java.util.Objects;

public class ChiTietHoaDon {
    private HoaDon hoaDon;
    private Ve ve;
    private double giaVe;
    private double giaGiam;
    private static final double PHI_DICH_VU = 2000;

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(HoaDon hoaDon, Ve ve) {
        setHoaDon(hoaDon);
        setVe(ve);
        tinhGiaVe();
        tinhGiaGiam();
    }

    public ChiTietHoaDon(HoaDon hoaDon, Ve ve, double giaGiam, double giaVe) {
        setHoaDon(hoaDon);
        setVe(ve);
        setGiaGiam(giaGiam);
        setGiaVe(giaVe);
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public double getGiaVe() {
        return giaVe;
    }

    public double getGiaGiam() {
        return giaGiam;
    }

    public void setGiaGiam(double giaGiam) {
        this.giaGiam = giaGiam;
    }

    public void setGiaVe(double giaVe) {
        this.giaVe = giaVe;
    }

    public Ve getVe() {
        return ve;
    }

    public void setVe(Ve ve) {
        if (ve == null) {
            throw new IllegalArgumentException("Vé không hợp lệ");
        }
        this.ve = ve;
    }

    public void tinhGiaVe() {
        double giaVe = 0;
        double giaCho = ve.getCtlt().getGiaCho();
        LoaiVe loaiVe = ve.getLoaiVe();
        if(!loaiVe.getMaLoaiVe().equals("VNL")) {
            giaVe = giaCho * (1 - loaiVe.getMucGiamGia()) + PHI_DICH_VU;
        } else {
            if (ve.isKhuHoi()) {
                giaVe = giaCho * 0.9 + PHI_DICH_VU;
            } else {
                giaVe = giaCho + PHI_DICH_VU;
            }
        }
        this.giaVe = Math.round(giaVe/1000) * 1000;
    }

    public void tinhGiaGiam() {
        double giaGiam = 0;
        double giaCho = ve.getCtlt().getGiaCho();
        LoaiVe loaiVe = ve.getLoaiVe();

        if(!loaiVe.getMaLoaiVe().equals("VNL")) {
            giaGiam = giaCho * loaiVe.getMucGiamGia();
        } else {
            if (ve.isKhuHoi()) {
                giaGiam = giaCho * 0.1;
            }
        }
        this.giaGiam = Math.round(giaGiam/1000) * 1000;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietHoaDon that = (ChiTietHoaDon) o;
        return Objects.equals(hoaDon, that.hoaDon) && Objects.equals(ve, that.ve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hoaDon, ve);
    }
}
