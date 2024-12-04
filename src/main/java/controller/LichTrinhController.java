package controller;

import connectdb.ConnectDB;
import dao.*;
import entity.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class LichTrinhController implements Initializable {

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
    private DatePicker datePicker_tgDKDen;

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

    @FXML
    private TextField infoGioDi;

    @FXML
    private TextField infoPhutDi;

    @FXML
    private TextField infoGioDen;

    @FXML
    private TextField infoPhutDen;

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
        list = new ArrayList<>();
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

        for (String soHieuTau : listSoHieuTau) {
            cb_soHieuTau.getItems().add(soHieuTau);
        }
        new AutoCompleteComboBoxListener<>(cb_GaDen);
        new AutoCompleteComboBoxListener<>(cb_GaDi);
        new AutoCompleteComboBoxListener<>(cb_soHieuTau);
        new AutoCompleteComboBoxListener<>(cb_infoGaDen);
        new AutoCompleteComboBoxListener<>(cb_infoGaDi);
        cb_infoTrangThaiHoatDong.getItems().addAll("Hoạt động", "Không hoạt động");

        col_maLichTrinh.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaLichTrinh()));
        col_soHieuTau.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChuyenTau().getSoHieutau()));
        col_gaDi.setCellValueFactory(cellData -> new SimpleStringProperty(ga_DAO.getGaTheoMaGa(cellData.getValue().getGaDi().getMaGa()).getTenGa()));
        col_maDen.setCellValueFactory(cellData -> new SimpleStringProperty(ga_DAO.getGaTheoMaGa(cellData.getValue().getGaDen().getMaGa()).getTenGa()));
        col_tgKhoiHanh.setCellValueFactory(cellData -> new SimpleStringProperty(formatter.format(cellData.getValue().getThoiGianKhoiHanh())));
        col_trangThaiHoatDong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isTinhTrang() ? "Hoạt động" : "Không hoạt động"));

        list = lichTrinh_DAO.traCuuDSLichTrinhSauNgayHienTai();
        tbl_lichTrinh.setItems(FXCollections.observableArrayList(list));

        tbl_lichTrinh.setOnMouseClicked(e -> {
            LichTrinh lichTrinh = tbl_lichTrinh.getSelectionModel().getSelectedItem();
            if (lichTrinh != null) {
                btn_add.setDisable(true);
                txt_maLichTrinh.setText(lichTrinh.getMaLichTrinh());
                cb_soHieuTau.setValue(lichTrinh.getChuyenTau().getSoHieutau());
                cb_infoGaDi.setValue(ga_DAO.getGaTheoMaGa(lichTrinh.getGaDi().getMaGa()).getTenGa());
                cb_infoGaDen.setValue(ga_DAO.getGaTheoMaGa(lichTrinh.getGaDen().getMaGa()).getTenGa());
                datePicker_tgKhoiHanh.setValue(lichTrinh.getThoiGianKhoiHanh().toLocalDate());
                infoGioDi.setText(String.valueOf(lichTrinh.getThoiGianKhoiHanh().getHour()));
                infoPhutDi.setText(String.valueOf(lichTrinh.getThoiGianKhoiHanh().getMinute()));
                datePicker_tgDKDen.setValue(lichTrinh.getThoiGianDuKienDen().toLocalDate());
                infoGioDen.setText(String.valueOf(lichTrinh.getThoiGianDuKienDen().getHour()));
                infoPhutDen.setText(String.valueOf(lichTrinh.getThoiGianDuKienDen().getMinute()));
                cb_infoTrangThaiHoatDong.setValue(col_trangThaiHoatDong.getCellData(lichTrinh));
                cb_infoGaDi.setDisable(true);
                cb_infoGaDen.setDisable(true);
                cb_soHieuTau.setDisable(true);
                if (lichTrinh.getThoiGianKhoiHanh().isBefore(LocalDateTime.now())) {
                    infoGioDi.setDisable(true);
                    infoPhutDi.setDisable(true);
                    datePicker_tgKhoiHanh.setDisable(true);
                    infoGioDen.setDisable(true);
                    infoPhutDen.setDisable(true);
                    datePicker_tgDKDen.setDisable(true);
                    cb_infoTrangThaiHoatDong.setDisable(true);
                    btn_update.setDisable(true);
                } else {
                    btn_update.setDisable(false);
                    infoGioDi.setDisable(false);
                    infoPhutDi.setDisable(false);
                    datePicker_tgKhoiHanh.setDisable(false);
                    infoGioDen.setDisable(false);
                    infoPhutDen.setDisable(false);
                    datePicker_tgDKDen.setDisable(false);
                    cb_infoTrangThaiHoatDong.setDisable(false);
                }
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

        infoGioDi.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d*")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Vui lòng nhập số");
                alert.show();
                infoGioDi.setText(oldValue);
            } else {
                if (newValue.length() == 2) {
                    int gio = Integer.parseInt(newValue);
                    if (gio < 0 || gio > 23) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setHeaderText("Giờ chỉ từ 0-23");
                        alert.show();
                        infoGioDi.setText(oldValue);
                    } else {
                        infoGioDi.setText(newValue);
                        infoPhutDi.requestFocus();
                    }
                }
            }
        });

        infoPhutDi.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d*")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Vui lòng nhập số");
                alert.show();
                infoPhutDi.setText(oldValue);
            } else {
                if (newValue.length() == 2) {
                    int phut = Integer.parseInt(newValue);
                    if (phut < 0 || phut > 59) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setHeaderText("Phút chỉ từ 0-59");
                        alert.show();
                        infoPhutDi.setText(oldValue);
                    } else {
                        infoPhutDi.setText(newValue);
                        datePicker_tgKhoiHanh.requestFocus();
                    }
                }
            }
        });

        infoGioDen.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d*")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Vui lòng nhập số");
                alert.show();
                infoGioDen.setText(oldValue);
            } else {
                if (newValue.length() == 2) {
                    int gio = Integer.parseInt(newValue);
                    if (gio < 0 || gio > 23) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setHeaderText("Giờ chỉ từ 0-23");
                        alert.show();
                        infoGioDen.setText(oldValue);
                    } else {
                        infoGioDen.setText(newValue);
                        infoPhutDen.requestFocus();
                    }
                }
            }
        });

        infoPhutDen.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d*")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Vui lòng nhập số");
                alert.show();
                infoPhutDen.setText(oldValue);
            } else {
                if (newValue.length() == 2) {
                    int phut = Integer.parseInt(newValue);
                    if (phut < 0 || phut > 59) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setHeaderText("Phút chỉ từ 0-59");
                        alert.show();
                        infoPhutDen.setText(oldValue);
                    } else {
                        infoPhutDen.setText(newValue);
                        datePicker_tgDKDen.requestFocus();
                    }
                }
            }
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
        list = new ArrayList<>();
        Ga gaDi = ga_DAO.getGaTheoTenGa(cb_GaDi.getValue());
        Ga gaDen = ga_DAO.getGaTheoTenGa(cb_GaDen.getValue());
        LocalDate ngayKH = dp_ngayKH.getValue();
        if (gaDi == null && gaDen == null && ngayKH == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Vui lòng nhập thông tin tìm kiếm");
            alert.show();
            cb_GaDi.requestFocus();
            return;
        }
        if (ngayKH == null) {
            list = lichTrinh_DAO.traCuuDSLichTrinh(gaDi.getMaGa(), gaDen.getMaGa());
        } else {
            if (gaDi != null && gaDen != null) {
                list = lichTrinh_DAO.traCuuDSLichTrinh(gaDi.getMaGa(), gaDen.getMaGa(), ngayKH);
            } else {
                list = lichTrinh_DAO.traCuuDSLichTrinhTheoNgay(ngayKH);
            }
        }
        if (list.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Không tìm thấy lịch trình nào");
            alert.show();
            clear();
            cb_GaDi.requestFocus();
            return;
        }
        tbl_lichTrinh.setItems(null);
        tbl_lichTrinh.setItems(FXCollections.observableArrayList(list));
    }

    protected void clear() {
        txt_maLichTrinh.clear();
        cb_soHieuTau.setValue(null);
        cb_soHieuTau.setPromptText("Số hiệu tàu");
        cb_soHieuTau.setDisable(false);
        cb_GaDi.setValue(null);
        cb_GaDi.setPromptText("Ga đi");
        cb_GaDen.setValue(null);
        cb_GaDen.setPromptText("Ga đến");
        dp_ngayKH.setValue(null);
        cb_infoGaDi.setValue(null);
        cb_infoGaDi.setPromptText("Ga đi");
        cb_infoGaDi.setDisable(false);
        cb_infoGaDen.setValue(null);
        cb_infoGaDen.setPromptText("Ga đến");
        cb_infoGaDen.setDisable(false);
        infoGioDi.clear();
        infoPhutDi.clear();
        datePicker_tgKhoiHanh.setValue(null);
        infoGioDen.clear();
        infoPhutDen.clear();
        datePicker_tgDKDen.setValue(null);
        cb_infoTrangThaiHoatDong.setValue(null);
        tbl_lichTrinh.setItems(null);
        tbl_lichTrinh.setItems(FXCollections.observableArrayList(list));
        cb_GaDi.requestFocus();
    }

    protected void add() {
        // TODO Auto-generated method
        String maLichTrinh;
        String soHieuTau = cb_soHieuTau.getValue();
        String gaDi = cb_infoGaDi.getValue();
        String gaDen = cb_infoGaDen.getValue();
        String gioKH = infoGioDi.getText();
        String phutKH = infoPhutDi.getText();
        LocalDate ngayKH = datePicker_tgKhoiHanh.getValue();
        String trangThai = cb_infoTrangThaiHoatDong.getValue();

        String ngayKHStr = ngayKH.format(DateTimeFormatter.ofPattern("ddMMyy"));
        maLichTrinh = "LT" + soHieuTau + ngayKHStr + ga_DAO.getGaTheoTenGa(gaDi).getMaGa() + ga_DAO.getGaTheoTenGa(gaDen).getMaGa();
        if (soHieuTau == null || gaDi == null || gaDen == null || gioKH == null || phutKH == null || ngayKH == null || trangThai == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Vui lòng nhập đầy đủ 12 thông tin");
            alert.show();
            return;
        }
        else {
            try {
                int gio = Integer.parseInt(gioKH);
                int phut = Integer.parseInt(phutKH);
                if (gio < 0 || gio > 23 || phut < 0 || phut > 59) {
                    throw new IllegalArgumentException("Giờ hoặc phút không hợp lệ!");
                }
                txt_maLichTrinh.setText(maLichTrinh);
                LocalDateTime thoiGianDen = tinhTGDen();
                LichTrinh lichTrinh = new LichTrinh(maLichTrinh, chuyenTau_DAO.getChuyenTauTheoID(soHieuTau), ga_DAO.getGaTheoTenGa(gaDi), ga_DAO.getGaTheoTenGa(gaDen), LocalDateTime.of(ngayKH, LocalTime.of(gio, phut)), thoiGianDen, trangThai.equals("Hoạt động") ? true : false);
                if (lichTrinh_DAO.create(lichTrinh)) {
                    ct_LichTrinh_DAO.addChiTietLichTrinh(maLichTrinh);
                    list.add(lichTrinh);
                    ObservableList<LichTrinh> updatedList = FXCollections.observableArrayList(list);
                    tbl_lichTrinh.getItems().clear();
                    tbl_lichTrinh.setItems(updatedList);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText("Thêm lịch trình thành công");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText("Thêm lịch trình thất bại");
                    alert.show();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Giờ và phút phải là số hợp lệ");
                alert.show();
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
        }

    }

    protected void update() {
        // TODO Auto-generated method stub
        String maLichTrinh = txt_maLichTrinh.getText();
        String soHieuTau = cb_soHieuTau.getValue();
        String gaDi = cb_infoGaDi.getValue();
        String gaDen = cb_infoGaDen.getValue();
        LocalDate ngayKH = datePicker_tgKhoiHanh.getValue();
        String gioKH = infoGioDi.getText();
        String phutKH = infoPhutDi.getText();

        String trangThai = cb_infoTrangThaiHoatDong.getValue();
        if (ngayKH == null || gioKH == null || phutKH == null || trangThai == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Vui lòng nhập đầy đủ thông tin");
            alert.show();
            return;
        }

        try {
            int gio = Integer.parseInt(gioKH);
            int phut = Integer.parseInt(phutKH);

            if (gio < 0 || gio > 23 || phut < 0 || phut > 59) {
                throw new IllegalArgumentException("Giờ hoặc phút không hợp lệ!");
            }

            LocalDateTime thoiGianDen = tinhTGDen();

            LichTrinh lichTrinhNew = new LichTrinh(maLichTrinh, chuyenTau_DAO.getChuyenTauTheoID(soHieuTau), ga_DAO.getGaTheoTenGa(gaDi), ga_DAO.getGaTheoTenGa(gaDen), LocalDateTime.of(ngayKH, LocalTime.of(gio, phut)), thoiGianDen, trangThai.equals("Hoạt động") ? true : false);
            if (lichTrinh_DAO.updateInfo(lichTrinhNew)) {
                int index = tbl_lichTrinh.getSelectionModel().getSelectedIndex();
                list.set(index, lichTrinhNew);
                ObservableList<LichTrinh> updatedList = FXCollections.observableArrayList(list);
                tbl_lichTrinh.getItems().clear();
                tbl_lichTrinh.setItems(updatedList);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Cập nhật lịch trình thành công");
                alert.show();

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Cập nhật lịch trình thất bại");
                alert.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Giờ và phút phải là số hợp lệ");
            alert.show();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
    }

    protected LocalDateTime tinhTGDen() {
//        double khoangCach = ga_DAO.KhoangCach(ga_DAO.getGaTheoTenGa(cb_infoGaDen.getValue()).getMaGa());
//        if (khoangCach == 0) {
//            khoangCach = ga_DAO.KhoangCach(ga_DAO.getGaTheoTenGa(cb_infoGaDi.getValue()).getMaGa());
//        }
//        double vanToc = cb_soHieuTau.getValue().startsWith("SE") ? 76.58 : 60;
//        double thoiGianPhut = (khoangCach / vanToc) * 60;
//
//        LocalDateTime thoiGianDen = LocalDateTime.of(
//                datePicker_tgKhoiHanh.getValue(),
//                LocalTime.of(Integer.parseInt(infoGioDi.getText()), Integer.parseInt(infoPhutDi.getText()))
//        ).plusMinutes((long) thoiGianPhut);
//
//        if (thoiGianDen.getHour() >= 24) {
//            thoiGianDen = thoiGianDen.plusDays(1);
//        }
//
//        datePicker_tgDKDen.setValue(thoiGianDen.toLocalDate());
//        infoGioDen.setText(String.valueOf(thoiGianDen.getHour()));
//        infoPhutDen.setText(String.valueOf(thoiGianDen.getMinute()));
//
//        return thoiGianDen;
        return null;
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
        infoGioDi.clear();
        infoPhutDi.clear();
        datePicker_tgKhoiHanh.setValue(null);
        infoGioDen.clear();
        infoPhutDen.clear();
        datePicker_tgDKDen.setValue(null);
        cb_infoTrangThaiHoatDong.setValue(null);
        cb_infoTrangThaiHoatDong.setPromptText("Trạng thái");
        cb_soHieuTau.requestFocus();
        tbl_lichTrinh.getSelectionModel().clearSelection();
    }
}