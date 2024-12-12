package dao;

import connectdb.ConnectDB;
import entity.KhachHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class KhachHang_DAO {

    public KhachHang_DAO() {
    }

    public ArrayList<KhachHang> getAllKhachHang() {
        ArrayList<KhachHang> dsKhachHang = new ArrayList<>();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            ConnectDB.getInstance();
            con = ConnectDB.getConnection();
            String sql = "SELECT * FROM KhachHang";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                KhachHang khachHang = getInfo(rs);
                dsKhachHang.add(khachHang);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dsKhachHang;
    }

    public boolean create(KhachHang kh) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm;
        int n = 0;
        try {
            stm = con.prepareStatement("insert into KhachHang(TenKH, SoCCCD, SDT, Email) values(?,?,?,?)");

            stm.setString(1, kh.getTenKH());
            stm.setString(2, kh.getSoCCCD());
            stm.setString(3, kh.getSdt());
            stm.setString(4, kh.getEmail());

            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean update(KhachHang kh) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm;
        int n = 0;
        try {
            stm = con.prepareStatement("update KhachHang set TenKH = ?, SoCCCD = ?, SDT = ?, Email = ? where MaKH = ?");

            stm.setString(1, kh.getTenKH());
            stm.setString(2, kh.getSoCCCD());
            stm.setString(3, kh.getSdt());
            stm.setString(4, kh.getEmail());
            stm.setString(5, kh.getMaKH());

            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean delete(String maKH) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm;
        int n = 0;
        try {
            stm = con.prepareStatement("delete from KhachHang where MaKH = ?");
            stm.setString(1, maKH);
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

    public KhachHang getKhachHangTheoMaKH(String maKH) {
        KhachHang kh = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from KhachHang where maKH = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, maKH);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                kh = getInfo(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kh;
    }

    public KhachHang getKHTheoCCCD(String soCCCD) {
        KhachHang kh = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from KhachHang where SoCCCD = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, soCCCD);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                kh = getInfo(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kh;
    }

    public KhachHang getKhachHangTheoSDT(String sdt) {
        KhachHang kh = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from KhachHang where SDT = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, sdt);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String maKH = rs.getString(1);
                String tenKH = rs.getString(2);
                String cmnd = rs.getString(3);
                String email = rs.getString(5);
                kh = new KhachHang(maKH, tenKH, cmnd, sdt, email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kh;
    }

    public KhachHang getInfo(ResultSet rs) {
        KhachHang kh = null;
        try {
            String maKH = rs.getString(1);
            String tenKH = rs.getString(2);
            String cmnd = rs.getString(3);
            String sdt = rs.getString(4);
            String email = rs.getString(5);

            kh = new KhachHang(maKH, tenKH, cmnd, sdt, email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kh;
    }
}


