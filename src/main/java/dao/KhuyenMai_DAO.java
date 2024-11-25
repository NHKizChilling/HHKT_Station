package dao;

import connectdb.ConnectDB;
import entity.KhuyenMai;
import entity.NhanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class KhuyenMai_DAO {

    public KhuyenMai_DAO() {
    }

    public void themKhuyenMai() {

    }

    public void suaKhuyenMai() {
    }

    public void xoaKhuyenMai() {
    }

    public void timKhuyenMai() {
    }

    public ArrayList<KhuyenMai> getAllKM() {
        ArrayList<KhuyenMai> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from KhuyenMai";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                KhuyenMai km = getInfo(rs);
                list.add(km);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public KhuyenMai getKMTheoMa(String maKM) {
        KhuyenMai km = null;
        PreparedStatement stm = null;

        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from KhuyenMai where maKM = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, maKM);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                km = getInfo(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return km;
    }

    public KhuyenMai getKMGiamCaoNhat() {
        KhuyenMai km = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select TOP 1 * from KhuyenMai order by mucKM DESC";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if (rs.next()) {
                km = getInfo(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return km;
    }

    public ArrayList<KhuyenMai> getKMTheoNgay(LocalDate ngay) {
        ArrayList<KhuyenMai> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from KhuyenMai where ? between ngayApDung and ngayHetHan";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setDate(1, java.sql.Date.valueOf(ngay));
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                KhuyenMai km = getInfo(rs);
                list.add(km);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themKhuyenMai(KhuyenMai km) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("insert into KhuyenMai values(?,?,?,?,?,?)");
            stm.setString(1, km.getMaKM());
            stm.setString(2, km.getMoTa());
            stm.setDate(3, java.sql.Date.valueOf(km.getNgayApDung()));
            stm.setDate(4, java.sql.Date.valueOf(km.getNgayHetHan()));
            stm.setFloat(5, km.getMucKM());
            stm.setBoolean(6, km.isTrangThai());
            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean suaKhuyenMai(KhuyenMai km) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update KhuyenMai set moTa = ?, ngayApDung = ?, ngayHetHan = ?, mucKM = ?, trangThai = ? where maKM = ?");
            stm.setString(1, km.getMoTa());
            stm.setDate(2, java.sql.Date.valueOf(km.getNgayApDung()));
            stm.setDate(3, java.sql.Date.valueOf(km.getNgayHetHan()));
            stm.setFloat(4, km.getMucKM());
            stm.setBoolean(5, km.isTrangThai());
            stm.setString(6, km.getMaKM());
            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean xoaKhuyenMai(String maKM) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("delete from KhuyenMai where maKM = ?");
            stm.setString(1, maKM);
            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean capNhatTrangThaiKM(String maKM, boolean trangThai) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update KhuyenMai set trangThai = ? where maKM = ?");
            stm.setBoolean(1, trangThai);
            stm.setString(2, maKM);
            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public KhuyenMai getInfo(ResultSet rs) {
        KhuyenMai km = null;
        try {
            String maKM = rs.getString(1);
            String moTa = rs.getString(2);
            LocalDate ngayApDung = rs.getDate(3).toLocalDate();
            LocalDate ngayHetHan = rs.getDate(4).toLocalDate();
            float mucKM = rs.getFloat(5);
            boolean tinhTrangCV = rs.getBoolean(6);

            km = new KhuyenMai(maKM, moTa, ngayApDung, ngayHetHan, mucKM, tinhTrangCV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return km;
    }
}
