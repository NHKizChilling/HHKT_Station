package entity;

import java.util.Objects;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String soCCCD;
    private String sdt;
    private String email;

    public KhachHang() {
    }

    public KhachHang(String maKH) {
        setMaHanhKhach(maKH);
    }

    public KhachHang(String tenKH, String soCCCD, String sdt, String email) {
        setTenKH(tenKH);
        setSoCCCD(soCCCD);
        setSdt(sdt);
        setEmail(email);
    }

    public KhachHang(String maKH, String tenKH, String soCCCD, String sdt,
                     String email) {
        this.maKH = maKH;
        setTenKH(tenKH);
        setSoCCCD(soCCCD);
        setSdt(sdt);
        setEmail(email);
    }

    public String getMaKH() {
        return maKH;
    }

    // mã hành khách bao gồm: HK+8 chữ số tăng dần
    public void setMaHanhKhach(String maKH) {
        if (maKH == null || maKH.isBlank()) {
            throw new IllegalArgumentException("Mã khách hàng không hợp lệ");
        }
        if (!maKH.matches("KH[0-9]{8}$")) {
            throw new IllegalArgumentException("Mã khách hàng không hợp lệ");
        }
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    // tên hành khách thì mỗi chứ cái đầu phải ghi hoa và không chứa ký tự đặc biệt, số. phải có khoảng trắng giữa các chữ
    public void setTenKH(String tenKH) {
        if (tenKH == null || tenKH.isBlank()) {
            throw new IllegalArgumentException("Tên hành khách không hợp lệ");
        }
//        if (!tenHanhKhach.matches("^[A-Z][a-z]+(\\s[A-Z][a-z]+)*$")) {
//            throw new IllegalArgumentException("Tên hành khách không hợp lệ");
//        }
        this.tenKH = tenKH;
    }

    public String getSoCCCD() {
        return soCCCD;
    }

    public void setSoCCCD(String soCCCD) {
        if (soCCCD == null || soCCCD.isBlank()) {
            throw new IllegalArgumentException("Số CCCD không hợp lệ");
        }
        if (!soCCCD.matches("0[0-9]{11}$")) {
            throw new IllegalArgumentException("Số CCCD không hợp lệ");
        }
        this.soCCCD = soCCCD;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        if (!sdt.matches("0[0-9]{9,10}$")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!email.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+$")) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KhachHang khachHang = (KhachHang) o;
        return Objects.equals(maKH, khachHang.maKH);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maKH);
    }
}
