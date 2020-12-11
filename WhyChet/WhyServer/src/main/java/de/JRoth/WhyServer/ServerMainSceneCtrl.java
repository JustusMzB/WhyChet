package de.JRoth.WhyServer;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;



public class ServerMainSceneCtrl {
    @FXML
    Tab tbUsers;
    @FXML
    VBox vbxUsers;
    @FXML
    Button btnDummy;

    @FXML
    void addDummy(ActionEvent event) {
        Label label = new Label();
        label.setText("HelloDummy");
        try {
            HBox uv = FXMLLoader.load(getClass().getResource("/UserView.fxml"));
            vbxUsers.getChildren().add(uv);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void closeServer(ActionEvent event){
        Main.server.terminate();
    }
}
