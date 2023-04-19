package com.example.program.app.controller;

import com.example.program.app.App;
import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.Message;
import com.example.program.util.StringConfig;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.*;

public class MainController2 extends NavigationScreen.Screen{
    @FXML
    private BorderPane barcodeIconPane;

    double x, y = 0;

    public MainController2(){
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/invoice.fxml"), StringConfig.getPropertiesFromResource());

            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
            handleMousePosition();
        } catch (IOException ex) {
            Message.error(StringConfig.getValue("err.ui.load") + "\n" + ex);
        }
    }

    @Override
    public void onCreate() {
        barcodeIconPane.setCenter(GlyphsDude.createIcon(FontAwesomeIcon.LINE_CHART, "22px"));
    }

    @Override
    public void onStart(){
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
