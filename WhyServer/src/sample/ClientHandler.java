package sample;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
    public Socket getClient() {
        return client;
    }

    private Socket client;
    private Server server;
    public ClientHandler(Socket client, Server server){
        this.client = client;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream clientMessager = new ObjectOutputStream(client.getOutputStream());
            Message LoginMessage = new Message("[SERVER]", server.onlineUserString(), 1);
            clientMessager.writeObject(LoginMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/*
Currently in complete super Alpha.
TO DO:
    *Login Dialogue with User Addition
    *Continuous chat dialogue
    *By Checking for new Messages in Room and sending them to Client
 */
