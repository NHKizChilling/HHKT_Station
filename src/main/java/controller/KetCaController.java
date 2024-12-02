package controller;

import connectdb.ConnectDB;
import dao.CT_HoaDon_DAO;
import dao.HoaDon_DAO;
import dao.NhanVien_DAO;
import dao.Ve_DAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.NhanVien;
import entity.Ve;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class KetCaController implements Initializable {

    @FXML
    private Label lbl_gioMoCa;

    @FXML
    private Label lbl_tenNV;

    @FXML
    private Label lbl_tienDauCa;

    @FXML
    private Label lbl_tienTrongCa;

    @FXML
    private Label lbl_soVeBan;

    @FXML
    private Label lbl_soVeHuy;

    @FXML
    private Label lbl_soVeDoi;

    @FXML
    private Label lbl_tienBanVe;

    @FXML
    private Label lbl_tienTraVe;

    @FXML
    private Label lbl_tienTraVeDoi;

    @FXML
    private Label lbl_tienThuVeDoi;

    @FXML
    private Label lbl_tienCuoiCa;

    @FXML
    private TextField txt_tienMatThu;

    @FXML
    private TextField txt_tienChenhLech;

    @FXML
    private TextField txt_ghiChu;

    @FXML
    private Button btn_dongCa;

    @FXML
    private Button btn_inPhieu;

    @FXML
    private Label lbl_thongBao;

    private NhanVien nv;
    private LocalDateTime gioBatDau;
    private double tienDauCa;
    private double tongTien;

    private NhanVien_DAO nhanVien_dao;
    private HoaDon_DAO hoaDon_dao;
    private CT_HoaDon_DAO ct_hoaDon_dao;
    private Ve_DAO ve_dao;

    NumberFormat df = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDAO();
        initComponent();

        btn_dongCa.setOnAction(event -> {
            if (txt_tienMatThu.getText().isEmpty() || txt_tienChenhLech.getText().isEmpty()) {
                lbl_thongBao.setText("Vui lòng nhập đủ thông tin");
                return;
            }

            // TODO: làm kết ca, hỏi xem có nên tạo table Ca không?
        });
    }

    private void initComponent() {
        renderDauCa();
        renderTrongCa();
    }

    private void dangXuat() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Đăng xuất");
        alert.setContentText("Bạn có chắc chắn muốn đăng xuất?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            ConnectDB.disconnect();
            getData.nv = null;
            getData.hd = null;
            getData.kh = null;
            getData.lt = null;
            getData.ltkh = null;
            getData.dsve = null;
            getData.dscthd = null;
            System.exit(0);
        }
    }

    public void setInfo(NhanVien nv, LocalDateTime gioBatDau, Double tienDauCa) {
        this.nv = nv;
        this.gioBatDau = gioBatDau;
        this.tienDauCa = tienDauCa;

    }

    private void renderDauCa() {
        lbl_gioMoCa.setText(gioBatDau.toString());
        lbl_tenNV.setText(nv.getTenNhanVien());
        lbl_tienDauCa.setText(df.format(tienDauCa));
    }

    private void renderTrongCa() {
        int soVeBan = 0;
        int soVeHuy = 0;
        int soVeDoi = 0;

        double tienBanVe = 0;
        double tienTraVe = 0;
        double tienTraVeDoi = 0;
        double tienThuVeDoi = 0;

        // Lấy các hóa đơn trong khoảng thời gian từ đầu ca đến hiện tại
        ArrayList<HoaDon> dsHoaDon = hoaDon_dao.getHoaDonTheoNV(nv.getMaNhanVien()).stream()
        .filter(hd -> hd.getNgayLapHoaDon().isAfter(gioBatDau) && hd.getNgayLapHoaDon().isBefore(LocalDateTime.now()))
        .collect(Collectors.toCollection(ArrayList::new));

        for (HoaDon hd : dsHoaDon) {
            ArrayList<ChiTietHoaDon> dsCTHD = ct_hoaDon_dao.getCT_HoaDon(hd.getMaHoaDon());
            for (ChiTietHoaDon cthd : dsCTHD) {
                String maVe = cthd.getVe().getMaVe();
                Ve ve = ve_dao.getVeTheoID(maVe);
                String tinhTrangVe = ve.getTinhTrangVe();
                if (tinhTrangVe.equals("DaBan")) {
                    soVeBan++;
                    tienBanVe += cthd.getGiaVe();
                } else if (tinhTrangVe.equals("DaHuy")) {
                    soVeHuy++;
                    tienTraVe += cthd.getGiaVe();
                } else if (tinhTrangVe.equals("DaDoi")) {
                    soVeDoi++;
                    // TODO: Tính tiền thu, tiền trả cho vé đổi
                }
            }
        }

        lbl_soVeBan.setText(String.valueOf(soVeBan));
        lbl_soVeHuy.setText(String.valueOf(soVeHuy));
        lbl_soVeDoi.setText(String.valueOf(soVeDoi));

        tongTien = tienBanVe - tienTraVe - tienTraVeDoi + tienThuVeDoi;
        lbl_tienBanVe.setText(df.format(tienBanVe));
        lbl_tienTraVe.setText(df.format(tienTraVe));
        lbl_tienTraVeDoi.setText(df.format(tienTraVeDoi));
        lbl_tienThuVeDoi.setText(df.format(tienThuVeDoi));

        lbl_tienTrongCa.setText(df.format(tongTien));
    }

    public void renderCuoiCa() {
        lbl_tienCuoiCa.setText(df.format(tongTien));
    }

    private void initDAO() {
        nhanVien_dao = new NhanVien_DAO();
        hoaDon_dao = new HoaDon_DAO();
        ct_hoaDon_dao = new CT_HoaDon_DAO();
        ve_dao = new Ve_DAO();
    }
}
