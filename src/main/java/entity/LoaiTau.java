package entity;

import java.util.Objects;

public class LoaiTau {
    private String maLoaiTau;
    private String tenLoaiTau;

    public LoaiTau() {
    }

    public LoaiTau(String maLoaiTau, String tenLoaiTau) {
        setMaLoaiTau(maLoaiTau);
        setTenLoaiTau(tenLoaiTau);
    }

    public String getMaLoaiTau() {
        return maLoaiTau;
    }

    // tên viết tắt của tên các loại tàu (VD: SE: super express)
    public void setMaLoaiTau(String maLoaiTau) {
        if (maLoaiTau == null || maLoaiTau.isBlank()) {
            throw new IllegalArgumentException("Mã loại tàu không hợp lệ");
        }
        this.maLoaiTau = maLoaiTau;
    }

    public String getTenLoaiTau() {
        return tenLoaiTau;
    }

    public void setTenLoaiTau(String tenLoaiTau) {
        if (tenLoaiTau == null || tenLoaiTau.isBlank()) {
            throw new IllegalArgumentException("Tên loại tàu không hợp lệ");
        }
        this.tenLoaiTau = tenLoaiTau;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoaiTau loaiTau)) return false;
        return Objects.equals(getMaLoaiTau(), loaiTau.getMaLoaiTau());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMaLoaiTau());
    }
}
