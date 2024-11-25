package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;

import java.net.URL;
import java.util.ResourceBundle;

public class TabThongKeController implements Initializable {

    @FXML
    private Tab tab_doanhThu;

    @FXML
    private Tab tab_khac;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Parent doanhThuContent = FXMLLoader.load(getClass().getResource("/gui/thong-ke-doanh-thu.fxml"));
            Parent khacContent = FXMLLoader.load(getClass().getResource("/gui/thong-ke-nv.fxml"));

            tab_doanhThu.setContent(doanhThuContent);
            tab_khac.setContent(khacContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
