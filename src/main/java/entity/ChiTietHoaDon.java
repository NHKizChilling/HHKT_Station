package entity;

public class ChiTietHoaDon {
    private HoaDon hoaDon;
    private Ve ve;
    private double giaVe;
    private double giaGiam;

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

    public double tinhGiaVe() {
        double giaCho = ve.getChoNgoi().getGiaCho();
        double khoangCach = ve.getLichTrinh().getGaDen().getKhoangCach();
        double giaCoBan = giaCho * khoangCach;
        double heSoKhoangCach = 1;
        double heSoToa;

        String maLoaiToa = ve.getChoNgoi().getToa().getLoaiToa().getMaLoaiToa();

        if (khoangCach <= 100) {
            heSoKhoangCach += 0.1;
        } else if (khoangCach >= 101 && khoangCach <= 250) {
            heSoKhoangCach += 0.25;
        } else if(khoangCach >= 251 && khoangCach <= 1000) {
            heSoKhoangCach += 0.5;
        } else {
            heSoKhoangCach += 1;
        }

        if(maLoaiToa.equals("GNK4")) {
            heSoToa = 1.25;
        } else if(maLoaiToa.equals("GNK6")) {
            heSoToa = 1.5;
        } else if(maLoaiToa.equals("TVIP")) {
            heSoToa = 2;
        } else {
            heSoToa = 1;
        }
        this.giaVe = giaCoBan * heSoKhoangCach * heSoToa;
        return giaVe;
    }

    public double tinhGiaGiam() {
        double mucGiamGia = ve.getLoaiVe().getMucGiamGia();
        boolean khuHoi = ve.isKhuHoi();

        if(mucGiamGia > 0.1) {
            giaGiam = giaVe * mucGiamGia;
        } if (khuHoi) {
            giaGiam = giaVe * 0.1;
        } else {
            giaGiam = giaVe * mucGiamGia;
        }
        return giaGiam;
    }
}
