package de.JRoth.WhyChet.WhyShareClasses.Messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LiteRoom implements Serializable {
    public String getRoomName() {
        return roomName;
    }

    public long getRoomId() {
        return roomId;
    }

    public List<LiteUser> getUsers() {
        return users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    private String roomName;
    private long roomId;

    //Using a LinkedList, because the List Interface is not per se serializeable
    private LinkedList<LiteUser> users;
    private LinkedList<Message> messages;

    public LiteRoom(String roomName, long roomId, LinkedList<LiteUser> users, LinkedList<Message> messages) {
        this.roomName = roomName;
        this.roomId = roomId;
        this.users = users;
        this.messages = messages;
    }
}
