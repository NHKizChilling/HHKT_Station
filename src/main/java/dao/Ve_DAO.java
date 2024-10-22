package dao;

import connectdb.ConnectDB;
import entity.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Ve_DAO {

    public Ve_DAO() {
    }

    public ArrayList<Ve> getAllVe() {
        ArrayList<Ve> list = new ArrayList<>();

        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from Ve";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String maVe = rs.getString(1);
                HanhKhach hanhKhach = new HanhKhach(rs.getString(2));
                ChiTietLichTrinh chiTietLichTrinh = new ChiTietLichTrinh(new ChoNgoi(rs.getString(3)), new LichTrinh(rs.getString(4)));
                LoaiVe loaiVe = new LoaiVe(rs.getString(5));
                String tenHK = rs.getString(6);
                String soCCCD = rs.getString(7);
                LocalDate ngaySinh = rs.getDate(8).toLocalDate();
                String tinhTrangVe = rs.getString(9);
                boolean khuHoi = rs.getBoolean(10);

                Ve ve = new Ve(maVe, hanhKhach, chiTietLichTrinh, loaiVe, tenHK, soCCCD, ngaySinh, tinhTrangVe, khuHoi);

                list.add(ve);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Ve getVeTheoID(String maVe) {
        Ve ve = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            PreparedStatement stm = null;
            String sql = "Select * from Ve where MaVe = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, maVe);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                HanhKhach hanhKhach = new HanhKhach(rs.getString(2));
                ChiTietLichTrinh chiTietLichTrinh = new ChiTietLichTrinh(new ChoNgoi(rs.getString(3)), new LichTrinh(rs.getString(4)));
                LoaiVe loaiVe = new LoaiVe(rs.getString(5));
                String tenHK = rs.getString(6);
                String soCCCD = rs.getString(7);

                LocalDate ngaySinh = null;
                if (rs.getDate(8) != null) {
                    ngaySinh = rs.getDate(8).toLocalDate();
                }
                String tinhTrangVe = rs.getString(9);
                boolean khuHoi = rs.getBoolean(10);

                ve = new Ve(maVe, hanhKhach, chiTietLichTrinh, loaiVe, tenHK, soCCCD, ngaySinh, tinhTrangVe, khuHoi);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ve;
    }

    public Ve getLaiVe(Ve ve) {
        Ve v = null;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            PreparedStatement stm = null;
            String sql = "Select * from Ve where MaHK = ? and MaSoCho = ? and MaLichTrinh = ? and TinhTrangVe = 'DaBan'";
            stm = con.prepareStatement(sql);
            stm.setString(1, ve.getHanhKhach().getMaHanhKhach());
            stm.setString(2, ve.getCtlt().getChoNgoi().getMaChoNgoi());
            stm.setString(3, ve.getCtlt().getLichTrinh().getMaLichTrinh());

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                String maVe = rs.getString(1);
                HanhKhach hanhKhach = new HanhKhach(rs.getString(2));
                ChiTietLichTrinh chiTietLichTrinh = new ChiTietLichTrinh(new ChoNgoi(rs.getString(3)), new LichTrinh(rs.getString(4)));
                LoaiVe loaiVe = new LoaiVe(rs.getString(5));
                String tenHK = rs.getString(6);
                String soCCCD = rs.getString(7);

                LocalDate ngaySinh = null;
                if (rs.getDate(8) != null) {
                    ngaySinh = rs.getDate(8).toLocalDate();
                }
                String tinhTrangVe = rs.getString(9);
                boolean khuHoi = rs.getBoolean(10);

                v = new Ve(maVe, hanhKhach, chiTietLichTrinh, loaiVe, tenHK, soCCCD, ngaySinh, tinhTrangVe, khuHoi);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    public boolean create(Ve ve) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            String sql = "Insert into Ve(MaHK, MaSoCho, MaLichTrinh, MaLoaiVe, TenHK, SoCCCD, NgaySinh, TinhTrangVe, KhuHoi) values(?,?,?,?,?,?,?,?,?)";
            stm = con.prepareStatement(sql);
            stm.setString(1, ve.getHanhKhach().getMaHanhKhach());
            stm.setString(2, ve.getCtlt().getChoNgoi().getMaChoNgoi());
            stm.setString(3, ve.getCtlt().getLichTrinh().getMaLichTrinh());
            stm.setString(4, ve.getLoaiVe().getMaLoaiVe());
            stm.setString(5, ve.getTenHanhKhach());
            if (ve.getSoCCCD() != null) {
                stm.setString(6, ve.getSoCCCD());
            } else {
                stm.setString(6, null);
            }
            if (ve.getNgaySinh() != null) {
                stm.setDate(7, Date.valueOf(ve.getNgaySinh()));
            } else {
                stm.setDate(7, null);
            }
            stm.setString(8, ve.getTinhTrangVe());
            stm.setBoolean(9, ve.isKhuHoi());

            n = stm.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

    public boolean update(Ve ve) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update Ve set MaLoaiVe = ?, MaHK = ?, MaSoCho = ?, MaLichTrinh = ?, TenHK =?, SoCCCD = ?, NgaySinh = ?, TinhTrangVe = ?, KhuHoi = ? where MaVe = ?");

            stm.setString(1, ve.getLoaiVe().getMaLoaiVe());
            stm.setString(2, ve.getHanhKhach().getMaHanhKhach());
            stm.setString(3, ve.getCtlt().getChoNgoi().getMaChoNgoi());
            stm.setString(4, ve.getCtlt().getLichTrinh().getMaLichTrinh());
            stm.setString(5, ve.getTenHanhKhach());
            stm.setString(6, ve.getSoCCCD());
            LocalDate ngaySinh = ve.getNgaySinh();
            if (ngaySinh == null) {
                stm.setDate(7, null);
            } else {
                stm.setDate(7, Date.valueOf(ve.getNgaySinh()));
            }
            stm.setString(8, ve.getTinhTrangVe());
            stm.setString(10, ve.getMaVe());
            stm.setBoolean(9, ve.isKhuHoi());

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

    public boolean updateTinhTrangVe(String maVe, String tinhTrangVe) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stm = null;
        int n = 0;
        try {
            stm = con.prepareStatement("update Ve set TinhTrangVe = ? where MaVe = ?");

            stm.setString(1, tinhTrangVe);
            stm.setString(2, maVe);

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

    public ArrayList<Ve> getDSVeTheoMaHK(String maHK) {
        ArrayList<Ve> list = new ArrayList<>();

        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            PreparedStatement stm = null;
            String sql = "Select * from Ve where MaHK = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, maHK);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Ve ve = getInfo(rs);

                list.add(ve);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Ve> getVeTheoTinhTrang(String tinhTrangVe) {
        ArrayList<Ve> list = new ArrayList<>();

        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            PreparedStatement stm = null;
            String sql = "Select * from Ve where TinhTrangVe = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, tinhTrangVe);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Ve ve = getInfo(rs);

                list.add(ve);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private Ve getInfo(ResultSet rs) {
        Ve ve = null;
        try {
            String maVe = rs.getString(1);
            HanhKhach hanhKhach = new HanhKhach(rs.getString(2));
            ChiTietLichTrinh chiTietLichTrinh = new ChiTietLichTrinh(new ChoNgoi(rs.getString(3)), new LichTrinh(rs.getString(4)));
            LoaiVe loaiVe = new LoaiVe(rs.getString(5));
            String tenHK = rs.getString(6);
            String soCCCD = rs.getString(7);
            LocalDate ngaySinh = null;
            if (rs.getDate(8) != null) {
                ngaySinh = rs.getDate(8).toLocalDate();
            }
            String tinhTrangVe = rs.getString(9);
            boolean khuHoi = rs.getBoolean(10);

            ve = new Ve(maVe, hanhKhach, chiTietLichTrinh, loaiVe, tenHK, soCCCD, ngaySinh, tinhTrangVe, khuHoi);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ve;
    }
}
