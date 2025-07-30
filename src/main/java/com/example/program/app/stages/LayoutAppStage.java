package com.example.program.app.stages;

import com.example.program.util.StringConfig;
import com.example.program.util.widget.hotkey.HotKeyManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LayoutAppStage extends Application {
    private static AnchorPane page;

    private final Screen screen = Screen.getPrimary();
    private final Rectangle2D window = screen.getVisualBounds();
    public static Stage stage;

    @Override
    public void start(final Stage stage) {
        try {
            loadView(stage);
            maximizeWindow();
            Scene scene = showScene(stage);
            registerHotKeys(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadView(Stage stage) throws IOException {
        LayoutAppStage.stage = stage;
        FXMLLoader loader = new FXMLLoader(LayoutAppStage.class.getResource("/view/app_layout.fxml"), StringConfig.getPropertiesFromResource());
        page = loader.load();
    }

    private Scene showScene(Stage stage) {
        Scene scene = new Scene(page);
        stage.getIcons().add(new Image(Objects.requireNonNull(LayoutAppStage.class.getResourceAsStream("/img/oscilloscope/icon.png"))));
        stage.setScene(scene);
        stage.show();
        return scene;
    }

    public void maximizeWindow() {
        stage.setX(window.getMinX());
        stage.setY(window.getMinY());
        stage.setWidth(window.getWidth());
        stage.setHeight(window.getHeight());
    }

    private void registerHotKeys(Scene scene) {
        HotKeyManager.getInstance().registerHandler(scene);
    }
}
