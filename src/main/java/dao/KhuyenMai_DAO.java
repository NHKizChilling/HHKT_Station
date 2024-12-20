package dao;

import connectdb.ConnectDB;
import entity.KhuyenMai;
import entity.NhanVien;

import java.sql.*;
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

    public ArrayList<KhuyenMai> getKMHienCo() {
        ArrayList<KhuyenMai> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from KhuyenMai where TrangThai = 1";
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
        PreparedStatement stm;

        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from KhuyenMai where MaKM = ?";
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
            String sql = "Select TOP 1 * from KhuyenMai where TrangThai = 1 order by MucKM DESC";
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
            String sql = "Select * from KhuyenMai where ? >= ngayApDung and ? <= ngayHetHan";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setDate(1, java.sql.Date.valueOf(ngay));
            stm.setDate(2, java.sql.Date.valueOf(ngay));
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
            stm = con.prepareStatement("insert into KhuyenMai values('temp',?,?,?,?,?)");
            stm.setString(1, km.getMoTa());
            stm.setDate(2, java.sql.Date.valueOf(km.getNgayApDung()));
            stm.setDate(3, java.sql.Date.valueOf(km.getNgayHetHan()));
            stm.setFloat(4, km.getMucKM());
            stm.setBoolean(5, km.isTrangThai());
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
            stm = con.prepareStatement("update KhuyenMai set MoTa = ?, NgayApDung = ?, NgayHetHan = ?, MucKM = ?, TrangThai = ? where MaKM = ?");
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
            stm = con.prepareStatement("delete from KhuyenMai where MaKM = ?");
            stm.setString(1, maKM);
            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean kichHoatKhuyenMai() {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update KhuyenMai set TrangThai = 1 where NgayApDung = ? and TrangThai = 0");
            stm.setDate(1, Date.valueOf(LocalDate.now()));
            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean khoaKhuyenMai() {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update KhuyenMai set TrangThai = 0 where NgayHetHan < ? and TrangThai = 1");
            stm.setDate(1, Date.valueOf(LocalDate.now()));
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
            stm = con.prepareStatement("update KhuyenMai set TrangThai = ? where MaKM = ?");
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
