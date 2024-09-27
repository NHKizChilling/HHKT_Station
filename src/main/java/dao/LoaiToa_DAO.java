package dao;

import connectdb.ConnectDB;
import entity.LoaiToa;

import java.sql.*;
import java.util.ArrayList;

public class LoaiToa_DAO {

    public LoaiToa_DAO() {
    }

    public ArrayList<LoaiToa> getAllLoaiToa() {
        ArrayList<LoaiToa> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "Select * from LoaiToa";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String maLoaiToa = rs.getString(1);
                String tenLoaiToa = rs.getString(2);

                LoaiToa loaiToa = new LoaiToa(maLoaiToa, tenLoaiToa);
                list.add(loaiToa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(LoaiToa loaiToa) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("insert into LoaiToa values(?,?)");

            stm.setString(1, loaiToa.getMaLoaiToa());
            stm.setString(2, loaiToa.getTenLoaiToa());

            n = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return n>0;
    }

    public boolean update(LoaiToa loaiToa) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update LoaiToa set TenLoaiToa = ? where MaLoaiToa = ?");

            stm.setString(1, loaiToa.getTenLoaiToa());
            stm.setString(2, loaiToa.getMaLoaiToa());

            n = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return n>0;
    }

    public boolean delete(String maLoaiToa) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("delete from LoaiToa where MaLoaiToa = ?");

            stm.setString(1, maLoaiToa);

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

    public LoaiToa getLoaiToaTheoMa(String maLoaiToa) {
        LoaiToa loaiToa = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "Select * from LoaiToa where MaLoaiToa = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, maLoaiToa);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String tenLoaiToa = rs.getString(2);

                loaiToa = new LoaiToa(maLoaiToa, tenLoaiToa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loaiToa;
    }

    public boolean xoaLoaiToaTheoMa(String maLoaiToa) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("delete from LoaiToa where MaLoaiToa = ?");

            stm.setString(1, maLoaiToa);

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
}
