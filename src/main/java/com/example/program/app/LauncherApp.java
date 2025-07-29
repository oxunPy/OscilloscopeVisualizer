package com.example.program.app;

import com.example.program.util.StringConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class LauncherApp extends Application {
    public static final Logger log = LoggerFactory.getLogger(LauncherApp.class);

    public static Stage stage;
    public static Stage launchStage;
    public static Scene scene;
    private AnchorPane page;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        LauncherApp.stage = stage;
        stage.getIcons().addAll(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/img/oscilloscope/icon.png"))));
        show(new Stage());
    }

    private void show(final Stage stage) {
        try {
            launchStage = stage;
            page = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/view/launch.fxml")), StringConfig.getPropertiesFromResource());
            scene = new Scene(page);

            stage.initStyle(StageStyle.UNDECORATED);
            stage.setWidth(500);
            stage.setHeight(340);
            stage.setResizable(false);

            stage.getIcons().addAll(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/img/oscilloscope/icon.png"))));

            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            System.out.println("Login Initialization Error!" + ex);
        }
    }
}
