package com.example.program.app.view;

import com.example.program.app.starter.LayoutAppStarter;
import com.example.program.app.starter.LauncherAppStarter;
import com.example.program.common.animation.FadeInLeftTransition;
import com.example.program.common.animation.FadeInRightTransition;
import com.example.program.common.animation.FadeInTransition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LaunchWindow implements Initializable {

    private enum Result {
        ERROR,
        SUCCESS
    }

    @FXML
    private Text lblWelcome;
    @FXML
    private Text lblRudy;
    @FXML
    private VBox vboxBottom;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        longStart();
    }

    private void longStart() {
        Service<Result> service = new Service<Result>() {
            @Override
            protected Task<Result> createTask() {
                return new Task<Result>() {
                    @Override
                    protected Result call() throws Exception {
                        Thread.sleep(3000);
                        return Result.SUCCESS;
                    }
                };
            }
        };
        service.start();
        service.setOnRunning(event -> animate());
        service.setOnSucceeded(event -> {
            if(service.getValue() == Result.SUCCESS) {
                close();
                new LayoutAppStarter().start(new Stage());
            }
        });
    }

    private void animate() {
        new FadeInLeftTransition(lblWelcome).play();
        new FadeInRightTransition(lblRudy).play();
        new FadeInTransition(vboxBottom).play();
    }

    private void close() {
        LauncherAppStarter.stage.close();
    }


}
