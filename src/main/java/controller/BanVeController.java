/*
 * @ (#) BanVeController.java       1.0     04/10/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import entity.Ga;
import entity.LichTrinh;
import javafx.fxml.Initializable;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   04/10/2024
 * version: 1.0
 */
public class BanVeController implements Initializable {
    private Ga gadi = null;
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
    private TableColumn<Ga, String> colGaDen;

    @FXML
    private TableColumn<LichTrinh, String> colGaDi;

    @FXML
    private TableColumn<LichTrinh, LocalDateTime> colGioKH;

    @FXML
    private TableColumn<Integer, Integer> colSTT;

    @FXML
    private TableColumn<LichTrinh, String> colSoHieuTau;

    @FXML
    private TableColumn<LichTrinh, LocalDateTime> colTGDen;

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
    private TableView<?> tbTCTLT;

    @FXML
    void hienThiThongTin(MouseEvent event) {

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        chkKhuHoi.setOnAction(e -> {
            if (chkKhuHoi.isSelected()) {
                dpNgayVe.setDisable(false);
            } else {
                dpNgayVe.setDisable(true);
            }
        });

        cbGaDi.getItems().add("Hồ Chí Minh");
        cbGaDi.getItems().add("Hà Nội");
        cbGaDi.getItems().add("Đà Nẵng");
        cbGaDi.getItems().add("Nha Trang");
        cbGaDi.getItems().add("Đà Lạt");
        cbGaDen.getItems().add("Hồ Chí Minh");
        cbGaDen.getItems().add("Hà Nội");
        cbGaDen.getItems().add("Đà Nẵng");
        cbGaDen.getItems().add("Nha Trang");
        cbGaDen.getItems().add("Đà Lạt");

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



        if (dpNgayKH.getValue() != null && dpNgayKH.getValue().isBefore(LocalDate.now())) {
            dpNgayKH.setValue(LocalDate.now());
        }
        if (dpNgayKH.getValue() != null && dpNgayKH.getValue().isAfter(dpNgayVe.getValue())) {
            dpNgayVe.setValue(dpNgayKH.getValue());
        }

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
