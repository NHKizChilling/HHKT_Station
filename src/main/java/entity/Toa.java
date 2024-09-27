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
        this.maToa = maToa;
    }

    public int getSoSTToa() {
        return soSTToa;
    }

    public void setSoSTToa(int soSTToa) {
        this.soSTToa = soSTToa;
    }

    public LoaiToa getLoaiToa() {
        return loaiToa;
    }

    public void setLoaiToa(LoaiToa loaiToa) {
        if (loaiToa == null) {
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
