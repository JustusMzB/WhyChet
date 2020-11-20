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
        //boolean connected = true;                                                                 //is never used
        Scanner keyboard = new Scanner(System.in);

        try {
            System.out.println("Looking for Server ....");
            Socket server = new Socket("localhost", 3969);
            System.out.println("Connected to server." + server.getLocalAddress() + " "+ server.getLocalPort());
            ObjectOutputStream msgServer = new ObjectOutputStream(server.getOutputStream());

            Listener receiveAndDisplay = new Listener(server);
            System.out.println("Starting Listener...");
            receiveAndDisplay.start();

            String nextLine;
            Message message;
            String username = keyboard.nextLine();
            int roomID = 0;

//            try {
//                if(((Message) receiveAndDisplay.getOberservedStream().readObject()).displayString().equals(
//                        "Type in your Username please. If it is unknown, you will receive a new account.")){
//                    username = takeUserName(keyboard);
//                    msgServer.writeObject(new Message(username, username, 1));
//                }
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
            while (true) {
                nextLine = keyboard.nextLine();
                System.out.println("Received Keyboard input: " + nextLine);
                roomID = isOrder(nextLine);
                try {
                    message = new Message(username, nextLine, roomID);
                    msgServer.writeObject(message);
                } catch (IOException e) {
                    System.err.println("Failed to send message " + nextLine + ". Closing connection.");
                    server.close();
                    break;
                }
            }
        } catch(IOException e) {
            System.err.println("Could not connect to host");
            e.printStackTrace();
        }
    }

    private static String takeUserName(Scanner keyboard) {
        String username = keyboard.nextLine();
        while(isOrder(username)!=1){
            System.out.println("That name must not be taken.");
            username = keyboard.nextLine();
        }
        return username;
    }

    private static int isOrder(String nextLine) {

        if (nextLine.equalsIgnoreCase("\\disconnect")) {
            return -1;
//                    receiveAndDisplay.setConnected(false);
//                    server.close();
//                    continue;
        }
//                 else if (nextLine.equalsIgnoreCase("\\close")) {
//                    server.close();
//                    break;

        else {
            return 1;
        }

    }
}
