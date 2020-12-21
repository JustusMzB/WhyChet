package de.JRoth.WhyServer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.net.Socket;

public class User {
    private Socket socket;
    private String name;
    private String password;
    private ClientHandler handler;
    private BooleanProperty isOnline = new SimpleBooleanProperty(false);
    private Server server;

    //public sample.Room& room;
    public User(Socket socket, Server server) {
        this.socket = socket;
        this.server =server;
    }

    public User(String name, String password, Socket socket, Server server) {
        this.name = name;
        this.password = password;
        this.socket = socket;
        this.server = server;
    }

    public Socket getSocket() {
        return socket;
    }


    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnline() {
        return isOnline.get();
    }

    public void logOff(){
        server.displayType().log("[USER] "+ name + " is being logged off");
        isOnline.set(false);
        if(handler != null) {
            handler.terminate();
            this.handler = null;
        }
        for (User i : server.getUsers().values()){
            if(i.isOnline()){
                i.receiveMessage(name + " went offline");
            }
        }

    }

    private void receiveMessage(String s) {
        if (handler != null){
            try {
                handler.sendText(s);
            } catch (IOException e) {
                server.displayType().errLog("[USER]" + name + " Couldn't receive Message");
                e.printStackTrace();
            }
        }
    }

    public void logOn(ClientHandler handler) {
        isOnline.set(true);
        this.handler = handler;
        for(User i : server.getUsers().values()){
            if (!i.equals(this)){
                i.receiveMessage(name + " just came online.");
            }
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public BooleanProperty getOnlineProperty(){
        return isOnline;
    }

    public boolean hasPassword(String pw) {
        return password.equals(pw);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {
            String otherName = ((User) obj).getName();
            if (otherName != null) {
                return otherName.equals(this.getName());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
