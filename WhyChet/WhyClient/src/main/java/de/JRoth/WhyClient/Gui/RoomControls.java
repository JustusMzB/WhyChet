package de.JRoth.WhyClient.Gui;

import de.JRoth.WhyChet.WhyShareClasses.Messages.LiteRoom;
import de.JRoth.WhyClient.Client;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;

public class RoomControls extends VBox {
    private HashMap<Long, Component> roomComponents;
    private Client client;

    public void bootStrap(Client client){
        this.client = client;
    }

    public RoomControls(){
        roomComponents = new HashMap<>();
        this.setPrefWidth(100);
    }


    public void updateRoom(LiteRoom room){
        Component newRoomComp = RoomComponent.makeComponent(room, client);
        Component oldRoomView = roomComponents.get(room.getRoomId());
        roomComponents.put(room.getRoomId(), newRoomComp);
        if (oldRoomView != null) {
            Platform.runLater(() -> {
                super.getChildren().remove(oldRoomView.getView());
                super.getChildren().add(newRoomComp.getView());
            });
        } else {
            Platform.runLater(() -> {
                super.getChildren().add(newRoomComp.getView());
            });
        }

    }
}
