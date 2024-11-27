package entity;

import java.util.Objects;

public class Toa {
    private String maToa;
    private int soSTToa;
    private LoaiToa loaiToa;
    private ChuyenTau chuyenTau;

    public Toa() {
    }

    public Toa(String maToa) {
        setMaToa(maToa);
    }

    public Toa(String maToa, int soSTToa, LoaiToa loaiToa, ChuyenTau chuyenTau) {
        setMaToa(maToa);
        setSoSTToa(soSTToa);
        setLoaiToa(loaiToa);
        setChuyenTau(chuyenTau);
    }

    public String getMaToa() {
        return maToa;
    }

    public void setMaToa(String maToa) {
        if (maToa == null || maToa.isBlank()) {
            throw new IllegalArgumentException("Mã toa không hợp lệ");
        }
        // maToa có dạng: 3-4 sô hoặc chữ + T + 1-2 chũ số
        if (!maToa.matches("^[a-zA-Z0-9]{3,4}T[0-9]{1,2}$")) {
            throw new IllegalArgumentException("Mã toa không hợp lệ");
        }
    }

    public int getSoSTToa() {
        return soSTToa;
    }

    public void setSoSTToa(int soSTToa) {
        if (soSTToa <= 0 || soSTToa > 12) {
            throw new IllegalArgumentException("Số sức chứa của toa phải lớn hơn 0 và nhỏ hơn 12");
        }
        this.soSTToa = soSTToa;
    }

    public LoaiToa getLoaiToa() {
        return loaiToa;
    }

    public void setLoaiToa(LoaiToa loaiToa) {
        if (loaiToa == null) {
            throw new IllegalArgumentException("Loại toa không hợp lệ");
        }
        // loaiToa gồm các loại: NC, NM, TVIP, GNK6, GNK4
        if (!loaiToa.getMaLoaiToa().equals("NC") && !loaiToa.getMaLoaiToa().equals("NM") && !loaiToa.getMaLoaiToa().equals("TVIP") && !loaiToa.getMaLoaiToa().equals("GNK6") && !loaiToa.getMaLoaiToa().equals("GNK4")) {
            throw new IllegalArgumentException("Loại toa không hợp lệ");
        }
        this.loaiToa = loaiToa;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Toa toa)) return false;
        return Objects.equals(getMaToa(), toa.getMaToa());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMaToa());
    }
}
