package controller;

import dao.*;
import entity.*;
<<<<<<< Updated upstream
=======
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
>>>>>>> Stashed changes
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
<<<<<<< Updated upstream
=======
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
>>>>>>> Stashed changes

import java.net.URL;
<<<<<<< Updated upstream
=======
import java.text.NumberFormat;
>>>>>>> Stashed changes
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HuyVeController implements Initializable {

    // Box tìm kiếm
    @FXML
    private ComboBox<String> cb_search;

    @FXML
    private Button btn_lamMoi;

    @FXML
    private TextField txt_search;

    @FXML
    private Button btn_search;

    @FXML
<<<<<<< Updated upstream
=======
    private Button btnQuetMaVe;

    // Table thông tin vé
    @FXML
>>>>>>> Stashed changes
    private TableView<Ve> tbl_thongTinVe;

    @FXML
    private TableColumn<Ve, String> col_maVe;

    @FXML
<<<<<<< Updated upstream
    private TableColumn<Ve, String> col_maHK;
=======
    private TableColumn<Ve, String> col_thongTinHK;
>>>>>>> Stashed changes

    @FXML
    private TableColumn<Ve, String> col_tinhTrangVe;

    @FXML
<<<<<<< Updated upstream
    private TableColumn<Ve, String> col_khuHoi;

    @FXML
    private TableColumn<Ve, String> col_maSoCho;

    @FXML
    private TableColumn<Ve, String> col_maLichTrinh;

    @FXML
    private TableColumn<Ve, LocalDate> col_dob;
=======
    private TableColumn<Ve, String> col_thongTinVe;

    @FXML
    private TableColumn<Ve, String> col_giaVe;
>>>>>>> Stashed changes

    @FXML
    private TableColumn<Ve, Boolean> col_chonVe;

<<<<<<< Updated upstream
    @FXML
    private TableColumn<Ve, String> col_cccd;

    @FXML
    private Button btn_huyVe;
=======
>>>>>>> Stashed changes

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
    private Button btn_yeuCau;

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
    private Button btn_xacNhan;

    @FXML
    private Label lbl_thongBao2;

    // Gọi DAO
    private Ve_DAO ve_dao;
    private HanhKhach_DAO hanhKhach_dao;
    private CT_HoaDon_DAO ct_hoaDon_dao;
    private HoaDon_DAO hoaDon_dao;
    private LichTrinh_DAO lichTrinh_dao;
    private CT_LichTrinh_DAO ct_lichTrinh_dao;
    private LoaiVe_DAO loaiVe_dao;
    private HoaDon hoaDon;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
<<<<<<< Updated upstream
        ve_dao = new Ve_DAO();
        hanhKhach_dao = new HanhKhach_DAO();
        ct_hoaDon_dao = new CT_HoaDon_DAO();
        hoaDon_dao = new HoaDon_DAO();
        lichTrinh_dao = new LichTrinh_DAO();
=======
>>>>>>> Stashed changes

        initDAO();

<<<<<<< Updated upstream
        cb_search.getItems().addAll("Mã hành khách", "Số CCCD", "Số điện thoại");

=======
        ArrayList<Ve> selectedVe = new ArrayList<>();

        cb_search.getItems().addAll("Mã hóa đơn", "Mã vé");
        cb_search.setValue("Mã vé");

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

>>>>>>> Stashed changes
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
                hoaDon = hoaDon_dao.getHoaDonTheoMa(ctHoaDon.getHoaDon().getMaHoaDon());
                if (ve != null) {
                    listVe.add(ve);
                }
            } else {
                hoaDon = hoaDon_dao.getHoaDonTheoMa(key);
                if (hoaDon != null) {
                    //listVe = ve_dao.getVeTheoID(hoaDon.getMaHoaDon());
                    ArrayList<ChiTietHoaDon> listCTHD = ct_hoaDon_dao.getCT_HoaDon(hoaDon.getMaHoaDon());
                    for (ChiTietHoaDon ct : listCTHD) {
                        Ve ve = ve_dao.getVeTheoID(ct.getVe().getMaVe());
                        listVe.add(ve);
                    }
                }
            }
<<<<<<< Updated upstream
            ArrayList<Ve> listVe = ve_dao.getDSVeTheoMaHK(search);
            renderTable(listVe);
=======
            if (listVe.isEmpty()) {
                lbl_thongBao.setText("Không tìm thấy vé hoặc hóa đơn");
            } else {
                renderTable(listVe);
                lbl_thongBao.setText("");
                KhachHang kh = khach_Hang_dao.getKhachHangTheoMaKH(hoaDon.getKhachHang().getMaKH());
                txt_tenNguoiDat.setText(kh.getTenKH());
                txt_email.setText(kh.getEmail());
                txt_sdt.setText(kh.getSdt());
                txt_cccd.setText(kh.getSoCCCD());
            }
