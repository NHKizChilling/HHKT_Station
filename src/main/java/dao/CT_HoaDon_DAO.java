package dao;

import connectdb.ConnectDB;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.Ve;

import java.sql.*;
import java.util.ArrayList;

public class CT_HoaDon_DAO {

    public CT_HoaDon_DAO() {
    }

    public ArrayList<ChiTietHoaDon> getAllCT_HoaDon() {
        ArrayList<ChiTietHoaDon> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "Select * from ChiTietHoaDon";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {
                HoaDon hoaDon = new HoaDon(rs.getString(1));
                Ve ve = new Ve(rs.getString(2));
                int soLuong = rs.getInt(3);
                double giamGia = rs.getDouble(4);

                ChiTietHoaDon ctHoaDon = new ChiTietHoaDon(hoaDon, ve, soLuong, giamGia);
                list.add(ctHoaDon);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(ChiTietHoaDon cthd) {
        PreparedStatement stm = null;
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        int n = 0;
        try {
            stm = con.prepareStatement("insert into ChiTietHoaDon values(?,?,?,?)");

            stm.setString(1, cthd.getHoaDon().getMaHoaDon());
            stm.setString(2, cthd.getVe().getMaVe());
            stm.setDouble(3, cthd.getGiaVe());
            stm.setDouble(4, cthd.getGiaGiam());

            n = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return n>0;
    }

    public boolean update(ChiTietHoaDon cthd) {
        PreparedStatement stm = null;
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        int n = 0;
        try {
            stm = con.prepareStatement("update ChiTietHoaDon set GiaVe = ?, GiaGiam = ? where MaHD = ? and MaVe = ?");

            stm.setDouble(1, cthd.getGiaVe());
            stm.setDouble(2, cthd.getGiaGiam());
            stm.setString(3, cthd.getHoaDon().getMaHoaDon());
            stm.setString(4, cthd.getVe().getMaVe());

            n = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return n > 0;
    }

    public boolean delete(ChiTietHoaDon cthd) {
        PreparedStatement stm = null;
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        int n = 0;
        try {
            stm = con.prepareStatement("delete from ChiTietHoaDon where MaHD = ? and MaVe = ?");

            stm.setString(1, cthd.getHoaDon().getMaHoaDon());
            stm.setString(2, cthd.getVe().getMaVe());

            n = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return n > 0;
    }

    public ChiTietHoaDon getCT_HoaDon(String maHD, String maVe) {
        ChiTietHoaDon cthd = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "Select * from ChiTietHoaDon where MaHD = ? and MaVe = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, maHD);
            st.setString(2, maVe);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                HoaDon hoaDon = new HoaDon(rs.getString(1));
                Ve ve = new Ve(rs.getString(2));
                double giaVe = rs.getDouble(3);
                double giaGiam = rs.getDouble(4);

                cthd = new ChiTietHoaDon(hoaDon, ve, giaVe, giaGiam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cthd;
    }

    public ChiTietHoaDon getCT_HoaDonTheoMaVe(String maVe) {
        ChiTietHoaDon cthd = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "Select * from ChiTietHoaDon where MaVe = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, maVe);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                HoaDon hoaDon = new HoaDon(rs.getString(1));
                Ve ve = new Ve(rs.getString(2));
                double giaVe = rs.getDouble(3);
                double giaGiam = rs.getDouble(4);

                cthd = new ChiTietHoaDon(hoaDon, ve, giaGiam, giaVe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cthd;
    }

    public ArrayList<ChiTietHoaDon> getCT_HoaDon(String maHD) {
        ArrayList<ChiTietHoaDon> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "Select * from ChiTietHoaDon where MaHD = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, maHD);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                HoaDon hoaDon = new HoaDon(rs.getString(1));
                Ve ve = new Ve(rs.getString(2));
                double giaVe = rs.getDouble(3);
                double giaGiam = rs.getDouble(4);

                ChiTietHoaDon cthd = new ChiTietHoaDon(hoaDon, ve, giaGiam, giaVe);
                list.add(cthd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

