package de.JRoth.WhyChet.WhyShareClasses.Messages;

public class RoomMessage extends Message{
    private LiteRoom room;

    public LiteRoom getRoom(){
        return room;
    }

    public RoomMessage(LiteRoom room){ //Addroom-Conform RoomMessage
        super("Server", room.getRoomName(), -4);
        this.room = room;
    }

    public RoomMessage(String sender, String content, long roomID) {
        super(sender, content, roomID);
    }

}
