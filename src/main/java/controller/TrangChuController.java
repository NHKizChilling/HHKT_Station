/*
 * @ (#) TrangChuController.java       1.0     28/09/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import connectdb.ConnectDB;
import dao.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.Ve;
import gui.TrangChu_GUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   28/09/2024
 * version: 1.0
 */
public class TrangChuController implements Initializable {

    @FXML
    private AnchorPane paneMain;
    @FXML
    private AnchorPane acpVe;
    @FXML
    private AnchorPane acpTK;
    @FXML
    private AnchorPane acpQLTK;
    @FXML
    private AnchorPane acpNV;
    @FXML
    private AnchorPane acpQL;
    @FXML
    private JFXButton btnFVe;
    @FXML
    private JFXButton btnFQLHD;
    @FXML
    private JFXButton btnQLNV;
    @FXML
    private JFXButton btnFKH;
    @FXML
    private JFXButton btnFBCTK;
    @FXML
    private JFXButton btnQLBCTK;
    @FXML
    private JFXButton btnTKDoanhThu;
    @FXML
    private JFXButton btnTKNV;
    @FXML
    private JFXButton btnFCT;
    @FXML
    private JFXButton btnFKM;
    @FXML
    private JFXButton btnUM;
    @FXML
    private JFXButton btnBanVeGUI;
    @FXML
    private JFXButton btnDoiVeGUI;
    @FXML
    private JFXButton btnHuyVeGUI;

    @FXML
    private JFXButton btnQLCT;

    @FXML
    private JFXButton btnQLHD;

    @FXML
    private JFXButton btnQLKH;

    @FXML
    private JFXButton btnQLKM;

    @FXML
    private JFXButton btnUMQL;

    @FXML
    private JFXButton btnQLTKDoanhThu;

    @FXML
    private JFXButton btnQLTKNV;

    @FXML
    private Label lblTenNhanVien;
    @FXML
    private Label timer;
    @FXML
    private Label lblNgay;
    @FXML
    private FontAwesomeIcon iconDown;
    @FXML
    private FontAwesomeIcon iconDownTK;
    @FXML
    private FontAwesomeIcon iconDownQLTK;
    @FXML
    private FontAwesomeIcon iconCT;

    @FXML
    private FontAwesomeIcon iconHD;

    @FXML
    private FontAwesomeIcon iconHK;

    @FXML
    private FontAwesomeIcon iconHelp;

    @FXML
    private FontAwesomeIcon iconNV;

    @FXML
    private FontAwesomeIcon iconTK;

    @FXML
    private FontAwesomeIcon iconKM;


    private final Ve_DAO ve_dao = new Ve_DAO();
    private final HoaDon_DAO hd_dao = new HoaDon_DAO();
    private final CT_LichTrinh_DAO ctlt_dao = new CT_LichTrinh_DAO();

