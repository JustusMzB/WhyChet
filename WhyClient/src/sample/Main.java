package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;



//ALL MAIN FUNCTIONS ARE UTTER TRASH. THE CLIENT IS NOT YET IMPLEMENTED!
public class Main /*extends Application*/ {

    /*@Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }*/


    public static void main(String[] args) {
        //launch(args);
        try {
            Socket server = new Socket("localhost", 1969);
            ObjectInputStream serverMessages = new ObjectInputStream(server.getInputStream());
            Object loginMessage = serverMessages.readObject();
            if(loginMessage.getClass() == Message.class){
                System.out.println(((Message) loginMessage).displayString());
            }
        } catch (IOException e) {
            System.err.println("Could not connect to host");
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            System.err.println("Object received from Server could not be decyphered");
            e.printStackTrace();
        }

    }
}
