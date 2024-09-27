package entity;

import java.util.Objects;

public class TaiKhoan {
    private NhanVien nhanVien;
    private String matKhau;
    private String trangThaiTK;

    public TaiKhoan() {
    }

    public TaiKhoan(NhanVien nhanVien, String matKhau, String trangThaiTK) {
        setNhanVien(nhanVien);
        setMatKhau(matKhau);
        setTrangThaiTK(trangThaiTK);
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        if (nhanVien == null) {
            throw new IllegalArgumentException("Nhân viên không hợp lệ");
        }
        this.nhanVien = nhanVien;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        if (matKhau == null || matKhau.isBlank()) {
            throw new IllegalArgumentException("Mật khẩu không hợp lệ");
        }
        this.matKhau = matKhau;
    }


    public String getTrangThaiTK() {
        return trangThaiTK;
    }

    // trạng thái tài khoản: đang hoạt động, tạm ngưng hoạt động, đã ngưng hoạt động
    public void setTrangThaiTK(String trangThaiTK) {
        if (trangThaiTK == null || trangThaiTK.isBlank()) {
            throw new IllegalArgumentException("Trạng thái tài khoản không hợp lệ");
        }
        if (!trangThaiTK.equals("đang hoạt động") && !trangThaiTK.equals("tạm ngưng hoạt động") && !trangThaiTK.equals("đã ngưng hoạt động")) {
            throw new IllegalArgumentException("Trạng thái tài khoản không hợp lệ");
        }
        this.trangThaiTK = trangThaiTK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaiKhoan taiKhoan)) return false;
        return Objects.equals(getNhanVien(), taiKhoan.getNhanVien());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getNhanVien());
    }
}
