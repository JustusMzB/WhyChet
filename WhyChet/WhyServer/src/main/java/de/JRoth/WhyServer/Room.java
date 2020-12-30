package de.JRoth.WhyServer;

import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class Room {
    private final long id;
    private final List<Message> chat = Collections.synchronizedList(new LinkedList<>());
    private final Server server;
    private final String name;
    /*TO DO
     *Users in Room
     */
    private final List<User> members = Collections.synchronizedList(new LinkedList<>());

    public Room(int id, String name, Server server) {
        this.id = id;
        this.name = name;
        this.server = server;
    }

    public List<Message> getChat() {
        return chat;
    }

    public List<User> getMembers() {
        return members;
    }

    public long getId() {
        return id;
    }

    public int size() {
        return chat.size();
    }

    public Message getMessage(int index) {
        return chat.get(index);
    }

    public void addMessage(Message message) {
        if (message.getRoomID() == id) {
            chat.add(message);
            server.displayType().chatMessage(message, id);
        }
    }

    public void addMember(User user) {
        members.add(user);
        server.displayType().memberJoined(this, user);
    }

    public void removeMember(User user) {
        members.remove(user);
        server.displayType().memberLeft(this, user);
    }

    public String getName() {
        return name;
    }
}
