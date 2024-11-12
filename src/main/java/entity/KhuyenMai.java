package entity;

import java.util.Objects;

public class KhuyenMai {
    //MaKM VARCHAR(15) PRIMARY KEY,
    //	MoTa VARCHAR(50) NOT NULL,
    //	NgayApDung DATE NOT NULL,
    //	NgayHetHan DATE NOT NULL,
    //	MucKM FLOAT NOT NULL,
    //	TrangThai BIT NOT NULL
    private String maKM;
    private String moTa;
    private String ngayApDung;
    private String ngayHetHan;
    private float mucKM;
    private boolean trangThai;

    public KhuyenMai() {
    }

    public KhuyenMai(String maKM) {
        this.maKM = maKM;
    }

    public KhuyenMai(String maKM, String moTa, String ngayApDung, String ngayHetHan, float mucKM, boolean trangThai) {
        this.maKM = maKM;
        this.moTa = moTa;
        this.ngayApDung = ngayApDung;
        this.ngayHetHan = ngayHetHan;
        this.mucKM = mucKM;
        this.trangThai = trangThai;
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getNgayApDung() {
        return ngayApDung;
    }

    public void setNgayApDung(String ngayApDung) {
        this.ngayApDung = ngayApDung;
    }

    public String getNgayHetHan() {
        return ngayHetHan;
    }

    public void setNgayHetHan(String ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }

    public float getMucKM() {
        return mucKM;
    }

    public void setMucKM(float mucKM) {
        this.mucKM = mucKM;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KhuyenMai khuyenMai = (KhuyenMai) o;
        return Objects.equals(maKM, khuyenMai.maKM);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maKM);
    }
}
