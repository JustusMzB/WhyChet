package de.JRoth.WhyServer;

import de.JRoth.WhyChet.WhyShareClasses.Messages.LoginMessage;
import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
import de.JRoth.WhyChet.WhyShareClasses.Messages.RoomMessage;
import de.JRoth.WhyChet.WhyShareClasses.Messages.ServerResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.JRoth.WhyServer.LiteObjectFactory.makeLite;


public class ClientHandler extends Terminateable {

    private final Socket client;
    private final Server server;
    private final InputHandler inputHandler;
    private final DisplayService display;
    private User user;
    private ObjectOutputStream msgOut;
    private ObjectInputStream msgIn;
    private AtomicBoolean running = new AtomicBoolean(false);

    //Constructor
    public ClientHandler(Socket client, Server server) {
        this.client = client;
        this.server = server;
        try {
            this.msgOut = new ObjectOutputStream(client.getOutputStream());
            this.msgIn = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            msgIn = null;
            msgOut = null;
        }
        this.inputHandler = new InputHandler();
        display = server.displayType();
        user = new User("Uninitialized", "Uninitialized".hashCode(), server);

    }

    //Login Dialogue
    private void login() {
        String username = null;
        String password = null;
        LoginMessage login = null;
        Message receivedMessage = null;


        boolean loginSuccess = false;

        while (!loginSuccess) {

            //receiving a proper loginmessage
            do {
                try {
                    receivedMessage = receiveMessage();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    break;
                }
            } while (receivedMessage.getRoomID() != -7);

            try {
                login = (LoginMessage) receivedMessage;
                username = login.getUserName();
                password = login.getPassword();

            } catch (NullPointerException e) {
                e.printStackTrace();
                terminate();
            }


            try {
                user = new User(username, password.hashCode(), server);
                // User whose name is already registered.
                if (server.getUsers().containsKey(username)) {
                    //Username is taken
                    if (server.getUsers().get(username).isOnline()) { //User already online
                        sendMessage(new ServerResponse(false, "User is already online"));
                    } else if (server.getUsers().get(username).hasPassword(password)) {//User offline: Password check
                        loginSuccess = true;
                        user = server.getUsers().get(username);
                    } else {
                        sendMessage(new ServerResponse(false, "Wrong Password"));
                    }
                    //Username is unknown
                } else {
                    server.getUsers().put(username, user);
                    loginSuccess = true;
                    display.addUser(user);
                }
            } catch (IOException | NullPointerException e) {
                display.errLog("[CLIENTHANDLER] " + client + "Had Exeption during Login");
                e.printStackTrace();
                terminate();
                break;
            }
        }
        //Login successful
        //This will still be reached upon exception, creating user Uninitialized
        if (loginSuccess) {
            try {
                sendMessage(new ServerResponse(true, "Login Successful"));
            } catch (IOException e) {
                display.errLog("[CLIENTHANDLER] " + client + "Had Exeption during Login");
                e.printStackTrace();
                terminate();
            }
            user.logOn(this);
            new InputHandler().start();
            try {
                sendText("Users Online:\n" + onlineUserString());
                sendMessage(RoomMessage.setRoomMessage(makeLite(user.getRoom())));
            } catch (IOException e) {
                display.errLog("[CLIENTHANDLER] " + client + "Had Exeption during Login");
                e.printStackTrace();
                terminate();
            }

        }
    }

    //Chat Dialogue
    private void chatService() {
        int messageAmount = user.getRoom().size();
        inputHandler.start();
        while (running.get()) {
            //EXTRA CAUTION IN FOR LOOP: If sendmessage fails, it extends the chat by 1 through attempting to disconnect
            for (int i = messageAmount; messageAmount < user.getRoom().size() && client.isConnected(); i++) {
                try {
                    sendMessage(user.getRoom().getMessage(i));
                } catch (IOException e) {
                    display.errLog("[CLIENTHANDLER] " + client + user.getName() + " Failed to update its Chat");
                    e.printStackTrace();
                    running.set(false);
                    break;
                }
                messageAmount++;
            }
        }
        //End of Connection has to be triggered in some interaction process, preferrably orderhandler
    }

