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
            String sql = "Update ChiTietLichTrinh set TrangThai = ?, GiaCho = ? where MaSoCho = ? and MaLichTrinh = ?";
            stm = con.prepareStatement(sql);
            stm.setString(3, ctlt.getChoNgoi().getMaChoNgoi());
            stm.setString(4, ctlt.getLichTrinh().getMaLichTrinh());
            stm.setBoolean(1, ctlt.isTrangThai());
            stm.setDouble(2, ctlt.getGiaCho());

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

    public boolean updateCTLT(ChiTietLichTrinh ctlt, boolean trangThai) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;

        int n = 0;
        try {
            String sql = "Update ChiTietLichTrinh set TrangThai = ? where MaSoCho = ? and MaLichTrinh = ?";
            stm = con.prepareStatement(sql);
            stm.setBoolean(1, trangThai);
            stm.setString(2, ctlt.getChoNgoi().getMaChoNgoi());
            stm.setString(3, ctlt.getLichTrinh().getMaLichTrinh());

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

    public boolean delete(String maLT) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;

        int n = 0;
        try {
            String sql = "Delete from ChiTietLichTrinh where MaLichTrinh = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, maLT);

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

    public ChiTietLichTrinh getCTLTTheoCN(String maLichTrinh, String maChoNgoi) {
        ChiTietLichTrinh ctlt = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            PreparedStatement stm;
            String sql = "Select * from ChiTietLichTrinh where MaLichTrinh = ? and MaSoCho = ?";
            stm = con.prepareStatement(sql);
            stm.setString(2, maChoNgoi);
            stm.setString(1, maLichTrinh);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                ctlt = getInfo(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ctlt;
    }

    public boolean getTrangThaiCN(String maLichTrinh, String maCho) {
        boolean trangThai = false;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            PreparedStatement stm;
            String sql = "Select TrangThai from ChiTietLichTrinh where MaLichTrinh = ? and MaSoCho = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, maLichTrinh);
            stm.setString(2, maCho);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                trangThai = rs.getBoolean(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trangThai;
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
            ChoNgoi choNgoi = new ChoNgoi(rs.getString(1));
            LichTrinh lichTrinh = new LichTrinh(rs.getString(2));
            boolean trangThai = rs.getBoolean(3);
            double giaCho = rs.getDouble(4);

            ctlt = new ChiTietLichTrinh(choNgoi, lichTrinh, trangThai, giaCho);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ctlt;
    }
}
