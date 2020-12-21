package de.JRoth.WhyServer.Gui;

import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public class Messages extends VBox {
    private HashMap<Message, View> views = new HashMap<>();

    public void addMessage(Message message){
        View newMessageView = MessageView.makeMsgView(message);
        views.put(message, newMessageView);
        Platform.runLater(() -> {
            getChildren().add(newMessageView.getNode());
        });

    }
}
