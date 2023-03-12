package com.example.program.util;

/**
 * Message creation from the dialog class
 */
public class Message {

    public static Dialog.Answer info(String message) {
        return Dialog.messages("INFO", StringConfig.getValue("label.info"), message);
    }

    public static Dialog.Answer info(String message, String title) {
        return Dialog.messages("INFO", title, message);
    }

    public static Dialog.Answer error(String message) {
        return Dialog.messages("ERROR", StringConfig.getValue("label.error"), message);
    }

    public static Dialog.Answer error(String message, String title) {
        return Dialog.messages("ERROR", title, message);
    }

    public static Dialog.Answer alert(String message) {
        return Dialog.messages("ALERT", StringConfig.getValue("label.alert"), message);
    }

    public static Dialog.Answer alert(String message, String title) {
        return Dialog.messages("ALERT", title, message);
    }

    public static Dialog.Answer confirm(String message) {
        return Dialog.messageConfirm(StringConfig.getValue("label.confirm"), message);
    }

    public static Dialog.Answer confirm(String title, String message) {
        return Dialog.messageConfirm(title, message);
    }
}
