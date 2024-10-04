package entity;

import java.util.Objects;

public class Ve {
    private String maVe;
    private LoaiVe loaiVe;
    private HanhKhach hanhKhach;
    private ChiTietLichTrinh ctlt;
    private String tinhTrangVe;
    private boolean khuHoi;

    public Ve() {
    }

    public Ve(String maVe) {
        setMaVe(maVe);
    }

    public Ve(String maVe, LoaiVe loaiVe, HanhKhach hanhKhach, ChiTietLichTrinh ctlt, String tinhTrangVe, boolean khuHoi) {
        this.maVe = maVe;
        this.loaiVe = loaiVe;
        this.hanhKhach = hanhKhach;
        this.ctlt = ctlt;
        this.tinhTrangVe = tinhTrangVe;
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

    public HanhKhach getHanhKhach() {
        return hanhKhach;
    }

    public void setHanhKhach(HanhKhach hanhKhach) {
        this.hanhKhach = hanhKhach;
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
        if (!tinhTrangVe.matches("^(Đã bán|Đã đổi|Đã hủy)$")) {
            throw new IllegalArgumentException("Tình trạng vé không hợp lệ");
        }
        this.tinhTrangVe = tinhTrangVe;
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
