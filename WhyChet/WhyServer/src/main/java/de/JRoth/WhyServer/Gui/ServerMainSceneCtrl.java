package de.JRoth.WhyServer.Gui;

import de.JRoth.WhyServer.DisplayService;
import de.JRoth.WhyServer.Main;
import de.JRoth.WhyServer.Server;
import de.JRoth.WhyServer.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;



public class ServerMainSceneCtrl implements DisplayService {
    final LogService logService = new ConsoleLog();

    Server server;
    Users userView;

    @FXML
    ScrollPane scrllpnUsers;

    @FXML
    Button btnClose;

    @Override
    public void log(String logEntry) {
        logService.log(logEntry);
    }

    @Override
    public void errLog(String logEntry) {
        logService.errLog(logEntry);
    }

    public void addUser(User user){
        Platform.runLater(new Users.AddUser(user, userView));
    }

    public void bootStrap(Users userView, Server server){
        this.userView = userView;
        this.server = server;
        scrllpnUsers.setContent(userView);
    }

    @FXML
    void closeServer(ActionEvent event){
        server.terminate();
    }
}
