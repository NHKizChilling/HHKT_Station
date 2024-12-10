package entity;

import java.util.Objects;

public class ChuyenTau {
    private String soHieutau;
    private LoaiTau loaiTau;

    public ChuyenTau() {
    }

    public ChuyenTau(String soHieutau) {
        setSoHieutau(soHieutau);
    }

    public ChuyenTau(String soHieutau, LoaiTau loaiTau) {
        setSoHieutau(soHieutau);
        setLoaiTau(loaiTau);
    }

    public String getSoHieutau() {
        return soHieutau;
    }

    public void setSoHieutau(String soHieutau) {
        if (soHieutau == null || soHieutau.isBlank()) {
            throw new IllegalArgumentException("Số hiệu tàu không được rỗng");
        }
        this.soHieutau = soHieutau;
    }

    public LoaiTau getLoaiTau() {
        return loaiTau;
    }

    public void setLoaiTau(LoaiTau loaiTau) {
        if (loaiTau == null) {
            throw new IllegalArgumentException("Loại tàu không được rỗng");
        }
        this.loaiTau = loaiTau;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChuyenTau chuyenTau)) return false;
        return Objects.equals(getSoHieutau(), chuyenTau.getSoHieutau());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getSoHieutau());
    }
}
