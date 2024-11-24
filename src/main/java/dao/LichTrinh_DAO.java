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
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                lichTrinh = getInfo(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lichTrinh;
    }

<<<<<<< Updated upstream
=======
    public ArrayList<LichTrinh> getDSLichTrinhTheoTrangThai(boolean trangThai) {
        ArrayList<LichTrinh> dslt = new ArrayList<>();
        PreparedStatement stm = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from LichTrinh where TrangThai = ?";
            stm = con.prepareStatement(sql);
            stm.setBoolean(1, trangThai);
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

    public ArrayList<LichTrinh> traCuuDSLichTrinh(String MaGaDi, String MaGaDen) {
        ArrayList<LichTrinh> dslt = new ArrayList<>();
        PreparedStatement stm = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from LichTrinh where MaGaDi = ? and MaGaDen = ? and TrangThai = 1";
            stm = con.prepareStatement(sql);
            stm.setString(1, MaGaDi);
            stm.setString(2, MaGaDen);
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

>>>>>>> Stashed changes
    public ArrayList<LichTrinh> traCuuDSLichTrinh(String MaGaDi, String MaGaDen, LocalDate ngayDi) {
        ArrayList<LichTrinh> dslt = new ArrayList<>();
        PreparedStatement stm = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from LichTrinh where MaGaDi = ? and MaGaDen = ? and YEAR(ThoiGianKhoiHanh) = ? and MONTH(ThoiGianKhoiHanh) = ? and DAY(ThoiGianKhoiHanh) = ? and TrangThai = 1";
            stm = con.prepareStatement(sql);
            stm.setString(1, MaGaDi);
            stm.setString(2, MaGaDen);
            stm.setInt(3, ngayDi.getYear());
            stm.setInt(4, ngayDi.getMonth().getValue());
            stm.setInt(5, ngayDi.getDayOfMonth());
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

    public ArrayList<LichTrinh> traCuuDSLichTrinhTheoNgay(LocalDate ngayDi) {
        ArrayList<LichTrinh> dslt = new ArrayList<>();
        PreparedStatement stm = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from LichTrinh where MaGaDi = 'SG' and YEAR(ThoiGianKhoiHanh) = ? and MONTH(ThoiGianKhoiHanh) = ? and DAY(ThoiGianKhoiHanh) = ? and TrangThai = 1";
            stm = con.prepareStatement(sql);
            stm.setInt(1, ngayDi.getYear());
            stm.setInt(2, ngayDi.getMonth().getValue());
            stm.setInt(3, ngayDi.getDayOfMonth());
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

    public int getSoLuongChoConTrong(String maLichTrinh) {
        int soLuongChoConTrong = 0;
        PreparedStatement stm = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "select count(*) from LichTrinh a join ChiTietLichTrinh b on a.MaLichTrinh = b.MaLichTrinh join ChoNgoi c on b.MaSoCho = c.MaSoCho where a.MaLichTrinh= ? and b.TrangThai = 1";
            stm = con.prepareStatement(sql);
            stm.setString(1, maLichTrinh);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                soLuongChoConTrong = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soLuongChoConTrong;
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
            stm.setString(2, lichTrinh.getGaDi().getMaGa());
            stm.setString(3, lichTrinh.getGaDen().getMaGa());
            stm.setTimestamp(4, java.sql.Timestamp.valueOf(lichTrinh.getThoiGianKhoiHanh()));
            stm.setTimestamp(5, java.sql.Timestamp.valueOf(lichTrinh.getThoiGianDuKienDen()));
            stm.setBoolean(6, lichTrinh.isTinhTrang());
            stm.setString(7, lichTrinh.getMaLichTrinh());

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
            stm.setString(3, lichTrinh.getGaDi().getMaGa());
            stm.setString(4, lichTrinh.getGaDen().getMaGa());
            stm.setTimestamp(5, java.sql.Timestamp.valueOf(lichTrinh.getThoiGianKhoiHanh()));
            stm.setTimestamp(6, java.sql.Timestamp.valueOf(lichTrinh.getThoiGianDuKienDen()));
            stm.setBoolean(7, lichTrinh.isTinhTrang());

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
            Ga gaDi = new Ga(rs.getString(3));
            Ga gaDen = new Ga(rs.getString(4));
            LocalDateTime thoiGianKhoiHanh = rs.getTimestamp(5).toLocalDateTime();
            LocalDateTime thoiGianDuKienDen = rs.getTimestamp(6).toLocalDateTime();
            boolean trangThai = rs.getBoolean(7);

            lichTrinh = new LichTrinh(maLichTrinh, chuyenTau, gaDi, gaDen, thoiGianKhoiHanh, thoiGianDuKienDen, trangThai);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lichTrinh;
    }
}
