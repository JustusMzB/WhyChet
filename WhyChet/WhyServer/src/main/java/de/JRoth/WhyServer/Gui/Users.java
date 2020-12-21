package de.JRoth.WhyServer.Gui;

import de.JRoth.WhyServer.User;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Users extends VBox {
    ObservableList<Node> children = super.getChildren();

    //For non-running application
    void addUser(User user){
        FXMLLoader userLoader = new FXMLLoader(getClass().getResource("/UserView.fxml"));

        Platform.runLater(() -> {
            try {
                children.add(userLoader.load());
                userLoader.<UserView>getController().setUser(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
