package com.example.program.app.controller;

import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.Message;
import com.example.program.util.StringConfig;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.io.IOException;
public class MainController extends NavigationScreen.Screen{
//    @FXML
//    private LineChart<Number, Number> lnChartHantek;
    @FXML
    private HBox hbChart;

    public MainController(){
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/main.fxml"), StringConfig.getPropertiesFromResource());

            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            Message.error(StringConfig.getValue("err.ui.load") + "\n" + ex);
        }
    }

    @Override
    public void onStart(){
        // Define X-axis
        NumberAxis xAxis = new NumberAxis(1960, 2020, 10);
        xAxis.setLabel("FREQUENCY");

        //Define Y-axis
        NumberAxis yAxis = new NumberAxis(-350, 350, 10);
        yAxis.setLabel("No.of schools");

        LineChart<Number, Number> lnChartHantek = new LineChart<>(xAxis, yAxis);
        lnChartHantek.setAnimated(true);
        lnChartHantek.setStyle("-fx-font-weight: bold;-fx-animated: true");
        lnChartHantek.setTitle("Hantek6022BE");
        HBox.setHgrow(lnChartHantek, Priority.ALWAYS);

        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
//        series1.setName("TIME");

        series1.getData().add(new XYChart.Data<>(1970, 15));
        series1.getData().add(new XYChart.Data<>(1980, 30));
        series1.getData().add(new XYChart.Data<>(1990, 60));
        series1.getData().add(new XYChart.Data<>(2000, 120));
        series1.getData().add(new XYChart.Data<>(2013, 240));
        series1.getData().add(new XYChart.Data<>(2014, 300));

        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
//        series2.setName("TIME");

        series2.getData().add(new XYChart.Data<>(1970, 20));
        series2.getData().add(new XYChart.Data<>(1980, 35));
        series2.getData().add(new XYChart.Data<>(1990, 65));
        series2.getData().add(new XYChart.Data<>(2000, 125));
        series2.getData().add(new XYChart.Data<>(2013, 245));
        series2.getData().add(new XYChart.Data<>(2014, 305));

        lnChartHantek.getData().addAll(series1, series2);

        hbChart.getChildren().add(lnChartHantek);

        lnChartHantek.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                System.out.println("oxun_123");
            }
        });
    }

    @Override
    public void onCreate() {

    }

}
