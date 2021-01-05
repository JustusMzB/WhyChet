package de.JRoth.WhyServer.Gui;

import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
import de.JRoth.WhyServer.Room;
import de.JRoth.WhyServer.User;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

import java.util.HashMap;

public class Rooms extends Accordion {
    private HashMap<Long, RoomView> controllers = new HashMap<>();
    private HashMap<Long, Node> children = new HashMap<>();

    public void removeRoom(Room room) {
        Node soughtChild = children.get(room.getId());
        Platform.runLater(() -> super.getChildren().remove(soughtChild));
    }

    void addRoom(Room room){
        View roomView = RoomView.makeRoomView(room);
        this.children.put(room.getId(), roomView.getNode());
        this.controllers.put(room.getId(), (RoomView) roomView.getController());
        super.getPanes().add((TitledPane) roomView.getNode());
    }

    void addMessage(Message message, long id){
        controllers.get(id).displayMessage(message);
    }

    void addUser(Room room, User user){
        this.controllers.get(room.getId()).addMember(user);
    }
    //Selects correct roomview and asks it to remove the user
    void removeUser(Room room, User user) {
        controllers.get(room.getId()).removeMember(user);
    }

    void updateRoom(Room target) {
        TitledPane toBeRelabeled = controllers.get(target.getId()).ttldpnRoom;
        Platform.runLater(() -> toBeRelabeled.setText(target.getName()));
    }
}
