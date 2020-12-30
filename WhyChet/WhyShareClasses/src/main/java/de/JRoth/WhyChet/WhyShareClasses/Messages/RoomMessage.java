package de.JRoth.WhyChet.WhyShareClasses.Messages;

public class RoomMessage extends Message{
    private LiteRoom room;

    public LiteRoom getRoom(){
        return room;
    }

    public RoomMessage addRoomMessage(LiteRoom room){
        return new RoomMessage(room, -4);
    }
    private RoomMessage(LiteRoom room, long orderID){ //Addroom-Conform RoomMessage
        super("Server", room.getRoomName(), orderID);
        this.room = room;
    }
    public static RoomMessage setRoomMessage(LiteRoom room){
        return new RoomMessage(room, -3);
    }

    public RoomMessage(String sender, String content, long roomID) {
        super(sender, content, roomID);
    }

}
