package controller;

import dao.KhuyenMai_DAO;
import entity.KhuyenMai;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class KhuyenMaiController implements Initializable, Serializable {

    @FXML
    private TextField txt_searchMaKM;

    @FXML
    private DatePicker dp_searchNgayBatDau;

    @FXML
    private ComboBox<String> cb_searchTrangThai;

    @FXML
    private Button btn_reset;

    @FXML
    private Button btn_search;

    @FXML
    private TableView<KhuyenMai> tbl_khuyenMai;

    @FXML
    private TableColumn<KhuyenMai, String> col_maKM;

    @FXML
    private TableColumn<KhuyenMai, String> col_moTa;

    @FXML
    private TableColumn<KhuyenMai, String> col_mucGiam;

    @FXML
    private TableColumn<KhuyenMai, String> col_batDau;

    @FXML
    private TableColumn<KhuyenMai, String> col_ketThuc;

    @FXML
    private TableColumn<KhuyenMai, String> col_trangThai;

    @FXML
    private TextField txt_maKM;

    @FXML
    private TextArea txt_moTa;

    @FXML
    private ComboBox<String> cb_mucGiam;

    @FXML
    private DatePicker dp_ngayBatDau;

    @FXML
    private DatePicker dp_ngayKetThuc;

    @FXML
    private ComboBox<String> cb_trangThai;

    @FXML
    private Button btn_clear;

    @FXML
    private Button btn_update;

    @FXML
    private Button btn_add;

    @FXML
    private AnchorPane acpFeature;

    private KhuyenMai_DAO khuyenMai_dao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        khuyenMai_dao = new KhuyenMai_DAO();
        ArrayList<KhuyenMai> list = khuyenMai_dao.getAllKM();


        cb_searchTrangThai.getItems().addAll("Tất cả", "Đang áp dụng", "Ngưng áp dụng", "Chưa áp dụng");
        cb_searchTrangThai.setValue("Tất cả");
        cb_mucGiam.getItems().addAll("5", "10", "15", "20", "25", "30", "35", "40", "45", "50");
        cb_trangThai.getItems().addAll("Đang áp dụng", "Ngưng áp dụng", "Chưa áp dụng");
        cb_trangThai.setValue("Đang áp dụng");
        renderTable(list);


        btn_search.setOnAction(e -> {
            String maKM = txt_searchMaKM.getText();
            String trangThai = cb_searchTrangThai.getValue();
            LocalDate ngayApDung = dp_searchNgayBatDau.getValue();

            ArrayList<KhuyenMai> newList = new ArrayList<>();
            KhuyenMai km;
            boolean boolTrangThai = !trangThai.equals("Ngưng áp dụng") && !trangThai.equals("Chưa áp dụng");

            if (maKM.isEmpty() && trangThai.equals("Tất cả") && ngayApDung == null) {
                renderTable(list);
            } else {
                if (!maKM.isEmpty()) {
                    km = khuyenMai_dao.getKMTheoMa(maKM);
                    if (km != null) {
                        newList.add(km);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setHeaderText(null);
                        alert.setContentText("Không tìm thấy khuyến mãi có mã " + maKM);
                        alert.showAndWait();
                        return;
                    }
                } else if (ngayApDung == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Vui lòng chọn ngày áp dụng");
                    alert.showAndWait();
                    return;
                }
                if (ngayApDung != null && trangThai.equals("Tất cả")) {
                    newList = khuyenMai_dao.getKMTheoNgay(ngayApDung);
                } else if (ngayApDung != null) {
                    newList = khuyenMai_dao.getKMTheoNgay(ngayApDung);
                    if (boolTrangThai) {
                        newList.stream()
                                .filter(KhuyenMai::isTrangThai)
                                .collect(Collectors.toCollection(ArrayList::new));
                    } else {
                        if (trangThai.equals("Ngưng áp dụng")) {
                            newList = newList.stream()
                                    .filter(k -> !k.isTrangThai() && k.getNgayHetHan().isBefore(LocalDate.now()))
                                    .collect(Collectors.toCollection(ArrayList::new));
                        } else {
                            newList = newList.stream()
                                    .filter(k -> !k.isTrangThai() && k.getNgayApDung().isAfter(LocalDate.now()))
                                    .collect(Collectors.toCollection(ArrayList::new));
                        }
                    }
                }
                if (newList.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Không tìm thấy khuyến mãi phù hợp");
                    alert.showAndWait();
                    return;
                }
                renderTable(newList);
            }
        });

        btn_reset.setOnAction(e -> {
            txt_searchMaKM.clear();
            dp_searchNgayBatDau.setValue(null);
            cb_searchTrangThai.setValue("Tất cả");
            btn_add.setDisable(false);
            btn_update.setDisable(true);
            btn_clear.fire();
            renderTable(khuyenMai_dao.getAllKM());
        });

        btn_clear.setOnAction(e -> {
            txt_maKM.clear();
            txt_moTa.clear();
            cb_mucGiam.setValue(null);
            dp_ngayBatDau.setValue(null);
            dp_ngayKetThuc.setValue(null);
            cb_trangThai.setValue("Đang áp dụng");
            btn_add.setDisable(false);
            btn_update.setDisable(true);
            tbl_khuyenMai.getSelectionModel().clearSelection();
        });

        btn_add.setOnAction(e -> {
            if (isValid()) {
                String maKM = txt_maKM.getText();
                String moTa = txt_moTa.getText();
                int mucGiam = Integer.parseInt(cb_mucGiam.getValue());
                LocalDate ngayBatDau = dp_ngayBatDau.getValue();
                LocalDate ngayKetThuc = dp_ngayKetThuc.getValue();
                boolean trangThai = cb_trangThai.getValue().equals("Đang áp dụng");

                KhuyenMai km = new KhuyenMai(maKM, moTa, ngayBatDau, ngayKetThuc, (float) mucGiam / 100, trangThai);
                if (khuyenMai_dao.themKhuyenMai(km)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Tạo khuyến mãi thành công");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Tạo khuyến mãi thất bại");
                    alert.showAndWait();
                }
                renderTable(khuyenMai_dao.getAllKM());
                btn_clear.fire();
            }
        });

        btn_update.setOnAction(e -> {
            if (isValid()) {
                String maKM = txt_maKM.getText();
                String moTa = txt_moTa.getText();
                int mucGiam = Integer.parseInt(cb_mucGiam.getValue());
                LocalDate ngayBatDau = dp_ngayBatDau.getValue();
                LocalDate ngayKetThuc = dp_ngayKetThuc.getValue();
                boolean trangThai = cb_trangThai.getValue().equals("Đang áp dụng");

                KhuyenMai km = new KhuyenMai(maKM, moTa, ngayBatDau, ngayKetThuc, (float) mucGiam / 100, trangThai);
                if (khuyenMai_dao.suaKhuyenMai(km)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Cập nhật khuyến mãi thành công");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Cập nhật khuyến mãi thất bại");
                    alert.showAndWait();
                }
                renderTable(khuyenMai_dao.getAllKM());
                btn_clear.fire();
            }
        });

        if (getData.nv.getChucVu().equals("Nhân viên")){
            acpFeature.setDisable(true);
        }

        tbl_khuyenMai.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txt_maKM.setText(newSelection.getMaKM());
                txt_moTa.setText(newSelection.getMoTa());
                cb_mucGiam.setValue(String.format("%.0f", newSelection.getMucKM() * 100));
                dp_ngayBatDau.setValue(newSelection.getNgayApDung());
                dp_ngayKetThuc.setValue(newSelection.getNgayHetHan());
                cb_trangThai.setValue(newSelection.isTrangThai() ? "Đang áp dụng" : !newSelection.getNgayApDung().isBefore(LocalDate.now()) ? "Chưa áp dụng" : "Ngưng áp dụng");
                btn_add.setDisable(true);
                btn_update.setDisable(false);
            }
        });
    }

    private void renderTable(ArrayList<KhuyenMai> list) {
        // Code here
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        ObservableList<KhuyenMai> data = FXCollections.observableArrayList(list);

        tbl_khuyenMai.refresh();
        tbl_khuyenMai.setItems(data);
        col_maKM.setCellValueFactory(new PropertyValueFactory<>("maKM"));
        col_moTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        col_mucGiam.setCellValueFactory(param -> new SimpleStringProperty(String.format("%.0f", param.getValue().getMucKM() * 100)));
        col_batDau.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getNgayApDung().format(formatter)));
        col_ketThuc.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getNgayHetHan().format(formatter)));
        col_trangThai.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().isTrangThai() ? "Đang áp dụng" : param.getValue().getNgayApDung().isBefore(LocalDate.now()) ? "Ngưng áp dụng" : "Chưa áp dụng"));
    }

    private boolean isValid() {
        String moTa = txt_moTa.getText();
        LocalDate ngayBatDau = dp_ngayBatDau.getValue();
        LocalDate ngayKetThuc = dp_ngayKetThuc.getValue();

        if (cb_mucGiam.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn mức giảm giá");
            alert.showAndWait();
            cb_mucGiam.requestFocus();
            return false;
        }

        if (ngayBatDau == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn ngày bắt đầu");
            alert.showAndWait();
            dp_ngayBatDau.requestFocus();
            return false;
        }

        if (ngayKetThuc == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn ngày kết thúc");
            alert.showAndWait();
            dp_ngayKetThuc.requestFocus();
            return false;
        }

        if (ngayKetThuc.isBefore(ngayBatDau)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Ngày kết thúc phải sau ngày bắt đầu");
            alert.showAndWait();
            dp_ngayKetThuc.requestFocus();
            return false;
        }

        if (moTa.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập mô tả");
            alert.showAndWait();
            txt_moTa.requestFocus();
            return false;
        }

        return true;
    }
}
