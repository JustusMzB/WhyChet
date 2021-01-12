package de.JRoth.WhyClient.Gui;

import de.JRoth.WhyChet.WhyShareClasses.Messages.LiteRoom;
import de.JRoth.WhyClient.Client;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.LinkedList;
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
        int oldIndex = -1;
        if(oldRoomView != null) {
            oldIndex  = super.getChildren().indexOf(oldRoomView.getView());
        }
            roomComponents.put(room.getRoomId(), newRoomComp);
            if (oldIndex != -1) {
                int finalOldIndex = oldIndex;
                Platform.runLater(() -> {
                    super.getChildren().set(finalOldIndex, newRoomComp.getView());
                });
        } else {
            Platform.runLater(() -> {
                super.getChildren().add(newRoomComp.getView());
            });
        }

    }

    public void deleteRoom(LiteRoom room) {
        deleteRoom(room.getRoomId());
    }

    private void deleteRoom(Long id){
        Node toRemove = roomComponents.get(id).getView();
        roomComponents.remove(id);
        Platform.runLater(() -> super.getChildren().remove(toRemove));
    }

    public void clear(){
        LinkedList<Long> idClone = new LinkedList<Long>(roomComponents.keySet());
        for (Long i: idClone){
        deleteRoom(i);
        }
    }
}