>>>>>>> Stashed changes
        });

        btn_lamMoi.setOnAction(e -> {
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
        });

<<<<<<< Updated upstream
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
=======
        // Thêm các vé đã được chọn trong checkbox vào selectedVe
        tbl_thongTinVe.setRowFactory(tv -> {
            TableRow<Ve> row = new TableRow<>();
            row.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    selectedVe.add(row.getItem());
                } else {
                    selectedVe.remove(row.getItem());
                }
            });
            return row;
        });
>>>>>>> Stashed changes

        btn_yeuCau.setOnAction(e -> {
            if (selectedVe.isEmpty()) { // Nếu không chọn vé nào
                lbl_thongBao.setText("Vui lòng chọn vé cần hủy");
                return;
            }

            ArrayList<Ve> dsVeHuy = new ArrayList<>(selectedVe); // Danh sách vé cần hủy để lọc ra các vé đã đổi
            ArrayList<Ve> dsVeDaDoi = new ArrayList<>(); // Danh sách vé đã đổi

            // Lấy thời gian khởi hành của lịch trình
            LichTrinh lt = lichTrinh_dao.getLichTrinhTheoID(selectedVe.get(0).getCtlt().getLichTrinh().getMaLichTrinh());
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
            int tongTienVe = 0;
            int tongLePhi = 0;

            // Tính tổng tiền vé và lệ phí
            for (Ve ve : dsVeHuy) { // Duyệt qua danh sách vé cần hủy đã lọc các vé đã đổi ra
                ChiTietHoaDon ctHoaDon = ct_hoaDon_dao.getCT_HoaDonTheoMaVe(ve.getMaVe());
<<<<<<< Updated upstream
                HoaDon hd = hoaDon_dao.getHoaDonTheoMa(ctHoaDon.getHoaDon().getMaHoaDon());

                // lấy giá vé
                txt_giaVe.setText(String.valueOf(ctHoaDon.getGiaVe()));

                LocalDateTime ngayLap = hd.getNgayLapHoaDon();
                // ngày theo format dd/MM/yyyy HH:mm
                txt_ngayLap.setText(ngayLap.getDayOfMonth() + "/" + ngayLap.getMonthValue() + "/" + ngayLap.getYear() + " " + ngayLap.getHour() + ":" + ngayLap.getMinute());

                LichTrinh lt = lichTrinh_dao.getLichTrinhTheoID(ve.getCtlt().getLichTrinh().getMaLichTrinh());
                txt_gaDi.setText(lt.getGaDi().getTenGa());
                txt_gaDen.setText(lt.getGaDen().getTenGa());

                LocalDateTime tgKhoiHanh = lt.getThoiGianKhoiHanh();
                LocalDateTime now = LocalDateTime.now();

                // nếu trả trước 4h thì hủy được, chiết khấu là 20%
                if (tgKhoiHanh.isAfter(now) && tgKhoiHanh.plusHours(4).isAfter(now)) {
                    txt_chietKhau.setText("20%");
                    txt_hoanTien.setText(String.valueOf(ctHoaDon.getGiaVe() * 0.8));
                    isHuy = true;
                } else {
                    label_thongBao.setText("Vé không thể hủy");
                }
=======
                tongTienVe += ctHoaDon.getGiaVe();
                tongLePhi += ctHoaDon.getGiaVe() * phanTram;
>>>>>>> Stashed changes
            }

            for (Ve ve : dsVeDaDoi) {
                ChiTietHoaDon ctHoaDon = ct_hoaDon_dao.getCT_HoaDonTheoMaVe(ve.getMaVe());
                tongTienVe += ctHoaDon.getGiaVe();
                tongLePhi += ctHoaDon.getGiaVe() * 0.3;
            }

            NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            txt_tongVeTra.setText(String.valueOf(dsVeHuy.size()));
            txt_tongTienVe.setText(currencyVN.format(tongTienVe));
            txt_tongLePhi.setText(currencyVN.format(tongLePhi));
            txt_tongTienTra.setText(currencyVN.format(tongTienVe - tongLePhi));
            btn_xacNhan.setDisable(false);
        });

