package controller;

import dao.*;
import entity.ChoNgoi;
import entity.LichTrinh;
import entity.Toa;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class TraCuuVeController implements Initializable {
    @FXML
    private HBox grTrain;
    @FXML
    private Pane paneToa;
    @FXML
    private Label lblToa;

    private String idBtnChosen = null;
    private String chosedId = null;
    ChoNgoi_DAO cn_dao = new ChoNgoi_DAO();
    CT_LichTrinh_DAO ctlt_dao = new CT_LichTrinh_DAO();
    Toa_DAO toa_dao = new Toa_DAO();
    LoaiToa_DAO loaitoa_dao = new LoaiToa_DAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LichTrinh lt = new LichTrinh_DAO().traCuuDSLichTrinhTheoNgay(LocalDate.of(2024, 12, 1)).get(0);
        ArrayList<Toa> dstoa = toa_dao.getAllToaTheoChuyenTau(lt.getChuyenTau().getSoHieutau());
        Collections.reverse(dstoa);

        GridPane gridPane = new GridPane();
        paneToa.getChildren().add(gridPane);
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

            imageView.setOnMouseClicked(e -> {
                idBtnChosen = null;
                lblToa.setText("Toa " + toa.getSoSTToa() + ": " + loaitoa_dao.getLoaiToaTheoMa(toa.getLoaiToa().getMaLoaiToa()).getTenLoaiToa());
                paneImg.setStyle("-fx-background-color: lightgreen;-fx-background-radius: 5;");
                //đổi màu các imageView còn lại
                if (chosedId != null) {
                    ImageView imageView1 = (ImageView) grTrain.lookup("#" + chosedId);
                    imageView1.getParent().setStyle("-fx-background-color: skyblue;-fx-background-radius: 5;");
                }
                chosedId = imageView.getId();
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
                            for (ChoNgoi cn : dscn) {
                                if(cn.getSttCho() == seatNumber) {
                                    seatButton.setId(cn.getMaChoNgoi());
                                }
                            }
                            seatButton.setMinSize(30, 30); // Kích thước nút ghế

                            // Kiểm tra trạng thái của ghế
                            ChoNgoi choNgoi = cn_dao.getChoNgoiTheoToa(toa.getMaToa(), seatNumber);

                            if (!ctlt_dao.getTrangThaiCN(lt.getMaLichTrinh(), choNgoi.getMaChoNgoi())) {
                                seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                            } else {
                                seatButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                            }
                            // Thêm nút vào GridPane
                            // Với row = 2 thêm lối đi ở giữa (tăng thêm 1 để tạo khoảng trống)
                            gridPane.add(seatButton, col + (col >= 8 ? 2 : 0), row > 1 ? row + 2 : row);
                            seatButton.setOnMouseClicked(event -> {
                                if (seatButton.getStyle().contains("green")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được chọn");
                                    alert.show();
                                } else if (seatButton.getStyle().contains("red")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được đặt");
                                    alert.show();
                                } else {
                                    if (idBtnChosen != null) {
                                        Button btn = (Button) gridPane.lookup("#" + idBtnChosen);
                                        btn.setStyle("-fx-background-color: white; -fx-text-fill: black;");

                                    }
                                    idBtnChosen = seatButton.getId();
                                    seatButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
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
                            for (ChoNgoi cn : dscn) {
                                if(cn.getSttCho() == seatNumber) {
                                    seatButton.setId(cn.getMaChoNgoi());
                                }
                            }
                            seatButton.setMinSize(30, 30); // Kích thước nút ghế

                            // Kiểm tra trạng thái của ghế
                            ChoNgoi choNgoi = cn_dao.getChoNgoiTheoToa(toa.getMaToa(), seatNumber);

                            if (!ctlt_dao.getTrangThaiCN(lt.getMaLichTrinh(), choNgoi.getMaChoNgoi())) {
                                seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                            } else {
                                seatButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                            }

                            // Thêm nút vào GridPane
                            // Với row = 2 thêm lối đi ở giữa (tăng thêm 1 để tạo khoảng trống)
                            gridPane.add(seatButton, col + (col >= 8 ? 2 : 0), row > 1 ? row + 2 : row);
                            seatButton.setOnMouseClicked(event -> {
                                if (seatButton.getStyle().contains("green")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được chọn");
                                    alert.show();
                                } else if (seatButton.getStyle().contains("red")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được đặt");
                                    alert.show();
                                } else {
                                    if (idBtnChosen != null) {
                                        Button btn = (Button) gridPane.lookup("#" + idBtnChosen);
                                        btn.setStyle("-fx-background-color: white; -fx-text-fill: black;");

                                    }
                                    idBtnChosen = seatButton.getId();
                                    seatButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
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
                            for (ChoNgoi cn : dscn) {
                                if(cn.getSttCho() == seatNumber) {
                                    seatButton.setId(cn.getMaChoNgoi());
                                }
                            }
                            seatButton.setMinSize(30, 30);
                            ChoNgoi choNgoi = cn_dao.getChoNgoiTheoToa(toa.getMaToa(), seatNumber);

                            if (!ctlt_dao.getTrangThaiCN(lt.getMaLichTrinh(), choNgoi.getMaChoNgoi())) {
                                seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                            } else {
                                seatButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                            }

                            // Thêm nút vào GridPane (với col/2 để tạo khoảng trống giữa các khoang)
                            gridPane.add(seatButton, col + (col / 2) + 1, row + 1);
                            seatButton.setOnMouseClicked(event -> {
                                if (seatButton.getStyle().contains("green")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được chọn");
                                    alert.show();
                                } else if (seatButton.getStyle().contains("red")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được đặt");
                                    alert.show();
                                } else {
                                    if (idBtnChosen != null) {
                                        Button btn = (Button) gridPane.lookup("#" + idBtnChosen);
                                        btn.setStyle("-fx-background-color: white; -fx-text-fill: black;");

                                    }
                                    idBtnChosen = seatButton.getId();
                                    seatButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
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
                            for (ChoNgoi cn : dscn) {
                                if(cn.getSttCho() == seatNumber) {
                                    seatButton.setId(cn.getMaChoNgoi());
                                }
                            }
                            seatButton.setMinSize(30, 30);
                            // Kiểm tra trạng thái của ghế
                            ChoNgoi choNgoi = cn_dao.getChoNgoiTheoToa(toa.getMaToa(), seatNumber);

                            if (!ctlt_dao.getTrangThaiCN(lt.getMaLichTrinh(), choNgoi.getMaChoNgoi())) {
                                seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                            } else {
                                seatButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                            }

                            // Thêm nút vào GridPane (với col/2 để tạo khoảng trống giữa các khoang)
                            gridPane.add(seatButton, col + (col / 2) + 1, row + 1);
                            seatButton.setOnMouseClicked(event -> {
                                if (seatButton.getStyle().contains("green")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được chọn");
                                    alert.show();
                                } else if (seatButton.getStyle().contains("red")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được đặt");
                                    alert.show();
                                } else {
                                    if (idBtnChosen != null) {
                                        Button btn = (Button) gridPane.lookup("#" + idBtnChosen);
                                        btn.setStyle("-fx-background-color: white; -fx-text-fill: black;");

                                    }
                                    idBtnChosen = seatButton.getId();
                                    seatButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
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
                            for (ChoNgoi cn : dscn) {
                                if(cn.getSttCho() == seatNumber) {
                                    seatButton.setId(cn.getMaChoNgoi());
                                }
                            }
                            seatButton.setMinSize(30, 30); // Kích thước nút ghế
                            // Kiểm tra trạng thái của ghế
                            ChoNgoi choNgoi = cn_dao.getChoNgoiTheoToa(toa.getMaToa(), seatNumber);

                            if (!ctlt_dao.getTrangThaiCN(lt.getMaLichTrinh(), choNgoi.getMaChoNgoi())) {
                                seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                            } else {
                                seatButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                            }
                            gridPane.add(seatButton, col + 1, row + 1);
                            seatButton.setOnMouseClicked(event -> {
                                if (seatButton.getStyle().contains("green")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được chọn");
                                    alert.show();
                                } else if (seatButton.getStyle().contains("red")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Lỗi");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ghế đã được đặt");
                                    alert.show();
                                } else {
                                    if (idBtnChosen != null) {
                                        Button btn = (Button) gridPane.lookup("#" + idBtnChosen);
                                        btn.setStyle("-fx-background-color: white; -fx-text-fill: black;");

                                    }
                                    idBtnChosen = seatButton.getId();
                                    seatButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");                                }
                            });
                        }
                    }
                }
            });
            imageView.setCursor(Cursor.HAND);
            paneImg.getChildren().add(imageView);
            grTrain.getChildren().add(paneImg);
        }
        ImageView imageView1 = new ImageView("file:src/main/resources/img/train.png");
        imageView1.setFitHeight(25);
        imageView1.setFitWidth(50);
        grTrain.getChildren().add(imageView1);

    }
}
