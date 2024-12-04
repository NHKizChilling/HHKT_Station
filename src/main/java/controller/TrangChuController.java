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
import entity.NhanVien;
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
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.stage.Modality;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
    private JFXButton btnFVe;
    @FXML
    private JFXButton btnFQLHD;
    @FXML
    private JFXButton btnFNV;
    @FXML
    private JFXButton btnFKH;
    @FXML
    private JFXButton btnFBCTK;
    @FXML
    private JFXButton btnFCT;
    @FXML
    private JFXButton btnUM;
    @FXML
    private JFXButton btnBanVeGUI;
    @FXML
    private JFXButton btnDoiVeGUI;
    @FXML
    private JFXButton btnHuyVeGUI;
    @FXML
    private JFXButton btnTraCuuVe;
    @FXML
    private Label lblTenNhanVien;
    @FXML
    private Label timer;
    @FXML
    private Label lblNgay;
    @FXML
    private FontAwesomeIcon iconDown;
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

    private NhanVien nv;
    private LocalDateTime gioMoCa;
    private double tienDauCa;


    private Ve_DAO ve_dao = new Ve_DAO();
    private HoaDon_DAO hd_dao = new HoaDon_DAO();
    private CT_LichTrinh_DAO ctlt_dao = new CT_LichTrinh_DAO();

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
        btnTraCuuVe.setOnMouseClicked(e -> chooseFeatureButtonLV2(btnTraCuuVe));
        btnFVe.setOnMouseClicked(e -> {
            try {
                ConnectDB.connect();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            new LichTrinh_DAO().updateTrangThaiCT(false);
            chooseFeatureButton(btnFVe);
            if (iconDown.getText().equals("\uF107")) {
                btnFVe.setStyle(btnFVe.getStyle() + "-fx-border-width: 2 0 0 0");
                iconDown.setIcon(FontAwesomeIcons.ANGLE_UP);
                acpVe.setVisible(true);
                List<Button> dsF = List.of(btnFQLHD, btnFNV, btnFKH, btnFBCTK, btnFCT, btnUM);
                List<FontAwesomeIcon> dsIcon = List.of(iconHD, iconNV, iconHK, iconCT, iconTK, iconHelp);
                dsF.forEach(btn -> {
                    btn.setLayoutY(btn.getLayoutY() + acpVe.getHeight());
                });
                dsIcon.forEach(icon -> {
                    icon.setLayoutY(icon.getLayoutY() + acpVe.getHeight());
                });
            } else {
                btnFVe.setStyle(btnFVe.getStyle() + "-fx-border-width: 0 0 2 0");
                iconDown.setIcon(FontAwesomeIcons.ANGLE_DOWN);
                acpVe.setVisible(false);
                //cho layout các button còn lại + 50
                List<Button> dsF = List.of(btnFQLHD, btnFNV, btnFKH, btnFBCTK, btnFCT, btnUM);
                List<FontAwesomeIcon> dsIcon = List.of(iconHD, iconNV, iconHK, iconCT, iconTK, iconHelp);
                dsF.forEach(btn -> {
                    btn.setLayoutY(btn.getLayoutY() - acpVe.getHeight());
                });
                dsIcon.forEach(icon -> {
                    icon.setLayoutY(icon.getLayoutY() - acpVe.getHeight());
                });
            }
        });
        btnFNV.setOnMouseClicked(e -> chooseFeatureButton(btnFNV));
        btnFQLHD.setOnMouseClicked(e -> chooseFeatureButton(btnFQLHD));
        btnFKH.setOnMouseClicked(e -> chooseFeatureButton(btnFKH));
        btnFBCTK.setOnMouseClicked(e -> chooseFeatureButton(btnFBCTK));
        btnFCT.setOnMouseClicked(e -> chooseFeatureButton(btnFCT));
        TrangChu_GUI.nv = getData.nv;
        lblTenNhanVien.setText("Chào, " + TrangChu_GUI.nv.getChucVu() + " " + TrangChu_GUI.nv.getTenNhanVien());
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
    protected void showTraCuuVeGUI() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("test.gui.fxml"));
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
    protected void onBtnTKClick() {
        FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("tab-thong-ke.fxml"));
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
    protected void showMoCaPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("mo-ca.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Mở Ca");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(paneMain.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();

            MoCaController controller = loader.getController();
            controller.setThongTin(nv, LocalDateTime.now());

            tienDauCa = controller.getTienDauCa();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void showKetCaPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("ket-ca.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Kết Ca");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(paneMain.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();

            KetCaController controller = loader.getController();
            controller.setInfo(nv, gioMoCa, tienDauCa);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            File file = new File(getClass().getResource("/home.html").toURI());
            Desktop.getDesktop().open(file);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private void chooseFeatureButton(Button btnChosed) {
        List<Button> dsF = List.of(btnFVe, btnFQLHD, btnFNV, btnFKH, btnFBCTK, btnFCT);
        dsF.forEach(btn -> {
            if (btn.equals(btnChosed)) {
                btn.setStyle(btn.getStyle() + "-fx-background-color: lightgray;-fx-border-color: blue;");
            } else {
                btn.setStyle(style);
            }
        });
    }

    private void chooseFeatureButtonLV2(Button btnChosed) {
        List<Button> dsF = List.of(btnBanVeGUI, btnDoiVeGUI, btnHuyVeGUI, btnTraCuuVe);
        dsF.forEach(btn -> {
            if (btn.equals(btnChosed)) {
                btn.setStyle(btn.getStyle() + "-fx-background-color: lightblue;-fx-border-color: white;");
            } else {
                btn.setStyle(styleLV2);
            }
        });
    }
}
