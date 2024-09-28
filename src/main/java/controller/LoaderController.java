/*
 * @ (#) LoaderController.java       1.0     28/09/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import gui.TrangChu_GUI;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Blend;

/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   28/09/2024
 * version: 1.0
 */
public class LoaderController {
    @FXML
    private ProgressBar loader;

    @FXML
    protected void onLoading() {
        double progress = 0.0;
        while(loader.getProgress() < 1.0) {
            progress += 0.1;
            loader.setProgress(Math.round(progress * 100.0) / 100.0);
            loader.setClip(loader.getStyleableNode());
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        TrangChu_GUI trangChu = new TrangChu_GUI();
        try {
            trangChu.changeScene("dang-nhap.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
