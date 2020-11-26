
package sample;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
     private Socket server;

    public Client (int port){
        try {
            this.server = new Socket("localhost", port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getServer(){
        return this.server;
    }

    public void execute(){
        Scanner keyboard = new Scanner(System.in);
        ObjectOutputStream msgServer = null;
        try {
            msgServer = new ObjectOutputStream(this.server.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Listener receiveAndDisplay = new Listener(server);

        System.out.println("Starting Listener...");
        receiveAndDisplay.start();
        String nextLine;
        Message message;
        String username = keyboard.nextLine();
        int roomID;

        try{
            while (true) {
                nextLine = keyboard.nextLine();
                //System.out.println("Received Keyboard input: " + nextLine);
                roomID = isOrder(nextLine);
                if (nextLine.equalsIgnoreCase("")) {
                    continue;
                }
                try {
                    message = new Message(username, nextLine, roomID);
                    msgServer.writeObject(message);
                } catch (IOException e) {
                    System.err.println("Failed to send message " + nextLine + ". Closing connection.");
                    server.close();
                    break;
                }
            }
        } catch(
                IOException e)
        {
            System.err.println("Could not connect to host");
            e.printStackTrace();
        }
    }

    private static int isOrder(String nextLine) {

        if (nextLine.equalsIgnoreCase("/disconnect")) {
            return -1;
//                    continue;
        }
//                 else if (nextLine.equalsIgnoreCase("\\close")) {
//                    server.close();
//                    break;
        else {
            return 1;
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

}

