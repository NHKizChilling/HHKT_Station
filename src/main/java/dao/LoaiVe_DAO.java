package dao;

import connectdb.ConnectDB;
import entity.LoaiTau;
import entity.LoaiToa;
import entity.LoaiVe;

import java.sql.*;
import java.util.ArrayList;

public class LoaiVe_DAO {

    public LoaiVe_DAO() {
    }

    public ArrayList<LoaiVe> getAllLoaiVe() {
        ArrayList<LoaiVe> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "Select * from LoaiVe";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String maLoaiVe = rs.getString(1);
                String tenLoaiVe = rs.getString(2);
                float mucGiamGia = rs.getFloat(3);

                LoaiVe loaiVe = new LoaiVe(maLoaiVe, tenLoaiVe, mucGiamGia);
                list.add(loaiVe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(LoaiVe loaiVe) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("insert into LoaiVe values(?,?,?)");

            stm.setString(1, loaiVe.getMaLoaiVe());
            stm.setString(2, loaiVe.getTenLoaiVe());
            stm.setFloat(3, loaiVe.getMucGiamGia());

            n = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }


    public boolean update(LoaiVe loaiVe) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update LoaiVe set TenLoaiVe = ?, MucGiamGia = ? where MaLoaiVe = ?");

            stm.setString(1, loaiVe.getTenLoaiVe());
            stm.setFloat(2, loaiVe.getMucGiamGia());
            stm.setString(3, loaiVe.getMaLoaiVe());

            n = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }


    public boolean delete(String maLoaiVe) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("delete from LoaiVe where MaLoaiVe = ?");

            stm.setString(1, maLoaiVe);

            n = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public LoaiVe getLoaiVeTheoTen(String tenLoai) {
        LoaiVe loaiVe = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "Select * from LoaiVe where TenLoaiVe = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, tenLoai);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String maLoaiVe1 = rs.getString(1);
                String tenLoaiVe = rs.getString(2);
                float mucGiamGia = rs.getFloat(3);

                loaiVe = new LoaiVe(maLoaiVe1, tenLoaiVe, mucGiamGia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loaiVe;
    }

    public LoaiVe getLoaiVeTheoMa(String maLoaiVe) {
        LoaiVe loaiVe = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "Select * from LoaiVe where MaLoaiVe = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, maLoaiVe);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String maLoaiVe1 = rs.getString(1);
                String tenLoaiVe = rs.getString(2);
                float mucGiamGia = rs.getFloat(3);

                loaiVe = new LoaiVe(maLoaiVe1, tenLoaiVe, mucGiamGia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loaiVe;
    }
}
