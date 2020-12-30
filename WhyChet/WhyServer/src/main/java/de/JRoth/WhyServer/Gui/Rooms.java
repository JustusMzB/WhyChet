package de.JRoth.WhyServer.Gui;

import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
import de.JRoth.WhyServer.Room;
import de.JRoth.WhyServer.User;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

import java.util.HashMap;

public class Rooms extends Accordion {
    private HashMap<Long, RoomView> controllers = new HashMap<>();

    void addRoom(Room room){
        View roomView = RoomView.makeRoomView(room);

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
}
