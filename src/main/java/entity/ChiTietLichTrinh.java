package entity;

import java.util.Objects;

public class ChiTietLichTrinh {
    private ChoNgoi choNgoi;
    private LichTrinh lichTrinh;
    private boolean trangThai;
    private double giaCho;
    public static final double GIA_CO_BAN = 500;

    public ChiTietLichTrinh() {
    }

    public ChiTietLichTrinh(ChoNgoi choNgoi, LichTrinh lichTrinh) {
        setChoNgoi(choNgoi);
        setLichTrinh(lichTrinh);
    }

    public ChiTietLichTrinh(ChoNgoi choNgoi, LichTrinh lichTrinh, boolean trangThai, double giaCho) {
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
            if (khoangCach <= 100) {
                giaCho = GIA_CO_BAN * khoangCach * 1.1;
            } else if (khoangCach <= 250) {
                giaCho = GIA_CO_BAN * khoangCach * 1.25;
            } else if (khoangCach <= 1000) {
                giaCho = GIA_CO_BAN * khoangCach * 1.5;
            } else {
                giaCho = GIA_CO_BAN * khoangCach * 2;
            }
        } else if (loaiToa.equals("NM")) {
            if (khoangCach <= 100) {
                giaCho = GIA_CO_BAN * khoangCach * 1.1 * 1.1;
            } else if (khoangCach <= 250) {
                giaCho = GIA_CO_BAN * khoangCach * 1.25 * 1.1;
            } else if (khoangCach <= 1000) {
                giaCho = GIA_CO_BAN * khoangCach * 1.5 * 1.1;
            } else {
                giaCho = GIA_CO_BAN * khoangCach * 2 * 1.1;
            }
        } else if (loaiToa.equals("GNK6")) {
            if (khoangCach <= 100) {
                giaCho = GIA_CO_BAN * khoangCach * 1.1 * 1.25;
            } else if (khoangCach <= 250) {
                giaCho = GIA_CO_BAN * khoangCach * 1.25 * 1.25;
            } else if (khoangCach <= 1000) {
                giaCho = GIA_CO_BAN * khoangCach * 1.5 * 1.25;
            } else {
                giaCho = GIA_CO_BAN * khoangCach * 2 * 1.25;
            }
        } else if (loaiToa.equals("GNK4")) {
            if (khoangCach <= 100) {
                giaCho = GIA_CO_BAN * khoangCach * 1.1 * 1.5;
            } else if (khoangCach <= 250) {
                giaCho = GIA_CO_BAN * khoangCach * 1.25 * 1.5;
            } else if (khoangCach <= 1000) {
                giaCho = GIA_CO_BAN * khoangCach * 1.5 * 1.5;
            } else {
                giaCho = GIA_CO_BAN * khoangCach * 2 * 1.5;
            }
        } else if (loaiToa.equals("TVIP")) {
            if (khoangCach <= 100) {
                giaCho = GIA_CO_BAN * khoangCach * 1.1 * 2;
            } else if (khoangCach <= 250) {
                giaCho = GIA_CO_BAN * khoangCach * 1.25 * 2;
            } else if (khoangCach <= 1000) {
                giaCho = GIA_CO_BAN * khoangCach * 1.5 * 2;
            } else {
                giaCho = GIA_CO_BAN * khoangCach * 2 * 2;
            }
        }
        this.giaCho = giaCho;
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
