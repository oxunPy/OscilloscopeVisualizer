package com.example.program.app.stages;

import com.example.program.util.StringConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;


public class LauncherAppStage extends Application {
    public static Stage stage;
    private AnchorPane page;

    @Override
    public void start(final Stage stage) throws Exception {
        stage.getIcons().addAll(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/img/oscilloscope/icon.png"))));
        show(new Stage());
    }

    public void show(final Stage stage) {
        try {
            LauncherAppStage.stage = stage;
            page = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/view/app_launch.fxml")), StringConfig.getPropertiesFromResource());
            Scene scene = new Scene(page);

            stage.initStyle(StageStyle.UNDECORATED);
            stage.setWidth(500);
            stage.setHeight(340);
            stage.setResizable(false);

            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            System.out.println("Login Initialization Error!" + ex);
        }
    }
}
