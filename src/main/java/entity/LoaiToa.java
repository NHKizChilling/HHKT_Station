package entity;

import java.util.Objects;

public class LoaiToa {
    private String maLoaiToa;
    private String tenLoaiToa;

    public LoaiToa() {
    }

    public LoaiToa(String maLoaiToa) {
        setMaLoaiToa(maLoaiToa);
    }

    public LoaiToa(String maLoaiToa, String tenLoaiToa) {
        setMaLoaiToa(maLoaiToa);
        setTenLoaiToa(tenLoaiToa);
    }

    public String getMaLoaiToa() {
        return maLoaiToa;
    }


    // mã loại toa bao gồm:  NC, NM, GNK6, GNK4, TVIP.
    public void setMaLoaiToa(String maLoaiToa) {
        if (maLoaiToa == null || maLoaiToa.isBlank()) {
            throw new IllegalArgumentException("Mã loại toa không hợp lệ");
        }
        if (!maLoaiToa.matches("^(NC|NM|GNK6|GNK4|TVIP)$")) {
            throw new IllegalArgumentException("Mã loại toa không hợp lệ");
        }
        this.maLoaiToa = maLoaiToa;
    }

    public String getTenLoaiToa() {
        return tenLoaiToa;
    }

    public void setTenLoaiToa(String tenLoaiToa) {
        if (tenLoaiToa == null || tenLoaiToa.isBlank()) {
            throw new IllegalArgumentException("Tên loại toa không hợp lệ");
        }
        this.tenLoaiToa = tenLoaiToa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoaiToa loaiToa)) return false;
        return Objects.equals(getMaLoaiToa(), loaiToa.getMaLoaiToa());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMaLoaiToa());
    }
}
