
package de.JRoth.WhyClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import de.JRoth.WhyChet.WhyShareClasses.Messages.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


public class Client {
    private String userName;
    private BooleanProperty isLoggedIn = new SimpleBooleanProperty(false);
    private BooleanProperty connected = new SimpleBooleanProperty(false);
    private Socket server;
    private UI ui;
    private Listener listener;
    ObjectOutputStream msgServer = null;
    ObjectInputStream msgIn = null;
    private HashMap<Long, LiteRoom> rooms = new HashMap<>();
    private LiteRoom myRoom = null;

    public void sendMessage(String text){
        try {
            msgServer.writeObject(new Message(userName, text, myRoom.getRoomId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setUsername(String userName){
        this.userName = userName;
        ui.setUsername(userName);
    }
    public void setLoggedIn(boolean b){
        isLoggedIn.set(b);
        ui.logOff();
    }
    public boolean isLoggedIn(){
        return isLoggedIn.get();
    }
    public void login(){
        setLoggedIn(internalLogin());
        ui.out("Login attempt was made. Result: " + isLoggedIn);
    }
    private boolean internalLogin() {
        boolean success = false;
        String[] loginData;
        String username;
        String password;

        try {
            do {
                loginData = ui.getLoginData();
                username = loginData[0];
                password = loginData[1];
                msgServer.writeObject(new LoginMessage(username, password));
                ui.out("Login credentials were sent.");
                ServerResponse response = null;
                //Waiting for proper response
                while (response == null) {
                    ui.out("Waiting for response");
                    Message message = (Message) msgIn.readObject();
                    ui.out("Something came from the server. ID: " + message.getRoomID());
                    if (message.getRoomID() == -8) {
                        response = (ServerResponse) message;
                        ui.out("Response received. Result: " + response.isSuccess());
                    }
                }
                success = response.isSuccess();
            } while (!success);
            setUsername(username);
            listener.start();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return success;
    }

    public Client(int port, UI ui) {
        this.ui = ui;
        try {
            this.server = new Socket("localhost", port);
            this.connected.set(true);
            msgServer = new ObjectOutputStream(this.server.getOutputStream());
            msgIn = new ObjectInputStream(this.server.getInputStream());
            listener = new Listener(server, ui);
        } catch (IOException e) {
            ui.out("Didn't connect: Couldn't instantiate socket");
            e.printStackTrace();
        }
    }

    private class Listener extends Thread {
        private ObjectInputStream observedStream;
        private boolean connected;
        private Socket server;
        private UI ui;

        public Listener(Socket server, UI ui){
            this.ui = ui;
            this.server = server;
            ui.out("[LISTENER CONSTRUCTION] Trying to connect to Objectstream ...");
            observedStream = msgIn;
            ui.out("[LISTENER CONSTRUCTION] Listener constructed successfully.");
            connected = true;
        }

        @Override
        public void run() {
            ui.out("[LISTENER] started successfully" );
            Message newMessage;
            while (connected){
                try {
                    if(observedStream.available() >= 0) {
                        newMessage = (Message) observedStream.readObject();
                        executeOrder(newMessage); // execute Order taken from server
                        ui.out("Message received");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Server Stream is Faulty. Closing connection.");
                    e.printStackTrace();
                    connected = false;
                    setLoggedIn(false);
                    break;
                }
            }
        }

        //adjust action to given roomID of message-object
        private void executeOrder(Message message){
            long id = message.getRoomID();
            if(id >= 0 )
                ui.displayMessage(message);
            else if(id == -1){
                ui.out("Connection is being closed");
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(id == -3){
                RoomMessage roomMessage = (RoomMessage) message;
                LiteRoom newMyRoom = roomMessage.getRoom();
                rooms.put(newMyRoom.getRoomId(), newMyRoom);
                myRoom = newMyRoom;
            }
            else if(id == -4){
                RoomMessage roomMessage = (RoomMessage) message;
                    rooms.put(roomMessage.getRoom().getRoomId(), roomMessage.getRoom());
                    ui.roomUpdate(roomMessage.getRoom());
            }
            else if(id == -5){
                Long deletedRoomID = Long.valueOf(message.getContent());
                ui.deleteRoom(rooms.get(deletedRoomID));
                rooms.remove(deletedRoomID);
            }
            else if(id == -6){
                LiteUser user = ((UserMessage)message).getUser();
                if (myRoom.getUsers().contains(user)){
                    myRoom.getUsers().get(myRoom.getUsers().indexOf(user)).setOnline(user.isOnline());
                    ui.userUpdate(user);
                }
            }
            else{
                ui.out("Received unknown Order");
            }
        }


        ObjectInputStream getOberservedStream(){
            return observedStream;
        }
    }

}

