package controller;

import connectdb.ConnectDB;
import dao.*;
import entity.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class ChuyenTauController implements Initializable {

    @FXML
    private Button btn_add;

    @FXML
    private Button btn_clear;

    @FXML
    private Button btn_lamMoi;

    @FXML
    private Button btn_timKiem;

    @FXML
    private Button btn_update;

    @FXML
    private ComboBox<String> cb_GaDen;

    @FXML
    private ComboBox<String> cb_GaDi;

    @FXML
    private ComboBox<String> cb_soHieuTau;

    @FXML
    private ComboBox<String> cb_infoGaDen;

    @FXML
    private ComboBox<String> cb_infoGaDi;

    @FXML
    private ComboBox<String> cb_viTri;

    @FXML
    private TableView<LichTrinh> tbl_lichTrinh;

    @FXML
    private ComboBox<String> cb_infoTrangThaiHoatDong;

    @FXML
    private TableColumn<LichTrinh, String> col_gaDi;

    @FXML
    private TableColumn<LichTrinh, String> col_maDen;

    @FXML
    private TableColumn<LichTrinh, String> col_maLichTrinh;

    @FXML
    private TableColumn<LichTrinh, String> col_soHieuTau;

    @FXML
    private TableColumn<LichTrinh, String> col_tgKhoiHanh;

    @FXML
    private TableColumn<LichTrinh, String> col_trangThaiHoatDong;

    @FXML
    private DatePicker datePicker_tgKhoiHanh;

    @FXML
    private DatePicker dp_ngayKH;

    @FXML
    private TextField txt_maLichTrinh;

    private LichTrinh_DAO lichTrinh_DAO;
    private CT_LichTrinh_DAO ct_LichTrinh_DAO;
    private Ga_DAO ga_DAO;
    private ChuyenTau_DAO chuyenTau_DAO;
    private ChoNgoi_DAO choNgoi_DAO;
    private Toa_DAO toa_DAO;

    private ArrayList<LichTrinh> list;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        // TODO Auto-generated method stub
        try {
            ConnectDB.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initDao();
        ArrayList<Ga> listGa = ga_DAO.getAllGa();
        for (Ga ga : listGa) {
            cb_GaDen.getItems().add(ga.getTenGa());
            cb_GaDi.getItems().add(ga.getTenGa());
            cb_infoGaDen.getItems().add(ga.getTenGa());
            cb_infoGaDi.getItems().add(ga.getTenGa());
        }
        ArrayList<String> listSoHieuTau = chuyenTau_DAO.getAll().stream().map(chuyenTau -> chuyenTau.getSoHieutau()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        cb_soHieuTau.getItems().addAll(listSoHieuTau);
        HashSet<String> set = ga_DAO.getAllGa().stream().map(ga -> ga.getViTri()).collect(HashSet::new, Set::add, Set::addAll);

        for (String viTri : set) {
            cb_viTri.getItems().add(viTri);
        }
        for (String soHieuTau : listSoHieuTau) {
            cb_soHieuTau.getItems().add(soHieuTau);
        }
        new AutoCompleteComboBoxListener<>(cb_GaDen);
        new AutoCompleteComboBoxListener<>(cb_GaDi);
        new AutoCompleteComboBoxListener<>(cb_soHieuTau);
        new AutoCompleteComboBoxListener<>(cb_infoGaDen);
        new AutoCompleteComboBoxListener<>(cb_infoGaDi);
        new AutoCompleteComboBoxListener<>(cb_viTri);
        cb_infoTrangThaiHoatDong.getItems().addAll("Hoạt động", "Tạm ngưng hoạt động", "Kết thúc");

        col_maLichTrinh.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaLichTrinh()));
        col_soHieuTau.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChuyenTau().getSoHieutau()));
        col_gaDi.setCellValueFactory(cellData -> new SimpleStringProperty(ga_DAO.getGaTheoMaGa(cellData.getValue().getGaDi().getMaGa()).getTenGa()));
        col_maDen.setCellValueFactory(cellData -> new SimpleStringProperty(ga_DAO.getGaTheoMaGa(cellData.getValue().getGaDen().getMaGa()).getTenGa()));
        col_tgKhoiHanh.setCellValueFactory(cellData -> new SimpleStringProperty(formatter.format(cellData.getValue().getThoiGianKhoiHanh())));
        col_trangThaiHoatDong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isTinhTrang() ? "Hoạt động" : (cellData.getValue().getThoiGianKhoiHanh().isAfter(LocalDateTime.now()) ? "Tạm ngưng hoạt động" : "Kết thúc")));
        tbl_lichTrinh.setOnMouseClicked(e -> {
            LichTrinh lichTrinh = tbl_lichTrinh.getSelectionModel().getSelectedItem();
            if (lichTrinh != null) {
                txt_maLichTrinh.setText(lichTrinh.getMaLichTrinh());
                cb_soHieuTau.setValue(lichTrinh.getChuyenTau().getSoHieutau());
                cb_infoGaDi.setValue(lichTrinh.getGaDi().getTenGa());
                cb_infoGaDen.setValue(lichTrinh.getGaDen().getTenGa());
                datePicker_tgKhoiHanh.setValue(lichTrinh.getThoiGianKhoiHanh().toLocalDate());
                cb_infoTrangThaiHoatDong.setValue(col_trangThaiHoatDong.getCellData(lichTrinh));
                cb_infoGaDi.setDisable(true);
                cb_infoGaDen.setDisable(true);
                cb_soHieuTau.setDisable(true);
            }
        });

        btn_timKiem.setOnAction(e -> {
            timKiem();
        });

        btn_lamMoi.setOnAction(e -> {
            clear();
        });

        btn_add.setOnAction(e -> {
            add();
        });

        btn_update.setOnAction(e -> {
            update();
        });

        btn_clear.setOnAction(e -> {
            refreshInfo();
        });
    }

    protected void initDao() {
        // TODO Auto-generated method stub
        lichTrinh_DAO = new LichTrinh_DAO();
        ct_LichTrinh_DAO = new CT_LichTrinh_DAO();
        ga_DAO = new Ga_DAO();
        chuyenTau_DAO = new ChuyenTau_DAO();
        choNgoi_DAO = new ChoNgoi_DAO();
        toa_DAO = new Toa_DAO();
    }

    protected void timKiem() {
        // TODO Auto-generated method stub
        String gaDi = cb_GaDi.getValue();
        String gaDen = cb_GaDen.getValue();
        LocalDate ngayKH = dp_ngayKH.getValue();
        if (ngayKH == null) {
            ngayKH = LocalDate.now();
            list = lichTrinh_DAO.traCuuDSLichTrinh(gaDi, gaDen, ngayKH);
        } else {
            if (cb_GaDi.getValue() == null && cb_GaDen.getValue() == null) {
                list = lichTrinh_DAO.traCuuDSLichTrinhTheoNgay(ngayKH);
            } else {
                list = lichTrinh_DAO.traCuuDSLichTrinh(gaDi, gaDen, ngayKH);
            }
        }
        if (list == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Không tìm thấy lịch trình nào");
            alert.show();
            clear();
            cb_GaDi.requestFocus();
            return;
        }
        tbl_lichTrinh.getSelectionModel().clearSelection();
        tbl_lichTrinh.getItems().addAll(list);
    }

    protected void clear() {
        txt_maLichTrinh.clear();
        cb_soHieuTau.setValue(null);
        cb_soHieuTau.setPromptText("Số hiệu tàu");
        cb_GaDi.setValue(null);
        cb_GaDi.setPromptText("Ga đi");
        cb_GaDen.setValue(null);
        cb_GaDen.setPromptText("Ga đến");
        cb_infoGaDi.setValue(null);
        cb_infoGaDi.setPromptText("Ga đi");
        cb_infoGaDen.setValue(null);
        cb_infoGaDen.setPromptText("Ga đến");
        cb_viTri.setValue(null);
        cb_viTri.setPromptText("Tỉnh/Thành phố");
        datePicker_tgKhoiHanh.setValue(null);
        cb_infoTrangThaiHoatDong.setValue(null);
        tbl_lichTrinh.setItems(null);
        cb_GaDi.requestFocus();
    }

    protected void add() {
        // TODO Auto-generated method stub
        String soHieuTau = cb_soHieuTau.getValue();
        String gaDi = cb_infoGaDi.getValue();
        String gaDen = cb_infoGaDen.getValue();
        LocalDate ngayKH = dp_ngayKH.getValue();
        String trangThai = cb_infoTrangThaiHoatDong.getValue();
        if (soHieuTau == null || gaDi == null || gaDen == null || ngayKH == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Vui lòng nhập đầy đủ thông tin");
            alert.show();
            return;
        }
        LichTrinh lichTrinh = new LichTrinh();
        lichTrinh.setChuyenTau(chuyenTau_DAO.getChuyenTauTheoID(soHieuTau));
        lichTrinh.setGaDi(ga_DAO.getGaTheoTenGa(gaDi));
        lichTrinh.setGaDen(ga_DAO.getGaTheoTenGa(gaDen));
        lichTrinh.setThoiGianKhoiHanh(LocalDateTime.of(ngayKH, LocalDateTime.now().toLocalTime()));
        lichTrinh.setTinhTrang(true);
        if (lichTrinh_DAO.create(lichTrinh)) {
            ArrayList<Toa> listToa = toa_DAO.getAllToaTheoChuyenTau(soHieuTau);
            for (Toa toa : listToa) {
                ArrayList<ChoNgoi> listChoNgoi = choNgoi_DAO.getDSChoNgoiTheoToa(toa.getMaToa());
                for (ChoNgoi choNgoi : listChoNgoi) {
                    choNgoi.setToa(toa);
                    ct_LichTrinh_DAO.create(new ChiTietLichTrinh(choNgoi, lichTrinh));
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Thêm lịch trình thành công");
            alert.show();
            clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Thêm lịch trình thất bại");
            alert.show();
        }
    }

    protected void update() {
        // TODO Auto-generated method stub
        String maLichTrinh = txt_maLichTrinh.getText();
        String soHieuTau = cb_soHieuTau.getValue();
        String gaDi = cb_infoGaDi.getValue();
        String gaDen = cb_infoGaDen.getValue();
        LocalDate ngayKH = dp_ngayKH.getValue();
        String trangThai = cb_infoTrangThaiHoatDong.getValue();
        if (maLichTrinh == null || soHieuTau == null || gaDi == null || gaDen == null || ngayKH == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Vui lòng nhập đầy đủ thông tin");
            alert.show();
            return;
        }
        LichTrinh lichTrinh = lichTrinh_DAO.getLichTrinhTheoID(maLichTrinh);
        lichTrinh.setChuyenTau(chuyenTau_DAO.getChuyenTauTheoID(soHieuTau));
        lichTrinh.setGaDi(ga_DAO.getGaTheoTenGa(gaDi));
        lichTrinh.setGaDen(ga_DAO.getGaTheoTenGa(gaDen));
        lichTrinh.setThoiGianKhoiHanh(LocalDateTime.of(ngayKH, LocalDateTime.now().toLocalTime()));
        lichTrinh.setTinhTrang(trangThai.equals("Hoạt động"));
        if (lichTrinh_DAO.updateTrangThaiChuyenTau(lichTrinh.getChuyenTau().getSoHieutau(), lichTrinh.isTinhTrang())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Cập nhật lịch trình thành công");
            alert.show();
            clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Cập nhật lịch trình thất bại");
            alert.show();
        }
    }

    protected void refreshInfo() {
        // TODO Auto-generated method stub
        cb_infoGaDi.setDisable(false);
        cb_infoGaDen.setDisable(false);
        cb_soHieuTau.setDisable(false);
        txt_maLichTrinh.clear();
        cb_soHieuTau.setValue(null);
        cb_soHieuTau.setPromptText("Số hiệu tàu");
        cb_infoGaDi.setValue(null);
        cb_infoGaDi.setPromptText("Ga đi");
        cb_infoGaDen.setValue(null);
        cb_infoGaDen.setPromptText("Ga đến");
        cb_viTri.setValue(null);
        cb_viTri.setPromptText("Tỉnh/Thành phố");
        datePicker_tgKhoiHanh.setValue(null);
        cb_infoTrangThaiHoatDong.setValue(null);
        cb_soHieuTau.requestFocus();
    }
}
