package com.example.program.app.controller;

import com.example.program.app.AppManager;
import com.example.program.app.property.OsciLanguageProperty;
import com.example.program.app.property.SettingProperty;
import com.example.program.app.service.OsciLanguageService;
import com.example.program.app.service.OsciSettingService;
import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.Message;
import com.example.program.util.Note;
import com.example.program.util.StringConfig;
import com.example.program.util.widget.combo.LanguageComboboxConverter;
import com.example.program.util.widget.combo.LanguageComboboxItem;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

public class SettingController extends NavigationScreen.Screen {

    @FXML
    private TextField tfAppName;
    @FXML
    private TextField tfAppVersion;
    @FXML
    private TextField tfTechSupport;
    @FXML
    private TextField tfAuthorName;
    @FXML
    private TextField tfAuthorContact;
    @FXML
    private ComboBox<OsciLanguageProperty> cbDefaultLanguage;
    @FXML
    private Button btnSave;

    private OsciSettingService osciSettingService = new OsciSettingService();
    private OsciLanguageService osciLanguageService = new OsciLanguageService();

    private ObjectProperty<SettingProperty> settings = new SimpleObjectProperty<>();

    public SettingController(){
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/setting.fxml"), StringConfig.getPropertiesFromResource());

            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            Message.error(StringConfig.getValue("err.ui.load") + "\n" + ex);
        }
    }

    @Override
    public void onStart(){
        settings.set(AppManager.getInstance().getSetting());
    }

    @Override
    public void onCreate() {
        SettingProperty appSetting = AppManager.getInstance().getSetting();
        tfAppName.setText(appSetting.getAppName());
        tfAppVersion.setText(appSetting.getVersion());
        tfTechSupport.setText(appSetting.getTechSupport());
        tfAuthorContact.setText(appSetting.getAuthorContact());

        cbDefaultLanguage.setCellFactory(param -> new LanguageComboboxItem());
        cbDefaultLanguage.setConverter(new LanguageComboboxConverter());
        cbDefaultLanguage.setButtonCell(new LanguageComboboxItem());

        cbDefaultLanguage.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null) settings.get().setLanguageId(newValue.getId());
        }));
        btnSave.setOnAction(event -> save());

        tfAppName.textProperty().addListener(event -> settings.get().setAppName(tfAppName.getText()));

        comboLanguage();
    }

    private void save(){
        try{
           osciSettingService.edit(settings.get());
            Note.info(StringConfig.getValue("info.saved.successfully"));
        }
        catch(Exception ex){
            Note.error(StringConfig.getValue("label.error") + ": \n" + ex.getMessage());
        }
    }

    private void comboLanguage(){
        Platform.runLater(() -> {
            List<OsciLanguageProperty> list = osciLanguageService.list();
            cbDefaultLanguage.setItems(FXCollections.observableArrayList(list));
        });
    }


}
