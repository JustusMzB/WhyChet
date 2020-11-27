
package sample;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket server;
    private boolean connected;

    public Client (int port){
        try {
            this.server = new Socket("localhost", port);
            this.connected = true;
        } catch (IOException e) {
            System.out.println("Didn't connect: Couldn't instantiate socket");
            e.printStackTrace();
        }
    }



    public void execute(){
        Scanner keyboard = new Scanner(System.in);
        ObjectOutputStream msgServer = null;
        String nextLine;
        Message message;
        int roomID;

        try {
            msgServer = new ObjectOutputStream(this.server.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Listener receiveAndDisplay = new Listener(server);
        System.out.println("Starting Listener...");
        receiveAndDisplay.start();
        String username = keyboard.nextLine();

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
        if (nextLine.equalsIgnoreCase("/disconnect".trim())) {
            return -1;
        }
        else {
            return 1;
        }
    }
}

