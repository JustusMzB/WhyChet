package de.JRoth.WhyClient;
import de.JRoth.WhyClient.Gui.GuiFassade;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;




public class Main extends Application {
    private Client client;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/GuiFront.fxml"));
        Scene primeScene = new Scene(loader.load());
        GuiFassade gui = loader.getController();

        client = new Client(1969, gui);
        gui.bootStrap(client);

        primaryStage.setTitle("WhyClient");
        primaryStage.setScene(primeScene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        this.client.closeConnection();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}
