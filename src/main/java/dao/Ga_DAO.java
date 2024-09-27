package dao;

import connectdb.ConnectDB;
import entity.Ga;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Ga_DAO {

    public Ga_DAO() {
    }

    public ArrayList<Ga> getAllGa() {
        ArrayList<Ga> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from Ga";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String maGa = rs.getString(1);
                String tenGa = rs.getString(2);
                String vitri = rs.getString(3);

                Ga ga = new Ga(maGa, tenGa, vitri);
                list.add(ga);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(Ga ga) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("insert into Ga values(?,?,?)");

            stm.setString(1, ga.getMaGa());
            stm.setString(2, ga.getTenGa());
            stm.setString(3, ga.getViTri());

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

    public boolean update(Ga ga) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update Ga set TenGa = ?, ViTri = ? where MaGa = ?");

            stm.setString(1, ga.getTenGa());
            stm.setString(2, ga.getViTri());
            stm.setString(3, ga.getMaGa());

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

    public boolean delete(String maGa) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("delete from Ga where MaGa = ?");

            stm.setString(1, maGa);

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

    public Ga getGaTheoMaGa(String maGa) {
        Ga ga = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from Ga where MaGa = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, maGa);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String tenGa = rs.getString(2);
                String viTri = rs.getString(3);
                ga = new Ga(maGa, tenGa, viTri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ga;
    }
}

