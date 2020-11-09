package sample;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Listener extends Thread {
    private ObjectInputStream observedStream;
    private boolean connected;

    public Listener(Socket server){
        connected = true;
        try {
            System.out.println("[LISTENER CONSTRUCTION] Trying to connect to Objectstream");
            observedStream = new ObjectInputStream(server.getInputStream());
            System.out.println("Listener constructed successfully");
        } catch (IOException e) {
            connected = false;
        }
    }
    @Override
    public void run() {
        System.out.println("[LISTENER] started successfully");
        Message newMessage;
        while (connected){
            try {
                if(observedStream.available() >= 0) {
                    newMessage = (Message) observedStream.readObject();
                    System.out.println(newMessage.displayString());
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Server Stream is Faulty. Closing connection.");
                connected = false;
                break;
            }
        }
    }
}
