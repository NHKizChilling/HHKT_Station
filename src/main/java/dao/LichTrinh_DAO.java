package dao;

import connectdb.ConnectDB;
import entity.ChuyenTau;
import entity.Ga;
import entity.LichTrinh;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class LichTrinh_DAO {

    public LichTrinh_DAO() {
    }

    public ArrayList<LichTrinh> getAll() {
        ArrayList<LichTrinh> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from LichTrinh";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String maLichTrinh = rs.getString(1);
                ChuyenTau chuyenTau = new ChuyenTau(rs.getString(2));
                Ga gaDi = new Ga(rs.getString(3));
                Ga gaDen = new Ga(rs.getString(4));
                int khoangCach = rs.getInt(5);
                LocalDateTime thoiGianKhoiHanh = rs.getTimestamp(6).toLocalDateTime();
                LocalDateTime thoiGianDuKienDen = rs.getTimestamp(7).toLocalDateTime();
                boolean trangThai = rs.getBoolean(8);

                LichTrinh lichTrinh = new LichTrinh(maLichTrinh, chuyenTau, gaDi, gaDen, khoangCach, thoiGianKhoiHanh, thoiGianDuKienDen, trangThai);

                list.add(lichTrinh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public LichTrinh getLichTrinhTheoID(String maLichTrinh) {
        LichTrinh lichTrinh = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from LichTrinh where maLichTrinh = ?";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if (rs.next()) {
                ChuyenTau chuyenTau = new ChuyenTau(rs.getString(2));
                Ga gaDi = new Ga(rs.getString(3));
                Ga gaDen = new Ga(rs.getString(4));
                int khoangCach = rs.getInt(5);
                LocalDateTime thoiGianKhoiHanh = rs.getTimestamp(6).toLocalDateTime();
                LocalDateTime thoiGianDuKienDen = rs.getTimestamp(7).toLocalDateTime();
                boolean trangThai = rs.getBoolean(8);

                lichTrinh = new LichTrinh(maLichTrinh, chuyenTau, gaDi, gaDen, khoangCach, thoiGianKhoiHanh, thoiGianDuKienDen, trangThai);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lichTrinh;
    }

    public boolean updateTrangThaiChuyenTau(String maLichTrinh, boolean trangThai) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        int n = 0;
        PreparedStatement stm = null;
        try {
            String sql = "update LichTrinh set trangThai = ? where maLichTrinh = ?";
            stm = con.prepareStatement(sql);
            stm.setBoolean(1, trangThai);
            stm.setString(2, maLichTrinh);

            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean updateInfo(LichTrinh lichTrinh) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        int n = 0;
        PreparedStatement stm = null;
        try {
            String sql = "update LichTrinh set chuyenTau = ?, gaDi = ?, gaDen = ?, khoangCach = ?, thoiGianKhoiHanh = ?, thoiGianDuKienDen = ?, trangThai = ? where maLichTrinh = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, lichTrinh.getChuyenTau().getSoHieutau());
            stm.setString(2, lichTrinh.getGaDi().getMaGa());
            stm.setString(3, lichTrinh.getGaDen().getMaGa());
            stm.setInt(4, lichTrinh.getKhoangCach());
            stm.setTimestamp(5, java.sql.Timestamp.valueOf(lichTrinh.getThoiGianKhoiHanh()));
            stm.setTimestamp(6, java.sql.Timestamp.valueOf(lichTrinh.getThoiGianDuKienDen()));
            stm.setBoolean(7, lichTrinh.isTinhTrang());
            stm.setString(8, lichTrinh.getMaLichTrinh());

            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean create(LichTrinh lichTrinh) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        int n = 0;
        PreparedStatement stm = null;
        try {
            String sql = "insert into LichTrinh values(?,?,?,?,?,?,?,?)";
            stm = con.prepareStatement(sql);
            stm.setString(1, lichTrinh.getMaLichTrinh());
            stm.setString(2, lichTrinh.getChuyenTau().getSoHieutau());
            stm.setString(3, lichTrinh.getGaDi().getMaGa());
            stm.setString(4, lichTrinh.getGaDen().getMaGa());
            stm.setInt(5, lichTrinh.getKhoangCach());
            stm.setTimestamp(6, java.sql.Timestamp.valueOf(lichTrinh.getThoiGianKhoiHanh()));
            stm.setTimestamp(7, java.sql.Timestamp.valueOf(lichTrinh.getThoiGianDuKienDen()));
            stm.setBoolean(8, lichTrinh.isTinhTrang());

            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public LichTrinh getOne(String maLichTrinh) {
        LichTrinh lichTrinh = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from LichTrinh where maLichTrinh = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, maLichTrinh);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                ChuyenTau chuyenTau = new ChuyenTau(rs.getString(2));
                Ga gaDi = new Ga(rs.getString(3));
                Ga gaDen = new Ga(rs.getString(4));
                int khoangCach = rs.getInt(5);
                LocalDateTime thoiGianKhoiHanh = rs.getTimestamp(6).toLocalDateTime();
                LocalDateTime thoiGianDuKienDen = rs.getTimestamp(7).toLocalDateTime();
                boolean trangThai = rs.getBoolean(8);

                lichTrinh = new LichTrinh(maLichTrinh, chuyenTau, gaDi, gaDen, khoangCach, thoiGianKhoiHanh, thoiGianDuKienDen, trangThai);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lichTrinh;
    }
}
