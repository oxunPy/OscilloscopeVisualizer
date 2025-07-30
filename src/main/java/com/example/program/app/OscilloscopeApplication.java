package com.example.program.app;

import com.example.program.app.stages.LauncherAppStage;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class OscilloscopeApplication extends Application {
    public static void main(String[] args) throws Exception {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initStage(primaryStage);
        startNewLauncher(primaryStage);
    }

    void initStage(Stage stage) {
         stage.initStyle(StageStyle.DECORATED);
        stage.getIcons().addAll(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/img/oscilloscope/icon.png"))));
    }

    void startNewLauncher(Stage stage) throws Exception {
        LauncherAppStage launcher = new LauncherAppStage();
        launcher.show(stage);
    }
}
