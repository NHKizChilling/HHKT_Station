package entity;

public class ChiTietHoaDon {
    private HoaDon hoaDon;
    private Ve ve;
    private double giaVe;
    private double giaGiam;
    private static final double PHI_DICH_VU = 2000;

    public HoaDon getHoaDon() {
        return hoaDon;
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
        if (hoaDon == null) {
            throw new IllegalArgumentException("Hóa đơn không hợp lệ");
        }
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
            giaVe = giaCho * (1 - loaiVe.getMucGiamGia()) * 1.1 + PHI_DICH_VU;
        } else {
            if (ve.isKhuHoi()) {
                giaVe = giaCho * 0.9 * 1.1 + PHI_DICH_VU;
            } else {
                giaVe = giaCho * 1.1 + PHI_DICH_VU;
            }
        }
        this.giaVe = giaVe;
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
        this.giaGiam = giaGiam;
    }
}
