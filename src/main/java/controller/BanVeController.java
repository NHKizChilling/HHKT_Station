/*
 * @ (#) BanVeController.java       1.0     04/10/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import connectdb.ConnectDB;
import dao.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import entity.*;
import gui.TrangChu_GUI;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   04/10/2024
 * version: 1.0
 */
public class BanVeController implements Initializable {
    private final LichTrinh_DAO lt_dao = new LichTrinh_DAO();
    private final CT_LichTrinh_DAO ctlt_dao = new CT_LichTrinh_DAO();
    private final Ga_DAO ga_dao = new Ga_DAO();
    private final KhachHang_DAO hk_dao = new KhachHang_DAO();
    private final HoaDon_DAO hd_dao = new HoaDon_DAO();
    private final Toa_DAO toa_dao = new Toa_DAO();
    private final LoaiToa_DAO ltoa_dao = new LoaiToa_DAO();
    private final ChoNgoi_DAO cn_dao = new ChoNgoi_DAO();

    private final String styleGheThuong = "-fx-background-color: white; -fx-text-fill: black;-fx-border-color: black;";
    private final String styleGheDaChon = "-fx-background-color: green; -fx-text-fill: white;-fx-border-color: black;";
    private final String styleGhe1 = "-fx-border-width: 0 0 0 3;";
    private final String styleGhe2 = "-fx-border-width: 0 3 0 0;";
    private final String styleGheDaDat = "-fx-background-color: red; -fx-text-fill: white;-fx-border-color: black;";
    private final String styleGiuong = "-fx-background-color: white; -fx-text-fill: black;-fx-border-color: black;-fx-border-width: 0 0 3 0;";
    private final String styleGiuongDaChon = "-fx-background-color: green; -fx-text-fill: white;-fx-border-color: black;-fx-border-width: 0 0 3 0;";
    private final String styleGiuongDaDat = "-fx-background-color: red; -fx-text-fill: white;-fx-border-color: black;-fx-border-width: 0 0 3 0;";

    @FXML
    private Pane paneTau;

    @FXML
    private Pane paneChonCD;

    @FXML
    private AnchorPane acpTTKH;

    @FXML
    private JFXButton btnLamMoi;

    @FXML
    private JFXButton btnLamMoiTTKH;

    @FXML
    private JFXButton btnTaoHD;

    @FXML
    private JFXButton btnTraCuuCT;

    @FXML
    private JFXButton btnChonCD;

    @FXML
    private JFXButton btnChonKH;

    @FXML
    private JFXButton btnChonFullToa;

    @FXML
    private JFXButton btnChonKhoang;

    @FXML
    private JFXButton btnHuyChon;

    @FXML
    private JFXButton btnXoaAllCN;

    @FXML
    private JFXComboBox<String> cbGaDen;

    @FXML
    private JFXComboBox<String> cbGaDi;

    @FXML
    private ComboBox<String> cbKhoang;

    @FXML
    private TableColumn<ChiTietLichTrinh, String> colGiaCho;

    @FXML
    private TableColumn<LichTrinh, Integer> colLT_SLTrong;

    @FXML
    private TableColumn<LichTrinh, String> colLT_SoHieu;

    @FXML
    private TableColumn<ChiTietLichTrinh, String> colSTTCho;

    @FXML
    private TableColumn<ChiTietLichTrinh, String> colSoHieuTau;

    @FXML
    private TableColumn<ChiTietLichTrinh, Integer> colToa;

    @FXML
    private TableColumn<ChiTietLichTrinh, FontAwesomeIcon> colXoaCN;

    @FXML
    private DatePicker dpNgayKH;

    @FXML
    private DatePicker dpNgayVe;

    @FXML
    private HBox grTrain;

    @FXML
    private BorderPane paneToa;

    @FXML
    private JFXRadioButton rdBtnKH;

    @FXML
    private JFXRadioButton rdBtnMC;