    Time time = new Time(DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
    private String style = null;
    private String styleLV2 = null;
    Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1),
                    e -> {
                        time.oneSecondPassed();
                        timer.setText(time.getCurrentTime());
                    }));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Objects.equals(getData.nv.getChucVu(), "Nhân viên")) {
            acpNV.setDisable(false);
            acpQL.setDisable(true);
            acpQL.setVisible(false);
        } else {
            acpNV.setDisable(true);
            acpQL.setDisable(false);
            acpQL.setVisible(true);
        }
        paneMain.getChildren().clear();
        try {
            ConnectDB.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("gioi-thieu.fxml"));
            double width = paneMain.getWidth();
            double height = paneMain.getHeight();
            paneMain.getChildren().clear();
            paneMain.getChildren().add(loader.load());
            paneMain.setPrefSize(width, height);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        lblNgay.setText(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.now()));
        timer.setText(time.getCurrentTime());
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        style = btnFVe.getStyle();
        styleLV2 = btnBanVeGUI.getStyle();
        iconDown.setIcon(FontAwesomeIcons.ANGLE_DOWN);
        btnBanVeGUI.setOnMouseClicked(e -> chooseFeatureButtonLV2(btnBanVeGUI));
        btnDoiVeGUI.setOnMouseClicked(e -> chooseFeatureButtonLV2(btnDoiVeGUI));
        btnHuyVeGUI.setOnMouseClicked(e -> chooseFeatureButtonLV2(btnHuyVeGUI));
        btnTKDoanhThu.setOnMouseClicked(e -> chooseFeatureButtonLV2(btnTKDoanhThu));
        btnTKNV.setOnMouseClicked(e -> chooseFeatureButtonLV2(btnTKNV));
        btnFVe.setOnMouseClicked(e -> {
            try {
                ConnectDB.connect();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            new LichTrinh_DAO().updateTrangThaiCT(false);
            List<Button> dsbtnFVe = List.of(btnBanVeGUI, btnDoiVeGUI, btnHuyVeGUI);
            dsbtnFVe.forEach(btn -> {
                btn.setStyle(styleLV2);
            });
            resetFBCTK();
            if (iconDown.getText().equals("\uF107")) {
                btnFVe.setStyle(style + "-fx-border-width: 2 0 0 0;");
                chooseFeatureButton(btnFVe);
                iconDown.setIcon(FontAwesomeIcons.ANGLE_UP);
                acpVe.setVisible(true);
                List<Button> dsF = List.of(btnFQLHD, btnFKH, btnFBCTK, btnFCT, btnFKM,btnUM);
                dsF.forEach(btn -> {
                    btn.setLayoutY(btn.getLayoutY() + acpVe.getHeight());
                });
                iconDownTK.setLayoutY(iconDownTK.getLayoutY() + acpVe.getHeight());
            } else {
                onClick();
                btnFVe.setStyle(style + "-fx-border-width: 0 0 2 0;");
                iconDown.setIcon(FontAwesomeIcons.ANGLE_DOWN);
                acpVe.setVisible(false);
                List<Button> dsF = List.of(btnFQLHD, btnFKH, btnFBCTK, btnFCT, btnFKM,btnUM);
                dsF.forEach(btn -> {
                    btn.setLayoutY(btn.getLayoutY() - acpVe.getHeight());
                });

                iconDownTK.setLayoutY(iconDownTK.getLayoutY() - acpVe.getHeight());
            }
        });
        btnQLNV.setOnMouseClicked(e -> {
            chooseFeatureButton(btnQLNV);
            resetFVe();
            resetFBCTK();
        });
        btnFQLHD.setOnMouseClicked(e -> {
            chooseFeatureButton(btnFQLHD);
            resetFVe();
            resetFBCTK();
        });
        btnFKH.setOnMouseClicked(e -> {
            chooseFeatureButton(btnFKH);
            resetFVe();
            resetFBCTK();
        });
        btnFBCTK.setOnMouseClicked(e -> {
            resetFVe();
            if (iconDownTK.getText().equals("\uF107")) {
                chooseFeatureButton(btnFBCTK);
                iconDownTK.setIcon(FontAwesomeIcons.ANGLE_UP);
                acpTK.setVisible(true);
                btnUM.setLayoutY(btnUM.getLayoutY() + acpTK.getHeight());
                iconHelp.setLayoutY(iconHelp.getLayoutY() + acpTK.getHeight());
            } else {
                onClick();
                btnFBCTK.setStyle(style + "-fx-border-width: 0 0 2 0;");
                iconDownTK.setIcon(FontAwesomeIcons.ANGLE_DOWN);
                acpTK.setVisible(false);
                btnUM.setLayoutY(btnUM.getLayoutY() - acpTK.getHeight());
                iconHelp.setLayoutY(iconHelp.getLayoutY() - acpTK.getHeight());
            }
        });
        btnQLBCTK.setOnMouseClicked(e -> {
            if (iconDownQLTK.getText().equals("\uF107")) {
                chooseFeatureButtonQL(btnQLBCTK);
                iconDownQLTK.setIcon(FontAwesomeIcons.ANGLE_UP);
                acpQLTK.setVisible(true);
                btnUMQL.setLayoutY(btnUMQL.getLayoutY() + acpQLTK.getHeight());
                iconHelp.setLayoutY(iconHelp.getLayoutY() + acpQLTK.getHeight());
            } else {
                onClick();
                btnQLBCTK.setStyle(style + "-fx-border-width: 0 0 2 0;");
                iconDownQLTK.setIcon(FontAwesomeIcons.ANGLE_DOWN);
                acpQLTK.setVisible(false);
                btnUMQL.setLayoutY(btnUMQL.getLayoutY() - acpQLTK.getHeight());
                iconHelp.setLayoutY(iconHelp.getLayoutY() - acpQLTK.getHeight());
            }
        });
        btnQLNV.setOnMouseClicked(e -> {
            chooseFeatureButtonQL(btnQLNV);
            resetQLBCTK();
        });
        btnQLCT.setOnMouseClicked(e -> {
            chooseFeatureButtonQL(btnQLCT);
            resetQLBCTK();
        });
        btnQLKH.setOnMouseClicked(e -> {
            chooseFeatureButtonQL(btnQLKH);
            resetQLBCTK();
        });
        btnQLKM.setOnMouseClicked(e -> {
            chooseFeatureButtonQL(btnQLKM);
            resetQLBCTK();
        });

        btnQLHD.setOnMouseClicked(e -> {
            chooseFeatureButtonQL(btnQLHD);
            resetQLBCTK();
        });

        btnQLTKDoanhThu.setOnMouseClicked(e -> {
            chooseFeatureButtonQLLV2(btnQLTKDoanhThu);
        });
        btnQLTKNV.setOnMouseClicked(e -> {
            chooseFeatureButtonQLLV2(btnQLTKNV);
        });

        btnFCT.setOnMouseClicked(e -> {
            chooseFeatureButton(btnFCT);
            resetFVe();
            resetFBCTK();
        });
        btnFKM.setOnMouseClicked(e -> {
            chooseFeatureButton(btnFKM);
            resetFVe();
            resetFBCTK();
        });
        TrangChu_GUI.nv = getData.nv;
        lblTenNhanVien.setText("Chào, " + TrangChu_GUI.nv.getChucVu() + " " + TrangChu_GUI.nv.getTenNhanVien());
    }

    private void resetFVe() {
        List<Button> dsbtnFVe = List.of(btnBanVeGUI, btnDoiVeGUI, btnHuyVeGUI);
        dsbtnFVe.forEach(btn -> {
            btn.setStyle(styleLV2);
        });
        iconDown.setIcon(FontAwesomeIcons.ANGLE_DOWN);
        if (acpVe.isVisible()) {
            acpVe.setVisible(false);
            List<Button> dsF = List.of(btnFQLHD, btnFKH, btnFBCTK, btnFCT, btnFKM,btnUM);
            List<FontAwesomeIcon> dsIcon = List.of(iconHD, iconNV, iconHK, iconCT, iconKM, iconTK, iconDownTK, iconHelp);
            dsF.forEach(btn -> {
                btn.setLayoutY(btn.getLayoutY() - acpVe.getHeight());
            });
            dsIcon.forEach(icon -> {
                icon.setLayoutY(icon.getLayoutY() - acpVe.getHeight());
            });
        }
    }

    private void resetFBCTK() {
        List<Button> listBtn = List.of(btnTKDoanhThu, btnTKNV);
        listBtn.forEach(btn -> {
            btn.setStyle(styleLV2);
        });
        iconDownTK.setIcon(FontAwesomeIcons.ANGLE_DOWN);
        if (acpTK.isVisible()) {
            acpTK.setVisible(false);
            btnUM.setLayoutY(btnUM.getLayoutY() - acpTK.getHeight());
            iconHelp.setLayoutY(iconHelp.getLayoutY() - acpTK.getHeight());
        }
    }

    private void resetQLBCTK() {
        List<Button> listBtn = List.of(btnQLTKDoanhThu, btnQLTKNV);
        listBtn.forEach(btn -> {
            btn.setStyle(styleLV2);
        });
        iconDownQLTK.setIcon(FontAwesomeIcons.ANGLE_DOWN);
        if (acpQLTK.isVisible()) {
            acpQLTK.setVisible(false);
            btnUMQL.setLayoutY(btnUMQL.getLayoutY() - acpQLTK.getHeight());
            iconHelp.setLayoutY(iconHelp.getLayoutY() - acpQLTK.getHeight());
        }
    }

    @FXML
    protected void showBanVeGUI() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("ban-ve.fxml"));
        double width = paneMain.getWidth();
        double height = paneMain.getHeight();
        try {
            paneMain.getChildren().clear();
            paneMain.getChildren().add(loader.load());
            paneMain.setPrefSize(width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<HoaDon> dsHD = hd_dao.getDSHDLuuTam();
        for (HoaDon hd : dsHD) {
            //Xóa hóa đơn lưu hơn 15 phút
            if (hd.getNgayLapHoaDon().plusMinutes(15).isBefore(LocalDateTime.now())) {
                ArrayList<ChiTietHoaDon> dsCTHD = new CT_HoaDon_DAO().getCT_HoaDon(hd.getMaHoaDon());
                for (ChiTietHoaDon cthd : dsCTHD) {
                    if (cthd != null) {
                        Ve ve = ve_dao.getVeTheoID(cthd.getVe().getMaVe());
                        ve_dao.updateTinhTrangVe(ve.getMaVe(), "DaHuy");
                        ctlt_dao.updateCTLT(ve.getCtlt(), true);
                    }
                }
            }
        }
    }

    @FXML
    protected void showDoiVeGUI() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("doi-ve.fxml"));
        double width = paneMain.getWidth();
        double height = paneMain.getHeight();
        try {
            paneMain.getChildren().clear();
            paneMain.getChildren().add(loader.load());
            paneMain.setPrefSize(width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<HoaDon> dsHD = hd_dao.getDSHDLuuTam();
        for (HoaDon hd : dsHD) {
            //Xóa hóa đơn lưu hơn 15 phút
            if (hd.getNgayLapHoaDon().plusMinutes(15).isBefore(LocalDateTime.now())) {
                ArrayList<ChiTietHoaDon> dsCTHD = new CT_HoaDon_DAO().getCT_HoaDon(hd.getMaHoaDon());
                for (ChiTietHoaDon cthd : dsCTHD) {
                    if (cthd != null) {
                        Ve ve = ve_dao.getVeTheoID(cthd.getVe().getMaVe());
                        ve_dao.updateTinhTrangVe(ve.getMaVe(), "DaHuy");
                        ctlt_dao.updateCTLT(ve.getCtlt(), true);
                    }
                }
            }
        }
    }

    @FXML
    protected void showHuyVeGUI() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("huy-ve.fxml"));
        double width = paneMain.getWidth();
        double height = paneMain.getHeight();
        try {
            paneMain.getChildren().clear();
            paneMain.getChildren().add(loader.load());
            paneMain.setPrefSize(width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<HoaDon> dsHD = hd_dao.getDSHDLuuTam();
        for (HoaDon hd : dsHD) {
            //Xóa hóa đơn lưu hơn 15 phút
            if (hd.getNgayLapHoaDon().plusMinutes(15).isBefore(LocalDateTime.now())) {
                ArrayList<ChiTietHoaDon> dsCTHD = new CT_HoaDon_DAO().getCT_HoaDon(hd.getMaHoaDon());
                for (ChiTietHoaDon cthd : dsCTHD) {
                    if (cthd != null) {
                        Ve ve = ve_dao.getVeTheoID(cthd.getVe().getMaVe());
                        ve_dao.updateTinhTrangVe(ve.getMaVe(), "DaHuy");
                        ctlt_dao.updateCTLT(ve.getCtlt(), true);
                    }
                }
            }
        }
    }

    @FXML
    protected void showHoaDonGUI() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("qly-hoa-don.fxml"));
        double width = paneMain.getWidth();
        double height = paneMain.getHeight();
        paneMain.getChildren().clear();
        try {
            paneMain.getChildren().add(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        paneMain.setPrefSize(width, height);
    }

    @FXML
    protected void showNhanVienGUI() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("nhan-vien.fxml"));
        double width = paneMain.getWidth();
        double height = paneMain.getHeight();
        paneMain.getChildren().clear();
        try {
            paneMain.getChildren().add(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        paneMain.setPrefSize(width, height);
    }

    @FXML
    protected void showChuyenTauGUI() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("chuyen-tau.fxml"));
        double width = paneMain.getWidth();
        double height = paneMain.getHeight();
        paneMain.getChildren().clear();
        try {
            paneMain.getChildren().add(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        paneMain.setPrefSize(width, height);
    }

    @FXML
    protected void showKHGUI() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("khach-hang.fxml"));
        double width = paneMain.getWidth();
        double height = paneMain.getHeight();
        paneMain.getChildren().clear();
        try {
            paneMain.getChildren().add(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void showKhuyenMaiGUI() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("khuyen-mai.fxml"));
        double width = paneMain.getWidth();
        double height = paneMain.getHeight();
        paneMain.getChildren().clear();
        try {
            paneMain.getChildren().add(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        paneMain.setPrefSize(width, height);
    }

    @FXML
    protected void onClick() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("gioi-thieu.fxml"));
        double width = paneMain.getWidth();
        double height = paneMain.getHeight();
        paneMain.getChildren().clear();
        try {
            paneMain.getChildren().add(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        paneMain.setPrefSize(width, height);
    }

    @FXML
    protected void showTKDoanhThuGUI() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("thong-ke-doanh-thu.fxml"));
        double width = paneMain.getWidth();
        double height = paneMain.getHeight();
        paneMain.getChildren().clear();
        try {
            paneMain.getChildren().add(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        paneMain.setPrefSize(width, height);
    }

    @FXML
    protected void showTKNhanVienGUI() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("thong-ke-nv.fxml"));
        double width = paneMain.getWidth();
        double height = paneMain.getHeight();
        paneMain.getChildren().clear();
        try {
            paneMain.getChildren().add(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        paneMain.setPrefSize(width, height);
    }

    @FXML
    protected void dangXuat() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Đăng xuất");
        alert.setContentText("Bạn có chắc chắn muốn đăng xuất?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            ConnectDB.disconnect();
            getData.nv = null;
            getData.hd = null;
            getData.kh = null;
            getData.lt = null;
            getData.ltkh = null;
            getData.dsve = null;
            getData.dscthd = null;
            System.exit(0);
        }
    }

    @FXML
    protected void openUserManual() {
        try {
            File file = new File(Objects.requireNonNull(getClass().getResource("/home.html")).toURI());
            Desktop.getDesktop().open(file);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void showKetCaPopup() {
        AnchorPane ketCaPane = null;
        try {
            ketCaPane = FXMLLoader.load(Objects.requireNonNull(TrangChu_GUI.class.getResource("ket-ca.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage ketCaStage = new Stage();
        ketCaStage.setTitle("Kết ca");
        ketCaStage.setScene(new Scene(ketCaPane));
        ketCaStage.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
        ketCaStage.show();
        ketCaStage.setOnCloseRequest(e1 -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận");
            alert.setHeaderText(null);
            alert.setContentText("Xác nhận thoát?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                ketCaStage.close();
            } else {
                e1.consume();
            }
        });
    }

    private void chooseFeatureButton(Button btnChosed) {
        List<Button> dsF = List.of(btnFVe, btnFQLHD, btnFKH, btnFBCTK, btnFCT, btnFKM);
        dsF.forEach(btn -> {
            if (btn.equals(btnChosed)) {
                btn.setStyle(btn.getStyle() + "-fx-background-color: lightgray;-fx-border-color: blue;");
            } else {
                btn.setStyle(style);
            }
        });
    }

    private void chooseFeatureButtonQL(Button btnChosed) {
        List<Button> dsF = List.of(btnQLNV, btnQLHD, btnQLKH, btnQLCT, btnQLBCTK, btnQLKM);
        dsF.forEach(btn -> {
            if (btn.equals(btnChosed)) {
                btn.setStyle(btn.getStyle() + "-fx-background-color: lightgray;-fx-border-color: blue;");
            } else {
                btn.setStyle(style);
            }
        });
    }

    private void chooseFeatureButtonLV2(Button btnChosed) {
        List<Button> dsF = List.of(btnBanVeGUI, btnDoiVeGUI, btnHuyVeGUI, btnTKDoanhThu, btnTKNV);
        dsF.forEach(btn -> {
            if (btn.equals(btnChosed)) {
                btn.setStyle(btn.getStyle() + "-fx-background-color: lightblue;-fx-border-color: white;");
            } else {
                btn.setStyle(styleLV2);
            }
        });
    }

    private void chooseFeatureButtonQLLV2(Button btnChosed) {
        List<Button> dsF = List.of(btnQLTKDoanhThu, btnQLTKNV);
        dsF.forEach(btn -> {
            if (btn.equals(btnChosed)) {
                btn.setStyle(btn.getStyle() + "-fx-background-color: lightblue;-fx-border-color: white;");
            } else {
                btn.setStyle(styleLV2);
            }
        });
    }
}
