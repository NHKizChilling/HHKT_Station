package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class LichTrinh {
    private String maLichTrinh;
    private ChuyenTau chuyenTau;
    private Ga gaDi;
    private Ga gaDen;
    private int khoangCach;
    private LocalDateTime thoiGianKhoiHanh;
    private LocalDateTime thoiGianDuKienDen;
    private boolean tinhTrang;


    public LichTrinh() {
    }

    public LichTrinh(String maLichTrinh, ChuyenTau chuyenTau, Ga gaDi, Ga gaDen, int khoangCach,
                     LocalDateTime thoiGianKhoiHanh, LocalDateTime thoiGianDuKienDen, boolean tinhTrang) {
        setMaLichTrinh(maLichTrinh);
        setChuyenTau(chuyenTau);
        setGaDi(gaDi);
        setGaDen(gaDen);
        setKhoangCach(khoangCach);
        setThoiGianKhoiHanh(thoiGianKhoiHanh);
        setThoiGianDuKienDen(thoiGianDuKienDen);
        setTinhTrang(tinhTrang);
    }

    public String getMaLichTrinh() {
        return maLichTrinh;
    }

    public void setMaLichTrinh(String maLichTrinh) {
        if (maLichTrinh == null || maLichTrinh.isBlank()) {
            throw new IllegalArgumentException("Mã lịch trình không hợp lệ");
        }
        this.maLichTrinh = maLichTrinh;
    }

    public ChuyenTau getChuyenTau() {
        return chuyenTau;
    }

    public void setChuyenTau(ChuyenTau chuyenTau) {
        if (chuyenTau == null) {
            throw new IllegalArgumentException("Chuyến tàu không hợp lệ");
        }
        this.chuyenTau = chuyenTau;
    }

    public Ga getGaDi() {
        return gaDi;
    }

    public void setGaDi(Ga gaDi) {
        if (gaDi == null) {
            throw new IllegalArgumentException("Ga đi không hợp lệ");
        }
        this.gaDi = gaDi;
    }

    public Ga getGaDen() {
        return gaDen;
    }

    public void setGaDen(Ga gaDen) {
        if (gaDen == null) {
            throw new IllegalArgumentException("Ga đến không hợp lệ");
        }
        this.gaDen = gaDen;
    }

    public int getKhoangCach() {
        return khoangCach;
    }

    public void setKhoangCach(int khoangCach) {
        if (khoangCach <= 0) {
            throw new IllegalArgumentException("Khoảng cách không hợp lệ");
        }
        this.khoangCach = khoangCach;
    }

    public LocalDateTime getThoiGianKhoiHanh() {
        return thoiGianKhoiHanh;
    }

    public void setThoiGianKhoiHanh(LocalDateTime thoiGianKhoiHanh) {
        if (thoiGianKhoiHanh.isAfter(thoiGianDuKienDen)) {
            throw new IllegalArgumentException("Thời gian khởi hành không hợp lệ");
        }
        this.thoiGianKhoiHanh = thoiGianKhoiHanh;
    }

    public LocalDateTime getThoiGianDuKienDen() {
        return thoiGianDuKienDen;
    }

    public void setThoiGianDuKienDen(LocalDateTime thoiGianDuKienDen) {
        if (thoiGianDuKienDen.isBefore(thoiGianKhoiHanh)) {
            throw new IllegalArgumentException("Thời gian dự kiến đến không thể ở trước thời gian khởi hành");
        }
        if (thoiGianDuKienDen.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Thời gian dự kiến đến không thể ở trước thời gian hiện tại");
        }
        this.thoiGianDuKienDen = thoiGianDuKienDen;
    }

    public boolean isTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(boolean tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LichTrinh lichTrinh)) return false;
        return Objects.equals(getMaLichTrinh(), lichTrinh.getMaLichTrinh());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMaLichTrinh());
    }
}
