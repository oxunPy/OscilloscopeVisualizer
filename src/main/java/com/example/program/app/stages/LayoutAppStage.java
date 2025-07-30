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
            LayoutAppStage.stage = stage;
            FXMLLoader loader = new FXMLLoader(LayoutAppStage.class.getResource("/view/app_layout.fxml"), StringConfig.getPropertiesFromResource());
            page = loader.load();
            Scene scene = new Scene(page);

            stage.setX(window.getMinX());
            stage.setY(window.getMinY());
            stage.setWidth(window.getWidth());
            stage.setHeight(window.getHeight());

            stage.getIcons().add(new Image(Objects.requireNonNull(LayoutAppStage.class.getResourceAsStream("/img/oscilloscope/icon.png"))));

            stage.setScene(scene);
            stage.show();

            HotKeyManager.getInstance().registerHandler(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
