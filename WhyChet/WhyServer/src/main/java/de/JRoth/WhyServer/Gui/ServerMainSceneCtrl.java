package de.JRoth.WhyServer.Gui;

import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
import de.JRoth.WhyServer.DisplayService;
import de.JRoth.WhyServer.Room;
import de.JRoth.WhyServer.Server;
import de.JRoth.WhyServer.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;


public class ServerMainSceneCtrl implements DisplayService, Initializable {
    final LogService logService = new PersistentLog();

    Server server;
    Users allUsers;
    Rooms rooms;
    @FXML
    Button btnNewRoom;
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
        log("[SERVER] User " + newMember.getName() + " joined room " + room.getId());
        this.rooms.addUser(room, newMember);

    }

    @Override
    public void memberLeft(Room room, User leavingMember) {
        log("[SERVER] User " + leavingMember.getName() + " left room " + room.getId());
        rooms.removeUser(room, leavingMember);
    }

    public void addUser(User user) {
        allUsers.addUser(user);
    }

    @Override
    public void chatMessage(Message message, long id) {
        rooms.addMessage(message, id);
        log("[SERVER] a Message is being distributed via Room  " + id);
    }

    public void bootStrap(Server server) {

        //Loading Users into the general User view
        for (User i : server.getUsers().values()) {
            allUsers.addUser(i);
        }

        // Loading Rooms into room-menu
        for (Room i : server.getRooms()) {
            addRoom(i);
        }

        this.server = server;
    }

    public void addRoom(Room room) {
        log("[SERVER] New room " + room.getId() + " is being added.");
        this.rooms.addRoom(room);
    }

    @Override
    public void updateRoom(Room target) {
        rooms.updateRoom(target);
        log("Room " + target.getId() + "Was updated. Its new Name: " + target.getName());
    }

    @Override
    public void removeRoom(Room room) {
        rooms.removeRoom(room);
    }

    @FXML
    void closeServer(ActionEvent event) {
        server.terminate();
    }

    @FXML
    void addRoom(ActionEvent event) {
        TextInputDialog nameGetter = new TextInputDialog();
        nameGetter.setContentText("Name");
        nameGetter.setHeaderText("Name for the new room");
        Platform.runLater(() -> {
            nameGetter.showAndWait();
            String name = nameGetter.getResult();
            if (name != null) {
                server.addRoom(name);
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.allUsers = new Users();
        scrllpnUsers.setContent(allUsers);
        this.rooms = new Rooms();
        scrllpnRooms.setContent(rooms);
    }
}
