package de.JRoth.WhyChet.WhyShareClasses.Messages;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Message implements Serializable {
    private final String sender;
    private final LocalDateTime timeOfDeparture;
    private final String content;
    private final long roomID;


    public Message(String sender, String content, long roomID) {
        this.sender = sender;
        this.content = content;
        this.roomID = roomID;
        timeOfDeparture = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "sender: " + sender + "\n content:" + content + "\n Time: " + timeOfDeparture + "\n ID: " + roomID;
    }

    public String displayString() {
        String dT= timeOfDeparture.toString();
        //TO DO: implement serializable class, since this is rather ugly
        return "["+sender + "; " + dT.substring(8,10)+"."+dT.substring(5,7)+"."+dT.substring(0,4)+" "+dT.substring(11,19)+"]" + "\n" + content;
    }

    public String getSender() {
        return sender;
    }

    public LocalDateTime getTimeOfDeparture() {
        return timeOfDeparture;
    }

    public String getContent() {
        return content;
    }

    public long getRoomID() {
        return roomID;
    }

}
