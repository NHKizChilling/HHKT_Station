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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LichTrinhController implements Initializable {
    private LichTrinh_DAO lt_dao = new LichTrinh_DAO();
    private CT_LichTrinh_DAO ctlt_dao = new CT_LichTrinh_DAO();
    private Ga_DAO ga_dao = new Ga_DAO();
    private HanhKhach_DAO hk_dao = new HanhKhach_DAO();
    private Ve_DAO ve_dao = new Ve_DAO();
    private HoaDon_DAO hd_dao = new HoaDon_DAO();
    private ChuyenTau_DAO ct_dao = new ChuyenTau_DAO();
    private Toa_DAO toa_dao = new Toa_DAO();
    private LoaiToa_DAO ltoa_dao = new LoaiToa_DAO();
    private LoaiVe_DAO lv_dao = new LoaiVe_DAO();
    private ChoNgoi_DAO cn_dao = new ChoNgoi_DAO();

    @FXML
    private ComboBox<String> cb_gaDen;

    @FXML
    private ComboBox<String> cb_gaDi;

    @FXML
    private ComboBox<String> cb_timKiem;

    @FXML
    private ComboBox<String> cbo_timGaDen;

    @FXML
    private ComboBox<String> cbo_timGaDi;

    @FXML
    private ComboBox<String> cb_trangThai;

    @FXML
    private DatePicker tgKhoiHanh;

    @FXML
    private DatePicker tgDK;

    @FXML
    private DatePicker tgKHSearch;

    @FXML
    private Button btn_xoaTrang;

    @FXML
    private Button btn_timKiem;

    @FXML
    private Button btn_capNhat;

    @FXML
    private Button btnThemChuyenTau;

    @FXML
    private Button btn_xoa;

    @FXML
    private TableColumn<LichTrinh, String> col_maLichTrinh;

    @FXML
    private TableColumn<LichTrinh, String> col_soHieuTau;

    @FXML
    private TableColumn<LichTrinh, String> col_gaDi;

    @FXML
    private TableColumn<LichTrinh, String> col_gaDen;

    @FXML
    private TableColumn<LichTrinh, String> col_tgKhoiHanh;

    @FXML
    private TableColumn<LichTrinh, String> col_tgDK;

    @FXML
    private TableColumn<LichTrinh, String> col_trangThaiHoatDong;

    @FXML
    private TableColumn<LichTrinh, String>colChon;

    @FXML
    private TableView<LichTrinh> tbl_ChuyenTau;

    @FXML
    private TextField txt_timKiem;

    @FXML
    private TextField txt_soHieuTau;

    @FXML
    private TextField txt_maLichTrinh;

    private ObservableList<LichTrinh> dslt = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ConnectDB.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        cb_timKiem.getItems().addAll("Ga đi, ga đến", "Mã lịch trình");

        cb_timKiem.setValue("Ga đi, ga đến");
        txt_timKiem.setVisible(false);
        cbo_timGaDi.setVisible(true);
        cbo_timGaDi.setValue(null); // Đặt giá trị ban đầu khi thay đổi lựa chọn
        cbo_timGaDen.setVisible(true);
        cbo_timGaDen.setValue(null); // Đặt giá trị ban đầu khi thay đổi lựa chọn
        tgKHSearch.setVisible(true);
        tgKHSearch.setValue(null);
        cbo_timGaDen.requestFocus();
        cb_timKiem.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue.equals("Mã lịch trình")) {
                txt_timKiem.setVisible(true);
                cbo_timGaDi.setVisible(false);
                cbo_timGaDen.setVisible(false);
                tgKHSearch.setVisible(false);
                txt_timKiem.requestFocus();
            } else if (newValue.equals("Ga đi, ga đến")) {
                txt_timKiem.setVisible(false);
                cbo_timGaDi.setVisible(true);
                cbo_timGaDi.setValue(null); // Đặt giá trị ban đầu khi thay đổi lựa chọn
                cbo_timGaDen.setVisible(true);
                cbo_timGaDen.setValue(null); // Đặt giá trị ban đầu khi thay đổi lựa chọn
                cbo_timGaDen.requestFocus();
                tgKHSearch.setVisible(true);
                tgKHSearch.setValue(null);
            }
        });

        tbl_ChuyenTau.setStyle(tbl_ChuyenTau.getStyle() +
                "-fx-selection-bar: #FFD700; " +
                "-fx-selection-bar-non-focused: #FFD700; " +
                "-fx-alignment: CENTER; " +
                "-fx-content-display: CENTER; " +
                "-fx-background-color: #fff;");

        ArrayList<Ga> ga = new Ga_DAO().getAllGa();
        ArrayList<String> tenGa = new ArrayList<>();
        for (Ga g : ga) {
            tenGa.add(g.getTenGa());
        }
        cb_trangThai.setItems(FXCollections.observableArrayList("Không hoạt động", "Hoạt động"));

        cbo_timGaDi.setItems(FXCollections.observableList(tenGa));
        cbo_timGaDen.setItems(FXCollections.observableList(tenGa));
        new AutoCompleteComboBoxListener<>(cbo_timGaDi);
        new AutoCompleteComboBoxListener<>(cbo_timGaDen);

        cb_gaDi.setItems(FXCollections.observableList(tenGa));
        cb_gaDen.setItems(FXCollections.observableList(tenGa));
        new AutoCompleteComboBoxListener<>(cb_gaDi);
        new AutoCompleteComboBoxListener<>(cb_gaDen);

        cb_gaDen.setOnAction(e -> {
            if (cb_gaDi.getValue() != null && cb_gaDen.getValue() != null) {
                if (cb_gaDi.getValue().equals(cb_gaDen.getValue())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Ga đến và ga đi không được trùng nhau");
                    alert.show();
                    cb_gaDen.setValue(null);
                    cb_gaDen.requestFocus();
                }
            }
        });

        cb_gaDi.setOnAction(e -> {
            if (cb_gaDi.getValue() != null && cb_gaDen.getValue() != null) {
                if (cb_gaDi.getValue().equals(cb_gaDen.getValue())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Ga đến và ga đi không được trùng nhau");
                    alert.show();
                    cb_gaDi.setValue(null);
                    cb_gaDi.requestFocus();
                }
            }
        });
        cbo_timGaDen.setOnAction(e -> {
            if (cbo_timGaDi.getValue() != null && cbo_timGaDen.getValue() != null) {
                if (cbo_timGaDi.getValue().equals(cbo_timGaDen.getValue())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Ga đến và ga đi không được trùng nhau");
                    alert.show();
                    cbo_timGaDen.setValue(null);
                    cbo_timGaDen.requestFocus();
                }
            }
        });

        cbo_timGaDi.setOnAction(e -> {
            if (cbo_timGaDi.getValue() != null && cbo_timGaDen.getValue() != null) {
                if (cbo_timGaDi.getValue().equals(cbo_timGaDen.getValue())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Ga đến và ga đi không được trùng nhau");
                    alert.show();
                    cbo_timGaDi.setValue(null);
                    cbo_timGaDi.requestFocus();
                }
            }
        });
        tgKhoiHanh.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });
        tgKHSearch.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });

        btn_xoaTrang.setOnMouseClicked(e -> {
            cb_timKiem.setValue("Ga đi, ga đến");
            txt_timKiem.setVisible(false);
            cbo_timGaDi.setValue(null);
            cbo_timGaDen.setValue(null);
            tgKHSearch.setValue(null);
            cbo_timGaDi.requestFocus();
            cb_gaDi.setValue(null);
            cb_gaDen.setValue(null);
            tgKhoiHanh.setValue(null);
            cb_trangThai.setValue(null);
            cbo_timGaDi.setVisible(true);
            cbo_timGaDen.setVisible(true);
            tgKHSearch.setVisible(true);
            tbl_ChuyenTau.setItems(null);
            txt_maLichTrinh.setText(null);
            txt_soHieuTau.setText(null);
        });

        col_maLichTrinh.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("lichTrinh"));
        col_soHieuTau.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("chuyenTau"));
        col_gaDi.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("gaDi"));
        col_gaDen.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("gaDen"));
        col_tgKhoiHanh.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("thoiGianKhoiHanh"));
        col_tgDK.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("thoiGianDuKienDen"));
        col_trangThaiHoatDong.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("trangThaiHoatDong"));

        btn_timKiem.setOnMouseClicked(e -> {
            tbl_ChuyenTau.setItems(null);
            tbl_ChuyenTau.setVisible(true);

            if (cb_timKiem.getValue() == null || cb_timKiem.getEditor().getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn thông tin tìm kiếm");
                alert.show();
                cb_timKiem.requestFocus();
            } else if (txt_timKiem.getText().isEmpty() && cb_timKiem.getValue().equals("Mã lịch trình")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập thông tin tìm kiếm");
                alert.show();
                txt_timKiem.requestFocus();
            } else if (cbo_timGaDi.getValue() == null && cb_timKiem.getValue().equals("Ga đi, ga đến")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn Ga đi và Ga đến");
                alert.show();
                cbo_timGaDi.requestFocus();
            } else {
                if (cb_timKiem.getValue().equals("Mã lịch trình")) {
                    String maLichTrinh = lt_dao.getLichTrinhTheoID(txt_timKiem.getText()).getMaLichTrinh();
                    ArrayList<LichTrinh> list = lt_dao.traCuuDSLichTrinhTheoID(maLichTrinh);
                    dslt = FXCollections.observableList(list);
                    if (dslt.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setHeaderText(null);
                        alert.setContentText("Không tìm thấy lịch trình phù hợp");
                        alert.show();
                        cb_timKiem.requestFocus();
                    } else {
                        tbl_ChuyenTau.setItems(dslt);
                        tbl_ChuyenTau.requestFocus();
                        populateColumns();
                    }
                } else if (cb_timKiem.getValue().equals("Ga đi, ga đến")) {
                    String maGaDi = ga_dao.getGaTheoTenGa(cbo_timGaDi.getValue()).getMaGa();
                    String maGaDen = ga_dao.getGaTheoTenGa(cbo_timGaDen.getValue()).getMaGa();
                    ArrayList<LichTrinh> list1 = lt_dao.traCuuDSLichTrinh(maGaDi, maGaDen, tgKHSearch.getValue());
                    dslt = FXCollections.observableList(list1);
                    if (dslt.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setHeaderText(null);
                        alert.setContentText("Không tìm thấy lịch trình phù hợp");
                        alert.show();
                        cb_timKiem.requestFocus();
                    } else {
                        tbl_ChuyenTau.setItems(dslt);
                        tbl_ChuyenTau.requestFocus();
                        populateColumns();
                    }
                }
            }
        });

