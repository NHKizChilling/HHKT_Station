package dao;

import connectdb.ConnectDB;
import entity.ChuyenTau;
import entity.LoaiTau;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ChuyenTau_DAO {

    public ChuyenTau_DAO() {
    }

    public ArrayList<ChuyenTau> getAll() {
        ArrayList<ChuyenTau> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from ChuyenTau";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String soHieuTau = rs.getString(1);
                LoaiTau loaiTau = new LoaiTau(rs.getString(2));

                ChuyenTau chuyenTau = new ChuyenTau(soHieuTau, loaiTau);
                list.add(chuyenTau);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ChuyenTau getChuyenTauTheoID(String soHieuTau) {
        ChuyenTau chuyenTau = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from ChuyenTau where SoHieuTau = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, soHieuTau);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                LoaiTau loaiTau = new LoaiTau(rs.getString(2));
                chuyenTau = new ChuyenTau(soHieuTau, loaiTau);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chuyenTau;
    }



    public boolean create(ChuyenTau chuyenTau) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("insert into ChuyenTau values(?,?)");

            stm.setString(1, chuyenTau.getSoHieutau());
            stm.setString(2, chuyenTau.getLoaiTau().getMaLoaiTau());

            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean update(ChuyenTau chuyenTau) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update ChuyenTau set MaLoaiTau = ? where SoHieuTau = ?");

            stm.setString(1, chuyenTau.getLoaiTau().getMaLoaiTau());
            stm.setString(2, chuyenTau.getSoHieutau());

            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }
}
