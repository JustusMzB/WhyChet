package de.JRoth.WhyChet.WhyShareClasses.Messages;

public class ServerResponse extends Message{
    public boolean isSuccess() {
        return success;
    }

    private boolean success = false;

    public ServerResponse(String sender, String content, long roomID) {
        super(sender, content, roomID);
    }

    public ServerResponse(boolean success, String content){
        super("[SERVER]", content, -8);
        this.success = success;
    }
}
