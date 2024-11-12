package entity;

import java.time.LocalDate;
import java.util.Objects;

public class Ve {
    private String maVe;
    private KhachHang khachHang;
    private ChiTietLichTrinh ctlt;
    private LoaiVe loaiVe;
    private String tenHanhKhach;
    private String SoCCCD;
    private LocalDate ngaySinh;
    private String tinhTrangVe;
    private boolean khuHoi;

    public Ve() {
    }

    public Ve(String maVe) {
        setMaVe(maVe);
    }

    public Ve(KhachHang khachHang, ChiTietLichTrinh ctlt, LoaiVe loaiVe, String tenHanhKhach,
              String soCCCD, LocalDate ngaySinh, String tinhTrangVe, boolean khuHoi) {
        setHanhKhach(khachHang);
        setCtlt(ctlt);
        setLoaiVe(loaiVe);
        setTenHanhKhach(tenHanhKhach);
        setSoCCCD(soCCCD);
        setNgaySinh(ngaySinh);
        setTinhTrangVe(tinhTrangVe);
        setKhuHoi(khuHoi);
    }

    public Ve(String maVe, KhachHang khachHang, ChiTietLichTrinh ctlt, LoaiVe loaiVe,
              String tenHK, String SoCCCD, LocalDate ngaySinh, String tinhTrangVe, boolean khuHoi) {
        this.maVe = maVe;
        this.loaiVe = loaiVe;
        this.khachHang = khachHang;
        this.ctlt = ctlt;
        this.tenHanhKhach = tenHK;
        this.SoCCCD = SoCCCD;
        this.ngaySinh = ngaySinh;
        setTinhTrangVe(tinhTrangVe);
        this.khuHoi = khuHoi;
    }

    public boolean isKhuHoi() {
        return khuHoi;
    }

    public void setKhuHoi(boolean khuHoi) {
        this.khuHoi = khuHoi;
    }

    public String getMaVe() {
        return maVe;
    }

    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }

    public LoaiVe getLoaiVe() {
        return loaiVe;
    }

    public void setLoaiVe(LoaiVe loaiVe) {
        this.loaiVe = loaiVe;
    }

    public KhachHang getHanhKhach() {
        return khachHang;
    }

    public void setHanhKhach(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public ChiTietLichTrinh getCtlt() {
        return ctlt;
    }

    public void setCtlt(ChiTietLichTrinh ctlt) {
        this.ctlt = ctlt;
    }

    public String getTinhTrangVe() {
        return tinhTrangVe;
    }

    public void setTinhTrangVe(String tinhTrangVe) {
        if (tinhTrangVe == null || tinhTrangVe.isBlank()) {
            throw new IllegalArgumentException("Tình trạng vé không được rỗng");
        }
        if (!tinhTrangVe.matches("^(DaBan|DaDoi|DaHuy)$")) {
            throw new IllegalArgumentException("Tình trạng vé không hợp lệ");
        }
        this.tinhTrangVe = tinhTrangVe;
    }

    public String getTenHanhKhach() {
        return tenHanhKhach;
    }

    public void setTenHanhKhach(String tenHanhKhach) {
        if (tenHanhKhach == null || tenHanhKhach.isBlank()) {
            throw new IllegalArgumentException("Tên hành khách không được rỗng");
        }
        this.tenHanhKhach = tenHanhKhach;
    }

    public String getSoCCCD() {
        return SoCCCD;
    }

    public void setSoCCCD(String SoCCCD) {
        this.SoCCCD = SoCCCD;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ve ve)) return false;
        return Objects.equals(getMaVe(), ve.getMaVe());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMaVe());
    }
}
