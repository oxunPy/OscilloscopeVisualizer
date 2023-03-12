package com.example.program.util;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * Utility for formatting and checking text fields, labels and textareas
 */
public class Field {

    /**
     * Do not allow text fields with null values
     */
    public static boolean notEmpty(TextField... field) {

        boolean empty = false;

        for (TextField tf : field) {
            if (tf.getText() != null && tf.getText().trim().isEmpty()) {
                error(tf, "Check empty value!");
                empty = true;
            }
        }

        return empty;
    }

    /**
     * Clear text
     */
    public static void clean(TextField... node) {
        for (TextField field : node)
            field.setText("");
    }

    /**
     * Clear text
     */
    public static void clean(Label... node) {
        for (Label field : node)
            field.setText("");
    }

    /**
     * Clear text
     */
    public static void clean(TextArea... node) {
        for (TextArea field : node)
            field.setText("");
    }

    /**
     * Error indicator in the field informed with red border
     */
    public static void error(Node node, String message) {
        try {
            if (node != null) {
                node.setStyle("-fx-border-color: #ff7575;");
                origin(node);
            }
        } catch (Exception ex) {
            Note.error("Error Error field");
        }
    }

    /**
     * Clicking the field returns to the default field style
     */
    private static void origin(Node node) {
        node.setOnMouseClicked((MouseEvent me) -> {
            node.setStyle("-fx-border-color: #eaeaea;");
        });
    }

    /**
     * Display error in the login field if leave empty space or incorrect
     */
    public static void errorLogin(Node node) {
        node.setStyle("-fx-border-color: #ff8b8b;");
        node.setOnMouseClicked((MouseEvent me) -> {
            node.setStyle("-fx-border-color: transparent transparent #e8e8e8 transparent;");
        });
    }
}
