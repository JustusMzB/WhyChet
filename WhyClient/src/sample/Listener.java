package sample;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Listener extends Thread {
    private ObjectInputStream observedStream;
    private boolean connected;

    public Listener(Socket server){
        try {
            System.out.println("[LISTENER CONSTRUCTION] Trying to connect to Objectstream ...");
            observedStream = new ObjectInputStream(server.getInputStream());
            System.out.println("[LISTENER CONSTRUCTION] Listener constructed successfully.");
            connected = true;
        } catch (IOException e) {
            connected = false;
            System.out.println("[LISTENER CONSTRUCTION] Listener construction failed.");
        }
    }

    @Override
    public void run() {
        System.out.println("[LISTENER] started successfully" );
        Message newMessage;
        while (connected){
            try {
                if(observedStream.available() >= 0) {
                    newMessage = (Message) observedStream.readObject();
                    System.out.println(newMessage.displayString());
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Server Stream is Faulty. Closing connection.");
                e.printStackTrace();
                connected = false;
                break;
            }
        }
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public ObjectInputStream getOberservedStream(){
        return observedStream;
    }
}
