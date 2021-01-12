package de.JRoth.WhyClient.Gui;

import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public class Messages extends VBox {
    private HashMap<Message, Component> views = new HashMap<>();
    Messages(){
        this.getStyleClass().add("componentContainer");
    }

    public void addMessage(Message message){
        Component newMessageView = MessageView.makeMsgView(message);
        views.put(message, newMessageView);
        System.out.println("Message added");
        Platform.runLater(() -> getChildren().add(newMessageView.getView()));

    }
}
