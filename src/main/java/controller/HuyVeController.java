package controller;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import dao.*;
import entity.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HuyVeController implements Initializable {

    @FXML
    private ComboBox<String> cb_search;

    @FXML
    private TextField txt_search;

    @FXML
    private Button btn_search;

    @FXML
    private Button btnQuetMaVe;

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
    private Button btn_huyVe;

    @FXML
    private TextField txt_maHK;

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_sdt;

    @FXML
    private TextField txt_cccd;

    @FXML
    private TextField txt_tenHK;

    @FXML
    private TextField txt_ngayLap;

    @FXML
    private TextField txt_gaDi;

    @FXML
    private TextField txt_gaDen;

    @FXML
    private TextField txt_giaVe;

    @FXML
    private Button btn_lamMoi;

    @FXML
    private TextField txt_chietKhau;

    @FXML
    private TextField txt_hoanTien;

    @FXML
    private Label label_thongBao;

    private Ve_DAO ve_dao;
    private HanhKhach_DAO hanhKhach_dao;
    private CT_HoaDon_DAO ct_hoaDon_dao;
    private HoaDon_DAO hoaDon_dao;
    private LichTrinh_DAO lichTrinh_dao;
    private boolean isHuy = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ve_dao = new Ve_DAO();
        hanhKhach_dao = new HanhKhach_DAO();
        ct_hoaDon_dao = new CT_HoaDon_DAO();
        hoaDon_dao = new HoaDon_DAO();
        lichTrinh_dao = new LichTrinh_DAO();


        cb_search.getItems().addAll("Mã hành khách", "Mã vé", "Số điện thoại");

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
            String type = cb_search.getValue();
            if (type == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Vui lòng chọn loại tìm kiếm");
                alert.show();
                return;
            }
            if (search.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Vui lòng nhập thông tin tìm kiếm");
                alert.show();
                return;
            }
            if (type.equalsIgnoreCase("Mã vé")) {
                Ve ve = ve_dao.getVeTheoID(search);
                renderTable(new ArrayList<>(Arrays.asList(ve)));
                return;
            }
            ArrayList<Ve> listVe = ve_dao.getDSVeTheoMaHK(search);
            renderTable(listVe);
        });

        btn_lamMoi.setOnAction(e -> {
            txt_search.clear();
            cb_search.getSelectionModel().clearSelection();
            tbl_thongTinVe.getItems().clear();

            txt_maHK.clear();
            txt_email.clear();
            txt_sdt.clear();
            txt_cccd.clear();
            txt_tenHK.clear();
            txt_ngayLap.clear();
            txt_gaDi.clear();
            txt_gaDen.clear();
            txt_giaVe.clear();
            txt_chietKhau.clear();
            txt_hoanTien.clear();
            label_thongBao.setText("");
        });

        tbl_thongTinVe.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Ve ve = tbl_thongTinVe.getSelectionModel().getSelectedItem();
                String maHKTmp = ve.getHanhKhach().getMaHanhKhach();
                HanhKhach hk = hanhKhach_dao.getHanhKhachTheoMa(maHKTmp);
                txt_maHK.setText(hk.getMaHanhKhach());
                txt_email.setText(hk.getEmail());
                txt_sdt.setText(hk.getSdt());
                txt_cccd.setText(hk.getSoCCCD());
                txt_tenHK.setText(hk.getTenHanhKhach());

                //ngày lập hóa đơn được lấy bằng cách lấy mã hóa đơn từ chi tiết hóa đơn sau
                //sau đó lấy ngày lập hóa đơn từ hóa đơn
                ChiTietHoaDon ctHoaDon = ct_hoaDon_dao.getCT_HoaDonTheoMaVe(ve.getMaVe());
                HoaDon hd = hoaDon_dao.getHoaDonTheoMa(ctHoaDon.getHoaDon().getMaHoaDon());

                // lấy giá vé
                txt_giaVe.setText(String.valueOf(ctHoaDon.getGiaVe()));

                LocalDateTime ngayLap = hd.getNgayLapHoaDon();
                // ngày theo format dd/MM/yyyy HH:mm
                txt_ngayLap.setText(ngayLap.getDayOfMonth() + "/" + ngayLap.getMonthValue() + "/" + ngayLap.getYear() + " " + ngayLap.getHour() + ":" + ngayLap.getMinute());

                LichTrinh lt = lichTrinh_dao.getLichTrinhTheoID(ve.getCtlt().getLichTrinh().getMaLichTrinh());
                txt_gaDi.setText(new Ga_DAO().getGaTheoMaGa(lt.getGaDi().getMaGa()).getTenGa());
                txt_gaDen.setText(new Ga_DAO().getGaTheoMaGa(lt.getGaDen().getMaGa()).getTenGa());

                LocalDateTime tgKhoiHanh = lt.getThoiGianKhoiHanh();
                LocalDateTime now = LocalDateTime.now();
                // nếu trả trước 4h thì hủy được, chiết khấu là 20%
                if (tgKhoiHanh.isAfter(now) && tgKhoiHanh.minusHours(4).isBefore(now)) {
                    txt_chietKhau.setText("20%");
                    txt_hoanTien.setText(String.valueOf(ctHoaDon.getGiaVe() * 0.8));
                    isHuy = true;
                } else {
                    label_thongBao.setText("Vé không thể hủy");
                }
            }
        });

        btn_huyVe.setOnAction(e -> {
            if (isHuy) {
                Ve ve = tbl_thongTinVe.getSelectionModel().getSelectedItem();
                if (ve != null) {
                    boolean result = ve_dao.updateTinhTrangVe(ve.getMaVe(), "Đã hủy");
                    if (result) {
                        label_thongBao.setText("Hủy vé thành công");
                        isHuy = false;
                    } else {
                        label_thongBao.setText("Hủy vé thất bại");
                    }
                } else {
                    label_thongBao.setText("Vui lòng chọn vé cần hủy");
                }
            } else if (txt_maHK.getText().isEmpty()) {
                label_thongBao.setText("Vui lòng chọn vé cần hủy");
            } else {
                label_thongBao.setText("Vé không thể hủy");
            }
        });
    }

    public void renderTable(ArrayList<Ve> listVe) {
        // TODO
        ObservableList<Ve> data = FXCollections.observableArrayList(listVe);
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
}