//        btn_capNhat.setOnAction(e -> {
//            String maLT = txt_maLichTrinh.getText().trim();
//            String soHT = txt_soHieuTau.getText().trim();
//            String maGaDi = cb_gaDi.getValue();
//            String maGaDen = cb_gaDen.getValue();
//
//            LocalDate thoiGianKhoiHanhDate = tgKhoiHanh.getValue();
//            LocalDate thoiGianDuKienDate = tgDK.getValue();
//
//            // Chuyển đổi thành LocalDateTime
//            LocalDateTime thoiGianKhoiHanh = thoiGianKhoiHanhDate != null ? thoiGianKhoiHanhDate.atStartOfDay() : null;
//            LocalDateTime thoiGianDuKien = thoiGianDuKienDate != null ? thoiGianDuKienDate.atStartOfDay() : null;
//
//            ChuyenTau chuyenTau = new ChuyenTau(soHT);
//            Ga gaDiObj = new Ga(maGaDi);
//            Ga gaDenObj = new Ga(maGaDen);
//
//            boolean trangThai = cb_trangThai.getValue() != null && cb_trangThai.getValue().equalsIgnoreCase("Hoạt động");
//
//            if (maLT.isBlank()) {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Lỗi");
//                alert.setHeaderText("Chưa chọn lịch trình cần cập nhật");
//                alert.show();
//                return;
//            }
//
//            try {
//                // Tạo đối tượng LichTrinh
//                LichTrinh lt = new LichTrinh(maLT, chuyenTau, gaDiObj, gaDenObj, thoiGianKhoiHanh, thoiGianDuKien, trangThai);
//
//                // Cập nhật thông tin
//                LichTrinh_DAO lichTrinhDAO = new LichTrinh_DAO();
//                if (lichTrinhDAO.updateInfo(lt)) {
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                    alert.setTitle("Thành công");
//                    alert.setHeaderText("Cập nhật lịch trình thành công");
//                    alert.show();
//
//                    // Cập nhật danh sách lịch trình và làm mới bảng
//                    dslt.set(dslt.indexOf(lt), lt);
//                    renderLichTrinhTable(); // Gọi hàm để làm mới bảng
//                } else {
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("Lỗi");
//                    alert.setHeaderText("Cập nhật lịch trình thất bại");
//                    alert.show();
//                }
//            } catch (IllegalArgumentException ex) {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Lỗi");
//                alert.setHeaderText(ex.getMessage());
//                alert.show();
//            } catch (Exception ex) {
//                ex.printStackTrace(); // In ra thông báo lỗi
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Lỗi");
//                alert.setHeaderText("Đã xảy ra lỗi trong quá trình cập nhật lịch trình");
//                alert.show();
//            }
//        });

