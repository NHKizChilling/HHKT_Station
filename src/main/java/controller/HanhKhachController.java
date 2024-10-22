package controller;

import com.itextpdf.text.DocumentException;
import dao.*;
import entity.ChoNgoi;
import entity.HanhKhach;
import entity.LichTrinh;
import entity.Ve;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HanhKhachController implements Initializable {

    @FXML
    private ComboBox<String> cb_search;

    @FXML
    private TextField txt_searchBar;

    @FXML
    private Button btn_search;

    @FXML
    private Button btn_refresh;

    @FXML
    private TableView<HanhKhach> tbl_hanhKhach;

    @FXML
    private TableView<Ve> tbl_thongTinVe;


    // Cột thông tin hành khách
    @FXML
    private TableColumn<HanhKhach, String> col_cusId;

    @FXML
    private TableColumn<HanhKhach, String> col_name;

//    @FXML
//    private TableColumn<HanhKhach, String> col_gender;
//
//    @FXML
//    private TableColumn<HanhKhach, String> col_dob;

    @FXML
    private TableColumn<HanhKhach, String> col_cccd;

    @FXML
    private TableColumn<HanhKhach, String> col_sdt;

    @FXML
    private TableColumn<HanhKhach, String> col_email;


    // Cột thông tin vé
    @FXML
    private TableColumn<Ve, String> col_maVe;

    @FXML
    private TableColumn<Ve, String> col_maKH;

    @FXML
    private TableColumn<Ve, String> col_tinhTrangVe;

    @FXML
    private TableColumn<Ve, String> col_loaiVe;

    @FXML
    private TableColumn<Ve, String> col_loaiCho;

    @FXML
    private TableColumn<Ve, String> col_thongTinVe;

    @FXML
    private TableColumn<Ve, String> col_tenHK;

    // Cột thông tin hành khách
    @FXML
    private TextField txt_maHK;

    @FXML
    private TextField txt_tenHK;

    @FXML
    private TextField txt_cccd;

//    @FXML
//    private ComboBox<String> cb_gender;
//
//    @FXML
//    private DatePicker datePicker_dob;

    @FXML
    private TextField txt_sdt;

    @FXML
    private TextField txt_email;

    @FXML
    private Button btn_add;

    @FXML
    private Button btn_update;

    @FXML
    private Button btn_clear;

    @FXML
    private Button btn_inVe;

    private Ve_DAO ve_dao;
    private LichTrinh_DAO lichTrinh_dao;
    private HanhKhach_DAO hanhKhach_dao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ve_dao = new Ve_DAO();
        lichTrinh_dao = new LichTrinh_DAO();
        hanhKhach_dao = new HanhKhach_DAO();

        cb_search.getItems().addAll("Mã hành khách", "Số CCCD", "Số điện thoại");
        //cb_gender.getItems().addAll("Nam", "Nữ");

        renderTableHanhKhach(hanhKhach_dao.getAllHanhKhach());

        btn_search.setOnAction(e -> {
            String searchType = cb_search.getValue();
            if (searchType == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Vui lòng chọn loại tìm kiếm");
                alert.show();
                cb_search.requestFocus();
                cb_search.show();
                return;
            }
            String searchBar = txt_searchBar.getText();
            if (searchBar.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Vui lòng nhập từ khóa tìm kiếm");
                alert.show();
                txt_searchBar.requestFocus();
                return;
            }
            ArrayList<HanhKhach> dsHanhKhach = new ArrayList<>();
            HanhKhach hk;
            switch (searchType) {
                case "Mã hành khách":
                    hk = hanhKhach_dao.getHanhKhachTheoMa(searchBar);
                    if (hk != null) {
                        dsHanhKhach.add(hk);
                    }
                    break;
                case "Số CCCD":
                    hk = hanhKhach_dao.getHanhKhachTheoCCCD(searchBar);
                    if (hk != null) {
                        dsHanhKhach.add(hk);
                    }
                    break;
                case "Số điện thoại":
                    hk = hanhKhach_dao.getKhachHangTheoSDT(searchBar);
                    if (hk != null) {
                        dsHanhKhach.add(hk);
                    }
                    break;
            }
            if (dsHanhKhach.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Không tìm thấy kết quả");
                alert.show();
                txt_searchBar.requestFocus();
                return;
            }
            renderTableHanhKhach(dsHanhKhach);
        });

        btn_refresh.setOnAction(e -> {
            txt_searchBar.clear();
            txt_searchBar.requestFocus();
            renderTableHanhKhach(hanhKhach_dao.getAllHanhKhach());
            cb_search.setPromptText("Tìm kiếm theo");
            tbl_thongTinVe.setItems(null);
            txt_maHK.clear();
            txt_tenHK.clear();
            txt_cccd.clear();
            txt_sdt.clear();
            txt_email.clear();
        });

        tbl_hanhKhach.setOnMouseClicked(e -> {
            HanhKhach hk = tbl_hanhKhach.getSelectionModel().getSelectedItem();
            txt_maHK.setText(hk.getMaHanhKhach());
            txt_tenHK.setText(hk.getTenHanhKhach());
            txt_cccd.setText(hk.getSoCCCD());
            //cb_gender.setValue(hk.isGioiTinh() ? "Nữ" : "Nam");
            //datePicker_dob.setValue(hk.getNgaySinh());
            txt_sdt.setText(hk.getSdt());
            txt_email.setText(hk.getEmail());

            ArrayList<Ve> dsVeTheoMaHK = ve_dao.getDSVeTheoMaHK(hk.getMaHanhKhach());
            renderTableVe(dsVeTheoMaHK);
        });

        btn_clear.setOnAction(e -> {
            txt_maHK.clear();
            txt_tenHK.clear();
            txt_cccd.clear();
//            cb_gender.setValue(null);
//            datePicker_dob.setValue(null);
            txt_sdt.clear();
            txt_email.clear();
        });

        btn_add.setOnAction(e -> {
            String tenHK = txt_tenHK.getText();
            String cccd = txt_cccd.getText();
//            boolean gioiTinh = "Nữ".equals(cb_gender.getValue());
//            LocalDate dob = datePicker_dob.getValue();
            String sdt = txt_sdt.getText();
            String email = txt_email.getText();

            if (checkInput(tenHK, cccd, sdt, email)) {
                //HanhKhach hk = new HanhKhach(tenHK, cccd, sdt, dob, gioiTinh, email);
                HanhKhach hk = new HanhKhach(tenHK, cccd, sdt, email);
                if (hanhKhach_dao.create(hk)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thành công");
                    alert.setHeaderText("Thêm hành khách thành công");
                    alert.show();
                    renderTableHanhKhach(hanhKhach_dao.getAllHanhKhach());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText("Thêm hành khách thất bại");
                    alert.show();
                }
            }
        });

        btn_update.setOnAction(e -> {
            String maHK = txt_maHK.getText();
            String tenHK = txt_tenHK.getText();
            String cccd = txt_cccd.getText();
//            boolean gioiTinh = "Nữ".equals(cb_gender.getValue());
//            LocalDate dob = datePicker_dob.getValue();
            String sdt = txt_sdt.getText();
            String email = txt_email.getText();

            if (maHK.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Vui lòng chọn hành khách cần cập nhật");
                alert.show();
                return;
            }

            if (checkInput(tenHK, cccd, sdt, email)) {
                //HanhKhach hk = new HanhKhach(maHK, tenHK, cccd, sdt, dob, gioiTinh, email);
                HanhKhach hk = new HanhKhach(maHK, tenHK, cccd, sdt, email);
                if (hanhKhach_dao.update(hk)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thành công");
                    alert.setHeaderText("Cập nhật hành khách thành công");
                    alert.show();
                    renderTableHanhKhach(hanhKhach_dao.getAllHanhKhach());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText("Cập nhật hành khách thất bại");
                    alert.show();
                }
            }
        });

        tbl_thongTinVe.setOnMouseClicked(e -> {
            Ve ve = tbl_thongTinVe.getSelectionModel().getSelectedItem();
            if (!ve.getTinhTrangVe().equals("DaHuy")) {
                btn_inVe.setDisable(false);
            } else {
                btn_inVe.setDisable(true);

            }
        });

        btn_inVe.setOnAction(e -> {
            HanhKhach hk = tbl_hanhKhach.getSelectionModel().getSelectedItem();
            if (hk == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Vui lòng chọn hành khách cần in vé");
                alert.show();
                return;
            }
            Ve ve = tbl_thongTinVe.getSelectionModel().getSelectedItem();
            if (ve == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Vui lòng chọn vé cần in");
                alert.show();
                return;
            }
            // In vé
            try {
                new PrintPDF().inVe(ve);
            } catch (IOException | DocumentException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void renderTableHanhKhach(ArrayList<HanhKhach> list) {
        ObservableList<HanhKhach> dsHk = FXCollections.observableArrayList(list);
        tbl_hanhKhach.setItems(dsHk);
        col_cusId.setCellValueFactory(new PropertyValueFactory<>("maHanhKhach"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("tenHanhKhach"));
//        col_gender.setCellValueFactory(cellData -> {
//            boolean gioiTinh = cellData.getValue().isGioiTinh();
//            return new SimpleStringProperty(gioiTinh ? "Nữ" : "Nam");
//        });
//        col_dob.setCellValueFactory(param -> new SimpleStringProperty(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(param.getValue().getNgaySinh())));
        col_cccd.setCellValueFactory(new PropertyValueFactory<>("soCCCD"));
        col_sdt.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    public void renderTableVe(ArrayList<Ve> list) {
        ObservableList<Ve> data = FXCollections.observableArrayList(list);
        tbl_thongTinVe.setItems(data);
        col_maVe.setCellValueFactory(new PropertyValueFactory<>("maVe"));
        col_maKH.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getHanhKhach().getMaHanhKhach()));
        col_thongTinVe.setCellValueFactory(p -> {
            Ve ve = ve_dao.getVeTheoID(p.getValue().getMaVe());
            LichTrinh lt = new LichTrinh_DAO().getLichTrinhTheoID(ve.getCtlt().getLichTrinh().getMaLichTrinh());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new SimpleStringProperty(  lt.getChuyenTau().getSoHieutau()+ " - " + new Ga_DAO().getGaTheoMaGa(lt.getGaDi().getMaGa()).getTenGa() + " - " + new Ga_DAO().getGaTheoMaGa(lt.getGaDen().getMaGa()).getTenGa() + "\n" + formatter.format(lt.getThoiGianKhoiHanh()) + " - " + formatter.format(lt.getThoiGianDuKienDen()));
        });
        col_loaiCho.setCellValueFactory(p -> {
            Ve ve = ve_dao.getVeTheoID(p.getValue().getMaVe());
            ChoNgoi cn = new ChoNgoi_DAO().getChoNgoiTheoMa(ve.getCtlt().getChoNgoi().getMaChoNgoi());
            return new SimpleStringProperty(new LoaiToa_DAO().getLoaiToaTheoMa(new Toa_DAO().getToaTheoID(cn.getToa().getMaToa()).getLoaiToa().getMaLoaiToa()).getTenLoaiToa());
        });
        col_tinhTrangVe.setCellValueFactory(new PropertyValueFactory<>("tinhTrangVe"));
        col_loaiVe.setCellValueFactory(p -> {
            Ve ve = ve_dao.getVeTheoID(p.getValue().getMaVe());
            return new SimpleStringProperty(new LoaiVe_DAO().getLoaiVeTheoMa(ve.getLoaiVe().getMaLoaiVe()).getTenLoaiVe());
        });
        col_tenHK.setCellValueFactory(new PropertyValueFactory<>("tenHanhKhach"));
    }

    public boolean checkInput(String tenHK, String cccd, String sdt, String email) {
        if (tenHK.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Tên hành khách không được để trống");
            alert.show();
            txt_tenHK.requestFocus();
            return false;
        }
        if (cccd.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Số CCCD không được để trống");
            alert.show();
            txt_cccd.requestFocus();
            return false;
        }
        if (sdt.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Số điện thoại không được để trống");
            alert.show();
            txt_sdt.requestFocus();
            return false;
        }
        if (email.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Email không được để trống");
            alert.show();
            txt_email.requestFocus();
            return false;
        }
//        if (dob == null) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Lỗi");
//            alert.setHeaderText("Vui lòng chọn ngày sinh");
//            alert.show();
//            datePicker_dob.requestFocus();
//            return false;
//        }
        return true;
    }
}
