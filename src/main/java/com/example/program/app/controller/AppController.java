package com.example.program.app.controller;

import com.example.program.app.service.SettingService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    private static AppController instance;

    private final SettingService settingService = new SettingService();

    @FXML
    private AnchorPane boxContent;
    @FXML
    private VBox boxNotes;
    @FXML
    private Button btnLogin;

    public VBox boxNotes(){
        return boxNotes;
    }

    public static AppController getInstance(){
        if(instance == null){
            instance = new AppController();
        }
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void menuExit(){
        System.out.println("exiting !!!!");
    }
}
