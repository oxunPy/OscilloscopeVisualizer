package com.example.program.app.controller;

import com.example.program.app.App;
import com.example.program.app.AppManager;
import com.example.program.app.Launch;
import com.example.program.app.Login;
import com.example.program.app.service.OsciSettingService;
import com.example.program.common.navigation.Navigation;
import com.example.program.common.screen.Bundle;
import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.Message;
import com.example.program.util.StringConfig;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import static com.example.program.util.Dialog.*;

public class AppController implements Initializable {
    @FXML
    private AnchorPane boxContent;
    @FXML
    private VBox boxNotes;
    @FXML
    private VBox boxMenu;
    @FXML
    private Button btnInfo;
    private static AppController instance;

    private AppManager manager = AppManager.getInstance();

    private final OsciSettingService settingService = new OsciSettingService();

    private static NavigationScreen navigationScreen;

    double x, y = 0;
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

    private void initBase(){
        btnInfo.setText(StringConfig.getPropertiesFromResource().getString("label.info2"));
        btnInfo.getStyleClass().add("menu-info");
        btnInfo.setOnMouseClicked(e -> menuInfo());
    }


    void menuInfo() {
        Message.info(Arrays.asList(
                        new TextObject(StringConfig.formatValue("text.version", "")),
                        new TextObject(Launch.properties.getStr("app-version") + "\n\n", "-fx-font-weight : bold;"),

                        new TextObject(StringConfig.formatValue("text.developers", "")),
                        new TextObject(Launch.properties.getStr("app.developers") + "\n\n", "-fx-font-weight: bold;"),

                        new TextObject("Telegram: "),
                        new TextObject(Launch.properties.getStr("link-telegram"), "", "link", Launch.properties.getStr("link-telegram")),

                        new TextObject("\n" + StringConfig.formatValue("text.technicalSupport", "")),
                        new TextObject(Launch.properties.getStr("contact-technical-support"), "-fx-font-weight : bold;")),
                Launch.properties.getStr("app-name"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        navigationScreen = new NavigationScreen.Builder()
                .content(boxContent)
                .task(NavigationScreen.SINGLE_TOP_TASK)
                .welcomeScreen(new SettingController())
                .build();

        createMenu();

        initBase();
    }

    public void createMenu(){
        boxMenu.getChildren().clear();
        Navigation navigation = getNavigationMenu();
        navigation.update(boxMenu);
    }

    private Navigation getNavigationMenu(){
        return new Navigation(this, AppManager.getInstance())
                .singleOpen()
                .add(Navigation.item()
                        .select()
                        .title(StringConfig.getValue("label.menu.main"))
                        .iconStyleClass("menu-home")
                        .addChild(Navigation.item()
                                .title(StringConfig.getValue("label.graph.visualizer"))
                                .screen(MainController.class, Bundle.create()))
                        .addChild(Navigation.item()
                                .title(StringConfig.getValue("label.compare.graphs")))
                        .addChild(Navigation.item()
                                .title(StringConfig.getValue("label.predict")))
                )
                .add(Navigation.item()
                        .title(StringConfig.getValue("label.menu.file"))
                        .iconStyleClass("menu-file")
                        .addChild(Navigation.item()
                                .title(StringConfig.getValue("label.add.file"))
                                .screen(FileUploadController.class, Bundle.create()))
                )
                .add(Navigation.item()
                        .title(StringConfig.getValue("label.menu.records"))
                        .iconStyleClass("menu-record")
                        .addChild(Navigation.item()
                                .title(StringConfig.getValue("label.data.tools"))
                                .screen(ToolListController.class, Bundle.create()))
                        .addChild(Navigation.item()
                                .title(StringConfig.getValue("label.data.oscilloscope"))
                                .screen(OsciDataController.class, Bundle.create()))
                )
                .add(Navigation.item()
                        .title(StringConfig.getValue("label.menu.setting"))
                        .iconStyleClass("menu-setting")
                        .addChild(Navigation.item()
                                .title(StringConfig.getValue("label.manage.user"))
                                .screen(UserSettingController.class, Bundle.create()))
                        .addChild(Navigation.item()
                                .title(StringConfig.getValue("label.manage.setting"))
                                .screen(SettingController.class, Bundle.create()))
                        .addChild(Navigation.item()
                                .title(StringConfig.getValue("label.manage.appInfo")))
                );
    }

    @FXML
    public void menuExit(){
        System.out.println("exiting !!!!");
    }

    @FXML
    public void menuDashboard(){
        System.out.println("menu Dashboard");
    }

    public void exit(){
        App.stage.close();
        new Login().start(new Stage());
    }

    public void startScreen(NavigationScreen.Dansho dansho){
        if(navigationScreen != null){
            navigationScreen.startScreen(dansho);
        }
    }
}
