package dao;

import connectdb.ConnectDB;
import entity.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CT_LichTrinh_DAO {
    public CT_LichTrinh_DAO() {
    }

    public ArrayList<ChiTietLichTrinh> getAllChiTietLichTrinh() {
        ArrayList<ChiTietLichTrinh> list = new ArrayList<>();

        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from ChiTietLichTrinh";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                ChiTietLichTrinh ctlt = getInfo(rs);

                list.add(ctlt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(ChiTietLichTrinh ctlt) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm;

        int n = 0;
        try {
            String sql = "Insert into ChiTietLichTrinh values(?, ?, ?, ?)";
            stm = con.prepareStatement(sql);
            stm.setString(1, ctlt.getChoNgoi().getMaChoNgoi());
            stm.setString(2, ctlt.getLichTrinh().getMaLichTrinh());
            stm.setBoolean(3, ctlt.isTrangThai());
            stm.setDouble(4, ctlt.getGiaCho());

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

    public boolean update(ChiTietLichTrinh ctlt) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;

        int n = 0;
        try {
            String sql = "Update ChiTietLichTrinh set MaChoNgoi = ?, MaLichTrinh = ?, TrangThai = ?, GiaCho = ? where MaVe = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, ctlt.getChoNgoi().getMaChoNgoi());
            stm.setString(2, ctlt.getLichTrinh().getMaLichTrinh());
            stm.setBoolean(3, ctlt.isTrangThai());
            stm.setDouble(4, ctlt.getGiaCho());

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

    public boolean delete(String maVe) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;

        int n = 0;
        try {
            String sql = "Delete from ChiTietLichTrinh where MaVe = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, maVe);

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

    public ArrayList<ChiTietLichTrinh> getCtltTheoTrangThai(boolean trangThai) {
        ArrayList<ChiTietLichTrinh> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            PreparedStatement stm;
            String sql = "Select * from ChiTietLichTrinh where TrangThai = ?";
            stm = con.prepareStatement(sql);
            stm.setBoolean(1, trangThai);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ChiTietLichTrinh ctlt = getInfo(rs);

                list.add(ctlt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<ChiTietLichTrinh> getCtltTheoMaLichTrinh(String maLichTrinh) {
        ArrayList<ChiTietLichTrinh> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            PreparedStatement stm;
            String sql = "Select * from ChiTietLichTrinh where MaLichTrinh = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, maLichTrinh);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ChiTietLichTrinh ctlt = getInfo(rs);

                list.add(ctlt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ChiTietLichTrinh getInfo(ResultSet rs) {
        ChiTietLichTrinh ctlt = null;
        try {
            Ve ve = new Ve(rs.getString(1));
            ChoNgoi choNgoi = new ChoNgoi(rs.getString(2));
            LichTrinh lichTrinh = new LichTrinh(rs.getString(3));
            boolean trangThai = rs.getBoolean(4);
            double giaCho = rs.getDouble(5);

            ctlt = new ChiTietLichTrinh(choNgoi, lichTrinh, trangThai, giaCho);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ctlt;
    }
}
