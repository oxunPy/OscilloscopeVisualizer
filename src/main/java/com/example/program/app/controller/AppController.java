package com.example.program.app.controller;

import com.example.program.app.App;
import com.example.program.app.AppManager;
import com.example.program.app.Login;
import com.example.program.app.service.SettingService;
import com.example.program.common.screen.Bundle;
import com.example.program.common.screen.NavigationScreen;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    @FXML
    private AnchorPane boxContent;
    @FXML
    private VBox boxNotes;

    private static AppController instance;

    private AppManager manager = AppManager.getInstance();

    private final SettingService settingService = new SettingService();

    private NavigationScreen navigationScreen;


    public NavigationScreen getNavigateScreen(){
        return navigationScreen;
    }

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

        navigationScreen = new NavigationScreen.Builder()
                .content(boxContent)
                .task(NavigationScreen.SINGLE_TOP_TASK)
                .welcomeScreen(new SettingController())
                .build();

        navigationScreen.startScreen(new NavigationScreen.Dansho(MainController.class, new Bundle()));
    }

    @FXML
    public void menuExit(){
        System.out.println("exiting !!!!");
    }

    public void exit(){
        App.stage.close();
        new Login().start(new Stage());
    }
}
