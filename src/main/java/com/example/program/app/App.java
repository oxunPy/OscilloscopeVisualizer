package com.example.program.app;

import com.example.program.util.widget.hotkey.HotKeyManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class App extends Application {
    public static Stage stage;
    public static Scene scene;

    private static AnchorPane page;

    private Screen screen = Screen.getPrimary();


    private Rectangle2D window = screen.getVisualBounds();


    @Override
    public void start(final Stage stage){
        try{
            App.stage = stage;
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/view/app.fxml"));
            page = loader.load();

            scene = new Scene(page);
            stage.initStyle(StageStyle.DECORATED);

            stage.setX(window.getMinX());
            stage.setY(window.getMinY());
            stage.setWidth(window.getWidth());
            stage.setHeight(window.getHeight());

            stage.getIcons().add(new Image(App.class.getResourceAsStream("/img/oscilloscope.png")));

            stage.setScene(scene);
            stage.show();

            HotKeyManager.getInstance().registerHandler(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
