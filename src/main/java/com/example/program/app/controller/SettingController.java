package com.example.program.app.controller;

import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.Message;
import com.example.program.util.StringConfig;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class SettingController extends NavigationScreen.Screen {

    public SettingController(){
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/setting.fxml"), StringConfig.getPropertiesFromResource());

            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            Message.error(StringConfig.getValue("err.ui.load") + "\n" + ex);
        }    }

    @Override
    public void onStart(){

    }

    @Override
    public void onCreate() {

    }


}
