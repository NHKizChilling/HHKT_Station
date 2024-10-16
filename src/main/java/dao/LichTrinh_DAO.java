package dao;

import connectdb.ConnectDB;
import entity.ChuyenTau;
import entity.Ga;
import entity.LichTrinh;

import java.sql.*;
import java.time.LocalDate;
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
                LichTrinh lichTrinh = getInfo(rs);

                list.add(lichTrinh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public LichTrinh getLichTrinhTheoID(String maLichTrinh) {
        LichTrinh lichTrinh = null;
        PreparedStatement stm = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from LichTrinh where MaLichTrinh = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, maLichTrinh);
            ResultSet rs = stm.executeQuery(sql);
            if (rs.next()) {
                lichTrinh = getInfo(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lichTrinh;
    }

    public ArrayList<LichTrinh> traCuuDSLichTrinh(String MaGaDi, String MaGaDen, LocalDate ngayDi) {
        ArrayList<LichTrinh> dslt = new ArrayList<>();
        PreparedStatement stm = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from LichTrinh where MaGaDen = ? and YEAR(ThoiGianKhoiHanh) = ? and MONTH(ThoiGianKhoiHanh) = ? and DAY(ThoiGianKhoiHanh) = ? and TrangThai = 1";
            stm = con.prepareStatement(sql);
            stm.setString(1, MaGaDen);
            stm.setInt(2, ngayDi.getYear());
            stm.setInt(3, ngayDi.getMonth().getValue());
            stm.setInt(4, ngayDi.getDayOfMonth());
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LichTrinh lt = getInfo(rs);
                dslt.add(lt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dslt;
    }

    public boolean updateTrangThaiChuyenTau(String maLichTrinh, boolean trangThai) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        int n = 0;
        PreparedStatement stm = null;
        try {
            String sql = "update LichTrinh set TrangThai = ? where MaLichTrinh = ?";
            stm = con.prepareStatement(sql);
            stm.setBoolean(1, trangThai);
            stm.setString(2, maLichTrinh);

            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean updateTrangThaiCT(boolean trangThai) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        int n = 0;
        PreparedStatement stm = null;
        try {
            String sql = "update LichTrinh set TrangThai = ? where ThoiGianKhoiHanh < ?";
            stm = con.prepareStatement(sql);
            stm.setBoolean(1, trangThai);
            stm.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

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
            String sql = "update LichTrinh set SoHieuTau = ?, MaGaDi = ?, MaGaDen = ?, ThoiGianKhoiHanh = ?, ThoiGianDuKienDen = ?, TrangThai = ? where MaLichTrinh = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, lichTrinh.getChuyenTau().getSoHieutau());
            stm.setString(2, lichTrinh.getGaDen().getMaGa());
            stm.setTimestamp(3, java.sql.Timestamp.valueOf(lichTrinh.getThoiGianKhoiHanh()));
            stm.setTimestamp(4, java.sql.Timestamp.valueOf(lichTrinh.getThoiGianDuKienDen()));
            stm.setBoolean(5, lichTrinh.isTinhTrang());
            stm.setString(6, lichTrinh.getMaLichTrinh());

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
        PreparedStatement stm;
        try {
            String sql = "insert into LichTrinh values(?,?,?,?,?,?,?)";
            stm = con.prepareStatement(sql);
            stm.setString(1, lichTrinh.getMaLichTrinh());
            stm.setString(2, lichTrinh.getChuyenTau().getSoHieutau());
            stm.setString(3, lichTrinh.getGaDen().getMaGa());
            stm.setTimestamp(4, java.sql.Timestamp.valueOf(lichTrinh.getThoiGianKhoiHanh()));
            stm.setTimestamp(5, java.sql.Timestamp.valueOf(lichTrinh.getThoiGianDuKienDen()));
            stm.setBoolean(6, lichTrinh.isTinhTrang());

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
            String sql = "Select * from LichTrinh where MaLichTrinh = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, maLichTrinh);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                lichTrinh = getInfo(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lichTrinh;
    }

    public LichTrinh getInfo(ResultSet rs) {
        LichTrinh lichTrinh = null;
        try {
            String maLichTrinh = rs.getString(1);
            ChuyenTau chuyenTau = new ChuyenTau(rs.getString(2));
            Ga gaDen = new Ga(rs.getString(3));
            LocalDateTime thoiGianKhoiHanh = rs.getTimestamp(4).toLocalDateTime();
            LocalDateTime thoiGianDuKienDen = rs.getTimestamp(5).toLocalDateTime();
            boolean trangThai = rs.getBoolean(6);

            lichTrinh = new LichTrinh(maLichTrinh, chuyenTau, gaDen, thoiGianKhoiHanh, thoiGianDuKienDen, trangThai);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lichTrinh;
    }
}
