/*
 * @ (#) TrangChuController.java       1.0     28/09/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import connectdb.ConnectDB;
import dao.*;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.Ve;
import gui.TrangChu_GUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.glyphfont.FontAwesome;

import java.awt.*;
import java.awt.image.BufferedImage;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private Button btnFVe;
    @FXML
    private Button btnFQLHD;
    @FXML
    private Button btnFNV;
    @FXML
    private Button btnFHK;
    @FXML
    private Button btnFBCTK;
    @FXML
    private Button btnFCT;
    @FXML
    private Label lblTenNhanVien;
    @FXML
    private Label timer;
    @FXML
    private Label lblNgay;

    private Ve_DAO ve_dao = new Ve_DAO();
    private HoaDon_DAO hd_dao = new HoaDon_DAO();
    private CT_LichTrinh_DAO ctlt_dao = new CT_LichTrinh_DAO();

    Time time = new Time(DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
    private String style = null;
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
        btnFVe.setOnMouseClicked(e -> {
            try {
                ConnectDB.connect();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            new LichTrinh_DAO().updateTrangThaiCT(false);
            chooseFeatureButton(btnFVe);
        });
        btnFNV.setOnMouseClicked(e -> chooseFeatureButton(btnFNV));
        btnFQLHD.setOnMouseClicked(e -> chooseFeatureButton(btnFQLHD));
        btnFHK.setOnMouseClicked(e -> chooseFeatureButton(btnFHK));
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
    protected  void showNhanVienGUI() {
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


        primaryStage.setOnCloseRequest(e -> {
            timer.shutdown();
            webcam.close();
        });
    }

    private String decodeBarcode(BufferedImage image) {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        // Chỉ định định dạng mã vạch là Code 128, EAN-13, UPC-A hoặc QR Code
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(
                BarcodeFormat.CODE_128,
                BarcodeFormat.QR_CODE
        ));

        try {
            Result result = new MultiFormatReader().decode(bitmap, hints);
            return result.getText();
        } catch (NotFoundException e) {
            // Không tìm thấy mã vạch trong ảnh
            return null;
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
        try {
            FXMLLoader loader = new FXMLLoader(TrangChu_GUI.class.getResource("thong-ke.fxml"));
            double width = paneMain.getWidth();
            double height = paneMain.getHeight();
            paneMain.getChildren().clear();
            paneMain.getChildren().add(loader.load());
            paneMain.setPrefSize(width, height);

        } catch (Exception e) {
            throw new RuntimeException(e);
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
            getData.hk = null;
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
        List<Button> dsF = List.of(btnFVe, btnFNV, btnFHK, btnFBCTK, btnFCT);
        dsF.forEach(btn -> {
            if (btn.equals(btnChosed)) {
                btn.setStyle(btn.getStyle() + "-fx-background-color: skyblue;-fx-border-color: blue;");
            } else {
                btn.setStyle(style);
            }
        });
    }
}
