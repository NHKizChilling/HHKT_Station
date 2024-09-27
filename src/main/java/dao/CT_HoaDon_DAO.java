package dao;

import connectdb.ConnectDB;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.Ve;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CT_HoaDon_DAO {

    public CT_HoaDon_DAO() {
    }

    public ArrayList<ChiTietHoaDon> getAllCT_HoaDon() {
        ArrayList<ChiTietHoaDon> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "Select * from ChiTietHoaDon";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {
                HoaDon hoaDon = new HoaDon(rs.getString(1));
                Ve ve = new Ve(rs.getString(2));
                int soLuong = rs.getInt(3);
                double giamGia = rs.getDouble(4);

                ChiTietHoaDon ctHoaDon = new ChiTietHoaDon(hoaDon, ve, soLuong, giamGia);
                list.add(ctHoaDon);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}

