package controller;

import dao.*;
import entity.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DoiVeController implements Initializable {

    @FXML
    private ComboBox<String> cb_search;

    @FXML
    private TextField txt_search;

    @FXML
    private Button btn_lamMoi;

    @FXML
    private Button btn_search;

    @FXML
    private TableView<Ve> tbl_thongTinVe;

    @FXML
    private TableColumn<Ve, String> col_maVe;

    @FXML
    private TableColumn<Ve, String> col_maHK;

    @FXML
    private TableColumn<Ve, String> col_tinhTrangVe;

    @FXML
    private TableColumn<Ve, String> col_khuHoi;

    @FXML
    private TableColumn<Ve, String> col_maSoCho;

    @FXML
    private TableColumn<Ve, String> col_maLichTrinh;

    @FXML
    private TableColumn<Ve, LocalDate> col_dob;

    @FXML
    private TableColumn<Ve, String> col_tenHK;

    @FXML
    private TableColumn<Ve, String> col_cccd;

    @FXML
    private ComboBox<String> cb_gaDi;

    @FXML
    private ComboBox<String> cb_gaDen;

    @FXML
    private DatePicker dp_ngayKhoiHanh;

    @FXML
    private Button btnChonChoNgoi;

    @FXML
    private Button btnHuyChon;

    @FXML
    private Button btnChonCD;



//    @FXML
//    private TitledPane ttp_loaiVe;

    @FXML
    private Button btn_traCuuLT;

    @FXML
    private TableView<LichTrinh> tbl_lichTrinh;

    @FXML
    private TableColumn<LichTrinh, Integer> col_stt;

    @FXML
    private TableColumn<LichTrinh, String> col_soHieuTau;

    @FXML
    private TableColumn<LichTrinh, String> col_gaDi;

    @FXML
    private TableColumn<LichTrinh, String> col_gaDen;

    @FXML
    private TableColumn<LichTrinh, String> col_gioKH;

    @FXML
    private TableColumn<LichTrinh, String> col_trangThai;

    @FXML
    private TableColumn<LichTrinh, String> col_Chon;

    @FXML
    private TabPane tabToaTau;

    @FXML
    private AnchorPane acpToaTau;

    @FXML
    private Label label_thongBao;

    @FXML
    private Button btn_xacNhan;

    @FXML
    private Button btn_doiVe;


    LichTrinh_DAO lichTrinh_dao;
    Ve_DAO ve_dao;
    Ga_DAO ga_dao;
    CT_LichTrinh_DAO ctlt_dao;
    CT_HoaDon_DAO cthd_dao;
    HoaDon_DAO hd_dao;
    Ga_DAO ga_DAO;

    private String idBtnChosen = "";
    private Ve ve;
    private String newMaCho = "";
    private String newMaLichTrinh = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
        initDao();

        cb_search.getItems().addAll("Mã hành khách", "Mã vé", "Số CCCD");

        btn_search.setOnAction(e -> {
            String search = txt_search.getText();
            if (cb_search.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn loại tìm kiếm");
                alert.show();
                return;
            }
            if (search.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập thông tin tìm kiếm");
                alert.show();
                return;
            }
            ArrayList<Ve> listVe = new ArrayList<>();
            Ve ve = ve_dao.getVeTheoID(search);
            if (ve != null) {
                listVe.add(ve);
            }
            renderTableVe(listVe);
        });

        btn_lamMoi.setOnAction(e -> {
            txt_search.clear();
            cb_search.getSelectionModel().clearSelection();
            tbl_thongTinVe.getItems().clear();
            tbl_lichTrinh.getItems().clear();
            acpToaTau.setVisible(false);
            tabToaTau.getTabs().clear();
            cb_gaDi.getSelectionModel().clearSelection();
            cb_gaDen.getSelectionModel().clearSelection();
        });

        tbl_thongTinVe.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ve = tbl_thongTinVe.getSelectionModel().getSelectedItem();
                if (ve != null) {
                    String maVe = ve.getMaVe();
                    ChiTietHoaDon cthd = cthd_dao.getCT_HoaDonTheoMaVe(maVe);
                    HoaDon hd = hd_dao.getHoaDonTheoMa(cthd.getHoaDon().getMaHoaDon());

                    String maLichTrinh = ve.getCtlt().getLichTrinh().getMaLichTrinh();
                    LichTrinh lt = lichTrinh_dao.getLichTrinhTheoID(maLichTrinh);
                    LocalDateTime gioTauChay = lt.getThoiGianKhoiHanh();
                    LocalDateTime now = LocalDateTime.now();

                    // Nếu đổi vé trước 24h thì được đổi
                    if (gioTauChay.plusDays(1).isBefore(now)) {
                        label_thongBao.setText("Vé đã quá hạn đổi");
                        ve = null;
                    } else {
                        label_thongBao.setText("");
                    }
                }
            }
        });

        btn_xacNhan.setOnAction(e -> {
            if (ve == null) {
                label_thongBao.setText("Vui lòng chọn vé cần đổi");
                return;
            }
            LichTrinh lt = lichTrinh_dao.getLichTrinhTheoID(ve.getCtlt().getLichTrinh().getMaLichTrinh());
            String tenGaDi = ga_dao.getGaTheoMaGa(lt.getGaDi().getMaGa()).getTenGa();
            String tenGaDen = ga_dao.getGaTheoMaGa(lt.getGaDen().getMaGa()).getTenGa();

            //hiển thị tên ga đi và ga đến lên combobox
            cb_gaDi.getItems().add(tenGaDi);
            cb_gaDen.getItems().add(tenGaDen);
        });

        btn_traCuuLT.setOnAction(e -> {
            String gaDi = cb_gaDi.getValue();
            String gaDen = cb_gaDen.getValue();
            LocalDate ngayKhoiHanh = dp_ngayKhoiHanh.getValue();

            if (gaDi == null || gaDen == null || ngayKhoiHanh == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ga đi, ga đến và ngày khởi hành");
                alert.show();
                return;
            }

            ArrayList<LichTrinh> listLT = lichTrinh_dao.getAll().stream()
                    .filter(lt -> lt.getGaDi().getTenGa().equals(gaDi) && lt.getGaDen().getTenGa().equals(gaDen) && lt.getThoiGianKhoiHanh().toLocalDate().equals(ngayKhoiHanh))
                    .collect(Collectors.toCollection(ArrayList::new));
            renderTableLichTrinh(listLT);
        });

        btnChonChoNgoi.setOnAction(e -> {
            if (idBtnChosen.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ghế");
                alert.show();
                return;
            }
            // mã chố có dạng số hiệu tàu + mã toa + mã ghế
            String soHieuTau = col_soHieuTau.getCellData(tbl_lichTrinh.getSelectionModel().getSelectedIndex());
            String maToa = tabToaTau.getSelectionModel().getSelectedItem().getText().split(":")[0];
            String maGhe = idBtnChosen;
            newMaCho = soHieuTau + maToa + maGhe;
            newMaLichTrinh = tbl_lichTrinh.getSelectionModel().getSelectedItem().getMaLichTrinh();
        });

        btnHuyChon.setOnMouseClicked(e -> {
            if (!Objects.equals(idBtnChosen, "")) {
                Button btn = (Button) tabToaTau.getTabs().get(tabToaTau.getSelectionModel().getSelectedIndex()).getContent().lookup("#" + idBtnChosen);
                btn.setStyle(btn.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
                idBtnChosen = "";
            }
        });

        btn_doiVe.setOnAction(e -> {
            if (ve == null) {
                label_thongBao.setText("Vui lòng chọn vé cần đổi");
                return;
            }
            if (cb_gaDi.getValue() == null || cb_gaDen.getValue() == null || dp_ngayKhoiHanh.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ga đi, ga đến và ngày khởi hành");
                alert.show();
                return;
            }

            //lấy thông tin vé cũ
            HanhKhach hk = ve.getHanhKhach();
            LoaiVe lv = ve.getLoaiVe();
            String tenHK = ve.getTenHanhKhach();
            String soCCCD = ve.getSoCCCD();
            LocalDate dob = ve.getNgaySinh();
            boolean khuHoi = ve.isKhuHoi();
            ChoNgoi cn = new ChoNgoi(newMaCho);
            LichTrinh lt = new LichTrinh(newMaLichTrinh);
            ChiTietLichTrinh ctlt = new ChiTietLichTrinh(cn, lt);
            boolean khuHoiMoi = ve.isKhuHoi();

            //tạo vé mới
            Ve veMoi = new Ve(hk, ctlt, lv, tenHK, soCCCD, dob, "Đã đổi", khuHoiMoi);
            ve_dao.create(veMoi);

            //cập nhật vé cũ
            ve.setTinhTrangVe("Đã đổi");
            ve_dao.update(ve);

            // Gọi phương thức in vé

        });

    }

    public void renderTableVe(ArrayList<Ve> listVe) {
        // TODO
        ObservableList<Ve> data = FXCollections.observableArrayList(listVe);
        tbl_thongTinVe.setItems(data);
        col_maVe.setCellValueFactory(new PropertyValueFactory<>("maVe"));
        col_maHK.setCellValueFactory(new PropertyValueFactory<>("maHK"));
        col_maSoCho.setCellValueFactory(new PropertyValueFactory<>("maSoCho"));
        col_maLichTrinh.setCellValueFactory(new PropertyValueFactory<>("maLT"));
        col_tinhTrangVe.setCellValueFactory(new PropertyValueFactory<>("tinhTrangVe"));
        col_khuHoi.setCellValueFactory(new PropertyValueFactory<>("khuHoi"));
        col_dob.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        col_tenHK.setCellValueFactory(new PropertyValueFactory<>("tenHK"));
        col_cccd.setCellValueFactory(new PropertyValueFactory<>("soCCCD"));
    }

    public void renderTableLichTrinh(ArrayList<LichTrinh> listLT) {
        // TODO
        ObservableList<LichTrinh> data = FXCollections.observableArrayList(listLT);
        tbl_lichTrinh.setItems(data);
        col_stt.setCellValueFactory(new PropertyValueFactory<>("stt"));
        col_soHieuTau.setCellValueFactory(new PropertyValueFactory<>("soHieuTau"));
        col_gaDi.setCellValueFactory(param -> {
            String maGaDi = param.getValue().getGaDi().getMaGa();
            String tenGaDi = new Ga_DAO().getGaTheoMaGa(maGaDi).getTenGa();
            return new SimpleStringProperty(tenGaDi);
        });
        col_gaDen.setCellValueFactory(param -> {
            String maGaDen = param.getValue().getGaDen().getMaGa();
            String tenGaDen = new Ga_DAO().getGaTheoMaGa(maGaDen).getTenGa();
            return new SimpleStringProperty(tenGaDen);
        });
        col_gioKH.setCellValueFactory(param -> {
            LocalDateTime gioKH = param.getValue().getThoiGianKhoiHanh();
            return new SimpleStringProperty(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(gioKH));
        });
        col_trangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        //tạo nút trong cột
        Callback<TableColumn<LichTrinh, String>, TableCell<LichTrinh, String>> cellFactory = (TableColumn<LichTrinh, String> param) -> {
            // make cell containing buttons
            return new TableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Button btnChon = new Button("Xem chi tiết");
                        btnChon.setStyle("-fx-cursor: hand;-fx-background-color: #5098ff;-fx-text-fill: white;-fx-border-radius: 5px;");
                        HBox manageBtn = new HBox(btnChon);
                        manageBtn.setStyle("-fx-alignment:center;");
                        setGraphic(manageBtn);
                        setText(null);
                        btnChon.setOnAction(e -> {
                            //lấy thông tin lịch trình chọn xem
                            LichTrinh lt = tbl_lichTrinh.getItems().get(getIndex());
                            ArrayList<ChiTietLichTrinh> dsctlt = ctlt_dao.getCtltTheoMaLichTrinh(lt.getMaLichTrinh());
                            acpToaTau.setVisible(true);
                            tabToaTau.getTabs().clear();

                            GridPane gridPane1 = new GridPane();
                            tabToaTau.getTabs().add(new Tab("Toa 1: Ngồi mềm điều hòa", gridPane1));

                            gridPane1.setAlignment(Pos.CENTER_LEFT);
                            gridPane1.setPadding(new Insets(10));
                            gridPane1.setVgap(5);
                            gridPane1.setHgap(5);
                            int[][] seatNumbers1 = {
                                    {1, 8, 9, 16, 17, 24, 25, 32, 33, 40, 41, 48, 49, 56, 57, 64},
                                    {2, 7, 10, 15, 18, 23, 26, 31, 34, 39, 42, 47, 50, 55, 58, 63},
                                    {3, 6, 11, 14, 19, 22, 27, 30, 35, 38, 43, 46, 51, 54, 59, 62},
                                    {4, 5, 12, 13, 20, 21, 28, 29, 36, 37, 44, 45, 52, 53, 60, 61}
                            };

                            // Ghế đã được đặt (màu đỏ) và ghế còn trống (màu trắng)
                            int[] bookedSeats1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 44, 49, 55, 62};

                            Pane pane = new Pane();
                            pane.setPrefHeight(60);
                            pane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 2, 0, 2))));
                            Pane pane1 = new Pane();
                            pane1.setPrefHeight(60);
                            pane1.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 2, 0, 2))));
                            gridPane1.add(pane, 9, 0, 1, 2);
                            gridPane1.add(pane1, 9, 4, 1, 2);
                            // Tạo giao diện ghế
                            for (int row = 0; row < 4; row++) {
                                for (int col = 0; col < 16; col++) {
                                    int seatNumber = seatNumbers1[row][col];
                                    Button seatButton = new Button(String.valueOf(seatNumber));
                                    seatButton.setId(String.valueOf(seatNumber));
                                    if (col < 8) {
                                        seatButton.setStyle("-fx-border-color: black; -fx-border-width: 0 0 0 2");
                                    } else {
                                        seatButton.setStyle("-fx-border-color: black; -fx-border-width: 0 2 0 0");
                                    }
                                    seatButton.setMinSize(30, 30); // Kích thước nút ghế

                                    // Kiểm tra trạng thái của ghế
                                    if (DoiVeController.this.contains(bookedSeats1, seatNumber)) {
                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: red; -fx-text-fill: white;");
                                    } else {
                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
                                    }

                                    // Thêm nút vào GridPane
                                    // Với row = 2 thêm lối đi ở giữa (tăng thêm 1 để tạo khoảng trống)
                                    gridPane1.add(seatButton, col + (col >= 8 ? 2 : 0), row > 1 ? row + 2 : row);
                                    seatButton.setOnMouseClicked(event -> {
                                        if (seatButton.getStyle().contains("red")) {
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Lỗi");
                                            alert.setHeaderText(null);
                                            alert.setContentText("Ghế đã được đặt");
                                            alert.show();
                                        } else {
                                            if (!idBtnChosen.isEmpty()) {
                                                Button btn = (Button) gridPane1.lookup("#" + idBtnChosen);
                                                btn.setStyle(btn.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
                                            }
                                            idBtnChosen = seatButton.getId();
                                            seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: blue; -fx-text-fill: white;");
                                        }
                                    });
                                }
                            }
                            //Toa 2: giường nằm khoang 6 điều hòa
                            GridPane gridPane = new GridPane();
                            gridPane.setAlignment(Pos.CENTER_LEFT);
                            gridPane.setVgap(5);
                            gridPane.setHgap(5);
                            gridPane.add(new Label("T3"), 0, 1);
                            gridPane.add(new Label("T2"), 0, 2);
                            gridPane.add(new Label("T1"), 0, 3);
                            gridPane.add(new Label("  Khoang 1"), 1, 0, 3, 1);
                            gridPane.add(new Label("  Khoang 2"), 4, 0, 3, 1);
                            gridPane.add(new Label("  Khoang 3"), 7, 0, 3, 1);
                            gridPane.add(new Label("  Khoang 4"), 10, 0, 3, 1);
                            gridPane.add(new Label("  Khoang 5"), 13, 0, 3, 1);
                            gridPane.add(new Label("  Khoang 6"), 16, 0, 3, 1);
                            gridPane.add(new Label("  Khoang 7"), 19, 0, 3, 1);
                            // Số thứ tự ghế như hình (theo hàng từ T1 đến T3 và các khoang 1-7)
                            int[][] seatNumbers = {
                                    {5, 6, 11, 12, 17, 18, 23, 24, 29, 30, 35, 36, 41, 42},
                                    {3, 4, 9, 10, 15, 16, 21, 22, 27, 28, 33, 34, 39, 40},
                                    {1, 2, 7, 8, 13, 14, 19, 20, 25, 26, 31, 32, 37, 38}
                            };

                            // Ghế đã được đặt (màu đỏ) và ghế còn trống (màu trắng hoặc xanh)
                            int[] bookedSeats = {1, 2, 3, 4, 7, 8, 9, 10, 13, 14, 15, 16, 19, 20, 25, 26, 31, 32, 34, 37, 38};
                            int[] blueSeats = {5, 6, 11, 12, 17, 18};

                            // Tạo giao diện
                            for (int row = 0; row < 3; row++) {
                                for (int col = 0; col < 14; col++) {
                                    int seatNumber = seatNumbers[row][col];
                                    Button seatButton = new Button(String.valueOf(seatNumber));
                                    seatButton.setId(seatNumber + "");
                                    seatButton.setMinSize(30, 30); // Kích thước nút ghế
                                    seatButton.setStyle("-fx-border-color: black; -fx-border-width: 0 0 2 0");
                                    // Kiểm tra trạng thái của ghế
                                    if (DoiVeController.this.contains(bookedSeats, seatNumber)) {
                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: red; -fx-text-fill: white;");
                                    } else if (DoiVeController.this.contains(blueSeats, seatNumber)) {
                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: blue; -fx-text-fill: white;");
                                    } else {
                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
                                    }

                                    // Thêm nút vào GridPane (với col/2 để tạo khoảng trống giữa các khoang)
                                    gridPane.add(seatButton, col + (col / 2) + 1, row + 1);
                                    seatButton.setOnMouseClicked(event -> {
                                        if (seatButton.getStyle().contains("red")) {
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Lỗi");
                                            alert.setHeaderText(null);
                                            alert.setContentText("Ghế đã được đặt");
                                            alert.show();
                                        } else {
                                            if (!idBtnChosen.isEmpty()) {
                                                Button btn = (Button) gridPane1.lookup("#" + idBtnChosen);
                                                btn.setStyle(btn.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
                                            }
                                            idBtnChosen = seatButton.getId();
                                            seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: blue; -fx-text-fill: white;");
                                        }
                                    });
                                }
                            }
                            tabToaTau.getTabs().add(new Tab("Toa 2: Giường nằm khoang 6 điều hòa", gridPane));
                            //Toa 3: Giường nằm khoang 4 điều hòa
                            int[][] seatNumbers2 = {
                                    {3, 4, 7, 8, 11, 12, 15, 16, 19, 20, 23, 24, 27, 28},
                                    {1, 2, 5, 6, 9, 10, 13, 14, 17, 18, 21, 22, 25, 26}
                            };

                            // Ghế đã được đặt (màu đỏ) và ghế còn trống (màu trắng hoặc xanh)
                            GridPane gridPane2 = new GridPane();
                            gridPane2.setAlignment(Pos.CENTER_LEFT);
                            gridPane2.setVgap(5);
                            gridPane2.setHgap(5);
                            gridPane2.add(new Label("T2"), 0, 1);
                            gridPane2.add(new Label("T1"), 0, 2);
                            gridPane2.add(new Label("  Khoang 1"), 1, 0, 3, 1);
                            gridPane2.add(new Label("  Khoang 2"), 4, 0, 3, 1);
                            gridPane2.add(new Label("  Khoang 3"), 7, 0, 3, 1);
                            gridPane2.add(new Label("  Khoang 4"), 10, 0, 3, 1);
                            gridPane2.add(new Label("  Khoang 5"), 13, 0, 3, 1);
                            gridPane2.add(new Label("  Khoang 6"), 16, 0, 3, 1);
                            gridPane2.add(new Label("  Khoang 7"), 19, 0, 3, 1);
                            // Số thứ tự ghế như hình (theo hàng từ T1 đến T2 và các khoang 1-4)
                            for (int row = 0; row < 2; row++) {
                                for (int col = 0; col < 14; col++) {
                                    int seatNumber = seatNumbers2[row][col];
                                    Button seatButton = new Button(String.valueOf(seatNumber));
                                    seatButton.setId(seatNumber + "");
                                    seatButton.setMinSize(30, 30); // Kích thước nút ghế
                                    seatButton.setStyle("-fx-border-color: black; -fx-border-width: 0 0 2 0");
                                    // Kiểm tra trạng thái của ghế
                                    if (DoiVeController.this.contains(bookedSeats, seatNumber)) {
                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: red; -fx-text-fill: white;");
                                    } else if (DoiVeController.this.contains(blueSeats, seatNumber)) {
                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: blue; -fx-text-fill: white;");
                                    } else {
                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
                                    }

                                    // Thêm nút vào GridPane (với col/2 để tạo khoảng trống giữa các khoang)
                                    gridPane2.add(seatButton, col + (col / 2) + 1, row + 1);
                                    seatButton.setOnMouseClicked(event -> {
                                        if (seatButton.getStyle().contains("red")) {
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Lỗi");
                                            alert.setHeaderText(null);
                                            alert.setContentText("Ghế đã được đặt");
                                            alert.show();
                                        } else {
                                            if (!idBtnChosen.isEmpty()) {
                                                Button btn = (Button) gridPane1.lookup("#" + idBtnChosen);
                                                btn.setStyle(btn.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
                                            }
                                            idBtnChosen = seatButton.getId();
                                            seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: blue; -fx-text-fill: white;");
                                        }
                                    });
                                }
                            }
                            tabToaTau.getTabs().add(new Tab("Toa 3: Giường nằm khoang 4 điều hòa", gridPane2));
                        });
                    }
                }
            };
        };
        col_Chon.setCellFactory(cellFactory);
    }


    public void initDao() {
        lichTrinh_dao = new LichTrinh_DAO();
        ve_dao = new Ve_DAO();
        ga_dao = new Ga_DAO();
        ctlt_dao = new CT_LichTrinh_DAO();
        cthd_dao = new CT_HoaDon_DAO();
        hd_dao = new HoaDon_DAO();
        ga_DAO = new Ga_DAO();
    }

    private boolean contains(int[] array, int value) {
        for (int i : array) {
            if (i == value) {
                return true;
            }
        }

        return false;
    }
}
