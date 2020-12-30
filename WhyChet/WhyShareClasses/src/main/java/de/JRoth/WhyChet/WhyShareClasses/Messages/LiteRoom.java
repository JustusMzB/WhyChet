package de.JRoth.WhyChet.WhyShareClasses.Messages;

import java.util.ArrayList;
import java.util.List;

public class LiteRoom {
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
    private List<LiteUser> users;
    private List<Message> messages;

    public LiteRoom(String roomName, long roomId, List<LiteUser> users, List<Message> messages) {
        this.roomName = roomName;
        this.roomId = roomId;
        this.users = users;
        this.messages = messages;
    }
}
