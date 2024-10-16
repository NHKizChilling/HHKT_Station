package dao;

import com.sun.jdi.ArrayReference;
import connectdb.ConnectDB;
import entity.ChiTietLichTrinh;
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
                ChoNgoi choNgoi = getInfo(rs);

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
            String sql = "Select * from ChoNgoi where MaToa = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, maToa);

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                ChoNgoi choNgoi = getInfo(rs);

                list.add(choNgoi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<ChoNgoi> getChoNgoiDaDat(ChiTietLichTrinh chiTietLichTrinh) {
        ArrayList<ChoNgoi> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from ChoNgoi a join ChiTietLichTrinh b on a.MaChoNgoi = b.MaChoNgoi where TrangThai = 0";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                ChoNgoi choNgoi = getInfo(rs);

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
            stm = con.prepareStatement("update ChoNgoi set MaToa = ?, Tang = ?, Khoang = ?, TrangThai = ?, GiaCho = ? where MaChoNgoi = ?");

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

    public boolean delete(String maChoNgoi) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            String sql = "delete from ChoNgoi where MaChoNgoi = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, maChoNgoi);

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

    public ArrayList<ChoNgoi> getDsChoNgoiTheoToa(String maToa) {
        ArrayList<ChoNgoi> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from ChoNgoi where MaToa = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, maToa);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                ChoNgoi choNgoi = getInfo(rs);

                list.add(choNgoi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ChoNgoi getInfo(ResultSet rs) {
        ChoNgoi choNgoi = null;
        try {
            String maChoNgoi = rs.getString(1);
            Toa toa = new Toa(rs.getString(2));
            int sttCho = rs.getInt(3);
            int tang = rs.getInt(4);
            int khoang = rs.getInt(5);
            String trangThai = rs.getString(6);
            double giaCho = rs.getDouble(7);

            choNgoi = new ChoNgoi(maChoNgoi, toa, sttCho, tang, khoang, trangThai, giaCho);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return choNgoi;
    }
}
