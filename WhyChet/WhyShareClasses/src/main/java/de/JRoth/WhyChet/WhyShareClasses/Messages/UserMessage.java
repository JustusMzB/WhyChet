package de.JRoth.WhyChet.WhyShareClasses.Messages;

public class UserMessage extends Message {
    private LiteUser user;

    public UserMessage(LiteUser user, long orderID) {
        super("[SERVER]", user.getName(), orderID);
        this.user = user;
    }

    public static UserMessage getInfoMessage(LiteUser user){
        return new UserMessage(user, -6);
    }

    public UserMessage(String sender, String content, long roomID) {
        super(sender, content, roomID);
    }

    public LiteUser getUser() {
        return user;
    }
}
