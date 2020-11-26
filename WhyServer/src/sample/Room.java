package sample;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Room {
    private final long id;
    private final List<Message> chat = Collections.synchronizedList(new LinkedList<Message>());
    /*TO DO
     *Users in Room
     */
    private final List<String> members = Collections.synchronizedList(new LinkedList<String>());

    public Room(int id) {
        this.id = id;
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
        }
    }

    public void addMember(String name) {
        members.add(name);
    }

    public void removeMember(String name) {
        members.remove(name);
    }
}
