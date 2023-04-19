package com.example.program.app;

import com.example.program.app.service.OsciSettingService;
import com.example.program.app.service.OsciUserService;
import com.example.program.util.StringConfig;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class Login extends Application {

    public static Stage stage;
    private Scene scene;
    private AnchorPane page;

    private Screen screen = Screen.getPrimary();
    private final Rectangle2D windows = screen.getVisualBounds();

    private final OsciSettingService settingService = new OsciSettingService();
    private final OsciUserService userService = new OsciUserService();
    public LoginWindowType currentLoginType = null;

    public enum LoginWindowType {
        LOGIN_PASSWORD_WINDOW, PIN_WINDOW
    }

    @Override
    public void start(final Stage stage) {
        try {
            //start login
            show(stage);
        } catch (Exception ex) {
            System.out.println("Login Initialization Error!" + ex);
        }
    }

    private void show(final Stage stage) {
        try {
            this.stage = stage;

            stage.initStyle(StageStyle.DECORATED);
            stage.setResizable(false);
            page = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/view/login.fxml")), StringConfig.getPropertiesFromResource());
            this.scene = new Scene(page);

            stage.getIcons().addAll(new Image(this.getClass().getResourceAsStream("/img/oscilloscope/oscilloscope.png")));

            stage.setScene(this.scene);
            stage.show();
        } catch (Exception ex) {
            System.out.println("Login Initialization Error!" + ex);
        }
    }

    private void alert() {
        Alert alert = new Alert(Alert.AlertType.ERROR, StringConfig.getValue("text.theApplicationHasFailedToStart"));
        if (alert.showAndWait().get() == ButtonType.OK) exit();
    }

    private void exit() {
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
