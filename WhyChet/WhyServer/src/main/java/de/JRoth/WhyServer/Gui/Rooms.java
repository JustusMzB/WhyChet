package de.JRoth.WhyServer.Gui;

import de.JRoth.WhyServer.Room;
import de.JRoth.WhyServer.User;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

import java.util.HashMap;

public class Rooms extends Accordion {
    private HashMap<Long, RoomController> controllers = new HashMap<>();

    void addRoom(Room room){
        View roomView = RoomController.makeRoomView(room);

        this.controllers.put(room.getId(), (RoomController) roomView.getController());
        super.getPanes().add((TitledPane) roomView.getNode());
    }

    void addUser(Room room, User user){
        this.controllers.get(room.getId()).addMember(user);
    }
}
