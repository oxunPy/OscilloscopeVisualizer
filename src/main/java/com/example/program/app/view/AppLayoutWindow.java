package com.example.program.app.view;

import com.example.program.util.StringConfig;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AppLayoutWindow implements Initializable {
    @FXML
    private Label lbVersion;
    @FXML
    private Label lbHeader;
    @FXML
    private AnchorPane boxContent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbVersion.setText(StringConfig.formatValue("text.version", ""));

        MainWindow mainPage = new MainWindow();
        boxContent.getChildren().addAll(mainPage.getChildren());
    }
}
