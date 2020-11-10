package sample;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Room {
    private long id;
    private List<Message> chat = Collections.synchronizedList(new LinkedList<Message>());
    public Room(int id){
        this.id = id;
    }
    public void addMessage(Message message){
        if(message.getRoomID() == id){
            chat.add(message);
        }
    }
/*TO DO
    *Users in Room
 */
    private List<String> members = Collections.synchronizedList(new LinkedList<String>());
    public void addMember(String name){
        members.add(name);
    }

    public void removeMember(String name){
        members.remove(name);
    }
}
