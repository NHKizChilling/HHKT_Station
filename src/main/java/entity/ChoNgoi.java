package entity;

import java.util.Objects;

public class ChoNgoi {
    private String maChoNgoi;
    private Toa toa;
    private int sttCho;
    private int tang;
    private int khoang;

    public ChoNgoi() {
    }

    public ChoNgoi(String maChoNgoi) {
        setMaChoNgoi(maChoNgoi);
    }


    public ChoNgoi(String maChoNgoi, Toa toa, int sttCho, int tang, int khoang) {
        setMaChoNgoi(maChoNgoi);
        setToa(toa);
        setSttCho(sttCho);
        setTang(tang);
        setKhoang(khoang);
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
