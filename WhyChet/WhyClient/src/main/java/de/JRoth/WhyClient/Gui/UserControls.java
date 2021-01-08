package de.JRoth.WhyClient.Gui;

import de.JRoth.WhyChet.WhyShareClasses.Messages.LiteUser;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserControls extends VBox {
    private HashMap<String, Component> userComponents;
    void addUser(LiteUser user){
        Component newUserComp = UserGRep.makeUserComponent(user);
        userComponents.put(user.getName(), newUserComp);
        Platform.runLater(()->super.getChildren().add(newUserComp.getView()));
    }
    UserControls(List<LiteUser> users){
        for (LiteUser u : users){
            addUser(u);
        }
    }
}
