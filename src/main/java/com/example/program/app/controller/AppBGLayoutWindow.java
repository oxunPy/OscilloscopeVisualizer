package com.example.program.app.controller;

import com.example.program.app.LauncherApp;
import com.example.program.util.StringConfig;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AppBGLayoutWindow implements Initializable {
    @FXML
    private VBox boxNotes;
    @FXML
    private Label lbVersion;
    @FXML
    private AnchorPane boxContent;


    private static AppBGLayoutWindow instance;

    public VBox boxNotes(){
        return boxNotes;
    }

    public static AppBGLayoutWindow getInstance(){
        if(instance == null){
            instance = new AppBGLayoutWindow();
        }
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        lbVersion.setText(StringConfig.formatValue("text.version", ""));

        MainWindow mainPage = new MainWindow();
        boxContent.getChildren().addAll(mainPage.getChildren());
    }
}
