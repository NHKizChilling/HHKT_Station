package controller;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import dao.*;
import entity.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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


    @FXML
    private ComboBox<String> cb_gaDi;

    @FXML
    private ComboBox<String> cb_gaDen;

    @FXML
    private DatePicker dp_ngayKhoiHanh;

    @FXML
    private Button btnQuetMaVe;

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
    private ScrollPane scrToaTau;

    @FXML
    private Label lblGiaCN;

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
    Toa_DAO toa_dao;
    LoaiToa_DAO ltoa_dao;
    ChoNgoi_DAO cn_dao;

    private String idBtnChosen = "";
    private Ve ve;
    private String newMaCho = "";
    private String newMaLichTrinh = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
        initDao();
        cb_search.getItems().addAll("Mã hành khách", "Mã vé", "Số CCCD");
        cb_search.setOnAction(e -> {
            if (cb_search.getValue() != null && cb_search.getValue().equalsIgnoreCase("Mã vé")) {
                btnQuetMaVe.setDisable(false);
            } else {
                btnQuetMaVe.setDisable(true);
            }
        });

        //enable btn_traCuu khi chọn ngày kh
        dp_ngayKhoiHanh.setOnAction(e -> {
            if (dp_ngayKhoiHanh.getValue() != null) {
                btn_traCuuLT.setDisable(false);
            } else {
                btn_traCuuLT.setDisable(true);
            }
        });

        btnQuetMaVe.setOnMouseClicked(e -> {

                Webcam webcam = Webcam.getDefault();
                ScheduledExecutorService timer;
                webcam.open();
                //show fps
                ImageView imageView = new ImageView();
                imageView.setFitWidth(640);
                imageView.setFitHeight(480);
                StackPane root = new StackPane();
                root.getChildren().add(imageView);

                Stage primaryStage = new Stage();
                Scene scene = new Scene(root, 640, 480);
                primaryStage.setTitle("Tra cứu");
                primaryStage.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
                primaryStage.setScene(scene);
                primaryStage.show();
                // Cập nhật video stream và quét mã vạch theo chu kỳ
                timer = Executors.newSingleThreadScheduledExecutor();
                timer.scheduleAtFixedRate(() -> {
                    BufferedImage image = webcam.getImage();
                    if (image != null) {
                        Image fxImage = SwingFXUtils.toFXImage(image, null);
                        Platform.runLater(() -> imageView.setImage(fxImage));

                        // Quét mã vạch từ hình ảnh
                        String result = decodeBarcode(image);
                        if (result != null) {
                            txt_search.setText(result);
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setHeaderText(null);
                                alert.setTitle("Tra cứu");
                                alert.setContentText("Tra cứu thành công!");
                                alert.showAndWait();
                                primaryStage.close();
                            });
                            timer.shutdown();
                            webcam.close();
                            primaryStage.setOpacity(0);
                        }
                    }
                }, 0, 33, TimeUnit.MILLISECONDS);


                primaryStage.setOnCloseRequest(es -> {
                    timer.shutdown();
                    webcam.close();
                });



        });

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
            lamMoi();
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
                        btn_xacNhan.setDisable(false);
                    }
                }
            }
        });

        btn_xacNhan.setOnAction(e -> {
            if (ve == null) {
                label_thongBao.setText("Vui lòng chọn vé cần đổi");
                return;
            }
            if (!ve.getTinhTrangVe().equalsIgnoreCase("DaBan")) {
                label_thongBao.setText("Vé không thể đổi");
                return;
            }
            LichTrinh lt = lichTrinh_dao.getLichTrinhTheoID(ve.getCtlt().getLichTrinh().getMaLichTrinh());
            String tenGaDi = ga_dao.getGaTheoMaGa(lt.getGaDi().getMaGa()).getTenGa();
            String tenGaDen = ga_dao.getGaTheoMaGa(lt.getGaDen().getMaGa()).getTenGa();

            cb_gaDi.setValue(tenGaDi);
            cb_gaDen.setValue(tenGaDen);
        });

        btn_traCuuLT.setOnAction(e -> {
            String gaDi = cb_gaDi.getValue();
            String gaDen = cb_gaDen.getValue();

            String maGaDi = ga_dao.getGaTheoTenGa(gaDi).getMaGa();
            String maGaDen = ga_dao.getGaTheoTenGa(gaDen).getMaGa();

            if (gaDi == null || gaDen == null || dp_ngayKhoiHanh.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ga đi, ga đến và ngày khởi hành");
                alert.show();
                return;
            }

            ArrayList<LichTrinh> listLT = lichTrinh_dao.traCuuDSLichTrinh(maGaDi, maGaDen, dp_ngayKhoiHanh.getValue());
            renderTableLichTrinh(listLT);
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
            KhachHang hk = ve.getHanhKhach();
            LoaiVe lv = ve.getLoaiVe();
            String tenHK = ve.getTenHanhKhach();
            String soCCCD = ve.getSoCCCD();
            LocalDate dob = ve.getNgaySinh();
            boolean khuHoi = ve.isKhuHoi();
            ChoNgoi cn = new ChoNgoi(idBtnChosen);
            LichTrinh lt = new LichTrinh(getData.lt.getMaLichTrinh());
            ChiTietLichTrinh ctlt = ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), idBtnChosen);
            boolean khuHoiMoi = ve.isKhuHoi();

            Ve veMoi = new Ve(ve.getMaVe(), hk, ctlt, lv, tenHK, soCCCD, dob, "DaDoi", khuHoiMoi);

            //Update cthd
            ChiTietHoaDon cthd = cthd_dao.getCT_HoaDonTheoMaVe(veMoi.getMaVe());
            cthd.setVe(veMoi);
            cthd.tinhGiaVe();
            cthd.tinhGiaGiam();
            cthd_dao.update(cthd);
            //Update vé
            ve_dao.update(veMoi);

            // Gọi phương thức in vé
            try {
                new PrintPDF().inVe(new ArrayList<Ve>(Arrays.asList(veMoi)));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });

    }

    public void renderTableVe(ArrayList<Ve> listVe) {
        // TODO
        ObservableList<Ve> data = FXCollections.observableArrayList(listVe);
        tbl_thongTinVe.setItems(data);
        col_maVe.setCellValueFactory(new PropertyValueFactory<>("maVe"));
        col_maKH.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getHanhKhach().getMaKH()));
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

    public void renderTableLichTrinh(ArrayList<LichTrinh> listLT) {
        // TODO
        ObservableList<LichTrinh> data = FXCollections.observableArrayList(listLT);
        tbl_lichTrinh.setItems(data);
        col_stt.setCellValueFactory(p -> new SimpleIntegerProperty(tbl_lichTrinh.getItems().indexOf(p.getValue()) + 1).asObject());
        col_soHieuTau.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getChuyenTau().getSoHieutau()));
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
        col_trangThai.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().isTinhTrang() ? "Đang chạy" : "Đã kết thúc"));

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
                            LichTrinh lt = tbl_lichTrinh.getItems().get(getIndex());
                            getData.lt = lt;
                            btnChon.setCursor(Cursor.WAIT);
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
                            scrToaTau.setVisible(true);
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
                                                    btn_doiVe.setDisable(false);
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
                                                    btn_doiVe.setDisable(false);
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
                                                    btn_doiVe.setDisable(false);
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
                                                    btn_doiVe.setDisable(false);
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
                                                    btn_doiVe.setDisable(false);
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
                            btnChon.setCursor(Cursor.HAND);

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
        toa_dao = new Toa_DAO();
        ltoa_dao = new LoaiToa_DAO();
        cn_dao = new ChoNgoi_DAO();
    }

    private String decodeBarcode(BufferedImage image){
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        // Chỉ định định dạng mã vạch là Code 128, EAN-13, UPC-A hoặc QR Code
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(
                BarcodeFormat.CODE_128
        ));

        try {
            Result result = new MultiFormatReader().decode(bitmap, hints);
            return result.getText();
        } catch (NotFoundException e) {
            // Không tìm thấy mã vạch trong ảnh
            return null;
        }
    }

    public void lamMoi() {
        ve = null;
        btn_xacNhan.setDisable(true);
        btnQuetMaVe.setDisable(true);
        btn_doiVe.setDisable(true);
        btn_traCuuLT.setDisable(true);
        dp_ngayKhoiHanh.setValue(null);
        txt_search.clear();
        cb_search.setValue(null);
        tbl_thongTinVe.getItems().clear();
        tbl_lichTrinh.getItems().clear();
        acpToaTau.setVisible(false);
        tabToaTau.getTabs().clear();
        cb_gaDi.setValue(null);
        cb_gaDen.setValue(null);
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
