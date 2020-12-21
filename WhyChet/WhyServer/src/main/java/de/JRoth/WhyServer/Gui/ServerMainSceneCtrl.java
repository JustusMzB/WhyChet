package de.JRoth.WhyServer.Gui;

import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
import de.JRoth.WhyServer.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;


public class ServerMainSceneCtrl implements DisplayService, Initializable {
    final LogService logService = new ConsoleLog();


    Server server;
    Users allUsers;
    Rooms rooms;

    @FXML
    ScrollPane scrllpnUsers;
    @FXML
    ScrollPane scrllpnRooms;
    @FXML
    Button btnClose;
    @FXML
    Text txtLog;

    @Override
    public void log(String logEntry) {
        logService.log(logEntry);
        Platform.runLater(() -> {
            String logStr = txtLog.getText();
            logStr += logEntry + "\n";
            txtLog.setText(logStr);
        });
    }

    @Override
    public void errLog(String logEntry) {
        logService.errLog(logEntry);

        Platform.runLater(() -> {
            String logStr = txtLog.getText();
            logStr += logEntry + "\n";
            txtLog.setText(logStr);
        });
    }

    @Override
    public void memberJoined(Room room, User newMember) {
        log("[SERVER] User "+ newMember.getName() +" joined room " + room.getId());
        this.rooms.addUser(room, newMember);

    }

    @Override
    public void memberLeft(Room room, User leavingMember) {

    }

    public void addUser(User user){
        Platform.runLater(new Users.AddUser(user, allUsers));
    }

    @Override
    public void chatMessage(Message message, long id) {
        rooms.addMessage(message, id);
        log("[SERVER] a Message is being distributed via Room  " + id);
    }

    public void bootStrap(Server server){

        //Loading Users into the general User view
        for (User i : server.getUsers().values()){
            allUsers.addUser(i);
        }

        // Loading Rooms into room-menu
        for (Room i : server.getRooms()){
            addRoom(i);
        }

        this.server = server;
    }

    private void addRoom(Room room) {
        this.rooms.addRoom(room);
    }

    @FXML
    void closeServer(ActionEvent event){
        server.terminate();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.allUsers = new Users();
        scrllpnUsers.setContent(allUsers);
        this.rooms = new Rooms();
        scrllpnRooms.setContent(rooms);
    }
}
