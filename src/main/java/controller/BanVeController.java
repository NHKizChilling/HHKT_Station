/*
 * @ (#) BanVeController.java       1.0     04/10/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import connectdb.ConnectDB;
import dao.*;
import entity.*;
import gui.TrangChu_GUI;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.control.TitledPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   04/10/2024
 * version: 1.0
 */
public class BanVeController implements Initializable {
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
    private int stt = 0;
    @FXML
    private FontAwesomeIcon btnGiamSLhssv;

    @FXML
    private FontAwesomeIcon btnGiamSLnct;

    @FXML
    private FontAwesomeIcon btnGiamSLnl;

    @FXML
    private FontAwesomeIcon btnGiamSLte;

    @FXML
    private FontAwesomeIcon btnTangSLhssv;

    @FXML
    private FontAwesomeIcon btnTangSLnct;

    @FXML
    private FontAwesomeIcon btnTangSLnl;

    @FXML
    private FontAwesomeIcon btnTangSLte;

    @FXML
    private ComboBox<String> cbGaDen;

    @FXML
    private ComboBox<String> cbGaDi;

    @FXML
    private DatePicker dpNgayKH;

    @FXML
    private DatePicker dpNgayVe;

    @FXML
    private Button btnTraCuuCT;

    @FXML
    private Button btnLamMoi;

    @FXML
    private CheckBox chkKhuHoi;

    @FXML
    private TableColumn<LichTrinh, String> colGaDen;

    @FXML
    private TableColumn<LichTrinh, String> colGaDi;

    @FXML
    private TableColumn<LichTrinh, String> colGioKH;

    @FXML
    private TableColumn<LichTrinh, Integer> colSTT;

    @FXML
    private TableColumn<LichTrinh, String> colSoHieuTau;

    @FXML
    private TableColumn<LichTrinh, String> colTGDen;

    @FXML
    private TableColumn<LichTrinh, String> colSLT;

    @FXML
    private TableColumn<LichTrinh, String> colChon;

    @FXML
    private TableColumn<LichTrinh, String> colGaDenKH;

    @FXML
    private TableColumn<LichTrinh, String> colGaDiKH;

    @FXML
    private TableColumn<LichTrinh, String> colGioKHkh;

    @FXML
    private TableColumn<LichTrinh, Integer> colSTTKH;

    @FXML
    private TableColumn<LichTrinh, String> colSoHieuTauKH;

    @FXML
    private TableColumn<LichTrinh, String> colTGDenKH;

    @FXML
    private TableColumn<LichTrinh, String> colSLTKH;

    @FXML
    private TableColumn<LichTrinh, String> colChonKH;
    @FXML
    private TitledPane ttpLoaiVe;

    @FXML
    private Label lblSLhssv;

    @FXML
    private Label lblSLnct;

    @FXML
    private Label lblSLnl;

    @FXML
    private Label lblSLte;

    @FXML
    private Label lblGiaCN;

    @FXML
    private AnchorPane acpKH;

    @FXML
    private AnchorPane acpToaTau;

    @FXML
    private TabPane tabToaTau;

    @FXML
    private TabPane tabToaTauKH;

    @FXML
    private TabPane tabTTVe;

    @FXML
    private TableView<LichTrinh> tbTCTLT;

    @FXML
    private TableView<LichTrinh> tbTCTLTKH;

    @FXML
    private Button btnTaoHD;

    @FXML
    private Button btnLamMoiCN;

    @FXML
    private Button btnChonCD;

    @FXML
    private Button btnChonKH;

    @FXML
    private  Button btnChonChoNgoi;

    @FXML
    private ScrollPane scr1;

    @FXML
    private ScrollPane scr11;

    @FXML
    private  Button btnHuyChon;

    @FXML
    private TextField txtTenKH;

    @FXML
    private TextField txtSoCCCD;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtEmail;


    private boolean khuHoi = false;

    private ObservableList<LichTrinh> dslt = null;
    private ObservableList<LichTrinh> dsltkh = null;
    private String idBtnChosen = "";

    private String idBtnChonKH = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ConnectDB.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tbTCTLT.setItems(null);

        tbTCTLT.setStyle(tbTCTLT.getStyle() + "-fx-selection-bar: #FFD700; -fx-selection-bar-non-focused: #FFD700;-fx-alignment: CENTER;-fx-content-display: CENTER;-fx-background-color: #fff;");
        tbTCTLTKH.setItems(null);

        tbTCTLTKH.setStyle(tbTCTLT.getStyle() + "-fx-selection-bar: #FFD700; -fx-selection-bar-non-focused: #FFD700;-fx-alignment: CENTER;-fx-content-display: CENTER;-fx-background-color: #fff;");
        chkKhuHoi.setOnAction(e -> {
            if (chkKhuHoi.isSelected()) {
                khuHoi = true;
                dpNgayVe.setDisable(false);
            } else {
                khuHoi = false;
                dpNgayVe.setDisable(true);
                dpNgayVe.setValue(null);
            }
        });
        ArrayList<Ga> ga = new Ga_DAO().getAllGa();
        ArrayList<String> tenGa = new ArrayList<>();
        for (Ga g : ga) {
            tenGa.add(g.getTenGa());
        }
        cbGaDi.setItems(FXCollections.observableList(tenGa));
        cbGaDen.setItems(FXCollections.observableList(tenGa));
        new AutoCompleteComboBoxListener<>(cbGaDi);
        new AutoCompleteComboBoxListener<>(cbGaDen);
        cbGaDen.setOnAction(e -> {
            if (cbGaDi.getValue() != null && cbGaDen.getValue() != null) {
                if(cbGaDi.getValue().equals(cbGaDen.getValue())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Ga đến và ga đi không được trùng nhau");
                    alert.show();
                    cbGaDen.setValue(null);
                    cbGaDen.requestFocus();
                }
            }
        });

        cbGaDi.setOnAction(e -> {
            if (cbGaDi.getValue() != null && cbGaDen.getValue() != null) {
                if(cbGaDi.getValue().equals(cbGaDen.getValue())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Ga đến và ga đi không được trùng nhau");
                    alert.show();
                    cbGaDi.setValue(null);
                    cbGaDi.requestFocus();
                }
            }
        });

