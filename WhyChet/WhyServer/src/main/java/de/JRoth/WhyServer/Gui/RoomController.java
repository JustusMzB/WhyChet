package de.JRoth.WhyServer.Gui;

import de.JRoth.WhyServer.Room;
import de.JRoth.WhyServer.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RoomController implements Initializable {
    @FXML
    ScrollPane scrllpnUsers;
    @FXML
    TitledPane ttldpnRoom;
    @FXML
    ScrollPane scrllpnMessages;

    private Room room;
    private Users users;

   //Returns the Node in [0] and the controller in [1]
    public static View makeRoomView(Room room){
        FXMLLoader roomLoader = new FXMLLoader(RoomController.class.getResource("/RoomView.fxml"));
        TitledPane roomView = null;

        try {
            roomView = roomLoader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        RoomController roomController;
        roomController = roomLoader.getController();
        for(User i : room.getMembers()){
            roomController.users.addUser(i);
        }

        roomController.ttldpnRoom.setText((room.getName()));
        return new View(roomController, roomView);

    }
    public void addMember(User member){
        users.addUser(member);
    }

    public void setUsers(Users users) {
        this.users = users;
        scrllpnUsers.setContent(users);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUsers(new Users());
    }
}