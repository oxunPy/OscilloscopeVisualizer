package com.example.program.app.controller;

import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.Note;
import com.example.program.util.StringConfig;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import java.io.IOException;

public class ToolAddEditController extends NavigationScreen.Screen{

    @FXML
    private Button btnBack;

    public ToolAddEditController(){
        try{
            FXMLLoader fxml = new FXMLLoader(this.getClass().getResource("/view/add_tool.fxml"), StringConfig.getPropertiesFromResource());
            fxml.setController(this);
            fxml.setRoot(this);
            fxml.load();
        } catch (IOException e) {
            Note.error(StringConfig.getValue("err.ui.load"));
        }
    }

    @Override
    public void onStart(){
        btnBack.setOnMouseClicked(event -> finish(RESULT_OK));
    }

    @Override
    public void onCreate() {

    }
}
