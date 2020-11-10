package sample;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String sender;
    private String content;
    private long roomID;
    Date date;
    String timeOfDeparture;

    public Message(String sender, String content, long roomID){
        this.sender = sender;
        this.content = content;
        this.date = new Date();
        this.roomID = roomID;
        this.timeOfDeparture = (new Anzeige(date).getUhrzeit());
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

    public String getTimeOfDeparture() {
        return timeOfDeparture;
    }

    public String getContent() {
        return content;
    }

    public long getRoomID() {
        return roomID;
    }

}
