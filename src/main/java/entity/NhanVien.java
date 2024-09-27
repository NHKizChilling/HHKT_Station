package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class NhanVien {
    private String maNhanVien;
    private String tenNhanVien;
    private String soCCCD;
    private LocalDate ngaySinh;
    private boolean gioiTinh;
    private String sdt;
    private String chucVu;
    private String email;
    private String tinhTrangCV;

    public NhanVien() {
    }

    public NhanVien(String maNhanVien) {
        setMaNhanVien(maNhanVien);
    }

    public NhanVien(String maNhanVien, String tenNhanVien, String soCCCD, LocalDate ngaySinh,
                    boolean gioiTinh, String sdt, String chucVu, String email, String tinhTrangCV) {
        setMaNhanVien(maNhanVien);
        setTenNhanVien(tenNhanVien);
        setSoCCCD(soCCCD);
        setNgaySinh(ngaySinh);
        setGioiTinh(gioiTinh);
        setSdt(sdt);
        setChucVu(chucVu);
        setEmail(email);
        setTinhTrangCV(tinhTrangCV);
    }

    public NhanVien(ArrayList<NhanVien> dsNhanVien, String tenNhanVien, String soCCCD, LocalDate ngaySinh,
                    boolean gioiTinh, String sdt, String chucVu, String email, String tinhTrangCV) {
        setTenNhanVien(tenNhanVien);
        setSoCCCD(soCCCD);
        setNgaySinh(ngaySinh);
        setGioiTinh(gioiTinh);
        setSdt(sdt);
        setChucVu(chucVu);
        setEmail(email);
        setTinhTrangCV(tinhTrangCV);
        sinhMa(dsNhanVien);
    }

    public void sinhMa(ArrayList<NhanVien> list) {
        // mã nhân viên bao gồm: NV+ngày sinh nhân viên+4 chũ số tăng dần
        String ma = "NV" + this.ngaySinh.toString().replace("-", "");

        int num = list.size() + 1;
        if (num < 10) {
            ma += "000" + num;
        } else if (num < 100) {
            ma += "00" + num;
        } else if (num < 1000) {
            ma += "0" + num;
        } else {
            ma += num;
        }
        setMaNhanVien(ma);
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        if (ngaySinh.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày sinh không hợp lệ");
        }
        this.ngaySinh = ngaySinh;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        // tự sinh mã nhân viên
        if (maNhanVien == null || maNhanVien.isBlank()) {
            throw new IllegalArgumentException("Mã nhân viên không hợp lệ");
        }
        // mã nhân viên bao gồm: NV+ngày sinh nhân viên+4 chũ số tăng dần
        if (!maNhanVien.matches("NV[0-9]{8}$")) {
            throw new IllegalArgumentException("Mã nhân viên không hợp lệ");
        }

        this.maNhanVien = maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    // tên nhân viên thì mỗi chứ cái đầu phải ghi hoa và không chứa ký tự đặc biệt, số. phải có khoảng trắng giữa các chữ
    public void setTenNhanVien(String tenNhanVien) {
        if (tenNhanVien == null || tenNhanVien.isBlank()) {
            throw new IllegalArgumentException("Tên nhân viên không được để trống");
        }
        if (!tenNhanVien.matches("^[A-Z][a-z]+(\\s[A-Z][a-z]+)*$")) {
            throw new IllegalArgumentException("Tên nhân viên không hợp lệ");
        }
        this.tenNhanVien = tenNhanVien;
    }

    public String getSoCCCD() {
        return soCCCD;
    }

    public void setSoCCCD(String soCCCD) {
        if (soCCCD == null || soCCCD.isBlank()) {
            throw new IllegalArgumentException("Số CCCD không được để trống");
        }
        if (!soCCCD.matches("0[0-9]{11}$")) {
            throw new IllegalArgumentException("Số CCCD phải có 12 chữ số");
        }
        this.soCCCD = soCCCD;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        if (sdt == null || sdt.isBlank()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        }
        if (!sdt.matches("0[0-9]{9,10}$")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        this.sdt = sdt;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        if (chucVu == null || chucVu.isBlank()) {
            throw new IllegalArgumentException("Chức vụ không được để trống");
        }
        if (!chucVu.equals("Nhân viên") && !chucVu.equals("Quản lý")) {
            throw new IllegalArgumentException("Chức vụ không hợp lệ");
        }
        this.chucVu = chucVu;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }
        this.email = email;
    }
    public String getTinhTrangCV() {
        return tinhTrangCV;
    }

    public void setTinhTrangCV(String tinhTrangCV) {
        if (tinhTrangCV == null || tinhTrangCV.isBlank()) {
            throw new IllegalArgumentException("Tình trạng công việc không được để trống");
        }
        // gồm 3 trạng thái: "Đang làm việc", "Đã nghỉ việc", "Tạm nghỉ"
        if (!tinhTrangCV.equals("Đang làm việc") && !tinhTrangCV.equals("Đã nghỉ việc") && !tinhTrangCV.equals("Tạm nghỉ")) {
            throw new IllegalArgumentException("Tình trạng công việc không hợp lệ");
        }
        this.tinhTrangCV = tinhTrangCV;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NhanVien nhanVien)) return false;
        return Objects.equals(getMaNhanVien(), nhanVien.getMaNhanVien()) && Objects.equals(getSoCCCD(), nhanVien.getSoCCCD());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaNhanVien(), getSoCCCD());
    }
}
