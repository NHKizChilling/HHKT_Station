package entity;

import java.util.Objects;

public class LoaiVe {
    private String maLoaiVe;
    private String tenLoaiVe;
    private float mucGiamGia;

    public LoaiVe() {
    }

    public String getMaLoaiVe() {
        return maLoaiVe;
    }


    public LoaiVe(String maLoaiVe, String tenLoaiVe, float mucGiamGia) {
        setMaLoaiVe(maLoaiVe);
        setTenLoaiVe(tenLoaiVe);
        setMucGiamGia(mucGiamGia);
    }

    // mã loại vé bao gồm: BT, NCT, TE, TE6, HSSV
    public void setMaLoaiVe(String maLoaiVe) {
        if (maLoaiVe == null || maLoaiVe.isBlank()) {
            throw new IllegalArgumentException("Mã loại vé không hợp lệ");
        }
        if (!maLoaiVe.matches("^(BT|NCT|TE|TE6|HSSV)$")) {
            throw new IllegalArgumentException("Mã loại vé không hợp lệ");
        }
        this.maLoaiVe = maLoaiVe;
    }

    public String getTenLoaiVe() {
        return tenLoaiVe;
    }

    // tên loại vé bao gồm: vé thường, người cao tuổi, trẻ em, trẻ em dưới 6 tuổi, học sinh sinh viên
    public void setTenLoaiVe(String tenLoaiVe) {
        if (tenLoaiVe == null || tenLoaiVe.isBlank()) {
            throw new IllegalArgumentException("Tên loại vé không hợp lệ");
        }
        if (!tenLoaiVe.matches("^(Vé thường|Người cao tuổi|Trẻ em|Trẻ em dưới 6 tuổi|Học sinh sinh viên)$")) {
            throw new IllegalArgumentException("Tên loại vé không hợp lệ");
        }
        this.tenLoaiVe = tenLoaiVe;
    }

    public float getMucGiamGia() {
        return mucGiamGia;
    }

    // mức giảm bao gồm: 0%, 10%, 15%, 25%
    public void setMucGiamGia(float mucGiamGia) {
        if (mucGiamGia < 0 || mucGiamGia > 100) {
            throw new IllegalArgumentException("Mức giảm giá không hợp lệ");
        }
        if (mucGiamGia != 0 && mucGiamGia != 10 && mucGiamGia != 15 && mucGiamGia != 25) {
            throw new IllegalArgumentException("Mức giảm giá không hợp lệ");
        }
        this.mucGiamGia = mucGiamGia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoaiVe loaiVe)) return false;
        return Objects.equals(getMaLoaiVe(), loaiVe.getMaLoaiVe());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMaLoaiVe());
    }
}
