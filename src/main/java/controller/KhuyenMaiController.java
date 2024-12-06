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

    private KhuyenMai_DAO khuyenMai_dao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        khuyenMai_dao = new KhuyenMai_DAO();
        ArrayList<KhuyenMai> list = khuyenMai_dao.getAllKM();


        cb_searchTrangThai.getItems().addAll("Tất cả", "Đang hoạt động", "Ngưng hoạt động");
        cb_searchTrangThai.setValue("Tất cả");
        cb_mucGiam.getItems().addAll("5", "10", "15", "20", "25", "30", "35", "40", "45", "50");
        cb_trangThai.getItems().addAll("Đang áp dụng", "Ngưng áp dụng", "Chưa áp dụng");
        cb_trangThai.setValue("Đang áp dụng");
        renderTable(list);


        btn_search.setOnAction(e -> {
            String maKM = txt_searchMaKM.getText();
            String trangThai = cb_searchTrangThai.getValue();
            LocalDate ngayApDung = dp_searchNgayBatDau.getValue();
//            String ngayKetThuc = dp_searchNgayKetThuc.getValue() == null ? "" : dp_searchNgayKetThuc.getValue().toString();

            ArrayList<KhuyenMai> newList = new ArrayList<>();
            KhuyenMai km;
            boolean boolTrangThai = !trangThai.equals("Ngưng áp dụng");

            if (maKM.isEmpty() && trangThai.equals("Tất cả") && ngayApDung == null) {
                renderTable(list);
            } else {
                if (!maKM.isEmpty()) {
                    km = khuyenMai_dao.getKMTheoMa(maKM);
                    if (km != null) {
                        newList.add(km);
                    }
                }
                if (ngayApDung != null && trangThai.equals("Tất cả")) {
                    newList = khuyenMai_dao.getKMTheoNgay(ngayApDung);
                } else {
                    newList = khuyenMai_dao.getKMTheoNgay(ngayApDung);
                    if (boolTrangThai) {
                        newList.stream()
                                .filter(KhuyenMai::isTrangThai)
                                .collect(Collectors.toCollection(ArrayList::new));
                    } else {
                        newList.stream()
                                .filter(k -> !k.isTrangThai())
                                .collect(Collectors.toCollection(ArrayList::new));
                    }
                }
                renderTable(newList);
            }
        });

        btn_reset.setOnAction(e -> {
            txt_searchMaKM.clear();
            dp_searchNgayBatDau.setValue(null);
            cb_searchTrangThai.setValue("Tất cả");
            renderTable(list);
        });

        btn_clear.setOnAction(e -> {
            txt_maKM.clear();
            txt_moTa.clear();
            cb_mucGiam.setValue(null);
            dp_ngayBatDau.setValue(null);
            dp_ngayKetThuc.setValue(null);
            cb_trangThai.setValue("Đang áp dụng");
        });

        btn_add.setOnAction(e -> {
            String maKM = txt_maKM.getText();
            String moTa = txt_moTa.getText();
            float mucGiam = Float.parseFloat(cb_mucGiam.getValue());
            LocalDate ngayBatDau = dp_ngayBatDau.getValue();
            LocalDate ngayKetThuc = dp_ngayKetThuc.getValue();
            boolean trangThai = cb_trangThai.getValue().equals("Đang áp dụng");

            KhuyenMai km = new KhuyenMai(maKM, moTa, ngayBatDau, ngayKetThuc, mucGiam, trangThai);
            khuyenMai_dao.themKhuyenMai(km);
            renderTable(khuyenMai_dao.getAllKM());
        });

        btn_update.setOnAction(e -> {
            String maKM = txt_maKM.getText();
            String moTa = txt_moTa.getText();
            float mucGiam = Float.parseFloat(cb_mucGiam.getValue());
            LocalDate ngayBatDau = dp_ngayBatDau.getValue();
            LocalDate ngayKetThuc = dp_ngayKetThuc.getValue();
            boolean trangThai = cb_trangThai.getValue().equals("Đang áp dụng");

            KhuyenMai km = new KhuyenMai(maKM, moTa, ngayBatDau, ngayKetThuc, mucGiam, trangThai);
            khuyenMai_dao.suaKhuyenMai(km);
            renderTable(khuyenMai_dao.getAllKM());
        });

        tbl_khuyenMai.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txt_maKM.setText(newSelection.getMaKM());
                txt_moTa.setText(newSelection.getMoTa());
                cb_mucGiam.setValue(String.valueOf(newSelection.getMucKM() * 100));
                dp_ngayBatDau.setValue(newSelection.getNgayApDung());
                dp_ngayKetThuc.setValue(newSelection.getNgayHetHan());
                cb_trangThai.setValue(newSelection.isTrangThai() ? "Đang áp dụng" : newSelection.getNgayApDung().isBefore(LocalDate.now()) ? "Chưa áp dụng" : "Ngưng áp dụng");
            }
        });
    }

    private void renderTable(ArrayList<KhuyenMai> list) {
        // Code here
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        ObservableList<KhuyenMai> data = FXCollections.observableArrayList(list);

        tbl_khuyenMai.setItems(data);
        col_maKM.setCellValueFactory(new PropertyValueFactory<>("maKM"));
        col_moTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        col_mucGiam.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getMucKM() * 100)));
        col_batDau.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getNgayApDung().format(formatter)));
        col_ketThuc.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getNgayHetHan().format(formatter)));
        col_trangThai.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().isTrangThai() ? "Đang áp dụng" : param.getValue().getNgayApDung().isBefore(LocalDate.now()) ? "Chưa áp dụng" : "Ngưng áp dụng"));
    }
}
