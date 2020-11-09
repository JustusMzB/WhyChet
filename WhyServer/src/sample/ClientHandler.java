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

    /*private class InputHandler extends Thread{
        @Override

        private void orderHandling(Message order){} // Sollte mit ner Switch-Anweisung verschiedene Befehl-IDs behandeln
        public void run() {
            while (true){

                try {
                    Message message = (Message)(msgIn.readObject());
                    if(message.getRoomID() > 0) {
                        server.getRooms().get(0).addMessage(message);
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
    }*/



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
        try {
            //Login
            login();

            //Test Block Interaction
            String messageText;
            while (!client.isClosed()){
                messageText = receiveText();
                if(messageText.contains("/close")){
                    sendText("Alright. Connection closing...");
                    break;
                }
                else sendText("You said: " + messageText);
            }


            //End of Connection
            client.close();
            user.setOnline(false);

        } catch (IOException e) {
            System.err.println("[CLIENTHANDLER] " + currentThread() + " Crashed.");
            e.printStackTrace();
            try {
                client.close();
                user.setOnline(false);
            } catch (IOException ioException) {
                System.err.println("Client connection couldnt be closed");
                ioException.printStackTrace();
            }
        }
    }
}

/*
Currently in complete super Alpha.
TO DO:
    *Continuous chat dialogue
    *By Checking for new Messages in Room and sending them to Client
 */
