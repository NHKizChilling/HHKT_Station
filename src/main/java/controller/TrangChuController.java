/*
 * @ (#) TrangChuController.java       1.0     28/09/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import gui.TrangChu_GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;

/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   28/09/2024
 * version: 1.0
 */
public class TrangChuController {
    @FXML
    protected void onDangNhapClick() throws Exception {
        TrangChu_GUI trangChu = new TrangChu_GUI();
        trangChu.changeScene("loader.fxml");
    }
}
