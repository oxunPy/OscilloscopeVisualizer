package com.example.program.util;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Create links to actions that open the page in the browser
 */
public class Link {

    /**
     * Assists web addresses display on standard desktop
     */
    public static void address(String link) {
        try {
            Desktop.getDesktop().browse(new URI(link));
        } catch (URISyntaxException | IOException ex) {
            Message.error(String.format("Not display %s!", link));
        }
    }
}
