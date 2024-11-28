package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TabThongKeController {

//    @FXML
//    private Tab tab_doanhThu;
//
//    @FXML
//    private Tab tab_khac;
//
//    @FXML
//    private AnchorPane content1;
//
//    @FXML
//    private AnchorPane content2;
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        loadTabContent(tab_doanhThu, content1, "/gui/thong-ke-doanh-thu.fxml");
//        loadTabContent(tab_khac, content2, "/gui/thong-ke-khac.fxml");
//    }
//
//    private void loadTabContent(Tab tab, AnchorPane anchorPane, String fxmlPath) {
//        tab.setOnSelectionChanged(event -> {
//            if (tab.isSelected()) {
//                try {
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
//                    Parent content = loader.load();
//                    anchorPane.getChildren().setAll(content);
//                    tab.setContent(anchorPane);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    throw new RuntimeException("Failed to load " + fxmlPath, e);
//                }
//            }
//        });
//    }
}