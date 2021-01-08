package de.JRoth.WhyClient;
import de.JRoth.WhyClient.Gui.GuiFassade;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;




public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/GuiFront.fxml"));
        Scene primeScene = new Scene(loader.load());
        GuiFassade gui = loader.getController();

        Client client = new Client(1969, gui);
        gui.setClient(client);

        primaryStage.setTitle("WhyClient");
        primaryStage.setScene(primeScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
