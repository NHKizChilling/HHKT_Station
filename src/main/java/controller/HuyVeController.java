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
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HuyVeController implements Initializable {

    // Box tìm kiếm
    @FXML
    private ComboBox<String> cb_search;

    @FXML
    private JFXButton btn_lamMoi;

    @FXML
    private TextField txt_search;

    @FXML
    private JFXButton btn_search;

    @FXML
    private JFXButton btnQuetMaVe;

    // Table thông tin vé
    @FXML
    private TableView<Ve> tbl_thongTinVe;

    @FXML
    private TableColumn<Ve, String> col_maVe;

    @FXML
    private TableColumn<Ve, String> col_thongTinHK;

    @FXML
    private TableColumn<Ve, String> col_tinhTrangVe;

    @FXML
    private TableColumn<Ve, String> col_thongTinVe;

    @FXML
    private TableColumn<Ve, String> col_giaVe;

    @FXML
    private TableColumn<Ve, CheckBox> col_chonVe;

    // Box Thông tin người đặt vé
    @FXML
    private TextField txt_tenNguoiDat;

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_sdt;

    @FXML
    private TextField txt_cccd;

    @FXML
    private JFXButton btn_yeuCau;

    @FXML
    private Label lbl_thongBao;

    // Box trả vé
    @FXML
    private TextField txt_tongVeTra;

    @FXML
    private TextField txt_tongTienVe;

    @FXML
    private TextField txt_tongLePhi;

    @FXML
    private TextField txt_tongTienTra;

    @FXML
    private JFXButton btn_xacNhan;

    @FXML
    private Label lbl_thongBao2;

    // Gọi DAO
    private Ve_DAO ve_dao;
    private KhachHang_DAO hanhKhach_dao;
    private CT_HoaDon_DAO ct_hoaDon_dao;
    private HoaDon_DAO hoaDon_dao;
    private LichTrinh_DAO lichTrinh_dao;
    private LoaiVe_DAO loaiVe_dao;
    private HoaDon hoaDon;

    private NumberFormat currencyVN = NumberFormat.getCurrencyInstance(Locale.of("vi", "VN"));

    private final ArrayList<Ve> selectedVe = new ArrayList<>();
    private final HashMap<String, Double> mapLePhi = new HashMap<>(); // Lưu lệ phí của từng vé
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initDAO();


        cb_search.getItems().addAll("Mã hóa đơn", "Mã vé");
        cb_search.setValue("Mã vé");

        cb_search.setOnAction(e -> {
            if (cb_search.getValue().equals("Mã hóa đơn")) {
                txt_search.setPromptText("Nhập mã hóa đơn");
                btnQuetMaVe.setDisable(true);
            } else {
                txt_search.setPromptText("Nhập mã vé");
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
            String key = txt_search.getText();
            if (key.isBlank()) {
                lbl_thongBao.setText("Vui lòng nhập mã vé hoặc mã hóa đơn");
                return;
            }
            ArrayList<Ve> listVe = new ArrayList<>();
            if (cb_search.getValue().equals("Mã vé")) {
                Ve ve = ve_dao.getVeTheoID(key);
                ChiTietHoaDon ctHoaDon = ct_hoaDon_dao.getCT_HoaDonTheoMaVe(key);
                if (ctHoaDon == null) {
                    txt_search.clear();
                    txt_search.setPromptText("Mã vé không tồn tại");
                    txt_search.requestFocus();
                    return;
                }
                hoaDon = hoaDon_dao.getHoaDonTheoMa(ctHoaDon.getHoaDon().getMaHoaDon());
                if (ve != null) {
                    listVe.add(ve);
                }
            } else {
                hoaDon = hoaDon_dao.getHoaDonTheoMa(key);
                if (hoaDon != null) {
                    ArrayList<ChiTietHoaDon> listCTHD = ct_hoaDon_dao.getCT_HoaDon(hoaDon.getMaHoaDon());
                    for (ChiTietHoaDon ct : listCTHD) {
                        Ve ve = ve_dao.getVeTheoID(ct.getVe().getMaVe());
                        listVe.add(ve);
                    }
                }
            }
            if (listVe.isEmpty()) {
                txt_search.clear();
                txt_search.promptTextProperty().setValue("Mã hóa đơn không tồn tại");
                txt_search.requestFocus();
            } else {
                renderTable(listVe);
                lbl_thongBao.setText("");
                KhachHang kh = hanhKhach_dao.getKhachHangTheoMaKH(hoaDon.getKhachHang().getMaKH());
                txt_tenNguoiDat.setText(kh.getTenKH());
                txt_email.setText(kh.getEmail());
                txt_sdt.setText(kh.getSdt());
                txt_cccd.setText(kh.getSoCCCD());
            }
        });

        btn_lamMoi.setOnAction(e -> {
            lamMoi();
        });

        tbl_thongTinVe.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        col_maVe.setCellValueFactory(new PropertyValueFactory<>("maVe"));

        col_thongTinHK.setCellValueFactory(p -> {
            String tenHK = p.getValue().getTenHanhKhach();
            String cccd = p.getValue().getSoCCCD();
            LocalDate ngaySinh = p.getValue().getNgaySinh();
            String s;
            s = Objects.requireNonNullElse(tenHK, "");
            if (cccd != null) {
                s += "\n" + cccd;
            }
            if (ngaySinh != null) {
                s += "\n" + DateTimeFormatter.ofPattern("dd - MM - yyyy").format(ngaySinh);
            }

            return new SimpleStringProperty(s);
        });

        col_thongTinVe.setCellValueFactory(p -> {
            Ve ve = ve_dao.getVeTheoID(p.getValue().getMaVe());
            LoaiVe lv = loaiVe_dao.getLoaiVeTheoMa(ve.getLoaiVe().getMaLoaiVe());
            LichTrinh lt = new LichTrinh_DAO().getLichTrinhTheoID(ve.getCtlt().getLichTrinh().getMaLichTrinh());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new SimpleStringProperty(lt.getChuyenTau().getSoHieutau() + " - " + lt.getGaDi().getMaGa() + " - " + lt.getGaDen().getMaGa() + "\n" +
                    lt.getThoiGianKhoiHanh().format(formatter) + "\n" +
                    lv.getTenLoaiVe());
        });

        col_tinhTrangVe.setCellValueFactory(new PropertyValueFactory<>("tinhTrangVe"));

        col_giaVe.setCellValueFactory(p -> {
            ChiTietHoaDon ctHoaDon = ct_hoaDon_dao.getCT_HoaDonTheoMaVe(p.getValue().getMaVe());
            return new SimpleStringProperty(String.valueOf(ctHoaDon.getGiaVe()));
        });

        col_chonVe.setCellValueFactory(cellData -> {
            CheckBox checkBox = new CheckBox();
            Ve ve = cellData.getValue();
            checkBox.setOnAction(e -> {
                if (checkBox.isSelected()) {

                    if (ve.getTinhTrangVe().equals("DaHuy")) {
                        checkBox.setSelected(false);
                        tbl_thongTinVe.getSelectionModel().clearSelection(tbl_thongTinVe.getItems().indexOf(ve));
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setTitle("Hủy vé");
                        alert.setContentText("Vé đã được hủy");
                        alert.showAndWait();
                    } else {
                        tbl_thongTinVe.getSelectionModel().select(tbl_thongTinVe.getItems().indexOf(ve));
                        selectedVe.add(ve);
                    }
                } else {
                    tbl_thongTinVe.getSelectionModel().clearSelection(tbl_thongTinVe.getItems().indexOf(tbl_thongTinVe.getSelectionModel().getSelectedItem()));
                    selectedVe.remove(ve);
                }
            });
            return new SimpleObjectProperty<>(checkBox);
        });

        btn_yeuCau.setOnAction(e -> {
            if (selectedVe.isEmpty()) { // Nếu không chọn vé nào
                lbl_thongBao.setText("Vui lòng chọn vé cần hủy");
                return;
            }

            ArrayList<Ve> dsVeHuy = new ArrayList<>(selectedVe); // Danh sách vé cần hủy để lọc ra các vé đã đổi
            ArrayList<Ve> dsVeDaDoi = new ArrayList<>(); // Danh sách vé đã đổi

            // Lấy thời gian khởi hành của lịch trình
            LichTrinh lt = lichTrinh_dao.getLichTrinhTheoID(selectedVe.getFirst().getCtlt().getLichTrinh().getMaLichTrinh());
            LocalDateTime thoiGianKhoiHanh = lt.getThoiGianKhoiHanh();

            // Kiểm tra điều kiện hủy vé
            for (Iterator<Ve> iterator = dsVeHuy.iterator(); iterator.hasNext();) { // Duyệt qua danh sách vé cần hủy
                Ve ve = iterator.next(); // Lấy vé
                if (ve.getTinhTrangVe().equals("DaHuy")) { // Nếu vé đã hủy
                    lbl_thongBao.setText("Vé đã được hủy");
                    return;
                }
                // Nếu số vé > 1 => vé tập thể
                // Nếu thời gian khởi hành trừ thời gian hiện tại < 24h
                if ((selectedVe.size() > 1 && LocalDateTime.now().plusHours(24).isAfter(thoiGianKhoiHanh)) ||
                        // Nếu số vé = 1 => vé cá nhân
                        // Nếu thời gian khởi hành trừ thời gian hiện tại < 4h
                        (selectedVe.size() == 1 && LocalDateTime.now().plusHours(4).isAfter(thoiGianKhoiHanh))) {
                    lbl_thongBao.setText("Vé đã hết thời gian hủy");
                    return;
                }
                // Nếu vé đã đổi
                if (ve.getTinhTrangVe().equals("DaDoi")) {
                    dsVeDaDoi.add(ve); // Thêm vé vào danh sách vé đã đổi
                    iterator.remove(); // Xóa vé khỏi danh sách vé cần hủy
                }
            }
            // Xác định phần trăm hoàn trả vé
            // Nếu số vé > 1 và thời gian khởi hành trừ thời gian hiện tại trong khoảng 24h-72h thì phần trăm hoàn trả = 30%
            // Nếu trên 72h thì phần trăm hoàn trả = 20%
            // Nếu số vé = 1 và thời gian khởi hành trừ thời gian hiện tại trong khoảng 4h-24h thì phần trăm hoàn trả = 30%
            // Nếu dưới 4h thì phần trăm hoàn trả = 20%
            double phanTram = (selectedVe.size() > 1 && LocalDateTime.now().plusHours(24).isBefore(thoiGianKhoiHanh) &&
                    LocalDateTime.now().plusHours(72).isAfter(thoiGianKhoiHanh)) ||
                    (selectedVe.size() == 1 && LocalDateTime.now().plusHours(4).isBefore(thoiGianKhoiHanh) &&
                            LocalDateTime.now().plusHours(24).isAfter(thoiGianKhoiHanh)) ? 0.3 : 0.2;

            lbl_thongBao.setText("");
            double tongTienVe = 0;
            double tongLePhi = 0;

            // Tính tổng tiền vé và lệ phí
            for (Ve ve : dsVeHuy) { // Duyệt qua danh sách vé cần hủy đã lọc các vé đã đổi ra
                ChiTietHoaDon ctHoaDon = ct_hoaDon_dao.getCT_HoaDonTheoMaVe(ve.getMaVe());
                tongTienVe += Math.round(ctHoaDon.getGiaVe() / 1000) * 1000;
                double lePhi = Math.round((ctHoaDon.getGiaVe() * phanTram) / 1000) * 1000;
                tongLePhi += lePhi;
                mapLePhi.put(ve.getMaVe(), lePhi);
            }

            for (Ve ve : dsVeDaDoi) {
                ChiTietHoaDon ctHoaDon = ct_hoaDon_dao.getCT_HoaDonTheoMaVe(ve.getMaVe());
                tongTienVe += Math.round(ctHoaDon.getGiaVe() / 1000) * 1000;
                double lePhi = Math.round((ctHoaDon.getGiaVe() * 0.3) / 1000.0) * 1000;
                tongLePhi += lePhi;
                mapLePhi.put(ve.getMaVe(), lePhi);
            }

            dsVeHuy.addAll(dsVeDaDoi);

            txt_tongVeTra.setText(String.valueOf(dsVeHuy.size()));
            txt_tongTienVe.setText(currencyVN.format(tongTienVe));
            txt_tongLePhi.setText(currencyVN.format(tongLePhi));
            txt_tongTienTra.setText(currencyVN.format(tongTienVe - tongLePhi));
            btn_xacNhan.setDisable(false);
        });

        btn_xacNhan.setOnAction(e -> {
            if (selectedVe.isEmpty()) {
                lbl_thongBao.setText("Vui lòng chọn vé cần hủy");
                return;
            }
            getData.dsve = selectedVe;
            getData.mapLePhi = mapLePhi;

            lbl_thongBao.setText("");
            lbl_thongBao2.setText("Hủy vé thành công");
            txt_search.clear();
            tbl_thongTinVe.getItems().clear();
            txt_cccd.clear();
            txt_email.clear();
            txt_sdt.clear();
            txt_tenNguoiDat.clear();
            txt_tongLePhi.clear();
            txt_tongTienTra.clear();
            txt_tongTienVe.clear();
            txt_tongVeTra.clear();
            btn_xacNhan.setDisable(true);

            try {
                BorderPane hdHuyVePane = FXMLLoader.load(Objects.requireNonNull(TrangChu_GUI.class.getResource("hd-huy-ve.fxml")));                Stage hdHuyVeStage = new Stage();
                hdHuyVeStage.setTitle("Hóa đơn hủy vé");
                hdHuyVeStage.setScene(new Scene(hdHuyVePane));
                hdHuyVeStage.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
                hdHuyVeStage.show();
                hdHuyVeStage.setOnCloseRequest(e1 -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Xác nhận");
                    alert.setHeaderText(null);
                    alert.setContentText("Xác nhận thoát?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        HoaDon hoaDon = hoaDon_dao.getHoaDonVuaTao();
                        if (!hoaDon.isTrangThai() && hoaDon.getTongTien() == 0) {
                            hoaDon_dao.delete(hoaDon);
                        }
                        hdHuyVeStage.close();
                        lamMoi();
                    } else {
                        e1.consume();
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

    }

    public void renderTable(ArrayList<Ve> listVe) {
        ObservableList<Ve> list = FXCollections.observableArrayList(listVe);
        tbl_thongTinVe.setItems(list);
        col_maVe.setCellValueFactory(new PropertyValueFactory<>("maVe"));

        // col thông tin hành khách chứa TenHK, cccd, sdt
        col_thongTinHK.setCellValueFactory(p -> {
            String tenHK = p.getValue().getTenHanhKhach();
            String cccd = p.getValue().getSoCCCD();
            String ngaySinh = p.getValue().getNgaySinh() != null ? p.getValue().getNgaySinh().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
            String s;
            s = Objects.requireNonNullElse(tenHK, "");
            if (cccd != null) {
                s += "\nCCCD/CMND: " + cccd;
            }
            if (ngaySinh != null) {
                s += "\nNgày sinh: " + ngaySinh;
            }
            return new SimpleStringProperty(s);
        });

        col_thongTinVe.setCellValueFactory(p -> {
            Ve ve = ve_dao.getVeTheoID(p.getValue().getMaVe());
            LoaiVe lv = loaiVe_dao.getLoaiVeTheoMa(ve.getLoaiVe().getMaLoaiVe());
            LichTrinh lt = new LichTrinh_DAO().getLichTrinhTheoID(ve.getCtlt().getLichTrinh().getMaLichTrinh());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new SimpleStringProperty(new Ga_DAO().getGaTheoMaGa(lt.getGaDi().getMaGa()).getTenGa() + " - " +
                    new Ga_DAO().getGaTheoMaGa(lt.getGaDen().getMaGa()).getTenGa() + " \n" +
                    "Giờ khởi hành: \n" + lt.getThoiGianKhoiHanh().format(formatter) + " \n" +
                    lv.getTenLoaiVe());
        });

        Map<String, String> map = new HashMap<>();
        map.put("DaHuy", "Đã hủy");
        map.put("DaDoi", "Đã đổi");
        map.put("DaBan", "Đã bán");

        col_tinhTrangVe.setCellValueFactory(p -> new SimpleStringProperty(map.get(p.getValue().getTinhTrangVe())));

        col_giaVe.setCellValueFactory(p -> {
            ChiTietHoaDon ctHoaDon = ct_hoaDon_dao.getCT_HoaDonTheoMaVe(p.getValue().getMaVe());
            return new SimpleStringProperty(currencyVN.format(ctHoaDon.getGiaVe()));
        });

        col_chonVe.setCellValueFactory(cellData -> {
            CheckBox checkBox = new CheckBox();
            Ve ve = cellData.getValue();
            checkBox.setOnAction(e -> {
                if (checkBox.isSelected()) {
                    if (ve.getTinhTrangVe().equals("DaHuy")) {
                        checkBox.setSelected(false);
                        tbl_thongTinVe.getSelectionModel().clearSelection(tbl_thongTinVe.getItems().indexOf(ve));
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setTitle("Hủy vé");
                        alert.setContentText("Vé đã được hủy");
                        alert.showAndWait();
                    } else {
                        tbl_thongTinVe.getSelectionModel().select(tbl_thongTinVe.getItems().indexOf(ve));
                        selectedVe.add(ve);
                    }
                } else {
                    tbl_thongTinVe.getSelectionModel().clearSelection(tbl_thongTinVe.getItems().indexOf(tbl_thongTinVe.getSelectionModel().getSelectedItem()));
                    selectedVe.remove(ve);
                }
            });
            return new SimpleObjectProperty<>(checkBox);
        });
    }



    // Hàm giải mã mã vạch từ ảnh
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

    private void initDAO() {
        ve_dao = new Ve_DAO();
        hanhKhach_dao = new KhachHang_DAO();
        loaiVe_dao = new LoaiVe_DAO();
        ct_hoaDon_dao = new CT_HoaDon_DAO();
        hoaDon_dao = new HoaDon_DAO();
        lichTrinh_dao = new LichTrinh_DAO();
    }

    private void lamMoi() {
        txt_search.clear();
        tbl_thongTinVe.getItems().clear();
        txt_cccd.clear();
        txt_email.clear();
        txt_sdt.clear();
        txt_tenNguoiDat.clear();
        txt_tongLePhi.clear();
        txt_tongTienTra.clear();
        txt_tongTienVe.clear();
        txt_tongVeTra.clear();
        lbl_thongBao.setText("");
        selectedVe.clear();
        mapLePhi.clear();

        getData.dsve = null;
        getData.mapLePhi = null;
    }
}
