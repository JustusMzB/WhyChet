package de.JRoth.WhyClient.Gui;

import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.io.IOException;


public class MessageView{
    @FXML
    Label txtContent;

    public Message getMessage() {
        return message;
    }

    private Message message;


    public static Component makeMsgView(Message message){
        FXMLLoader msgLoader = new FXMLLoader(MessageView.class.getResource("/MessageView.fxml"));
        Node view = null;

        try {
             view = msgLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MessageView controller = msgLoader.getController();
        controller.txtContent.setText(message.displayString());
        return new Component(view, controller);
    }
}
