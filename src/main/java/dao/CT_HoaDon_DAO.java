package dao;

import connectdb.ConnectDB;
import entity.ChiTietHoaDon;

import java.util.ArrayList;

public class CT_HoaDon_DAO {

    public CT_HoaDon_DAO() {
    }

    public ArrayList<ChiTietHoaDon> getAllCT_HoaDon() {
        ArrayList<ChiTietHoaDon> list = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "Select * from CT_HoaDon";
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {

                String maHD = rs.getString("MaHD");
                HoaDon mhd = new HoaDon_DAO().getHoaDonTheoMaHD(maHD);

                String maSP = rs.getString(2);
                //SanPham msp = new SanPham(maSP);
                SanPham sp = sanPham_dao.getDSSanPhamTheoMa(maSP).get(0);

                int soLuong = rs.getInt(3);

                double chietKhau = rs.getDouble(4);

                CT_HoaDon ct = new CT_HoaDon(mhd, sp, soLuong, chietKhau);
                dsCTHD.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsCTHD;
    }

}
}
