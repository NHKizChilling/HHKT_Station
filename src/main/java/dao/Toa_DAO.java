package dao;

import connectdb.ConnectDB;
import entity.ChuyenTau;
import entity.LoaiToa;
import entity.Toa;

import java.sql.*;
import java.util.ArrayList;

public class Toa_DAO {

    public Toa_DAO() {
    }

    public ArrayList<Toa> getAllToa() {
        ArrayList<Toa> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "Select * from Toa";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String maToa = rs.getString(1);
                int soSTTToa = rs.getInt(2);
                ChuyenTau chuyenTau = new ChuyenTau(rs.getString(3));
                LoaiToa loaiToa = new LoaiToa(rs.getString(4));

                Toa toa = new Toa(maToa, soSTTToa, loaiToa, chuyenTau);
                list.add(toa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Toa> getAllToaTheoChuyenTau(String soHieuTau) {
        ArrayList<Toa> list = new ArrayList<>();
        PreparedStatement st = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "Select * from Toa where SoHieuTau = ? order by STTToa";
            st = con.prepareStatement(sql);
            st.setString(1, soHieuTau);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String maToa = rs.getString(1);
                int soSTTToa = rs.getInt(2);
                ChuyenTau chuyenTau = new ChuyenTau(rs.getString(3));
                LoaiToa loaiToa = new LoaiToa(rs.getString(4));

                Toa toa = new Toa(maToa, soSTTToa, loaiToa, chuyenTau);
                list.add(toa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Toa getToaTheoID(String maToa) {
        Toa toa = null;
        PreparedStatement stm = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from Toa where MaToa = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, maToa);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                int soSTTToa = rs.getInt(2);
                ChuyenTau chuyenTau = new ChuyenTau(rs.getString(3));
                LoaiToa loaiToa = new LoaiToa(rs.getString(4));

                toa = new Toa(maToa, soSTTToa, loaiToa, chuyenTau);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toa;
    }

    public boolean create(Toa toa) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        int n = 0;
        PreparedStatement stm = null;
        try {
            stm = con.prepareStatement("insert into Toa values(?,?,?,?)");

            stm.setString(1, toa.getMaToa());
            stm.setInt(2, toa.getSoSTToa());
            stm.setString(3, toa.getChuyenTau().getSoHieutau());
            stm.setString(4, toa.getLoaiToa().getMaLoaiToa());

            n = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean update(Toa toa) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            String sql = "update Toa set STTToa = ?, SoHieuTau = ?, MaLoaiToa = ? where MaToa = ?";
            stm = con.prepareStatement(sql);
            stm.setInt(1, toa.getSoSTToa());
            stm.setString(2, toa.getChuyenTau().getSoHieutau());
            stm.setString(3, toa.getLoaiToa().getMaLoaiToa());
            stm.setString(4, toa.getMaToa());

            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean delete(String maToa) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            String sql = "delete from Toa where MaToa = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, maToa);

            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }
}
