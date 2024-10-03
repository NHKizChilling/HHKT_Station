package entity;

import java.util.Objects;

public class ChiTietLichTrinh {
    private Ve ve;
    private ChoNgoi choNgoi;
    private LichTrinh lichTrinh;
    private boolean trangThai;
    private double giaCho;

    public ChiTietLichTrinh() {
    }

    public ChiTietLichTrinh(Ve ve) {
        setVe(ve);
    }

    public ChiTietLichTrinh(Ve ve, ChoNgoi choNgoi, LichTrinh lichTrinh, boolean trangThai, double giaCho) {
        this.ve = ve;
        this.choNgoi = choNgoi;
        this.lichTrinh = lichTrinh;
        this.trangThai = trangThai;
        this.giaCho = giaCho;
    }

    public void tinhGiaCho() {
        double giaCho = 0;
        String loaiToa = choNgoi.getToa().getLoaiToa().getMaLoaiToa();
        double khoangCach = lichTrinh.getGaDen().getKhoangCach();

        if (loaiToa.equals("NC")) {
            if (khoangCach <= 50) {
                giaCho = 50000;
            } else if (khoangCach <= 100) {
                giaCho = 60000;
            } else {
                giaCho = 70000;
            }
        } else if (loaiToa.equals("NM")) {
            if (khoangCach <= 50) {
                giaCho = 60000;
            } else if (khoangCach <= 100) {
                giaCho = 70000;
            } else {
                giaCho = 80000;
            }
        } else if (loaiToa.equals("GNK6")) {
            if (khoangCach <= 50) {
                giaCho = 70000;
            } else if (khoangCach <= 100) {
                giaCho = 80000;
            } else {
                giaCho = 90000;
            }
        } else if (loaiToa.equals("GNK4")) {
            if (khoangCach <= 50) {
                giaCho = 80000;
            } else if (khoangCach <= 100) {
                giaCho = 90000;
            } else {
                giaCho = 100000;
            }
        } else if (loaiToa.equals("TVIP")) {
            if (khoangCach <= 50) {
                giaCho = 90000;
            } else if (khoangCach <= 100) {
                giaCho = 100000;
            } else {
                giaCho = 110000;
            }
        }
        this.giaCho = giaCho;
    }

    public Ve getVe() {
        return ve;
    }

    public void setVe(Ve ve) {
        if (ve == null) {
            throw new IllegalArgumentException("Vé null");
        }
        this.ve = ve;
    }

    public ChoNgoi getChoNgoi() {
        return choNgoi;
    }

    public void setChoNgoi(ChoNgoi choNgoi) {
        if (choNgoi == null) {
            throw new IllegalArgumentException("Chỗ ngồi null");
        }
        this.choNgoi = choNgoi;
    }

    public LichTrinh getLichTrinh() {
        return lichTrinh;
    }

    public void setLichTrinh(LichTrinh lichTrinh) {
        if (lichTrinh == null) {
            throw new IllegalArgumentException("Lịch trình null");
        }
        this.lichTrinh = lichTrinh;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public double getGiaCho() {
        return giaCho;
    }

    public void setGiaCho(double giaCho) {
        if (giaCho < 0) {
            throw new IllegalArgumentException("Giá chỗ ngồi nhỏ hơn 0");
        }
        this.giaCho = giaCho;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChiTietLichTrinh that)) return false;
        return Objects.equals(getChoNgoi(), that.getChoNgoi()) && Objects.equals(getLichTrinh(), that.getLichTrinh());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChoNgoi(), getLichTrinh());
    }
}
