package de.JRoth.WhyServer;

import de.JRoth.WhyChet.WhyShareClasses.Messages.LiteUser;
import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
import de.JRoth.WhyChet.WhyShareClasses.Messages.RoomMessage;
import de.JRoth.WhyChet.WhyShareClasses.Messages.UserMessage;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static de.JRoth.WhyServer.LiteObjectFactory.makeLite;


public class Room {
    private final long id;
    private final List<Message> chat = Collections.synchronizedList(new LinkedList<>());
    private final Server server;
    private String name;
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
            for(User i : getMembers()){
                i.sendMessage(message);
            }
            server.displayType().chatMessage(message, id);
        }
    }

    public void addMember(User user) {
        if(user.isOnline()) {
            members.add(user);
            user.setRoom(this);
            RoomMessage roomMessage = RoomMessage.setRoomMessage(LiteObjectFactory.makeLite(this));
            user.sendMessage(roomMessage);

            //Update for other members
            LiteUser liteUser = LiteObjectFactory.makeLite(user);
            for (User i : members){
                i.sendMessage(UserMessage.getInfoMessage(liteUser));
            }

            //UI update
            server.displayType().memberJoined(this, user);
            for (User i : members) {
                i.notify("User " + user.getName() + " Joined the room.");
            }
        }
    }

    public void removeMember(User user) {
        members.remove(user);
        //Up for discussion: Notify the client that the user is not in a room

        //Notification of fellow members
        LiteUser liteUser = LiteObjectFactory.makeLite(user);
        for (User i : members){
            i.sendMessage(UserMessage.getLeaveMessage(liteUser));


            i.notify(user.getName() + " left the room.");
        }

        //UI update
        server.displayType().memberLeft(this, user);
    }

    public String getName() {
        return name;
    }

    //Quick and dirty: Direct hook for the GUI-Roomview without knowing server
    public void rename(String newName){
        server.editRoom(this, newName);
    }

    //Quick and Dirty: Deletion hook for Gui roomview
    public void delete(){
        server.removeRoom(this);
    }
    public void setName(String newName) {
        name = newName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return getId() == room.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
