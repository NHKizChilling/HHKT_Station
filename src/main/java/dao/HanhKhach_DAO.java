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
                HanhKhach hanhKhach = getInfo(rs);
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
            stm = con.prepareStatement("insert into HanhKhach(TenHK, SoCCCD, SDT, Email) values(?,?,?,?)");

            stm.setString(1, hk.getTenHanhKhach());
            stm.setString(2, hk.getSoCCCD());
            stm.setString(3, hk.getSdt());
            stm.setString(4, hk.getEmail());

            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean update(HanhKhach hk) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm;
        int n = 0;
        try {
            stm = con.prepareStatement("update HanhKhach set TenHK = ?, SoCCCD = ?, SDT = ?, Email = ? where MaHanhKhach = ?");

            stm.setString(1, hk.getTenHanhKhach());
            stm.setString(2, hk.getSoCCCD());
            stm.setString(3, hk.getSdt());
            stm.setString(4, hk.getEmail());
            stm.setString(5, hk.getMaHanhKhach());

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
            stm = con.prepareStatement("delete from HanhKhach where MaHK = ?");
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
            String sql = "Select * from HanhKhach where MaHK = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, maHanhKhach);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                hk = getInfo(rs);
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
                hk = getInfo(rs);
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
                String email = rs.getString(5);
                hk = new HanhKhach(maHanhKhach, tenHanhKhach, cmnd, sdt, email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hk;
    }

    public HanhKhach getInfo(ResultSet rs) {
        HanhKhach hk = null;
        try {
            String maHanhKhach = rs.getString(1);
            String tenHanhKhach = rs.getString(2);
            String cmnd = rs.getString(3);
            String sdt = rs.getString(4);
            String email = rs.getString(5);

            hk = new HanhKhach(maHanhKhach, tenHanhKhach, cmnd, sdt, email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hk;
    }
}


