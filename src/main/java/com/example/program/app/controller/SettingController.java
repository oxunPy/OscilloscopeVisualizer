package com.example.program.app.controller;

import com.example.program.app.App;
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
import javafx.beans.binding.LongBinding;
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
import java.util.Objects;

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

        settings.get().appNameProperty().bind(tfAppName.textProperty());
        settings.get().versionProperty().bind(tfAppVersion.textProperty());
        settings.get().techSupportProperty().bind(tfTechSupport.textProperty());
        settings.get().authorNameProperty().bind(tfAuthorName.textProperty());
        settings.get().authorContactProperty().bind(tfAuthorContact.textProperty());
    }

    @Override
    public void onCreate() {
        SettingProperty appSetting = AppManager.getInstance().getSetting();
        tfAppName.setText(appSetting.getAppName());
        tfAppVersion.setText(appSetting.getVersion());
        tfTechSupport.setText(appSetting.getTechSupport());
        tfAuthorContact.setText(appSetting.getAuthorContact());
        tfAuthorName.setText(appSetting.getAuthorName());

        cbDefaultLanguage.setCellFactory(param -> new LanguageComboboxItem());
        cbDefaultLanguage.setConverter(new LanguageComboboxConverter());
        cbDefaultLanguage.setButtonCell(new LanguageComboboxItem());

        cbDefaultLanguage.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null) settings.get().setLanguageId(newValue.getId());
        }));
        btnSave.setOnAction(event -> save());

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
            // select applicationSetting lang-value
            cbDefaultLanguage.setValue(cbDefaultLanguage.getItems().stream().filter(l -> Objects.equals(l.getId(), AppManager.getInstance().getSetting().getLanguageId())).findFirst().orElse(null));
        });
    }


}
