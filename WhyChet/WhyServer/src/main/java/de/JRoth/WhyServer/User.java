package de.JRoth.WhyServer;

import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;

public class User {
    private String name;
    private int passHash;
    private ClientHandler handler;
    private BooleanProperty isOnline = new SimpleBooleanProperty(false);
    private Server server;

    public Room getRoom() {
        return myRoom;
    }

    public void setRoom(Room myRoom) {
        this.myRoom = myRoom;
    }

    private Room myRoom;

    public User(Server server) {
        this.server = server;
    }

    public User(String name, int passHash, Server server) {
        this.name = name;
        this.passHash = passHash;
        this.server = server;
        this.isOnline.set(false);
    }
    public void notify(String note){
        if (isOnline()) {
            try {
                handler.sendText(note);
                server.displayType().log("[CLIENTHANDLER] " + name + " was notified: \"" + note + "\"");
            } catch (IOException e) {
                server.displayType().errLog("[User] " + name + ": Could not send notification " + note);
                e.printStackTrace();
            }
        }
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
        if(isOnline()) {
            server.displayType().log("[USER] " + name + " is being logged off");
            isOnline.set(false);

            //Makes sure that the user is not in any room anymore
            if (myRoom != null) {
                myRoom.removeMember(this);
                myRoom = null;
            }
            if (handler != null) {
                handler.terminate();
                this.handler = null;
            }
            for (User i : server.getUsers().values()) {
                if (i.isOnline()) {
                    i.notify(name + " went offline");
                }
            }
        }
    }

    void sendMessage(Message message) {
        if (handler != null){
            try {
                handler.sendMessage(message);
            } catch (IOException e) {
                server.displayType().errLog("[USER]" + name + " Couldn't receive Message");
                e.printStackTrace();
            }
        }
    }

    public void logOn(ClientHandler handler) {
        isOnline.set(true);

        //Puts the user in Global
        this.handler = handler;
        myRoom = server.getRooms().get(0);
        myRoom.addMember(this);

        //Sends online notification
        for(User i : server.getUsers().values()){
            if (! (i == this)){
                i.notify(name + " just came online.");
            }
        }
    }

    public void setPassword(String password) {
        this.passHash = password.hashCode();
    }
    public BooleanProperty getOnlineProperty(){
        return isOnline;
    }


    public boolean hasPassword(String pw) {
        return passHash== pw.hashCode();
    }
    public int getPassHash(){
        return passHash;
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
