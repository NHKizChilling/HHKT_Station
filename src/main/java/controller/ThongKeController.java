/*
 * @ (#) ThongKeController.java       1.0     01/10/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.util.ResourceBundle;

/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   01/10/2024
 * version: 1.0
 */
public class ThongKeController implements Initializable {
    @FXML
    private PieChart pieChart;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pieChart.getData().add(new PieChart.Data("Java", 50));
        pieChart.getData().add(new PieChart.Data("C++", 30));
        pieChart.getData().add(new PieChart.Data("Python", 20));
    }
}
