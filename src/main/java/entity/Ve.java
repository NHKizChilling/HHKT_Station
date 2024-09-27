package entity;

import java.util.Objects;

public class Ve {
    private String maVe;
    private LoaiVe loaiVe;
    private ChoNgoi choNgoi;
    private HanhKhach hanhKhach;
    private LichTrinh lichTrinh;
    private String tinhTrangVe;
    private boolean khuHoi;

    public Ve() {
    }

    public Ve(String maVe) {
        setMaVe(maVe);
    }

    public Ve(String maVe, LoaiVe loaiVe, ChoNgoi choNgoi, HanhKhach hanhKhach,
              LichTrinh lichTrinh, String tinhTrangVe, boolean khuHoi) {
        setMaVe(maVe);
        setLoaiVe(loaiVe);
        setChoNgoi(choNgoi);
        setHanhKhach(hanhKhach);
        setLichTrinh(lichTrinh);
        setTinhTrangVe(tinhTrangVe);
        setKhuHoi(khuHoi);
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

    public ChoNgoi getChoNgoi() {
        return choNgoi;
    }

    public void setChoNgoi(ChoNgoi choNgoi) {
        this.choNgoi = choNgoi;
    }

    public HanhKhach getHanhKhach() {
        return hanhKhach;
    }

    public void setHanhKhach(HanhKhach hanhKhach) {
        this.hanhKhach = hanhKhach;
    }

    public LichTrinh getLichTrinh() {
        return lichTrinh;
    }

    public void setLichTrinh(LichTrinh lichTrinh) {
        this.lichTrinh = lichTrinh;
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
