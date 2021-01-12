package de.JRoth.WhyClient.Gui;

import de.JRoth.WhyChet.WhyShareClasses.Messages.LiteRoom;
import de.JRoth.WhyClient.Client;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;

public class RoomComponent{
    @FXML
    Label txtName;

    @FXML
    public void switchRoom(Event event){
        boolean yes = true;
        if(event instanceof MouseEvent){
           yes = ((MouseEvent)event).getButton() == MouseButton.PRIMARY;
        }
        if(yes)
        client.switchRoom(room);
    }

    public void setRoom(LiteRoom room) {
        this.room = room;
        this.txtName.setText(room.getRoomName());
    }

    private LiteRoom room;
    private Client client;
    public static Component makeComponent(LiteRoom room, Client client){
        FXMLLoader loader = new FXMLLoader(RoomComponent.class.getResource("/roomview.fxml"));
        Node view = null;
        RoomComponent controls = null;
        try {
            view = loader.load();
            controls = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controls.setRoom(room);
        controls.client = client;
        return new Component(view, controls);
    }
}
