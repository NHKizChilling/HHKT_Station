package entity;

import java.time.LocalDate;
import java.util.Objects;

public class HanhKhach {
    private String maHanhKhach;
    private String tenHanhKhach;
    private String soCCCD;
    private String sdt;
    private String email;

    public HanhKhach() {
    }

    public HanhKhach(String maHanhKhach) {
        setMaHanhKhach(maHanhKhach);
    }

    public HanhKhach(String maHanhKhach, String tenHanhKhach, String soCCCD, String sdt,
                     String email) {
        this.maHanhKhach = maHanhKhach;
        setTenHanhKhach(tenHanhKhach);
        setSoCCCD(soCCCD);
        setSdt(sdt);
        setEmail(email);
    }

    public String getMaHanhKhach() {
        return maHanhKhach;
    }

    // mã hành khách bao gồm: HK+8 chữ số tăng dần
    public void setMaHanhKhach(String maHanhKhach) {
        if (maHanhKhach == null || maHanhKhach.isBlank()) {
            throw new IllegalArgumentException("Mã hành khách không hợp lệ");
        }
        if (!maHanhKhach.matches("HK[0-9]{8}$")) {
            throw new IllegalArgumentException("Mã hành khách không hợp lệ");
        }
        this.maHanhKhach = maHanhKhach;
    }

    public String getTenHanhKhach() {
        return tenHanhKhach;
    }

    // tên hành khách thì mỗi chứ cái đầu phải ghi hoa và không chứa ký tự đặc biệt, số. phải có khoảng trắng giữa các chữ
    public void setTenHanhKhach(String tenHanhKhach) {
        if (tenHanhKhach == null || tenHanhKhach.isBlank()) {
            throw new IllegalArgumentException("Tên hành khách không hợp lệ");
        }
//        if (!tenHanhKhach.matches("^[A-Z][a-z]+(\\s[A-Z][a-z]+)*$")) {
//            throw new IllegalArgumentException("Tên hành khách không hợp lệ");
//        }
        this.tenHanhKhach = tenHanhKhach;
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
        if (!(o instanceof HanhKhach hanhKhach)) return false;
        return Objects.equals(getMaHanhKhach(), hanhKhach.getMaHanhKhach()) && Objects.equals(getSoCCCD(), hanhKhach.getSoCCCD());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaHanhKhach(), getSoCCCD());
    }
}
