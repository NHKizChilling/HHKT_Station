package controller;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.jfoenix.controls.JFXButton;
import dao.*;
import entity.*;
import gui.TrangChu_GUI;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DoiVeController implements Initializable {


    private final String styleGheThuong = "-fx-background-color: white; -fx-text-fill: black;-fx-border-color: black;";
    private final String styleGheDaChon = "-fx-background-color: green; -fx-text-fill: white;-fx-border-color: black;";
    private final String styleGhe1 = "-fx-border-width: 0 0 0 3;";
    private final String styleGhe2 = "-fx-border-width: 0 3 0 0;";
    private final String styleGheDaDat = "-fx-background-color: red; -fx-text-fill: white;-fx-border-color: black;";
    private final String styleGiuong = "-fx-background-color: white; -fx-text-fill: black;-fx-border-color: black;-fx-border-width: 0 0 3 0;";
    private final String styleGiuongDaChon = "-fx-background-color: green; -fx-text-fill: white;-fx-border-color: black;-fx-border-width: 0 0 3 0;";
    private final String styleGiuongDaDat = "-fx-background-color: red; -fx-text-fill: white;-fx-border-color: black;-fx-border-width: 0 0 3 0;";

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
    private JFXButton btnTraCuuCT;

    @FXML
    private Label lblToa;

    @FXML
    private Label lblGia;

    @FXML
    private HBox paneTau;

    @FXML
    private HBox grTrain;

    @FXML
    private BorderPane paneToa;

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

    private Ve ve;
    private String chosedId;
    private ChiTietLichTrinh ctlt = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
        initDao();
        cb_search.getItems().addAll("Mã khách hàng", "Mã vé", "Mã hóa đơn");
        cb_search.setOnAction(e -> {
            btnQuetMaVe.setDisable(cb_search.getValue() == null || !cb_search.getValue().equalsIgnoreCase("Mã vé"));
        });

        dp_ngayKhoiHanh.setOnAction(e -> {
            btnTraCuuCT.setDisable(dp_ngayKhoiHanh.getValue() == null);
        });

        //không cho phép chọn ngày trước ngày hiện tại dp_ngayKhoiHanh
        dp_ngayKhoiHanh.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
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
            if (search.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập thông tin tìm kiếm");
                alert.show();
                txt_search.requestFocus();
                return;
            }
            if (cb_search.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn loại tìm kiếm");
                alert.show();
                return;
            }
            ArrayList<Ve> listVe = new ArrayList<>();
            if (cb_search.getValue().equalsIgnoreCase("Mã khách hàng")) {
                listVe = ve_dao.getDSVeTheoMaKH(search);
            } else if (cb_search.getValue().equalsIgnoreCase("Mã vé")) {
                Ve ve = ve_dao.getVeTheoID(search);
                if (ve != null) {
                    listVe.add(ve);
                }
            } else if (cb_search.getValue().equalsIgnoreCase("Mã hóa đơn")) {
                listVe = ve_dao.getDSVeTheoMaKH(search);
            }
            if (listVe.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Không tìm thấy vé");
                alert.show();
                return;
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn ngày khởi hành");
            alert.show();
        });

        btnTraCuuCT.setOnAction(e -> {
            String gaDi = cb_gaDi.getValue();
            String gaDen = cb_gaDen.getValue();

            String maGaDi = ga_dao.getGaTheoTenGa(gaDi).getMaGa();
            String maGaDen = ga_dao.getGaTheoTenGa(gaDen).getMaGa();

            if (dp_ngayKhoiHanh.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ngày khởi hành");
                alert.show();
                return;
            }

            ArrayList<LichTrinh> listLT = lichTrinh_dao.traCuuDSLichTrinh(maGaDi, maGaDen, dp_ngayKhoiHanh.getValue());
            showTauTheoLT(listLT);
        });

        btn_doiVe.setOnAction(e -> {
            if (cb_gaDi.getValue() == null || cb_gaDen.getValue() == null || dp_ngayKhoiHanh.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ga đi, ga đến và ngày khởi hành");
                alert.show();
                return;
            }

            //lấy thông tin vé cũ
            KhachHang kh = ve.getKhachHang();
            LoaiVe lv = ve.getLoaiVe();
            String tenHK = ve.getTenHanhKhach();
            String soCCCD = ve.getSoCCCD();
            LocalDate dob = ve.getNgaySinh();
            boolean khuHoiMoi = ve.isKhuHoi();


            HoaDon hd = new HoaDon("temp", getData.nv, kh, LocalDateTime.now(), new KhuyenMai(null), false);
            Ve veMoi = new Ve(ve.getMaVe(), kh, ctlt, new LoaiVe_DAO().getLoaiVeTheoMa(lv.getMaLoaiVe()), tenHK, soCCCD, dob, "DaDoi", khuHoiMoi);
            if (hd_dao.createTempInvoice(hd)) {
                //get hóa đơn vừa tạo
                getData.hd = hd_dao.getHoaDonVuaTao();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Tạo hóa đơn thất bại");
                alert.show();
                return;
            }

            getData.kh = new KhachHang_DAO().getKhachHangTheoMaKH(kh.getMaKH());
            getData.dsve = new ArrayList<>(List.of(veMoi));
            FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("hd_doitrave.fxml"));
            try {
                AnchorPane pane = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(pane);
                stage.setScene(scene);
                stage.setTitle("Đổi vé");
                stage.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
                stage.centerOnScreen();
                stage.show();
                stage.setOnCloseRequest(e1 -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Xác nhận");
                    alert.setHeaderText(null);
                    alert.setContentText("Xác nhận thoát?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        HoaDon hoaDon = hd_dao.getHoaDonVuaTao();
                        if (!hoaDon.isTrangThai() && hoaDon.getTongTien() == 0) {
                            hd_dao.delete(hoaDon);
                        }
                        stage.close();
                        lamMoi();
                    }
                });

                Button btnBack = (Button) pane.lookup("#btnBackBanVe");
                btnBack.setOnMouseClicked(e1 -> {
                    HoaDon hoaDon = hd_dao.getHoaDonVuaTao();
                    hd_dao.delete(hoaDon);
                    getData.hd = null;
                    getData.dsctlt = null;
                    getData.dsctltkh = null;
                    getData.kh = null;
                    getData.dsve = null;
                    getData.dscthd = null;
                    stage.close();
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

    }

    public void renderTableVe(ArrayList<Ve> listVe) {
        // TODO
        listVe.removeIf(ve -> ve.getTinhTrangVe().equalsIgnoreCase("DaDoi") || ve.getTinhTrangVe().equalsIgnoreCase("DaHuy"));
        ObservableList<Ve> data = FXCollections.observableArrayList(listVe);
        tbl_thongTinVe.setItems(data);
        col_maVe.setCellValueFactory(new PropertyValueFactory<>("maVe"));
        col_maKH.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKhachHang().getMaKH()));
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
        hints.put(DecodeHintType.POSSIBLE_FORMATS, List.of(
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
        chosedId = null;
        ctlt = null;
        btn_xacNhan.setDisable(true);
        btnQuetMaVe.setDisable(true);
        btn_doiVe.setDisable(true);
        btnTraCuuCT.setDisable(true);
        dp_ngayKhoiHanh.setValue(null);
        txt_search.clear();
        cb_search.setValue(null);
        cb_search.setPromptText("Tìm theo");
        tbl_thongTinVe.getItems().clear();
        paneTau.getChildren().clear();
        grTrain.getChildren().clear();
        lblToa.setText("");
        lblGia.setText("Giá: ");
        label_thongBao.setText("");
        paneToa.getChildren().clear();
        cb_gaDi.setValue(null);
        cb_gaDen.setValue(null);
    }

    private void showTauTheoLT(ArrayList<LichTrinh> list) {
        paneTau.getChildren().clear();

        for (LichTrinh lt : list) {
            FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("tau.fxml"));
            try {
                Pane pTau = loader.load();
                Label lblSoHieuTau = (Label) pTau.lookup("#lblSoHieuTau");
                Label lblTGKH = (Label) pTau.lookup("#lblTGKH");
                Label lblTGDen = (Label) pTau.lookup("#lblTGDen");
                ImageView imgTau = (ImageView) pTau.lookup("#imgTau");
                imgTau.setId(lt.getMaLichTrinh());
                lblSoHieuTau.setText(lt.getChuyenTau().getSoHieutau());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
                lblTGKH.setText(lt.getThoiGianKhoiHanh().format(formatter));
                lblTGDen.setText(lt.getThoiGianDuKienDen().format(formatter));
                imgTau.setOnMouseClicked(et -> {
                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setContrast(0);
                    imgTau.setEffect(colorAdjust);
                    for (LichTrinh l : list) {
                        if (!l.getMaLichTrinh().equals(imgTau.getId())) {
                            ImageView imageView = (ImageView) paneTau.lookup("#" + l.getMaLichTrinh());
                            ColorAdjust ca = new ColorAdjust();
                            ca.setContrast(-1.0);
                            imageView.setEffect(ca);
                        }
                    }
                    showToaTheoLT(lt);
                });
                paneTau.getChildren().add(pTau);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Pane pane = new Pane();
            pane.setPrefHeight(10);
            pane.setPrefWidth(10);
            paneTau.getChildren().add(pane);
        }
        if (!list.isEmpty()) {
            ImageView imageView = (ImageView) paneTau.lookup("#" + list.getFirst().getMaLichTrinh());
            imageView.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
        }
    }

    private void showToaTheoLT(LichTrinh lt) {
        chosedId = null;
        grTrain.getChildren().clear();
        ArrayList<Toa> dstoa = toa_dao.getAllToaTheoChuyenTau(lt.getChuyenTau().getSoHieutau());
        dstoa.sort(Comparator.comparing(Toa::getSoSTToa).reversed());
        GridPane gridPane = new GridPane();
        paneToa.setCenter(gridPane);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        for (Toa toa : dstoa) {
            Pane paneImg = new Pane();
            paneImg.setMaxHeight(18);
            paneImg.setPrefWidth(50);
            paneImg.setStyle("-fx-background-color: skyblue;-fx-background-radius: 5;");
            ImageView imageView = new ImageView("file:src/main/resources/img/trainCar.png");
            imageView.setId(toa.getMaToa());
            imageView.setFitHeight(25);
            imageView.setFitWidth(50);

            imageView.setOnMouseEntered(e -> {
                if(!paneImg.getStyle().contains("lightgreen")) {
                    paneImg.setStyle("-fx-background-color: #5098ff;-fx-background-radius: 5;");
                }
            });

            imageView.setOnMouseExited(e -> {
                if(!paneImg.getStyle().contains("lightgreen")) {
                    paneImg.setStyle("-fx-background-color: skyblue;-fx-background-radius: 5;");
                }
            });

            imageView.setOnMouseClicked(e -> {
                lblToa.setText("Toa " + toa.getSoSTToa() + ": " + ltoa_dao.getLoaiToaTheoMa(toa.getLoaiToa().getMaLoaiToa()).getTenLoaiToa());
                paneImg.setStyle("-fx-background-color: lightgreen;-fx-background-radius: 5;");
                //đổi màu các imageView còn lại
                if (chosedId != null) {
                    ImageView imageView1 = (ImageView) grTrain.lookup("#" + chosedId);
                    imageView1.getParent().setStyle("-fx-background-color: skyblue;-fx-background-radius: 5;");
                }
                chosedId = imageView.getId();
                gridPane.getChildren().clear();
                ArrayList<ChoNgoi> dscn = cn_dao.getDsChoNgoiTheoToa(toa.getMaToa());
                if (toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("NC")){


                    int seatNumbers[][] = {
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
                    gridPane.add(pane, 9, 0, 1, 2);
                    gridPane.add(pane1, 9, 4, 1, 2);
                    // Tạo giao diện ghế
                    for (int row = 0; row < 4; row++) {
                        for (int col = 0; col < 16; col++) {
                            int seatNumber = seatNumbers[row][col];
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
                                seatButton.setStyle(styleGheDaDat);
                            } else {
                                seatButton.setStyle(styleGheThuong);
                            }
                            if (col > 7) {
                                seatButton.setStyle(seatButton.getStyle() + styleGhe2);
                            } else {
                                seatButton.setStyle(seatButton.getStyle() + styleGhe1);
                            }
                            // Với row = 2 thêm lối đi ở giữa (tăng thêm 1 để tạo khoảng trống)
                            gridPane.add(seatButton, col + (col >= 8 ? 2 : 0), row > 1 ? row + 2 : row);
                            int finalCol = col;
                            if (ctlt != null && ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId())) {
                                seatButton.setStyle(styleGheDaChon + (finalCol > 7 ? styleGhe2 : styleGhe1));
                            }
                            seatButton.setOnMouseClicked(event -> {
                                if (seatButton.getStyle().contains("green")) {
                                    seatButton.setStyle(styleGheThuong + (finalCol > 7 ? styleGhe2 : styleGhe1));
                                    ctlt = null;
                                    btn_doiVe.setDisable(true);
                                } else if (seatButton.getStyle().contains("red")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được đặt");
                                    alert.show();
                                } else {
                                    if (ctlt != null) {
                                        Button btn = (Button) gridPane.lookup("#" + ctlt.getChoNgoi().getMaChoNgoi());
                                        if (btn != null) {
                                            btn.setStyle(styleGheThuong + (btn.getId().contains("2") ? styleGhe2 : styleGhe1));
                                        }
                                    }
                                    seatButton.setStyle(styleGheDaChon + (finalCol > 7 ? styleGhe2 : styleGhe1));
                                    ctlt = ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), seatButton.getId());
                                    btn_doiVe.setDisable(false);
                                }
                            });
                        }
                    }

                } else if (toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("NM")) {

                    int seatNumbers[][] = {
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
                    gridPane.add(pane, 9, 0, 1, 2);
                    gridPane.add(pane1, 9, 4, 1, 2);
                    // Tạo giao diện ghế
                    for (int row = 0; row < 4; row++) {
                        for (int col = 0; col < 16; col++) {
                            int seatNumber = seatNumbers[row][col];
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
                                seatButton.setStyle(styleGheDaDat);
                            } else {
                                seatButton.setStyle(styleGheThuong);
                            }
                            if (col > 7) {
                                seatButton.setStyle(seatButton.getStyle() + styleGhe2);
                            } else {
                                seatButton.setStyle(seatButton.getStyle() + styleGhe1);
                            }

                            // Với row = 2 thêm lối đi ở giữa (tăng thêm 1 để tạo khoảng trống)
                            gridPane.add(seatButton, col + (col >= 8 ? 2 : 0), row > 1 ? row + 2 : row);

                            int finalCol = col;

                            if (ctlt != null && ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId())) {
                                seatButton.setStyle(styleGheDaChon + (finalCol > 7 ? styleGhe2 : styleGhe1));
                            }

                            seatButton.setOnMouseClicked(event -> {
                                if (seatButton.getStyle().contains("green")) {
                                    seatButton.setStyle(styleGheThuong + (finalCol > 7 ? styleGhe2 : styleGhe1));
                                    ctlt = null;
                                    btn_doiVe.setDisable(true);
                                } else if (seatButton.getStyle().contains("red")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được đặt");
                                    alert.show();
                                } else {
                                    if (ctlt != null) {
                                        Button btn = (Button) gridPane.lookup("#" + ctlt.getChoNgoi().getMaChoNgoi());
                                        if (btn != null) {
                                            btn.setStyle(styleGheThuong + (btn.getId().contains("2") ? styleGhe2 : styleGhe1));
                                        }
                                    }
                                    seatButton.setStyle(styleGheDaChon + (finalCol > 7 ? styleGhe2 : styleGhe1));
                                    ctlt = ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), seatButton.getId());
                                    btn_doiVe.setDisable(false);
                                }
                            });
                        }
                    }
                } else if (toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("GNK4")) {
                    int[][] seatNumbers2 = {
                            {3, 4, 7, 8, 11, 12, 15, 16, 19, 20, 23, 24, 27, 28},
                            {1, 2, 5, 6, 9, 10, 13, 14, 17, 18, 21, 22, 25, 26}
                    };

                    // Ghế đã được đặt (màu đỏ) và ghế còn trống (màu trắng hoặc xanh)

                    gridPane.add(new Label("T2"), 0, 1);
                    gridPane.add(new Label("T1"), 0, 2);
                    gridPane.add(new Label("  Khoang 1"), 1, 0, 3, 1);
                    gridPane.add(new Label("  Khoang 2"), 4, 0, 3, 1);
                    gridPane.add(new Label("  Khoang 3"), 7, 0, 3, 1);
                    gridPane.add(new Label("  Khoang 4"), 10, 0, 3, 1);
                    gridPane.add(new Label("  Khoang 5"), 13, 0, 3, 1);
                    gridPane.add(new Label("  Khoang 6"), 16, 0, 3, 1);
                    gridPane.add(new Label("  Khoang 7"), 19, 0, 3, 1);
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
                                seatButton.setStyle(styleGiuongDaDat);
                            } else {
                                seatButton.setStyle(styleGiuong);
                            }

                            // Thêm nút vào GridPane (với col/2 để tạo khoảng trống giữa các khoang)
                            gridPane.add(seatButton, col + (col / 2) + 1, row + 1);

                            if (ctlt != null && ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId())) {
                                seatButton.setStyle(styleGiuongDaChon);
                            }
                            seatButton.setOnMouseClicked(event -> {
                                if (seatButton.getStyle().contains("green")) {
                                    seatButton.setStyle(styleGiuong);
                                    ctlt = null;
                                    btn_doiVe.setDisable(true);
                                } else if (seatButton.getStyle().contains("red")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được đặt");
                                    alert.show();
                                } else {
                                    if (ctlt != null) {
                                        Button btn = (Button) gridPane.lookup("#" + ctlt.getChoNgoi().getMaChoNgoi());
                                        if (btn != null) {
                                            btn.setStyle(styleGiuong);
                                        }
                                    }
                                    seatButton.setStyle(styleGiuongDaChon);
                                    ctlt = ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), seatButton.getId());
                                    btn_doiVe.setDisable(false);
                                }
                            });
                        }
                    }
                } else if (toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("GNK6")) {

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
                                seatButton.setStyle(styleGiuongDaDat);
                            } else {
                                seatButton.setStyle(styleGiuong);
                            }

                            // Thêm nút vào GridPane (với col/2 để tạo khoảng trống giữa các khoang)
                            gridPane.add(seatButton, col + (col / 2) + 1, row + 1);

                            if (ctlt != null && ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId())) {
                                seatButton.setStyle(styleGiuongDaChon);
                            }
                            seatButton.setOnMouseClicked(event -> {
                                if (seatButton.getStyle().contains("green")) {
                                    seatButton.setStyle(styleGiuong);
                                    ctlt = null;
                                    btn_doiVe.setDisable(true);
                                } else if (seatButton.getStyle().contains("red")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được đặt");
                                    alert.show();
                                } else {
                                    if (ctlt != null) {
                                        Button btn = (Button) gridPane.lookup("#" + ctlt.getChoNgoi().getMaChoNgoi());
                                        if (btn != null) {
                                            btn.setStyle(styleGiuong);
                                        }
                                    }
                                    seatButton.setStyle(styleGiuongDaChon);
                                    ctlt = ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), seatButton.getId());
                                    btn_doiVe.setDisable(false);
                                }
                            });
                        }
                    }
                } else if (toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("TVIP")) {
                    int[][] seatNumbers2 = {
                            {2, 4, 6, 8, 10, 12, 14},
                            {1, 3, 5, 7, 9, 11, 13}
                    };

                    // Ghế đã được đặt (màu đỏ) và ghế còn trống (màu trắng hoặc xanh)

                    gridPane.add(new Label("  Khoang 1"), 1, 0);
                    gridPane.add(new Label("  Khoang 2"), 2, 0);
                    gridPane.add(new Label("  Khoang 3"), 3, 0);
                    gridPane.add(new Label("  Khoang 4"), 4, 0);
                    gridPane.add(new Label("  Khoang 5"), 5, 0);
                    gridPane.add(new Label("  Khoang 6"), 6, 0);
                    gridPane.add(new Label("  Khoang 7"), 7, 0);
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
                                seatButton.setStyle(styleGiuongDaDat);
                            } else {
                                seatButton.setStyle(styleGiuong);
                            }
                            gridPane.add(seatButton, col + 1, row + 1);

                            if (ctlt != null && ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId())) {
                                seatButton.setStyle(styleGiuongDaChon);
                            }

                            seatButton.setOnMouseClicked(event -> {
                                if (seatButton.getStyle().contains("green")) {
                                    seatButton.setStyle(styleGiuong);
                                    ctlt = null;
                                    btn_doiVe.setDisable(true);
                                } else if (seatButton.getStyle().contains("red")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được đặt");
                                    alert.show();
                                } else {
                                    if (ctlt != null) {
                                        Button btn = (Button) gridPane.lookup("#" + ctlt.getChoNgoi().getMaChoNgoi());
                                        if (btn != null) {
                                            btn.setStyle(styleGiuong);
                                        }
                                    }
                                    seatButton.setStyle(styleGiuongDaChon);
                                    ctlt = ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), seatButton.getId());
                                    btn_doiVe.setDisable(false);
                                }
                            });
                        }
                    }
                }
            });
            imageView.setCursor(Cursor.HAND);
            paneImg.getChildren().add(imageView);
            Label lbl = new Label(toa.getSoSTToa() + "");
            lbl.setLayoutX(20);
            lbl.setLayoutY(30);
            lbl.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 10));
            paneImg.getChildren().add(lbl);

            grTrain.getChildren().add(paneImg);
        }
        ImageView imageView1 = new ImageView("file:src/main/resources/img/train.png");
        imageView1.setFitHeight(25);
        imageView1.setFitWidth(50);
        Label lbl = new Label(lt.getChuyenTau().getSoHieutau());
        lbl.setLayoutX(15);
        lbl.setLayoutY(30);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 10));
        Pane paneImg = new Pane();
        paneImg.getChildren().add(imageView1);
        paneImg.getChildren().add(lbl);
        grTrain.getChildren().add(paneImg);
        //get toa có stt1
        ImageView imageView = (ImageView) grTrain.lookup("#" + dstoa.stream().filter(toa -> toa.getSoSTToa() == 1).findFirst().get().getMaToa());
        imageView.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
    }
}