//        btnThemChuyenTau.setOnAction(e -> {
//            try {
//                String maLT = txt_maLichTrinh.getText().trim();
//                String soHT = txt_soHieuTau.getText().trim();
//                String maGaDi = cb_gaDi.getValue();
//                String maGaDen = cb_gaDen.getValue();
//
//                if (maGaDi == null || maGaDen == null || soHT.isEmpty()) {
//                    Alert alert = new Alert(Alert.AlertType.WARNING);
//                    alert.setTitle("Cảnh báo");
//                    alert.setHeaderText("Thông tin không đầy đủ");
//                    alert.setContentText("Vui lòng nhập đầy đủ thông tin");
//                    alert.show();
//                    return;
//                }
//
//                LocalDate thoiGianKhoiHanhDate = tgKhoiHanh.getValue();
//                LocalDate thoiGianDuKienDate = tgDK.getValue();
//
//                if (thoiGianKhoiHanhDate == null || thoiGianDuKienDate == null) {
//                    Alert alert = new Alert(Alert.AlertType.WARNING);
//                    alert.setTitle("Cảnh báo");
//                    alert.setHeaderText("Thông tin không đầy đủ");
//                    alert.setContentText("Vui lòng chọn thời gian khởi hành và dự kiến");
//                    alert.show();
//                    return;
//                }
//
//                // Chuyển đổi thành LocalDateTime
//                LocalDateTime thoiGianKhoiHanh = thoiGianKhoiHanhDate.atStartOfDay();
//                LocalDateTime thoiGianDuKien = thoiGianDuKienDate.atStartOfDay();
//
//                ChuyenTau chuyenTau = new ChuyenTau(soHT);
//                Ga gaDiObj = new Ga(maGaDi);
//                Ga gaDenObj = new Ga(maGaDen);
//
//                boolean trangThai = cb_trangThai.getValue() != null && cb_trangThai.getValue().equalsIgnoreCase("Hoạt động");
//
//                // Tạo mã lịch trình mới từ mã cũ
//                String maLTcu = dslt.get(dslt.size() - 1).getMaLichTrinh();
//                String ngayDi = maLTcu.substring(2, 10); // lấy ngày từ mã (ví dụ: "20231022")
//                String maLTmoi = "LT" + ngayDi + maGaDen; // Sử dụng mã ga đến từ giao diện
//
//                // Tạo đối tượng LichTrinh
//                LichTrinh lt = new LichTrinh(maLTmoi, chuyenTau, gaDiObj, gaDenObj, thoiGianKhoiHanh, thoiGianDuKien, trangThai);
//
//                // Thêm lịch trình vào database
//                if (lt_dao.create(lt)) {
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                    alert.setTitle("Thành công");
//                    alert.setHeaderText("Thêm lịch trình thành công");
//                    alert.show();
//
//                    dslt.add(lt);
//                } else {
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("Lỗi");
//                    alert.setHeaderText("Thêm lịch trình thất bại");
//                    alert.show();
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Lỗi");
//                alert.setHeaderText("Đã xảy ra lỗi");
//                alert.setContentText(ex.getMessage());
//                alert.show();
//            }
//        });

        btn_xoa.setOnMouseClicked(e -> {
            LichTrinh lichTrinhChon = tbl_ChuyenTau.getSelectionModel().getSelectedItem();
            if (lichTrinhChon != null) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Xác nhận xóa");
                confirmAlert.setHeaderText(null);
                confirmAlert.setContentText("Bạn có chắc chắn muốn xóa lịch trình này không?");
                ButtonType yesButton = new ButtonType("Có", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("Không", ButtonBar.ButtonData.NO);
                confirmAlert.getButtonTypes().setAll(yesButton, noButton);

                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == yesButton) {
                        // Cập nhật trạng thái thành không hoạt động
                        boolean result = lt_dao.updateTrangThaiChuyenTau(lichTrinhChon.getMaLichTrinh(), false);
                        if (result) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Thành công");
                            alert.setHeaderText(null);
                            alert.setContentText("Lịch trình đã được cập nhật thành không hoạt động.");
                            alert.show();

                            dslt.remove(lichTrinhChon);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Lỗi");
                            alert.setHeaderText(null);
                            alert.setContentText("Cập nhật trạng thái thất bại.");
                            alert.show();
                        }
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn lịch trình cần xóa.");
                alert.show();
            }
        });


        tbl_ChuyenTau.setOnMouseClicked(event -> {
                        LichTrinh selectedLichTrinh = tbl_ChuyenTau.getSelectionModel().getSelectedItem();

                        if (selectedLichTrinh != null) {
                            txt_maLichTrinh.setText(selectedLichTrinh.getMaLichTrinh());
                            txt_soHieuTau.setText(selectedLichTrinh.getChuyenTau().getSoHieutau());

                            cb_gaDi.setValue(ga_dao.getGaTheoMaGa(selectedLichTrinh.getGaDi().getMaGa()).getTenGa());
                            cb_gaDen.setValue(ga_dao.getGaTheoMaGa(selectedLichTrinh.getGaDen().getMaGa()).getTenGa());

                            tgKhoiHanh.setValue(selectedLichTrinh.getThoiGianKhoiHanh().toLocalDate());
                            tgDK.setValue(selectedLichTrinh.getThoiGianDuKienDen().toLocalDate());

                            cb_trangThai.setValue(selectedLichTrinh.isTinhTrang() ? "Hoạt động" : "Không hoạt động");
                        }

        });

    }

    private void populateColumns() {
        col_maLichTrinh.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getMaLichTrinh()));

        col_soHieuTau.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getChuyenTau().getSoHieutau()));

        col_gaDi.setCellValueFactory(param ->
                new SimpleStringProperty(ga_dao.getGaTheoMaGa(param.getValue().getGaDi().getMaGa()).getTenGa()));

        col_gaDen.setCellValueFactory(param ->
                new SimpleStringProperty(ga_dao.getGaTheoMaGa(param.getValue().getGaDen().getMaGa()).getTenGa()));

        col_tgKhoiHanh.setCellValueFactory(param ->
                new SimpleStringProperty(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(param.getValue().getThoiGianKhoiHanh())));

        col_tgDK.setCellValueFactory(param ->
                new SimpleStringProperty(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(param.getValue().getThoiGianDuKienDen())));

        col_trangThaiHoatDong.setCellValueFactory(param -> {
            boolean tinhTrang = param.getValue().isTinhTrang();
            return new SimpleStringProperty(tinhTrang ? "Hoạt động" : "Không hoạt động");
        });
    }
    private void renderLichTrinhTable() {
        ObservableList<LichTrinh> lichTrinhList = FXCollections.observableArrayList(dslt); // dslt là danh sách lưu trữ LichTrinh
        tbl_ChuyenTau.setItems(lichTrinhList);
        col_maLichTrinh.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("lichTrinh"));
        col_soHieuTau.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("chuyenTau"));
        col_gaDi.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("gaDi"));
        col_gaDen.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("gaDen"));
        col_tgKhoiHanh.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("thoiGianKhoiHanh"));
        col_tgDK.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("thoiGianDuKienDen"));
        col_trangThaiHoatDong.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("trangThaiHoatDong"));


    }
}
