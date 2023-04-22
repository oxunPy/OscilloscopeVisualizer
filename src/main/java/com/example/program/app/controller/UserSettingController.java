package com.example.program.app.controller;

import com.example.program.app.AppManager;
import com.example.program.app.entity.OsciUserEntity;
import com.example.program.app.property.UserProperty;
import com.example.program.app.service.OsciUserService;
import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.Message;
import com.example.program.util.Note;
import com.example.program.util.StringConfig;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Locale;

public class UserSettingController extends NavigationScreen.Screen{
    @FXML
    private ComboBox<OsciUserEntity.UserType> cbUserType;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfPrintableName;
    @FXML
    private TextField tfLogin;
    @FXML
    private Button btnSave;

    private ObjectProperty<UserProperty> userObj = new SimpleObjectProperty<>();
    private OsciUserService osciUserService = new OsciUserService();
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
        UserProperty appUser = AppManager.getInstance().getLoggedUser();
        userObj.set(appUser);

        tfFirstName.setText(appUser.getFirstName());
        tfLastName.setText(appUser.getLastName());
        tfLogin.setText(appUser.getLogin());
        tfPrintableName.setText(appUser.getPrintableName());

        btnSave.setOnAction(event -> saveAppUser());

        cbUserType.setItems(FXCollections.observableArrayList(OsciUserEntity.UserType.SIMPLE, OsciUserEntity.UserType.ADMIN));
        cbUserType.setOnAction(actionEvent -> onDefaultUserTypeChange(cbUserType.getSelectionModel().getSelectedIndex()));
    }

    private void onDefaultUserTypeChange(int userTypeIdx){
        userObj.get().setUserType(OsciUserEntity.UserType.values()[userTypeIdx]);
    }

    private void saveAppUser(){
        Platform.runLater(() -> {
            try{
                osciUserService.editAppUser(userObj.get());
            }
            catch(Exception ex){
                Note.error(StringConfig.getValue("err.db.edit"));
            }
        });
    }
}
