package dao;

import entity.LoaiTau;

import java.sql.*;
import java.util.ArrayList;


import connectdb.ConnectDB;

public class LoaiTau_DAO {

    public LoaiTau_DAO() {
    }

    public ArrayList<LoaiTau> getAllLoaiTau() {
        ArrayList<LoaiTau> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "Select * from LoaiTau";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String maLoaiTau = rs.getString(1);
                String tenLoaiTau = rs.getString(2);

                LoaiTau loaiTau = new LoaiTau(maLoaiTau, tenLoaiTau);
                list.add(loaiTau);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(LoaiTau loaiTau) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("insert into LoaiTau values(?,?)");

            stm.setString(1, loaiTau.getMaLoaiTau());
            stm.setString(2, loaiTau.getTenLoaiTau());

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

    public boolean update(LoaiTau loaiTau) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update LoaiTau set TenLoaiTau = ? where MaLoaiTau = ?");

            stm.setString(1, loaiTau.getTenLoaiTau());
            stm.setString(2, loaiTau.getMaLoaiTau());

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

    public boolean delete(String maLoaiTau) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("delete from LoaiTau where MaLoaiTau = ?");

            stm.setString(1, maLoaiTau);

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

    public LoaiTau getLoaiTauTheoMa(String maLoaiTau) {
        LoaiTau loaiTau = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "Select * from LoaiTau where MaLoaiTau = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, maLoaiTau);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String maLoaiTau1 = rs.getString(1);
                String tenLoaiTau = rs.getString(2);

                loaiTau = new LoaiTau(maLoaiTau1, tenLoaiTau);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loaiTau;
    }
}
