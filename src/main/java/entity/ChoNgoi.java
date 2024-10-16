package entity;

import java.util.Objects;

public class ChoNgoi {
    private String maChoNgoi;
    private Toa toa;
    private int sttCho;
    private int tang;
    private int khoang;
    private String trangThai;
    private double giaCho;

    public ChoNgoi() {
    }

    public ChoNgoi(String maChoNgoi) {
        setMaChoNgoi(maChoNgoi);
    }

    public ChoNgoi(String maChoNgoi, Toa toa, int sttCho, int tang, int khoang, String trangThai, double giaCho) {
        setMaChoNgoi(maChoNgoi);
        setToa(toa);
        setSttCho(sttCho);
        setTang(tang);
        setKhoang(khoang);
        setTrangThai(trangThai);
        setGiaCho(giaCho);
    }

    public ChoNgoi(String maChoNgoi, Toa toa, int sttCho, int tang, int khoang, String trangThai) {
        setMaChoNgoi(maChoNgoi);
        setToa(toa);
        setSttCho(sttCho);
        setTang(tang);
        setKhoang(khoang);
        setTrangThai(trangThai);
    }


    public double getGiaCho() {
        return giaCho;
    }

    public void setGiaCho(double giaCho) {
        if (giaCho < 0) {
            throw new IllegalArgumentException("Giá chỗ không hợp lệ");
        }
        this.giaCho = giaCho;
    }

    public String getMaChoNgoi() {
        return maChoNgoi;
    }


    //mã chỗ ngồi là: mã toa + CN + Stt chỗ ngồi
    public void setMaChoNgoi(String maChoNgoi) {
        if (maChoNgoi == null || maChoNgoi.isBlank()) {
            throw new IllegalArgumentException("Mã chỗ ngồi không hợp lệ");
        }
//        if (!maChoNgoi.matches("^[A-Z]{2}CN[0-9]{2}$")) {
//            throw new IllegalArgumentException("Mã chỗ ngồi không hợp lệ");
//        }
        this.maChoNgoi = maChoNgoi;
    }

    public Toa getToa() {
        return toa;
    }

    public void setToa(Toa toa) {
        if (toa == null) {
            throw new IllegalArgumentException("Toa không hợp lệ");
        }
        this.toa = toa;
    }

    public int getSttCho() {
        return sttCho;
    }

    public void setSttCho(int sttCho) {
        this.sttCho = sttCho;
    }

    public int getTang() {
        return tang;
    }

    public void setTang(int tang) {
        this.tang = tang;
    }

    public int getKhoang() {
        return khoang;
    }

    public void setKhoang(int khoang) {
        this.khoang = khoang;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChoNgoi choNgoi)) return false;
        return Objects.equals(getMaChoNgoi(), choNgoi.getMaChoNgoi());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMaChoNgoi());
    }
}
