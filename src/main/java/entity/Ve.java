package entity;

import java.time.LocalDate;
import java.util.Objects;

public class Ve {
    private String maVe;
    private HanhKhach maHK;
    private ChoNgoi maSoCho;
    private LichTrinh maLT;
    private LoaiVe maLoaiVe;
    private String tenHK;
    private String soCCCD;
    private LocalDate ngaySinh;
    private String tinhTrangVe;
    private boolean khuHoi;

    public Ve() {
    }

    public Ve(String maVe) {
        setMaVe(maVe);
    }

    public Ve(HanhKhach maHK, ChoNgoi maSoCho, LichTrinh maLT, LoaiVe maLoaiVe, String tenHK, String soCCCD, LocalDate ngaySinh, String tinhTrangVe, boolean khuHoi) {
        setMaHK(maHK);
        setMaSoCho(maSoCho);
        setMaLT(maLT);
        setMaLoaiVe(maLoaiVe);
        setTenHK(tenHK);
        setSoCCCD(soCCCD);
        setNgaySinh(ngaySinh);
        setTinhTrangVe(tinhTrangVe);
        setKhuHoi(khuHoi);
    }

    public Ve(String maVe, HanhKhach maHK, ChoNgoi maSoCho, LichTrinh maLT, LoaiVe maLoaiVe, String tenHK, String soCCCD, LocalDate ngaySinh, String tinhTrangVe, boolean khuHoi) {
        setMaVe(maVe);
        setMaHK(maHK);
        setMaSoCho(maSoCho);
        setMaLT(maLT);
        setMaLoaiVe(maLoaiVe);
        setTenHK(tenHK);
        setSoCCCD(soCCCD);
        setNgaySinh(ngaySinh);
        setTinhTrangVe(tinhTrangVe);
        setKhuHoi(khuHoi);
    }

    public String getMaVe() {
        return maVe;
    }

    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }

    public HanhKhach getMaHK() {
        return maHK;
    }

    public void setMaHK(HanhKhach maHK) {
        this.maHK = maHK;
    }

    public ChoNgoi getMaSoCho() {
        return maSoCho;
    }

    public void setMaSoCho(ChoNgoi maSoCho) {
        this.maSoCho = maSoCho;
    }

    public LichTrinh getMaLT() {
        return maLT;
    }

    public void setMaLT(LichTrinh maLT) {
        this.maLT = maLT;
    }

    public LoaiVe getMaLoaiVe() {
        return maLoaiVe;
    }

    public void setMaLoaiVe(LoaiVe maLoaiVe) {
        this.maLoaiVe = maLoaiVe;
    }

    public String getTenHK() {
        return tenHK;
    }

    public void setTenHK(String tenHK) {
        this.tenHK = tenHK;
    }

    public String getSoCCCD() {
        return soCCCD;
    }

    public void setSoCCCD(String soCCCD) {
        this.soCCCD = soCCCD;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getTinhTrangVe() {
        return tinhTrangVe;
    }

    public void setTinhTrangVe(String tinhTrangVe) {
        this.tinhTrangVe = tinhTrangVe;
    }

    public boolean isKhuHoi() {
        return khuHoi;
    }

    public void setKhuHoi(boolean khuHoi) {
        this.khuHoi = khuHoi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ve ve)) return false;
        return Objects.equals(getMaVe(), ve.getMaVe());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMaVe());
    }
}
