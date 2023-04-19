package com.example.program.app.controller;

import com.example.program.app.AppManager;
import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.Message;
import com.example.program.util.StringConfig;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Locale;

public class UserSettingController extends NavigationScreen.Screen{

    public UserSettingController(){
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/userSetting.fxml"), StringConfig.getPropertiesFromResource(new Locale(AppManager.getInstance().getSetting().getLanguageCode())));

            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            Message.error(StringConfig.getValue("err.ui.load") + "\n" + ex);
        }
    }


    @Override
    public void onCreate() {

    }
}
