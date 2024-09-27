package dao;

import connectdb.ConnectDB;
import entity.ChoNgoi;
import entity.Toa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ChoNgoi_DAO {

    public ChoNgoi_DAO() {
    }

    public ArrayList<ChoNgoi> getAllChoNgoi() {
        ArrayList<ChoNgoi> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from ChoNgoi";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String maChoNgoi = rs.getString(1);
                Toa toa = new Toa(rs.getString(2));
                int tang = rs.getInt(3);
                int khoang = rs.getInt(4);
                String trangThai = rs.getString(5);
                double giaCho = rs.getDouble(6);

                ChoNgoi choNgoi = new ChoNgoi(maChoNgoi, toa, tang, khoang, trangThai, giaCho);

                list.add(choNgoi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<ChoNgoi> getChoNgoiTheoToa(String maToa) {
        ArrayList<ChoNgoi> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from ChoNgoi where maToa = '" + maToa + "'";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String maChoNgoi = rs.getString(1);
                Toa toa = new Toa(rs.getString(2));
                int tang = rs.getInt(3);
                int khoang = rs.getInt(4);
                String trangThai = rs.getString(5);
                double giaCho = rs.getDouble(6);

                ChoNgoi choNgoi = new ChoNgoi(maChoNgoi, toa, tang, khoang, trangThai, giaCho);

                list.add(choNgoi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(ChoNgoi choNgoi) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("insert into ChoNgoi values(?,?,?,?,?,?)");

            stm.setString(1, choNgoi.getMaChoNgoi());
            stm.setString(2, choNgoi.getToa().getMaToa());
            stm.setInt(3, choNgoi.getTang());
            stm.setInt(4, choNgoi.getKhoang());
            stm.setString(5, choNgoi.getTrangThai());
            stm.setDouble(6, choNgoi.getGiaCho());

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

    public boolean update(ChoNgoi choNgoi) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update ChoNgoi set maToa = ?, tang = ?, khoang = ?, trangThai = ?, giaCho = ? where maChoNgoi = ?");

            stm.setString(1, choNgoi.getToa().getMaToa());
            stm.setInt(2, choNgoi.getTang());
            stm.setInt(3, choNgoi.getKhoang());
            stm.setString(4, choNgoi.getTrangThai());
            stm.setDouble(5, choNgoi.getGiaCho());
            stm.setString(6, choNgoi.getMaChoNgoi());

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
}