    @FXML
    private TableView<LichTrinh> tbLT;

    @FXML
    private TableView<ChiTietLichTrinh> tbTTCN;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtSoCCCD;

    @FXML
    private TextField txtTenKH;

    @FXML
    private Label lblToa;

    private String chosedId = null;
    private ObservableList<LichTrinh> dslt = null;
    private ArrayList<ChiTietLichTrinh> dsctlt = new ArrayList<>();
    private ObservableList<ChiTietLichTrinh> dsttcn = null;
    private ArrayList<ChiTietLichTrinh> dsctltkh = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ConnectDB.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tbLT.setItems(null);
        colLT_SoHieu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChuyenTau().getSoHieutau()));
        colLT_SLTrong.setCellValueFactory(cellData -> new SimpleIntegerProperty(lt_dao.getSoLuongChoConTrong(cellData.getValue().getMaLichTrinh())).asObject());
        tbLT.setStyle(tbLT.getStyle() + "-fx-selection-bar: #FFD700; -fx-selection-bar-non-focused: #FFD700;-fx-alignment: CENTER;-fx-content-display: CENTER;-fx-background-color: #fff;");

        tbTTCN.setItems(null);
        tbTTCN.setStyle(tbTTCN.getStyle() + "-fx-selection-bar: #FFD700; -fx-selection-bar-non-focused: #FFD700;-fx-alignment: CENTER;-fx-content-display: CENTER;-fx-background-color: #fff;");
        colSoHieuTau.setCellValueFactory(cellData -> {

            return new SimpleStringProperty(lt_dao.getLichTrinhTheoID(cellData.getValue().getLichTrinh().getMaLichTrinh()).getChuyenTau().getSoHieutau());
        });
        colToa.setCellValueFactory(cellData -> {
            Toa toa = toa_dao.getToaTheoID(cn_dao.getChoNgoiTheoMa(cellData.getValue().getChoNgoi().getMaChoNgoi()).getToa().getMaToa());
            return new SimpleIntegerProperty(toa.getSoSTToa()).asObject();
        });
        colSTTCho.setCellValueFactory(cellData -> {
            ChoNgoi cn = cn_dao.getChoNgoiTheoMa(cellData.getValue().getChoNgoi().getMaChoNgoi());
            Toa toa = toa_dao.getToaTheoID(cn.getToa().getMaToa());
            LoaiToa lt = ltoa_dao.getLoaiToaTheoMa(toa.getLoaiToa().getMaLoaiToa());
            return new SimpleStringProperty("Chỗ " + cn.getSttCho() + " " + lt.getTenLoaiToa());
        });
        colGiaCho.setCellValueFactory(cellData -> new SimpleStringProperty(DecimalFormat.getCurrencyInstance(Locale.of("vi", "VN")).format(cellData.getValue().getGiaCho())));
        colXoaCN.setCellValueFactory(cellData -> {
            FontAwesomeIcon icon = new FontAwesomeIcon();
            icon.setIcon(FontAwesomeIcons.TRASH);
            icon.setFill(Color.RED);
            icon.setSize("20");
            icon.setCursor(Cursor.HAND);
            icon.setOnMouseClicked(e -> {
                tbTTCN.getItems().remove(cellData.getValue());
                dsctlt.remove(cellData.getValue());
                tbTTCN.refresh();
                //đổi màu ghế đã chọn
                Button btn = (Button) paneToa.lookup("#" + cellData.getValue().getChoNgoi().getMaChoNgoi());
                if (btn != null) {
                    btn.setStyle(btn.getStyle() + "-fx-background-color: white; -fx-text-fill: black;");
                }
                btnXoaAllCN.setDisable(dsctlt.isEmpty());
            });
            return new SimpleObjectProperty<>(icon);
        });

        ArrayList<LichTrinh> temp = lt_dao.traCuuDSLichTrinhTheoNgay(LocalDate.of(2024, 12, 13));
        showTauTheoLT(temp);
        tbLT.setItems(FXCollections.observableList(temp));

        tbLT.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                LichTrinh lt1 = tbLT.getSelectionModel().getSelectedItem();
                if (lt1 != null) {
                    paneTau.lookup("#" + lt1.getMaLichTrinh()).fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, null));
                }
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
            if (cbGaDen.getSelectionModel().getSelectedItem() != null && cbGaDi.getSelectionModel().getSelectedItem() != null) {
                if (cbGaDi.getValue().equals(cbGaDen.getValue())) {
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
            if (cbGaDi.getSelectionModel().getSelectedItem() != null && cbGaDen.getSelectionModel().getSelectedItem() != null) {
                if (cbGaDi.getValue().equals(cbGaDen.getValue())) {
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
                setDisable(empty || date.isBefore(today));
            }
        });

        dpNgayVe.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (dpNgayKH.getValue() != null) {
                    LocalDate today = dpNgayKH.getValue();
                    setDisable(empty || date.isBefore(today));
                }
            }
        });

        btnLamMoi.setOnMouseClicked(e -> {
            lamMoi();
        });

        //Gộp 2 JFXRadioButton lại 1 nhóm
        ToggleGroup group = new ToggleGroup();
        rdBtnKH.setToggleGroup(group);
        rdBtnMC.setToggleGroup(group);
        rdBtnMC.setSelected(true);

        rdBtnMC.setOnAction(e -> {
            dpNgayVe.setDisable(true);
        });

        rdBtnKH.setOnAction(e -> {
            dpNgayVe.setDisable(false);
        });

        btnTraCuuCT.setOnMouseClicked(e -> {
            tbLT.setItems(null);
            txtTenKH.setText(null);
            txtSoCCCD.setText(null);
            txtSDT.setText(null);
            txtEmail.setText(null);
            if (cbGaDi.getValue() == null || cbGaDi.getEditor().getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ga đi");
                alert.show();
                cbGaDi.requestFocus();
            } else if (cbGaDen.getValue() == null || cbGaDen.getEditor().getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ga đến");
                alert.show();
                cbGaDen.requestFocus();
            } else if (dpNgayKH.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ngày khởi hành");
                alert.showAndWait();
                dpNgayKH.requestFocus();
                dpNgayKH.show();
            } else if (rdBtnKH.isSelected() && dpNgayVe.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ngày về");
                alert.showAndWait();
                dpNgayVe.requestFocus();
                dpNgayVe.show();
            } else {

                String maGaDi = ga_dao.getGaTheoTenGa(cbGaDi.getValue()).getMaGa();
                String maGaDen = ga_dao.getGaTheoTenGa(cbGaDen.getValue()).getMaGa();
                ArrayList<LichTrinh> list = lt_dao.traCuuDSLichTrinh(maGaDi, maGaDen, dpNgayKH.getValue());
                list.removeIf(l -> !l.getThoiGianKhoiHanh().isAfter(LocalDateTime.now()));
                if (list.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Không tìm thấy chuyến tàu phù hợp");
                    alert.show();
                    cbGaDi.requestFocus();
                } else {
                    showTauTheoLT(list);
                    if (rdBtnKH.isSelected()) {
                        paneChonCD.setVisible(true);
                    }
                }
            }

        });


        btnChonFullToa.setOnMouseClicked(e -> {
            LichTrinh lt = tbLT.getSelectionModel().getSelectedItem();
            ArrayList<ChoNgoi> dscn = cn_dao.getDsChoNgoiTheoToa(chosedId);
            dscn.sort(Comparator.comparing(ChoNgoi::getSttCho));
            GridPane gridPane = (GridPane) paneToa.getCenter();
            for (ChoNgoi cn : dscn) {
                Button btn = (Button) gridPane.lookup("#" + cn.getMaChoNgoi());
                if (!btn.getStyle().contains("red") && !btn.getStyle().contains("green")) {
                    btn.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, null));
                }
            }
            Toa toa = toa_dao.getToaTheoID(chosedId);
            if (!toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("NC") && !toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("NM")) {
                btnChonKhoang.setDisable(true);
            }
            btnHuyChon.setDisable(false);
            btnXoaAllCN.setDisable(false);
        });

        cbKhoang.setItems(FXCollections.observableArrayList("Khoang 1", "Khoang 2", "Khoang 3", "Khoang 4", "Khoang 5", "Khoang 6", "Khoang 7"));
        btnChonKhoang.setOnMouseClicked(e -> {
            if (cbKhoang.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn khoang");
                alert.show();
                cbKhoang.requestFocus();
                cbKhoang.show();
                return;
            }
            int khoang = Integer.parseInt(cbKhoang.getValue().split(" ")[1]);
            LichTrinh lt = tbLT.getSelectionModel().getSelectedItem();
            ArrayList<ChoNgoi> dscn = cn_dao.getDsChoNgoiTheoToa(chosedId);
            dscn.sort(Comparator.comparing(ChoNgoi::getSttCho));
            //đổi màu ghế đã chọn
            GridPane gridPane = (GridPane) paneToa.getCenter();
            for (ChoNgoi cn : dscn) {
                if (cn.getKhoang() == khoang) {
                    Button btn = (Button) gridPane.lookup("#" + cn.getMaChoNgoi());
                    if (!btn.getStyle().contains("red") && !btn.getStyle().contains("green")) {
                        btn.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, null));
                    }
                }
            }

            btnHuyChon.setDisable(false);
        });

        btnHuyChon.setOnMouseClicked(e -> {
            GridPane gridPane = (GridPane) paneToa.getCenter();
            for (Node node1 : gridPane.getChildren()) {
                if (node1 instanceof Button btn && (!node1.getStyle().contains("red") && node1.getStyle().contains("green"))) {
                    btn.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, null));
                }
            }
            Toa toa = toa_dao.getToaTheoID(chosedId);
            if (!toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("NC") && !toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("NM")) {
                btnChonKhoang.setDisable(false);
            }
            dsttcn = FXCollections.observableList(dsctlt);
            tbTTCN.setItems(dsttcn);
            btnChonFullToa.setDisable(false);
            btnHuyChon.setDisable(true);
            acpTTKH.setDisable(dsctlt.isEmpty());
        });

        btnChonCD.setOnMouseClicked(e -> {
            btnChonCD.setDisable(true);
            btnChonKH.setDisable(false);
            String maGaDi = ga_dao.getGaTheoTenGa(cbGaDi.getValue()).getMaGa();
            String maGaDen = ga_dao.getGaTheoTenGa(cbGaDen.getValue()).getMaGa();
            ArrayList<LichTrinh> list = lt_dao.traCuuDSLichTrinh(maGaDi, maGaDen, dpNgayKH.getValue());
            list.removeIf(l -> !l.getThoiGianKhoiHanh().isAfter(LocalDateTime.now()));
            dslt = FXCollections.observableList(list);
            tbLT.setItems(dslt);
            showTauTheoLT(list);
        });

        btnChonKH.setOnMouseClicked(e -> {
            btnChonCD.setDisable(false);
            btnChonKH.setDisable(true);
            String maGaDi = ga_dao.getGaTheoTenGa(cbGaDi.getValue()).getMaGa();
            String maGaDen = ga_dao.getGaTheoTenGa(cbGaDen.getValue()).getMaGa();
            ArrayList<LichTrinh> list = lt_dao.traCuuDSLichTrinh(maGaDen, maGaDi, dpNgayVe.getValue());
            list.removeIf(l -> !l.getThoiGianKhoiHanh().isAfter(LocalDateTime.now()));
            dslt = FXCollections.observableList(list);
            tbLT.setItems(dslt);
            showTauTheoLT(list);
        });

        btnXoaAllCN.setOnMouseClicked(e -> {
            tbTTCN.getItems().clear();
            dsctlt.clear();
            dsctltkh.clear();
            tbTTCN.refresh();
            GridPane gridPane = (GridPane) paneToa.getCenter();
            for (Node node1 : gridPane.getChildren()) {
                if (node1 instanceof Button btn && !node1.getStyle().contains("red") && node1.getStyle().contains("green")) {
                    btn.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, null));
                }
            }
            Toa toa = toa_dao.getToaTheoID(chosedId);
            if (!toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("NC") && !toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("NM")) {
                btnChonKhoang.setDisable(false);
            }
            btnChonFullToa.setDisable(false);
            btnHuyChon.setDisable(true);
            btnXoaAllCN.setDisable(true);
            acpTTKH.setDisable(true);
        });

        btnTaoHD.setOnMouseClicked(e -> {
            //Kiểm tra các field trống
            if (txtTenKH.getText() == null || txtTenKH.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập tên khách hàng");
                alert.show();
                txtTenKH.requestFocus();
                return;
            }
            if (txtSoCCCD.getText() == null || txtSoCCCD.getText().isEmpty()) {
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
            if (txtSDT.getText() == null || txtSDT.getText().isEmpty()) {
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
            if (txtEmail.getText() == null || txtEmail.getText().isEmpty()) {
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
            if (hk_dao.getKhachHangTheoSDT(txtSDT.getText()) == null && hk_dao.getKHTheoCCCD(txtSoCCCD.getText()) == null) {
                hk_dao.create(new KhachHang("temp", txtTenKH.getText(), txtSoCCCD.getText(), txtSDT.getText(), txtEmail.getText()));
            }
            getData.kh = hk_dao.getKhachHangTheoSDT(txtSDT.getText());
            getData.dsctlt = dsctlt;
            getData.dsctltkh = dsctltkh;
            LocalDateTime now = LocalDateTime.now();
            now = now.minusNanos(now.getNano());
            HoaDon hd = new HoaDon("temp", getData.nv, getData.kh, now, new KhuyenMai(null), false);
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
            try {
                AnchorPane acpHoaDon = FXMLLoader.load(Objects.requireNonNull(TrangChu_GUI.class.getResource("hoa-don.fxml")));
                Stage stgHoaDon = new Stage();
                stgHoaDon.setTitle("Thanh toán");
                stgHoaDon.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
                stgHoaDon.setScene(new Scene(acpHoaDon));
                stgHoaDon.sizeToScene();
                stgHoaDon.show();
                stgHoaDon.setOnCloseRequest(e1 -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Xác nhận");
                    alert.setHeaderText(null);
                    alert.setContentText("Xác nhận thoát?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        HoaDon hoaDon = hd_dao.getHoaDonVuaTao();
                        if (!hoaDon.isTrangThai() && hoaDon.getTongTien() == 0) {
                            hd_dao.delete(hoaDon);
                        }
                        stgHoaDon.close();
                        lamMoi();
                    } else {
                        e1.consume();
                    }
                });

                Button btnBack = (Button) acpHoaDon.lookup("#btnBackBanVe");
                btnBack.setOnMouseClicked(e1 -> {
                    HoaDon hoaDon = hd_dao.getHoaDonVuaTao();
                    hd_dao.delete(hoaDon);
                    getData.hd = null;
                    getData.dsctlt = null;
                    getData.dsctltkh = null;
                    getData.kh = null;
                    getData.dsve = null;
                    getData.dscthd = null;
                    stgHoaDon.close();
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
        //Nhập số điện thoại hiển thị ra khách hàng

        txtSDT.setOnKeyTyped(e -> {
            if (hk_dao.getKhachHangTheoSDT(txtSDT.getText()) != null) {
                KhachHang kh = hk_dao.getKhachHangTheoSDT(txtSDT.getText());
                txtTenKH.setText(kh.getTenKH());
                txtSoCCCD.setText(kh.getSoCCCD());
                txtEmail.setText(kh.getEmail());
            }

        });

        txtSoCCCD.setOnKeyTyped(e -> {
            if (hk_dao.getKHTheoCCCD(txtSoCCCD.getText()) != null) {
                KhachHang kh = hk_dao.getKHTheoCCCD(txtSoCCCD.getText());
                txtTenKH.setText(kh.getTenKH());
                txtSDT.setText(kh.getSdt());
                txtEmail.setText(kh.getEmail());
            }
        });

        btnLamMoiTTKH.setOnMouseClicked(e -> {
            txtTenKH.setText("");
            txtSoCCCD.setText("");
            txtSDT.setText("");
            txtEmail.setText("");
        });
    }

    private void lamMoi() {
        getData.hd = null;
        getData.dsctlt = null;
        getData.dsctltkh = null;
        getData.kh = null;
        getData.dsve = null;
        getData.dscthd = null;
        dsctlt.clear();
        dsctltkh.clear();
        paneTau.getChildren().clear();
        paneChonCD.setVisible(false);
        acpTTKH.setDisable(true);
        btnChonCD.setDisable(true);
        btnChonKH.setDisable(false);
        btnXoaAllCN.setDisable(true);
        showTauTheoLT(lt_dao.traCuuDSLichTrinhTheoNgay(LocalDate.of(2024, 12, 13)));
        cbGaDi.setValue(null);
        cbGaDi.requestFocus();
        cbGaDen.setValue(null);
        dpNgayKH.setValue(null);
        dpNgayVe.setDisable(true);
        dpNgayVe.setValue(null);
        rdBtnMC.setSelected(true);
        tbTTCN.setItems(null);
        txtTenKH.setText(null);
        txtSoCCCD.setText(null);
        txtSDT.setText(null);
        txtEmail.setText(null);
    }

    private void showTauTheoLT(ArrayList<LichTrinh> list) {
        dslt = FXCollections.observableList(list);
        tbLT.setItems(dslt);
        tbLT.refresh();
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
                    tbLT.getSelectionModel().select(lt);
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
            //thêm khoảng cách giữa 2 tàu
            Pane pane = new Pane();
            pane.setPrefHeight(10);
            pane.setPrefWidth(10);
            paneTau.getChildren().add(pane);
        }
        //chọn tàu đầu tiên
        if (!list.isEmpty()) {
            ImageView imageView = (ImageView) paneTau.lookup("#" + list.getFirst().getMaLichTrinh());
            imageView.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
        }
    }

    private void showToaTheoLT(LichTrinh lt) {
        chosedId = null;
        grTrain.getChildren().clear();
        ArrayList<Toa> dstoa = toa_dao.getAllToaTheoChuyenTau(lt.getChuyenTau().getSoHieutau());
        if (!btnChonKH.isDisable()) {
            Collections.reverse(dstoa);
        } else {
            ImageView imageView1 = new ImageView("file:src/main/resources/img/train1.png");
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
        }
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
                //enable btnChonKhoang khi không phải toa NC hay NM
                if (!toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("NC") && !toa.getLoaiToa().getMaLoaiToa().equalsIgnoreCase("NM")) {
                    cbKhoang.setDisable(false);
                    btnChonKhoang.setDisable(false);
                } else {
                    cbKhoang.setDisable(true);
                    btnChonKhoang.setDisable(true);
                }
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
                            seatButton.setCursor(Cursor.HAND);
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

                            //đổi màu seatbtn nếu đã có trong tbCTCN
                            int finalCol = col;
                            for (ChiTietLichTrinh ctlt : dsctlt) {
                                if (ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId())) {
                                    seatButton.setStyle(styleGheDaChon  + (finalCol > 7 ? styleGhe2 : styleGhe1));
                                }
                            }
                            seatButton.setOnMouseClicked(event -> {
                                if (seatButton.getStyle().contains("green")) {
                                    seatButton.setStyle(styleGheThuong + (finalCol > 7 ? styleGhe2 : styleGhe1));
                                    dsctlt.removeIf(ctlt -> ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId()));
                                    dsctltkh.removeIf(ctlt -> ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId()));
                                    dsttcn = FXCollections.observableList(dsctlt);
                                    tbTTCN.setItems(dsttcn);
                                } else if (seatButton.getStyle().contains("red")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được đặt");
                                    alert.show();
                                } else {
                                    seatButton.setStyle(styleGheDaChon + (finalCol > 7 ? styleGhe2 : styleGhe1));
                                    dsctlt.add(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), seatButton.getId()));
                                    if (btnChonKH.isDisable()) {
                                        dsctltkh.add(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), seatButton.getId()));
                                    }
                                    dsttcn = FXCollections.observableList(dsctlt);
                                    tbTTCN.setItems(dsttcn);
                                    tbTTCN.refresh();
                                }
                                if (!dsctlt.isEmpty()) {
                                    btnXoaAllCN.setDisable(false);
                                    acpTTKH.setDisable(false);
                                } else {
                                    btnXoaAllCN.setDisable(true);
                                    acpTTKH.setDisable(true);
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
                            seatButton.setCursor(Cursor.HAND);
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
                            for (ChiTietLichTrinh ctlt : dsctlt) {
                                if (ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId())) {
                                    seatButton.setStyle(styleGheDaChon + (finalCol > 7 ? styleGhe2 : styleGhe1));
                                }
                            }
                            seatButton.setOnMouseClicked(event -> {
                                if (seatButton.getStyle().contains("green")) {
                                    seatButton.setStyle(styleGheThuong + (finalCol > 7 ? styleGhe2 : styleGhe1));
                                    dsctlt.removeIf(ctlt -> ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId()));
                                    dsctltkh.removeIf(ctlt -> ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId()));
                                    dsttcn = FXCollections.observableList(dsctlt);
                                    tbTTCN.setItems(dsttcn);
                                } else if (seatButton.getStyle().contains("red")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được đặt");
                                    alert.show();
                                } else {
                                    seatButton.setStyle(styleGheDaChon + (finalCol > 7 ? styleGhe2 : styleGhe1));
                                    dsctlt.add(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), seatButton.getId()));
                                    if (btnChonKH.isDisable()) {
                                        dsctltkh.add(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), seatButton.getId()));
                                    }
                                    dsttcn = FXCollections.observableList(dsctlt);
                                    tbTTCN.setItems(dsttcn);
                                    tbTTCN.refresh();
                                }
                                if (!dsctlt.isEmpty()) {
                                    btnXoaAllCN.setDisable(false);
                                    acpTTKH.setDisable(false);
                                } else {
                                    btnXoaAllCN.setDisable(true);
                                    acpTTKH.setDisable(true);
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
                            seatButton.setCursor(Cursor.HAND);
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

                            for (ChiTietLichTrinh ctlt : dsctlt) {
                                if (ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId())) {
                                    seatButton.setStyle(styleGiuongDaChon);
                                }
                            }
                            seatButton.setOnMouseClicked(event -> {
                                if (seatButton.getStyle().contains("green")) {
                                    seatButton.setStyle(styleGiuong);
                                    dsctlt.removeIf(ctlt -> ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId()));
                                    dsctltkh.removeIf(ctlt -> ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId()));
                                    dsttcn = FXCollections.observableList(dsctlt);
                                    tbTTCN.setItems(dsttcn);
                                } else if (seatButton.getStyle().contains("red")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được đặt");
                                    alert.show();
                                } else {
                                    seatButton.setStyle(styleGiuongDaChon);
                                    dsctlt.add(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), seatButton.getId()));
                                    if (btnChonKH.isDisable()) {
                                        dsctltkh.add(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), seatButton.getId()));
                                    }
                                    dsttcn = FXCollections.observableList(dsctlt);
                                    tbTTCN.setItems(dsttcn);
                                    tbTTCN.refresh();
                                }
                                if (!dsctlt.isEmpty()) {
                                    btnXoaAllCN.setDisable(false);
                                    acpTTKH.setDisable(false);
                                } else {
                                    btnXoaAllCN.setDisable(true);
                                    acpTTKH.setDisable(true);
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
                            seatButton.setCursor(Cursor.HAND);
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

                            for (ChiTietLichTrinh ctlt : dsctlt) {
                                if (ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId())) {
                                    seatButton.setStyle(styleGiuongDaChon);
                                }
                            }
                            seatButton.setOnMouseClicked(event -> {
                                if (seatButton.getStyle().contains("green")) {
                                    seatButton.setStyle(styleGiuong);
                                    dsctlt.removeIf(ctlt -> ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId()));
                                    dsctltkh.removeIf(ctlt -> ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId()));
                                    dsttcn = FXCollections.observableList(dsctlt);
                                    tbTTCN.setItems(dsttcn);
                                } else if (seatButton.getStyle().contains("red")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được đặt");
                                    alert.show();
                                } else {
                                    seatButton.setStyle(styleGiuongDaChon);
                                    dsctlt.add(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), seatButton.getId()));
                                    if (btnChonKH.isDisable()) {
                                        dsctltkh.add(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), seatButton.getId()));
                                    }
                                    dsttcn = FXCollections.observableList(dsctlt);
                                    tbTTCN.setItems(dsttcn);
                                    tbTTCN.refresh();
                                }
                                if (!dsctlt.isEmpty()) {
                                    btnXoaAllCN.setDisable(false);
                                    acpTTKH.setDisable(false);
                                } else {
                                    btnXoaAllCN.setDisable(true);
                                    acpTTKH.setDisable(true);
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
                            seatButton.setCursor(Cursor.HAND);
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

                            for (ChiTietLichTrinh ctlt : dsctlt) {
                                if (ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId())) {
                                    seatButton.setStyle(styleGiuongDaChon);
                                }
                            }
                            seatButton.setOnMouseClicked(event -> {
                                if (seatButton.getStyle().contains("green")) {
                                    seatButton.setStyle(styleGiuong);
                                    dsctlt.removeIf(ctlt -> ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId()));
                                    dsctltkh.removeIf(ctlt -> ctlt.getChoNgoi().getMaChoNgoi().equals(seatButton.getId()));
                                    dsttcn = FXCollections.observableList(dsctlt);
                                    tbTTCN.setItems(dsttcn);
                                } else if (seatButton.getStyle().contains("red")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được đặt");
                                    alert.show();
                                } else {
                                    seatButton.setStyle(styleGiuongDaChon);
                                    dsctlt.add(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), seatButton.getId()));
                                    if (btnChonKH.isDisable()) {
                                        dsctltkh.add(ctlt_dao.getCTLTTheoCN(lt.getMaLichTrinh(), seatButton.getId()));
                                    }
                                    dsttcn = FXCollections.observableList(dsctlt);
                                    tbTTCN.setItems(dsttcn);
                                    tbTTCN.refresh();
                                }
                                if (!dsctlt.isEmpty()) {
                                    btnXoaAllCN.setDisable(false);
                                    acpTTKH.setDisable(false);
                                } else {
                                    btnXoaAllCN.setDisable(true);
                                    acpTTKH.setDisable(true);
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
        if (!btnChonKH.isDisable()) {
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
        }
        //get toa có stt1
        ImageView imageView = (ImageView) grTrain.lookup("#" + dstoa.stream().filter(toa -> toa.getSoSTToa() == 1).findFirst().get().getMaToa());
        imageView.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
    }
}
