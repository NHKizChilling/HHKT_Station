/*
 * @ (#) BanVeController.java       1.0     04/10/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import connectdb.ConnectDB;
import dao.CT_LichTrinh_DAO;
import dao.Ga_DAO;
import dao.LichTrinh_DAO;
import entity.ChiTietLichTrinh;
import entity.ChoNgoi;
import entity.Ga;
import entity.LichTrinh;
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
    private TableColumn<LichTrinh, String> colChon;

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
    private Button btnThanhToan;

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

    private boolean khuHoi = false;


    private ObservableList<LichTrinh> dslt = null;
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
            chkKhuHoi.setSelected(false);
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

            if (dslt != null) {
                tbTCTLT.getItems().clear();
                dslt.clear();
            }
            acpToaTau.setVisible(false);
            tabToaTau.getTabs().clear();
            acpKH.setVisible(false);
            scr11.setVisible(false);
            tabTTVe.getTabs().clear();
        });

        btnTraCuuCT.setOnMouseClicked(e -> {
            tbTCTLT.setItems(null);
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
                    colSoHieuTau.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("chuyenTau"));
                    colGaDi.setCellValueFactory((new PropertyValueFactory<LichTrinh, String>("gaDi")));
                    colGaDen.setCellValueFactory((new PropertyValueFactory<LichTrinh, String>("gaDen")));
                    colGioKH.setCellValueFactory((new PropertyValueFactory<LichTrinh, String>("thoiGianKhoiHanh")));
                    colTGDen.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("thoiGianDuKienDen"));
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
                                            ArrayList<ChiTietLichTrinh> dsctlt = ctlt_dao.getCtltTheoMaLichTrinh(lt.getMaLichTrinh());
                                            acpToaTau.setVisible(true);
                                            tabToaTau.getTabs().clear();

                                            GridPane gridPane1 = new GridPane();

                                            tabToaTau.getTabs().add(new Tab("Toa 1: Ngồi mềm điều hòa", gridPane1));

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
                                                    if (BanVeController.this.contains(bookedSeats1, seatNumber)) {
                                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: red; -fx-text-fill: white;");
                                                    } else {
                                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
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
                                                    if (BanVeController.this.contains(bookedSeats, seatNumber)) {
                                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: red; -fx-text-fill: white;");
                                                    } else if (BanVeController.this.contains(blueSeats, seatNumber)) {
                                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: blue; -fx-text-fill: white;");
                                                    } else {
                                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
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
                                            int[] bookedSeats2 = {1, 2, 3, 4, 7, 8, 9, 10, 13, 14, 15, 16, 19, 20, 25, 26, 31, 32, 34, 37, 38};
                                            int[] blueSeats2 = {5, 6, 11, 12, 17, 18};
                                            GridPane gridPane2 = new GridPane();
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
                                                    seatButton.setId(seatNumber + "");
                                                    seatButton.setMinSize(30, 30); // Kích thước nút ghế
                                                    seatButton.setStyle("-fx-border-color: black; -fx-border-width: 0 0 2 0");
                                                    // Kiểm tra trạng thái của ghế
                                                    if (BanVeController.this.contains(bookedSeats, seatNumber)) {
                                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: red; -fx-text-fill: white;");
                                                    } else if (BanVeController.this.contains(blueSeats, seatNumber)) {
                                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: blue; -fx-text-fill: white;");
                                                    } else {
                                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
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
                                            AnchorPane pnTTCN;
                                            try {
                                                pnTTCN = FXMLLoader.load(TrangChu_GUI.class.getResource("thong-tin-ve.fxml"));
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
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
                                                            cbLHK.getItems().add("Học sinh - Sinh viên");
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
                                                                        } else if (comboBox.getValue().equalsIgnoreCase( "Người lớn") || comboBox.getValue().equalsIgnoreCase("Học sinh - Sinh viên")) {
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
                                                            cbLHK.getItems().add("Học sinh - Sinh viên");
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
                                                                        } else if (comboBox.getValue().equalsIgnoreCase( "Người lớn") || comboBox.getValue().equalsIgnoreCase("Học sinh - Sinh viên")) {
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
                                                            cbLHK.getItems().add("Học sinh - Sinh viên");
                                                            cbLHK.getItems().add("Trẻ em");
                                                            cbLHK.setValue("Học sinh - Sinh viên");
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
                                                                        } else if (comboBox.getValue().equalsIgnoreCase( "Người lớn") || comboBox.getValue().equalsIgnoreCase("Học sinh - Sinh viên")) {
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
                                                            cbLHK.getItems().add("Học sinh - Sinh viên");
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
                                                                        } else if (comboBox.getValue().equalsIgnoreCase( "Người lớn") || comboBox.getValue().equalsIgnoreCase("Học sinh - Sinh viên")) {
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

        btnThanhToan.setOnMouseClicked(e -> {
            try {
                BorderPane borderPane = FXMLLoader.load(TrangChu_GUI.class.getResource("dang-nhap.fxml"));
                Stage stgThanhToan = new Stage();
                stgThanhToan.setTitle("Thanh toán");
                stgThanhToan.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
                stgThanhToan.setScene(new Scene(borderPane));
                stgThanhToan.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

        btnLamMoiCN.setOnMouseClicked(e -> {
            tabTTVe.getSelectionModel().getSelectedItem().getContent().setDisable(false);
        });

        btnHuyChon.setOnMouseClicked(e -> {
            if (!scr11.isVisible()) {
                if (idBtnChosen != "") {
                    Button btn = (Button) tabToaTau.getTabs().get(tabToaTau.getSelectionModel().getSelectedIndex()).getContent().lookup("#" + idBtnChosen);
                    btn.setStyle(btn.getStyle() + "-fx-background-color: white; -fx-text-fill: black;");
                }
                idBtnChosen = "";
                btnChonChoNgoi.setDisable(true);
                btnHuyChon.setDisable(true);
            } else if (scr11.isVisible()) {
                if (idBtnChonKH != "") {
                    Button btn = (Button) tabToaTauKH.getTabs().get(tabToaTauKH.getSelectionModel().getSelectedIndex()).getContent().lookup("#" + idBtnChonKH);
                    btn.setStyle(btn.getStyle() + "-fx-background-color: white; -fx-text-fill: black;");
                }
                idBtnChonKH = "";
                btnChonChoNgoi.setDisable(true);
                btnHuyChon.setDisable(true);
            }
        });

        btnChonChoNgoi.setOnMouseClicked(e -> {
            if (idBtnChosen != "" || idBtnChonKH != "") {
                TextField txtMaCho =(TextField) tabTTVe.getTabs().get(tabTTVe.getSelectionModel().getSelectedIndex()).getContent().lookup("#txtMaCho");
                TextField txtLoaiCho =(TextField) tabTTVe.getTabs().get(tabTTVe.getSelectionModel().getSelectedIndex()).getContent().lookup("#txtLoaiCho");
                TextField txtMaCNKH = (TextField) tabTTVe.getTabs().get(tabTTVe.getSelectionModel().getSelectedIndex()).getContent().lookup("#txtMaChoKH");
                TextField txtLoaiChoKH =(TextField) tabTTVe.getTabs().get(tabTTVe.getSelectionModel().getSelectedIndex()).getContent().lookup("#txtLoaiChoKH");

                if (scr11.isVisible()) {
                    Button btnKH = (Button) tabToaTauKH.getTabs().get(tabToaTauKH.getSelectionModel().getSelectedIndex()).getContent().lookup("#" + idBtnChonKH);
                    if (txtMaCNKH.getText().isEmpty() || txtMaCNKH.getText() == null) {
                        txtMaCNKH.setText(idBtnChonKH);
                        txtLoaiChoKH.setText(tabToaTauKH.getSelectionModel().getSelectedIndex() + "");
                        btnKH.setStyle(btnKH.getStyle() + ";-fx-background-color: green; -fx-text-fill: white;");
                        tabTTVe.getSelectionModel().selectNext();
                    } else {
                        Button btnOld = (Button) tabToaTauKH.getTabs().get(Integer.parseInt(txtLoaiChoKH.getText())).getContent().lookup("#" + txtMaCNKH.getText());
                        btnOld.setStyle(btnOld.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
                        txtMaCNKH.setText(idBtnChonKH);
                        txtLoaiChoKH.setText(tabToaTauKH.getSelectionModel().getSelectedIndex() + "");
                        btnKH.setStyle(btnKH.getStyle() + ";-fx-background-color: green; -fx-text-fill: white;");
                        tabTTVe.getSelectionModel().selectNext();
                    }
                    idBtnChonKH = "";
                    btnChonChoNgoi.setDisable(true);
                    btnHuyChon.setDisable(true);
                } else if (scr1.isVisible()){
                    if (!txtMaCho.getText().isEmpty() && txtMaCho.getText() != null) {
                        Button btn = (Button) tabToaTau.getTabs().get(tabToaTau.getSelectionModel().getSelectedIndex()).getContent().lookup("#" + idBtnChosen);
                        Button btnOld = (Button) tabToaTau.getTabs().get(Integer.parseInt(txtLoaiCho.getText())).getContent().lookup("#" + txtMaCho.getText());
                        btnOld.setStyle(btnOld.getStyle() + ";-fx-background-color: white;;-fx-text-fill: black;");
                        txtMaCho.setText(idBtnChosen);
                        txtLoaiCho.setText(tabToaTau.getSelectionModel().getSelectedIndex() + "");
                        btn.setStyle(btn.getStyle() + ";-fx-background-color: green; -fx-text-fill: white;");
                        tabTTVe.getSelectionModel().selectNext();
                    } else {
                        Button btn = (Button) tabToaTau.getTabs().get(tabToaTau.getSelectionModel().getSelectedIndex()).getContent().lookup("#" + idBtnChosen);
                        btn.setStyle(btn.getStyle() + ";-fx-background-color: green; -fx-text-fill: white;");
                        txtMaCho.setText(idBtnChosen);
                        txtLoaiCho.setText(tabToaTau.getSelectionModel().getSelectedIndex() + "");
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
                        btnChuyenChonKH.setOnMouseClicked(ek -> {
                            for (Tab tab : tabTTVe.getTabs()) {
                                TextField txtMaCN1 = (TextField) tab.getContent().lookup("#txtMaCho");
                                if (txtMaCN1.getText().isEmpty() || txtMaCN1.getText() == null) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Vui lòng chọn ghế");
                                    alert.showAndWait();
                                    tabTTVe.getSelectionModel().select(tab);
                                    break;
                                }
                            }
                            btnChuyenChonKH.setVisible(false);
                            btnXacNhanTT.setVisible(true);
                            btnChonKH.setDisable(true);
                            btnChonCD.setDisable(false);
                            tabTTVe.getSelectionModel().selectFirst();
                            scr11.setVisible(true);
                            tbTCTLT.setItems(FXCollections.observableList(lt_dao.traCuuDSLichTrinh(ga_dao.getGaTheoTenGa(cbGaDi.getValue()).getMaGa(), ga_dao.getGaTheoTenGa(cbGaDen.getValue()).getMaGa(), dpNgayVe.getValue())));
                            colSTT.setCellValueFactory(column -> new SimpleIntegerProperty(tbTCTLT.getItems().indexOf(column.getValue()) + 1).asObject());
                            GridPane gridPane1 = new GridPane();

                            tabToaTauKH.getTabs().add(new Tab("Toa 1: Ngồi mềm điều hòa kh", gridPane1));

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
                                    if (BanVeController.this.contains(bookedSeats1, seatNumber)) {
                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: red; -fx-text-fill: white;");
                                    } else {
                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
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
                                                Button btn = (Button) gridPane1.lookup("#" + idBtnChonKH);
                                                btn.setStyle(btn.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
                                            }
                                            idBtnChonKH = seatButton.getId();
                                            seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: blue; -fx-text-fill: white;");
                                        }
                                    });
                                }
                            }
                            //Toa 2: giường nằm khoang 6 điều hòa
                            GridPane gridPane = new GridPane();
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
                                    if (BanVeController.this.contains(bookedSeats, seatNumber)) {
                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: red; -fx-text-fill: white;");
                                    } else if (BanVeController.this.contains(blueSeats, seatNumber)) {
                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: blue; -fx-text-fill: white;");
                                    } else {
                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
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
                                                Button btn = (Button) gridPane1.lookup("#" + idBtnChonKH);
                                                btn.setStyle(btn.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
                                            }
                                            idBtnChonKH = seatButton.getId();
                                            seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: blue; -fx-text-fill: white;");
                                        }
                                    });
                                }
                            }
                            tabToaTauKH.getTabs().add(new Tab("Toa 2: Giường nằm khoang 6 điều hòa", gridPane));
                            //Toa 3: Giường nằm khoang 4 điều hòa
                            int[][] seatNumbers2 = {
                                    {3, 4, 7, 8, 11, 12, 15, 16, 19, 20, 23, 24, 27, 28},
                                    {1, 2, 5, 6, 9, 10, 13, 14, 17, 18, 21, 22, 25, 26}
                            };

                            // Ghế đã được đặt (màu đỏ) và ghế còn trống (màu trắng hoặc xanh)
                            int[] bookedSeats2 = {1, 2, 3, 4, 7, 8, 9, 10, 13, 14, 15, 16, 19, 20, 25, 26, 31, 32, 34, 37, 38};
                            int[] blueSeats2 = {5, 6, 11, 12, 17, 18};
                            GridPane gridPane2 = new GridPane();
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
                                    seatButton.setId(seatNumber + "");
                                    seatButton.setMinSize(30, 30); // Kích thước nút ghế
                                    seatButton.setStyle("-fx-border-color: black; -fx-border-width: 0 0 2 0");
                                    // Kiểm tra trạng thái của ghế
                                    if (BanVeController.this.contains(bookedSeats, seatNumber)) {
                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: red; -fx-text-fill: white;");
                                    } else if (BanVeController.this.contains(blueSeats, seatNumber)) {
                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: blue; -fx-text-fill: white;");
                                    } else {
                                        seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
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
                                                Button btn = (Button) gridPane1.lookup("#" + idBtnChonKH);
                                                btn.setStyle(btn.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
                                            }
                                            idBtnChonKH = seatButton.getId();
                                            seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: blue; -fx-text-fill: white;");
                                        }
                                    });
                                }
                            }
                            tabToaTauKH.getTabs().add(new Tab("Toa 3: Giường nằm khoang 4 điều hòa", gridPane2));
                        });

                        if (!txtMaCN.getText().isEmpty() && txtMaCN.getText() != null) {
                            btnChuyenChonKH.setTextFill(Color.BLACK);
                        } else {
                            btnChuyenChonKH.setVisible(false);
                            btnChuyenChonKH.setTextFill(Color.GRAY);
                        }
                        if (!txtMaCNKH.getText().isEmpty() && txtMaCNKH.getText() != null) {
                            btnChuyenChonKH.setVisible(false);
                            btnXacNhanTT.setVisible(true);
                            btnXacNhanTT.setDisable(false);
                            tabTTVe.getSelectionModel().selectFirst();

                        } else {
                            btnChuyenChonKH.setVisible(false);
                        }
                    } else {
                        btnXacNhanTT.setVisible(true);
                        btnXacNhanTT.setDisable(false);
                        tabTTVe.getSelectionModel().selectFirst();
                    }

                }
            }
        });

        btnChonCD.setOnMouseClicked(e -> {
            btnChonCD.setDisable(true);
            btnChonKH.setDisable(false);
            scr11.setVisible(false);
        });

        btnChonKH.setOnMouseClicked(e -> {
            btnChonKH.setDisable(true);
            btnChonCD.setDisable(false);
            scr11.setVisible(true);
            tabTTVe.getSelectionModel().selectFirst();

            if (tabToaTauKH.getTabs().size() <= 0) {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Lỗi");
                alert1.setHeaderText(null);
                alert1.setContentText("Vui lòng chọn chuyến đi");
                alert1.show();
                String maGaDi = ga_dao.getGaTheoTenGa(cbGaDi.getValue()).getMaGa();
                String maGaDen = ga_dao.getGaTheoTenGa(cbGaDen.getValue()).getMaGa();
                ArrayList<LichTrinh> list = lt_dao.traCuuDSLichTrinh(maGaDen, maGaDi, dpNgayVe.getValue());
                dslt = FXCollections.observableList(list);
                if (dslt.size() > 0) {
                    tbTCTLT.setItems(dslt);
                    colSTT.setCellValueFactory(column -> new SimpleIntegerProperty(tbTCTLT.getItems().indexOf(column.getValue()) + 1).asObject());
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

                tabToaTauKH.getTabs().clear();
                GridPane gridPane1 = new GridPane();

                tabToaTauKH.getTabs().add(new Tab("Toa 1: Ngồi mềm điều hòa KH", gridPane1));

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
                        if (BanVeController.this.contains(bookedSeats1, seatNumber)) {
                            seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: red; -fx-text-fill: white;");
                        } else {
                            seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
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
                                alert1.setTitle("Lỗi");
                                alert1.setHeaderText(null);
                                alert1.setContentText("Ghế đã được đặt");
                                alert1.show();
                            } else {
                                btnChonChoNgoi.setDisable(false);
                                btnHuyChon.setDisable(false);
                                if (idBtnChonKH != "") {
                                    Button btn = (Button) gridPane1.lookup("#" + idBtnChonKH);
                                    btn.setStyle(btn.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
                                }
                                idBtnChonKH = seatButton.getId();
                                seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: blue; -fx-text-fill: white;");
                            }
                        });
                    }
                }
                //Toa 2: giường nằm khoang 6 điều hòa
                GridPane gridPane = new GridPane();
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
                        if (BanVeController.this.contains(bookedSeats, seatNumber)) {
                            seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: red; -fx-text-fill: white;");
                        } else if (BanVeController.this.contains(blueSeats, seatNumber)) {
                            seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: blue; -fx-text-fill: white;");
                        } else {
                            seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
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
                                    Button btn = (Button) gridPane1.lookup("#" + idBtnChonKH);
                                    btn.setStyle(btn.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
                                }
                                idBtnChonKH = seatButton.getId();
                                seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: blue; -fx-text-fill: white;");
                            }
                        });
                    }
                }
                tabToaTauKH.getTabs().add(new Tab("Toa 2: Giường nằm khoang 6 điều hòa", gridPane));
                //Toa 3: Giường nằm khoang 4 điều hòa
                int[][] seatNumbers2 = {
                        {3, 4, 7, 8, 11, 12, 15, 16, 19, 20, 23, 24, 27, 28},
                        {1, 2, 5, 6, 9, 10, 13, 14, 17, 18, 21, 22, 25, 26}
                };

                // Ghế đã được đặt (màu đỏ) và ghế còn trống (màu trắng hoặc xanh)
                int[] bookedSeats2 = {1, 2, 3, 4, 7, 8, 9, 10, 13, 14, 15, 16, 19, 20, 25, 26, 31, 32, 34, 37, 38};
                int[] blueSeats2 = {5, 6, 11, 12, 17, 18};
                GridPane gridPane2 = new GridPane();
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
                        seatButton.setId(seatNumber + "");
                        seatButton.setMinSize(30, 30); // Kích thước nút ghế
                        seatButton.setStyle("-fx-border-color: black; -fx-border-width: 0 0 2 0");
                        // Kiểm tra trạng thái của ghế
                        if (BanVeController.this.contains(bookedSeats, seatNumber)) {
                            seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: red; -fx-text-fill: white;");
                        } else if (BanVeController.this.contains(blueSeats, seatNumber)) {
                            seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: blue; -fx-text-fill: white;");
                        } else {
                            seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
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
                                    Button btn = (Button) gridPane1.lookup("#" + idBtnChonKH);
                                    btn.setStyle(btn.getStyle() + ";-fx-background-color: white; -fx-text-fill: black;");
                                }
                                idBtnChonKH = seatButton.getId();
                                seatButton.setStyle(seatButton.getStyle() + ";-fx-background-color: blue; -fx-text-fill: white;");
                            }
                        });
                    }
                }
                tabToaTauKH.getTabs().add(new Tab("Toa 3: Giường nằm khoang 4 điều hòa", gridPane2));
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
