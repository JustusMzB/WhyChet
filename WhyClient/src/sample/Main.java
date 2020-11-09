package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


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
        boolean connected = true;
        Scanner keyboard = new Scanner(System.in);

        try {
            System.out.println("Looking for Server ....");
            Socket server = new Socket("localhost", 1969);
            System.out.println("Connected to server.");
            ObjectOutputStream msgServer = new ObjectOutputStream(server.getOutputStream());

            Listener receiveAndDisplay = new Listener(server);
            receiveAndDisplay.start();
            System.out.println("Listener started.");


            String nextLine;
            Message message;
            String username  = "TestUser";
            while (true){
                nextLine = keyboard.nextLine();
                System.out.println("Received Keyboard input: "+ nextLine);
                try {
                    message = new Message(username, nextLine, 1);
                    msgServer.writeObject(message);
                } catch (IOException e){
                    System.err.println("Failed to send message "+ nextLine + ". Closing connection.");
                    server.close();
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Could not connect to host");
            e.printStackTrace();
        }

    }
}
