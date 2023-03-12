package com.example.program.util;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Utility with predefined animations
 */
public class Animation {

    private static FadeTransition fade;

    //  create animation fade
    public static void fade(Node node) {
        fade = new FadeTransition(Duration.seconds(1), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setCycleCount(1);
        fade.setAutoReverse(true);
        fade.play();
    }

    //  create fade animation by defining source opacity, target and duration of the animation
    public static void fade(Node node, double start, double end, int time) {
        fade = new FadeTransition(Duration.seconds(time), node);
        fade.setFromValue(start);
        fade.setToValue(end);
        fade.setCycleCount(1);
        fade.setAutoReverse(true);
        fade.play();
    }

    //  create fade animation by defining source opacity, target and duration of the animation
    public static void stopfade() {
        fade.stop();
    }
}
