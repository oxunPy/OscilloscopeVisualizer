package com.example.program.app.controller;

import com.example.program.app.App;
import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.Message;
import com.example.program.util.StringConfig;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import org.apache.commons.lang.math.NumberUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainController extends NavigationScreen.Screen{

    double x, y = 0;

    public MainController(){
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/main.fxml"), StringConfig.getPropertiesFromResource());

            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
            handleMousePosition();
        } catch (IOException ex) {
            Message.error(StringConfig.getValue("err.ui.load") + "\n" + ex);
        }
    }

    @Override
    public void onStart(){
        try {
            drawGraph(readFromFile(new FileInputStream("C:/Users/oxun/Desktop/data.txt")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawGraph(List<Double> data){
        // Define X-axis
        NumberAxis xAxis = new NumberAxis(0, 1000, 50);
        xAxis.setAnimated(true);

        //Define Y-axis
        NumberAxis yAxis = new NumberAxis(-3, 3, 1);
        yAxis.setAnimated(true);

        // data for drawing graph
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for(int i = 0; i < 1000; i++){
            series.getData().add(new XYChart.Data<>(i, data.get(i)));
        }


        LineChart<Number, Number> lnChartHantek = createSimpleLineChart(xAxis, yAxis, "Сравнение графиков!");
        HBox.setHgrow(lnChartHantek, Priority.ALWAYS);
        lnChartHantek.getData().add(series);

//        vbCharts.getChildren().add(lnChartHantek);
        VBox.setVgrow(lnChartHantek, Priority.ALWAYS);

/*        lnChartHantek.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                System.out.println("oxun_123");
            }
        });*/

        // >>>  forecast-line-chart
        LineChart<Number, Number> lnChartForecast = createSimpleLineChart(xAxis, yAxis, "График прогнозирования!");
        lnChartForecast.getData().add(new XYChart.Series<>(series.getData()));
        lnChartForecast.setStyle("-fx-border-color: red;");
        HBox.setHgrow(lnChartHantek, Priority.ALWAYS);
//        hbForecast.getChildren().add(lnChartForecast);
        // <<<
    }

    private List<Double> readFromFile(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        List<Double> osciDataFileList = new ArrayList<>();
        try {
            String line;
            osciDataFileList = new ArrayList<>();
            while ((line = nextLine(reader)) != null) {
                if(isValidNumber(line)){
                    osciDataFileList.add(Double.parseDouble(line));
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("SQL file could not be read", e);
        }
        return osciDataFileList;
    }

    private String nextLine(BufferedReader reader) throws IOException {
        String line;
        do {
            line = reader.readLine();
        } while (line != null && !isValidLine(line) );
        return line;
    }

    private boolean isValidLine(String line) {
        return !line.trim().isEmpty();
    }

    private boolean isValidNumber(String line){
        return isValidLine(line) && NumberUtils.isNumber(line);
    }

    @Override
    public void onCreate() {
//        fileIconPane.setCenter(GlyphsDude.createIcon(FontAwesomeIcon.FILE, "22px"));
    }

    private LineChart<Number, Number> createSimpleLineChart(NumberAxis xAxis, NumberAxis yAxis, String title){
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(true);
        chart.setStyle("-fx-font-weight: bold;");
        chart.setCreateSymbols(false);
        chart.setTitle(title);
        return chart;
    }


    void handleMousePosition(){
        this.setOnMouseClicked(event -> {
            x = event.getX();
            y = event.getY();
        });

        this.setOnMouseDragged(event -> {
            App.stage.setX(event.getScreenX() - x);
            App.stage.setY(event.getScreenY() - y);
        });
    }


}
