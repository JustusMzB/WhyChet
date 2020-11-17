package sample;

import java.io.Serializable;
/* TO DO
 *Timestamp Implementation

 */

public class Message implements Serializable {
    private final String sender;
    private final long timeOfDeparture = 0;
    private final String content;
    private final long roomID;

    public Message(String sender, String content, long roomID) {
        this.sender = sender;
        this.content = content;
        this.roomID = roomID;
    }

    public String toString() {
        return sender + "\n" + content + "\n Time: " + timeOfDeparture + "\n ID: " + roomID;
    }

    public String displayString() {
        return sender + "; " + timeOfDeparture + "\n" + content;
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
