package de.JRoth.WhyServer;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
    Scene root;
    static Server server;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(this.getClass().getResource("/ServerMainScene.fxml"));
        primaryStage.setTitle("Hello World");
        this.root = new Scene(root, 300, 275);
        primaryStage.setScene(this.root);
        primaryStage.show();
    }


    public static void main(String[] args) {
        Main.server = new Server(1969);
        Main.server.start();
        launch(args);
    }
}
