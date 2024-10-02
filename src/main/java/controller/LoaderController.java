/*
 * @ (#) LoaderController.java       1.0     28/09/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import entity.NhanVien;
import gui.TrangChu_GUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Effect;

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
    private ProgressBar loader;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loader.setStyle("-fx-arrows-visible: true");
        double progress = 0.0;
        while(loader.getProgress() < 1.0) {
            progress += 0.1;
            loader.setProgress(Math.round(progress * 100.0) / 100.0);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
