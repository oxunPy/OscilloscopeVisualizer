package com.example.program.app.controller;

import com.example.program.app.App;
import com.example.program.app.AppManager;
import com.example.program.app.Login;
import com.example.program.app.property.DeviceService;
import com.example.program.app.property.UserProperty;
import com.example.program.app.service.LoginService;
import com.example.program.app.service.SettingService;
import com.example.program.app.service.UserService;
import com.example.program.common.animation.FadeInLeftTransition;
import com.example.program.common.animation.FadeInRightTransition;
import com.example.program.util.Dialog;
import com.example.program.util.Field;
import com.example.program.util.Message;
import com.example.program.util.StringConfig;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private LoginService loginService = new LoginService();
    private SettingService settingService = new SettingService();
    private UserService userService = new UserService();
    private DeviceService deviceService = new DeviceService();

    @FXML
    private Label txtUser;
    @FXML
    private Label lblPassword;
    @FXML
    private Label lbWelcome;
    @FXML
    private TextField txtLogin;
    @FXML
    private PasswordField txtPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    void login(){

        if (!txtLogin.getText().isEmpty()) {
            Field.errorLogin(txtLogin);
            return;
        }

        if(txtPassword.getText().length() < 4){
            Message.error("At least 4 numbers!!!");
            Field.error(txtPassword, "at least 4 numbers");
            return;
        }

        String login = txtLogin.getText();
        String password = txtPassword.getText();

        if (loginService.authenticatePassword(login, password)) {
            UserProperty user = loginService.getLoggedUser(login);
            finishLogin(user);
        } else {
            Message.error("Incorrect password, check the values!");
            Field.errorLogin(txtLogin);
        }
    }

    private void finishLogin(UserProperty user) {
        AppManager.Builder builder = new AppManager.Builder();
        builder.user(userService.userProperty(user.getId()))
                .setting(settingService.getApplicationSetting())
                .device(deviceService.getDevice())
                .build();

        Stage appStage = new Stage();
        appStage.setOnCloseRequest(event -> {
//            System.out.println("Close button click");
            Dialog.Answer response = Message.confirm(StringConfig.getValue("application.close.request") + " ?");

            if (response == Dialog.Answer.NO) {
                event.consume();
            }
        });
        new App().start(appStage);
        Login.stage.close();

    }

    private void animate(){
        new FadeInRightTransition(lbWelcome).play();
        new FadeInLeftTransition(txtUser).play();
        new FadeInLeftTransition(txtPassword).play();
    }
}
