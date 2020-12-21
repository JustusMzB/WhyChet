package de.JRoth.WhyServer.Gui;

import javafx.scene.Node;

public class View {
    private Object controller;
    private Node node;

    public View(Object controller, Node node) {
        this.controller = controller;
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public Object getController() {
        return controller;
    }

}
