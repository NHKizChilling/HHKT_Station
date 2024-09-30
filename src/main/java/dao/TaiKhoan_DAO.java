package dao;

import connectdb.ConnectDB;
import entity.NhanVien;
import entity.TaiKhoan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class TaiKhoan_DAO {

    public TaiKhoan_DAO() {
    }

    public ArrayList<TaiKhoan> getAllTaiKhoan() {
        ArrayList<TaiKhoan> dsTaiKhoan = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM TaiKhoan";
            Statement stm = con.createStatement();

            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan();

                tk.setNhanVien(new NhanVien(rs.getString(1)));
                tk.setMatKhau(rs.getString(2));
                tk.setTrangThaiTK(rs.getString(3));

                dsTaiKhoan.add(tk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsTaiKhoan;
    }

    public boolean create(TaiKhoan tk) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("insert into TaiKhoan values(?,?,?)");

            stm.setString(1, tk.getNhanVien().getMaNhanVien());
            stm.setString(2, tk.getMatKhau());
            stm.setString(3, tk.getTrangThaiTK());

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

    public boolean delete(String maNhanVien, String traThaiTK) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update TaiKhoan set TrangThaiTK = ? where MaNhanVien = ?");

            stm.setString(1, traThaiTK);
            stm.setString(2, maNhanVien);

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

    public boolean update(TaiKhoan tk) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update TaiKhoan set MatKhau = ?, TrangThaiTK = ? where MaNhanVien = ?");

            stm.setString(1, tk.getMatKhau());
            stm.setString(2, tk.getTrangThaiTK());
            stm.setString(3, tk.getNhanVien().getMaNhanVien());

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

    public boolean doiMatKhau(String maNhanVien, String matKhauMoi) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update TaiKhoan set MatKhau = ? where MaNhanVien = ?");

            stm.setString(1, matKhauMoi);
            stm.setString(2, maNhanVien);

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

    public TaiKhoan getTaiKhoanTheoMaNV(String maNhanVien) {
        TaiKhoan tk = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM TaiKhoan WHERE MaNV = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, maNhanVien);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                tk = new TaiKhoan();

                tk.setNhanVien(new NhanVien(rs.getString("MaNV")));
                tk.setMatKhau(rs.getString("MatKhau"));
                tk.setTrangThaiTK(rs.getString("TrangThaiTK"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tk;
    }

}
