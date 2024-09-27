package dao;

import connectdb.ConnectDB;
import entity.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Ve_DAO {

    public Ve_DAO() {
    }

    public ArrayList<Ve> getAllVe() {
        ArrayList<Ve> list = new ArrayList<>();

        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from Ve";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String maVe = rs.getString(1);
                LoaiVe loaiVe = new LoaiVe(rs.getString(2));
                ChoNgoi choNgoi = new ChoNgoi(rs.getString(3));
                HanhKhach hanhKhach = new HanhKhach(rs.getString(4));
                LichTrinh lichTrinh = new LichTrinh(rs.getString(5));
                String tinhTrangVe = rs.getString(6);
                boolean khuHoi = rs.getBoolean(7);

                Ve ve = new Ve(maVe, loaiVe, choNgoi, hanhKhach, lichTrinh, tinhTrangVe, khuHoi);

                list.add(ve);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Ve getVeTheoID(String maVe) {
        Ve ve = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from Ve where maVe = '" + maVe + "'";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                LoaiVe loaiVe = new LoaiVe(rs.getString(2));
                ChoNgoi choNgoi = new ChoNgoi(rs.getString(3));
                HanhKhach hanhKhach = new HanhKhach(rs.getString(4));
                LichTrinh lichTrinh = new LichTrinh(rs.getString(5));
                String tinhTrangVe = rs.getString(6);
                boolean khuHoi = rs.getBoolean(7);

                ve = new Ve(maVe, loaiVe, choNgoi, hanhKhach, lichTrinh, tinhTrangVe, khuHoi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ve;
    }

    public boolean create(Ve ve) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            String sql = "Insert into Ve values(?,?,?,?,?,?,?)";
            stm = con.prepareStatement(sql);
            stm.setString(1, ve.getMaVe());
            stm.setString(2, ve.getLoaiVe().getMaLoaiVe());
            stm.setString(3, ve.getChoNgoi().getMaChoNgoi());
            stm.setString(4, ve.getHanhKhach().getMaHanhKhach());
            stm.setString(5, ve.getLichTrinh().getMaLichTrinh());
            stm.setString(6, ve.getTinhTrangVe());
            stm.setBoolean(7, ve.isKhuHoi());

            n = stm.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

    public boolean update(Ve ve) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update Ve set MaLoaiVe = ?, MaChoNgoi = ?, MaHanhKhach = ?, MaLichTrinh = ?, TinhTrangVe = ? where MaVe = ?");

            stm.setString(1, ve.getLoaiVe().getMaLoaiVe());
            stm.setString(2, ve.getChoNgoi().getMaChoNgoi());
            stm.setString(3, ve.getHanhKhach().getMaHanhKhach());
            stm.setString(4, ve.getLichTrinh().getMaLichTrinh());
            stm.setString(5, ve.getTinhTrangVe());
            stm.setString(6, ve.getMaVe());

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

    public boolean updateTinhTrangVe(String maVe, String tinhTrangVe) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update Ve set TinhTrangVe = ? where MaVe = ?");

            stm.setString(1, tinhTrangVe);
            stm.setString(2, maVe);

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

    public ArrayList<Ve> getVeTheoTinhTrang(String tinhTrangVe) {
        ArrayList<Ve> list = new ArrayList<>();

        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from Ve where TinhTrangVe = '" + tinhTrangVe + "'";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String maVe = rs.getString(1);
                LoaiVe loaiVe = new LoaiVe(rs.getString(2));
                ChoNgoi choNgoi = new ChoNgoi(rs.getString(3));
                HanhKhach hanhKhach = new HanhKhach(rs.getString(4));
                LichTrinh lichTrinh = new LichTrinh(rs.getString(5));
                String tinhTrang = rs.getString(6);
                boolean khuHoi = rs.getBoolean(7);

                Ve ve = new Ve(maVe, loaiVe, choNgoi, hanhKhach, lichTrinh, tinhTrang, khuHoi);

                list.add(ve);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
