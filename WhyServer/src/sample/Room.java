package sample;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Room {
    public long getId() {
        return id;
    }

    public int size(){
        return chat.size();
    }
    public Message getMessage(int index){
        return chat.get(index);
    }

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
}
