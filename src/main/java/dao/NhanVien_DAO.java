package dao;

import connectdb.ConnectDB;
import entity.NhanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class NhanVien_DAO {

    public NhanVien_DAO() {
    }

    public ArrayList<NhanVien> getAll() {
        ArrayList<NhanVien> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from NhanVien";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                NhanVien nv = getInfo(rs);

                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(NhanVien nv) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("insert into NhanVien values(?,?,?,?,?,?,?,?,?)");

            stm.setString(1, nv.getMaNhanVien());
            stm.setString(2, nv.getTenNhanVien());
            stm.setString(3, nv.getSoCCCD());
            stm.setDate(4, java.sql.Date.valueOf(nv.getNgaySinh()));
            stm.setBoolean(5, nv.isGioiTinh());
            stm.setString(6, nv.getSdt());
            stm.setString(7, nv.getEmail());
            stm.setString(8, nv.getChucVu());
            stm.setString(9, nv.getTinhTrangCV());

            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return n > 0;
    }

    public boolean updateTinhTrangCV(String maNV, String tinhTrangCV) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update NhanVien set TinhTrangCV = ? where MaNV = ?");

            stm.setString(1, tinhTrangCV);
            stm.setString(2, maNV);

            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return n > 0;
    }

    public ArrayList<NhanVien> getDSQuanLy() {
        ArrayList<NhanVien> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from NhanVien where ChucVu = 'Quản lý'";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                NhanVien nv = getInfo(rs);

                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<NhanVien> getDSNhanVien() {
        ArrayList<NhanVien> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from NhanVien where ChucVu = 'Nhân viên'";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                NhanVien nv = getInfo(rs);

                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateInfo(NhanVien nv) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update NhanVien set HoTenNV = ?, SoCCCD = ?, NgaySinh = ?, GioiTinh = ?, SDT = ?, Email = ?, ChucVu = ?, TinhTrangCV = ? where MaNhanVien = ?");

            stm.setString(1, nv.getTenNhanVien());
            stm.setString(2, nv.getSoCCCD());
            stm.setDate(3, java.sql.Date.valueOf(nv.getNgaySinh()));
            stm.setBoolean(4, nv.isGioiTinh());
            stm.setString(5, nv.getSdt());
            stm.setString(6, nv.getEmail());
            stm.setString(7, nv.getChucVu());
            stm.setString(8, nv.getTinhTrangCV());
            stm.setString(9, nv.getMaNhanVien());

            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return n > 0;
    }

    public NhanVien getNhanVien(String maNV) {
        NhanVien nv = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from NhanVien where MaNhanVien = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, maNV);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                nv = getInfo(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nv;
    }

    public NhanVien getInfo(ResultSet rs) {
        NhanVien nv = null;
        try {
            String maNV = rs.getString(1);
            String tenNV = rs.getString(2);
            String soCCCD = rs.getString(3);
            LocalDate ngaySinh = rs.getDate(4).toLocalDate();
            boolean gioiTinh = rs.getBoolean(5);
            String sdt = rs.getString(6);
            String email = rs.getString(7);
            String chucVu = rs.getString(8);
            String tinhTrangCV = rs.getString(9);

            nv = new NhanVien(maNV, tenNV, soCCCD, ngaySinh, gioiTinh, sdt, chucVu, email, tinhTrangCV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nv;
    }
}
