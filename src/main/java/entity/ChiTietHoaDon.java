package entity;

public class ChiTietHoaDon {
    private HoaDon hoaDon;
    private Ve ve;

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        if (hoaDon == null) {
            throw new IllegalArgumentException("Hóa đơn không hợp lệ");
        }
        this.hoaDon = hoaDon;
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
        double mucGiamGia = tinhGiamGia();
        LichTrinh lichTrinh = ve.getLichTrinh();
        LoaiToa loaiToa = ve.getChoNgoi().getToa().getLoaiToa();
        return mucGiamGia * ve.getChoNgoi().tinhGiaCho(loaiToa.getMaLoaiToa(), lichTrinh.getKhoangCach());
    }

    public double tinhGiamGia() {
        return ve.getLoaiVe().getMucGiamGia() * ve.getChoNgoi().tinhGiaCho(ve.getChoNgoi().getToa().getLoaiToa().getMaLoaiToa(), ve.getLichTrinh().getKhoangCach());
    }
}
