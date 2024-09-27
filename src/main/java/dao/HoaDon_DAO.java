package dao;

import connectdb.ConnectDB;
import entity.HanhKhach;
import entity.HoaDon;
import entity.NhanVien;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HoaDon_DAO {

    public HoaDon_DAO() {
    }

    public ArrayList<HoaDon> getAllHoaDon() {
        ArrayList<HoaDon> list = new ArrayList<>();

        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from HoaDon";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String maHoaDon = rs.getString(1);
                LocalDateTime ngayLap = rs.getTimestamp(2).toLocalDateTime();
                NhanVien maNhanVien = new NhanVien(rs.getString(3));
                HanhKhach maKhachHang = new HanhKhach(rs.getString(4));
                double tongTien = rs.getDouble(5);
                double tongGiamGia = rs.getDouble(6);

                HoaDon hoaDon = new HoaDon(maHoaDon, maNhanVien, ngayLap, maKhachHang, tongTien, tongGiamGia);

                list.add(hoaDon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
