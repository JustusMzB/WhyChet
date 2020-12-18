package de.JRoth.WhyServer;

import de.JRoth.WhyServer.Gui.ServerMainSceneCtrl;
import de.JRoth.WhyServer.Gui.UserView;
import de.JRoth.WhyServer.Gui.Users;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class Main extends Application{
    private Server server;


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/ServerMainScene.fxml"));
        Scene mainScene = new Scene(mainLoader.load());
        ServerMainSceneCtrl mainController = mainLoader.getController();
        Users userDisplay = new Users();
        server = new Server(1969, mainController);
        mainController.bootStrap(userDisplay, server);
        server.start();
        stage.setScene(mainScene);
        stage.show();

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        server.terminate();
    }
}
