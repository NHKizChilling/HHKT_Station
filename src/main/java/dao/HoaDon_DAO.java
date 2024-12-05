package dao;

import connectdb.ConnectDB;
import entity.KhachHang;
import entity.HoaDon;
import entity.KhuyenMai;
import entity.NhanVien;

import java.sql.*;
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

                HoaDon hoaDon = getInfo(rs);
                list.add(hoaDon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public HoaDon getHoaDonTheoMa(String maHoaDon) {
        HoaDon hoaDon = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            PreparedStatement stm = null;
            String sql = "Select * from HoaDon where MaHD = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, maHoaDon);

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                hoaDon = getInfo(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hoaDon;
    }

    public ArrayList<HoaDon> getHoaDonTheoNV(String maNV, LocalDateTime ngayLap) {
        ArrayList<HoaDon> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            PreparedStatement stm = null;
            String sql = "Select * from HoaDon where MaNV = ? and YEAR(NgayLapHoaDon) = ? and MONTH(NgayLapHoaDon) = ? and DAY(NgayLapHoaDon) = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, maNV);
            stm.setInt(2, ngayLap.getYear());
            stm.setInt(3, ngayLap.getMonthValue());
            stm.setInt(4, ngayLap.getDayOfMonth());

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                HoaDon hoaDon = getInfo(rs);
                list.add(hoaDon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<HoaDon> getHoaDonTheoKH(String maKH) {
        ArrayList<HoaDon> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            PreparedStatement stm = null;
            String sql = "Select * from HoaDon where MaKH = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, maKH);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                HoaDon hoaDon = getInfo(rs);
                list.add(hoaDon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<HoaDon> getHoaDonTheoNV(String maNV) {
        ArrayList<HoaDon> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            PreparedStatement stm = null;
            String sql = "Select * from HoaDon where MaNV = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, maNV);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                HoaDon hoaDon = getInfo(rs);
                list.add(hoaDon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public HoaDon getHoaDonVuaTao() {
        HoaDon hoaDon = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            PreparedStatement stm = null;
            String sql = "Select TOP 1 * from HoaDon order by MaHD desc";
            stm = con.prepareStatement(sql);

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                hoaDon = getInfo(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hoaDon;
    }

    public boolean create(HoaDon hoaDon) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("insert into HoaDon values(?,?,?,?,?,?)");

            stm.setString(1, hoaDon.getMaHoaDon());
            stm.setString(2, hoaDon.getNhanVien().getMaNhanVien());
            stm.setString(3, hoaDon.getKhachHang().getMaKH());
            stm.setTimestamp(4, java.sql.Timestamp.valueOf(hoaDon.getNgayLapHoaDon()));
            stm.setDouble(5, hoaDon.getTongTien());
            stm.setDouble(6, hoaDon.getTongGiamGia());

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

    public boolean createTempInvoice(HoaDon hoaDon) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("insert into HoaDon(MaNV, MaKH, NgayLapHoaDon, TongTien, TongGiamGia, TrangThai) values(?,?,?,0,0,?)");

            stm.setString(1, hoaDon.getNhanVien().getMaNhanVien());
            stm.setString(2, hoaDon.getKhachHang().getMaKH());
            stm.setTimestamp(3, java.sql.Timestamp.valueOf(hoaDon.getNgayLapHoaDon()));
            stm.setBoolean(4, hoaDon.isTrangThai());

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

    public ArrayList<HoaDon> getDSHDLuuTam() {
        ArrayList<HoaDon> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            PreparedStatement stm;
            String sql = "Select * from HoaDon where TrangThai = 0 and TongTien != 0";
            stm = con.prepareStatement(sql);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                HoaDon hoaDon = getInfo(rs);

                list.add(hoaDon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean update(HoaDon hoaDon) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update HoaDon set MaNV = ?, MaKH = ?, NgayLapHoaDon = ?, MaKM = ?,TongTien = ?, TongGiamGia = ?, TrangThai = ? where MaHD = ?");

            stm.setString(1, hoaDon.getNhanVien().getMaNhanVien());
            stm.setString(2, hoaDon.getKhachHang().getMaKH());
            stm.setTimestamp(3, java.sql.Timestamp.valueOf(hoaDon.getNgayLapHoaDon()));
            stm.setString(4, hoaDon.getKhuyenMai().getMaKM());
            stm.setDouble(5, hoaDon.getTongTien());
            stm.setDouble(6, hoaDon.getTongGiamGia());
            stm.setBoolean(7, hoaDon.isTrangThai());
            stm.setString(8, hoaDon.getMaHoaDon());

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

    public boolean delete(HoaDon hoaDon) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("delete from HoaDon where MaHD = ?");

            stm.setString(1, hoaDon.getMaHoaDon());

            n = stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public ArrayList<HoaDon> getDSHDTheoNam(String nam) {
        ArrayList<HoaDon> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            PreparedStatement stm;
            String sql = "Select * from HoaDon where YEAR(NgayLapHoaDon) = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, nam);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                HoaDon hoaDon = getInfo(rs);

                list.add(hoaDon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<HoaDon> getDSHDTheoThang(String nam, String thang) {
        ArrayList<HoaDon> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            PreparedStatement stm;
            String sql = "Select * from HoaDon where YEAR(NgayLapHoaDon) = ? and MONTH(NgayLapHoaDon) = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, nam);
            stm.setString(2, thang);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                HoaDon hoaDon = getInfo(rs);

                list.add(hoaDon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<HoaDon> getDSHDTheoNgay(LocalDateTime ngayLap) {
        ArrayList<HoaDon> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            PreparedStatement stm;
            String sql = "Select * from HoaDon where YEAR(NgayLapHoaDon) = ? and MONTH(NgayLapHoaDon) = ? and DAY(NgayLapHoaDon) = ?";
            stm = con.prepareStatement(sql);
            stm.setInt(1, ngayLap.getYear());
            stm.setInt(2, ngayLap.getMonthValue());
            stm.setInt(3, ngayLap.getDayOfMonth());

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                HoaDon hoaDon = getInfo(rs);

                list.add(hoaDon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public HoaDon getInfo(ResultSet rs) {
        HoaDon hoaDon = null;
        try {
            String maHoaDon = rs.getString(1);
            NhanVien nhanVien = new NhanVien(rs.getString(2));
            KhachHang khachHang = new KhachHang(rs.getString(3));
            LocalDateTime ngayLap = rs.getTimestamp(4).toLocalDateTime();
            KhuyenMai khuyenMai = new KhuyenMai(rs.getString(5));
            double tongTien = rs.getDouble(6);
            double tongGiamGia = rs.getDouble(7);
            boolean trangThai = rs.getBoolean(8);

            hoaDon = new HoaDon(maHoaDon, nhanVien,  khachHang, ngayLap, khuyenMai, tongTien, tongGiamGia, trangThai);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hoaDon;
    }
}
