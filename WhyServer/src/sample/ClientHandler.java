package sample;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientHandler extends Thread {

    private Socket client;
    private Server server;
    private User user;
    private ObjectOutputStream msgOut;
    private ObjectInputStream msgIn;
    private InputHandler inputHandler;
    //Constructor
    public ClientHandler(Socket client, Server server){
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

    }


    //Internal Helper Thread
    private class InputHandler extends Thread{

        private void orderHandling(Message order){
            System.out.println("[CLIENTHANDLER]" + user.getName() + " Received an order");
            switch ((int) order.getRoomID()) {
                case 1:{
                    sendText("Alright. Connection closing...");
                    disconnect();
                    break;
                }
                default: System.err.println("Received unknown order ID: " + order.getRoomID() + " from user " + user.getName());
            }
        } // Sollte mit ner Switch-Anweisung verschiedene Befehl-IDs behandeln

        private void messageHandler(Message message){
            System.out.println("[CLIENTHANDLER]" + user.getName() + " Received a Message");
            Room myRoom;
            int index = 0;
            do {
                myRoom = server.getRooms().get(index);
                index++;
            } while (index < server.getRooms().size() &&  myRoom.getId() != message.getRoomID());
            myRoom.addMessage(message);
        }

        @Override
        public void run() {
            while (true){

                try {
                    Message message = (Message)(msgIn.readObject());
                    if(message.getRoomID() > 0) {
                        messageHandler(message);
                    } else {
                        orderHandling(message);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("[SERVER] [CLIENTHANDLER] "+ this.getId() + " " + user.getName()+"'s InputStream is faulty.");
                    disconnect();
                    break;
                }
            }
        }
    }

    //Login Dialogue
    private void login(){
        String username;
        String password;
        boolean loginSuccess = false;
        boolean correctPW = false;

        while(!loginSuccess) {
            sendText("Type in your Username please. If it is unknown, you will receive a new account.");
            username = receiveText();
            sendText("Type in your password.");
            password = receiveText();
            user = new User(username, password, client);
            // User of that name is already registered.
            if(server.getUsers().containsKey(username)) {
                if (server.getUsers().get(username).hasPassword(password)) {
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
                sendText("Registration Successful.");
            }
        }
        //Login successful
        user.setOnline(true);
        for(Room i : server.getRooms()){
            i.addMessage(new Message("[SERVER]", user.getName() + " just came online", i.getId()));
        }
        sendText("Users online:\n" + onlineUserString());


    }

    //Chat Dialogue
    private void chatService(){
        int messageAmount = server.getRooms().get(0).size();
        inputHandler.start();
        while (!client.isClosed()){
            //EXTRA CAUTION IN FOR LOOP: If sendmessage fails, it extends the chat by 1 through attempting to disconnect
            for(int i = messageAmount; messageAmount < server.getRooms().get(0).size() && client.isConnected(); i++){
                sendMessage(server.getRooms().get(0).getMessage(i));
                messageAmount ++;
            }
        }
        //End of Connection has to be triggered in some interaction process, preferrably orderhandler
    }
    //NO Methods that use this one can be called in here.
    private void disconnect(){
        System.out.println("[SERVER] [CLIENTHANDLER] "+ this.getId() + " Disconnecting User "+user.getName());
        user.setOnline(false);
        inputHandler.interrupt();
        try {
            client.close();
        } catch (IOException e) {
            System.err.println("[SERVER] [CLIENTHANDLER]" + this.getId() + " Issue while closing connection to "+ user.getName());
            e.printStackTrace();
        }
        System.out.println(("[SERVER] [CLIENTHANDLER]" + this.getId()) + " User" + user.getName() + " was logged out. Posting notice in the Chatrooms...");
        for (Room i : server.getRooms()){
            i.addMessage(new Message("[SERVER]", user.getName() + " went offline", i.getId()));
        }
    }

    @Override
    public void run() {
        //Login
        login();
        chatService();
    }


    //Helper Methods
    private String onlineUserString(){
        String result = "";
        for(User i : server.getUsers().values()){
            if (i.isOnline()){
                result += i.getName() + "\n";
            }
        }
        if(result.length() == 0){
            result += "None";
        }
        return result;
    }

    private String receiveText(){
        try {
            return ((Message)msgIn.readObject()).getContent();
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            System.out.println("[SERVER] [CLIENTHANDLER] "+ this.getId() + " Couldn't receive Message from " + user.getName());
            disconnect();
            return "";
        }
    }

    private void sendText(String text){
        Message message = new Message("[SERVER]", text, 1);
        try {
            msgOut.writeObject(message);
        } catch (IOException | NullPointerException e) {
            System.out.println("[SERVER] [CLIENTHANDLER] "+ this.getId() + " Couldn't deliver Message to " + user.getName());
            disconnect();
        }
    }

    private void sendMessage(Message message){
        try {
            msgOut.writeObject(message);
        } catch (IOException e) {
            System.err.println("[SERVER] [CLIENTHANDLER] "+ this.getId() + " Couldn't deliver Message" + message.toString() + " to " + user.getName());
            disconnect();
        }
    }


    //Getters and Setters
    public Socket getClient() {
        return client;
    }
}

/*
Currently in complete super Alpha.
TO DO:
    *Continuous chat dialogue
    *By Checking for new Messages in Room and sending them to Client
 */
