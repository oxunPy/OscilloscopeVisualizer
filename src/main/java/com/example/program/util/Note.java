package com.example.program.util;

import com.example.program.app.controller.AppController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Create Briefing Notes, Message System Simplification
 */
public class Note {

    private static int qtNotes = 0;

    /**
     * Create note and add to last box
     */
    private static void note(VBox box, String message, String type) {

        HBox note = new HBox(label(message, type));
        note.getStylesheets().add("css/dialog.css");

        Button btClose = close(box, note);
        note.getChildren().add(btClose);
        note.getStyleClass().add("box-note");
        Animation.fade(note, 0.5, 1, 1);
        ++qtNotes;

        if (qtNotes >= 11) {
            box.getChildren().remove(0);
            --qtNotes;
        }

        box.getChildren().add(note);
        timer(btClose, 3000);
    }

    /**
     * Timer
     */
    public static void timer(Button btClose, int delay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(btClose::fire);
            }
        }, delay);
    }

    /**
     * Add action close note
     */
    private static Button close(VBox box, HBox note) {

        Button action = new Button();
        action.getStyleClass().add("bt-close-note");

        action.setOnAction((ActionEvent e) -> {
            box.getChildren().remove(note);
            --qtNotes;
        });

        return action;
    }

    /**
     * Format note message
     */
    private static Label label(String text, String type) {

        Label message = new Label(text);
        message.getStyleClass().add("note-text");
        HBox.setHgrow(message, Priority.ALWAYS);
        message.setMaxWidth(Double.MAX_VALUE);
        icon(type, message);

        return message;
    }

    /**
     * As the note type displays its respective icon
     */
    private static void icon(String type, Label message) {
        switch (type) {
            case "INFO":
                message.getStyleClass().add("note-info");
                break;
            case "ERROR":
                message.getStyleClass().add("note-error");
                break;
            case "ALERT":
                message.getStyleClass().add("note-alert");
                break;
            case "CONFIRM":
                message.getStyleClass().add("note-confirm");
                break;
            default:
                message.getStyleClass().add("note-info");
                break;
        }
    }

    public static void alert(String message) {
        note(AppController.getInstance().boxNotes(), message, "ALERT");
    }

    public static void info(String message) {
        note(AppController.getInstance().boxNotes(), message, "INFO");
    }

    public static void error(String message) {
        note(AppController.getInstance().boxNotes(), message, "ERROR");
    }

    public static void confirm(String message) {
        note(AppController.getInstance().boxNotes(), message, "CONFIRM");
    }
}
