package dao;

import connectdb.ConnectDB;
import entity.HanhKhach;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class HanhKhach_DAO {

    public HanhKhach_DAO() {
    }

    public ArrayList<HanhKhach> getAllHanhKhach() {
        ArrayList<HanhKhach> listHanhKhach = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM HanhKhach";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String maHanhKhach = rs.getString(1);
                String tenHanhKhach = rs.getString(2);
                String cmnd = rs.getString(3);
                String sdt = rs.getString(4);
                LocalDate ngaySinh = rs.getDate(5).toLocalDate();
                boolean gioiTinh = rs.getBoolean(6);
                String email = rs.getString(7);

                HanhKhach hanhKhach = new HanhKhach(maHanhKhach, tenHanhKhach, cmnd, sdt, ngaySinh, gioiTinh, email);
                listHanhKhach.add(hanhKhach);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listHanhKhach;
    }

    public boolean create(HanhKhach hk) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm;
        int n = 0;
        try {
            stm = con.prepareStatement("insert into HanhKhach values(?,?,?,?,?,?,?)");

            stm.setString(1, hk.getMaHanhKhach());
            stm.setString(2, hk.getTenHanhKhach());
            stm.setString(3, hk.getSoCCCD());
            stm.setString(4, hk.getSdt());
            stm.setDate(5, java.sql.Date.valueOf(hk.getNgaySinh()));
            stm.setBoolean(6, hk.isGioiTinh());
            stm.setString(7, hk.getEmail());

            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return n > 0;
    }

    public boolean update(HanhKhach hk) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm;
        int n = 0;
        try {
            stm = con.prepareStatement("update HanhKhach set TenHanhKhach = ?, SoCCCD = ?, SDT = ?, NgaySinh = ?, GioiTinh = ?, Email = ? where MaHanhKhach = ?");

            stm.setString(1, hk.getTenHanhKhach());
            stm.setString(2, hk.getSoCCCD());
            stm.setString(3, hk.getSdt());
            stm.setDate(4, java.sql.Date.valueOf(hk.getNgaySinh()));
            stm.setBoolean(5, hk.isGioiTinh());
            stm.setString(6, hk.getEmail());
            stm.setString(7, hk.getMaHanhKhach());

            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return n > 0;
    }

    public boolean delete(String maHanhKhach) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm;
        int n = 0;
        try {
            stm = con.prepareStatement("delete from HanhKhach where MaHanhKhach = ?");
            stm.setString(1, maHanhKhach);
            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return n > 0;
    }

    public HanhKhach getHanhKhachTheoMa(String maHanhKhach) {
        HanhKhach hk = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from HanhKhach where MaHanhKhach = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, maHanhKhach);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String tenHanhKhach = rs.getString(2);
                String cmnd = rs.getString(3);
                String sdt = rs.getString(4);
                LocalDate ngaySinh = rs.getDate(5).toLocalDate();
                boolean gioiTinh = rs.getBoolean(6);
                String email = rs.getString(7);
                hk = new HanhKhach(maHanhKhach, tenHanhKhach, cmnd, sdt, ngaySinh, gioiTinh, email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hk;
    }

    public HanhKhach getHanhKhachTheoCCCD(String soCCCD) {
        HanhKhach hk = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from HanhKhach where SoCCCD = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, soCCCD);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String maHanhKhach = rs.getString(1);
                String tenHanhKhach = rs.getString(2);
                String sdt = rs.getString(4);
                LocalDate ngaySinh = rs.getDate(5).toLocalDate();
                boolean gioiTinh = rs.getBoolean(6);
                String email = rs.getString(7);
                hk = new HanhKhach(maHanhKhach, tenHanhKhach, soCCCD, sdt, ngaySinh, gioiTinh, email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hk;
    }

    public HanhKhach getKhachHangTheoSDT(String sdt) {
        HanhKhach hk = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from HanhKhach where SDT = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, sdt);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String maHanhKhach = rs.getString(1);
                String tenHanhKhach = rs.getString(2);
                String cmnd = rs.getString(3);
                LocalDate ngaySinh = rs.getDate(5).toLocalDate();
                boolean gioiTinh = rs.getBoolean(6);
                String email = rs.getString(7);
                hk = new HanhKhach(maHanhKhach, tenHanhKhach, cmnd, sdt, ngaySinh, gioiTinh, email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hk;
    }

}


