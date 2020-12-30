package de.JRoth.WhyServer.Gui;

import de.JRoth.WhyServer.User;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;

public class Users extends VBox {
    ObservableList<Node> children = super.getChildren();
    HashMap<User, View> users = new HashMap<>();

    //For non-running application
    void addUser(User user){
        FXMLLoader userLoader = new FXMLLoader(getClass().getResource("/UserView.fxml"));
        Node userNode = null;
        UserView userController = null;
        try {
             userNode = userLoader.load();
             userController = userLoader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
        assert userController != null;
        userController.setUser(user);
        users.put(user, new View(userController, userNode));

        Node finalUserNode = userNode;

        Platform.runLater(() -> children.add(finalUserNode));
    }

    public void removeUser(User member) {
        Node toBeRemoved = users.get(member).getNode();
        Platform.runLater(()-> children.remove(toBeRemoved));
        users.remove(member);
    }


    //Allows use through Platform.runLater
    static class AddUser implements Runnable{
        private final User user;
        private final VBox target;
        AddUser(User user, VBox target){
            this.user = user;
            this.target = target;
        }
        @Override
        public void run() {
            FXMLLoader userLoader = new FXMLLoader(getClass().getResource("/UserView.fxml"));

            try {
                target.getChildren().add(userLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            userLoader.<UserView>getController().setUser(user);
        }
    }
}
