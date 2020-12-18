package de.JRoth.WhyClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
public class Listener extends Thread {
    private ObjectInputStream observedStream;
    private boolean connected;
    private Socket server;

    public Listener(Socket server){
        try {
            this.server = server;
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
                    executeOrder(newMessage); // execute Order taken from server
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Server Stream is Faulty. Closing connection.");
                e.printStackTrace();
                connected = false;
                break;
            }
        }
    }

    //adjust action to given roomID of message-object
    private void executeOrder(Message message){
        long id = message.getRoomID();
        if(id==1)
            System.out.println(message.displayString());
        else if(id == -1){
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println(" ID unknown. Shutting down.");
        }
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public ObjectInputStream getOberservedStream(){
        return observedStream;
    }
}
