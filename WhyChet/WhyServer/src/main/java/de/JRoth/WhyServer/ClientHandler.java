package de.JRoth.WhyServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;


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
        user = new User("Uninitialized", "Uninitialized", client, server);

    }

    //Login Dialogue
    private void login() {
        String username;
        String password;
        boolean loginSuccess = false;

        while (!loginSuccess) {
            try {
                sendText("Type in your Username please. If it is unknown, you will receive a new account.");
                username = receiveText();
                sendText("Type in your password.");
                password = receiveText();
                user = new User(username, password, client, server);
                // User whose name is already registered.
                if (server.getUsers().containsKey(username)) {
                    //Username is taken
                    if (server.getUsers().get(username).isOnline()) {
                        sendText("The User is already logged in");
                    } else if (server.getUsers().get(username).hasPassword(password)) {
                        sendText("Login Successful.");
                        loginSuccess = true;
                        user = server.getUsers().get(username);
                    } else {
                        sendText("The Username is TAKEN!");
                    }
                    //Username is unknown
                } else {
                    server.getUsers().put(username, user);
                    loginSuccess = true;
                    display.addUser(user);
                    server.getRooms().get(0).addMember(user);
                    sendText("Registration Successful.");
                }
            }catch (IOException | NullPointerException | ClassNotFoundException e) {
                display.errLog("[CLIENTHANDLER] " + client + "Had Exeption during Login");
                e.printStackTrace();
                terminate();
                break;
            }
        }
        //Login successful
        user.logOn(this);
        try {
            sendText("Users Online:\n" + onlineUserString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Chat Dialogue
    private void chatService() {
        int messageAmount = server.getRooms().get(0).size();
        inputHandler.start();
        while (running.get()) {
            //EXTRA CAUTION IN FOR LOOP: If sendmessage fails, it extends the chat by 1 through attempting to disconnect
            for (int i = messageAmount; messageAmount < server.getRooms().get(0).size() && client.isConnected(); i++) {
                try {
                    sendMessage(server.getRooms().get(0).getMessage(i));
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
        chatService();
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

    private void sendMessage(Message message) throws IOException {
            msgOut.writeObject(message);
    }

    //Getters and Setters
    public Socket getClient() {
        return client;
    }

    public User getUser() {
        return user;
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
                        display.errLog("[CLIENTHANDLER] "+ client + " "+ user.getName() + " Connection error during user-induced Logout");
                        e.printStackTrace();
                    }
                    user.logOff();
                    break;
                }
                default:
                    display.errLog("Received unknown order ID: " + order.getRoomID() + " from user " + user.getName());
            }
        } // Sollte mit ner Switch-Anweisung verschiedene Befehl-IDs behandeln

        private void messageHandler(Message message) {
            display.log("[CLIENTHANDLER]" + user.getName() + " Sent in a Message");
            message = new Message(user.getName(),message.getContent(),message.getRoomID());
            Room myRoom;
            int index = 0;
            do {
                myRoom = server.getRooms().get(index);
                index++;
            } while (index < server.getRooms().size() && myRoom.getId() != message.getRoomID());
            myRoom.addMessage(message);
        }

        @Override
        public void run() {
            while (running.get()) {
                try {
                    Message message = (Message) (msgIn.readObject());
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
