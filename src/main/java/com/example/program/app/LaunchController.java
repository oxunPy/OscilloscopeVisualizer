package com.example.program.app;

import com.example.program.app.entity.OsciUserEntity;
import com.example.program.app.service.OsciUserService;
import com.example.program.common.animation.FadeInLeftTransition;
import com.example.program.common.animation.FadeInRightTransition;
import com.example.program.common.animation.FadeInTransition;
import com.example.program.util.StringConfig;
import com.example.program.util.persistence.HibernateUtil;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class LaunchController implements Initializable {


    private OsciUserService userService = new OsciUserService();

    private enum Result {
        NOT_AVAILABLE_DATABASE,
        USER_NOT_EXISTS,
        TERMINAL_NOT_EXIST,

        AUTH_NOT_SET,
        SUCCESSFULLY
    }

    @FXML
    private Text lblWelcome;
    @FXML
    private Text lblRudy;
    @FXML
    private VBox vboxBottom;
    @FXML
    private Label lblClose;


    @FXML
    void minimize(ActionEvent event) {
        Login.stage.setIconified(true);
    }

    @FXML
    void close(ActionEvent event) {
        Login.stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        longStart();
        lblClose.setOnMouseClicked((MouseEvent event) -> {
            Platform.exit();
            System.exit(0);
        });
        // TODO
    }

    private void longStart() {

        Service<Result> service = new Service<Result>() {
            @Override
            protected Task<Result> createTask() {
                return new Task<Result>() {
                    @Override
                    protected Result call() throws Exception {

                        if (!AuthenticationUtil.isDatabaseAvailable(Launch.properties.getStr("db-host"))) {
                            return Result.NOT_AVAILABLE_DATABASE;
                        }

                        // 2-STEP run SQL Functions
                        HibernateUtil.executeSQLQuery("sql/function.sql", Arrays.asList("getSecretUser", "insertSecretUser") );

                        //  3-STEP
                        OsciUserEntity adminUser = AuthenticationUtil.getAdminUser();
                        if (adminUser == null) {
                            return Result.USER_NOT_EXISTS;
                        }

                        // 4-STEP
                        if(!userService.getAuthSet()){
                            return Result.AUTH_NOT_SET;
                        }

                        //  5-STEP
//                        AuthenticationData.instance.log(this.getClass(), StringConfig.getValue("auth.terminal.activeInServerAndLocale"));

                        return Result.SUCCESSFULLY;
                    }
                };
            }
        };
        service.start();
        service.setOnRunning((WorkerStateEvent event) -> {
            new FadeInLeftTransition(lblWelcome).play();
            new FadeInRightTransition(lblRudy).play();
            new FadeInTransition(vboxBottom).play();
        });
        service.setOnSucceeded((WorkerStateEvent event) -> {
            switch (service.getValue()) {
                case SUCCESSFULLY:
                    close();
                    new Login().start(new Stage());
                    break;
                case NOT_AVAILABLE_DATABASE:
                    Launch.scene = new Scene(new AuthenticationWizard(Launch.stage, 0), 400, 300);
                    Launch.stage.setScene(Launch.scene);
                    close();
                    Launch.stage.show();
                    break;
                case USER_NOT_EXISTS:
                    AuthenticationUtil.initDefaults();
                    AuthenticationData.instance.log(this.getClass(), StringConfig.getValue("auth.user.noExistInLocale"));
                    longStart();
                    break;

                case AUTH_NOT_SET:
                    AuthenticationData.instance.log(this.getClass(), StringConfig.getValue("auth.notSet"));
                    Launch.scene = new Scene(new AuthenticationWizard(Launch.stage, 1), 400, 300);
                    Launch.stage.setScene(Launch.scene);
                    Launch.stage.show();
                    close();
                default:
                    close();
            }

        });
    }

    private void close() {
        Launch.launchStage.close();
    }


}
