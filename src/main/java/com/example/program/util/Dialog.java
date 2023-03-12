package com.example.program.util;

import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Create dialog windows
 */
public class Dialog {

    private static final Screen screen = Screen.getPrimary();
    private static final Rectangle2D windows = screen.getVisualBounds();
    private static MyDialog dialog;
    private MyDialog myBarcodeDialog;
    private static Answer answer = Answer.CANCEL;
    private static Button printBtn = null;
    private static Button exit = null;

    private static Label icon(String type) {

        Label label = new Label();

        switch (type) {
            case "INFO":
                label.getStyleClass().add("img-dialog");
                break;
            case "ERROR":
                label.getStyleClass().add("img-dialog-error");
                break;
            case "ALERT":
                label.getStyleClass().add("img-dialog-alert");
                break;
            case "CONFIRM":
                label.getStyleClass().add("img-dialog-confirm");
                break;
            default:
                label.getStyleClass().add("img-dialog");
                break;
        }
        return label;
    }

    /**
     * Formats text (title and description) of the message
     */
    private static VBox text(String title, String message) {
        Label lblTitle = new Label(title);
        lblTitle.getStyleClass().add("title-dialogs");

        Label lblMessage = new Label(message);
        lblMessage.getStyleClass().add("message-dialogs");

        VBox box = new VBox();
        box.getChildren().addAll(lblTitle, lblMessage);
        box.getStyleClass().add("box-message");

        return box;
    }

    /**
     * Add actions like Yes, No, Cancel, Ok
     */
    private static HBox actions() {
        Button ok = new Button("OK");
        ok.setOnAction((ActionEvent e) -> {
            dialog.close();
            answer = Answer.YES;
        });
        ok.getStyleClass().add("bt-ok");

        HBox box = new HBox();
        box.getStyleClass().add("box-action-dialog");
        box.getChildren().addAll(ok);

        return box;
    }

    /**
     * Add title and alert description to the message box
     */
    public static Answer messages(String type, String title, String message) {
        box(icon(type), text(title, message), actions());

        return answer;
    }

    /**
     * Allows the user to return a reply to the message according to the message type such as: OK, YES, NO and CANCEL
     */
    public static Answer messageConfirm(String title, String message) {
        Button yes = new Button(StringConfig.getValue("button.yes"));
        yes.setOnAction((ActionEvent e) -> {
            dialog.close();
            answer = Answer.YES;
        });
        yes.getStyleClass().add("bt-yes");

        Button no = new Button(StringConfig.getValue("button.no"));
        no.setOnAction((ActionEvent e) -> {
            dialog.close();
            answer = Answer.NO;
        });
        no.getStyleClass().add("bt-no");

        HBox box = new HBox();
        box.getStyleClass().add("box-action-dialog");
        box.getChildren().addAll(yes, no);

        box(icon("CONFIRM"), text(title, message), box);

        return answer;
    }

    public static Answer messageNotSelected(String title, String message) {
        Button ok = new Button(StringConfig.getValue("button.ok"));
        ok.setOnAction((ActionEvent e) -> {
            dialog.close();
            answer = Answer.OK;
        });
        ok.getStyleClass().add("bt-ok");

        HBox box = new HBox();
        box.getStyleClass().add("box-action-dialog");
        box.getChildren().add(ok);

        box(icon("ALERT"), text(title, message), box);

        return answer;
    }

    /**
     * Main Box that adds and formats the icon, message and action to the message box
     */
    private static void box(Label icon, VBox message, HBox action) {
        GridPane grid = new GridPane();
        grid.add(icon, 0, 0);
        grid.add(message, 1, 0);
        grid.add(action, 1, 1);
        grid.getStyleClass().add("box-grid");
        grid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setHgrow(Priority.NEVER);
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(columnConstraints1, columnConstraints2);

        HBox boxCentral = new HBox(grid);
        boxCentral.getStyleClass().add("box-msg");
        Resize.margin(boxCentral, 0);

        AnchorPane boxMain = new AnchorPane(boxCentral);
        boxMain.setStyle("-fx-background-color: rgba(0, 0, 0, 0.0);");
        Resize.margin(boxMain, 0);

        boxDialog(boxMain);
    }


    /**
     * Main message box and added the main screen
     */
    private static void boxDialog(AnchorPane pane) {
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("css/dialog.css");
        scene.setFill(Color.TRANSPARENT);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                dialog.close();
                answer = Answer.YES;
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                dialog.close();
                answer = Answer.CANCEL;
            }
        });
        dialog = new MyDialog(new Stage(), scene);
        dialog.display();
    }

    public enum Answer {
        NO, YES, OK, CANCEL
    }

    /**
     * Creates and formats the main stage that will display the dialog message
     */
    private static class MyDialog extends Stage {

        public MyDialog(Stage stage, Scene scene) {
            initStyle(StageStyle.TRANSPARENT);
            initModality(Modality.APPLICATION_MODAL);
            initOwner(stage);
            setX(windows.getMinX());
            setY(windows.getMinY());
            setWidth(windows.getWidth());
            setHeight(windows.getHeight());
            setScene(scene);
        }

        public MyDialog(Stage stage, Scene scene, double window_height, double window_width) {
            initStyle(StageStyle.TRANSPARENT);
            initModality(Modality.APPLICATION_MODAL);
            initOwner(stage);
            setScene(scene);
        }

        public void display() {
            centerOnScreen();
            showAndWait();
        }
    }
}
