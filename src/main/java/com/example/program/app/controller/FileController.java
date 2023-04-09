package com.example.program.app.controller;

import com.example.program.common.screen.Bundle;
import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.Message;
import com.example.program.util.Resize;
import com.example.program.util.StringConfig;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;

public class FileController extends NavigationScreen.Screen{

    @FXML
    private ImageView Exit;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;
    @FXML
    private Button btnOpenFileChooser;

    @FXML
    private AnchorPane slider;

    @FXML
    private AnchorPane centerView;

    @FXML
    private Button btnBack;

    public FileController(){
        try{
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/file.fxml"), StringConfig.getPropertiesFromResource());
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
        Message.error(StringConfig.getValue("err.ui.load") + "\n" + ex);
    }
    }

    @Override
    public void onStart(){

        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });
        slider.setTranslateX(-176);
        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-176);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(false);
                MenuClose.setVisible(true);
            });
        });

        MenuClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-176);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(true);
                MenuClose.setVisible(false);
            });
        });
    }


    @Override
    public void onCreate() {
        btnOpenFileChooser.setOnMouseClicked(event -> {
            AppController.getInstance().getNavigateScreen().startScreen(new NavigationScreen.Dansho(FileUploadController.class, new Bundle()));
            try {
                changeContent(centerView, FileUploadController.class.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        btnBack.setOnMouseClicked(event -> {
            AppController.getInstance().getNavigateScreen().startScreen(new NavigationScreen.Dansho(MainController.class, new Bundle()));
        });

    }

    private void changeContent(AnchorPane content, AnchorPane child) {
        if (child == null)
            return;
        content.getChildren().clear();
        content.getChildren().add(child);
        Resize.margin(child, 0);
    }
}
