package sample;

import java.io.Serializable;

public class Message implements Serializable {
    private String sender;
    private long timeOfDeparture;
    private String content;
    private long roomID;

    public Message(String sender, String content, long roomID){
        this.sender = sender;
        this.content = content;
        this.roomID = roomID;
    }

    public String toString(){
        return sender +"\n" + content + "\n Time: " + timeOfDeparture + "\n ID: " + roomID;
    }

    public String displayString(){
        return sender + "; " + timeOfDeparture +"\n" + content;
    }

    public String getSender() {
        return sender;
    }

    public long getTimeOfDeparture() {
        return timeOfDeparture;
    }

    public String getContent() {
        return content;
    }

    public long getRoomID() {
        return roomID;
    }

}
