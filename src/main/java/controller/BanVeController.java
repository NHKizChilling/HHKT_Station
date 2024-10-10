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
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import entity.ChiTietLichTrinh;
import entity.Ga;
import entity.LichTrinh;
import gui.TrangChu_GUI;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
    private AnchorPane pnlThongTinCho;

    @FXML
    private TabPane tabToaTau;

    @FXML
    private TabPane tabTTVe;

    @FXML
    private TableView<LichTrinh> tbTCTLT;

    private ObservableList<LichTrinh> dslt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ConnectDB.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tbTCTLT.setStyle(tbTCTLT.getStyle() + "-fx-selection-bar: #FFD700; -fx-selection-bar-non-focused: #FFD700;-fx-alignment: CENTER;-fx-content-display: CENTER;-fx-background-color: #fff;");
        chkKhuHoi.setOnAction(e -> {
            if (chkKhuHoi.isSelected()) {
                dpNgayVe.setDisable(false);
            } else {
                dpNgayVe.setDisable(true);
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

        btnTraCuuCT.setOnMouseClicked(e -> {
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
                alert.show();
                dpNgayKH.requestFocus();
            } else if(chkKhuHoi.isSelected() && dpNgayVe.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ngày về");
                alert.show();
                dpNgayVe.requestFocus();
            } else {
                if(Integer.parseInt(lblSLnl.getText()) > 0 || Integer.parseInt(lblSLnct.getText()) > 0 || Integer.parseInt(lblSLhssv.getText()) > 0 || Integer.parseInt(lblSLte.getText()) > 0) {
                    String maGaDi = ga_dao.getGaTheoTenGa(cbGaDi.getValue()).getMaGa();
                    String maGaDen = ga_dao.getGaTheoTenGa(cbGaDen.getValue()).getMaGa();
                    dslt = FXCollections.observableList(lt_dao.getAll());
                    colSoHieuTau.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("chuyenTau"));
                    colGaDi.setCellValueFactory((new PropertyValueFactory<LichTrinh, String>("")));
                    colGioKH.setCellValueFactory((new PropertyValueFactory<LichTrinh, String>("thoiGianKhoiHanh")));
                    colTGDen.setCellValueFactory(new PropertyValueFactory<LichTrinh, String>("thoiGianDuKienDen"));
                    tbTCTLT.setItems(dslt);

                    colSTT.setCellValueFactory(column -> new SimpleIntegerProperty(tbTCTLT.getItems().indexOf(column.getValue()) + 1).asObject());
                    colGaDi.setCellValueFactory(param -> {
                        return new SimpleStringProperty(cbGaDi.getValue());
                    });

                    colGaDen.setCellValueFactory(param -> {
                        Ga g = ga_dao.getGaTheoMaGa(param.getValue().getGaDen().getMaGa());
                        return new SimpleStringProperty(g.getTenGa());
                    });
                    colSoHieuTau.setCellValueFactory(param ->
                            new SimpleStringProperty(param.getValue().getChuyenTau().getSoHieutau()));
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
                                //that cell created only on non-empty rows
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
                                        tabToaTau.getTabs().clear();
                                        Tab tab = new Tab("Toa 1: Ngồi mềm điều hòa");
                                        //Thê, grid pane vào tab
                                        GridPane n = new GridPane();
                                        n.setAlignment(Pos.CENTER);
                                        n.setPrefWidth(200);
                                        n.setPadding(new Insets(10, 10, 10, 10));
                                        n.setVgap(10);
                                        n.setHgap(10);
                                        //Thêm 64 chỗ ngồi vào grid pane
                                        for (int i = 0; i < 4; i++) {
                                             for (int j = 0; j < 8; j++) {
                                                Button btn = new Button();
                                                btn.setPrefSize(25, 25);
                                                btn.setStyle("-fx-background-color: #5098ff;-fx-text-fill: white;-fx-border-radius: 5px;-fx-font-size: 5px");
                                                btn.setText(String.valueOf(i * 8 + j + 1));
                                                n.add(btn, j, i);
                                                btn.setOnMouseClicked(ec -> {
                                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                                    alert.setTitle("Xác nhận");
                                                    alert.setHeaderText(null);
                                                    alert.setContentText("Bạn có chắc chắn muốn chọn ghế số " + btn.getText());
                                                    alert.showAndWait();
                                                });
                                            }
                                        }
                                        tab.setContent(n);
                                        tab.setStyle("-fx-alignment:center;");
                                        tabToaTau.getTabs().add(tab);
                                    });
                                }
                            }
                        };
                        return cell;
                    };
                    colChon.setCellFactory(cellFoctory);

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
                if (lblSLnct.getText().equals("0") && lblSLhssv.getText().equals("0") && lblSLte.getText().equals("0")) {
                    btnTangSLte.setDisable(false);
                }
            }
        });

        btnGiamSLnl.setOnMouseClicked(e -> {
            stt = Integer.parseInt(lblSLnl.getText());
            stt--;
            lblSLnl.setText(String.valueOf(stt));
            if (stt > 0) {
                btnGiamSLnl.setDisable(false);
            } else {
                btnGiamSLnl.setDisable(true);
                if (lblSLnct.getText().equals("0") && lblSLhssv.getText().equals("0")) {
                    btnTangSLte.setDisable(true);
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
                if (lblSLnl.getText().equals("0") && lblSLhssv.getText().equals("0") && lblSLte.getText().equals("0")) {
                    btnTangSLte.setDisable(false);
                }
            }
        });

        btnGiamSLnct.setOnMouseClicked(e -> {
            stt = Integer.parseInt(lblSLnct.getText());
            stt--;
            lblSLnct.setText(String.valueOf(stt));
            if (stt > 0) {
                btnGiamSLnct.setDisable(false);
            } else {
                btnGiamSLnct.setDisable(true);
                if (lblSLnl.getText().equals("0") && lblSLhssv.getText().equals("0")) {
                    btnTangSLte.setDisable(true);
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
                if (lblSLnl.getText().equals("0") && lblSLnct.getText().equals("0") && lblSLte.getText().equals("0")) {
                    btnTangSLte.setDisable(false);
                }
            }
        });

        btnGiamSLhssv.setOnMouseClicked(e -> {
            stt = Integer.parseInt(lblSLhssv.getText());
            stt--;
            lblSLhssv.setText(String.valueOf(stt));
            if (stt > 0) {
                btnGiamSLhssv.setDisable(false);
            } else {
                btnGiamSLhssv.setDisable(true);
                if (lblSLnl.getText().equals("0") && lblSLnct.getText().equals("0")) {
                    btnTangSLte.setDisable(true);
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
            }
        });

        btnGiamSLte.setOnMouseClicked(e -> {
            stt = Integer.parseInt(lblSLte.getText());
            stt--;
            lblSLte.setText(String.valueOf(stt));
            if (stt > 0) {
                btnGiamSLte.setDisable(false);
            } else {
                btnGiamSLte.setDisable(true);
            }
        });
    }
}
