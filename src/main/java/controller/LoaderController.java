/*
 * @ (#) LoaderController.java       1.0     28/09/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import entity.NhanVien;
import gui.TrangChu_GUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Effect;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   28/09/2024
 * version: 1.0
 */
public class LoaderController implements Initializable {
    private TrangChu_GUI trangChu = new TrangChu_GUI();
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label lblLoading;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double progress = 0.0;
        progressBar.setProgress(progress);


    }
}
