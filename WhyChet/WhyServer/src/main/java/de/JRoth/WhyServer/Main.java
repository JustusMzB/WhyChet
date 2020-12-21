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
        // Initialisation of GUI
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/ServerMainScene.fxml"));
        Scene mainScene = new Scene(mainLoader.load());
        ServerMainSceneCtrl mainController = mainLoader.getController();

        //Initialisation of Server (maybe persistence loading)
        server = new Server(1969, mainController);

        //Initialisation of users and rooms into GUI
        mainController.bootStrap(server);

        //Start server
        server.start();

        //Starting Gui
        stage.setScene(mainScene);
        stage.show();

    }

    @Override //Ensures proper closing upon the closing of the gui
    public void stop() throws Exception {
        super.stop();
        server.terminate();
    }
}
