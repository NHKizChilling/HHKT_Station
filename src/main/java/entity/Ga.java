package entity;

import java.util.Objects;

public class Ga {
    private String maGa;
    private String tenGa;
    private String viTri;
    private int khoangCach;


    public Ga() {
    }

    public Ga(String maGa) {
        setMaGa(maGa);
    }

    public Ga(String maGa, String tenGa, String viTri, int khoangCach) {
        setMaGa(maGa);
        setTenGa(tenGa);
        setViTri(viTri);
        setKhoangCach(khoangCach);
    }

    public int getKhoangCach() {
        return khoangCach;
    }

    public void setKhoangCach(int khoangCach) {
        if (khoangCach < 0) {
            throw new IllegalArgumentException("Khoảng cách không hợp lệ");
        }
        this.khoangCach = khoangCach;
    }

    public String getMaGa() {
        return maGa;
    }

    public void setMaGa(String maGa) {
        this.maGa = maGa;
    }

    public String getTenGa() {
        return tenGa;
    }

    public void setTenGa(String tenGa) {
        this.tenGa = tenGa;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ga ga)) return false;
        return Objects.equals(getMaGa(), ga.getMaGa());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMaGa());
    }
}