    //NO Methods that use this one can be called in here.
    public void terminate() {
        display.log("[SERVER] [CLIENTHANDLER] " + client + " Terminating Clienthandler for " + user.getName());
        running.set(false);
        try {
            client.close();
        } catch (IOException e) {
            display.errLog("[SERVER] [CLIENTHANDLER]" + this.getId() + " Issue while closing connection to " + user.getName());
            e.printStackTrace();
        }
        display.log(("[SERVER] [CLIENTHANDLER]" + this.getId()) + " Client of " + user.getName() + " was disconnected. Clienthandler will close.");
    }

    @Override
    public void run() {
        running.set(true);
        //Login
        login();
        //chatService();
    }

    //Helper Methods
    private String onlineUserString() {
        String result = "";
        for (User i : server.getUsers().values()) {
            if (i.isOnline()) {
                result += i.getName() + "\n";
            }
        }
        if (result.length() == 0) {
            result += "None";
        }
        return result;
    }

    private String receiveText() throws IOException, ClassNotFoundException {

        return ((Message) msgIn.readObject()).getContent();
    }

    public void sendText(String text) throws IOException {
        Message message = new Message("[SERVER]", text, 1);
        msgOut.writeObject(message);
    }

    void sendMessage(Message message) throws IOException {
        msgOut.writeObject(message);
    }

    private Message receiveMessage() throws IOException, ClassNotFoundException {

        return (Message) msgIn.readObject();

    }

    //Internal Helper Thread
    private class InputHandler extends Thread {

        private void orderHandling(Message order) {
            display.log("[CLIENTHANDLER]" + user.getName() + " Received an order");
            switch ((int) order.getRoomID()) {
                case -1: {
                    try {
                        sendText("Alright. Connection closing...");
                    } catch (IOException e) {
                        display.errLog("[CLIENTHANDLER] " + client + " " + user.getName() + " Connection error during user-induced Logout");
                        e.printStackTrace();
                    }
                    user.logOff();
                    break;
                }
                case -3: {
                    switchRoom(order);
                    break;
                }
                default:
                    display.errLog("Received unknown order ID: " + order.getRoomID() + " from user " + user.getName());
            }
        } // Sollte mit ner Switch-Anweisung verschiedene Befehl-IDs behandeln

        private void messageHandler(Message message) {
            display.log("[CLIENTHANDLER]" + user.getName() + " Sent in a Message:" + message.getContent());
            if (message.getRoomID() == user.getRoom().getId()) {
                user.getRoom().addMessage(message);
            }
        }

        private void switchRoom(Message message) {
            display.log("[CLIENTHANDLER] " + user.getName() + " Requests room switch to " + message.getContent());
            user.getRoom().removeMember(user);
            RoomMessage roomMessage = (RoomMessage) message;
            long requestedId = roomMessage.getRoom().getRoomId();
            for (Room i : server.getRooms()) {
                if (i.getId() == requestedId) {
                    i.addMember(user);
                }
            }
        }

        @Override
        public void run() {
            display.log("[SERVER] [CLIENTHANDLER] " + user.getName() + " Client-Listener started");
            while (running.get()) {
                try {
                    Message message = (Message) (msgIn.readObject());
                    server.displayType().log("[SERVER] [CLIENTHANDLER] " + user.getName() + "Sent something in ");
                    if (message.getRoomID() > 0) {
                        messageHandler(message);
                    } else {
                        orderHandling(message);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    if (running.get()) { // checking wether intentional termination was demanded
                        display.errLog("[SERVER] [CLIENTHANDLER] " + this.getId() + " " + user.getName() + "'s InputStream is faulty.");
                        user.logOff();
                    } else {
                        terminate();
                    }
                    break;
                }
            }
        }
    }
}
