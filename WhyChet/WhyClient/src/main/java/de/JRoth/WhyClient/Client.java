package de.JRoth.WhyClient;

import de.JRoth.WhyChet.WhyShareClasses.Messages.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;


public class Client {
    ObjectOutputStream msgServer = null;
    ObjectInputStream msgIn = null;
    private int port;
    private String userName;
    private BooleanProperty isLoggedIn = new SimpleBooleanProperty(false);
    private BooleanProperty connected = new SimpleBooleanProperty(false);
    private Socket server = null;
    private UI ui;
    private Listener listener;
    private HashMap<Long, LiteRoom> rooms = new HashMap<>();
    private LiteRoom myRoom = null;

    public Client(int port, UI ui) {
        this.ui = ui;
        this.port = port;
        this.listener = new Listener(ui);
    }

    private void initialiseServer() throws IOException {
        server = new Socket("localhost", port);
        msgIn = new ObjectInputStream(server.getInputStream());
        msgServer = new ObjectOutputStream(server.getOutputStream());
        connected.set(true);
    }

    public void sendMessage(String text) {
        ui.out("Sending a Message");
        try {
            msgServer.writeObject(new Message(userName, text, myRoom.getRoomId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUsername(String userName) {
        this.userName = userName;
        ui.setUsername(userName);
    }

    public boolean isLoggedIn() {
        return isLoggedIn.get();
    }

    public void setLoggedIn(boolean b) {
        isLoggedIn.set(b);
        ui.logOff();
    }

    public void login() {
        try {
            initialiseServer();
            setLoggedIn(internalLogin());
            ui.out("Login attempt was made. Result: " + isLoggedIn);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean internalLogin() {
        boolean success = false;
        String[] loginData;
        String username;
        String password;

        try {

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
                    ui.displayMessage(response);
                    ;
                }
            }
            success = response.isSuccess();
            if (success) {
                setUsername(username);
                listener.start();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return success;
    }

    public void switchRoom(LiteRoom room) {
        try {
            msgServer.writeObject(RoomMessage.setRoomMessage(room));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        isLoggedIn.set(false);
        connected.set(false);

        try {
            msgServer.writeObject(new Message(userName, "logout", -1));
            msgServer.close();
            msgIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listener = new Listener(ui);
        ui.logOff();
    }

    private void terminate() {
        if (connected.get()) {
            ui.out("Unintended Client termination");
        }
        closeConnection();
    }

    private class Listener extends Thread {

        private UI ui;

        public Listener(UI ui) {
            this.ui = ui;

            ui.out("[LISTENER CONSTRUCTION] Listener constructed successfully.");
        }

        @Override
        public void run() {
            //Only works if server is defined by now
            ui.out("[LISTENER] started successfully");
            Message newMessage;
            while (connected.get()) {
                ui.out("Entering reception cycle");
                try {
                    if (msgIn.available() >= 0) {
                        newMessage = (Message) msgIn.readObject();
                        executeOrder(newMessage); // execute Order taken from server
                        ui.out("Message received");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    if (isLoggedIn()) {
                        System.err.println("Server Stream is Faulty. Closing connection.");
                        e.printStackTrace();
                        terminate();

                        break;
                    } else {
                        break;
                    }
                }
            }
            ui.out("Listener stopped");
        }

        //adjust action to given roomID of message-object
        private void executeOrder(Message message) {
            long id = message.getRoomID();
            if (id >= 0)
                ui.displayMessage(message);
            else if (id == -1) {
                ui.out("Connection is being closed");
                try {
                    server.close();
                } catch (IOException e) {
                    terminate();
                    e.printStackTrace();
                }
            } else if (id == -3) {
                RoomMessage roomMessage = (RoomMessage) message;
                LiteRoom newMyRoom = roomMessage.getRoom();
                rooms.put(newMyRoom.getRoomId(), newMyRoom);
                myRoom = newMyRoom;
                ui.setMyRoom(myRoom);
            } else if (id == -4) {
                RoomMessage roomMessage = (RoomMessage) message;
                rooms.put(roomMessage.getRoom().getRoomId(), roomMessage.getRoom());
                ui.roomUpdate(roomMessage.getRoom());
            } else if (id == -5) {
                Long deletedRoomID = Long.valueOf(message.getContent());
                ui.deleteRoom(rooms.get(deletedRoomID));
                rooms.remove(deletedRoomID);
            } else if (id == -6) {
                LiteUser user = ((UserMessage) message).getUser();
                ui.userUpdate(user);
            } else if (id == -9) {
                LiteUser leavingUser = ((UserMessage) message).getUser();
                    ui.out("Removing user " + leavingUser.getName() + " from scope");
                    ui.deleteUser(leavingUser);
                    myRoom.getUsers().remove(leavingUser);
            } else {
                ui.out("Received unknown Order");
            }
        }

    }

}

