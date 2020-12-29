package de.JRoth.WhyChet.WhyShareClasses.Messages;

import java.util.ArrayList;

public class LiteRoom {
    public String getRoomName() {
        return roomName;
    }

    public long getRoomId() {
        return roomId;
    }

    public ArrayList<LiteUser> getUsers() {
        return users;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    private String roomName;
    private long roomId;
    private ArrayList<LiteUser> users;
    private ArrayList<Message> messages;

    public LiteRoom(String roomName, long roomId, ArrayList<LiteUser> users, ArrayList<Message> messages) {
        this.roomName = roomName;
        this.roomId = roomId;
        this.users = users;
        this.messages = messages;
    }
}
