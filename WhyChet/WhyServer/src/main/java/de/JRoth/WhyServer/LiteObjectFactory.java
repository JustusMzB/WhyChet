package de.JRoth.WhyServer;

import de.JRoth.WhyChet.WhyShareClasses.Messages.LiteRoom;
import de.JRoth.WhyChet.WhyShareClasses.Messages.LiteUser;
import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
import de.JRoth.WhyChet.WhyShareClasses.Messages.RoomMessage;
import de.JRoth.WhyServer.Room;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class LiteObjectFactory {
    public static LiteUser makeLite(User user){
        return new LiteUser(user.getName(), user.isOnline());
    }

    public static List<LiteUser> makeLite(List<User> users){
        ArrayList<LiteUser> retList = new ArrayList<>();
        for (User i : users){
            retList.add(makeLite(i));
        }
        return retList;
    }
    public static LiteRoom makeLite(Room room){
        int subListStart = room.getChat().size()-100;
        if( subListStart < 0){
            subListStart = 0;
        }
        int subListEnd = room.getChat().size()-1;
        if(subListEnd < 0){
            subListEnd = 0;
        }
        List<Message> messageSelection = new ArrayList<>();
        if(room.getChat().size() > 0){
            messageSelection = room.getChat().subList(subListStart, subListEnd);
        }
        //Returns a literoom with the 100 last messages
        return new LiteRoom(room.getName(), room.getId(), makeLite(room.getMembers()), messageSelection);
    }
}
