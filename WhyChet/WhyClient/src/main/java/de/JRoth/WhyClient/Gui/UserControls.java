package de.JRoth.WhyClient.Gui;

import de.JRoth.WhyChet.WhyShareClasses.Messages.LiteUser;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class UserControls extends VBox {
    private HashMap<String, Component> userComponents;
    private List<LiteUser> visibleUsers;

    public void setVisibleUsers(List<LiteUser> visibleUsers) {
        this.visibleUsers = visibleUsers;
        userComponents.clear();
        for(LiteUser i : visibleUsers){
            userComponents.put(i.getName(), UserGRep.makeUserComponent(i));
        }
        Platform.runLater(()->{
            super.getChildren().clear();
            for(Component i: userComponents.values()){
                super.getChildren().add(i.getView());
            }
        });
    }

    void putUser(LiteUser user){

        Component newUserComp = UserGRep.makeUserComponent(user);
        Node newUserView = newUserComp.getView();

        if(userComponents.containsKey(user.getName())){
            Node oldView = userComponents.get(user.getName()).getView();
            userComponents.put(user.getName(), newUserComp );
            Platform.runLater(() -> {
                super.getChildren().remove(oldView);
                super.getChildren().add(newUserView);
            });
        } else {
            userComponents.put(user.getName(), newUserComp);
            Platform.runLater(() -> {
                super.getChildren().add(newUserView);
            });
        }

    }


    UserControls(List<LiteUser> users){
        this.visibleUsers = new LinkedList<>();
        this.userComponents = new HashMap<>();
        for (LiteUser u : users){
            putUser(u);
        }
    }
    UserControls(){
        this.visibleUsers = new LinkedList<>();
        this.userComponents = new HashMap<>();
    }

    public void deleteUser(LiteUser leavingUser) {
        if(userComponents.containsKey(leavingUser.getName())) {
            Node leavingUserNode = userComponents.get(leavingUser.getName()).getView();
            userComponents.remove(leavingUser.getName());
            Platform.runLater(() -> super.getChildren().remove(leavingUserNode));
        }
    }
}
