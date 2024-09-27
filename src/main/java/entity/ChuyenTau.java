package entity;

import java.util.Objects;

public class ChuyenTau {
    private String soHieutau;
    private LoaiTau loaiTau;

    public ChuyenTau() {
    }

    public ChuyenTau(String soHieutau, LoaiTau loaiTau) {
        setSoHieutau(soHieutau);
        setLoaiTau(loaiTau);
    }

    public String getSoHieutau() {
        return soHieutau;
    }

    // có dạng AAAAXXXX, trong đó: AAAA là tên viết tắt của chủ so huu hoac ten thuong mai, XXXX là 4 chữ số
    public void setSoHieutau(String soHieutau) {
        if (soHieutau == null || soHieutau.isBlank()) {
            throw new IllegalArgumentException("Số hiệu tàu không được rỗng");
        }
        if (!soHieutau.matches("^[A-Z]{4}[0-9]{4}$")) {
            throw new IllegalArgumentException("Số hiệu tàu không hợp lệ");
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
