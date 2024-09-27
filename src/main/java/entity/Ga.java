package entity;

import java.util.Objects;

public class Ga {
    private String maGa;
    private String tenGa;
    private String viTri;


    public Ga() {
    }

    public Ga(String maGa) {
        setMaGa(maGa);
    }

    public Ga(String maGa, String tenGa, String viTri) {
        setMaGa(maGa);
        setTenGa(tenGa);
        setViTri(viTri);
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