<<<<<<< Updated upstream
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
=======
        btn_xacNhan.setOnAction(e -> {
            if (selectedVe.isEmpty()) {
                lbl_thongBao.setText("Vui lòng chọn vé cần hủy");
                return;
>>>>>>> Stashed changes
            }
            lbl_thongBao.setText("");
            for (Ve ve : selectedVe) {
                ve.setTinhTrangVe("DaDoi");
                ve_dao.update(ve);
            }
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
            selectedVe.clear();
            btn_xacNhan.setDisable(true);

            // TODO: in phiếu hủy vé
        });

    }

    public void renderTable(ArrayList<Ve> listVe) {
        // TODO
        ObservableList<Ve> list = FXCollections.observableArrayList(listVe);
        tbl_thongTinVe.setItems(list);
        col_maVe.setCellValueFactory(new PropertyValueFactory<>("maVe"));
<<<<<<< Updated upstream
        col_maHK.setCellValueFactory(new PropertyValueFactory<>("maHK"));
        col_maSoCho.setCellValueFactory(new PropertyValueFactory<>("maSoCho"));
        col_maLichTrinh.setCellValueFactory(new PropertyValueFactory<>("maLT"));
        col_tinhTrangVe.setCellValueFactory(new PropertyValueFactory<>("tinhTrangVe"));
        col_khuHoi.setCellValueFactory(new PropertyValueFactory<>("khuHoi"));
        col_dob.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        col_tenHK.setCellValueFactory(new PropertyValueFactory<>("tenHK"));
        col_cccd.setCellValueFactory(new PropertyValueFactory<>("soCCCD"));
=======

        // col thông tin hành khách chứa TenHK, cccd, sdt
        col_thongTinHK.setCellValueFactory(p -> {
            KhachHang kh = khach_Hang_dao.getKhachHangTheoMaKH(p.getValue().getHanhKhach().getMaKH());
            return new SimpleStringProperty(kh.getTenKH() + " \n " + "CCCD/CMND: " + kh.getSoCCCD() + " \n " + "SĐT: " + kh.getSdt());
        });

        col_thongTinVe.setCellValueFactory(p -> {
            Ve ve = ve_dao.getVeTheoID(p.getValue().getMaVe());
            LoaiVe lv = loaiVe_dao.getLoaiVeTheoMa(ve.getLoaiVe().getMaLoaiVe());
            LichTrinh lt = new LichTrinh_DAO().getLichTrinhTheoID(ve.getCtlt().getLichTrinh().getMaLichTrinh());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new SimpleStringProperty("Ga đi: " + new Ga_DAO().getGaTheoMaGa(lt.getGaDi().getMaGa()).getTenGa() + " \n " +
                    "Ga đến: " + new Ga_DAO().getGaTheoMaGa(lt.getGaDen().getMaGa()).getTenGa() + " \n " +
                    "Thời gian khởi hành: " + lt.getThoiGianKhoiHanh().format(formatter) + " \n " +
                    lv.getTenLoaiVe());
        });

        col_tinhTrangVe.setCellValueFactory(new PropertyValueFactory<>("tinhTrangVe"));

        col_giaVe.setCellValueFactory(p -> {
            ChiTietHoaDon ctHoaDon = ct_hoaDon_dao.getCT_HoaDonTheoMaVe(p.getValue().getMaVe());
            return new SimpleStringProperty(String.valueOf(ctHoaDon.getGiaVe()));
        });

        col_chonVe.setCellFactory(new CheckBoxCellFactory());
    }



    // Hàm giải mã mã vạch từ ảnh
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
>>>>>>> Stashed changes
    }

    private void initDAO() {
        ve_dao = new Ve_DAO();
        khach_Hang_dao = new KhachHang_DAO();
        loaiVe_dao = new LoaiVe_DAO();
        ct_hoaDon_dao = new CT_HoaDon_DAO();
        hoaDon_dao = new HoaDon_DAO();
        lichTrinh_dao = new LichTrinh_DAO();
        ct_lichTrinh_dao = new CT_LichTrinh_DAO();
    }


    /**
     * A custom cell factory for creating table cells with checkboxes.
     * This class implements the Callback interface to provide a custom TableCell
     * for a TableColumn of type Ve with Boolean values.
     */
    public class CheckBoxCellFactory implements Callback<TableColumn<Ve, Boolean>, TableCell<Ve, Boolean>> {

        /**
         * Creates a new TableCell containing a CheckBox for each cell in the column.
         *
         * @param param the TableColumn for which this cell factory is being called
         * @return a TableCell containing a CheckBox
         */
        @Override
        public TableCell<Ve, Boolean> call(TableColumn<Ve, Boolean> param) {
            return new TableCell<Ve, Boolean>() {
                private final CheckBox checkBox = new CheckBox();

                /**
                 * Updates the item in the cell. This method is called whenever the item in the cell changes.
                 *
                 * @param item  the new item for the cell
                 * @param empty whether or not this cell represents data from the list. If it is empty, then it does not
                 */
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(checkBox);
                        checkBox.setSelected(item != null && item);
                        checkBox.setOnAction(e -> {
                            Ve ve = getTableView().getItems().get(getIndex());
                            // Handle checkbox state change
                        });
                    }
                }
            };
        }
    }
}