        dpNgayKH.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });

        dpNgayVe.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = dpNgayKH.getValue();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });

        btnLamMoi.setOnMouseClicked(e -> {
            cbGaDi.setValue(null);
            cbGaDi.requestFocus();
            cbGaDen.setValue(null);
            dpNgayKH.setValue(null);
            dpNgayVe.setDisable(true);
            dpNgayVe.setValue(null);
            chkKhuHoi.setSelected(false);
            khuHoi = false;
            lblSLnl.setText("0");
            lblSLnct.setText("0");
            lblSLhssv.setText("0");
            lblSLte.setText("0");
            btnChonCD.setDisable(true);
            btnChonKH.setDisable(true);
            btnTangSLte.setDisable(true);
            btnGiamSLte.setDisable(true);
            btnGiamSLnct.setDisable(true);
            btnGiamSLnl.setDisable(true);
            btnGiamSLhssv.setDisable(true);
            btnTangSLte.setFill(Color.GRAY);
            btnGiamSLte.setFill(Color.GRAY);
            btnGiamSLnct.setFill(Color.GRAY);
            btnGiamSLnl.setFill(Color.GRAY);
            btnGiamSLhssv.setFill(Color.GRAY);
            tbTCTLT.setItems(null);
            tbTCTLTKH.setItems(null);
            tbTCTLTKH.setVisible(false);
            acpToaTau.setVisible(false);
            tabToaTau.getTabs().clear();
            acpKH.setVisible(false);
            scr11.setVisible(false);
            tabTTVe.getTabs().clear();
        });

        colSoHieuTau.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("chuyenTau"));
        colGaDi.setCellValueFactory((new PropertyValueFactory<LichTrinh, String>("gaDi")));
        colGaDen.setCellValueFactory((new PropertyValueFactory<LichTrinh, String>("gaDen")));
        colGioKH.setCellValueFactory((new PropertyValueFactory<LichTrinh, String>("thoiGianKhoiHanh")));
        colTGDen.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("thoiGianDuKienDen"));
        colSLT.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("lichTrinh"));

        colSoHieuTauKH.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("chuyenTau"));
        colGaDiKH.setCellValueFactory((new PropertyValueFactory<LichTrinh, String>("gaDi")));
        colGaDenKH.setCellValueFactory((new PropertyValueFactory<LichTrinh, String>("gaDen")));
        colGioKHkh.setCellValueFactory((new PropertyValueFactory<LichTrinh, String>("thoiGianKhoiHanh")));
        colTGDenKH.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("thoiGianDuKienDen"));
        colSLTKH.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("lichTrinh"));

        btnTraCuuCT.setOnMouseClicked(e -> {
            tbTCTLT.setItems(null);
            tbTCTLTKH.setItems(null);
            tbTCTLT.setVisible(true);
            tbTCTLTKH.setVisible(false);
            idBtnChosen = "";
            idBtnChonKH = "";
            acpKH.setVisible(false);
            if (cbGaDi.getValue() == null || cbGaDi.getEditor().getText() == "") {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ga đi");
                alert.show();
                cbGaDi.requestFocus();
            } else if(cbGaDen.getValue() == null || cbGaDen.getEditor().getText() == "") {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ga đến");
                alert.show();
                cbGaDen.requestFocus();
            } else if(dpNgayKH.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ngày khởi hành");
                alert.showAndWait();
                dpNgayKH.requestFocus();
                dpNgayKH.show();
            } else if(chkKhuHoi.isSelected() && dpNgayVe.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ngày về");
                alert.showAndWait();
                dpNgayVe.requestFocus();
                dpNgayVe.show();
            } else {
                if(Integer.parseInt(lblSLnl.getText()) > 0 || Integer.parseInt(lblSLnct.getText()) > 0 || Integer.parseInt(lblSLhssv.getText()) > 0 || Integer.parseInt(lblSLte.getText()) > 0) {
                    String maGaDi = ga_dao.getGaTheoTenGa(cbGaDi.getValue()).getMaGa();
                    String maGaDen = ga_dao.getGaTheoTenGa(cbGaDen.getValue()).getMaGa();
                    ArrayList<LichTrinh> list = lt_dao.traCuuDSLichTrinh(maGaDi, maGaDen, dpNgayKH.getValue());
                    dslt = FXCollections.observableList(list);
                    if (dslt.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setHeaderText(null);
                        alert.setContentText("Không tìm thấy chuyến tàu phù hợp");
                        alert.show();
                        cbGaDi.requestFocus();
                    } else {
                        tbTCTLT.setItems(dslt);
                        tbTCTLT.requestFocus();
                        tabToaTau.getTabs().clear();
                        tabToaTauKH.getTabs().clear();
                        tabTTVe.getTabs().clear();
                        acpToaTau.setVisible(false);
                        if (khuHoi) {
                            btnChonKH.setDisable(false);
                            btnChonCD.setDisable(true);
                        } else {
                            btnChonKH.setDisable(true);
                            btnChonCD.setDisable(true);
                        }

                        colSTT.setCellValueFactory(column -> new SimpleIntegerProperty(tbTCTLT.getItems().indexOf(column.getValue()) + 1).asObject());

                        colSoHieuTau.setCellValueFactory(param ->
                                new SimpleStringProperty(param.getValue().getChuyenTau().getSoHieutau()));

                        colGaDi.setCellValueFactory(param ->
                                new SimpleStringProperty(ga_dao.getGaTheoMaGa(param.getValue().getGaDi().getMaGa()).getTenGa()));

                        colGaDen.setCellValueFactory(param ->
                                new SimpleStringProperty(ga_dao.getGaTheoMaGa(param.getValue().getGaDen().getMaGa()).getTenGa()));

                        colGioKH.setCellValueFactory(param -> {
                            return new SimpleStringProperty(DateTimeFormatter.ofPattern("HH:mm").format(param.getValue().getThoiGianKhoiHanh()));
                        });
                        colTGDen.setCellValueFactory(param -> {
                            return new SimpleStringProperty(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(param.getValue().getThoiGianDuKienDen()));
                        });

                        colSLT.setCellValueFactory(param -> {
                            return new SimpleStringProperty(lt_dao.getSoLuongChoConTrong(param.getValue().getMaLichTrinh()) + "");
                        });
                        Callback<TableColumn<LichTrinh, String>, TableCell<LichTrinh, String>> cellFoctory = (TableColumn<LichTrinh, String> param) -> {
                            // make cell containing buttons
                            final TableCell<LichTrinh, String> cell = new TableCell<LichTrinh, String>() {
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
                                        btnChon.setId("btnChon");
                                        manageBtn.setStyle("-fx-alignment:center;");
                                        setGraphic(manageBtn);
                                        setText(null);
                                        btnChon.setOnAction(e -> {
                                                //lấy thông tin lịch trình chọn xem

                                            LichTrinh lt = tbTCTLT.getItems().get(getIndex());
                                            getData.lt = lt;
                                            ArrayList<Toa> dstoa = toa_dao.getAllToaTheoChuyenTau(lt.getChuyenTau().getSoHieutau());
                                            ArrayList<ChiTietLichTrinh> dsctlt = ctlt_dao.getCtltTheoMaLichTrinh(lt.getMaLichTrinh());
                                            if (dsctlt.isEmpty()) {
                                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                                alert.setTitle("Thông báo");
                                                alert.setHeaderText(null);
                                                alert.setContentText("Lịch trình rỗng");
                                                alert.show();
                                                return;
                                            }
                                            acpToaTau.setVisible(true);
                                            tabToaTau.getTabs().clear();
                                            for (Toa toa : dstoa) {
                                                Tab tab = new Tab("Toa " + toa.getSoSTToa() + " - " + ltoa_dao.getLoaiToaTheoMa(toa.getLoaiToa().getMaLoaiToa()).getTenLoaiToa());
                                                tab.setClosable(false);
                                                ArrayList<ChoNgoi> dscn = cn_dao.getDsChoNgoiTheoToa(toa.getMaToa());
                                                if (toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("NC")){

                                                    GridPane gridPane1 = new GridPane();
                                                    tab.setContent(gridPane1);
                                                    tabToaTau.getTabs().add(tab);

                                                    gridPane1.setAlignment(Pos.CENTER);
                                                    gridPane1.setPadding(new Insets(10));
                                                    gridPane1.setVgap(5);
                                                    gridPane1.setHgap(5);
                                                    int seatNumbers1[][] = {
                                                            {1, 8, 9, 16, 17, 24, 25, 32, 33, 40, 41, 48, 49, 56, 57, 64},
                                                            {2, 7, 10, 15, 18, 23, 26, 31, 34, 39, 42, 47, 50, 55, 58, 63},
                                                            {3, 6, 11, 14, 19, 22, 27, 30, 35, 38, 43, 46, 51, 54, 59, 62},
                                                            {4, 5, 12, 13, 20, 21, 28, 29, 36, 37, 44, 45, 52, 53, 60, 61}
                                                    };

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
                                                            for (ChoNgoi cn : dscn) {
                                                                if(cn.getSttCho() == seatNumber) {
                                                                    seatButton.setId(cn.getMaChoNgoi());
                                                                }
                                                            }
                                                            seatButton.setMinSize(30, 30); // Kích thước nút ghế

                                                            // Kiểm tra trạng thái của ghế
                                                            ChoNgoi choNgoi = cn_dao.getChoNgoiTheoToa(toa.getMaToa(), seatNumber);

                                                            if (!ctlt_dao.getTrangThaiCN(lt.getMaLichTrinh(), choNgoi.getMaChoNgoi())) {
                                                                seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                                            } else {
                                                                seatButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                            }
                                                            // Thêm nút vào GridPane
                                                            // Với row = 2 thêm lối đi ở giữa (tăng thêm 1 để tạo khoảng trống)
                                                            gridPane1.add(seatButton, col + (col >= 8 ? 2 : 0), row > 1 ? row + 2 : row);
                                                            seatButton.setOnMouseClicked(event -> {
                                                                if (seatButton.getStyle().contains("green")) {
                                                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                                                    alert.setTitle("Lỗi");
                                                                    alert.setHeaderText(null);
                                                                    alert.setContentText("Ghế đã được chọn");
                                                                    alert.show();
                                                                } else if (seatButton.getStyle().contains("red")) {
                                                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                                                    alert.setTitle("Lỗi");
                                                                    alert.setHeaderText(null);
                                                                    alert.setContentText("Ghế đã được đặt");
                                                                    alert.show();
                                                                } else {
                                                                    btnChonChoNgoi.setDisable(false);
                                                                    btnHuyChon.setDisable(false);
                                                                    if (idBtnChosen != "") {
                                                                        for (Tab t: tabToaTau.getTabs()) {
                                                                            GridPane gp = (GridPane) t.getContent();
                                                                            Button btn = (Button) gp.lookup("#" + idBtnChosen);
                                                                            if (btn != null) {
                                                                                btn.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    idBtnChosen = seatButton.getId();
                                                                    seatButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                                                    lblGiaCN.setText(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), seatButton.getId()).getGiaCho() + "");
                                                                }
                                                            });
                                                        }
                                                    }
                                                    //Tạo chú thích chỗ ngồi chưa chọn, đã chọn, đã đặt
                                                    Label lblChuaChon = new Label("Chưa chọn");
                                                    lblChuaChon.setAlignment(Pos.CENTER);
                                                    lblChuaChon.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                    lblChuaChon.setPadding(new Insets(5));
                                                    lblChuaChon.setPrefSize(70, 30);
                                                    Label lblDangXem = new Label("Đang xem");
                                                    lblDangXem.setAlignment(Pos.CENTER);
                                                    lblDangXem.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                                    lblDangXem.setPadding(new Insets(5));
                                                    lblDangXem.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                                    Label lblDaChon = new Label("Đã chọn");
                                                    lblDaChon.setAlignment(Pos.CENTER);
                                                    lblDaChon.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                                                    lblDaChon.setPadding(new Insets(5));
                                                    lblDaChon.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                                    Label lblDaDat = new Label("Đã đặt");
                                                    lblDaDat.setAlignment(Pos.CENTER);
                                                    lblDaDat.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                                    lblDaDat.setPadding(new Insets(5));
                                                    lblDaDat.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                                    gridPane1.add(lblChuaChon, 0, 7, 3, 1);
                                                    gridPane1.add(lblDangXem, 3, 7, 3, 1);
                                                    gridPane1.add(lblDaChon, 0, 8, 3, 1);
                                                    gridPane1.add(lblDaDat, 3, 8, 3, 1);

                                                } else if (toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("NM")) {
                                                    GridPane gridPane1 = new GridPane();
                                                    tab.setContent(gridPane1);
                                                    tabToaTau.getTabs().add(tab);

                                                    gridPane1.setAlignment(Pos.CENTER);
                                                    gridPane1.setPadding(new Insets(10));
                                                    gridPane1.setVgap(5);
                                                    gridPane1.setHgap(5);
                                                    int seatNumbers1[][] = {
                                                            {1, 8, 9, 16, 17, 24, 25, 32, 33, 40, 41, 48, 49, 56, 57, 64},
                                                            {2, 7, 10, 15, 18, 23, 26, 31, 34, 39, 42, 47, 50, 55, 58, 63},
                                                            {3, 6, 11, 14, 19, 22, 27, 30, 35, 38, 43, 46, 51, 54, 59, 62},
                                                            {4, 5, 12, 13, 20, 21, 28, 29, 36, 37, 44, 45, 52, 53, 60, 61}
                                                    };

                                                    // Ghế đã được đặt (màu đỏ) và ghế còn trống (màu trắng)

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
                                                            for (ChoNgoi cn : dscn) {
                                                                if(cn.getSttCho() == seatNumber) {
                                                                    seatButton.setId(cn.getMaChoNgoi());
                                                                }
                                                            }
                                                            seatButton.setMinSize(30, 30); // Kích thước nút ghế

                                                            // Kiểm tra trạng thái của ghế
                                                            ChoNgoi choNgoi = cn_dao.getChoNgoiTheoToa(toa.getMaToa(), seatNumber);

                                                            if (!ctlt_dao.getTrangThaiCN(lt.getMaLichTrinh(), choNgoi.getMaChoNgoi())) {
                                                                seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                                            } else {
                                                                seatButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                            }

                                                            // Thêm nút vào GridPane
                                                            // Với row = 2 thêm lối đi ở giữa (tăng thêm 1 để tạo khoảng trống)
                                                            gridPane1.add(seatButton, col + (col >= 8 ? 2 : 0), row > 1 ? row + 2 : row);
                                                            seatButton.setOnMouseClicked(event -> {
                                                                if (seatButton.getStyle().contains("green")) {
                                                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                                                    alert.setTitle("Lỗi");
                                                                    alert.setHeaderText(null);
                                                                    alert.setContentText("Ghế đã được chọn");
                                                                    alert.show();
                                                                } else if (seatButton.getStyle().contains("red")) {
                                                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                                                    alert.setTitle("Lỗi");
                                                                    alert.setHeaderText(null);
                                                                    alert.setContentText("Ghế đã được đặt");
                                                                    alert.show();
                                                                } else {
                                                                    btnChonChoNgoi.setDisable(false);
                                                                    btnHuyChon.setDisable(false);
                                                                    if (idBtnChosen != "") {
                                                                        for (Tab t: tabToaTau.getTabs()) {
                                                                            GridPane gp = (GridPane) t.getContent();
                                                                            Button btn = (Button) gp.lookup("#" + idBtnChosen);
                                                                            if (btn != null) {
                                                                                btn.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    idBtnChosen = seatButton.getId();
                                                                    seatButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                                                    lblGiaCN.setText(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(),seatButton.getId()).getGiaCho() + "");
                                                                }
                                                            });
                                                        }
                                                    }
                                                    //Tạo chú thích chỗ ngồi chưa chọn, đã chọn, đã đặt
                                                    Label lblChuaChon = new Label("Chưa chọn");
                                                    lblChuaChon.setAlignment(Pos.CENTER);
                                                    lblChuaChon.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                    lblChuaChon.setPadding(new Insets(5));
                                                    lblChuaChon.setPrefSize(70, 30);
                                                    Label lblDangXem = new Label("Đang xem");
                                                    lblDangXem.setAlignment(Pos.CENTER);
                                                    lblDangXem.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                                    lblDangXem.setPadding(new Insets(5));
                                                    lblDangXem.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                                    Label lblDaChon = new Label("Đã chọn");
                                                    lblDaChon.setAlignment(Pos.CENTER);
                                                    lblDaChon.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                                                    lblDaChon.setPadding(new Insets(5));
                                                    lblDaChon.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                                    Label lblDaDat = new Label("Đã đặt");
                                                    lblDaDat.setAlignment(Pos.CENTER);
                                                    lblDaDat.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                                    lblDaDat.setPadding(new Insets(5));
                                                    lblDaDat.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                                    gridPane1.add(lblChuaChon, 0, 7, 3, 1);
                                                    gridPane1.add(lblDangXem, 3, 7, 3, 1);
                                                    gridPane1.add(lblDaChon, 0, 8, 3, 1);
                                                    gridPane1.add(lblDaDat, 3, 8, 3, 1);
                                                } else if (toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("GNK4")) {
                                                    int[][] seatNumbers2 = {
                                                            {3, 4, 7, 8, 11, 12, 15, 16, 19, 20, 23, 24, 27, 28},
                                                            {1, 2, 5, 6, 9, 10, 13, 14, 17, 18, 21, 22, 25, 26}
                                                    };

                                                    // Ghế đã được đặt (màu đỏ) và ghế còn trống (màu trắng hoặc xanh)

                                                    GridPane gridPane2 = new GridPane();
                                                    tab.setContent(gridPane2);
                                                    tabToaTau.getTabs().add(tab);
                                                    gridPane2.setAlignment(Pos.CENTER);
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
                                                            for (ChoNgoi cn : dscn) {
                                                                if(cn.getSttCho() == seatNumber) {
                                                                    seatButton.setId(cn.getMaChoNgoi());
                                                                }
                                                            }
                                                            seatButton.setMinSize(30, 30);
                                                            ChoNgoi choNgoi = cn_dao.getChoNgoiTheoToa(toa.getMaToa(), seatNumber);

                                                            if (!ctlt_dao.getTrangThaiCN(lt.getMaLichTrinh(), choNgoi.getMaChoNgoi())) {
                                                                seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                                            } else {
                                                                seatButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                            }

                                                            // Thêm nút vào GridPane (với col/2 để tạo khoảng trống giữa các khoang)
                                                            gridPane2.add(seatButton, col + (col / 2) + 1, row + 1);
                                                            seatButton.setOnMouseClicked(event -> {
                                                                if (seatButton.getStyle().contains("green")) {
                                                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                                                    alert.setTitle("Lỗi");
                                                                    alert.setHeaderText(null);
                                                                    alert.setContentText("Ghế đã được chọn");
                                                                    alert.show();
                                                                } else if (seatButton.getStyle().contains("red")) {
                                                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                                                    alert.setTitle("Lỗi");
                                                                    alert.setHeaderText(null);
                                                                    alert.setContentText("Ghế đã được đặt");
                                                                    alert.show();
                                                                } else {
                                                                    btnChonChoNgoi.setDisable(false);
                                                                    btnHuyChon.setDisable(false);
                                                                    if (idBtnChosen != "") {
                                                                        for (Tab t: tabToaTau.getTabs()) {
                                                                            GridPane gp = (GridPane) t.getContent();
                                                                            Button btn = (Button) gp.lookup("#" + idBtnChosen);
                                                                            if (btn != null) {
                                                                                btn.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    idBtnChosen = seatButton.getId();
                                                                    seatButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                                                    lblGiaCN.setText(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(),seatButton.getId()).getGiaCho() + "");
                                                                }
                                                            });
                                                        }
                                                    }
                                                    //Tạo chú thích chỗ ngồi chưa chọn, đã chọn, đã đặt
                                                    Label lblChuaChon = new Label("Chưa chọn");
                                                    lblChuaChon.setAlignment(Pos.CENTER);
                                                    lblChuaChon.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                    lblChuaChon.setPadding(new Insets(5));
                                                    lblChuaChon.setPrefSize(70, 30);
                                                    Label lblDangXem = new Label("Đang xem");
                                                    lblDangXem.setAlignment(Pos.CENTER);
                                                    lblDangXem.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                                    lblDangXem.setPadding(new Insets(5));
                                                    lblDangXem.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                                    Label lblDaChon = new Label("Đã chọn");
                                                    lblDaChon.setAlignment(Pos.CENTER);
                                                    lblDaChon.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                                                    lblDaChon.setPadding(new Insets(5));
                                                    lblDaChon.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                                    Label lblDaDat = new Label("Đã đặt");
                                                    lblDaDat.setAlignment(Pos.CENTER);
                                                    lblDaDat.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                                    lblDaDat.setPadding(new Insets(5));
                                                    lblDaDat.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                                    gridPane2.add(lblChuaChon, 0, 7, 3, 1);
                                                    gridPane2.add(lblDangXem, 3, 7, 3, 1);
                                                    gridPane2.add(lblDaChon, 0, 8, 3, 1);
                                                    gridPane2.add(lblDaDat, 3, 8, 3, 1);
                                                } else if (toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("GNK6")) {
                                                    GridPane gridPane = new GridPane();
                                                    tab.setContent(gridPane);
                                                    tabToaTau.getTabs().add(tab);
                                                    gridPane.setAlignment(Pos.CENTER);
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

                                                    // Tạo giao diện
                                                    for (int row = 0; row < 3; row++) {
                                                        for (int col = 0; col < 14; col++) {
                                                            int seatNumber = seatNumbers[row][col];
                                                            Button seatButton = new Button(String.valueOf(seatNumber));
                                                            for (ChoNgoi cn : dscn) {
                                                                if(cn.getSttCho() == seatNumber) {
                                                                    seatButton.setId(cn.getMaChoNgoi());
                                                                }
                                                            }
                                                            seatButton.setMinSize(30, 30);
                                                            // Kiểm tra trạng thái của ghế
                                                            ChoNgoi choNgoi = cn_dao.getChoNgoiTheoToa(toa.getMaToa(), seatNumber);

                                                            if (!ctlt_dao.getTrangThaiCN(lt.getMaLichTrinh(), choNgoi.getMaChoNgoi())) {
                                                                seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                                            } else {
                                                                seatButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                            }

                                                            // Thêm nút vào GridPane (với col/2 để tạo khoảng trống giữa các khoang)
                                                            gridPane.add(seatButton, col + (col / 2) + 1, row + 1);
                                                            seatButton.setOnMouseClicked(event -> {
                                                                if (seatButton.getStyle().contains("green")) {
                                                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                                                    alert.setTitle("Lỗi");
                                                                    alert.setHeaderText(null);
                                                                    alert.setContentText("Ghế đã được chọn");
                                                                    alert.show();
                                                                } else if (seatButton.getStyle().contains("red")) {
                                                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                                                    alert.setTitle("Lỗi");
                                                                    alert.setHeaderText(null);
                                                                    alert.setContentText("Ghế đã được đặt");
                                                                    alert.show();
                                                                } else {
                                                                    btnChonChoNgoi.setDisable(false);
                                                                    btnHuyChon.setDisable(false);
                                                                    if (idBtnChosen != "") {
                                                                        for (Tab t: tabToaTau.getTabs()) {
                                                                            GridPane gp = (GridPane) t.getContent();
                                                                            Button btn = (Button) gp.lookup("#" + idBtnChosen);
                                                                            if (btn != null) {
                                                                                btn.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    idBtnChosen = seatButton.getId();
                                                                    seatButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                                                    lblGiaCN.setText(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(),seatButton.getId()).getGiaCho() + "");
                                                                }
                                                            });
                                                        }
                                                    }
                                                    //Tạo chú thích chỗ ngồi chưa chọn, đã chọn, đã đặt
                                                    Label lblChuaChon = new Label("Chưa chọn");
                                                    lblChuaChon.setAlignment(Pos.CENTER);
                                                    lblChuaChon.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                    lblChuaChon.setPadding(new Insets(5));
                                                    lblChuaChon.setPrefSize(70, 30);
                                                    Label lblDangXem = new Label("Đang xem");
                                                    lblDangXem.setAlignment(Pos.CENTER);
                                                    lblDangXem.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                                    lblDangXem.setPadding(new Insets(5));
                                                    lblDangXem.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                                    Label lblDaChon = new Label("Đã chọn");
                                                    lblDaChon.setAlignment(Pos.CENTER);
                                                    lblDaChon.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                                                    lblDaChon.setPadding(new Insets(5));
                                                    lblDaChon.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                                    Label lblDaDat = new Label("Đã đặt");
                                                    lblDaDat.setAlignment(Pos.CENTER);
                                                    lblDaDat.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                                    lblDaDat.setPadding(new Insets(5));
                                                    lblDaDat.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                                    gridPane.add(lblChuaChon, 0, 7, 3, 1);
                                                    gridPane.add(lblDangXem, 3, 7, 3, 1);
                                                    gridPane.add(lblDaChon, 0, 8, 3, 1);
                                                    gridPane.add(lblDaDat, 3, 8, 3, 1);
                                                } else if (toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("TVIP")) {
                                                    int[][] seatNumbers2 = {
                                                            {2, 4, 6, 8, 10, 12, 14},
                                                            {1, 3, 5, 7, 9, 11, 13}
                                                    };

                                                    // Ghế đã được đặt (màu đỏ) và ghế còn trống (màu trắng hoặc xanh)
                                                    int[] bookedSeats = null;
                                                    GridPane gridPane2 = new GridPane();
                                                    tab.setContent(gridPane2);
                                                    tabToaTau.getTabs().add(tab);
                                                    gridPane2.setAlignment(Pos.CENTER);
                                                    gridPane2.setVgap(5);
                                                    gridPane2.setHgap(5);
                                                    gridPane2.add(new Label("  Khoang 1"), 1, 0);
                                                    gridPane2.add(new Label("  Khoang 2"), 2, 0);
                                                    gridPane2.add(new Label("  Khoang 3"), 3, 0);
                                                    gridPane2.add(new Label("  Khoang 4"), 4, 0);
                                                    gridPane2.add(new Label("  Khoang 5"), 5, 0);
                                                    gridPane2.add(new Label("  Khoang 6"), 6, 0);
                                                    gridPane2.add(new Label("  Khoang 7"), 7, 0);
                                                    // Số thứ tự ghế như hình (theo hàng từ T1 đến T2 và các khoang 1-4)
                                                    for (int row = 0; row < 2; row++) {
                                                        for (int col = 0; col < 7; col++) {
                                                            int seatNumber = seatNumbers2[row][col];
                                                            Button seatButton = new Button(String.valueOf(seatNumber));
                                                            for (ChoNgoi cn : dscn) {
                                                                if(cn.getSttCho() == seatNumber) {
                                                                    seatButton.setId(cn.getMaChoNgoi());
                                                                }
                                                            }
                                                            seatButton.setMinSize(30, 30); // Kích thước nút ghế
                                                            // Kiểm tra trạng thái của ghế
                                                            ChoNgoi choNgoi = cn_dao.getChoNgoiTheoToa(toa.getMaToa(), seatNumber);

                                                            if (!ctlt_dao.getTrangThaiCN(lt.getMaLichTrinh(), choNgoi.getMaChoNgoi())) {
                                                                seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                                            } else {
                                                                seatButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                            }
                                                            gridPane2.add(seatButton, col + 1, row + 1);
                                                            seatButton.setOnMouseClicked(event -> {
                                                                if (seatButton.getStyle().contains("green")) {
                                                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                                                    alert.setTitle("Lỗi");
                                                                    alert.setHeaderText(null);
                                                                    alert.setContentText("Ghế đã được chọn");
                                                                    alert.show();
                                                                } else if (seatButton.getStyle().contains("red")) {
                                                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                                                    alert.setTitle("Lỗi");
                                                                    alert.setHeaderText(null);
                                                                    alert.setContentText("Ghế đã được đặt");
                                                                    alert.show();
                                                                } else {
                                                                    btnChonChoNgoi.setDisable(false);
                                                                    btnHuyChon.setDisable(false);
                                                                    if (idBtnChosen != "") {
                                                                        for (Tab t: tabToaTau.getTabs()) {
                                                                            GridPane gp = (GridPane) t.getContent();
                                                                            Button btn = (Button) gp.lookup("#" + idBtnChosen);
                                                                            if (btn != null) {
                                                                                btn.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    idBtnChosen = seatButton.getId();
                                                                    seatButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                                                    lblGiaCN.setText(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(),seatButton.getId()).getGiaCho() + "");
                                                                }
                                                            });
                                                        }
                                                    }
                                                    //Tạo chú thích chỗ ngồi chưa chọn, đã chọn, đã đặt
                                                    Label lblChuaChon = new Label("Chưa chọn");
                                                    lblChuaChon.setAlignment(Pos.CENTER);
                                                    lblChuaChon.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                    lblChuaChon.setPadding(new Insets(5));
                                                    lblChuaChon.setPrefSize(70, 30);
                                                    Label lblDangXem = new Label("Đang xem");
                                                    lblDangXem.setAlignment(Pos.CENTER);
                                                    lblDangXem.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                                    lblDangXem.setPadding(new Insets(5));
                                                    lblDangXem.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                                    Label lblDaChon = new Label("Đã chọn");
                                                    lblDaChon.setAlignment(Pos.CENTER);
                                                    lblDaChon.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                                                    lblDaChon.setPadding(new Insets(5));
                                                    lblDaChon.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                                    Label lblDaDat = new Label("Đã đặt");
                                                    lblDaDat.setAlignment(Pos.CENTER);
                                                    lblDaDat.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                                    lblDaDat.setPadding(new Insets(5));
                                                    lblDaDat.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                                    gridPane2.add(lblChuaChon, 0, 7, 3, 1);
                                                    gridPane2.add(lblDangXem, 3, 7, 3, 1);
                                                    gridPane2.add(lblDaChon, 0, 8, 3, 1);
                                                    gridPane2.add(lblDaDat, 3, 8, 3, 1);
                                                }
                                            }

                                            if (tabTTVe.getTabs().isEmpty()) {
                                                tabTTVe.getTabs().clear();
                                                int slnl = Integer.parseInt(lblSLnl.getText());
                                                int slnct = Integer.parseInt(lblSLnct.getText());
                                                int slhssv = Integer.parseInt(lblSLhssv.getText());
                                                int slte = Integer.parseInt(lblSLte.getText());
                                                if (slnl > 0) {
                                                    for (int i = 0; i < slnl; i++) {
                                                        Tab tab = new Tab("Người lớn " + (i + 1));
                                                        try {
                                                            tab.setContent(FXMLLoader.load(TrangChu_GUI.class.getResource("thong-tin-ve.fxml")));
                                                            ComboBox<String> cbLHK = (ComboBox)tab.getContent().lookup("#cbLoaiHK");
                                                            cbLHK.getItems().add("Người lớn");
                                                            cbLHK.getItems().add("Người cao tuổi");
                                                            cbLHK.getItems().add("Học sinh, sinh viên");
                                                            cbLHK.getItems().add("Trẻ em");
                                                            cbLHK.setValue("Người lớn");
                                                            Button btnXacNhanTT = (Button)tab.getContent().lookup("#btnXacNhanTT");
                                                            btnXacNhanTT.setOnMouseClicked(e1 -> {
                                                                for(Tab t : tabTTVe.getTabs()) {
                                                                    TextField txtMaCN = (TextField) t.getContent().lookup("#txtMaCho");
                                                                    TextField txtMaCNKH = (TextField) t.getContent().lookup("#txtMaChoKH");
                                                                    if (!txtMaCN.getText().isEmpty() && txtMaCN.getText() != null) {
                                                                        TextField txtTenHK = (TextField) t.getContent().lookup("#txtTenHK");
                                                                        TextField txtSoCCCD = (TextField) t.getContent().lookup("#txtSoCCCD");
                                                                        DatePicker dpNS = (DatePicker) t.getContent().lookup("#dpNgaySinh");
                                                                        ComboBox<String> comboBox = (ComboBox<String>) t.getContent().lookup("#cbLoaiHK");
                                                                        if (comboBox.getValue().equalsIgnoreCase("Trẻ em")) {
                                                                            if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtTenHK.requestFocus();
                                                                            } else if (dpNS.getValue() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                dpNS.requestFocus();
                                                                            }
                                                                        } else if (comboBox.getValue().equalsIgnoreCase( "Người lớn") || comboBox.getValue().equalsIgnoreCase("Học sinh, sinh viên")) {
                                                                            if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtTenHK.requestFocus();
                                                                            } else if (txtSoCCCD.getText().isEmpty() && txtSoCCCD.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtSoCCCD.requestFocus();
                                                                            }
                                                                        } else if (comboBox.getValue().equalsIgnoreCase( "Người cao tuổi")){
                                                                            if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtTenHK.requestFocus();
                                                                            } else if (txtSoCCCD.getText().isEmpty() && txtSoCCCD.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtSoCCCD.requestFocus();
                                                                            } else if (dpNS.getValue() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                dpNS.requestFocus();
                                                                            }
                                                                        }
                                                                    } else {
                                                                        tabTTVe.getSelectionModel().getSelectedItem();
                                                                        txtMaCN.requestFocus();
                                                                        if (khuHoi && (txtMaCNKH.getText().isEmpty() || txtMaCNKH.getText() == null)) {
                                                                            tabTTVe.getSelectionModel().select(t);
                                                                            txtMaCNKH.requestFocus();
                                                                        }
                                                                    }
                                                                }
                                                                acpKH.setVisible(true);
                                                            });
                                                            if (chkKhuHoi.isSelected()){
                                                                AnchorPane acpKH = (AnchorPane) tab.getContent().lookup("#acpKH");
                                                                acpKH.setVisible(true);
                                                            }
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
                                                        tabTTVe.getTabs().add(tab);
                                                    }
                                                }
                                                if (slnct > 0) {
                                                    for (int i = 0; i < slnct; i++) {
                                                        Tab tab = new Tab("Người cao tuổi " + (i + 1));
                                                        try {
                                                            tab.setContent(FXMLLoader.load(TrangChu_GUI.class.getResource("thong-tin-ve.fxml")));
                                                            ComboBox<String> cbLHK = (ComboBox)tab.getContent().lookup("#cbLoaiHK");
                                                            cbLHK.getItems().add("Người lớn");
                                                            cbLHK.getItems().add("Người cao tuổi");
                                                            cbLHK.getItems().add("Học sinh, sinh viên");
                                                            cbLHK.getItems().add("Trẻ em");
                                                            cbLHK.setValue("Người cao tuổi");

                                                            DatePicker dpNgaySinh = (DatePicker)tab.getContent().lookup("#dpNgaySinh");
                                                            dpNgaySinh.setDisable(false);
                                                            Button btnXacNhanTT = (Button)tab.getContent().lookup("#btnXacNhanTT");
                                                            btnXacNhanTT.setOnMouseClicked(e1 -> {
                                                                for(Tab t : tabTTVe.getTabs()) {
                                                                    TextField txtMaCN = (TextField) t.getContent().lookup("#txtMaCho");
                                                                    TextField txtMaCNKH = (TextField) t.getContent().lookup("#txtMaChoKH");
                                                                    if (!txtMaCN.getText().isEmpty() && txtMaCN.getText() != null) {
                                                                        TextField txtTenHK = (TextField) t.getContent().lookup("#txtTenHK");
                                                                        TextField txtSoCCCD = (TextField) t.getContent().lookup("#txtSoCCCD");
                                                                        DatePicker dpNS = (DatePicker) t.getContent().lookup("#dpNgaySinh");
                                                                        ComboBox<String> comboBox = (ComboBox<String>) t.getContent().lookup("#cbLoaiHK");
                                                                        if (comboBox.getValue().equalsIgnoreCase("Trẻ em")) {
                                                                            if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtTenHK.requestFocus();
                                                                            } else if (dpNS.getValue() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                dpNS.requestFocus();
                                                                            }
                                                                        } else if (comboBox.getValue().equalsIgnoreCase( "Người lớn") || comboBox.getValue().equalsIgnoreCase("Học sinh, sinh viên")) {
                                                                            if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtTenHK.requestFocus();
                                                                            } else if (txtSoCCCD.getText().isEmpty() && txtSoCCCD.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtSoCCCD.requestFocus();
                                                                            }
                                                                        } else if (comboBox.getValue().equalsIgnoreCase( "Người cao tuổi")){
                                                                            if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtTenHK.requestFocus();
                                                                            } else if (txtSoCCCD.getText().isEmpty() && txtSoCCCD.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtSoCCCD.requestFocus();
                                                                            } else if (dpNS.getValue() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                dpNS.requestFocus();
                                                                            }
                                                                        }
                                                                    } else {
                                                                        tabTTVe.getSelectionModel().getSelectedItem();
                                                                        txtMaCN.requestFocus();
                                                                        if (khuHoi && (txtMaCNKH.getText().isEmpty() || txtMaCNKH.getText() == null)) {
                                                                            tabTTVe.getSelectionModel().select(t);
                                                                            txtMaCNKH.requestFocus();
                                                                        }
                                                                    }
                                                                }
                                                                acpKH.setVisible(true);
                                                            });
                                                            if (chkKhuHoi.isSelected()){
                                                                AnchorPane acpKH = (AnchorPane) tab.getContent().lookup("#acpKH");
                                                                acpKH.setVisible(true);
                                                            }
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
                                                        tabTTVe.getTabs().add(tab);
                                                    }
                                                }
                                                if (slhssv > 0) {
                                                    for (int i = 0; i < slhssv; i++) {
                                                        Tab tab = new Tab("HSSV " + (i + 1));
                                                        try {
                                                            tab.setContent(FXMLLoader.load(TrangChu_GUI.class.getResource("thong-tin-ve.fxml")));
                                                            ComboBox<String> cbLHK = (ComboBox)tab.getContent().lookup("#cbLoaiHK");
                                                            cbLHK.getItems().add("Người lớn");
                                                            cbLHK.getItems().add("Người cao tuổi");
                                                            cbLHK.getItems().add("Học sinh, sinh viên");
                                                            cbLHK.getItems().add("Trẻ em");
                                                            cbLHK.setValue("Học sinh, sinh viên");
                                                            Button btnXacNhanTT = (Button)tab.getContent().lookup("#btnXacNhanTT");
                                                            btnXacNhanTT.setOnMouseClicked(e1 -> {
                                                                for(Tab t : tabTTVe.getTabs()) {
                                                                    TextField txtMaCN = (TextField) t.getContent().lookup("#txtMaCho");
                                                                    TextField txtMaCNKH = (TextField) t.getContent().lookup("#txtMaChoKH");
                                                                    if (!txtMaCN.getText().isEmpty() && txtMaCN.getText() != null) {
                                                                        TextField txtTenHK = (TextField) t.getContent().lookup("#txtTenHK");
                                                                        TextField txtSoCCCD = (TextField) t.getContent().lookup("#txtSoCCCD");
                                                                        DatePicker dpNS = (DatePicker) t.getContent().lookup("#dpNgaySinh");
                                                                        ComboBox<String> comboBox = (ComboBox<String>) t.getContent().lookup("#cbLoaiHK");
                                                                        if (comboBox.getValue().equalsIgnoreCase("Trẻ em")) {
                                                                            if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtTenHK.requestFocus();
                                                                            } else if (dpNS.getValue() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                dpNS.requestFocus();
                                                                            }
                                                                        } else if (comboBox.getValue().equalsIgnoreCase( "Người lớn") || comboBox.getValue().equalsIgnoreCase("Học sinh, sinh viên")) {
                                                                            if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtTenHK.requestFocus();
                                                                            } else if (txtSoCCCD.getText().isEmpty() && txtSoCCCD.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtSoCCCD.requestFocus();
                                                                            }
                                                                        } else if (comboBox.getValue().equalsIgnoreCase( "Người cao tuổi")){
                                                                            if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtTenHK.requestFocus();
                                                                            } else if (txtSoCCCD.getText().isEmpty() && txtSoCCCD.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtSoCCCD.requestFocus();
                                                                            } else if (dpNS.getValue() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                dpNS.requestFocus();
                                                                            }
                                                                        }
                                                                    } else {
                                                                        tabTTVe.getSelectionModel().getSelectedItem();
                                                                        txtMaCN.requestFocus();
                                                                        if (khuHoi && (txtMaCNKH.getText().isEmpty() || txtMaCNKH.getText() == null)) {
                                                                            tabTTVe.getSelectionModel().select(t);
                                                                            txtMaCNKH.requestFocus();
                                                                        }
                                                                    }
                                                                }
                                                                acpKH.setVisible(true);
                                                            });                                                            if (chkKhuHoi.isSelected()){
                                                                AnchorPane acpKH = (AnchorPane) tab.getContent().lookup("#acpKH");
                                                                acpKH.setVisible(true);
                                                            }
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
                                                        tabTTVe.getTabs().add(tab);
                                                    }
                                                }
                                                if (slte > 0) {
                                                    for (int i = 0; i < slte; i++) {
                                                        Tab tab = new Tab("Trẻ em " + (i + 1));
                                                        try {
                                                            tab.setContent(FXMLLoader.load(TrangChu_GUI.class.getResource("thong-tin-ve.fxml")));
                                                            ComboBox<String> cbLHK = (ComboBox)tab.getContent().lookup("#cbLoaiHK");
                                                            cbLHK.getItems().add("Người lớn");
                                                            cbLHK.getItems().add("Người cao tuổi");
                                                            cbLHK.getItems().add("Học sinh, sinh viên");
                                                            cbLHK.getItems().add("Trẻ em");
                                                            cbLHK.setValue("Trẻ em");
                                                            DatePicker dpNgaySinh = (DatePicker)tab.getContent().lookup("#dpNgaySinh");
                                                            dpNgaySinh.setDisable(false);
                                                            TextField txtSoCCCD = (TextField)tab.getContent().lookup("#txtSoCCCD");
                                                            txtSoCCCD.setDisable(true);
                                                            Button btnXacNhanTT = (Button)tab.getContent().lookup("#btnXacNhanTT");
                                                            btnXacNhanTT.setOnMouseClicked(e1 -> {
                                                                for(Tab t : tabTTVe.getTabs()) {
                                                                    TextField txtMaCN = (TextField) t.getContent().lookup("#txtMaCho");
                                                                    TextField txtMaCNKH = (TextField) t.getContent().lookup("#txtMaChoKH");
                                                                    if (!txtMaCN.getText().isEmpty() && txtMaCN.getText() != null) {
                                                                        TextField txtTenHK = (TextField) t.getContent().lookup("#txtTenHK");
                                                                        TextField txtsoCCCD = (TextField) t.getContent().lookup("#txtSoCCCD");
                                                                        DatePicker dpNS = (DatePicker) t.getContent().lookup("#dpNgaySinh");
                                                                        ComboBox<String> comboBox = (ComboBox<String>) t.getContent().lookup("#cbLoaiHK");
                                                                        if (comboBox.getValue().equalsIgnoreCase("Trẻ em")) {
                                                                            if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtTenHK.requestFocus();
                                                                            } else if (dpNS.getValue() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                dpNS.requestFocus();
                                                                            }
                                                                        } else if (comboBox.getValue().equalsIgnoreCase( "Người lớn") || comboBox.getValue().equalsIgnoreCase("Học sinh, sinh viên")) {
                                                                            if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtTenHK.requestFocus();
                                                                            } else if (txtsoCCCD.getText().isEmpty() && txtsoCCCD.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtsoCCCD.requestFocus();
                                                                            }
                                                                        } else if (comboBox.getValue().equalsIgnoreCase( "Người cao tuổi")){
                                                                            if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtTenHK.requestFocus();
                                                                            } else if (txtsoCCCD.getText().isEmpty() && txtsoCCCD.getText() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                txtsoCCCD.requestFocus();
                                                                            } else if (dpNS.getValue() == null) {
                                                                                tabTTVe.getSelectionModel().select(t);
                                                                                dpNS.requestFocus();
                                                                            }
                                                                        }
                                                                    } else {
                                                                        tabTTVe.getSelectionModel().getSelectedItem();
                                                                        txtMaCN.requestFocus();
                                                                        if (khuHoi && (txtMaCNKH.getText().isEmpty() || txtMaCNKH.getText() == null)) {
                                                                            tabTTVe.getSelectionModel().select(t);
                                                                            txtMaCNKH.requestFocus();
                                                                        }
                                                                    }
                                                                }
                                                                acpKH.setVisible(true);
                                                            });
                                                            if (chkKhuHoi.isSelected()){
                                                                AnchorPane acpKH = (AnchorPane) tab.getContent().lookup("#acpKH");
                                                                acpKH.setVisible(true);
                                                            }
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
                                                        tabTTVe.getTabs().add(tab);
                                                    }
                                                }
                                            }

                                        });
                                    }
                                }
                            };
                            return cell;
                        };
                        colChon.setCellFactory(cellFoctory);
                    }


                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Vui lòng chọn số lượng vé");
                    alert.show();
                    ttpLoaiVe.setExpanded(true);
                    ttpLoaiVe.setCollapsible(true);
                }
            }
        });

        btnTangSLnl.setOnMouseClicked(e -> {
            stt = Integer.parseInt(lblSLnl.getText());
            stt++;
            lblSLnl.setText(String.valueOf(stt));
            if (stt > 0) {
                btnGiamSLnl.setDisable(false);
                btnGiamSLnl.setFill(Color.WHITE);
                if (lblSLnct.getText().equals("0") && lblSLhssv.getText().equals("0") && lblSLte.getText().equals("0")) {
                    btnTangSLte.setDisable(false);
                    btnTangSLte.setFill(Color.BLACK);
                }
            }
        });

        btnGiamSLnl.setOnMouseClicked(e -> {
            stt = Integer.parseInt(lblSLnl.getText());
            stt--;
            lblSLnl.setText(String.valueOf(stt));
            if (stt > 0) {
                btnGiamSLnl.setDisable(false);
                btnGiamSLnl.setFill(Color.WHITE);

            } else {
                btnGiamSLnl.setDisable(true);
                btnGiamSLnl.setFill(Color.GRAY);
                if (lblSLnct.getText().equals("0") && lblSLhssv.getText().equals("0")) {
                    btnTangSLte.setDisable(true);
                    btnTangSLte.setFill(Color.GRAY);
                    btnGiamSLte.setDisable(true);
                    btnGiamSLte.setFill(Color.GRAY);
                    lblSLte.setText("0");
                }
            }
        });

        btnTangSLnct.setOnMouseClicked(e -> {
            stt = Integer.parseInt(lblSLnct.getText());
            stt++;
            lblSLnct.setText(String.valueOf(stt));
            if (stt > 0) {
                btnGiamSLnct.setDisable(false);
                btnGiamSLnct.setFill(Color.WHITE);
                if (lblSLnl.getText().equals("0") && lblSLhssv.getText().equals("0") && lblSLte.getText().equals("0")) {
                    btnTangSLte.setDisable(false);
                    btnTangSLte.setFill(Color.BLACK);
                }
            }
        });

        btnGiamSLnct.setOnMouseClicked(e -> {
            stt = Integer.parseInt(lblSLnct.getText());
            stt--;
            lblSLnct.setText(String.valueOf(stt));
            if (stt > 0) {
                btnGiamSLnct.setDisable(false);
                btnGiamSLnct.setFill(Color.WHITE);
            } else {
                btnGiamSLnct.setDisable(true);
                btnGiamSLnct.setFill(Color.GRAY);
                if (lblSLnl.getText().equals("0") && lblSLhssv.getText().equals("0")) {
                    btnTangSLte.setDisable(true);
                    btnTangSLte.setFill(Color.GRAY);
                    btnGiamSLte.setDisable(true);
                    btnGiamSLte.setFill(Color.GRAY);
                    lblSLte.setText("0");
                }
            }
        });

        btnTangSLhssv.setOnMouseClicked(e -> {
            stt = Integer.parseInt(lblSLhssv.getText());
            stt++;
            lblSLhssv.setText(String.valueOf(stt));
            if (stt > 0) {
                btnGiamSLhssv.setDisable(false);
                btnGiamSLhssv.setFill(Color.WHITE);
                if (lblSLnl.getText().equals("0") && lblSLnct.getText().equals("0") && lblSLte.getText().equals("0")) {
                    btnTangSLte.setDisable(false);
                    btnTangSLte.setFill(Color.BLACK);
                }
            }
        });

        btnGiamSLhssv.setOnMouseClicked(e -> {
            stt = Integer.parseInt(lblSLhssv.getText());
            stt--;
            lblSLhssv.setText(String.valueOf(stt));
            if (stt > 0) {
                btnGiamSLhssv.setDisable(false);
                btnGiamSLhssv.setFill(Color.WHITE);
            } else {
                btnGiamSLhssv.setDisable(true);
                btnGiamSLhssv.setFill(Color.GRAY);
                if (lblSLnl.getText().equals("0") && lblSLnct.getText().equals("0")) {
                    btnTangSLte.setDisable(true);
                    btnTangSLte.setFill(Color.GRAY);
                    btnGiamSLte.setDisable(true);
                    btnGiamSLte.setFill(Color.GRAY);
                    lblSLte.setText("0");
                }
            }
        });

        btnTangSLte.setOnMouseClicked(e -> {
            stt = Integer.parseInt(lblSLte.getText());
            stt++;
            lblSLte.setText(String.valueOf(stt));
            if (stt > 0) {
                btnGiamSLte.setDisable(false);
                btnGiamSLte.setFill(Color.WHITE);
            }
        });

        btnGiamSLte.setOnMouseClicked(e -> {
            stt = Integer.parseInt(lblSLte.getText());
            stt--;
            lblSLte.setText(String.valueOf(stt));
            if (stt > 0) {
                btnGiamSLte.setDisable(false);
                btnGiamSLte.setFill(Color.WHITE);
            } else {
                btnGiamSLte.setDisable(true);
                btnGiamSLte.setFill(Color.GRAY);
            }
        });

        btnTaoHD.setOnMouseClicked(e -> {
            //Kiểm tra các field trống
            if (txtTenKH.getText().isEmpty() || txtTenKH.getText() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập tên khách hàng");
                alert.show();
                txtTenKH.requestFocus();
                return;
            }
            if (txtSoCCCD.getText().isEmpty() || txtSoCCCD.getText() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập số CCCD");
                alert.show();
                txtSoCCCD.requestFocus();
                return;
            } else {
                if (!txtSoCCCD.getText().matches("^0[0-9]{11}$")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Số CCCD chỉ chứa 12 chữ số");
                    alert.show();
                    txtSoCCCD.requestFocus();
                    return;
                }
            }
            if (txtSDT.getText().isEmpty() || txtSDT.getText() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập số điện thoại");
                alert.show();
                txtSDT.requestFocus();
                return;
            } else {
                if (!txtSDT.getText().matches("^0[0-9]{9}$")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Số điện thoại không hợp lệ");
                    alert.show();
                    txtSDT.requestFocus();
                    return;
                }
            }
            if (txtEmail.getText().isEmpty() || txtEmail.getText() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập email");
                alert.show();
                txtEmail.requestFocus();
                return;
            } else {
                if (!txtEmail.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Email không hợp lệ");
                    alert.show();
                    txtEmail.requestFocus();
                    return;
                }
            }
            if (hk_dao.getKhachHangTheoSDT(txtSDT.getText()) == null && hk_dao.getHanhKhachTheoCCCD(txtSoCCCD.getText()) == null) {
                hk_dao.create(new HanhKhach("temp",txtTenKH.getText(), txtSoCCCD.getText(), txtSDT.getText(), txtEmail.getText()));
            }
            getData.hk = hk_dao.getKhachHangTheoSDT(txtSDT.getText());
            int i = 1;
            ArrayList<Ve> dsve = new ArrayList<>();
            for(Tab t : tabTTVe.getTabs()) {
                TextField txtMaCho =(TextField) t.getContent().lookup("#txtMaCho");

                TextField txtMaChoKH = (TextField) t.getContent().lookup("#txtMaChoKH");

                TextField txtTenHK =(TextField) t.getContent().lookup("#txtTenHK");
                TextField txtSoCCCD =(TextField) t.getContent().lookup("#txtSoCCCD");
                String loaiHK = ((ComboBox<String>) t.getContent().lookup("#cbLoaiHK")).getValue();
                DatePicker dpNgaySinh = (DatePicker) t.getContent().lookup("#dpNgaySinh");
                Ve ve = new Ve("temp" + i++,getData.hk,ctlt_dao.getCTLTTheoCN(getData.lt.getMaLichTrinh(), txtMaCho.getText()), new LoaiVe_DAO().getLoaiVeTheoTen(loaiHK), txtTenHK.getText(), txtSoCCCD.getText(), dpNgaySinh.getValue(), "DaBan", false);
                dsve.add(ve);
                if (khuHoi) {;
                    Ve veKH = new Ve("temp" + i++,getData.hk,ctlt_dao.getCTLTTheoCN(getData.ltkh.getMaLichTrinh(), txtMaChoKH.getText()), new LoaiVe_DAO().getLoaiVeTheoTen(loaiHK), txtTenHK.getText(), txtSoCCCD.getText(), dpNgaySinh.getValue(), "DaBan", true);
                    dsve.add(veKH);
                }
            }
            getData.dsve = dsve;
            LocalDateTime now = LocalDateTime.now();
            now = now.minusNanos(now.getNano());
            HoaDon hd = new HoaDon("temp", getData.nv, getData.hk, now, false);
            if (hd_dao.createTempInvoice(hd)) {
                //get hóa đơn vừa tạo
                getData.hd = hd_dao.getHoaDonTheoNLHD(hd.getNgayLapHoaDon());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Tạo hóa đơn thất bại");
                alert.show();
                return;
            }
            try {
                AnchorPane acpHoaDon = FXMLLoader.load(TrangChu_GUI.class.getResource("hoa-don.fxml"));
                Stage stgHoaDon = new Stage();
                stgHoaDon.setTitle("Thanh toán");
                stgHoaDon.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
                stgHoaDon.setScene(new Scene(acpHoaDon));
                stgHoaDon.sizeToScene();
                stgHoaDon.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
        //Nhập số điện thoại hiển thị ra khách hàng

        txtSDT.setOnKeyTyped(e -> {
            if (!txtSDT.getText().isEmpty() && !txtSDT.getText().matches("^0[0-9]{9}$")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Số điện thoại không hợp lệ");
                alert.show();

                txtSDT.requestFocus();
                return;
            }
            if (hk_dao.getKhachHangTheoSDT(txtSDT.getText()) != null) {
                HanhKhach kh = hk_dao.getKhachHangTheoSDT(txtSDT.getText());
                txtTenKH.setText(kh.getTenHanhKhach());
                txtSoCCCD.setText(kh.getSoCCCD());
                txtEmail.setText(kh.getEmail());
            }
        });

        txtSoCCCD.setOnKeyTyped(e -> {
            if (!txtSoCCCD.getText().isEmpty() && !txtSoCCCD.getText().matches("^0[0-9]{11}$")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Số CCCD chỉ chứa 12 chữ số");
                alert.show();
                txtSoCCCD.requestFocus();
                return;
            }
            if (hk_dao.getHanhKhachTheoCCCD(txtSoCCCD.getText()) != null) {
                HanhKhach kh = hk_dao.getHanhKhachTheoCCCD(txtSoCCCD.getText());
                txtTenKH.setText(kh.getTenHanhKhach());
                txtSDT.setText(kh.getSdt());
                txtEmail.setText(kh.getEmail());
            }
        });

        txtEmail.setOnKeyTyped(e -> {
            if (!txtEmail.getText().isEmpty() && !txtEmail.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Email không hợp lệ");
                alert.show();
                txtEmail.requestFocus();
                return;
            }
        });


        btnLamMoiCN.setOnMouseClicked(e -> {
            txtTenKH.setText("");
            txtSoCCCD.setText("");
            txtSDT.setText("");
            txtEmail.setText("");
        });

        btnHuyChon.setOnMouseClicked(e -> {
            if (!scr11.isVisible()) {
                if (idBtnChosen != "") {
                    Button btn = (Button) tabToaTau.getTabs().get(tabToaTau.getSelectionModel().getSelectedIndex()).getContent().lookup("#" + idBtnChosen);
                    btn.setStyle(btn.getStyle() + "-fx-background-color: white; -fx-text-fill: black;");
                    lblGiaCN.setText(null);
                }
                idBtnChosen = "";
                btnChonChoNgoi.setDisable(true);
                btnHuyChon.setDisable(true);
            } else if (scr11.isVisible()) {
                if (idBtnChonKH != "") {
                    Button btn = (Button) tabToaTauKH.getTabs().get(tabToaTauKH.getSelectionModel().getSelectedIndex()).getContent().lookup("#" + idBtnChonKH);
                    btn.setStyle(btn.getStyle() + "-fx-background-color: white; -fx-text-fill: black;");
                    lblGiaCN.setText(null);
                }
                idBtnChonKH = "";
                btnChonChoNgoi.setDisable(true);
                btnHuyChon.setDisable(true);
            }
        });

        btnChonChoNgoi.setOnMouseClicked(e -> {
            if (idBtnChosen != "" || idBtnChonKH != "") {
                TextField txtMaCho =(TextField) tabTTVe.getTabs().  get(tabTTVe.getSelectionModel().getSelectedIndex()).getContent().lookup("#txtMaCho");
                TextField txtLoaiCho =(TextField) tabTTVe.getTabs().get(tabTTVe.getSelectionModel().getSelectedIndex()).getContent().lookup("#txtLoaiCho");
                TextField txtSTTToa =(TextField) tabTTVe.getTabs().get(tabTTVe.getSelectionModel().getSelectedIndex()).getContent().lookup("#txtSTTToa");
                TextField txtGia =(TextField) tabTTVe.getTabs().get(tabTTVe.getSelectionModel().getSelectedIndex()).getContent().lookup("#txtGia");

                TextField txtMaCNKH = (TextField) tabTTVe.getTabs().get(tabTTVe.getSelectionModel().getSelectedIndex()).getContent().lookup("#txtMaChoKH");
                TextField txtLoaiChoKH =(TextField) tabTTVe.getTabs().get(tabTTVe.getSelectionModel().getSelectedIndex()).getContent().lookup("#txtLoaiChoKH");
                TextField txtSTTToaKH =(TextField) tabTTVe.getTabs().get(tabTTVe.getSelectionModel().getSelectedIndex()).getContent().lookup("#txtSTTToaKH");
                TextField txtGiaKH =(TextField) tabTTVe.getTabs().get(tabTTVe.getSelectionModel().getSelectedIndex()).getContent().lookup("#txtGiaKH");

                if (scr11.isVisible()) {
                    Button btnKH = (Button) tabToaTauKH.getTabs().get(tabToaTauKH.getSelectionModel().getSelectedIndex()).getContent().lookup("#" + idBtnChonKH);
                    if (txtMaCNKH.getText().isEmpty() || txtMaCNKH.getText() == null) {
                        txtMaCNKH.setText(idBtnChonKH);
                        txtLoaiChoKH.setText(tabToaTauKH.getSelectionModel().getSelectedItem().getText().split(" - ")[1]);
                        txtSTTToaKH.setText(tabToaTauKH.getSelectionModel().getSelectedIndex() + 1 + "");
                        txtGiaKH.setText(lblGiaCN.getText());
                        btnKH.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                        lblGiaCN.setText(null);
                        tabTTVe.getSelectionModel().selectNext();
                    } else {
                        Button btnOld = (Button) tabToaTauKH.getTabs().get(Integer.parseInt(txtSTTToaKH.getText()) - 1).getContent().lookup("#" + txtMaCNKH.getText());
                        btnOld.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                        txtMaCNKH.setText(idBtnChonKH);
                        txtLoaiChoKH.setText(tabToaTauKH.getSelectionModel().getSelectedItem().getText().split(" - ")[1]);
                        txtSTTToaKH.setText(tabToaTauKH.getSelectionModel().getSelectedIndex() + 1 + "");
                        txtGiaKH.setText(lblGiaCN.getText());
                        btnKH.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                        lblGiaCN.setText(null);
                        tabTTVe.getSelectionModel().selectNext();
                    }
                    idBtnChonKH = "";
                    btnChonChoNgoi.setDisable(true);
                    btnHuyChon.setDisable(true);
                } else if (scr1.isVisible()){
                    if (!txtMaCho.getText().isEmpty() && txtMaCho.getText() != null) {
                        Button btn = (Button) tabToaTau.getTabs().get(tabToaTau.getSelectionModel().getSelectedIndex()).getContent().lookup("#" + idBtnChosen);
                        Button btnOld = (Button) tabToaTau.getTabs().get(Integer.parseInt(txtSTTToa.getText()) - 1).getContent().lookup("#" + txtMaCho.getText());
                        btnOld.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                        txtMaCho.setText(idBtnChosen);
                        txtLoaiCho.setText(tabToaTau.getSelectionModel().getSelectedItem().getText().split(" - ")[1]);
                        txtSTTToa.setText(tabToaTau.getSelectionModel().getSelectedIndex() + 1 + "");
                        txtGia.setText(lblGiaCN.getText());
                        btn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                        lblGiaCN.setText(null);
                        tabTTVe.getSelectionModel().selectNext();
                    } else {
                        Button btn = (Button) tabToaTau.getTabs().get(tabToaTau.getSelectionModel().getSelectedIndex()).getContent().lookup("#" + idBtnChosen);
                        btn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                        txtMaCho.setText(idBtnChosen);
                        txtLoaiCho.setText(tabToaTau.getSelectionModel().getSelectedItem().getText().split(" - ")[1]);
                        txtSTTToa.setText(tabToaTau.getSelectionModel().getSelectedIndex() + 1 + "");
                        txtGia.setText(lblGiaCN.getText());
                        lblGiaCN.setText(null);
                        tabTTVe.getSelectionModel().selectNext();
                    }
                    idBtnChosen = "";
                    btnChonChoNgoi.setDisable(true);
                    btnHuyChon.setDisable(true);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ghế");
                alert.show();
            }
            if (tabTTVe.getTabs().size() > 0 && tabTTVe.getSelectionModel().getSelectedIndex() == tabTTVe.getTabs().size() - 1){

                Button btnXacNhanTT = (Button) tabTTVe.getSelectionModel().getSelectedItem().getContent().lookup("#btnXacNhanTT");
                TextField txtMaCN = (TextField) tabTTVe.getSelectionModel().getSelectedItem().getContent().lookup("#txtMaCho");
                if (!txtMaCN.getText().isEmpty() && txtMaCN.getText() != null) {
                    if (khuHoi) {
                        TextField txtMaCNKH = (TextField) tabTTVe.getSelectionModel().getSelectedItem().getContent().lookup("#txtMaChoKH");
                        Button btnChuyenChonKH = (Button) tabTTVe.getSelectionModel().getSelectedItem().getContent().lookup("#btnChuyenChonKH");

                        if (!txtMaCNKH.getText().isEmpty() && txtMaCNKH.getText() != null) {
                            btnChuyenChonKH.setVisible(false);
                            btnXacNhanTT.setVisible(true);
                            btnXacNhanTT.setDisable(false);
                            btnXacNhanTT.setOnMouseClicked(e1 -> {
                                for (Tab t : tabTTVe.getTabs()) {
                                    TextField txtMaCho = (TextField) t.getContent().lookup("#txtMaCho");
                                    TextField txtMaChoKH = (TextField) t.getContent().lookup("#txtMaChoKH");
                                    if (!txtMaCho.getText().isEmpty() && txtMaCho.getText() != null) {
                                        TextField txtTenHK = (TextField) t.getContent().lookup("#txtTenHK");
                                        TextField txtSoCCCD = (TextField) t.getContent().lookup("#txtSoCCCD");
                                        DatePicker dpNS = (DatePicker) t.getContent().lookup("#dpNgaySinh");
                                        ComboBox<String> comboBox = (ComboBox<String>) t.getContent().lookup("#cbLoaiHK");
                                        if (comboBox.getValue().equalsIgnoreCase("Trẻ em")) {
                                            if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                tabTTVe.getSelectionModel().select(t);
                                                txtTenHK.requestFocus();
                                                return;
                                            } else if (dpNS.getValue() == null) {
                                                tabTTVe.getSelectionModel().select(t);
                                                dpNS.requestFocus();
                                                return;
                                            }
                                        } else if (comboBox.getValue().equalsIgnoreCase("Người lớn") || comboBox.getValue().equalsIgnoreCase("Học sinh, sinh viên")) {
                                            if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                tabTTVe.getSelectionModel().select(t);
                                                txtTenHK.requestFocus();
                                                return;
                                            } else if (txtSoCCCD.getText().isEmpty() || txtSoCCCD.getText() == null) {
                                                tabTTVe.getSelectionModel().select(t);
                                                txtSoCCCD.requestFocus();
                                                return;
                                            }
                                        } else if (comboBox.getValue().equalsIgnoreCase("Người cao tuổi")) {
                                            if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                tabTTVe.getSelectionModel().select(t);
                                                txtTenHK.requestFocus();
                                                return;
                                            } else if (txtSoCCCD.getText().isEmpty() || txtSoCCCD.getText() == null) {
                                                tabTTVe.getSelectionModel().select(t);
                                                txtSoCCCD.requestFocus();
                                                return;
                                            } else if (dpNS.getValue() == null) {
                                                tabTTVe.getSelectionModel().select(t);
                                                dpNS.requestFocus();
                                                return;
                                            }
                                        }
                                    } else {
                                        tabTTVe.getSelectionModel().select(t);
                                        txtMaCN.requestFocus();
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Lỗi");
                                        alert.setHeaderText(null);
                                        alert.setContentText("Vui lòng chọn ghế");
                                        alert.showAndWait();
                                        if (khuHoi && txtMaChoKH.getText().isEmpty()) {
                                            tabTTVe.getSelectionModel().select(t);
                                            txtMaCNKH.requestFocus();
                                            return;
                                        }
                                    }
                                }
                                tabTTVe.getSelectionModel().selectFirst();
                                acpKH.setVisible(true);
                            });
                            tabTTVe.getSelectionModel().selectFirst();

                        } else {
                            btnChuyenChonKH.setVisible(false);
                        }
                    } else {
                        btnXacNhanTT.setVisible(true);
                        btnXacNhanTT.setDisable(false);
                        btnXacNhanTT.setOnMouseClicked(e1 -> {
                            for (Tab t : tabTTVe.getTabs()) {
                                TextField txtMaCho = (TextField) t.getContent().lookup("#txtMaCho");
                                TextField txtMaCNKH = (TextField) t.getContent().lookup("#txtMaChoKH");
                                if (!txtMaCho.getText().isEmpty() && txtMaCho.getText() != null) {
                                    TextField txtTenHK = (TextField) t.getContent().lookup("#txtTenHK");
                                    TextField txtSoCCCD = (TextField) t.getContent().lookup("#txtSoCCCD");
                                    DatePicker dpNS = (DatePicker) t.getContent().lookup("#dpNgaySinh");
                                    ComboBox<String> comboBox = (ComboBox<String>) t.getContent().lookup("#cbLoaiHK");
                                    if (comboBox.getValue().equalsIgnoreCase("Trẻ em")) {
                                        if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                            tabTTVe.getSelectionModel().select(t);
                                            txtTenHK.requestFocus();
                                            return;
                                        } else if (dpNS.getValue() == null) {
                                            tabTTVe.getSelectionModel().select(t);
                                            dpNS.requestFocus();
                                            return;
                                        }
                                    } else if (comboBox.getValue().equalsIgnoreCase("Người lớn") || comboBox.getValue().equalsIgnoreCase("Học sinh, sinh viên")) {
                                        if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                            tabTTVe.getSelectionModel().select(t);
                                            txtTenHK.requestFocus();
                                            return;
                                        } else if (txtSoCCCD.getText().isEmpty() || txtSoCCCD.getText() == null) {
                                            tabTTVe.getSelectionModel().select(t);
                                            txtSoCCCD.requestFocus();
                                            return;
                                        }
                                    } else if (comboBox.getValue().equalsIgnoreCase("Người cao tuổi")) {
                                        if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                            tabTTVe.getSelectionModel().select(t);
                                            txtTenHK.requestFocus();
                                            return;
                                        } else if (txtSoCCCD.getText().isEmpty() || txtSoCCCD.getText() == null) {
                                            tabTTVe.getSelectionModel().select(t);
                                            txtSoCCCD.requestFocus();
                                            return;
                                        } else if (dpNS.getValue() == null) {
                                            tabTTVe.getSelectionModel().select(t);
                                            dpNS.requestFocus();
                                            return;
                                        }
                                    }
                                } else {
                                    tabTTVe.getSelectionModel().select(t);
                                    txtMaCN.requestFocus();
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Vui lòng chọn ghế");
                                    alert.showAndWait();
                                    if (khuHoi && txtMaCNKH.getText().isEmpty()) {
                                        tabTTVe.getSelectionModel().select(t);
                                        txtMaCNKH.requestFocus();
                                        return;
                                    }
                                }
                            }
                            tabTTVe.getSelectionModel().selectFirst();
                            acpKH.setVisible(true);
                        });
                    }

                }
            }
        });

        btnChonCD.setOnMouseClicked(e -> {
            btnChonCD.setDisable(true);
            btnChonKH.setDisable(false);
            scr11.setVisible(false);
            scr1.setVisible(true);
            tabTTVe.getSelectionModel().selectFirst();
            tbTCTLTKH.setVisible(false);
            tbTCTLT.setVisible(true);
        });

        btnChonKH.setOnMouseClicked(e -> {
            btnChonKH.setDisable(true);
            btnChonCD.setDisable(false);
            scr11.setVisible(true);
            scr1.setVisible(false);
            tbTCTLT.setVisible(false);
            tbTCTLTKH.setVisible(true);
            tabTTVe.getSelectionModel().selectFirst();
            String maGaDi = ga_dao.getGaTheoTenGa(cbGaDi.getValue()).getMaGa();
            String maGaDen = ga_dao.getGaTheoTenGa(cbGaDen.getValue()).getMaGa();
            ArrayList<LichTrinh> list = lt_dao.traCuuDSLichTrinh(maGaDen, maGaDi, dpNgayVe.getValue());
            dsltkh = FXCollections.observableList(list);
            if (dsltkh.size() > 0) {
                tbTCTLTKH.setItems(dsltkh);
                colSTTKH.setCellValueFactory(column -> new SimpleIntegerProperty(tbTCTLTKH.getItems().indexOf(column.getValue()) + 1).asObject());

                colSoHieuTauKH.setCellValueFactory(param ->
                        new SimpleStringProperty(param.getValue().getChuyenTau().getSoHieutau()));

                colGaDiKH.setCellValueFactory(param ->
                        new SimpleStringProperty(ga_dao.getGaTheoMaGa(param.getValue().getGaDi().getMaGa()).getTenGa()));

                colGaDenKH.setCellValueFactory(param ->
                        new SimpleStringProperty(ga_dao.getGaTheoMaGa(param.getValue().getGaDen().getMaGa()).getTenGa()));

                colGioKHkh.setCellValueFactory(param -> {
                    return new SimpleStringProperty(DateTimeFormatter.ofPattern("HH:mm").format(param.getValue().getThoiGianKhoiHanh()));
                });
                colTGDenKH.setCellValueFactory(param -> {
                    return new SimpleStringProperty(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(param.getValue().getThoiGianDuKienDen()));
                });

                colSLTKH.setCellValueFactory(param -> {
                    return new SimpleStringProperty(lt_dao.getSoLuongChoConTrong(param.getValue().getMaLichTrinh()) + "");
                });
                Callback<TableColumn<LichTrinh, String>, TableCell<LichTrinh, String>> cellFactory = (TableColumn<LichTrinh, String> param) -> {
                    // make cell containing buttons
                    final TableCell<LichTrinh, String> cell = new TableCell<LichTrinh, String>() {
                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                                setText(null);

                            } else {
                                Button btnChonKH = new Button("Xem chi tiết");
                                btnChonKH.setStyle("-fx-cursor: hand;-fx-background-color: #5098ff;-fx-text-fill: white;-fx-border-radius: 5px;");
                                HBox manageBtn = new HBox(btnChonKH);
                                btnChonKH.setId("btnChon");
                                manageBtn.setStyle("-fx-alignment:center;");
                                setGraphic(manageBtn);
                                setText(null);
                                btnChonKH.setOnAction(e -> {
                                    //lấy thông tin lịch trình chọn xem

                                    LichTrinh lt = tbTCTLTKH.getItems().get(getIndex());
                                    getData.ltkh = lt;
                                    ArrayList<ChiTietLichTrinh> dsctlt = ctlt_dao.getCtltTheoMaLichTrinh(lt.getMaLichTrinh());
                                    if (dsctlt.isEmpty()) {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Thông báo");
                                        alert.setHeaderText(null);
                                        alert.setContentText("Lịch trình rỗng");
                                        alert.show();
                                        return;
                                    }
                                    ArrayList<Toa> dstoa = toa_dao.getAllToaTheoChuyenTau(lt.getChuyenTau().getSoHieutau());
                                    if (!acpToaTau.isVisible()) {
                                        acpToaTau.setVisible(true);
                                    }
                                    tabToaTauKH.getTabs().clear();
                                    for (Toa toa : dstoa) {
                                        Tab tab = new Tab("Toa " + toa.getSoSTToa() + " - " + ltoa_dao.getLoaiToaTheoMa(toa.getLoaiToa().getMaLoaiToa()).getTenLoaiToa());
                                        tab.setClosable(false);
                                        ArrayList<ChoNgoi> dscn = cn_dao.getDsChoNgoiTheoToa(toa.getMaToa());
                                        if (toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("NC")){

                                            GridPane gridPane1 = new GridPane();
                                            tab.setContent(gridPane1);
                                            tabToaTauKH.getTabs().add(tab);

                                            gridPane1.setAlignment(Pos.CENTER);
                                            gridPane1.setPadding(new Insets(10));
                                            gridPane1.setVgap(5);
                                            gridPane1.setHgap(5);
                                            int seatNumbers1[][] = {
                                                    {1, 8, 9, 16, 17, 24, 25, 32, 33, 40, 41, 48, 49, 56, 57, 64},
                                                    {2, 7, 10, 15, 18, 23, 26, 31, 34, 39, 42, 47, 50, 55, 58, 63},
                                                    {3, 6, 11, 14, 19, 22, 27, 30, 35, 38, 43, 46, 51, 54, 59, 62},
                                                    {4, 5, 12, 13, 20, 21, 28, 29, 36, 37, 44, 45, 52, 53, 60, 61}
                                            };

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
                                                    for (ChoNgoi cn : dscn) {
                                                        if(cn.getSttCho() == seatNumber) {
                                                            seatButton.setId(cn.getMaChoNgoi());
                                                        }
                                                    }
                                                    seatButton.setMinSize(30, 30); // Kích thước nút ghế

                                                    // Kiểm tra trạng thái của ghế
                                                    ChoNgoi choNgoi = cn_dao.getChoNgoiTheoToa(toa.getMaToa(), seatNumber);

                                                    if (!ctlt_dao.getTrangThaiCN(lt.getMaLichTrinh(), choNgoi.getMaChoNgoi())) {
                                                        seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                                    } else {
                                                        seatButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                    }
                                                    // Thêm nút vào GridPane
                                                    // Với row = 2 thêm lối đi ở giữa (tăng thêm 1 để tạo khoảng trống)
                                                    gridPane1.add(seatButton, col + (col >= 8 ? 2 : 0), row > 1 ? row + 2 : row);
                                                    seatButton.setOnMouseClicked(event -> {
                                                        if (seatButton.getStyle().contains("green")) {
                                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                                            alert.setTitle("Lỗi");
                                                            alert.setHeaderText(null);
                                                            alert.setContentText("Ghế đã được chọn");
                                                            alert.show();
                                                        } else if (seatButton.getStyle().contains("red")) {
                                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                                            alert.setTitle("Lỗi");
                                                            alert.setHeaderText(null);
                                                            alert.setContentText("Ghế đã được đặt");
                                                            alert.show();
                                                        } else {
                                                            btnChonChoNgoi.setDisable(false);
                                                            btnHuyChon.setDisable(false);
                                                            if (idBtnChonKH != "") {
                                                                for (Tab t: tabToaTauKH.getTabs()) {
                                                                    GridPane gp = (GridPane) t.getContent();
                                                                    Button btn = (Button) gp.lookup("#" + idBtnChonKH);
                                                                    if (btn != null) {
                                                                        btn.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            idBtnChonKH = seatButton.getId();
                                                            seatButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                                            lblGiaCN.setText(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), seatButton.getId()).getGiaCho() + "");
                                                        }
                                                    });
                                                }
                                            }
                                            //Tạo chú thích chỗ ngồi chưa chọn, đã chọn, đã đặt
                                            Label lblChuaChon = new Label("Chưa chọn");
                                            lblChuaChon.setAlignment(Pos.CENTER);
                                            lblChuaChon.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                            lblChuaChon.setPadding(new Insets(5));
                                            lblChuaChon.setPrefSize(70, 30);
                                            Label lblDangXem = new Label("Đang xem");
                                            lblDangXem.setAlignment(Pos.CENTER);
                                            lblDangXem.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                            lblDangXem.setPadding(new Insets(5));
                                            lblDangXem.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                            Label lblDaChon = new Label("Đã chọn");
                                            lblDaChon.setAlignment(Pos.CENTER);
                                            lblDaChon.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                                            lblDaChon.setPadding(new Insets(5));
                                            lblDaChon.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                            Label lblDaDat = new Label("Đã đặt");
                                            lblDaDat.setAlignment(Pos.CENTER);
                                            lblDaDat.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                            lblDaDat.setPadding(new Insets(5));
                                            lblDaDat.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                            gridPane1.add(lblChuaChon, 0, 7, 3, 1);
                                            gridPane1.add(lblDangXem, 3, 7, 3, 1);
                                            gridPane1.add(lblDaChon, 0, 8, 3, 1);
                                            gridPane1.add(lblDaDat, 3, 8, 3, 1);
                                        } else if (toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("NM")) {
                                            GridPane gridPane1 = new GridPane();
                                            tab.setContent(gridPane1);
                                            tabToaTauKH.getTabs().add(tab);

                                            gridPane1.setAlignment(Pos.CENTER);
                                            gridPane1.setPadding(new Insets(10));
                                            gridPane1.setVgap(5);
                                            gridPane1.setHgap(5);
                                            int seatNumbers1[][] = {
                                                    {1, 8, 9, 16, 17, 24, 25, 32, 33, 40, 41, 48, 49, 56, 57, 64},
                                                    {2, 7, 10, 15, 18, 23, 26, 31, 34, 39, 42, 47, 50, 55, 58, 63},
                                                    {3, 6, 11, 14, 19, 22, 27, 30, 35, 38, 43, 46, 51, 54, 59, 62},
                                                    {4, 5, 12, 13, 20, 21, 28, 29, 36, 37, 44, 45, 52, 53, 60, 61}
                                            };

                                            // Ghế đã được đặt (màu đỏ) và ghế còn trống (màu trắng)

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
                                                    for (ChoNgoi cn : dscn) {
                                                        if(cn.getSttCho() == seatNumber) {
                                                            seatButton.setId(cn.getMaChoNgoi());
                                                        }
                                                    }
                                                    seatButton.setMinSize(30, 30); // Kích thước nút ghế

                                                    // Kiểm tra trạng thái của ghế
                                                    ChoNgoi choNgoi = cn_dao.getChoNgoiTheoToa(toa.getMaToa(), seatNumber);

                                                    if (!ctlt_dao.getTrangThaiCN(lt.getMaLichTrinh(), choNgoi.getMaChoNgoi())) {
                                                        seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                                    } else {
                                                        seatButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                    }

                                                    // Thêm nút vào GridPane
                                                    // Với row = 2 thêm lối đi ở giữa (tăng thêm 1 để tạo khoảng trống)
                                                    gridPane1.add(seatButton, col + (col >= 8 ? 2 : 0), row > 1 ? row + 2 : row);
                                                    seatButton.setOnMouseClicked(event -> {
                                                        if (seatButton.getStyle().contains("green")) {
                                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                                            alert.setTitle("Lỗi");
                                                            alert.setHeaderText(null);
                                                            alert.setContentText("Ghế đã được chọn");
                                                            alert.show();
                                                        } else if (seatButton.getStyle().contains("red")) {
                                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                                            alert.setTitle("Lỗi");
                                                            alert.setHeaderText(null);
                                                            alert.setContentText("Ghế đã được đặt");
                                                            alert.show();
                                                        } else {
                                                            btnChonChoNgoi.setDisable(false);
                                                            btnHuyChon.setDisable(false);
                                                            if (idBtnChonKH != "") {
                                                                for (Tab t: tabToaTauKH.getTabs()) {
                                                                    GridPane gp = (GridPane) t.getContent();
                                                                    Button btn = (Button) gp.lookup("#" + idBtnChonKH);
                                                                    if (btn != null) {
                                                                        btn.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            idBtnChonKH = seatButton.getId();
                                                            seatButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                                            lblGiaCN.setText(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(),seatButton.getId()).getGiaCho() + "");
                                                        }
                                                    });
                                                }
                                            }
                                            //Tạo chú thích chỗ ngồi chưa chọn, đã chọn, đã đặt
                                            Label lblChuaChon = new Label("Chưa chọn");
                                            lblChuaChon.setAlignment(Pos.CENTER);
                                            lblChuaChon.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                            lblChuaChon.setPadding(new Insets(5));
                                            lblChuaChon.setPrefSize(70, 30);
                                            Label lblDangXem = new Label("Đang xem");
                                            lblDangXem.setAlignment(Pos.CENTER);
                                            lblDangXem.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                            lblDangXem.setPadding(new Insets(5));
                                            lblDangXem.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                            Label lblDaChon = new Label("Đã chọn");
                                            lblDaChon.setAlignment(Pos.CENTER);
                                            lblDaChon.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                                            lblDaChon.setPadding(new Insets(5));
                                            lblDaChon.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                            Label lblDaDat = new Label("Đã đặt");
                                            lblDaDat.setAlignment(Pos.CENTER);
                                            lblDaDat.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                            lblDaDat.setPadding(new Insets(5));
                                            lblDaDat.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                            gridPane1.add(lblChuaChon, 0, 7, 3, 1);
                                            gridPane1.add(lblDangXem, 3, 7, 3, 1);
                                            gridPane1.add(lblDaChon, 0, 8, 3, 1);
                                            gridPane1.add(lblDaDat, 3, 8, 3, 1);
                                        } else if (toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("GNK4")) {
                                            int[][] seatNumbers2 = {
                                                    {3, 4, 7, 8, 11, 12, 15, 16, 19, 20, 23, 24, 27, 28},
                                                    {1, 2, 5, 6, 9, 10, 13, 14, 17, 18, 21, 22, 25, 26}
                                            };

                                            // Ghế đã được đặt (màu đỏ) và ghế còn trống (màu trắng hoặc xanh)

                                            GridPane gridPane2 = new GridPane();
                                            tab.setContent(gridPane2);
                                            tabToaTauKH.getTabs().add(tab);
                                            gridPane2.setAlignment(Pos.CENTER);
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
                                                    for (ChoNgoi cn : dscn) {
                                                        if(cn.getSttCho() == seatNumber) {
                                                            seatButton.setId(cn.getMaChoNgoi());
                                                        }
                                                    }
                                                    seatButton.setMinSize(30, 30);
                                                    ChoNgoi choNgoi = cn_dao.getChoNgoiTheoToa(toa.getMaToa(), seatNumber);

                                                    if (!ctlt_dao.getTrangThaiCN(lt.getMaLichTrinh(), choNgoi.getMaChoNgoi())) {
                                                        seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                                    } else {
                                                        seatButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                    }

                                                    // Thêm nút vào GridPane (với col/2 để tạo khoảng trống giữa các khoang)
                                                    gridPane2.add(seatButton, col + (col / 2) + 1, row + 1);
                                                    seatButton.setOnMouseClicked(event -> {
                                                        if (seatButton.getStyle().contains("green")) {
                                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                                            alert.setTitle("Lỗi");
                                                            alert.setHeaderText(null);
                                                            alert.setContentText("Ghế đã được chọn");
                                                            alert.show();
                                                        } else if (seatButton.getStyle().contains("red")) {
                                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                                            alert.setTitle("Lỗi");
                                                            alert.setHeaderText(null);
                                                            alert.setContentText("Ghế đã được đặt");
                                                            alert.show();
                                                        } else {
                                                            btnChonChoNgoi.setDisable(false);
                                                            btnHuyChon.setDisable(false);
                                                            if (idBtnChonKH != "") {
                                                                for (Tab t: tabToaTauKH.getTabs()) {
                                                                    GridPane gp = (GridPane) t.getContent();
                                                                    Button btn = (Button) gp.lookup("#" + idBtnChonKH);
                                                                    if (btn != null) {
                                                                        btn.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            idBtnChonKH = seatButton.getId();
                                                            seatButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                                            lblGiaCN.setText(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(),seatButton.getId()).getGiaCho() + "");
                                                        }
                                                    });
                                                }
                                            }
                                            //Tạo chú thích chỗ ngồi chưa chọn, đã chọn, đã đặt
                                            Label lblChuaChon = new Label("Chưa chọn");
                                            lblChuaChon.setAlignment(Pos.CENTER);
                                            lblChuaChon.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                            lblChuaChon.setPadding(new Insets(5));
                                            lblChuaChon.setPrefSize(70, 30);
                                            Label lblDangXem = new Label("Đang xem");
                                            lblDangXem.setAlignment(Pos.CENTER);
                                            lblDangXem.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                            lblDangXem.setPadding(new Insets(5));
                                            lblDangXem.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                            Label lblDaChon = new Label("Đã chọn");
                                            lblDaChon.setAlignment(Pos.CENTER);
                                            lblDaChon.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                                            lblDaChon.setPadding(new Insets(5));
                                            lblDaChon.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                            Label lblDaDat = new Label("Đã đặt");
                                            lblDaDat.setAlignment(Pos.CENTER);
                                            lblDaDat.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                            lblDaDat.setPadding(new Insets(5));
                                            lblDaDat.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                            gridPane2.add(lblChuaChon, 0, 7, 3, 1);
                                            gridPane2.add(lblDangXem, 3, 7, 3, 1);
                                            gridPane2.add(lblDaChon, 0, 8, 3, 1);
                                            gridPane2.add(lblDaDat, 3, 8, 3, 1);
                                        } else if (toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("GNK6")) {
                                            GridPane gridPane = new GridPane();
                                            tab.setContent(gridPane);
                                            tabToaTauKH.getTabs().add(tab);
                                            gridPane.setAlignment(Pos.CENTER);
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

                                            // Tạo giao diện
                                            for (int row = 0; row < 3; row++) {
                                                for (int col = 0; col < 14; col++) {
                                                    int seatNumber = seatNumbers[row][col];
                                                    Button seatButton = new Button(String.valueOf(seatNumber));
                                                    for (ChoNgoi cn : dscn) {
                                                        if(cn.getSttCho() == seatNumber) {
                                                            seatButton.setId(cn.getMaChoNgoi());
                                                        }
                                                    }
                                                    seatButton.setMinSize(30, 30);
                                                    // Kiểm tra trạng thái của ghế
                                                    ChoNgoi choNgoi = cn_dao.getChoNgoiTheoToa(toa.getMaToa(), seatNumber);

                                                    if (!ctlt_dao.getTrangThaiCN(lt.getMaLichTrinh(), choNgoi.getMaChoNgoi())) {
                                                        seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                                    } else {
                                                        seatButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                    }

                                                    // Thêm nút vào GridPane (với col/2 để tạo khoảng trống giữa các khoang)
                                                    gridPane.add(seatButton, col + (col / 2) + 1, row + 1);
                                                    seatButton.setOnMouseClicked(event -> {
                                                        if (seatButton.getStyle().contains("green")) {
                                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                                            alert.setTitle("Lỗi");
                                                            alert.setHeaderText(null);
                                                            alert.setContentText("Ghế đã được chọn");
                                                            alert.show();
                                                        } else if (seatButton.getStyle().contains("red")) {
                                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                                            alert.setTitle("Lỗi");
                                                            alert.setHeaderText(null);
                                                            alert.setContentText("Ghế đã được đặt");
                                                            alert.show();
                                                        } else {
                                                            btnChonChoNgoi.setDisable(false);
                                                            btnHuyChon.setDisable(false);
                                                            if (idBtnChonKH != "") {
                                                                for (Tab t: tabToaTauKH.getTabs()) {
                                                                    GridPane gp = (GridPane) t.getContent();
                                                                    Button btn = (Button) gp.lookup("#" + idBtnChonKH);
                                                                    if (btn != null) {
                                                                        btn.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            idBtnChonKH = seatButton.getId();
                                                            seatButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                                            lblGiaCN.setText(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(),seatButton.getId()).getGiaCho() + "");
                                                        }
                                                    });
                                                }
                                            }
                                            //Tạo chú thích chỗ ngồi chưa chọn, đã chọn, đã đặt
                                            Label lblChuaChon = new Label("Chưa chọn");
                                            lblChuaChon.setAlignment(Pos.CENTER);
                                            lblChuaChon.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                            lblChuaChon.setPadding(new Insets(5));
                                            lblChuaChon.setPrefSize(70, 30);
                                            Label lblDangXem = new Label("Đang xem");
                                            lblDangXem.setAlignment(Pos.CENTER);
                                            lblDangXem.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                            lblDangXem.setPadding(new Insets(5));
                                            lblDangXem.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                            Label lblDaChon = new Label("Đã chọn");
                                            lblDaChon.setAlignment(Pos.CENTER);
                                            lblDaChon.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                                            lblDaChon.setPadding(new Insets(5));
                                            lblDaChon.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                            Label lblDaDat = new Label("Đã đặt");
                                            lblDaDat.setAlignment(Pos.CENTER);
                                            lblDaDat.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                            lblDaDat.setPadding(new Insets(5));
                                            lblDaDat.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                            gridPane.add(lblChuaChon, 0, 7, 3, 1);
                                            gridPane.add(lblDangXem, 3, 7, 3, 1);
                                            gridPane.add(lblDaChon, 0, 8, 3, 1);
                                            gridPane.add(lblDaDat, 3, 8, 3, 1);
                                        } else if (toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("TVIP")) {
                                            int[][] seatNumbers2 = {
                                                    {2, 4, 6, 8, 10, 12, 14},
                                                    {1, 3, 5, 7, 9, 11, 13}
                                            };

                                            // Ghế đã được đặt (màu đỏ) và ghế còn trống (màu trắng hoặc xanh)
                                            int[] bookedSeats = null;
                                            GridPane gridPane2 = new GridPane();
                                            tab.setContent(gridPane2);
                                            tabToaTauKH.getTabs().add(tab);
                                            gridPane2.setAlignment(Pos.CENTER);
                                            gridPane2.setVgap(5);
                                            gridPane2.setHgap(5);
                                            gridPane2.add(new Label("  Khoang 1"), 1, 0);
                                            gridPane2.add(new Label("  Khoang 2"), 2, 0);
                                            gridPane2.add(new Label("  Khoang 3"), 3, 0);
                                            gridPane2.add(new Label("  Khoang 4"), 4, 0);
                                            gridPane2.add(new Label("  Khoang 5"), 5, 0);
                                            gridPane2.add(new Label("  Khoang 6"), 6, 0);
                                            gridPane2.add(new Label("  Khoang 7"), 7, 0);
                                            // Số thứ tự ghế như hình (theo hàng từ T1 đến T2 và các khoang 1-4)
                                            for (int row = 0; row < 2; row++) {
                                                for (int col = 0; col < 7; col++) {
                                                    int seatNumber = seatNumbers2[row][col];
                                                    Button seatButton = new Button(String.valueOf(seatNumber));
                                                    for (ChoNgoi cn : dscn) {
                                                        if(cn.getSttCho() == seatNumber) {
                                                            seatButton.setId(cn.getMaChoNgoi());
                                                        }
                                                    }
                                                    seatButton.setMinSize(30, 30); // Kích thước nút ghế
                                                    // Kiểm tra trạng thái của ghế
                                                    ChoNgoi choNgoi = cn_dao.getChoNgoiTheoToa(toa.getMaToa(), seatNumber);

                                                    if (!ctlt_dao.getTrangThaiCN(lt.getMaLichTrinh(), choNgoi.getMaChoNgoi())) {
                                                        seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                                    } else {
                                                        seatButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                    }
                                                    gridPane2.add(seatButton, col + 1, row + 1);
                                                    seatButton.setOnMouseClicked(event -> {
                                                        if (seatButton.getStyle().contains("green")) {
                                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                                            alert.setTitle("Lỗi");
                                                            alert.setHeaderText(null);
                                                            alert.setContentText("Ghế đã được chọn");
                                                            alert.show();
                                                        } else if (seatButton.getStyle().contains("red")) {
                                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                                            alert.setTitle("Lỗi");
                                                            alert.setHeaderText(null);
                                                            alert.setContentText("Ghế đã được đặt");
                                                            alert.show();
                                                        } else {
                                                            btnChonChoNgoi.setDisable(false);
                                                            btnHuyChon.setDisable(false);
                                                            if (idBtnChonKH != "") {
                                                                for (Tab t: tabToaTauKH.getTabs()) {
                                                                    GridPane gp = (GridPane) t.getContent();
                                                                    Button btn = (Button) gp.lookup("#" + idBtnChonKH);
                                                                    if (btn != null) {
                                                                        btn.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            idBtnChonKH = seatButton.getId();
                                                            seatButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                                            lblGiaCN.setText(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(),seatButton.getId()).getGiaCho() + "");
                                                        }
                                                    });
                                                }
                                            }
                                            //Tạo chú thích chỗ ngồi chưa chọn, đã chọn, đã đặt
                                            Label lblChuaChon = new Label("Chưa chọn");
                                            lblChuaChon.setAlignment(Pos.CENTER);
                                            lblChuaChon.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                                            lblChuaChon.setPadding(new Insets(5));
                                            lblChuaChon.setPrefSize(70, 30);
                                            Label lblDangXem = new Label("Đang xem");
                                            lblDangXem.setAlignment(Pos.CENTER);
                                            lblDangXem.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                                            lblDangXem.setPadding(new Insets(5));
                                            lblDangXem.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                            Label lblDaChon = new Label("Đã chọn");
                                            lblDaChon.setAlignment(Pos.CENTER);
                                            lblDaChon.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                                            lblDaChon.setPadding(new Insets(5));
                                            lblDaChon.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                            Label lblDaDat = new Label("Đã đặt");
                                            lblDaDat.setAlignment(Pos.CENTER);
                                            lblDaDat.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                            lblDaDat.setPadding(new Insets(5));
                                            lblDaDat.setPrefSize(lblChuaChon.getPrefWidth(), lblChuaChon.getPrefHeight());
                                            gridPane2.add(lblChuaChon, 0, 7, 3, 1);
                                            gridPane2.add(lblDangXem, 3, 7, 3, 1);
                                            gridPane2.add(lblDaChon, 0, 8, 3, 1);
                                            gridPane2.add(lblDaDat, 3, 8, 3, 1);
                                        }
                                    }

                                    if (tabTTVe.getTabs().isEmpty()) {
                                        tabTTVe.getTabs().clear();
                                        int slnl = Integer.parseInt(lblSLnl.getText());
                                        int slnct = Integer.parseInt(lblSLnct.getText());
                                        int slhssv = Integer.parseInt(lblSLhssv.getText());
                                        int slte = Integer.parseInt(lblSLte.getText());
                                        if (slnl > 0) {
                                            for (int i = 0; i < slnl; i++) {
                                                Tab tab = new Tab("Người lớn " + (i + 1));
                                                try {
                                                    tab.setContent(FXMLLoader.load(TrangChu_GUI.class.getResource("thong-tin-ve.fxml")));
                                                    ComboBox<String> cbLHK = (ComboBox)tab.getContent().lookup("#cbLoaiHK");
                                                    cbLHK.getItems().add("Người lớn");
                                                    cbLHK.getItems().add("Người cao tuổi");
                                                    cbLHK.getItems().add("Học sinh, sinh viên");
                                                    cbLHK.getItems().add("Trẻ em");
                                                    cbLHK.setValue("Người lớn");
                                                    Button btnXacNhanTT = (Button)tab.getContent().lookup("#btnXacNhanTT");
                                                    btnXacNhanTT.setOnMouseClicked(e1 -> {
                                                        for(Tab t : tabTTVe.getTabs()) {
                                                            TextField txtMaCN = (TextField) t.getContent().lookup("#txtMaCho");
                                                            TextField txtMaCNKH = (TextField) t.getContent().lookup("#txtMaChoKH");
                                                            if (!txtMaCN.getText().isEmpty() && txtMaCN.getText() != null) {
                                                                TextField txtTenHK = (TextField) t.getContent().lookup("#txtTenHK");
                                                                TextField txtSoCCCD = (TextField) t.getContent().lookup("#txtSoCCCD");
                                                                DatePicker dpNS = (DatePicker) t.getContent().lookup("#dpNgaySinh");
                                                                ComboBox<String> comboBox = (ComboBox<String>) t.getContent().lookup("#cbLoaiHK");
                                                                if (comboBox.getValue().equalsIgnoreCase("Trẻ em")) {
                                                                    if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtTenHK.requestFocus();
                                                                    } else if (dpNS.getValue() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        dpNS.requestFocus();
                                                                    }
                                                                } else if (comboBox.getValue().equalsIgnoreCase( "Người lớn") || comboBox.getValue().equalsIgnoreCase("Học sinh, sinh viên")) {
                                                                    if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtTenHK.requestFocus();
                                                                    } else if (txtSoCCCD.getText().isEmpty() && txtSoCCCD.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtSoCCCD.requestFocus();
                                                                    }
                                                                } else if (comboBox.getValue().equalsIgnoreCase( "Người cao tuổi")){
                                                                    if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtTenHK.requestFocus();
                                                                    } else if (txtSoCCCD.getText().isEmpty() && txtSoCCCD.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtSoCCCD.requestFocus();
                                                                    } else if (dpNS.getValue() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        dpNS.requestFocus();
                                                                    }
                                                                }
                                                            } else {
                                                                tabTTVe.getSelectionModel().getSelectedItem();
                                                                txtMaCN.requestFocus();
                                                                if (khuHoi && (txtMaCNKH.getText().isEmpty() || txtMaCNKH.getText() == null)) {
                                                                    tabTTVe.getSelectionModel().select(t);
                                                                    txtMaCNKH.requestFocus();
                                                                }
                                                            }
                                                        }
                                                        acpKH.setVisible(true);
                                                    });
                                                    if (chkKhuHoi.isSelected()){
                                                        AnchorPane acpKH = (AnchorPane) tab.getContent().lookup("#acpKH");
                                                        acpKH.setVisible(true);
                                                    }
                                                } catch (IOException ex) {
                                                    throw new RuntimeException(ex);
                                                }
                                                tabTTVe.getTabs().add(tab);
                                            }
                                        }
                                        if (slnct > 0) {
                                            for (int i = 0; i < slnct; i++) {
                                                Tab tab = new Tab("Người cao tuổi " + (i + 1));
                                                try {
                                                    tab.setContent(FXMLLoader.load(TrangChu_GUI.class.getResource("thong-tin-ve.fxml")));
                                                    ComboBox<String> cbLHK = (ComboBox)tab.getContent().lookup("#cbLoaiHK");
                                                    cbLHK.getItems().add("Người lớn");
                                                    cbLHK.getItems().add("Người cao tuổi");
                                                    cbLHK.getItems().add("Học sinh, sinh viên");
                                                    cbLHK.getItems().add("Trẻ em");
                                                    cbLHK.setValue("Người cao tuổi");

                                                    DatePicker dpNgaySinh = (DatePicker)tab.getContent().lookup("#dpNgaySinh");
                                                    dpNgaySinh.setDisable(false);
                                                    Button btnXacNhanTT = (Button)tab.getContent().lookup("#btnXacNhanTT");
                                                    btnXacNhanTT.setOnMouseClicked(e1 -> {
                                                        for(Tab t : tabTTVe.getTabs()) {
                                                            TextField txtMaCN = (TextField) t.getContent().lookup("#txtMaCho");
                                                            TextField txtMaCNKH = (TextField) t.getContent().lookup("#txtMaChoKH");
                                                            if (!txtMaCN.getText().isEmpty() && txtMaCN.getText() != null) {
                                                                TextField txtTenHK = (TextField) t.getContent().lookup("#txtTenHK");
                                                                TextField txtSoCCCD = (TextField) t.getContent().lookup("#txtSoCCCD");
                                                                DatePicker dpNS = (DatePicker) t.getContent().lookup("#dpNgaySinh");
                                                                ComboBox<String> comboBox = (ComboBox<String>) t.getContent().lookup("#cbLoaiHK");
                                                                if (comboBox.getValue().equalsIgnoreCase("Trẻ em")) {
                                                                    if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtTenHK.requestFocus();
                                                                    } else if (dpNS.getValue() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        dpNS.requestFocus();
                                                                    }
                                                                } else if (comboBox.getValue().equalsIgnoreCase( "Người lớn") || comboBox.getValue().equalsIgnoreCase("Học sinh, sinh viên")) {
                                                                    if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtTenHK.requestFocus();
                                                                    } else if (txtSoCCCD.getText().isEmpty() && txtSoCCCD.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtSoCCCD.requestFocus();
                                                                    }
                                                                } else if (comboBox.getValue().equalsIgnoreCase( "Người cao tuổi")){
                                                                    if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtTenHK.requestFocus();
                                                                    } else if (txtSoCCCD.getText().isEmpty() && txtSoCCCD.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtSoCCCD.requestFocus();
                                                                    } else if (dpNS.getValue() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        dpNS.requestFocus();
                                                                    }
                                                                }
                                                            } else {
                                                                tabTTVe.getSelectionModel().getSelectedItem();
                                                                txtMaCN.requestFocus();
                                                                if (khuHoi && (txtMaCNKH.getText().isEmpty() || txtMaCNKH.getText() == null)) {
                                                                    tabTTVe.getSelectionModel().select(t);
                                                                    txtMaCNKH.requestFocus();
                                                                }
                                                            }
                                                        }
                                                        acpKH.setVisible(true);
                                                    });
                                                    if (chkKhuHoi.isSelected()){
                                                        AnchorPane acpKH = (AnchorPane) tab.getContent().lookup("#acpKH");
                                                        acpKH.setVisible(true);
                                                    }
                                                } catch (IOException ex) {
                                                    throw new RuntimeException(ex);
                                                }
                                                tabTTVe.getTabs().add(tab);
                                            }
                                        }
                                        if (slhssv > 0) {
                                            for (int i = 0; i < slhssv; i++) {
                                                Tab tab = new Tab("HSSV " + (i + 1));
                                                try {
                                                    tab.setContent(FXMLLoader.load(TrangChu_GUI.class.getResource("thong-tin-ve.fxml")));
                                                    ComboBox<String> cbLHK = (ComboBox)tab.getContent().lookup("#cbLoaiHK");
                                                    cbLHK.getItems().add("Người lớn");
                                                    cbLHK.getItems().add("Người cao tuổi");
                                                    cbLHK.getItems().add("Học sinh, sinh viên");
                                                    cbLHK.getItems().add("Trẻ em");
                                                    cbLHK.setValue("Học sinh, sinh viên");
                                                    Button btnXacNhanTT = (Button)tab.getContent().lookup("#btnXacNhanTT");
                                                    btnXacNhanTT.setOnMouseClicked(e1 -> {
                                                        for(Tab t : tabTTVe.getTabs()) {
                                                            TextField txtMaCN = (TextField) t.getContent().lookup("#txtMaCho");
                                                            TextField txtMaCNKH = (TextField) t.getContent().lookup("#txtMaChoKH");
                                                            if (!txtMaCN.getText().isEmpty() && txtMaCN.getText() != null) {
                                                                TextField txtTenHK = (TextField) t.getContent().lookup("#txtTenHK");
                                                                TextField txtSoCCCD = (TextField) t.getContent().lookup("#txtSoCCCD");
                                                                DatePicker dpNS = (DatePicker) t.getContent().lookup("#dpNgaySinh");
                                                                ComboBox<String> comboBox = (ComboBox<String>) t.getContent().lookup("#cbLoaiHK");
                                                                if (comboBox.getValue().equalsIgnoreCase("Trẻ em")) {
                                                                    if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtTenHK.requestFocus();
                                                                    } else if (dpNS.getValue() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        dpNS.requestFocus();
                                                                    }
                                                                } else if (comboBox.getValue().equalsIgnoreCase( "Người lớn") || comboBox.getValue().equalsIgnoreCase("Học sinh, sinh viên")) {
                                                                    if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtTenHK.requestFocus();
                                                                    } else if (txtSoCCCD.getText().isEmpty() && txtSoCCCD.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtSoCCCD.requestFocus();
                                                                    }
                                                                } else if (comboBox.getValue().equalsIgnoreCase( "Người cao tuổi")){
                                                                    if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtTenHK.requestFocus();
                                                                    } else if (txtSoCCCD.getText().isEmpty() && txtSoCCCD.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtSoCCCD.requestFocus();
                                                                    } else if (dpNS.getValue() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        dpNS.requestFocus();
                                                                    }
                                                                }
                                                            } else {
                                                                tabTTVe.getSelectionModel().getSelectedItem();
                                                                txtMaCN.requestFocus();
                                                                if (khuHoi && (txtMaCNKH.getText().isEmpty() || txtMaCNKH.getText() == null)) {
                                                                    tabTTVe.getSelectionModel().select(t);
                                                                    txtMaCNKH.requestFocus();
                                                                }
                                                            }
                                                        }
                                                        acpKH.setVisible(true);
                                                    });                                                            if (chkKhuHoi.isSelected()){
                                                        AnchorPane acpKH = (AnchorPane) tab.getContent().lookup("#acpKH");
                                                        acpKH.setVisible(true);
                                                    }
                                                } catch (IOException ex) {
                                                    throw new RuntimeException(ex);
                                                }
                                                tabTTVe.getTabs().add(tab);
                                            }
                                        }
                                        if (slte > 0) {
                                            for (int i = 0; i < slte; i++) {
                                                Tab tab = new Tab("Trẻ em " + (i + 1));
                                                try {
                                                    tab.setContent(FXMLLoader.load(TrangChu_GUI.class.getResource("thong-tin-ve.fxml")));
                                                    ComboBox<String> cbLHK = (ComboBox)tab.getContent().lookup("#cbLoaiHK");
                                                    cbLHK.getItems().add("Người lớn");
                                                    cbLHK.getItems().add("Người cao tuổi");
                                                    cbLHK.getItems().add("Học sinh, sinh viên");
                                                    cbLHK.getItems().add("Trẻ em");
                                                    cbLHK.setValue("Trẻ em");
                                                    DatePicker dpNgaySinh = (DatePicker)tab.getContent().lookup("#dpNgaySinh");
                                                    dpNgaySinh.setDisable(false);
                                                    TextField txtSoCCCD = (TextField)tab.getContent().lookup("#txtSoCCCD");
                                                    txtSoCCCD.setDisable(true);
                                                    Button btnXacNhanTT = (Button)tab.getContent().lookup("#btnXacNhanTT");
                                                    btnXacNhanTT.setOnMouseClicked(e1 -> {
                                                        for(Tab t : tabTTVe.getTabs()) {
                                                            TextField txtMaCN = (TextField) t.getContent().lookup("#txtMaCho");
                                                            TextField txtMaCNKH = (TextField) t.getContent().lookup("#txtMaChoKH");
                                                            if (!txtMaCN.getText().isEmpty() && txtMaCN.getText() != null) {
                                                                TextField txtTenHK = (TextField) t.getContent().lookup("#txtTenHK");
                                                                TextField txtsoCCCD = (TextField) t.getContent().lookup("#txtSoCCCD");
                                                                DatePicker dpNS = (DatePicker) t.getContent().lookup("#dpNgaySinh");
                                                                ComboBox<String> comboBox = (ComboBox<String>) t.getContent().lookup("#cbLoaiHK");
                                                                if (comboBox.getValue().equalsIgnoreCase("Trẻ em")) {
                                                                    if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtTenHK.requestFocus();
                                                                    } else if (dpNS.getValue() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        dpNS.requestFocus();
                                                                    }
                                                                } else if (comboBox.getValue().equalsIgnoreCase( "Người lớn") || comboBox.getValue().equalsIgnoreCase("Học sinh, sinh viên")) {
                                                                    if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtTenHK.requestFocus();
                                                                    } else if (txtsoCCCD.getText().isEmpty() && txtsoCCCD.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtsoCCCD.requestFocus();
                                                                    }
                                                                } else if (comboBox.getValue().equalsIgnoreCase( "Người cao tuổi")){
                                                                    if (txtTenHK.getText().isEmpty() || txtTenHK.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtTenHK.requestFocus();
                                                                    } else if (txtsoCCCD.getText().isEmpty() && txtsoCCCD.getText() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        txtsoCCCD.requestFocus();
                                                                    } else if (dpNS.getValue() == null) {
                                                                        tabTTVe.getSelectionModel().select(t);
                                                                        dpNS.requestFocus();
                                                                    }
                                                                }
                                                            } else {
                                                                tabTTVe.getSelectionModel().getSelectedItem();
                                                                txtMaCN.requestFocus();
                                                                if (khuHoi && (txtMaCNKH.getText().isEmpty() || txtMaCNKH.getText() == null)) {
                                                                    tabTTVe.getSelectionModel().select(t);
                                                                    txtMaCNKH.requestFocus();
                                                                }
                                                            }
                                                        }
                                                        acpKH.setVisible(true);
                                                    });
                                                    if (chkKhuHoi.isSelected()){
                                                        AnchorPane acpKH = (AnchorPane) tab.getContent().lookup("#acpKH");
                                                        acpKH.setVisible(true);
                                                    }
                                                } catch (IOException ex) {
                                                    throw new RuntimeException(ex);
                                                }
                                                tabTTVe.getTabs().add(tab);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    };
                    return cell;
                };
                colChonKH.setCellFactory(cellFactory);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Không có lịch trình nào");
                alert.showAndWait();
                dpNgayVe.requestFocus();
                dpNgayVe.show();
                return;
            }
        });

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
