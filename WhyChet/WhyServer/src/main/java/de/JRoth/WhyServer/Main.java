package de.JRoth.WhyServer;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(this.getClass().getResource("/ServerMainScene.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        Server myServer = new Server(1969);
        myServer.start();
        ServerOrderService orderService = new ConsoleServerController(myServer);
        while (!orderService.closeSignal()) {
            orderService.instruct();
        }
        launch(args);

    }
}
