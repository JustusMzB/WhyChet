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


    public Socket getClient() {
        return client;
    }

    private class InputHandler extends Thread{

        private void orderHandling(Message order){
            System.out.println("[CLIENTHANDLER]" + user.getName() + " Received an order");
            switch ((int) order.getRoomID()){
                case -1:
                    sendText("Alright. Connection closing...");
                    try {
                        client.close();
                    } catch (IOException e) {
                        System.err.println("Couldnt close Connection");
                        e.printStackTrace();
                    }
                    user.setOnline(false);
                    break;

                default:
                    System.err.println("Received unknown order ID: " + order.getRoomID()+ " from user " + user.getName());
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
                    System.err.println("[CLIENTHANDLER] Faulty Instream");
                    try {
                        client.close();
                    } catch (IOException ioException) {
                        System.err.println("[CLIENTHANDLER] Couldnt close client connection.");
                    }
                    break;
                }
            }
        }
    }



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

    }
    private String receiveText(){
        try {
            return ((Message)msgIn.readObject()).getContent();
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            System.out.println("[CLIENTHANDLER] " + client.getInetAddress() + ": Couldnt receive Text");
            user.setOnline(false);
            try {
                client.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return "";
        }
    }
    private void sendText(String text){
        Message message = new Message("[SERVER]", text, 1);
        try {
            msgOut.writeObject(message);
        } catch (IOException | NullPointerException e) {
            System.out.println("[CLIENTHANDLER]"+ client.getInetAddress() + ": Couldnt send message" + message);
            user.setOnline(false);
            try {
                client.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    private void sendMessage(Message message){
        try {
            msgOut.writeObject(message);
        } catch (IOException e) {
            System.err.println("[CLIENTHANDLER] "+ client.getInetAddress() + " failed to send Message" + message.toString());
            user.setOnline(false);
            try {
                client.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private void login(){
        String username;
        String password;
        boolean loginSuccess = false;

        while(!loginSuccess) {
            sendText("Type in your Username please. If it is unknown, you will receive a new account.");
            username = receiveText();
            sendText("Type in your password.");
            password = receiveText();
            user = new User(username, password, client);
            if(server.getUsers().contains(user)){
                sendText("Username is taken, and not with that password.");
            } else {
                user.setOnline(true);
                server.getUsers().add(user);
                loginSuccess = true;
                sendText("Login Successful. You are now online.\n" + "Users Online:\n"+server.onlineUserString());
            }
        }


    }

    @Override
    public void run() {
        //Login
        login();

        //Test Block Interaction
        int messageAmount = server.getRooms().get(0).size();
        InputHandler inputHandler = new InputHandler();
        inputHandler.start();
        while (!client.isClosed()){
                for(int i = messageAmount; messageAmount < server.getRooms().get(0).size(); i++){
                    sendMessage(server.getRooms().get(0).getMessage(i));
                    messageAmount ++;
                }
        }


        //End of Connection has to be triggered in some interaction process, preferrably orderhandler


    }
}

/*
Currently in complete super Alpha.
TO DO:
    *Continuous chat dialogue
    *By Checking for new Messages in Room and sending them to Client
 */
