/*
 * @ (#) ThongTinVeController.java       1.0     15/10/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   15/10/2024
 * version: 1.0
 */
public class ThongTinVeController implements Initializable {
    @FXML
    private Button btnXacNhanTT;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnXacNhanTT.setOnAction(event -> {
            //Cho container disable
            //Gửi thông tin vé lên server
            //Nhận thông báo từ server

        });
    }
}
