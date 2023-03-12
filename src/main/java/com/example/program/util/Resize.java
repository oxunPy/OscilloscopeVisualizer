package com.example.program.util;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * Resizing the components according to their relative allowing to size the distance between the node and its parent
 */
public class Resize {

    /**
     * Function to facilitate the resizing of the nodes to their size according to the distance of their relative
     *
     * @param node     node of the component to be resized
     * @param top    distance from the top to the parent node
     * @param right  distance from the right to the parent node
     * @param bottom distance from the bottom to the parent node
     * @param left   distance from the left to the parent node
     */
    public static void margin(Node node, double top, double right, double bottom, double left) {
        AnchorPane.setTopAnchor(node, top);
        AnchorPane.setRightAnchor(node, right);
        AnchorPane.setBottomAnchor(node, bottom);
        AnchorPane.setLeftAnchor(node, left);
    }

    /**
     * Function to facilitate the resizing of the nodes to their size according to the distance of their relative
     *
     * @param node    node of the component to be resized
     * @param value values for all nodes
     */
    public static void margin(Node node, double value) {
        AnchorPane.setTopAnchor(node, value);
        AnchorPane.setRightAnchor(node, value);
        AnchorPane.setBottomAnchor(node, value);
        AnchorPane.setLeftAnchor(node, value);
    }

    /**
     * Set top, right, and left margin values
     */
    public static void margin(Node node, double top, double right, double left) {
        AnchorPane.setTopAnchor(node, top);
        AnchorPane.setRightAnchor(node, right);
        AnchorPane.setLeftAnchor(node, left);
    }

    /**
     * Set left and right margin values
     */
    public static void margin(Node node, double right, double left) {
        AnchorPane.setRightAnchor(node, right);
        AnchorPane.setLeftAnchor(node, left);
    }

    /**
     * Set top margin values
     */
    public static void marginTop(Node node, double top) {
        AnchorPane.setTopAnchor(node, top);
    }

    /**
     * Set right margin values
     */
    public static void marginRight(Node node, double right) {
        AnchorPane.setRightAnchor(node, right);
    }

    /**
     * Set bottom margin values
     */
    public static void marginBottom(Node node, double bottom) {
        AnchorPane.setBottomAnchor(node, bottom);
    }

    /**
     * Set left margin values
     */
    public static void marginLeft(Node node, double left) {
        AnchorPane.setLeftAnchor(node, left);
    }
}
