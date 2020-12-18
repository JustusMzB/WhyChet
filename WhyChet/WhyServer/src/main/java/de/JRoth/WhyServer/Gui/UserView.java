package de.JRoth.WhyServer.Gui;

import de.JRoth.WhyServer.User;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class UserView implements Initializable {
    private User user;
    BooleanProperty isOnline = new SimpleBooleanProperty(false);

    @FXML
    MenuItem kick;

    @FXML
    Label lblUsername;

    @FXML
    CheckBox chckbxOnline;

    @FXML
    void kick(ActionEvent event){
        user.logOff();
    }

    void setUser(User user){

        this.user = user;
        lblUsername.setText(user.getName());
        Platform.runLater(() -> {
            isOnline.bind(user.getOnlineProperty());
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chckbxOnline.selectedProperty().bind(isOnline);
    }
}
