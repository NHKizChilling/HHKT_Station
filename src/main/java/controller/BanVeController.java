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
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private TableView<?> tbTCTLT;

    @FXML
    void hienThiThongTin(MouseEvent event) {

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chkKhuHoi.setOnMouseClicked(e -> {
            if (chkKhuHoi.isSelected()) {
                pnlThongTinCho.setVisible(true);
            } else {
                pnlThongTinCho.setVisible(false);
            }
        });
    }
}
