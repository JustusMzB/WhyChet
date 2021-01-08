package de.JRoth.WhyClient.Gui;

import javafx.scene.Node;

public class Component {
    private Node view;
    private Object control;

    public Component(Node node, Object controls) {
        this.control = controls;
        this.view = node;
    }

    public Node getView() {
        return view;
    }

    public Object getControl() {
        return control;
    }
}
